package com.fs.eiim.restful.vo.person;

import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.Person;
import com.fs.eiim.service.BaseDataService;
import org.mx.StringUtils;
import org.mx.dal.EntityFactory;

public class PersonFormVO {
    private String id, firstName, lastName, title, phone, mobile, email, desc;
    private String accountId, nickname, avatar;

    public BaseDataService.PersonAccountTuple get() {
        Person person = EntityFactory.createEntity(Person.class);
        person.setId(id);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setTitle(title);
        person.setPhone(phone);
        person.setMobile(mobile);
        person.setEmail(email);
        person.setDesc(desc);

        Account account = null;
        if (!StringUtils.isBlank(accountId)) {
            account = EntityFactory.createEntity(Account.class);
            account.setId(accountId);
            account.setNickName(nickname);
            account.setAvatar(avatar);
        }
        return BaseDataService.PersonAccountTuple.valueOf(person, account);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
