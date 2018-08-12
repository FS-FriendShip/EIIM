package com.fs.eiim.service;

import com.fs.eiim.dal.entity.AccountState;

import java.util.List;

/**
 * 描述： 账户服务接口定义
 *
 * @author john peng
 * Date time 2018/8/12 下午2:27
 */
public interface AccountService {
    AccountState login(String code, String password);

    void logout(String accountId);

    List<AccountState> getAllAccountsStatus();
}