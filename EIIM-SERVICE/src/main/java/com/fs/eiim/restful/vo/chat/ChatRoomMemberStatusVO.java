package com.fs.eiim.restful.vo.chat;

public class ChatRoomMemberStatusVO {
    private String accountCode, status;
    private boolean isTop = false;

    public String getAccountCode() {
        return accountCode;
    }

    public String getStatus() {
        return status;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTop(boolean top) {
        isTop = top;
    }
}
