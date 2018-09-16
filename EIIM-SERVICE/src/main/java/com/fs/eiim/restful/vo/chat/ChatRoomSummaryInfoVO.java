package com.fs.eiim.restful.vo.chat;

import com.fs.eiim.service.ChatRoomService;

public class ChatRoomSummaryInfoVO extends ChatRoomInfoVO {
    private int unread = 0;
    private ChatMessageVO latestMessage;

    public static ChatRoomSummaryInfoVO valueOf(ChatRoomService.ChatRoomSummary chatRoomSummary) {
        if (chatRoomSummary == null) {
            return null;
        }
        ChatRoomSummaryInfoVO summaryInfoVO = new ChatRoomSummaryInfoVO();
        ChatRoomInfoVO.transform(chatRoomSummary.getChatRoom(), summaryInfoVO);
        summaryInfoVO.unread = chatRoomSummary.getUnread();
        summaryInfoVO.latestMessage = ChatMessageVO.valueOf(chatRoomSummary.getLatestMessage());

        return summaryInfoVO;
    }

    public int getUnread() {
        return unread;
    }

    public ChatMessageVO getLatestMessage() {
        return latestMessage;
    }
}
