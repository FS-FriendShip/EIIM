package com.fs.eiim.dal.entity;

import org.mx.dal.entity.MongoBaseEntity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 描述： 聊天室成员信息实体类定义
 *
 * @author john peng
 * Date time 2018/8/8 下午4:13
 */
@Document(collection = "chatroom-member")
public class ChatRoomMemberEntity extends MongoBaseEntity implements ChatRoomMember {
    @DBRef
    private ChatRoom chatRoom;
    private boolean isTop = false;
    private String status = "online";
    @DBRef
    private Account account;
    private long lastAccessTime = System.currentTimeMillis();

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomMember#getChatRoom()
     */
    @Override
    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomMember#setChatRoom(ChatRoom)
     */
    @Override
    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }


    /**
     * {@inheritDoc}
     *
     * @see ChatRoomMember#isTop()
     */
    @Override
    public boolean isTop() {
        return isTop;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomMember#setTop(boolean)
     */
    @Override
    public void setTop(boolean isTop) {
        this.isTop = isTop;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomMember#getStatus()
     */
    @Override
    public String getStatus() {
        return status;
    }
    /**
     * {@inheritDoc}
     *
     * @see ChatRoomMember#setStatus(String)
     */
    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomMember#getAccount()
     */
    @Override
    public Account getAccount() {
        return account;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomMember#setAccount(Account)
     */
    @Override
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomMember#getLastAccessTime()
     */
    @Override
    public long getLastAccessTime() {
        return lastAccessTime;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoomMember#setLastAccessTime(long)
     */
    @Override
    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }
}
