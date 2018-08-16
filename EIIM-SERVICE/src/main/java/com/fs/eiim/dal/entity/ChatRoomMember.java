package com.fs.eiim.dal.entity;

/**
 * 描述： 聊天室成员信息接口定义
 *
 * @author john peng
 * Date time 2018/8/8 下午2:34
 */
public interface ChatRoomMember {
    boolean isTop();

    void setTop(boolean isTop);

    String getStatus();

    void setStatus(String status);

    Account getAccount();

    void setAccount(Account account);

    long getLastAccessTime();

    void setLastAccessTime(long lastAccessTime);

    boolean isValid();

    void setValid(boolean valid);
}
