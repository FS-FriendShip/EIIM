package com.fs.eiim.restful.vo.account;

public class AuthenticationVO {
    private String accountCode, password;

    public String getAccountCode() {
        return accountCode;
    }

    public String getPassword() {
        return password;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
