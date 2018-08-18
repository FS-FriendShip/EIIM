package com.fs.eiim.restful.vo.account;

import com.fs.eiim.dal.entity.AccountState;

import java.util.ArrayList;
import java.util.List;

public class AccountStateVO {
    private AccountInfoVO account;
    private String status, token;
    private Long loginTime, logoutTime;

    public static AccountStateVO valueOf(AccountState accountState) {
        if (accountState == null) {
            return null;
        }
        AccountStateVO accountStateVO = new AccountStateVO();
        accountStateVO.account = AccountInfoVO.valueOf(accountState.getAccount());
        accountStateVO.status = accountState.getStatus();
        accountStateVO.token = accountState.getToken();
        accountStateVO.loginTime = accountState.getLoginTime();
        accountStateVO.logoutTime = accountState.getLogoutTime();
        return accountStateVO;
    }

    public static List<AccountStateVO> valueOf(List<AccountState> states) {
        List<AccountStateVO> list = new ArrayList<>();
        if (states != null && !states.isEmpty()) {
            states.forEach(state -> list.add(valueOf(state)));
        }
        return list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public Long getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Long logoutTime) {
        this.logoutTime = logoutTime;
    }

    public boolean isOnline() {
        // 状态为离线或者登出时间不为空，表示该账户不在线
        return !("offline".equalsIgnoreCase(status) || logoutTime != null);
    }
}
