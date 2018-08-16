package com.fs.eiim.restful.vo.chat;

import com.fs.eiim.dal.entity.ChatNotice;
import com.fs.eiim.dal.entity.ChatRoom;
import org.mx.dal.EntityFactory;

public class ChatNoticeFormVO {
    private String chatRoomId, noticeType, notice;

    public ChatNotice get() {
        ChatNotice chatNotice = EntityFactory.createEntity(ChatNotice.class);
        ChatRoom chatRoom = EntityFactory.createEntity(ChatRoom.class);
        chatRoom.setId(chatRoomId);
        chatNotice.setChatRoom(chatRoom);
        chatNotice.setNoticeType(noticeType);
        chatNotice.setNotice(notice);
        return chatNotice;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
