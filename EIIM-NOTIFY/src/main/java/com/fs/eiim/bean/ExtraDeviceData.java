package com.fs.eiim.bean;

import com.alibaba.fastjson.JSON;

/**
 * 描述： 通信终端额外的设备信息类定义
 *
 * @author john peng
 * Date time 2018/8/21 下午8:28
 */
public class ExtraDeviceData {
    private String accountCode, eiimCode;

    public ExtraDeviceData() {
        super();
    }

    public ExtraDeviceData(String accountCode, String eiimCode) {
        this();
        this.accountCode = accountCode;
        this.eiimCode = eiimCode;
    }

    /**
     * {@inheritDoc}
     *
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getEiimCode() {
        return eiimCode;
    }

    public void setEiimCode(String eiimCode) {
        this.eiimCode = eiimCode;
    }
}