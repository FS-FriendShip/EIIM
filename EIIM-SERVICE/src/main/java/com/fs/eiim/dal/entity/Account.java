package com.fs.eiim.dal.entity;

/**
 * 描述： 账户信息接口定义
 *
 * @author john peng
 * Date time 2018/8/8 下午1:19
 */
public interface Account extends org.mx.comps.rbac.dal.entity.Account {
    String getEiimCode();

    void setEiimCode(String eiimCode);

    String getNickName();

    void setNickName(String nickName);

    Person getPerson();

    void setPerson(Person person);

    String getAvatar();

    void setAvatar(String avatar);
}
