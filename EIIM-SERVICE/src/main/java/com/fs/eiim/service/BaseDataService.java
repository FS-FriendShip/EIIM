package com.fs.eiim.service;

import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.BaseDataItem;
import com.fs.eiim.dal.entity.Org;
import com.fs.eiim.dal.entity.Person;

import java.util.List;
import java.util.Map;

/**
 * 描述： 基础数据服务接口定义
 *
 * @author john peng
 * Date time 2018/8/11 下午6:37
 */
public interface BaseDataService {
    /**
     * 获取所有的字典数据项
     *
     * @return 字典数据项集合，Key形如："字典代码-字典项代码"
     */
    Map<String, BaseDataItem> getAllBaseData();

    List<Org> getAllOrgs();

    Org saveOrgInfo(Org org);

    Org getOrgInfo(String orgId);

    List<PersonAccountTuple> getAllPersons();

    PersonAccountTuple savePersonInfo(PersonAccountTuple tuple);

    PersonAccountTuple getPersonInfo(String personId);

    PersonAccountTuple enablePersonAccount(String personId);

    void changeAccountPassword(String accountId, String oldPassword, String newPassword);

    class PersonAccountTuple {
        private Person person;
        private Account account;

        private PersonAccountTuple(Person person, Account account) {
            super();
            this.person = person;
            this.account = account;
        }

        public static PersonAccountTuple valueOf(Person person, Account account) {
            return new PersonAccountTuple(person, account);
        }

        public Person getPerson() {
            return person;
        }

        public Account getAccount() {
            return account;
        }
    }
}
