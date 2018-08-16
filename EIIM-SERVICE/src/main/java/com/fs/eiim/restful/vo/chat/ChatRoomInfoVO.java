package com.fs.eiim.restful.vo.chat;

import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.ChatRoom;
import com.fs.eiim.dal.entity.ChatRoomMember;
import com.fs.eiim.restful.vo.account.AccountInfoVO;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomInfoVO {
    private String id, name, desc;
    private AccountInfoVO creator;
    private List<ChatRoomMemberVO> members;

    public static ChatRoomInfoVO valueOf(ChatRoom chatRoom, List<ChatRoomMember> members) {
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

        if (members != null && !members.isEmpty()) {
            List<ChatRoomMemberVO> list = new ArrayList<>(members.size());
            members.forEach(member -> list.add(ChatRoomMemberVO.valueOf(member)));
            chatRoomInfoVO.members = list;
        }

        return chatRoomInfoVO;
    }

    public static List<ChatRoomInfoVO> valueOf(List<ChatRoom> chatRooms) {
        if (chatRooms == null || chatRooms.isEmpty()) {
            return null;
        }
        List<ChatRoomInfoVO> list = new ArrayList<>();
        chatRooms.forEach(chatRoom -> list.add(valueOf(chatRoom, null)));
        return list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public AccountInfoVO getCreator() {
        return creator;
    }

    public void setCreator(AccountInfoVO creator) {
        this.creator = creator;
    }

    public List<ChatRoomMemberVO> getMembers() {
        return members;
    }

    public void setMembers(List<ChatRoomMemberVO> members) {
        this.members = members;
    }
}
