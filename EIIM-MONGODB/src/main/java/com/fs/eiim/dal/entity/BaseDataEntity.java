package com.fs.eiim.dal.entity;

import org.mx.dal.entity.MongoBaseDictEntity;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 描述： 基础数据实体类定义
 *
 * @author john peng
 * Date time 2018/8/8 下午3:54
 */
@Document(collection = "basedata")
public class BaseDataEntity extends MongoBaseDictEntity implements BaseData {
    private String parentCode, value;

    /**
     * {@inheritDoc}
     *
     * @see BaseData#getParentCode()
     */
    @Override
    public String getParentCode() {
        return parentCode;
    }

    /**
     * {@inheritDoc}
     *
     * @see BaseData#setParentCode(String)
     */
    @Override
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    /**
     * {@inheritDoc}
     *
     * @see BaseData#getValue()
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     *
     * @see BaseData#setValue(String)
     */
    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
