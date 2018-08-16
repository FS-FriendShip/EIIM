package com.fs.eiim.dal.entity;

import org.mx.dal.entity.Base;

import java.util.Set;

/**
 * 描述： 聊天室信息接口定义
 *
 * @author john peng
 * Date time 2018/8/8 下午1:22
 */
public interface ChatRoom extends Base {
    String getName();

    void setName(String name);

    Account getCreator();

    void setCreator(Account creator);

    String getDesc();

    void setDesc(String desc);

    Set<ChatRoomMember> getMembers();

    void setMembers(Set<ChatRoomMember> members);
}
