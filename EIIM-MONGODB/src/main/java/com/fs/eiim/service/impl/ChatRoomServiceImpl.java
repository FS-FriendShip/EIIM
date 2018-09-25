package com.fs.eiim.service.impl;

import com.fs.eiim.dal.entity.*;
import com.fs.eiim.error.UserInterfaceEiimErrorException;
import com.fs.eiim.service.ChatRoomService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.mx.StringUtils;
import org.mx.comps.rbac.error.UserInterfaceRbacErrorException;
import org.mx.dal.EntityFactory;
import org.mx.dal.Pagination;
import org.mx.dal.service.GeneralAccessor;
import org.mx.dal.service.GeneralDictAccessor;
import org.mx.error.UserInterfaceSystemErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
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
    private MongoTemplate template;

    /**
     * 构造函数
     *
     * @param accessor 字典数据访问接口
     */
    @Autowired
    public ChatRoomServiceImpl(@Qualifier("generalDictAccessorMongodb") GeneralDictAccessor accessor,
                               MongoTemplate template) {
        super();
        this.accessor = accessor;
        this.template = template;
    }
    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#getMessageAttachmentById(String)
     */
    @Override
    public Attachment getMessageAttachmentById(String id) {
        return accessor.getById(id, Attachment.class);
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
            int unread;
            ChatMessage lastMessage = null;
            String chatRoomId = chatRoom.getId();
            long sentTime = 0;
            GeneralAccessor.ConditionTuple sentTimeCond;
            if (map.containsKey(chatRoomId)) {
                ChatRoomSummaryRequest request = map.get(chatRoomId);
                Direction direction = request.getDirection();
                String messageId = request.getLastMessageId();
                ChatMessage chatMessage = accessor.getById(messageId, ChatMessage.class);
                if (direction == Direction.FORWARD) {
                    // 需要取未读消息
                    if (chatMessage != null) {
                        sentTime = chatMessage.getSentTime();
                    }
                    sentTimeCond = GeneralAccessor.ConditionTuple.gte("sentTime", sentTime + 1);
                } else {
                    if (chatMessage != null) {
                        sentTime = chatMessage.getSentTime();
                    }
                    sentTimeCond = GeneralAccessor.ConditionTuple.lte("sentTime", sentTime - 1);
                }
            } else {
                sentTimeCond = GeneralAccessor.ConditionTuple.gte("sentTime", sentTime);
            }
            List<ChatMessage> list = accessor.find(null, GeneralAccessor.ConditionGroup.and(
                    sentTimeCond, GeneralAccessor.ConditionTuple.eq("chatRoom", chatRoom)),
                    GeneralAccessor.RecordOrderGroup.group(GeneralAccessor.RecordOrder.desc("sentTime")),
                    ChatMessage.class);
            unread = list.size();
            if (unread > 0) {
                lastMessage = list.get(0);
                transformChatMessage(lastMessage);
            }
            return new ChatRoomSummary(chatRoom, unread, lastMessage);
        }).collect(Collectors.toList());
    }

    private void transformChatMessage(ChatMessage chatMessage) {
        if (chatMessage != null) {
            if (chatMessage.getMessageType() == ChatMessage.MessageType.TEXT) {
                chatMessage.setMessageByType(new ChatMessage.TextMessage((String)chatMessage.getMessage()));
            } else if (chatMessage.getMessageType() == ChatMessage.MessageType.FILE) {
                if (chatMessage.getMessage() == null) {
                    if (logger.isErrorEnabled()) {
                        logger.error("The attachment's id is null.");
                    }
                    throw new UserInterfaceSystemErrorException(
                            UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
                    );
                }
                String id = chatMessage.getMessage().toString();
                Attachment attachment = accessor.getById(id, Attachment.class);
                if (attachment != null) {
                    chatMessage.setMessageByType(new ChatMessage.FileMessage(attachment));
                } else {
                    if (logger.isWarnEnabled()) {
                        logger.warn(String.format("The attachment[%s] not found.", id));
                    }
                }
            } else {
                if (logger.isWarnEnabled()) {
                    logger.warn(String.format("Unsupported message type: %s.",
                            chatMessage.getMessageType()));
                }
            }
        }
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
        // 先取出文本消息
        List<ChatMessage> textMessages = accessor.find(GeneralAccessor.ConditionGroup.and(
                GeneralAccessor.ConditionTuple.eq("chatRoom", chatRoom),
                GeneralAccessor.ConditionTuple.gte("sentTime", lastAccessTime),
                GeneralAccessor.ConditionTuple.eq("messageType", ChatMessage.MessageType.TEXT)
        ), ChatMessage.class);

        List<ChatMessage> result = new ArrayList<>();
        textMessages.forEach(message -> {
            message.setMessageByType(new ChatMessage.TextMessage((String)message.getMessage()));
            result.add(message);
        });
        // 再取出文件消息
        LookupOperation lookup = LookupOperation.newLookup().from("attachment")
                .localField("message").foreignField("_id").as("attachments");
        AggregationOperation match = Aggregation.match(Criteria.where("messageType").is("FILE")
                .andOperator(Criteria.where("chatRoom").is(template.getConverter().toDBRef(chatRoom, null))
                .andOperator(Criteria.where("sentTime").gte(lastAccessTime))));
        List<ChatMessage> fileMessages = getFileChatMessageByAggregate(lookup, match);
        if (!fileMessages.isEmpty()) {
            result.addAll(fileMessages);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private List<ChatMessage> getFileChatMessageByAggregate(LookupOperation lookup, AggregationOperation match) {
        List<ChatMessage> result = new ArrayList<>();
        Aggregation aggregation = Aggregation.newAggregation(lookup, match);
        List<Document> list = template.aggregate(aggregation, "chatMessage", Document.class)
                .getMappedResults();
        list.forEach(document -> {
            ChatMessage chatMessage = template.getConverter().read(ChatMessage.class, document);
            List<Document> attachments = (List<Document>)document.get("attachments");
            if (attachments != null && !attachments.isEmpty()) {
                Attachment attachment = template.getConverter().read(Attachment.class, attachments.get(0));
                chatMessage.setMessageByType(new ChatMessage.FileMessage(attachment));
                result.add(chatMessage);
            }
        });
        return result;
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
        long sentTime = -1;
        if (!StringUtils.isBlank(lastMessageId)) {
            ChatMessage chatMessage = accessor.getById(lastMessageId, ChatMessage.class);
            if (chatMessage != null) {
                sentTime = chatMessage.getSentTime();
            }
        }
        GeneralAccessor.ConditionGroup group;
        AggregationOperation match;
        if (direction == Direction.FORWARD) {
            // 新消息
            group = GeneralAccessor.ConditionGroup.and(
                    GeneralAccessor.ConditionTuple.eq("chatRoom", chatRoom),
                    GeneralAccessor.ConditionTuple.gte("sentTime", sentTime + 1),
                    GeneralAccessor.ConditionTuple.eq("messageType", "TEXT")
            );
            match = Aggregation.match(Criteria.where("messageType").is("FILE")
                    .andOperator(Criteria.where("chatRoom").is(template.getConverter().toDBRef(chatRoom, null))
                            .andOperator(Criteria.where("sentTime").gte(sentTime + 1))));
        } else {
            // 旧消息
            group = GeneralAccessor.ConditionGroup.and(
                    GeneralAccessor.ConditionTuple.eq("chatRoom", chatRoom),
                    GeneralAccessor.ConditionTuple.lte("sentTime", sentTime - 1),
                    GeneralAccessor.ConditionTuple.eq("messageType", "TEXT")
            );
            match = Aggregation.match(Criteria.where("messageType").is("FILE")
                    .andOperator(Criteria.where("chatRoom").is(template.getConverter().toDBRef(chatRoom, null))
                            .andOperator(Criteria.where("sentTime").lte(sentTime - 1))));
        }
        List<ChatMessage> result = new ArrayList<>();
        List<ChatMessage> textMessages = accessor.find(pagination, group, GeneralAccessor.RecordOrderGroup.group(
                GeneralAccessor.RecordOrder.asc("sentTime")
        ), ChatMessage.class);
        textMessages.forEach(message -> {
            message.setMessageByType(new ChatMessage.TextMessage((String)message.getMessage()));
            result.add(message);
        });
        // 再取出文件消息
        LookupOperation lookup = LookupOperation.newLookup().from("attachment")
                .localField("message").foreignField("_id").as("attachments");
        List<ChatMessage> fileMessages = getFileChatMessageByAggregate(lookup, match);
        if (!fileMessages.isEmpty()) {
            result.addAll(fileMessages);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomService#saveChatMessage(String, String, String, ChatMessage.MessageType, String)
     */
    @Override
    public ChatMessage saveChatMessage(String accoutnCode, String eiimCode, String chatRoomId, ChatMessage.MessageType messageType,
                                       String message) {
        if (StringUtils.isBlank(accoutnCode) || StringUtils.isBlank(chatRoomId) || messageType == null ||
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
        chatMessage.setMessageType(messageType);
        if (messageType == ChatMessage.MessageType.FILE) {
            chatMessage.setMessage(new ObjectId(message));
        } else {
            chatMessage.setMessage(message);
        }
        chatMessage.setSender(sender);
        chatMessage.setSentTime(System.currentTimeMillis());
        try {
            chatMessage = accessor.save(chatMessage);
            transformChatMessage(chatMessage);
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Save a chat message successfully, chat room id: %s, account code: %s," +
                        "message type: %s, message: %s.", chatRoomId, accoutnCode, messageType, message));
            }
            return chatMessage;
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
