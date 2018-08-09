package com.fs.eiim.dal.entity;

import org.mx.comps.rbac.dal.entity.Department;

/**
 * 描述： 组织单位信息接口定义，为树状字典
 *
 * @author john peng
 * Date time 2018/8/8 下午1:19
 */
public interface Org extends Department {
    String getType();
    void setType(String type);
}
