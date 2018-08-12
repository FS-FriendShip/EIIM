package com.fs.eiim.restful.vo.account;

public class PasswordInfoVO {
    private String accountId, oldPassword, newPassword;

    public String getAccountId() {
        return accountId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
