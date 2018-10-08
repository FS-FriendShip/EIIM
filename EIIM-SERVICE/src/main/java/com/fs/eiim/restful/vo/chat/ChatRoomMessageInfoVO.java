package com.fs.eiim.restful.vo.chat;

import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.ChatMessage;
import com.fs.eiim.dal.entity.ChatRoom;
import com.fs.eiim.dal.entity.ChatRoomMember;
import com.fs.eiim.restful.vo.account.AccountInfoVO;
import com.fs.eiim.service.ChatRoomService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChatRoomMessageInfoVO {
    private String id, name, desc;
    private AccountInfoVO creator;
    private List<ChatRoomMemberVO> members;
    private List<ChatMessageVO> messages;

    public static ChatRoomMessageInfoVO valueOf(ChatRoom chatRoom, List<ChatMessage> messages) {
        if (chatRoom == null) {
            return null;
        }
        ChatRoomMessageInfoVO chatRoomMessageInfoVO = new ChatRoomMessageInfoVO();
        chatRoomMessageInfoVO.id = chatRoom.getId();
        chatRoomMessageInfoVO.name = chatRoom.getName();
        chatRoomMessageInfoVO.desc = chatRoom.getDesc();

        Account creator = chatRoom.getCreator();
        if (creator != null) {
            chatRoomMessageInfoVO.creator = AccountInfoVO.valueOf(creator, null);
        }

        Set<ChatRoomMember> members = chatRoom.getMembers();
        if (members != null && !members.isEmpty()) {
            List<ChatRoomMemberVO> list = new ArrayList<>(members.size());
            members.forEach(member -> list.add(ChatRoomMemberVO.valueOf(member)));
            chatRoomMessageInfoVO.members = list;
        }

        if (members != null && !members.isEmpty()) {
            chatRoomMessageInfoVO.messages = ChatMessageVO.valueOf(messages);
        }

        return chatRoomMessageInfoVO;
    }

    public static List<ChatRoomMessageInfoVO> valueOf(List<ChatRoomService.ChatRoomMessageTuple> tuples) {
        List<ChatRoomMessageInfoVO> messageInfoVOS = new ArrayList<>();
        if (tuples != null && !tuples.isEmpty()) {
            tuples.forEach(tuple -> messageInfoVOS.add(valueOf(tuple.getChatRoom(), tuple.getMessages())));
        }
        return messageInfoVOS;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public AccountInfoVO getCreator() {
        return creator;
    }

    public List<ChatRoomMemberVO> getMembers() {
        return members;
    }

    public List<ChatMessageVO> getMessages() {
        return messages;
    }
}
