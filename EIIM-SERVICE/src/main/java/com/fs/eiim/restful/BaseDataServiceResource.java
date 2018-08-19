package com.fs.eiim.restful;

import com.fs.eiim.error.UserInterfaceEiimErrorException;
import com.fs.eiim.restful.vo.baseData.BaseDataItemVO;
import com.fs.eiim.restful.vo.baseData.BaseDataVO;
import com.fs.eiim.service.BaseDataService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.error.UserInterfaceException;
import org.mx.service.rest.vo.DataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 描述： 基础字典数据服务RESTful资源
 *
 * @author john peng
 * Date time 2018/8/19 上午9:56
 */
@Component("baseDataServiceResource")
@Path("/rest/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BaseDataServiceResource {
    private static final Log logger = LogFactory.getLog(BaseDataServiceResource.class);

    private BaseDataService baseDataService;

    @Autowired
    public BaseDataServiceResource(BaseDataService baseDataService) {
        super();
        this.baseDataService = baseDataService;
    }

    @Path("baseDatas")
    @GET
    public DataVO<List<BaseDataVO>> getAllBaseDatas() {
        try {
            List<BaseDataService.BaseData> baseDatas = baseDataService.getAllBaseData();
            return new DataVO<>(BaseDataVO.valueOf(baseDatas));
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Get all base data fail.", ex);
            }
            return new DataVO<>(new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.EIIM_OTHER_FAIL
            ));
        }
    }

    @Path("baseDatas/{baseDataCategoryCode}")
    @GET
    public DataVO<List<BaseDataItemVO>> getBaseDataItems(@PathParam("baseDataCategoryCode") String baseDataCategoryCode) {
        try {
            List<BaseDataService.BaseDataItem> baseDataItems = baseDataService.getBaseDataItems(baseDataCategoryCode);
            return new DataVO<>(BaseDataItemVO.valueOf(baseDataItems));
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("Get all base data[%s]'s items fail.", baseDataCategoryCode), ex);
            }
            return new DataVO<>(new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.EIIM_OTHER_FAIL
            ));
        }
    }

    @Path("baseDatas/{baseDataCategoryCode}/items/{code}")
    @GET
    public DataVO<BaseDataItemVO> getBaseDataItems(@PathParam("baseDataCategoryCode") String baseDataCategoryCode,
                                                   @PathParam("code") String code) {
        try {
            BaseDataService.BaseDataItem item = baseDataService.getBaseDataItem(baseDataCategoryCode, code);
            return new DataVO<>(BaseDataItemVO.valueOf(item));
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("Get all base data[%s]'s item[%s] fail.", baseDataCategoryCode, code), ex);
            }
            return new DataVO<>(new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.EIIM_OTHER_FAIL
            ));
        }
    }
}
