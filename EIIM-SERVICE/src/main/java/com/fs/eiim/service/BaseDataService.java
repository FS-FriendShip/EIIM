package com.fs.eiim.service;

import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.AccountState;
import com.fs.eiim.dal.entity.Org;
import com.fs.eiim.dal.entity.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述： 基础数据服务接口定义
 *
 * @author john peng
 * Date time 2018/8/11 下午6:37
 */
public interface BaseDataService {
    String uuidMale = "5ba75ab696a54e2a9005968b", uuidFemale = "5ba75abf96a54e2a9005968c";

    /**
     * 获取所有的字典数据项
     *
     * @return 字典数据项集合，Key形如："字典代码-字典项代码"
     */
    List<BaseData> getAllBaseData();

    List<BaseDataItem> getBaseDataItems(String categoryCode);

    BaseDataItem getBaseDataItem(String categoryCode, String code);

    List<Org> getAllOrgs();

    OrgInfo saveOrgInfo(Org org);

    OrgInfo validOrg(String orgId, boolean valid);

    OrgInfo getOrgInfo(String orgId);

    List<PersonAccountTuple> getAllPersons();

    PersonAccountTuple savePersonInfo(PersonAccountTuple tuple);

    PersonAccountTuple getPersonInfo(String personId);

    PersonAccountTuple enablePersonAccount(String personId, AccountInitialInfo accountInfo);

    boolean validPersonAccount(List<String> personIds, boolean valid);

    void changeAccountPassword(String accountCode, String oldPassword, String newPassword);

    class BaseData {
        private String id, code, name;
        private List<BaseDataItem> items = new ArrayList<>();

        public BaseData(String id, String code, String name) {
            super();
            this.id = id;
            this.code = code;
            this.name = name;
        }

        public BaseData(String id, String code, String name, List<BaseDataItem> items) {
            this(id, code, name);
            this.items = items;
        }

        public String getId() {
            return id;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public List<BaseDataItem> getItems() {
            return items;
        }
    }

    class BaseDataItem {
        private String code, name, value, parentCode;
        private BaseDataItem parent;
        private List<BaseDataItem> children = new ArrayList<>();

        public BaseDataItem(String code, String name, String value) {
            super();
            this.code = code;
            this.name = name;
            this.value = value;
        }

        public BaseDataItem(String code, String name, String value, String parentCode) {
            this(code, name, value);
            this.parentCode = parentCode;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public String getParentCode() {
            return parentCode;
        }

        public BaseDataItem getParent() {
            return parent;
        }

        public List<BaseDataItem> getChildren() {
            return children;
        }

        public void setParent(BaseDataItem parent) {
            this.parent = parent;
        }

        public void setChildren(List<BaseDataItem> children) {
            this.children = children;
        }
    }

    class PersonAccountTuple {
        private Person person;
        private Account account;
        private AccountState accountState;
        private Org org;

        private PersonAccountTuple(Person person, Account account, AccountState accountState, Org org) {
            super();
            this.person = person;
            this.account = account;
            this.accountState = accountState;
            this.org = org;
        }

        public static PersonAccountTuple valueOf(Person person, Account account, Org org) {
            return new PersonAccountTuple(person, account, null, org);
        }

        public static PersonAccountTuple valueOf(Person person, Account account, AccountState accountState, Org org) {
            return new PersonAccountTuple(person, account, accountState, org);
        }

        public Person getPerson() {
            return person;
        }

        public Account getAccount() {
            return account;
        }

        public AccountState getAccountState() {
            return accountState;
        }

        public Org getOrg() {
            return org;
        }
    }

    class AccountInitialInfo {
        private String accountCode, password, nickname, avatar, roleCode;

        public AccountInitialInfo(String accountCode, String password, String nickname, String avatar, String roleCode) {
            super();
            this.accountCode = accountCode;
            this.password = password;
            this.nickname = nickname;
            this.avatar = avatar;
            this.roleCode = roleCode;
        }

        public String getRoleCode() {
            return roleCode;
        }

        public String getAccountCode() {
            return accountCode;
        }

        public String getNickname() {
            return nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getPassword() {
            return password;
        }
    }

    class OrgInfo {
        private Org org;
        private List<PersonAccountTuple> employees;
        private PersonAccountTuple manager;

        public OrgInfo(Org org, PersonAccountTuple manager, List<PersonAccountTuple> employees) {
            super();
            this.org = org;
            this.manager = manager;
            this.employees = employees;
        }

        public Org getOrg() {
            return org;
        }

        public List<PersonAccountTuple> getEmployees() {
            return employees;
        }

        public PersonAccountTuple getManager() {
            return manager;
        }
    }
}
