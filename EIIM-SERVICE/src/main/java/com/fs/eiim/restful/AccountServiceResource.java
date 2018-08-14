package com.fs.eiim.restful;

import com.fs.eiim.dal.entity.AccountState;
import com.fs.eiim.restful.vo.account.AccountStateVO;
import com.fs.eiim.restful.vo.account.AuthenticationVO;
import com.fs.eiim.service.AccountService;
import org.mx.comps.rbac.error.UserInterfaceRbacErrorException;
import org.mx.error.UserInterfaceException;
import org.mx.error.UserInterfaceSystemErrorException;
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

    @Autowired
    public AccountServiceResource(AccountService accountService) {
        super();
        this.accountService = accountService;
    }

    @Path("login")
    @POST
    public DataVO<AccountStateVO> login(AuthenticationVO authenticate) {
        if (authenticate == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        try {
            AccountState accountState = accountService.login(authenticate.getAccountCode(),
                    authenticate.getPassword());
            return new DataVO<>(AccountStateVO.valueOf(accountState));
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            return new DataVO<>(new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.RBAC_OTHER_FAIL
            ));
        }
    }

    @Path("logout/{accountId}")
    @GET
    public DataVO<Boolean> logout(@PathParam("accountId") String accountId) {
        try {
            accountService.logout(accountId);
            return new DataVO<>(true);
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            return new DataVO<>(new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.RBAC_OTHER_FAIL
            ));
        }
    }

    @Path("accounts/status")
    @GET
    public DataVO<List<AccountStateVO>> getAllAccountsStatus() {
        try {
            List<AccountState> states = accountService.getAllAccountsStatus();
            return new DataVO<>(AccountStateVO.valueOf(states));
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            return new DataVO<>(new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.RBAC_OTHER_FAIL
            ));
        }
    }
}
