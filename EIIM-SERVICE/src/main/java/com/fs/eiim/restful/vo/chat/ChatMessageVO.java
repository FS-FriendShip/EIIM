package com.fs.eiim.restful.vo.chat;

import com.fs.eiim.restful.vo.account.AccountInfoVO;

public class ChatMessageVO {
    private String id, messageType, message;
    private AccountInfoVO sender;
    private Long sendTime;
}
