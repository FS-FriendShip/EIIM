package com.fs.eiim.dal.entity;

/**
 * 描述： 字典项类定义
 *
 * @author john peng
 * Date time 2018/8/17 下午1:44
 */
public class BaseDataItemEntity implements BaseDataItem {
    private String code, name, value, parentCode;

    /**
     * 默认的构造函数
     */
    public BaseDataItemEntity() {
        super();
    }

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