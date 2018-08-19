package com.fs.eiim.service.impl;

import com.fs.eiim.service.BaseDataCacheService;
import com.fs.eiim.service.BaseDataService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 描述： 基础字典数据缓存服务类
 *
 * @author john peng
 * Date time 2018/8/19 下午1:12
 */
@Component("baseDataCacheService")
@CacheConfig(cacheNames = {"eiim"})
public class BaseDataCacheServiceImpl implements BaseDataCacheService {

    @CachePut(key = "'baseData.'.concat(#categoryCode)")
    @Override
    public List<BaseDataService.BaseDataItem> putBaseDataItemListCache(String categoryCode, List<BaseDataService.BaseDataItem> items) {
        return items;
    }

    @CachePut(key = "'baseData.'.concat(#categoryCode).concat(#code)")
    @Override
    public BaseDataService.BaseDataItem putBaseDataItemCache(String categoryCode, String code, BaseDataService.BaseDataItem item) {
        return item;
    }
}
