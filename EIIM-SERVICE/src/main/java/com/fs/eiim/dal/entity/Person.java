package com.fs.eiim.dal.entity;

import org.mx.comps.rbac.dal.entity.User;

/**
 * 描述： 用户人员信息接口定义
 *
 * @author john peng
 * Date time 2018/8/8 下午1:03
 */
public interface Person extends User {
    Org getOrganization();
    void setOrganization(Org org);
    String getTitle();
    void setTitle(String title);
    String getPhone();
    void setPhone(String phone);
    String getMobile();
    void setMobile(String mobile);
    String getEmail();
    void setEmail(String email);
}
