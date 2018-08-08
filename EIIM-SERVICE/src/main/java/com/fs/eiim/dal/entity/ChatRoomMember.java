package com.fs.eiim.dal.entity;

import org.mx.dal.entity.Base;

/**
 * 描述： 聊天室成员信息接口定义
 *
 * @author john peng
 * Date time 2018/8/8 下午2:34
 */
public interface ChatRoomMember extends Base {
    ChatRoom getChatRoom();

    void setChatRoom(ChatRoom chatRoom);

    Account getAccount();

    void setAccount(Account account);

    long getLastAccessTime();

    void setLastAccessTime(long lastAccessTime);
}
