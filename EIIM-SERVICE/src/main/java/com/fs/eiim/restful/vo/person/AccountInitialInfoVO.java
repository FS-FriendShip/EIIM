package com.fs.eiim.restful.vo.person;

import com.fs.eiim.service.BaseDataService;

public class AccountInitialInfoVO {
    private String accountCode, password, nickname, avatar;

    public BaseDataService.AccountInitialInfo get() {
        return new BaseDataService.AccountInitialInfo(accountCode, password, nickname, avatar);
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
