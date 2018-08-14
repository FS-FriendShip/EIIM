package com.fs.eiim.service.impl;

import com.fs.eiim.dal.entity.*;
import com.fs.eiim.error.UserInterfaceEiimErrorException;
import com.fs.eiim.service.BaseDataService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.DigestUtils;
import org.mx.StringUtils;
import org.mx.comps.rbac.dal.entity.Role;
import org.mx.comps.rbac.dal.entity.User;
import org.mx.comps.rbac.error.UserInterfaceRbacErrorException;
import org.mx.dal.EntityFactory;
import org.mx.dal.service.GeneralAccessor;
import org.mx.dal.service.GeneralDictAccessor;
import org.mx.error.UserInterfaceSystemErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 描述： 基础数据服务实现类定义
 *
 * @author john peng
 * Date time 2018/8/11 下午6:39
 */
@Component("baseDataService")
@CacheConfig(cacheNames = {"eiim"})
public class BaseDataServiceImpl implements BaseDataService {
    private static final Log logger = LogFactory.getLog(BaseDataServiceImpl.class);

    private GeneralDictAccessor accessor;

    /**
     * 构造函数
     *
     * @param accessor 数据库访问接口
     */
    @Autowired
    public BaseDataServiceImpl(@Qualifier("generalDictAccessorMongodb") GeneralDictAccessor accessor) {
        super();
        this.accessor = accessor;
    }

    /**
     * {@inheritDoc}
     *
     * @see BaseDataService#getAllBaseData()
     */
    @Cacheable(key = "'baseData'")
    @Override
    public Map<String, BaseDataItem> getAllBaseData() {
        List<BaseData> baseDatas = accessor.list(BaseData.class, true);
        Map<String, BaseDataItem> map = new HashMap<>(baseDatas.size());
        for (BaseData baseData : baseDatas) {
            for (BaseDataItem item : baseData.getItems()) {
                String key = String.format("%s-%s", baseData.getCode(), item.getCode());
                map.put(key, item);
            }
        }
        return map;
    }

    @Override
    public List<Org> getAllOrgs() {
        return accessor.find(GeneralAccessor.ConditionTuple.eq("parent", null), Org.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Org saveOrgInfo(Org org) {
        Org parent = null;
        if (org.getParent() != null && !StringUtils.isBlank(org.getParent().getId())) {
            parent = accessor.getById(org.getParent().getId(), Org.class);
            if (parent == null) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("The org[%s] not found.", org.getParent().getId()));
                }
                throw new UserInterfaceEiimErrorException(
                        UserInterfaceEiimErrorException.EiimErrors.ORG_NOT_FOUND
                );
            }
        }

        Person manager = null;
        if (org.getManager() != null && !StringUtils.isBlank(org.getManager().getId())) {
            manager = accessor.getById(org.getManager().getId(), Person.class);
            if (manager == null) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("The manager[%s] not found.", org.getManager().getId()));
                }
                throw new UserInterfaceEiimErrorException(
                        UserInterfaceEiimErrorException.EiimErrors.PERSON_NOT_FOUND
                );
            }
        }

        Set<User> employees = null;
        if (org.getEmployees() != null && org.getEmployees().size() > 0) {
            employees = new HashSet<>();
            for (User employee : org.getEmployees()) {
                User user = accessor.getById(employee.getId(), User.class);
                if (user == null) {
                    if (logger.isErrorEnabled()) {
                        logger.error(String.format("The person[%s] not found.", employee.getId()));
                    }
                    throw new UserInterfaceRbacErrorException(
                            UserInterfaceRbacErrorException.RbacErrors.USER_NOT_FOUND
                    );
                } else {
                    employees.add(user);
                }
            }
        }

        if (!StringUtils.isBlank(org.getId())) {
            Org checkOrg = accessor.getById(org.getId(), Org.class);
            if (checkOrg != null) {
                checkOrg.setCode(org.getCode());
                checkOrg.setName(org.getName());
                checkOrg.setType(org.getType());
                org = checkOrg;
            } else {
                org.setId(null);
            }
        }
        org.setParent(parent);
        org.setManager(manager);
        org.setEmployees(employees);
        return accessor.save(org);
    }

    @Override
    public Org getOrgInfo(String orgId) {
        return accessor.getById(orgId, Org.class);
    }

    @Override
    public List<PersonAccountTuple> getAllPersons() {
        List<Person> persons = accessor.list(Person.class);
        List<PersonAccountTuple> tuples = new ArrayList<>();
        for (Person person : persons) {
            Account account = accessor.findOne(Collections.singletonList(
                    GeneralAccessor.ConditionTuple.eq("person", person)
            ), Account.class);
            tuples.add(PersonAccountTuple.valueOf(person, account));
        }
        return tuples;
    }

    @Override
    public PersonAccountTuple savePersonInfo(PersonAccountTuple tuple) {
        Person person = tuple.getPerson();
        Account account = tuple.getAccount();
        if (person == null) {
            if (logger.isErrorEnabled()) {
                logger.error("The person object is null, can not be saved.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        if (!StringUtils.isBlank(person.getId())) {
            Person checkedPerson = accessor.getById(person.getId(), Person.class);
            if (checkedPerson != null) {
                checkedPerson.setFirstName(person.getFirstName());
                checkedPerson.setLastName(person.getLastName());
                checkedPerson.setTitle(person.getTitle());
                checkedPerson.setPhone(person.getPhone());
                checkedPerson.setMobile(person.getMobile());
                checkedPerson.setEmail(person.getEmail());
                checkedPerson.setDesc(person.getDesc());
                person = checkedPerson;
            }
        }
        person = accessor.save(person);
        if (account != null && !StringUtils.isBlank(account.getId())) {
            Account checkAccount = accessor.getById(account.getId(), Account.class);
            if (checkAccount != null) {
                checkAccount.setNickName(account.getNickName());
                checkAccount.setAvatar(account.getAvatar());
                checkAccount.setPerson(person);
                account = accessor.save(checkAccount);
            } else {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("The account[%s] not found.", account.getId()));
                }
                throw new UserInterfaceRbacErrorException(
                        UserInterfaceRbacErrorException.RbacErrors.ACCOUNT_NOT_FOUND
                );
            }
        } else {
            account = null;
        }
        return PersonAccountTuple.valueOf(person, account);
    }

    @Override
    public PersonAccountTuple getPersonInfo(String personId) {
        if (StringUtils.isBlank(personId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The person's id is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        Person person = accessor.getById(personId, Person.class);
        if (person == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The person[%s] not found.", personId));
            }
            throw new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.USER_NOT_FOUND
            );
        }
        Account account = accessor.findOne(Collections.singletonList(
                GeneralAccessor.ConditionTuple.eq("person", person)
        ), Account.class);
        if (account == null) {
            if (logger.isWarnEnabled()) {
                logger.warn(String.format("The person[%s] not be enabled a account.", person.getFullName()));
            }
        }
        return PersonAccountTuple.valueOf(person, account);
    }

    @Override
    public PersonAccountTuple enablePersonAccount(String personId) {
        if (StringUtils.isBlank(personId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The person's id is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        Person person = accessor.getById(personId, Person.class);
        if (person == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The person[%s] not found.", personId));
            }
            throw new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.USER_NOT_FOUND
            );
        }
        Role roleUser = accessor.getByCode("User", Role.class);
        if (roleUser == null) {
            if (logger.isErrorEnabled()) {
                logger.error("The role[User] not found.");
            }
            throw new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.ROLE_NOT_FOUND
            );
        }
        Account account = accessor.findOne(Collections.singletonList(
                GeneralAccessor.ConditionTuple.eq("person", person)
        ), Account.class);
        if (account == null) {
            // 账户不存在，创建账户
            account = EntityFactory.createEntity(Account.class);
            account.setPassword(DigestUtils.md5("edmund8888"));
            account.setName(person.getFullName());
            account.getRoles().add(roleUser);
            account.setNickName(person.getFirstName());
            account.setCode(String.format("EEIM-%d", accessor.count(Account.class)));
            account.setEiimCode(account.getCode());
            account.setPerson(person);
            account.setDesc(person.getFullName());
            account.setValid(true);
            accessor.save(account);
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Enable persion[%s]'s account sccuessfully, the eiim code: %s.",
                        person.getFullName(), account.getEiimCode()));
            }
        } else {
            // 账户已经存在
            if (logger.isWarnEnabled()) {
                logger.warn(String.format("The person[%s] has already enable the account, the eiim code: %s.",
                        person.getFullName(), account.getEiimCode()));
            }
        }
        return PersonAccountTuple.valueOf(person, account);
    }

    @Override
    public void changeAccountPassword(String accountCode, String oldPassword, String newPassword) {
        if (StringUtils.isBlank(accountCode)) {
            if (logger.isErrorEnabled()) {
                logger.error("The account's code is blank.");
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.ACCOUNT_BLANK_CODE
            );
        }
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The password is blank, old: %s, new: %s.", oldPassword, newPassword));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.ACCOUNT_BLANK_PASSWORD
            );
        }
        Account account = accessor.getByCode(accountCode, Account.class);
        if (account == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The account[%s] not found", accountCode));
            }
            throw new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.ACCOUNT_NOT_FOUND
            );
        }
        if (!DigestUtils.md5(oldPassword).equals(account.getPassword())) {
            throw new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.ACCOUNT_PASSWORD_NOT_MATCHED
            );
        }
        account.setPassword(DigestUtils.md5(newPassword));
        accessor.save(account);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Modify account[%s]'s password successfully.", accountCode));
        }
    }
}
