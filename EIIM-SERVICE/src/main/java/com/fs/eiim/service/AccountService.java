package com.fs.eiim.service;

import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.AccountState;

import java.util.List;

/**
 * 描述： 账户服务接口定义
 *
 * @author john peng
 * Date time 2018/8/12 下午2:27
 */
public interface AccountService {
    AccountState login(String accountCode, String password);

    AccountState validateToken(String token);

    void logout(String accountId);

    AccountState getAccountStateByAccountId(String accountId);

    Account valid(String accountId, boolean valid);

    List<AccountState> getAllAccountsStatus();
}
