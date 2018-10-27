package com.fs.eiim.service.impl;

import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.AccountState;
import com.fs.eiim.dal.entity.Person;
import com.fs.eiim.error.UserInterfaceEiimErrorException;
import com.fs.eiim.service.AccountService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.DigestUtils;
import org.mx.StringUtils;
import org.mx.comps.rbac.error.UserInterfaceRbacErrorException;
import org.mx.dal.EntityFactory;
import org.mx.dal.service.GeneralAccessor;
import org.mx.dal.service.GeneralDictAccessor;
import org.mx.error.UserInterfaceSystemErrorException;
import org.mx.jwt.error.UserInterfaceJwtErrorException;
import org.mx.jwt.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("accountService")
public class AccountServiceImpl implements AccountService {
    private static final Log logger = LogFactory.getLog(AccountServiceImpl.class);
    private GeneralDictAccessor accessor;
    private JwtService jwtService;

    /**
     * 构造函数
     *
     * @param accessor   数据库访问接口
     * @param jwtService JWT服务接口
     */
    @Autowired
    public AccountServiceImpl(@Qualifier("generalDictAccessorMongodb") GeneralDictAccessor accessor,
                              JwtService jwtService) {
        super();
        this.accessor = accessor;
        this.jwtService = jwtService;
    }

    @Override
    public AccountState getAccountStateByAccountId(String accountId) {
        if (StringUtils.isBlank(accountId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The account's id is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        Account account = accessor.getById(accountId, Account.class);
        if (account == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The account[%s] not found.", accountId));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.ACCOUNT_NOT_FOUND
            );
        }
        List<AccountState> accountStates = accessor.find(
                GeneralAccessor.ConditionTuple.eq("account", account), AccountState.class);
        if (accountStates != null && accountStates.size() > 0) {
            if (accountStates.size() > 1) {
                // 有多条登录记录，取最后一条（时间最大）
                accountStates.sort((o1, o2) -> (int) (o1.getLoginTime() - o2.getLoginTime()));
            }
            return accountStates.get(0);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see AccountService#login(String, String)
     */
    @Override
    public AccountState login(String accountCode, String password) {
        if (StringUtils.isBlank(accountCode)) {
            if (logger.isErrorEnabled()) {
                logger.error("The account's code is blank.");
            }
            throw new UserInterfaceEiimErrorException(UserInterfaceEiimErrorException.EiimErrors.ACCOUNT_BLANK_CODE);
        }
        if (StringUtils.isBlank(password)) {
            if (logger.isErrorEnabled()) {
                logger.error("The account's password is blank.");
            }
            throw new UserInterfaceEiimErrorException(UserInterfaceEiimErrorException.EiimErrors.ACCOUNT_BLANK_PASSWORD);
        }
        Person person = accessor.findOne(GeneralAccessor.ConditionGroup.or(
                GeneralAccessor.ConditionTuple.eq("mobile", accountCode),
                GeneralAccessor.ConditionTuple.eq("email", accountCode)
        ), Person.class);
        List<GeneralAccessor.ConditionGroup> conditions = new ArrayList<>();
        conditions.add(GeneralAccessor.ConditionTuple.eq("code", accountCode));
        conditions.add(GeneralAccessor.ConditionTuple.eq("nickname", accountCode));
        conditions.add(GeneralAccessor.ConditionTuple.eq("eiimCode", accountCode));
        if (person != null) {
            conditions.add(GeneralAccessor.ConditionTuple.eq("person", person));
        }
        Account account = accessor.findOne(GeneralAccessor.ConditionGroup.or(conditions), Account.class);
        if (account == null) {
            throw new UserInterfaceRbacErrorException(UserInterfaceRbacErrorException.RbacErrors.ACCOUNT_NOT_FOUND);
        }
        if (!account.isValid()) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The account[%s] is invalid.", account.getId()));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.ACCOUNT_INVALID
            );
        }
        String digestPassword = DigestUtils.md5(password);
        if (!digestPassword.equals(account.getPassword())) {
            // 密码不匹配
            throw new UserInterfaceRbacErrorException(UserInterfaceRbacErrorException.RbacErrors.ACCOUNT_PASSWORD_NOT_MATCHED);
        }

        // 签发登录令牌
        String token = jwtService.signToken(accountCode);

        // 记录登录状态
        AccountState accountState = getAccountStateByAccountId(account.getId());
        if (accountState == null) {
            // 首次登录
            accountState = EntityFactory.createEntity(AccountState.class);
        }

        // 保存新的账户状态
        accountState.setAccount(account);
        accountState.setLoginTime(System.currentTimeMillis());
        accountState.setLogoutTime(null);
        accountState.setStatus("online");
        accountState.setToken(token);
        try {
            accessor.save(accountState);
            return accountState;
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Save account login state fail.", ex);
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.ACCOUNT_STATE_SAVE_FAIL
            );
        }
    }

    @Override
    public AccountState validateToken(String token) {
        if (StringUtils.isBlank(token)) {
            if (logger.isErrorEnabled()) {
                logger.error("The account's token is blank.");
            }
            throw new UserInterfaceEiimErrorException(UserInterfaceEiimErrorException.EiimErrors.ACCOUNT_BLANK_TOKEN);
        }
        JwtService.JwtVerifyResult result = jwtService.verifyToken(token);
        if (result.isPassed()) {
            // 验证通过
            String accountCode = result.getClaims().get("code").asString();
            Account account = accessor.getByCode(accountCode, Account.class);
            if (account == null) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("The account[%s] not found.", accountCode));
                }
                throw new UserInterfaceRbacErrorException(UserInterfaceRbacErrorException.RbacErrors.ACCOUNT_NOT_FOUND);
            }
            // 记录登录状态
            return getAccountStateByAccountId(account.getId());
        } else {
            // 验证失败
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The token[%s] is invalid.", token));
            }
            throw new UserInterfaceJwtErrorException(
                    UserInterfaceJwtErrorException.JwtErrors.JWT_VERIFY_FAIL
            );
        }
    }

    @Override
    public void logout(String accountId) {
        if (StringUtils.isBlank(accountId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The account's id is blank.");
            }
            throw new UserInterfaceSystemErrorException(UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM);
        }
        Account account = accessor.getById(accountId, Account.class);
        if (account == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The account[%s] not found.", accountId));
            }
            throw new UserInterfaceRbacErrorException(UserInterfaceRbacErrorException.RbacErrors.ACCOUNT_NOT_FOUND);
        }
        AccountState accountState = getAccountStateByAccountId(account.getId());
        if (accountState == null) {
            // 没有登录过，数据异常
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The account[%s] has not login record.", account.getCode()));
            }
            throw new UserInterfaceEiimErrorException(UserInterfaceEiimErrorException.EiimErrors.NO_LOGIN_RECORD);
        }
        accountState.setStatus("offline");
        accountState.setLogoutTime(System.currentTimeMillis());
        try {
            accessor.save(accountState);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Save account logout state fail.", ex);
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.ACCOUNT_STATE_SAVE_FAIL
            );
        }
    }

    @Override
    public Account valid(String accountId, boolean valid) {
        if (StringUtils.isBlank(accountId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The account's id is blank.");
            }
            throw new UserInterfaceSystemErrorException(UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM);
        }
        Account account = accessor.getById(accountId, Account.class);
        if (account == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The account[%s] not found.", accountId));
            }
            throw new UserInterfaceRbacErrorException(UserInterfaceRbacErrorException.RbacErrors.ACCOUNT_NOT_FOUND);
        }
        if (valid == account.isValid()) {
            if (logger.isWarnEnabled()) {
                logger.warn(String.format("The account[%s] is %s, input: %s.", account.getId(), account.isValid(), valid));
            }
        } else {
            account.setValid(valid);
            account = accessor.save(account);
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("The account[%s] is %s.", accountId, account.isValid()));
            }
        }
        return account;
    }

    @Override
    public void resetPassword(String accountId, String password) {
        if (StringUtils.isBlank(accountId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The account's id is blank.");
            }
            throw new UserInterfaceSystemErrorException(UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM);
        }
        Account account = accessor.getById(accountId, Account.class);
        if (account == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The account[%s] not found.", accountId));
            }
            throw new UserInterfaceRbacErrorException(UserInterfaceRbacErrorException.RbacErrors.ACCOUNT_NOT_FOUND);
        }
        if (!account.isValid()) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The account[%s] is invalid.", account.getId()));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.ACCOUNT_INVALID
            );
        }
        account.setPassword(DigestUtils.md5(password));
        accessor.save(account);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Reset the account[%s]'s password successfully.", accountId));
        }
    }

    @Override
    public Account getAccountById(String accountId) {
        if (StringUtils.isBlank(accountId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The account's id is blank.");
            }
            throw new UserInterfaceSystemErrorException(UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM);
        }
        return accessor.getById(accountId, Account.class);
    }

    @Override
    public List<AccountState> getAllAccountsStatus() {
        return accessor.list(AccountState.class);
    }
}
