package com.fs.eiim.restful.vo.accredit;

import com.fs.eiim.restful.vo.account.AccountInfoVO;

public class AccreditInfoVO {
    private String id;
    private AccountInfoVO srcAccount, tarAccount;
    private boolean valid = false;
    private Long startTime, closeTime;
}
