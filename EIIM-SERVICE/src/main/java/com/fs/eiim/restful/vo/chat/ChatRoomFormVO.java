package com.fs.eiim.restful.vo.chat;

import java.util.List;

public class ChatRoomFormVO {
    private String id, name;
    private List<String> addAccountCodes;
    private List<String> delAccountCodes;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getAddAccountCodes() {
        return addAccountCodes;
    }

    public List<String> getDelAccountCodes() {
        return delAccountCodes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddAccountCodes(List<String> addAccountCodes) {
        this.addAccountCodes = addAccountCodes;
    }

    public void setDelAccountCodes(List<String> delAccountCodes) {
        this.delAccountCodes = delAccountCodes;
    }
}
