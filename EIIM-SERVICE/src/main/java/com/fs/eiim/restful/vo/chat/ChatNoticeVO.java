package com.fs.eiim.restful.vo.chat;

import com.fs.eiim.dal.entity.ChatNotice;

import java.util.ArrayList;
import java.util.List;

public class ChatNoticeVO {
    private String id, noticeType, notice;
    private Long publishTime;

    public static ChatNoticeVO valueOf(ChatNotice chatNotice) {
        if (chatNotice == null) {
            return null;
        }
        ChatNoticeVO chatNoticeVO = new ChatNoticeVO();
        chatNoticeVO.id = chatNotice.getId();
        chatNoticeVO.noticeType = chatNotice.getNoticeType();
        chatNoticeVO.notice = chatNotice.getNotice();
        chatNoticeVO.publishTime = chatNotice.getPushTime();
        return chatNoticeVO;
    }

    public static List<ChatNoticeVO> valueOf(List<ChatNotice> chatNotices) {
        List<ChatNoticeVO> list = new ArrayList<>(chatNotices.size());
        if (chatNotices != null && !chatNotices.isEmpty()) {
            chatNotices.forEach(chatNotice -> list.add(valueOf(chatNotice)));
        }
        return list;
    }

    public String getId() {
        return id;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public String getNotice() {
        return notice;
    }

    public Long getPublishTime() {
        return publishTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public void setPublishTime(Long publishTime) {
        this.publishTime = publishTime;
    }
}
