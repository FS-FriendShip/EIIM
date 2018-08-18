package com.fs.eiim.restful.vo.person;

import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.Person;
import com.fs.eiim.restful.vo.account.AccountInfoVO;
import com.fs.eiim.service.BaseDataService;
import org.mx.comps.rbac.dal.entity.User;

import java.util.ArrayList;
import java.util.List;

public class PersonInfoVO {
    private String id, name, title, phone, mobile, email, desc;
    private boolean hasAccount = false;
    private User.Sex sex = User.Sex.NA;
    private AccountInfoVO account;

    public static PersonInfoVO valueOf(User user, Account account) {
        if (user == null) {
            return null;
        }
        PersonInfoVO personInfoVO = new PersonInfoVO();
        personInfoVO.id = user.getId();
        personInfoVO.name = user.getFullName();
        personInfoVO.desc = user.getDesc();
        personInfoVO.sex = user.getSex();
        personInfoVO.hasAccount = account != null;
        if (account != null) {
            personInfoVO.account = AccountInfoVO.valueOf(account);
        }
        return personInfoVO;
    }

    public static PersonInfoVO valueOf(Person person, Account account) {
        if (person == null) {
            return null;
        }
        PersonInfoVO personInfoVO = new PersonInfoVO();
        personInfoVO.id = person.getId();
        personInfoVO.name = person.getFullName();
        personInfoVO.title = person.getTitle();
        personInfoVO.phone = person.getPhone();
        personInfoVO.mobile = person.getMobile();
        personInfoVO.email = person.getEmail();
        personInfoVO.desc = person.getDesc();
        personInfoVO.sex = person.getSex();
        personInfoVO.hasAccount = account != null;
        if (account != null) {
            personInfoVO.account = AccountInfoVO.valueOf(account);
        }
        return personInfoVO;
    }

    public static List<PersonInfoVO> valueOf(List<BaseDataService.PersonAccountTuple> tuples) {
        List<PersonInfoVO> persons = new ArrayList<>();
        if (tuples != null && !tuples.isEmpty()) {
            tuples.forEach(tuple -> persons.add(PersonInfoVO.valueOf(tuple.getPerson(), tuple.getAccount())));
        }
        return persons;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean hasAccount() {
        return hasAccount;
    }

    public void setHasAccount(boolean hasAccount) {
        this.hasAccount = hasAccount;
    }

    public User.Sex getSex() {
        return sex;
    }

    public void setSex(User.Sex sex) {
        this.sex = sex;
    }

    public AccountInfoVO getAccount() {
        return account;
    }

    public void setAccount(AccountInfoVO account) {
        this.account = account;
    }
}
