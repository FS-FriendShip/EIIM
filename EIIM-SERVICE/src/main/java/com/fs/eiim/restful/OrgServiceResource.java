package com.fs.eiim.restful;

import com.fs.eiim.dal.entity.Org;
import com.fs.eiim.restful.vo.org.OrgFormVO;
import com.fs.eiim.restful.vo.org.OrgInfoVO;
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
 * 描述： 组织单位部门服务的RESTful资源定义
 *
 * @author john peng
 * Date time 2018/8/12 下午8:06
 */
@Component("orgServiceResource")
@Path("rest/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrgServiceResource {
    private static final Log logger = LogFactory.getLog(OrgServiceResource.class);

    private BaseDataService baseDataService;
    private SessionDataStore sessionDataStore;

    @Autowired
    public OrgServiceResource(BaseDataService baseDataService, SessionDataStore sessionDataStore) {
        super();
        this.baseDataService = baseDataService;
        this.sessionDataStore = sessionDataStore;
    }

    @Path("orgs")
    @GET
    @RestAuthenticate
    public DataVO<List<OrgInfoVO>> getAllOrgs() {
        List<Org> orgs = baseDataService.getAllOrgs();
        return new DataVO<>(OrgInfoVO.valueOf(orgs));
    }

    private DataVO<OrgInfoVO> saveOrg(OrgFormVO orgFormVO) {
        if (orgFormVO == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        BaseDataService.OrgInfo org = baseDataService.saveOrgInfo(orgFormVO.get());
        sessionDataStore.removeCurrentUserCode();
        return new DataVO<>(OrgInfoVO.valueOf(org));
    }

    @Path("orgs/new")
    @POST
    @RestAuthenticate
    public DataVO<OrgInfoVO> newOrg(@QueryParam("accountCode") String accountCode, OrgFormVO orgFormVO) {
        orgFormVO.setId(null);
        sessionDataStore.setCurrentUserCode(accountCode);
        return saveOrg(orgFormVO);
    }

    @Path("orgs/{orgId}")
    @PUT
    @RestAuthenticate
    public DataVO<OrgInfoVO> modifyOrg(@PathParam("orgId") String orgId, @QueryParam("accountCode") String accountCode,
                                       OrgFormVO orgFormVO) {
        if (StringUtils.isBlank(orgId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The organization's id is blank.");
            }
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        orgFormVO.setId(orgId);
        sessionDataStore.setCurrentUserCode(accountCode);
        return saveOrg(orgFormVO);
    }

    @Path("orgs/{orgId}")
    @GET
    @RestAuthenticate
    public DataVO<OrgInfoVO> getOrg(@PathParam("orgId") String orgId) {
        if (StringUtils.isBlank(orgId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The organization's id is blank.");
            }
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        BaseDataService.OrgInfo org = baseDataService.getOrgInfo(orgId);
        return new DataVO<>(OrgInfoVO.valueOf(org));
    }
}
