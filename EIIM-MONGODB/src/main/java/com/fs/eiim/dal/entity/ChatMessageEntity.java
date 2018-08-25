package com.fs.eiim.dal.entity;

import org.mx.dal.entity.MongoBaseEntity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 描述： 聊天室消息信息实体类定义
 *
 * @author john peng
 * Date time 2018/8/8 下午4:09
 */
@Document(collection = "chatMessage")
public class ChatMessageEntity extends MongoBaseEntity implements ChatMessage {
    @DBRef
    private ChatRoom chatRoom;
    @DBRef
    private Account sender;
    private long sentTime = System.currentTimeMillis();
    private String messageType, message;

    /**
     * {@inheritDoc}
     *
     * @see ChatMessage#getChatRoom()
     */
    @Override
    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatMessage#setChatRoom(ChatRoom)
     */
    @Override
    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatMessage#getSender()
     */
    @Override
    public Account getSender() {
        return sender;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatMessage#setSender(Account)
     */
    @Override
    public void setSender(Account sender) {
        this.sender = sender;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatMessage#getSentTime()
     */
    @Override
    public long getSentTime() {
        return sentTime;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatMessage#setSentTime(long)
     */
    @Override
    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatMessage#getMessageType()
     */
    @Override
    public String getMessageType() {
        return messageType;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatMessage#setMessageType(String)
     */
    @Override
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatMessage#getMessage()
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatMessage#setMessage(String)
     */
    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
