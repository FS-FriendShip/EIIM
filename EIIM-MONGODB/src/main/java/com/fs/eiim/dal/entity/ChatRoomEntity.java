package com.fs.eiim.dal.entity;

import org.mx.dal.entity.MongoBaseEntity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 描述： 聊天室信息实体类定义
 *
 * @author john peng
 * Date time 2018/8/8 下午4:01
 */
@Document(collection = "chatroom")
public class ChatRoomEntity extends MongoBaseEntity implements ChatRoom {
    private String name, desc;
    @DBRef
    private Account creator;
    private boolean isTop;

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
     * @see ChatRoom#isTop()
     */
    @Override
    public boolean isTop() {
        return isTop;
    }

    /**
     * {@inheritDoc}
     *
     * @see ChatRoom#setTop(boolean)
     */
    @Override
    public void setTop(boolean isTop) {
        this.isTop = isTop;
    }
}
