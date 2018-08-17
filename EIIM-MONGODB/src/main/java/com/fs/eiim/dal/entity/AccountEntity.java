package com.fs.eiim.dal.entity;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 描述： EIIM账户实体扩展类，从 {@link org.mx.comps.rbac.dal.entity.AccountEntity} 继承而来。
 *
 * @author john peng
 * Date time 2018/8/8 下午3:01
 */
@Document(collection = "account")
public class AccountEntity extends org.mx.comps.rbac.dal.entity.AccountEntity implements Account {
    private String eiimCode, nickName, avatar;
    @DBRef
    private Person person;

    /**
     * {@inheritDoc}
     *
     * @see Account#getEiimCode()
     */
    @Override
    public String getEiimCode() {
        return eiimCode;
    }

    /**
     * {@inheritDoc}
     *
     * @see Account#setEiimCode(String)
     */
    @Override
    public void setEiimCode(String eiimCode) {
        this.eiimCode = eiimCode;
    }

    /**
     * {@inheritDoc}
     *
     * @see Account#getNickName()
     */
    @Override
    public String getNickName() {
        return nickName;
    }

    /**
     * {@inheritDoc}
     *
     * @see Account#setNickName(String)
     */
    @Override
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * {@inheritDoc}
     *
     * @see Account#getPerson()
     */
    @Override
    public Person getPerson() {
        return person;
    }

    /**
     * {@inheritDoc}
     *
     * @see Account#setPerson(Person)
     */
    @Override
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * {@inheritDoc}
     *
     * @see Account#getAvatar()
     */
    @Override
    public String getAvatar() {
        return avatar;
    }

    /**
     * {@inheritDoc}
     *
     * @see Account#setAvatar(String)
     */
    @Override
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
