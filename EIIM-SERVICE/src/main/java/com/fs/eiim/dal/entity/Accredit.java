package com.fs.eiim.dal.entity;

import org.mx.dal.entity.Base;

/**
 * 描述： 账户授权信息接口定义
 *
 * @author john peng
 * Date time 2018/8/8 下午1:20
 */
public interface Accredit extends Base {
    Account getSrcAccount();

    void setSrcAccount(Account srcAccount);

    Account getTarAccount();

    void setTarAccount(Account tarAccount);

    long getStartTime();

    void setStartTime(long startTime);

    Long getCloseTime();

    void setCloseTime(Long closeTime);
}
