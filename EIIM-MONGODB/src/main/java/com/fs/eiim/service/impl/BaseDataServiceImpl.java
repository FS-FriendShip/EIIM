package com.fs.eiim.service.impl;

import com.fs.eiim.dal.entity.BaseData;
import com.fs.eiim.dal.entity.BaseDataItem;
import com.fs.eiim.dal.entity.Org;
import com.fs.eiim.service.BaseDataService;
import org.mx.dal.service.GeneralAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述： 基础数据服务实现类定义
 *
 * @author john peng
 * Date time 2018/8/11 下午6:39
 */
@Component("baseDataService")
@CacheConfig(cacheNames = {"eiim"})
public class BaseDataServiceImpl implements BaseDataService {
    private GeneralAccessor accessor;

    /**
     * 构造函数
     *
     * @param accessor 数据库访问接口
     */
    @Autowired
    public BaseDataServiceImpl(@Qualifier("generalAccessorMongodb") GeneralAccessor accessor) {
        super();
        this.accessor = accessor;
    }

    /**
     * {@inheritDoc}
     *
     * @see BaseDataService#getAllBaseData()
     */
    @Cacheable(key = "'baseData'")
    @Override
    public Map<String, BaseDataItem> getAllBaseData() {
        List<BaseData> baseDatas = accessor.list(BaseData.class, true);
        Map<String, BaseDataItem> map = new HashMap<>(baseDatas.size());
        for (BaseData baseData : baseDatas) {
            for (BaseDataItem item : baseData.getItems()) {
                String key = String.format("%s-%s", baseData.getCode(), item.getCode());
                map.put(key, item);
            }
        }
        return map;
    }

    @Override
    public List<Org> getAllOrgs() {
        // TODO
        return null;
    }

    @Override
    public Org saveOrgInfo(Org org) {
        // TODO
        return null;
    }

    @Override
    public Org getOrgInfo(String orgId) {
        // TODO
        return null;
    }

    @Override
    public List<PersonAccountTuple> getAllPersons() {
        // TODO
        return null;
    }

    @Override
    public PersonAccountTuple savePersonInfo(PersonAccountTuple tuple) {
        // TODO
        return null;
    }

    @Override
    public PersonAccountTuple getPersonInfo(String personId) {
        // TODO
        return null;
    }

    @Override
    public PersonAccountTuple enablePersonAccount(String personId) {
        // TODO
        return null;
    }

    @Override
    public void changeAccountPassword(String accountCode, String oldPassword, String newPassword) {
        // TODO
    }
}
