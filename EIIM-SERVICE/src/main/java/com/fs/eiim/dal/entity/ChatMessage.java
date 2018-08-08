package com.fs.eiim.dal.entity;

import org.mx.dal.entity.Base;

/**
 * 描述： 聊天消息信息接口定义
 *
 * @author john peng
 * Date time 2018/8/8 下午1:25
 */
public interface ChatMessage extends Base {
    ChatRoom getChatRoom();

    void setChatRoom(ChatRoom chatRoom);

    Account getSender();

    void setSender(Account sender);

    long getSentTime();

    void setSentTime(long sentTime);

    String getMessageType();

    void setMessageType(String messageType);

    String getMessage();

    void setMessage(String essage);
}
