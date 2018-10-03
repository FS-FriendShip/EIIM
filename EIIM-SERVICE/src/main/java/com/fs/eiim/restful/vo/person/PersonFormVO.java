package com.fs.eiim.restful.vo.person;

import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.Org;
import com.fs.eiim.dal.entity.Person;
import com.fs.eiim.service.BaseDataService;
import org.mx.StringUtils;
import org.mx.comps.rbac.dal.entity.User;
import org.mx.dal.EntityFactory;

public class PersonFormVO {
    private String id, orgId, firstName, lastName, fullName, title, phone, mobile, email, desc;
    private String accountId, nickname, avatar;
    private User.Sex sex = User.Sex.NA;

    public BaseDataService.PersonAccountTuple get() {
        Person person = EntityFactory.createEntity(Person.class);
        person.setId(id);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setFullName(fullName);
        person.setTitle(title);
        person.setPhone(phone);
        person.setMobile(mobile);
        person.setEmail(email);
        person.setDesc(desc);
        person.setSex(sex);

        if (!StringUtils.isBlank(orgId)) {
            Org org = EntityFactory.createEntity(Org.class);
            org.setId(orgId);
            person.setOrganization(org);
        }

        Account account = null;
        if (!StringUtils.isBlank(accountId)) {
            account = EntityFactory.createEntity(Account.class);
            account.setId(accountId);
            account.setNickName(nickname);
            account.setAvatar(avatar);
        }
        return BaseDataService.PersonAccountTuple.valueOf(person, account, null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public User.Sex getSex() {
        return sex;
    }

    public void setSex(User.Sex sex) {
        this.sex = sex;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
