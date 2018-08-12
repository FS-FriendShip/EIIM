package com.fs.eiim.restful.vo.chat;

import com.fs.eiim.restful.vo.account.AccountInfoVO;

import java.util.List;

public class ChatRoomVO {
    private String id, name, desc;
    private boolean isTop = false;
    private AccountInfoVO creator;
    private List<RoomMemberVO> members;
}
