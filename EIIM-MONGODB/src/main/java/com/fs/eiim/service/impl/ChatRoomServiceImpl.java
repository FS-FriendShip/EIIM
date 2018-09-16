package com.fs.eiim.service.impl;

import com.fs.eiim.dal.entity.*;
import com.fs.eiim.error.UserInterfaceEiimErrorException;
import com.fs.eiim.service.ChatRoomService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.StringUtils;
import org.mx.comps.rbac.error.UserInterfaceRbacErrorException;
import org.mx.dal.EntityFactory;
import org.mx.dal.Pagination;
import org.mx.dal.service.GeneralAccessor;
import org.mx.dal.service.GeneralDictAccessor;
import org.mx.error.UserInterfaceSystemErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 描述： 聊天室服务实现
 *
 * @author john peng
 * Date time 2018/8/16 下午4:45
 */
@Component("chatRoomService")
public class ChatRoomServiceImpl implements ChatRoomService {
    private static final Log logger = LogFactory.getLog(ChatRoomServiceImpl.class);

    private GeneralDictAccessor accessor;

    /**
     * 构造函数
     *
     * @param accessor 字典数据访问接口
     */
    @Autowired
    public ChatRoomServiceImpl(@Qualifier("generalDictAccessorMongodb") GeneralDictAccessor accessor) {
        super();
        this.accessor = accessor;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#getAllChatRooms()
     */
    @Override
    public List<ChatRoom> getAllChatRooms() {
        return accessor.list(ChatRoom.class);
    }

    private boolean hasMember(ChatRoom chatRoom, String accountCode) {
        Set<ChatRoomMember> members = chatRoom.getMembers();
        if (members != null && !members.isEmpty()) {
            for (ChatRoomMember member : members) {
                Account account = member.getAccount();
                if (account != null && accountCode.equals(account.getCode())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#getAllChatRoomsByAccount(String)
     */
    @Override
    public List<ChatRoom> getAllChatRoomsByAccount(String accountCode) {
        if (StringUtils.isBlank(accountCode)) {
            if (logger.isErrorEnabled()) {
                logger.error("The account's code is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        List<ChatRoom> chatRooms = accessor.list(ChatRoom.class);
        return chatRooms.stream().filter(chatRoom -> hasMember(chatRoom, accountCode)).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#getAllChatRoomsByAccount(String, List)
     */
    @Override
    public List<ChatRoomSummary> getAllChatRoomsByAccount(String accountCode, List<ChatRoomSummaryRequest> summaryRequests) {
        if (StringUtils.isBlank(accountCode)) {
            if (logger.isErrorEnabled()) {
                logger.error("The account's code is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        Map<String, ChatRoomSummaryRequest> map = new HashMap<>();
        if (summaryRequests != null && !summaryRequests.isEmpty()) {
            summaryRequests.forEach(request -> map.put(request.getChatRoomId(), request));
        }
        List<ChatRoom> chatRooms = accessor.list(ChatRoom.class);
        return chatRooms.stream().filter(chatRoom -> hasMember(chatRoom, accountCode)).map(chatRoom -> {
            int unread = 0;
            ChatMessage lastMessage = null;
            String chatRoomId = chatRoom.getId();
            if (map.containsKey(chatRoomId)) {
                ChatRoomSummaryRequest request = map.get(chatRoomId);
                Direction direction = request.getDirection();
                if (direction == Direction.FORWARD) {
                    // 需要取未读消息
                    String messageId = request.getLastMessageId();
                    ChatMessage chatMessage = accessor.getById(messageId, ChatMessage.class);
                    long beginTime = 0;
                    if (chatMessage != null) {
                        beginTime = chatMessage.getSentTime() + 1;
                    }
                    List<ChatMessage> list = accessor.find(null, GeneralAccessor.ConditionGroup.and(
                            GeneralAccessor.ConditionTuple.gte("sentTime", beginTime),
                            GeneralAccessor.ConditionTuple.eq("chatRoom", chatRoom)
                    ), GeneralAccessor.RecordOrderGroup.group(
                            GeneralAccessor.RecordOrder.desc("sentTime")
                    ), ChatMessage.class);
                    unread = list.size();
                    if (unread > 0) {
                        lastMessage = list.get(0);
                    }
                }
            }
            return new ChatRoomSummary(chatRoom, unread, lastMessage);
        }).collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#saveChatRoom(String, String, List, List, String)
     */
    @Override
    public ChatRoom saveChatRoom(String chatRoomId, String chatRoomName, List<String> addAccountCodes,
                                 List<String> delAccountCodes, String creatorCode) {
        ChatRoom chatRoom = null;
        if (!StringUtils.isBlank(chatRoomId)) {
            chatRoom = accessor.getById(chatRoomId, ChatRoom.class);
        }
        if (chatRoom == null) {
            chatRoom = EntityFactory.createEntity(ChatRoom.class);
        }
        if (!StringUtils.isBlank(chatRoomName)) {
            chatRoom.setName(chatRoomName);
        }
        if (chatRoom.getMembers() != null && !chatRoom.getMembers().isEmpty() && delAccountCodes != null) {
            for (String accountCode : delAccountCodes) {
                chatRoom.getMembers().removeIf(member -> {
                    Account account = member.getAccount();
                    return account != null && accountCode.equals(account.getCode());
                });
            }
        }
        if (addAccountCodes != null) {
            for (String accountCode : addAccountCodes) {
                Account account = accessor.getByCode(accountCode, Account.class);
                if (account == null) {
                    if (logger.isErrorEnabled()) {
                        logger.error(String.format("The account[%s] not found.", accountCode));
                    }
                    throw new UserInterfaceRbacErrorException(
                            UserInterfaceRbacErrorException.RbacErrors.ACCOUNT_NOT_FOUND
                    );
                }
                ChatRoomMember member = new ChatRoomEntity.ChatRoomMemberEntity();
                member.setAccount(account);
                chatRoom.getMembers().add(member);
            }
        }
        if (!StringUtils.isBlank(creatorCode)) {
            Account creator = accessor.getByCode(creatorCode, Account.class);
            if (creator == null) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("The account[%s] not found.", creatorCode));
                }
                throw new UserInterfaceRbacErrorException(
                        UserInterfaceRbacErrorException.RbacErrors.ACCOUNT_NOT_FOUND
                );
            }
            chatRoom.setCreator(creator);
        }
        try {
            return accessor.save(chatRoom);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Save chat room info fail.", ex);
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.CHATROOM_SAVE_FAIL
            );
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#deleteChatRoom(String, String)
     */
    @Override
    public void deleteChatRoom(String chatRoomId, String accountCode) {
        if (StringUtils.isBlank(chatRoomId) || StringUtils.isBlank(accountCode)) {
            if (logger.isErrorEnabled()) {
                logger.error("The chat room's id or account's code is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        ChatRoom chatRoom = accessor.getById(chatRoomId, ChatRoom.class);
        if (chatRoom == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The chat room[%s] not found.", chatRoomId));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.CHATROOM_NOT_FOUND
            );
        }
        if (chatRoom.getMembers() != null && !chatRoom.getMembers().isEmpty()) {
            for (ChatRoomMember member : chatRoom.getMembers()) {
                Account account = member.getAccount();
                if (account != null && accountCode.equals(account.getCode())) {
                    member.setValid(false);
                    try {
                        accessor.save(chatRoom);
                        if (logger.isDebugEnabled()) {
                            logger.debug(String.format("Delete the chat room successfully, chat room: %s, account: %s.",
                                    chatRoomId, accountCode));
                        }
                        return;
                    } catch (Exception ex) {
                        if (logger.isErrorEnabled()) {
                            logger.error("Delete the chat room fail.", ex);
                        }
                        throw new UserInterfaceEiimErrorException(
                                UserInterfaceEiimErrorException.EiimErrors.CHATROOM_DELETE_FAIL
                        );
                    }
                }
            }
        }
        if (logger.isWarnEnabled()) {
            logger.warn(String.format("Delete the chat room fail, chat room: %s, account: %s.",
                    chatRoomId, accountCode));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#getMessageSender(String)
     */
    @Override
    public Account getMessageSender(String accountCode) {
        return accessor.getByCode(accountCode, Account.class);
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#getChatRoomMembers(String)
     */
    @Override
    public List<ChatRoomMember> getChatRoomMembers(String chatRoomId) {
        if (StringUtils.isBlank(chatRoomId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The chat room's id is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        ChatRoom chatRoom = accessor.getById(chatRoomId, ChatRoom.class);
        if (chatRoom == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The chat room[%s] not found.", chatRoomId));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.CHATROOM_NOT_FOUND
            );
        }
        return new ArrayList<>(chatRoom.getMembers());
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#topChatRoom(String, String, boolean)
     */
    @Override
    public ChatRoomMember topChatRoom(String chatRoomId, String accountCode, boolean isTop) {
        if (StringUtils.isBlank(chatRoomId) || StringUtils.isBlank(accountCode)) {
            if (logger.isErrorEnabled()) {
                logger.error("The chat room's id or account's code is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        ChatRoom chatRoom = accessor.getById(chatRoomId, ChatRoom.class);
        if (chatRoom == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The chat room[%s] not found.", chatRoomId));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.CHATROOM_NOT_FOUND
            );
        }
        if (chatRoom.getMembers() != null && !chatRoom.getMembers().isEmpty()) {
            for (ChatRoomMember member : chatRoom.getMembers()) {
                Account account = member.getAccount();
                if (account != null && accountCode.equals(account.getCode())) {
                    member.setTop(isTop);
                    try {
                        accessor.save(chatRoom);
                        if (logger.isDebugEnabled()) {
                            logger.debug(String.format("Top the chat room successfully, chat room: %s, account: %s.",
                                    chatRoomId, accountCode));
                        }
                        return member;
                    } catch (Exception ex) {
                        if (logger.isErrorEnabled()) {
                            logger.error("Top the chat room fail.", ex);
                        }
                        throw new UserInterfaceEiimErrorException(
                                UserInterfaceEiimErrorException.EiimErrors.CHATROOM_TOP_FAIL
                        );
                    }
                }
            }
        }
        if (logger.isWarnEnabled()) {
            logger.warn(String.format("Top the chat room fail, chat room: %s, account: %s.",
                    chatRoomId, accountCode));
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#changeMemberStatus(String, String, String)
     */
    @Override
    public ChatRoomMember changeMemberStatus(String chatRoomId, String accountCode, String status) {
        if (StringUtils.isBlank(chatRoomId) || StringUtils.isBlank(accountCode)) {
            if (logger.isErrorEnabled()) {
                logger.error("The chat room's id or account's code is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        ChatRoom chatRoom = accessor.getById(chatRoomId, ChatRoom.class);
        if (chatRoom == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The chat room[%s] not found.", chatRoomId));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.CHATROOM_NOT_FOUND
            );
        }
        if (chatRoom.getMembers() != null && !chatRoom.getMembers().isEmpty()) {
            for (ChatRoomMember member : chatRoom.getMembers()) {
                Account account = member.getAccount();
                if (account != null && accountCode.equals(account.getCode())) {
                    member.setStatus(status);
                    try {
                        accessor.save(chatRoom);
                        if (logger.isDebugEnabled()) {
                            logger.debug(String.format("Change the chat room's status successfully, chat room: %s, account: %s.",
                                    chatRoomId, accountCode));
                        }
                        return member;
                    } catch (Exception ex) {
                        if (logger.isErrorEnabled()) {
                            logger.error("Save chat member's state fail.", ex);
                        }
                        throw new UserInterfaceEiimErrorException(
                                UserInterfaceEiimErrorException.EiimErrors.CHATROOM_MEMBER_STATE_SAVE_FAIL
                        );
                    }
                }
            }
        }
        if (logger.isWarnEnabled()) {
            logger.warn(String.format("Change the chat room's status fail, chat room: %s, account: %s.",
                    chatRoomId, accountCode));
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#getAllUnreadMessages(String, String)
     */
    @Override
    public List<ChatMessage> getAllUnreadMessages(String chatRoomId, String accountCode) {
        if (StringUtils.isBlank(chatRoomId) || StringUtils.isBlank(accountCode)) {
            if (logger.isErrorEnabled()) {
                logger.error("The chat room's id or account's code is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        ChatRoom chatRoom = accessor.getById(chatRoomId, ChatRoom.class);
        if (chatRoom == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The chat room[%s] not found.", chatRoomId));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.CHATROOM_NOT_FOUND
            );
        }
        Account account = accessor.getByCode(accountCode, Account.class);
        if (account == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The account[%s] not found.", accountCode));
            }
            throw new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.ACCOUNT_NOT_FOUND
            );
        }
        long lastAccessTime = 0;
        if (chatRoom.getMembers() != null && !chatRoom.getMembers().isEmpty()) {
            for (ChatRoomMember member : chatRoom.getMembers()) {
                if (member.getAccount() != null && accountCode.equals(member.getAccount().getCode())) {
                    lastAccessTime = member.getLastAccessTime();
                }
            }
        }
        return accessor.find(GeneralAccessor.ConditionGroup.and(
                GeneralAccessor.ConditionTuple.eq("chatRoom", chatRoom),
                GeneralAccessor.ConditionTuple.gte("sentTime", lastAccessTime)
        ), ChatMessage.class);
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#getAllUnreadMessages(String)
     */
    @Override
    public List<ChatRoomMessageTuple> getAllUnreadMessages(String accountCode) {
        if (StringUtils.isBlank(accountCode)) {
            if (logger.isErrorEnabled()) {
                logger.error("The account's code is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        List<ChatRoom> chatRooms = getAllChatRoomsByAccount(accountCode);
        List<ChatRoomMessageTuple> tuples = new ArrayList<>();
        if (chatRooms != null && !chatRooms.isEmpty()) {
            chatRooms.forEach(chatRoom -> {
                List<ChatMessage> messages = getAllUnreadMessages(chatRoom.getId(), accountCode);
                tuples.add(new ChatRoomMessageTuple(chatRoom, messages));
            });
        }
        return tuples;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#getMessagesByRequest(String, String, String, Direction, Pagination)
     */
    @Override
    public List<ChatMessage> getMessagesByRequest(String chatRoomId, String accountCode, String lastMessageId,
                                                  Direction direction, Pagination pagination) {
        if (StringUtils.isBlank(chatRoomId) || StringUtils.isBlank(accountCode)) {
            if (logger.isErrorEnabled()) {
                logger.error("The chat room's id or the account's code is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        ChatRoom chatRoom = accessor.getById(chatRoomId, ChatRoom.class);
        if (chatRoom == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The chat room[%s] not found.", chatRoomId));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.CHATROOM_NOT_FOUND
            );
        }
        if (!hasMember(chatRoom, accountCode)) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The account[%s] not in the chat room[%s].", accountCode, chatRoomId));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.CHATROOM_MEMBER_NOT_FOUND
            );
        }
        long beginTime = -1;
        if (!StringUtils.isBlank(lastMessageId)) {
            ChatMessage chatMessage = accessor.getById(lastMessageId, ChatMessage.class);
            if (chatMessage != null) {
                beginTime = chatMessage.getSentTime() + 1;
            }
        }
        GeneralAccessor.ConditionGroup group;
        if (direction == Direction.FORWARD) {
            // 新消息
            group = GeneralAccessor.ConditionGroup.and(
                    GeneralAccessor.ConditionTuple.eq("chatRoom", chatRoom),
                    GeneralAccessor.ConditionTuple.gte("sentTime", beginTime)
            );
        } else {
            // 旧消息
            group = GeneralAccessor.ConditionGroup.and(
                    GeneralAccessor.ConditionTuple.eq("chatRoom", chatRoom),
                    GeneralAccessor.ConditionTuple.lte("sentTime", beginTime)
            );
        }
        return accessor.find(pagination, group, GeneralAccessor.RecordOrderGroup.group(
                GeneralAccessor.RecordOrder.asc("sentTime")
        ), ChatMessage.class);
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#saveChatMessage(String, String, String, String, String)
     */
    @Override
    public ChatRoom saveChatMessage(String accoutnCode, String eiimCode, String chatRoomId, String messageType,
                                    String message) {
        if (StringUtils.isBlank(accoutnCode) || StringUtils.isBlank(chatRoomId) || StringUtils.isBlank(messageType) ||
                StringUtils.isBlank(message)) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("Any parameter invalid, account code: %s, chat room id: %s, " +
                        "message type: %s, message: %s.", accoutnCode, chatRoomId, messageType, message));
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        Account sender = accessor.getByCode(accoutnCode, Account.class);
        if (sender == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The account[%s] not found.", accoutnCode));
            }
            throw new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.ACCOUNT_NOT_FOUND
            );
        }
        ChatRoom chatRoom = accessor.getById(chatRoomId, ChatRoom.class);
        if (chatRoom == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The chat room[%s] not found.", chatRoomId));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.CHATROOM_NOT_FOUND
            );
        }
        ChatMessage chatMessage = EntityFactory.createEntity(ChatMessage.class);
        chatMessage.setChatRoom(chatRoom);
        chatMessage.setMessage(message);
        chatMessage.setMessageType(messageType);
        chatMessage.setSender(sender);
        chatMessage.setSentTime(System.currentTimeMillis());
        try {
            accessor.save(chatMessage);
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Save a chat message successfully, chat room id: %s, account code: %s," +
                        "message type: %s, message: %s.", chatRoomId, accoutnCode, messageType, message));
            }
            return chatRoom;
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Save chat message fail.", ex);
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.CHATROOM_MESSAGE_SAVE_FAIL
            );
        }
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#getAllChatRoomNotices(String)
     */
    @Override
    public List<ChatNotice> getAllChatRoomNotices(String chatRoomId) {
        if (StringUtils.isBlank(chatRoomId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The chat room's id is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        ChatRoom chatRoom = accessor.getById(chatRoomId, ChatRoom.class);
        if (chatRoom == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The chat room[%s] not found.", chatRoomId));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.CHATROOM_NOT_FOUND
            );
        }
        return accessor.find(GeneralAccessor.ConditionGroup.and(
                GeneralAccessor.ConditionTuple.eq("chatRoom", chatRoom),
                GeneralAccessor.ConditionTuple.eq("valid", true)
        ), ChatNotice.class);
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#saveChatRoomNotice(String, String, String, String)
     */
    @Override
    public void saveChatRoomNotice(String chatRoomId, String noticeId, String noticeType, String notice) {
        if (StringUtils.isBlank(chatRoomId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The chat room's id is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        ChatRoom chatRoom = accessor.getById(chatRoomId, ChatRoom.class);
        if (chatRoom == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The chat room[%s] not found.", chatRoomId));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.CHATROOM_NOT_FOUND
            );
        }
        ChatNotice chatNotice = null;
        if (!StringUtils.isBlank(noticeId)) {
            chatNotice = accessor.getById(noticeId, ChatNotice.class);
        }
        if (chatNotice == null) {
            chatNotice = EntityFactory.createEntity(ChatNotice.class);
        }
        chatNotice.setNoticeType(noticeType);
        chatNotice.setNotice(notice);
        chatNotice.setChatRoom(chatRoom);
        chatNotice.setPushTime(System.currentTimeMillis());
        try {
            accessor.save(chatNotice);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Save chat room's notice fail.", ex);
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.CHATROOM_NOTICE_SAVE_FAIL
            );
        }
    }
}
