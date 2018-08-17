package com.fs.eiim.dal.entity;

import org.mx.dal.entity.MongoBaseEntity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 描述： 账户状态实体类定义
 *
 * @author john peng
 * Date time 2018/8/8 下午3:05
 */
@Document(collection = "accountState")
public class AccountStateEntity extends MongoBaseEntity implements AccountState {
    private String status, token;
    @DBRef
    private Account account;
    private long loginTime = System.currentTimeMillis();
    private Long logoutTime;

    /**
     * {@inheritDoc}
     *
     * @see AccountState#getAccount()
     */
    @Override
    public Account getAccount() {
        return account;
    }

    /**
     * {@inheritDoc}
     *
     * @see AccountState#setAccount(Account)
     */
    @Override
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * {@inheritDoc}
     *
     * @see AccountState#getStatus()
     */
    @Override
    public String getStatus() {
        return status;
    }

    /**
     * {@inheritDoc}
     *
     * @see AccountState#setStatus(String)
     */
    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * {@inheritDoc}
     *
     * @see AccountState#getToken()
     */
    @Override
    public String getToken() {
        return token;
    }

    /**
     * {@inheritDoc}
     *
     * @see AccountState#setToken(String)
     */
    @Override
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * {@inheritDoc}
     *
     * @see AccountState#getLoginTime()
     */
    @Override
    public long getLoginTime() {
        return loginTime;
    }

    /**
     * {@inheritDoc}
     *
     * @see AccountState#setLoginTime(long)
     */
    @Override
    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    /**
     * {@inheritDoc}
     *
     * @see AccountState#getLogoutTime()
     */
    @Override
    public Long getLogoutTime() {
        return logoutTime;
    }

    /**
     * {@inheritDoc}
     *
     * @see AccountState#setLogoutTime(Long)
     */
    @Override
    public void setLogoutTime(Long logoutTime) {
        this.logoutTime = logoutTime;
    }
}
