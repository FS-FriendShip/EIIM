package com.fs.eiim.dal.entity;

import org.mx.dal.entity.BaseDict;

import java.util.Set;

/**
 * 描述： 基础数据分类字典接口定义
 *
 * @author john peng
 * Date time 2018/8/8 下午12:57
 */
public interface BaseData extends BaseDict {
    /**
     * 添加一个字典数据项
     *
     * @param code  代码
     * @param name  名称
     * @param value 值
     * @return 字典数据项集合
     */
    Set<? extends BaseDataItem> addItem(String code, String name, String value);

    /**
     * 添加一个字典数据项
     *
     * @param code       代码
     * @param name       名称
     * @param value      值
     * @param parentCode 父级字典项代码
     * @return 字典数据项集合
     */
    Set<? extends BaseDataItem> addItem(String code, String name, String value, String parentCode);

    /**
     * 获取基础数据字典项集合
     *
     * @return 技术数据字典项集合
     */
    Set<? extends BaseDataItem> getItems();

    /**
     * 设置基础数据字典项集合
     *
     * @param items 基础数据字典项集合
     */
    void setItems(Set<BaseDataItem> items);
}
