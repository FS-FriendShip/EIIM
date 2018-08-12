package com.fs.eiim.restful;

import com.fs.eiim.restful.vo.account.PasswordInfoVO;
import com.fs.eiim.restful.vo.person.PersonFormVO;
import com.fs.eiim.restful.vo.person.PersonInfoVO;
import com.fs.eiim.service.AccountService;
import com.fs.eiim.service.BaseDataService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.StringUtils;
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
 * 描述： 人员账户管理服务RESTful资源定义
 *
 * @author john peng
 * Date time 2018/8/12 下午3:20
 */
@Component("personServiceResource")
@Path("rest/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonServiceResource {
    private static final Log logger = LogFactory.getLog(PersonServiceResource.class);

    private AccountService accountService;
    private BaseDataService baseDataService;

    @Autowired
    public PersonServiceResource(AccountService accountService, BaseDataService baseDataService) {
        super();
        this.accountService = accountService;
        this.baseDataService = baseDataService;
    }

    @Path("persons")
    @GET
    public DataVO<List<PersonInfoVO>> getAllPersons() {
        try {
            List<BaseDataService.PersonAccountTuple> persons = baseDataService.getAllPersons();
            return new DataVO<>(PersonInfoVO.valueOf(persons));
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            return new DataVO<>(new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.RBAC_OTHER_FAIL
            ));
        }
    }

    private DataVO<PersonInfoVO> savePerson(PersonFormVO personFormVO) {
        if (personFormVO == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        try {
            BaseDataService.PersonAccountTuple tuple = baseDataService.savePersonInfo(personFormVO.get());
            return new DataVO<>(PersonInfoVO.valueOf(tuple.getPerson(), tuple.getAccount()));
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            return new DataVO<>(new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.RBAC_OTHER_FAIL
            ));
        }
    }

    @Path("persons/new")
    @POST
    public DataVO<PersonInfoVO> newPerson(PersonFormVO personFormVO) {
        if (logger.isWarnEnabled()) {
            logger.warn(String.format("The person's id need blank string, but it is '%s'.", personFormVO.getId()));
        }
        personFormVO.setId(null);
        return savePerson(personFormVO);
    }

    @Path("persons/{personId}")
    @PUT
    public DataVO<PersonInfoVO> modifyPerson(@PathParam("personId") String personId, PersonFormVO personFormVO) {
        if (StringUtils.isBlank(personId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The person's id is blank.");
            }
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        personFormVO.setId(personId);
        return savePerson(personFormVO);
    }

    @Path("persons/{personId}")
    @GET
    public DataVO<PersonInfoVO> getPerson(@PathParam("personId") String personId) {
        if (StringUtils.isBlank(personId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The person's id is blank.");
            }
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        try {
            BaseDataService.PersonAccountTuple tuple = baseDataService.getPersonInfo(personId);
            return new DataVO<>(PersonInfoVO.valueOf(tuple.getPerson(), tuple.getAccount()));
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            return new DataVO<>(new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.RBAC_OTHER_FAIL
            ));
        }
    }

    @Path("persons/{personId}/enable")
    @GET
    public DataVO<PersonInfoVO> enablePersonAccount(@PathParam("personId") String personId) {
        if (StringUtils.isBlank(personId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The person's id is blank.");
            }
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        try {
            BaseDataService.PersonAccountTuple tuple = baseDataService.enablePersonAccount(personId);
            return new DataVO<>(PersonInfoVO.valueOf(tuple.getPerson(), tuple.getAccount()));
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            return new DataVO<>(new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.RBAC_OTHER_FAIL
            ));
        }
    }

    @Path("persons/accounts/password")
    @PUT
    public DataVO<Boolean> changeAccountPassword(PasswordInfoVO passwordInfoVO) {
        if (passwordInfoVO == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        try {
            baseDataService.changeAccountPassword(passwordInfoVO.getAccountId(), passwordInfoVO.getOldPassword(),
                    passwordInfoVO.getNewPassword());
            return new DataVO<>(true);
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            return new DataVO<>(new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.RBAC_OTHER_FAIL
            ));
        }
    }
}
