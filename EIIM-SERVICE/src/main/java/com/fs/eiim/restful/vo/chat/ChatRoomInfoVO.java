package com.fs.eiim.restful.vo.chat;

import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.ChatRoom;
import com.fs.eiim.dal.entity.ChatRoomMember;
import com.fs.eiim.restful.vo.account.AccountInfoVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ChatRoomInfoVO {
    private String id, name, desc;
    private AccountInfoVO creator;
    private List<ChatRoomMemberVO> members;

    public static ChatRoomInfoVO valueOf(ChatRoom chatRoom) {
        if (chatRoom == null) {
            return null;
        }
        ChatRoomInfoVO chatRoomInfoVO = new ChatRoomInfoVO();
        chatRoomInfoVO.id = chatRoom.getId();
        chatRoomInfoVO.name = chatRoom.getName();
        chatRoomInfoVO.desc = chatRoom.getDesc();

        Account creator = chatRoom.getCreator();
        if (creator != null) {
            chatRoomInfoVO.creator = AccountInfoVO.valueOf(creator);
        }

        Set<ChatRoomMember> members = chatRoom.getMembers();
        if (members != null && !members.isEmpty()) {
            List<ChatRoomMemberVO> list = new ArrayList<>(members.size());
            members.forEach(member -> list.add(ChatRoomMemberVO.valueOf(member)));
            chatRoomInfoVO.members = list;
        }

        return chatRoomInfoVO;
    }

    public static List<ChatRoomInfoVO> valueOf(List<ChatRoom> chatRooms) {
        List<ChatRoomInfoVO> list = new ArrayList<>();
        if (chatRooms != null && !chatRooms.isEmpty()) {
            chatRooms.forEach(chatRoom -> list.add(valueOf(chatRoom)));
        }
        return list;
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
}
