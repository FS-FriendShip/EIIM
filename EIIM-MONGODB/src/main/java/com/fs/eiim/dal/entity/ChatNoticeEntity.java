package com.fs.eiim.dal.entity;

import org.mx.dal.entity.MongoBaseEntity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 描述： 聊天室公告信息实体类定义
 *
 * @author john peng
 * Date time 2018/8/8 下午4:06
 */
@Document(collection = "chatnotice")
public class ChatNoticeEntity extends MongoBaseEntity implements ChatNotice {
    @DBRef
    private ChatRoom chatRoom;
    private String noticeType, notice;
    private long pushTime;

    /**
     * {@inheritDoc}
     *
     * @see ChatNotice#getChatRoom()
     */
    @Override
    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatNotice#setChatRoom(ChatRoom)
     */
    @Override
    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatNotice#getNoticeType()
     */
    @Override
    public String getNoticeType() {
        return noticeType;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatNotice#setNoticeType(String)
     */
    @Override
    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatNotice#getNotice()
     */
    @Override
    public String getNotice() {
        return notice;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatNotice#setNotice(String)
     */
    @Override
    public void setNotice(String notice) {
        this.notice = notice;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatNotice#getPushTime()
     */
    @Override
    public long getPushTime() {
        return pushTime;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatNotice#setPushTime(long)
     */
    @Override
    public void setPushTime(long pushTime) {
        this.pushTime = pushTime;
    }
}
