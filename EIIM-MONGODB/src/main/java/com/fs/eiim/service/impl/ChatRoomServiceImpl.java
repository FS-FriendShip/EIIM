package com.fs.eiim.service.impl;

import com.fs.eiim.dal.entity.*;
import com.fs.eiim.error.UserInterfaceEiimErrorException;
import com.fs.eiim.service.ChatRoomService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.StringUtils;
import org.mx.comps.rbac.error.UserInterfaceRbacErrorException;
import org.mx.dal.EntityFactory;
import org.mx.dal.service.GeneralAccessor;
import org.mx.dal.service.GeneralDictAccessor;
import org.mx.error.UserInterfaceSystemErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#getAllChatRoomsByAccount(String)
     */
    @Override
    public List<ChatRoom> getAllChatRoomsByAccount(String accountId) {
        if (StringUtils.isBlank(accountId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The account's id is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        List<ChatRoom> chatRooms = accessor.list(ChatRoom.class);
        return chatRooms.stream().filter(chatRoom -> {
            Set<ChatRoomMember> members = chatRoom.getMembers();
            if (members != null && !members.isEmpty()) {
                for (ChatRoomMember member : members) {
                    Account account = member.getAccount();
                    if (account != null && accountId.equals(account.getId())) {
                        return true;
                    }
                }
            }
            return false;
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
                    logger.error(String.format("The account[%s] not found.", creator));
                }
                throw new UserInterfaceRbacErrorException(
                        UserInterfaceRbacErrorException.RbacErrors.ACCOUNT_NOT_FOUND
                );
            }
            chatRoom.setCreator(creator);
        }
        return accessor.save(chatRoom);
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#deleteChatRoom(String, String)
     */
    @Override
    public void deleteChatRoom(String chatRoomId, String accountId) {
        if (StringUtils.isBlank(chatRoomId) || StringUtils.isBlank(accountId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The chat room's id or account's id is blank.");
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
                if (account != null && accountId.equals(account.getId())) {
                    member.setValid(false);
                    accessor.save(chatRoom);
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Delete the chat room successfully, chat room: %s, account: %s.",
                                chatRoomId, accountId));
                    }
                    return;
                }
            }
        }
        if (logger.isWarnEnabled()) {
            logger.warn(String.format("Delete the chat room fail, chat room: %s, account: %s.",
                    chatRoomId, accountId));
        }
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
                    accessor.save(chatRoom);
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Top the chat room successfully, chat room: %s, account: %s.",
                                chatRoomId, accountCode));
                    }
                    return member;
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
                    accessor.save(chatRoom);
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Change the chat room's status successfully, chat room: %s, account: %s.",
                                chatRoomId, accountCode));
                    }
                    return member;
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
     * @see ChatRoomService#getAllUnreadMessage(String, String)
     */
    @Override
    public List<ChatMessage> getAllUnreadMessage(String chatRoomId, String accountId) {
        if (StringUtils.isBlank(chatRoomId) || StringUtils.isBlank(accountId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The chat room's id or account's id is blank.");
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
        Account account = accessor.getById(accountId, Account.class);
        if (account == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The account[%s] not found.", accountId));
            }
            throw new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.ACCOUNT_NOT_FOUND
            );
        }
        long lastAccessTime = 0;
        if (chatRoom.getMembers() != null && !chatRoom.getMembers().isEmpty()) {
            for (ChatRoomMember member : chatRoom.getMembers()) {
                if (member.getAccount() != null && accountId.equals(member.getAccount().getId())) {
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
        accessor.save(chatNotice);
    }
}
