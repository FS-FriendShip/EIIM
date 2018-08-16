package com.fs.eiim.restful.vo.chat;

public class ChatRoomMemberStatusVO {
    private String accountId, status;
    private boolean isTop = false;

    public String getAccountId() {
        return accountId;
    }

    public String getStatus() {
        return status;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTop(boolean top) {
        isTop = top;
    }
}
