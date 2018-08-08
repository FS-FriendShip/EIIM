package com.fs.eiim.dal.entity;

import org.mx.dal.entity.Base;

/**
 * 描述： 聊天室公告信息接口定义
 *
 * @author john peng
 * Date time 2018/8/8 下午2:30
 */
public interface ChatNotice extends Base {
    ChatRoom getChatRoom();

    void setChatRoom(ChatRoom chatRoom);

    String getNoticeType();

    void setNoticeType(String noticeType);

    String getNotice();

    void setNotice(String notice);

    long getPushTime();

    void setPushTime(long pushTime);
}
