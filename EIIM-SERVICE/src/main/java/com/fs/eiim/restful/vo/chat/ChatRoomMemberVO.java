package com.fs.eiim.restful.vo.chat;

import com.fs.eiim.dal.entity.ChatRoomMember;
import com.fs.eiim.restful.vo.account.AccountInfoVO;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomMemberVO {
    private String id, status;
    private AccountInfoVO account;
    private Long lastAccessTime;
    private boolean isTop = false;

    public static ChatRoomMemberVO valueOf(ChatRoomMember member) {
        if (member == null) {
            return null;
        }
        ChatRoomMemberVO chatRoomMemberVO = new ChatRoomMemberVO();
        chatRoomMemberVO.id = member.getId();
        chatRoomMemberVO.status = member.getStatus();
        chatRoomMemberVO.account = AccountInfoVO.valueOf(member.getAccount());
        chatRoomMemberVO.lastAccessTime = member.getLastAccessTime();
        chatRoomMemberVO.isTop = member.isTop();
        return chatRoomMemberVO;
    }

    public static List<ChatRoomMemberVO> valueOf(List<ChatRoomMember> members) {
        if (members == null || members.isEmpty()) {
            return null;
        }
        List<ChatRoomMemberVO> list = new ArrayList<>();
        members.forEach(member -> list.add(valueOf(member)));
        return list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AccountInfoVO getAccount() {
        return account;
    }

    public void setAccount(AccountInfoVO account) {
        this.account = account;
    }

    public Long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }
}
