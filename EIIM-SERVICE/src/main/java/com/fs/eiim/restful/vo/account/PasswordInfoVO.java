package com.fs.eiim.restful.vo.account;

public class PasswordInfoVO {
    private String accountCode, oldPassword, newPassword;

    public String getAccountCode() {
        return accountCode;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
