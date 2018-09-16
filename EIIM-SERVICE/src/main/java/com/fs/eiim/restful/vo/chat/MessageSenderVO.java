package com.fs.eiim.restful.vo.chat;

import com.fs.eiim.dal.entity.Account;

public class MessageSenderVO {
    private String id, code, nickname, avatar;

    public static MessageSenderVO valueOf(Account account) {
        if (account == null) {
            return null;
        }
        MessageSenderVO vo = new MessageSenderVO();
        vo.id = account.getId();
        vo.code = account.getCode();
        vo.nickname = account.getNickName();
        vo.avatar = account.getAvatar();
        return vo;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }
}
