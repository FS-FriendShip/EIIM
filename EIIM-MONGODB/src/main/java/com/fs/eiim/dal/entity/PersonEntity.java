package com.fs.eiim.dal.entity;

import org.mx.comps.rbac.dal.entity.UserEntity;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 描述： EIIM人员信息实体类定义
 *
 * @author john peng
 * Date time 2018/8/8 下午4:16
 */
@Document(collection = "person")
public class PersonEntity extends UserEntity implements Person {
    @DBRef
    private Org organization;
    private String title, phone, mobile, email;

    /**
     * {@inheritDoc}
     *
     * @see Person#getOrganization()
     */
    @Override
    public Org getOrganization() {
        return organization;
    }

    /**
     * {@inheritDoc}
     *
     * @see Person#setOrganization(Org)
     */
    @Override
    public void setOrganization(Org org) {
        this.organization = org;
    }

    /**
     * {@inheritDoc}
     *
     * @see Person#getTitle()
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * {@inheritDoc}
     *
     * @see Person#setTitle(String)
     */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * {@inheritDoc}
     *
     * @see Person#getPhone()
     */
    @Override
    public String getPhone() {
        return phone;
    }

    /**
     * {@inheritDoc}
     *
     * @see Person#setPhone(String)
     */
    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * {@inheritDoc}
     *
     * @see Person#getMobile()
     */
    @Override
    public String getMobile() {
        return mobile;
    }

    /**
     * {@inheritDoc}
     *
     * @see Person#setMobile(String)
     */
    @Override
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * {@inheritDoc}
     *
     * @see Person#getEmail()
     */
    @Override
    public String getEmail() {
        return email;
    }

    /**
     * {@inheritDoc}
     *
     * @see Person#setEmail(String)
     */
    @Override
    public void setEmail(String email) {
        this.email = email;
    }
}
