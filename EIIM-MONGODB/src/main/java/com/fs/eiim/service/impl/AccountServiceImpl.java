package com.fs.eiim.service.impl;

import com.fs.eiim.dal.entity.AccountState;
import com.fs.eiim.service.AccountService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.dal.service.GeneralAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("accountService")
public class AccountServiceImpl implements AccountService {
    private static final Log logger = LogFactory.getLog(AccountServiceImpl.class);
    private GeneralAccessor accessor;

    /**
     * 构造函数
     *
     * @param accessor 数据库访问接口
     */
    @Autowired
    public AccountServiceImpl(@Qualifier("generalAccessorMongodb") GeneralAccessor accessor) {
        super();
        this.accessor = accessor;
    }

    @Override
    public AccountState login(String code, String password) {
        // TODO
        return null;
    }

    @Override
    public void logout(String accountId) {
        // TODO
    }

    @Override
    public List<AccountState> getAllAccountsStatus() {
        // TODO
        return null;
    }
}
