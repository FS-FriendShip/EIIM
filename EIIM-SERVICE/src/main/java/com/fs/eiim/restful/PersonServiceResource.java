package com.fs.eiim.restful;

import com.fs.eiim.restful.vo.account.AccountInfoVO;
import com.fs.eiim.restful.vo.account.PasswordInfoVO;
import com.fs.eiim.restful.vo.person.AccountInitialInfoVO;
import com.fs.eiim.restful.vo.person.PersonFormVO;
import com.fs.eiim.restful.vo.person.PersonInfoVO;
import com.fs.eiim.service.BaseDataService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.StringUtils;
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

    private BaseDataService baseDataService;
    private SessionDataStore sessionDataStore;

    @Autowired
    public PersonServiceResource(BaseDataService baseDataService, SessionDataStore sessionDataStore) {
        super();
        this.baseDataService = baseDataService;
        this.sessionDataStore = sessionDataStore;
    }

    @Path("persons")
    @GET
    @RestAuthenticate
    public DataVO<List<PersonInfoVO>> getAllPersons() {
        List<BaseDataService.PersonAccountTuple> persons = baseDataService.getAllPersons();
        return new DataVO<>(PersonInfoVO.valueOf(persons));
    }

    private DataVO<PersonInfoVO> savePerson(PersonFormVO personFormVO) {
        if (personFormVO == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        BaseDataService.PersonAccountTuple tuple = baseDataService.savePersonInfo(personFormVO.get());
        sessionDataStore.removeCurrentUserCode();
        return new DataVO<>(PersonInfoVO.valueOf(tuple));
    }

    @Path("persons/new")
    @POST
    @RestAuthenticate
    public DataVO<PersonInfoVO> newPerson(@QueryParam("accountCode") String accountCode, PersonFormVO personFormVO) {
        if (logger.isWarnEnabled()) {
            logger.warn(String.format("The person's id need blank string, but it is '%s'.", personFormVO.getId()));
        }
        personFormVO.setId(null);
        sessionDataStore.setCurrentUserCode(accountCode);
        return savePerson(personFormVO);
    }

    @Path("persons/{personId}")
    @PUT
    @RestAuthenticate
    public DataVO<PersonInfoVO> modifyPerson(@PathParam("personId") String personId,
                                             @QueryParam("accountCode") String accountCode,
                                             PersonFormVO personFormVO) {
        if (StringUtils.isBlank(personId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The person's id is blank.");
            }
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        personFormVO.setId(personId);
        sessionDataStore.setCurrentUserCode(accountCode);
        return savePerson(personFormVO);
    }

    @Path("persons/{personId}")
    @GET
    @RestAuthenticate
    public DataVO<PersonInfoVO> getPerson(@PathParam("personId") String personId) {
        if (StringUtils.isBlank(personId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The person's id is blank.");
            }
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        BaseDataService.PersonAccountTuple tuple = baseDataService.getPersonInfo(personId);
        return new DataVO<>(PersonInfoVO.valueOf(tuple));
    }

    @Path("persons/{personId}/enable")
    @POST
    @RestAuthenticate
    public DataVO<AccountInfoVO> enablePersonAccount(@PathParam("personId") String personId,
                                                     @QueryParam("accountCode") String accountCode,
                                                     AccountInitialInfoVO accountInitialInfoVO) {
        if (StringUtils.isBlank(personId) || accountInitialInfoVO == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        sessionDataStore.setCurrentUserCode(accountCode);
        BaseDataService.PersonAccountTuple tuple = baseDataService.enablePersonAccount(personId,
                accountInitialInfoVO.get());
        sessionDataStore.removeCurrentUserCode();
        return new DataVO<>(AccountInfoVO.valueOf(tuple.getAccount(), tuple.getAccountState()));
    }

    @Path("persons/account/password")
    @PUT
    @RestAuthenticate
    public DataVO<Boolean> changeAccountPassword(@QueryParam("accountCode") String accountCode, PasswordInfoVO passwordInfoVO) {
        if (passwordInfoVO == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        sessionDataStore.setCurrentUserCode(accountCode);
        baseDataService.changeAccountPassword(passwordInfoVO.getAccountCode(), passwordInfoVO.getOldPassword(),
                passwordInfoVO.getNewPassword());
        sessionDataStore.removeCurrentUserCode();
        return new DataVO<>(true);
    }
}
