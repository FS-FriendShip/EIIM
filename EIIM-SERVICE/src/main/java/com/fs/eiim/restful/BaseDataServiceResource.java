package com.fs.eiim.restful;

import com.fs.eiim.restful.vo.baseData.BaseDataItemVO;
import com.fs.eiim.restful.vo.baseData.BaseDataVO;
import com.fs.eiim.service.BaseDataService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.service.rest.auth.RestAuthenticate;
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
    @RestAuthenticate
    public DataVO<List<BaseDataVO>> getAllBaseDatas() {
        List<BaseDataService.BaseData> baseDatas = baseDataService.getAllBaseData();
        return new DataVO<>(BaseDataVO.valueOf(baseDatas));
    }

    @Path("baseDatas/{baseDataCategoryCode}")
    @GET
    @RestAuthenticate
    public DataVO<List<BaseDataItemVO>> getBaseDataItems(@PathParam("baseDataCategoryCode") String baseDataCategoryCode) {
        List<BaseDataService.BaseDataItem> baseDataItems = baseDataService.getBaseDataItems(baseDataCategoryCode);
        return new DataVO<>(BaseDataItemVO.valueOf(baseDataItems));
    }

    @Path("baseDatas/{baseDataCategoryCode}/items/{code}")
    @GET
    @RestAuthenticate
    public DataVO<BaseDataItemVO> getBaseDataItems(@PathParam("baseDataCategoryCode") String baseDataCategoryCode,
                                                   @PathParam("code") String code) {
        BaseDataService.BaseDataItem item = baseDataService.getBaseDataItem(baseDataCategoryCode, code);
        return new DataVO<>(BaseDataItemVO.valueOf(item));
    }
}
