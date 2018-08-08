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
