package com.fs.eiim.dal.entity;

import org.mx.dal.entity.MongoBaseEntity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 描述： 授权实体信息类定义
 *
 * @author john peng
 * Date time 2018/8/8 下午3:47
 */
@Document(collection = "accredit-ext")
public class AccreditEntity extends MongoBaseEntity implements Accredit {
    @DBRef
    private Account srcAccount, tarAccount;
    private long startTime = System.currentTimeMillis();
    private Long closeTime;

    /**
     * {@inheritDoc}
     *
     * @see Accredit#getSrcAccount()
     */
    @Override
    public Account getSrcAccount() {
        return srcAccount;
    }

    /**
     * {@inheritDoc}
     *
     * @see Accredit#setSrcAccount(Account)
     */
    @Override
    public void setSrcAccount(Account srcAccount) {
        this.srcAccount = srcAccount;
    }

    /**
     * {@inheritDoc}
     *
     * @see Accredit#getTarAccount()
     */
    @Override
    public Account getTarAccount() {
        return tarAccount;
    }

    /**
     * {@inheritDoc}
     *
     * @see Accredit#setTarAccount(Account)
     */
    @Override
    public void setTarAccount(Account tarAccount) {
        this.tarAccount = tarAccount;
    }

    /**
     * {@inheritDoc}
     *
     * @see Accredit#getStartTime()
     */
    @Override
    public long getStartTime() {
        return startTime;
    }

    /**
     * {@inheritDoc}
     *
     * @see Accredit#setStartTime(long)
     */
    @Override
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * {@inheritDoc}
     *
     * @see Accredit#getCloseTime()
     */
    @Override
    public Long getCloseTime() {
        return closeTime;
    }

    /**
     * {@inheritDoc}
     *
     * @see Accredit#setCloseTime(Long)
     */
    @Override
    public void setCloseTime(Long closeTime) {
        this.closeTime = closeTime;
    }
}
