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
     * 添加一个字典数据项
     *
     * @param code  代码
     * @param name  名称
     * @param value 值
     * @return 字典数据项集合
     */
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
     * 添加一个字典数据项
     *
     * @param code       代码
     * @param name       名称
     * @param value      值
     * @param parentCode 父级字典项代码
     * @return 字典数据项集合
     */
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

    /**
     * 字典数据项类定义
     */
    private class BaseDataItemEntity implements BaseDataItem {
        private String code, name, value, parentCode;

        /**
         * 构造函数
         *
         * @param code  代码
         * @param name  名称
         * @param value 值
         */
        public BaseDataItemEntity(String code, String name, String value) {
            super();
            this.code = code;
            this.name = name;
            this.value = value;
        }

        /**
         * 构造函数
         *
         * @param code       代码
         * @param name       名称
         * @param value      值
         * @param parentCode 父级字典项代码
         */
        public BaseDataItemEntity(String code, String name, String value, String parentCode) {
            this(code, name, value);
            this.parentCode = parentCode;
        }

        /**
         * {@inheritDoc}
         *
         * @see BaseDataItem#getCode()
         */
        @Override
        public String getCode() {
            return code;
        }

        /**
         * {@inheritDoc}
         *
         * @see BaseDataItem#setCode(String)
         */
        @Override
        public void setCode(String code) {
            this.code = code;
        }

        /**
         * {@inheritDoc}
         *
         * @see BaseDataItem#getName()
         */
        @Override
        public String getName() {
            return name;
        }

        /**
         * {@inheritDoc}
         *
         * @see BaseDataItem#setName(String)
         */
        @Override
        public void setName(String name) {
            this.name = name;
        }

        /**
         * {@inheritDoc}
         *
         * @see BaseDataItem#getValue()
         */
        @Override
        public String getValue() {
            return value;
        }

        /**
         * {@inheritDoc}
         *
         * @see BaseDataItem#setValue(String)
         */
        @Override
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * {@inheritDoc}
         *
         * @see BaseDataItem#getParentCode()
         */
        @Override
        public String getParentCode() {
            return parentCode;
        }

        /**
         * {@inheritDoc}
         *
         * @see BaseDataItem#setParentCode(String)
         */
        @Override
        public void setParentCode(String parentCode) {
            this.parentCode = parentCode;
        }
    }
}
