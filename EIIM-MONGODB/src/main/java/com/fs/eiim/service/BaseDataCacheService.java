package com.fs.eiim.service;

import java.util.List;

/**
 * 描述： 基础数据缓存服务接口定义
 *
 * @author john peng
 * Date time 2018/8/19 下午1:10
 */
public interface BaseDataCacheService {
    List<BaseDataService.BaseDataItem> putBaseDataItemListCache(String categoryCode, List<BaseDataService.BaseDataItem> items);

    BaseDataService.BaseDataItem putBaseDataItemCache(String categoryCode, String code, BaseDataService.BaseDataItem item);
}
