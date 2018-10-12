package com.fs.eiim.restful;

import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.AccountState;
import com.fs.eiim.restful.vo.account.AccountInfoVO;
import com.fs.eiim.restful.vo.account.AccountStateVO;
import com.fs.eiim.restful.vo.account.AuthenticationVO;
import com.fs.eiim.service.AccountService;
import org.mx.dal.session.SessionDataStore;
import org.mx.error.UserInterfaceSystemErrorException;
import org.mx.service.rest.auth.RestAuthenticate;
import org.mx.service.rest.vo.DataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 描述： 账户服务相关的RESTful资源定义
 *
 * @author john peng
 * Date time 2018/8/12 下午2:24
 */
@Component("accountServiceResource")
@Path("rest/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountServiceResource {
    private AccountService accountService;
    private SessionDataStore sessionDataStore;

    @Autowired
    public AccountServiceResource(AccountService accountService, SessionDataStore sessionDataStore) {
        super();
        this.accountService = accountService;
        this.sessionDataStore = sessionDataStore;
    }

    @Path("login")
    @POST
    public DataVO<AccountStateVO> login(AuthenticationVO authenticate) {
        if (authenticate == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        sessionDataStore.setCurrentUserCode(authenticate.getAccountCode());
        AccountState accountState = accountService.login(authenticate.getAccountCode(),
                authenticate.getPassword());
        sessionDataStore.removeCurrentUserCode();
        return new DataVO<>(AccountStateVO.valueOf(accountState));
    }

    @Path("logout/{accountId}")
    @RestAuthenticate
    @GET
    public DataVO<Boolean> logout(@PathParam("accountId") String accountId,
                                  @QueryParam("accountCode") String accountCode) {
        sessionDataStore.setCurrentUserCode(accountCode);
        accountService.logout(accountId);
        sessionDataStore.removeCurrentUserCode();
        return new DataVO<>(true);
    }

    @Path("accounts/{accountId}/invalid")
    @RestAuthenticate
    @GET
    public DataVO<AccountInfoVO> invalid(@PathParam("accountId") String accountId,
                                         @QueryParam("accountCode") String accountCode) {
        sessionDataStore.setCurrentUserCode(accountCode);
        Account account = accountService.invalid(accountId);
        sessionDataStore.removeCurrentUserCode();
        return new DataVO<>(AccountInfoVO.valueOf(account, null));
    }

    @Path("accounts/status")
    @GET
    @RestAuthenticate
    public DataVO<List<AccountStateVO>> getAllAccountsStatus() {
        List<AccountState> states = accountService.getAllAccountsStatus();
        return new DataVO<>(AccountStateVO.valueOf(states));
    }
}
