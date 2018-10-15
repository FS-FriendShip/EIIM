package com.fs.eiim.restful.vo.chat;

import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageVO {
    private String id, chatRoomId;
    private ChatMessage.MessageType messageType;
    private ChatMessage.Message message;
    private MessageSenderVO sender;
    private Long sentTime;

    public static ChatMessageVO valueOf(ChatMessage chatMessage) {
        if (chatMessage == null) {
            return null;
        }
        ChatMessageVO chatMessageVO = new ChatMessageVO();
        chatMessageVO.id = chatMessage.getId();
        chatMessageVO.chatRoomId = chatMessage.getChatRoom().getId();
        chatMessageVO.messageType = chatMessage.getMessageType();
        chatMessageVO.message = chatMessage.getMessageByType();
        chatMessageVO.sentTime = chatMessage.getSentTime();
        Account sender = chatMessage.getSender();
        if (sender != null) {
            chatMessageVO.sender = MessageSenderVO.valueOf(sender);
        }
        return chatMessageVO;
    }

    public static List<ChatMessageVO> valueOf(List<ChatMessage> chatMessages) {
        List<ChatMessageVO> list = new ArrayList<>(chatMessages.size());
        if (!chatMessages.isEmpty()) {
            chatMessages.forEach(chatMessage -> list.add(valueOf(chatMessage)));
        }
        return list;
    }

    public String getId() {
        return id;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public ChatMessage.MessageType getMessageType() {
        return messageType;
    }

    public ChatMessage.Message getMessage() {
        return message;
    }

    public MessageSenderVO getSender() {
        return sender;
    }

    public Long getSentTime() {
        return sentTime;
    }
}
