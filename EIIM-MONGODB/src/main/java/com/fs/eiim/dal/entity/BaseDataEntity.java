package com.fs.eiim.dal.entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.StringUtils;
import org.mx.dal.entity.MongoBaseDictEntity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

/**
 * 描述： 基础数据实体类定义
 *
 * @author john peng
 * Date time 2018/8/8 下午3:54
 */
@Document(collection = "basedata")
public class BaseDataEntity extends MongoBaseDictEntity implements BaseData {
    private static final Log logger = LogFactory.getLog(BaseDataEntity.class);
    private Set<BaseDataItem> items = new HashSet<>();

    /**
     * {@inheritDoc}
     *
     * @see BaseData#addItem(String, String, String)
     */
    @Override
    public Set<? extends BaseDataItem> addItem(String code, String name, String value) {
        if (items == null) {
            items = new HashSet<>();
        }
        if (!StringUtils.isBlank(code) && !StringUtils.isBlank(value)) {
            items.add(new BaseDataItemEntity(code, name, value));
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn(String.format("Any parameter invalid, code: %s, name: %s, value: %s.", code, name, value));
            }
        }
        return items;
    }

    /**
     * {@inheritDoc}
     *
     * @see BaseData#addItem(String, String, String, String)
     */
    @Override
    public Set<? extends BaseDataItem> addItem(String code, String name, String value, String parentCode) {
        if (items == null) {
            items = new HashSet<>();
        }
        if (!StringUtils.isBlank(code) && !StringUtils.isBlank(value) && !StringUtils.isBlank(parentCode)) {
            items.add(new BaseDataItemEntity(code, name, value, parentCode));
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn(String.format("Any parameter invalid, code: %s, name: %s, value: %s, parent code: %s.", code, name, value, parentCode));
            }
        }
        return items;
    }

    /**
     * {@inheritDoc}
     *
     * @see BaseData#getItems()
     */
    @Override
    public Set<? extends BaseDataItem> getItems() {
        return items;
    }

    /**
     * {@inheritDoc}
     *
     * @see BaseData#setItems(Set)
     */
    @Override
    public void setItems(Set<BaseDataItem> items) {
        this.items = items;
    }
}
