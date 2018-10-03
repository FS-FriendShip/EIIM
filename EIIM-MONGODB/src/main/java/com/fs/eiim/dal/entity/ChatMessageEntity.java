package com.fs.eiim.dal.entity;

import org.mx.dal.entity.MongoBaseEntity;
import org.springframework.data.annotation.Transient;
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
    private MessageType messageType = MessageType.TEXT;
    private Object message;
    @Transient
    private Message messageByType;

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
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatMessage#setMessageType(MessageType)
     */
    @Override
    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatMessage#getMessageType()
     */
    @Override
    public Message getMessageByType() {
        return messageByType;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatMessage#setMessageByType(Message)
     */
    @Override
    public void setMessageByType(Message messageByType) {
        this.messageByType = messageByType;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatMessage#getMessage()
     */
    @Override
    public Object getMessage() {
        return message;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatMessage#setMessage(Object)
     */
    @Override
    public void setMessage(Object message) {
        this.message = message;
    }
}
