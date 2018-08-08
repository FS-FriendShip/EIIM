package com.fs.eiim.dal.entity;

import org.mx.dal.entity.BaseDictTree;

import java.util.Set;

/**
 * 描述： 组织单位信息接口定义，为树状字典
 *
 * @author john peng
 * Date time 2018/8/8 下午1:19
 */
public interface Org extends BaseDictTree {
    String getType();
    void setType(String type);
    Person getManager();
    void setManager(Person manager);
    Set<Person> getEmployees();
    void setEmployees(Set<Person> employees);
}
