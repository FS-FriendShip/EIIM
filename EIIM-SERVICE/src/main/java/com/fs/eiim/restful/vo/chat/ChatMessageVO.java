package com.fs.eiim.restful.vo.chat;

import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.ChatMessage;
import com.fs.eiim.restful.vo.account.AccountInfoVO;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageVO {
    private String id, messageType, message;
    private AccountInfoVO sender;
    private Long sentTime;

    public static ChatMessageVO valueOf(ChatMessage chatMessage) {
        if (chatMessage == null) {
            return null;
        }
        ChatMessageVO chatMessageVO = new ChatMessageVO();
        chatMessageVO.id = chatMessage.getId();
        chatMessageVO.messageType = chatMessage.getMessageType();
        chatMessageVO.message = chatMessage.getMessage();
        chatMessageVO.sentTime = chatMessage.getSentTime();
        Account sender = chatMessage.getSender();
        if (sender != null) {
            chatMessageVO.sender = AccountInfoVO.valueOf(sender);
        }
        return chatMessageVO;
    }

    public static List<ChatMessageVO> valueOf(List<ChatMessage> chatMessages) {
        if (chatMessages == null || chatMessages.isEmpty()) {
            return null;
        }
        List<ChatMessageVO> list = new ArrayList<>(chatMessages.size());
        chatMessages.forEach(chatMessage -> list.add(valueOf(chatMessage)));
        return list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AccountInfoVO getSender() {
        return sender;
    }

    public void setSender(AccountInfoVO sender) {
        this.sender = sender;
    }

    public Long getSentTime() {
        return sentTime;
    }

    public void setSentTime(Long sentTime) {
        this.sentTime = sentTime;
    }
}
