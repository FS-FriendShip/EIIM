package com.fs.eiim.dal.entity;

import org.mx.dal.entity.MongoBaseEntity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

/**
 * 描述： 聊天室信息实体类定义
 *
 * @author john peng
 * Date time 2018/8/8 下午4:01
 */
@Document(collection = "chatRoom")
public class ChatRoomEntity extends MongoBaseEntity implements ChatRoom {
    private String name, desc;
    @DBRef
    private Account creator;
    private Set<ChatRoomMember> members = new HashSet<>();

    /**
     * {@inheritDoc}
     *
     * @see ChatRoom#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoom#setName(String)
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoom#getCreator()
     */
    @Override
    public Account getCreator() {
        return creator;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoom#setCreator(Account)
     */
    @Override
    public void setCreator(Account creator) {
        this.creator = creator;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoom#getDesc()
     */
    @Override
    public String getDesc() {
        return desc;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoom#setDesc(String)
     */
    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoom#getMembers()
     */
    @Override
    public Set<ChatRoomMember> getMembers() {
        return members;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoom#setMembers(Set)
     */
    @Override
    public void setMembers(Set<ChatRoomMember> members) {
        this.members = members;
    }

    static public class ChatRoomMemberEntity implements ChatRoomMember {
        private boolean isTop = false, valid = true;
        private String status = "online";
        private Account account;
        private long lastAccessTime;

        @Override
        public boolean isTop() {
            return isTop;
        }

        @Override
        public void setTop(boolean top) {
            isTop = top;
        }

        @Override
        public String getStatus() {
            return status;
        }

        @Override
        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public Account getAccount() {
            return account;
        }

        @Override
        public void setAccount(Account account) {
            this.account = account;
        }

        @Override
        public long getLastAccessTime() {
            return lastAccessTime;
        }

        @Override
        public void setLastAccessTime(long lastAccessTime) {
            this.lastAccessTime = lastAccessTime;
        }

        @Override
        public boolean isValid() {
            return valid;
        }

        @Override
        public void setValid(boolean valid) {
            this.valid = valid;
        }
    }
}
