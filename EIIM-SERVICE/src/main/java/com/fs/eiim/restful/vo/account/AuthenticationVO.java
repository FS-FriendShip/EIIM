package com.fs.eiim.restful.vo.account;

public class AuthenticationVO {
    private String code, password;

    public String getCode() {
        return code;
    }

    public String getPassword() {
        return password;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
