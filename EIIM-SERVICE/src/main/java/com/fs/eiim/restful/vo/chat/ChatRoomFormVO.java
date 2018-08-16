package com.fs.eiim.restful.vo.chat;

import java.util.List;

public class ChatRoomFormVO {
    private String id, name;
    private List<String> addAccountIds;
    private List<String> delAccountIds;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getAddAccountIds() {
        return addAccountIds;
    }

    public List<String> getDelAccountIds() {
        return delAccountIds;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddAccountIds(List<String> addAccountIds) {
        this.addAccountIds = addAccountIds;
    }

    public void setDelAccountIds(List<String> delAccountIds) {
        this.delAccountIds = delAccountIds;
    }
}
