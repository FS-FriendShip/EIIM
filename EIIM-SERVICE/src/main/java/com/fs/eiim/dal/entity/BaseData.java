package com.fs.eiim.dal.entity;

import org.mx.dal.entity.BaseDict;

/**
 * 描述： 基础数据字典接口定义
 *
 * @author john peng
 * Date time 2018/8/8 下午12:57
 */
public interface BaseData extends BaseDict {
    /**
     * 获取父级代码
     *
     * @return 父级代码
     */
    String getParentCode();

    /**
     * 设置父级代码
     *
     * @param parentCode 父级代码
     */
    void setParentCode(String parentCode);

    /**
     * 获取基础数据的值
     *
     * @return 值
     */
    String getValue();

    /**
     * 设置基础数据的值
     *
     * @param value 值
     */
    void setValue(String value);
}
