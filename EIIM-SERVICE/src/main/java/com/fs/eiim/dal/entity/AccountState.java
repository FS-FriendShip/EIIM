package com.fs.eiim.dal.entity;

import org.mx.dal.entity.Base;

/**
 * 描述： 账户状态信息接口定义
 *
 * @author john peng
 * Date time 2018/8/8 下午1:16
 */
public interface AccountState extends Base {
    Account getAccount();

    void setAccount(Account account);

    String getStatus();

    void setStatus(String status);

    String getToken();

    void setToken(String token);

    long getLoginTime();

    void setLoginTime(long loginTime);

    Long getLogoutTime();

    void setLogoutTime(Long logoutTime);
}
