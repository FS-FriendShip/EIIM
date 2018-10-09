package com.fs.eiim.restful.vo.chat;

import com.fs.eiim.dal.entity.ChatRoomMember;
import com.fs.eiim.restful.vo.account.AccountInfoVO;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomMemberVO {
    private String status;
    private AccountInfoVO account;
    private Long lastAccessTime;
    private boolean isTop = false;

    public static ChatRoomMemberVO valueOf(ChatRoomMember member) {
        if (member == null) {
            return null;
        }
        ChatRoomMemberVO chatRoomMemberVO = new ChatRoomMemberVO();
        chatRoomMemberVO.status = member.getStatus();
        chatRoomMemberVO.account = AccountInfoVO.valueOf(member.getAccount(), null);
        chatRoomMemberVO.lastAccessTime = member.getLastAccessTime();
        chatRoomMemberVO.isTop = member.isTop();
        return chatRoomMemberVO;
    }

    public static List<ChatRoomMemberVO> valueOf(List<ChatRoomMember> members) {
        List<ChatRoomMemberVO> list = new ArrayList<>();
        if (members != null && !members.isEmpty()) {
            members.forEach(member -> list.add(valueOf(member)));
        }
        return list;
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
