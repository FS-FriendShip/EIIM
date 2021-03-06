package com.fs.eiim.service.impl;

import com.fs.eiim.dal.entity.*;
import com.fs.eiim.error.UserInterfaceEiimErrorException;
import com.fs.eiim.service.AccountService;
import com.fs.eiim.service.BaseDataCacheService;
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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.query.Criteria.where;

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
    private BaseDataCacheService baseDataCacheService;
    private AccountService accountService;

    private MongoTemplate mongoTemplate;

    /**
     * 构造函数
     *
     * @param accessor             数据库访问接口
     * @param baseDataCacheService 基础字典数据缓存服务接口
     * @param accountService       账户服务接口
     * @param mongoTemplate        Mongodb操作模版
     */
    @Autowired
    public BaseDataServiceImpl(@Qualifier("generalDictAccessorMongodb") GeneralDictAccessor accessor,
                               BaseDataCacheService baseDataCacheService,
                               AccountService accountService,
                               MongoTemplate mongoTemplate) {
        super();
        this.accessor = accessor;
        this.baseDataCacheService = baseDataCacheService;
        this.accountService = accountService;
        this.mongoTemplate = mongoTemplate;
    }

    @Cacheable(key = "'baseData.'.concat(#categoryCode)")
    @Override
    public List<BaseDataItem> getBaseDataItems(String categoryCode) {
        if (StringUtils.isBlank(categoryCode)) {
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        List<BaseData> list = getAllBaseData();
        if (list != null && !list.isEmpty()) {
            for (BaseData baseData : list) {
                if (baseData.getCode().equals(categoryCode)) {
                    return baseData.getItems();
                }
            }
        }
        if (logger.isWarnEnabled()) {
            logger.warn(String.format("There are none base data item for category[%s].", categoryCode));
        }
        return null;
    }

    @Cacheable(key = "'baseData.'.concat(#categoryCode).concat(#code)")
    @Override
    public BaseDataItem getBaseDataItem(String categoryCode, String code) {
        if (StringUtils.isBlank(categoryCode) || StringUtils.isBlank(code)) {
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        List<BaseDataItem> list = getBaseDataItems(categoryCode);
        if (list != null && !list.isEmpty()) {
            for (BaseDataItem item : list) {
                if (item.getCode().equals(code)) {
                    return item;
                }
            }
        }
        if (logger.isWarnEnabled()) {
            logger.warn(String.format("The base data item[%s:%s] not found.", categoryCode, code));
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @see BaseDataService#getAllBaseData()
     */
    @Override
    public List<BaseData> getAllBaseData() {
        if (logger.isDebugEnabled()) {
            logger.debug("Load base data from db.");
        }
        List<com.fs.eiim.dal.entity.BaseData> baseDatas = accessor.list(com.fs.eiim.dal.entity.BaseData.class, true);
        List<BaseData> list = new ArrayList<>();
        if (baseDatas != null && !baseDatas.isEmpty()) {
            for (com.fs.eiim.dal.entity.BaseData baseData : baseDatas) {
                Set<? extends com.fs.eiim.dal.entity.BaseDataItem> itemEntities = baseData.getItems();
                List<BaseDataItem> items = new ArrayList<>();
                if (itemEntities != null && !itemEntities.isEmpty()) {
                    Map<String, BaseDataItem> mapItems = new HashMap<>();
                    boolean isTree = false;
                    for (com.fs.eiim.dal.entity.BaseDataItem item : itemEntities) {
                        BaseDataItem itemData = new BaseDataItem(item.getCode(), item.getName(), item.getValue(),
                                item.getParentCode());
                        mapItems.put(item.getCode(), itemData);
                        baseDataCacheService.putBaseDataItemCache(baseData.getCode(), item.getCode(), itemData);
                        if (!isTree && !StringUtils.isBlank(item.getParentCode())) {
                            isTree = true;
                        }
                    }
                    if (isTree) {
                        // 树
                        mapItems.values().forEach(item -> {
                            BaseDataItem parent = mapItems.get(item.getCode());
                            item.setParent(parent);
                            if (parent != null) {
                                if (parent.getChildren() == null) {
                                    parent.setChildren(new ArrayList<>());
                                }
                                parent.getChildren().add(item);
                            }
                        });
                        items.addAll(mapItems.values().stream().filter(item -> item.getParent() == null)
                                .collect(Collectors.toList()));
                    } else {
                        // 非树
                        items.addAll(mapItems.values());
                    }
                }
                list.add(new BaseData(baseData.getId(), baseData.getCode(), baseData.getName(), items));
                baseDataCacheService.putBaseDataItemListCache(baseData.getCode(), items);
            }
        }
        return list;
    }

    @Override
    public List<Org> getAllOrgs() {
        List<Org> orgs = accessor.find(GeneralAccessor.ConditionTuple.eq("parent", null), Org.class);
        if (orgs != null) {
            return orgs.stream().filter(org -> org.isValid()).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public OrgInfo saveOrgInfo(Org org) {
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
                User user = accessor.getById(employee.getId(), Person.class);
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
            Org updatedOrg = accessor.getById(org.getId(), Org.class);
            if (updatedOrg != null) {
                updatedOrg.setCode(org.getCode());
                updatedOrg.setName(org.getName());
                updatedOrg.setType(org.getType());

                org = updatedOrg;
            } else {
                throw new UserInterfaceEiimErrorException(
                        UserInterfaceEiimErrorException.EiimErrors.ORG_NOT_FOUND
                );
            }
        } else {
            org.setParent(parent);
            org.setManager(manager);
            org.setEmployees(employees);
        }

        try {
            org = accessor.save(org);
            if (parent != null) {
                parent.getChildren().add(org);
                accessor.save(parent);
            }
            return getOrgInfo(org.getId());
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Save org fail.", ex);
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.ORG_SAVE_FAIL
            );
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public OrgInfo validOrg(String orgId, boolean valid) {
        if (StringUtils.isBlank(orgId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The org's id is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        Org org = accessor.getById(orgId, Org.class);
        if (org == null) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("The org[%s] not found.", orgId));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.ORG_NOT_FOUND
            );
        }
        if (valid == org.isValid()) {
            if (logger.isWarnEnabled()) {
                logger.warn(String.format("The org[%s] is %s, input: %s.", orgId, org.isValid(), valid));
            }
        } else {
            org.setValid(valid);
            org = accessor.save(org);
            // 如果valid = false，则处理下级节点
            if (!valid) {
                Set<Org> children = org.getChildren();
                if (children != null && !children.isEmpty()) {
                    children.forEach(child -> validOrg(child.getId(), false));
                }
            }
        }
        PersonAccountTuple manager = null;
        List<PersonAccountTuple> employees = new ArrayList<>();
        if (org.getManager() != null && !StringUtils.isBlank(org.getManager().getId())) {
            manager = getPersonInfo(org.getManager().getId());
        }
        if (org.getEmployees() != null && !org.getEmployees().isEmpty()) {
            org.getEmployees().forEach(employee -> {
                if (employee != null && !StringUtils.isBlank(employee.getId())) {
                    employees.add(getPersonInfo(employee.getId()));
                }
            });
        }
        return new OrgInfo(org, manager, employees);
    }

    @Override
    public OrgInfo getOrgInfo(String orgId) {
        if (StringUtils.isBlank(orgId)) {
            if (logger.isErrorEnabled()) {
                logger.error("The org's id is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        Org org = accessor.getById(orgId, Org.class);
        if (org != null) {
            PersonAccountTuple manager = null;
            List<PersonAccountTuple> employees = new ArrayList<>();
            if (org.getManager() != null && !StringUtils.isBlank(org.getManager().getId())) {
                manager = getPersonInfo(org.getManager().getId());
            }
            if (org.getEmployees() != null && !org.getEmployees().isEmpty()) {
                org.getEmployees().forEach(employee -> {
                    if (employee != null && employee.isValid() && !StringUtils.isBlank(employee.getId())) {
                        employees.add(getPersonInfo(employee.getId()));
                    }
                });
            }
            return new OrgInfo(org, manager, employees);
        } else {
            return null;
        }
    }

    private Org getOrgByPerson(Person person) {
        if (person == null) {
            return null;
        }
        Query query = new Query();
        query.addCriteria(where("employees").all(person));
        List<OrgEntity> orgs = mongoTemplate.find(query, OrgEntity.class);
        if (!orgs.isEmpty()) {
            return orgs.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<PersonAccountTuple> getAllPersons() {
        List<Person> persons = accessor.list(Person.class);
        List<PersonAccountTuple> tuples = new ArrayList<>();
        for (Person person : persons) {
            Account account = accessor.findOne(GeneralAccessor.ConditionTuple.eq("person", person),
                    Account.class);
            tuples.add(PersonAccountTuple.valueOf(person, account, getOrgByPerson(person)));
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
        Org org = null;
        if (person.getOrganization() != null && !StringUtils.isBlank(person.getOrganization().getId())) {
            org = accessor.getById(person.getOrganization().getId(), Org.class);
            if (org == null) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("The org[%s] not found.", person.getOrganization().getId()));
                }
                throw new UserInterfaceEiimErrorException(
                        UserInterfaceEiimErrorException.EiimErrors.ORG_NOT_FOUND
                );
            }
        }
        if (StringUtils.isBlank(person.getMobile())) {
            if (logger.isErrorEnabled()) {
                logger.error("The person's mobile is blank.");
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.PERSON_MOBILE_BLANK
            );
        }
        if (StringUtils.isBlank(person.getEmail())) {
            if (logger.isErrorEnabled()) {
                logger.error("The person's email is blank.");
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.PERSON_EMAIL_BLANK
            );
        }
        if (StringUtils.isBlank(person.getId())) {
            // 如果新增人员，判定相关的mobile和email是否已经被使用过
            Person check = accessor.findOne(GeneralAccessor.ConditionTuple.eq("mobile", person.getMobile()), Person.class);
            if (check != null) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("The person[%s] has existed.", person.getMobile()));
                }
                throw new UserInterfaceEiimErrorException(
                        UserInterfaceEiimErrorException.EiimErrors.PERSON_MOBILE_EXIST
                );
            }
            check = accessor.findOne(GeneralAccessor.ConditionTuple.eq("email", person.getEmail()), Person.class);
            if (check != null) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("The person[%s] has existed.", person.getEmail()));
                }
                throw new UserInterfaceEiimErrorException(
                        UserInterfaceEiimErrorException.EiimErrors.PERSON_EMAIL_EXIST
                );
            }
        }
        if (!StringUtils.isBlank(person.getId())) {
            Person checkedPerson = accessor.getById(person.getId(), Person.class);
            if (checkedPerson != null) {
                checkedPerson.setFirstName(person.getFirstName());
                checkedPerson.setLastName(person.getLastName());
                checkedPerson.setFullName(person.getFullName());
                checkedPerson.setTitle(person.getTitle());
                checkedPerson.setPhone(person.getPhone());
                checkedPerson.setMobile(person.getMobile());
                checkedPerson.setEmail(person.getEmail());
                checkedPerson.setDesc(person.getDesc());
                checkedPerson.setSex(person.getSex());
                person = checkedPerson;
            }
        }
        try {
            person = accessor.save(person);
            // 如果指定了组织，则将该人员加入到该组织
            if (org != null) {
                org.getEmployees().add(person);
                accessor.save(org);
            }
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Save person info fail.", ex);
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.PERSON_SAVE_FAIL
            );
        }
//        if (account != null && !StringUtils.isBlank(account.getId())) {
//            Account checkAccount = accessor.getById(account.getId(), Account.class);
//            if (checkAccount != null) {
//                checkAccount.setNickName(account.getNickName());
//                checkAccount.setAvatar(account.getAvatar());
//                checkAccount.setPerson(person);
//                try {
//                    account = accessor.save(checkAccount);
//                } catch (Exception ex) {
//                    if (logger.isErrorEnabled()) {
//                        logger.error("Save account info fail.", ex);
//                    }
//                    throw new UserInterfaceEiimErrorException(
//                            UserInterfaceEiimErrorException.EiimErrors.ACCOUNT_SAVE_FAIL
//                    );
//                }
//            } else {
//                if (logger.isErrorEnabled()) {
//                    logger.error(String.format("The account[%s] not found.", account.getId()));
//                }
//                throw new UserInterfaceRbacErrorException(
//                        UserInterfaceRbacErrorException.RbacErrors.ACCOUNT_NOT_FOUND
//                );
//            }
//        } else {
//            account = null;
//        }

        return PersonAccountTuple.valueOf(person, account, getOrgByPerson(person));
    }


    @Override
    public PersonAccountTuple enablePersonAccount(String personId, AccountInitialInfo accountInfo) {
        if (StringUtils.isBlank(personId) || accountInfo == null) {
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        if (StringUtils.isBlank(accountInfo.getPassword())) {
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.ACCOUNT_BLANK_PASSWORD
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

        String roleCode = accountInfo.getRoleCode();
        Role roleUser = accessor.getByCode(roleCode, Role.class);
        if (roleUser == null) {
            if (logger.isErrorEnabled()) {
                logger.error("The role[" + roleCode + "] not found.");
            }
            throw new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.ROLE_NOT_FOUND
            );
        }
        Account account = accessor.findOne(GeneralAccessor.ConditionTuple.eq("person", person), Account.class);
        if (account == null) {
            // 账户不存在，创建账户
            account = EntityFactory.createEntity(Account.class);
            account.setCode(accountInfo.getAccountCode());
            account.setPassword(DigestUtils.md5(accountInfo.getPassword()));
            account.setNickName(person.getFullName());
            account.setAvatar(accountInfo.getAvatar());
            account.setEiimCode(String.format("EEIM-%s", DigestUtils.uuid()));
            account.setName(person.getFullName());
            account.getRoles().add(roleUser);
            account.setPerson(person);
            account.setDesc(person.getFullName());
            account.setValid(true);

            // 如果没有设置avatar，则根据性别设置默认的头像，默认为男士头像
            if (StringUtils.isBlank(account.getAvatar())) {
                account.setAvatar(person.getSex() == User.Sex.FEMALE ? uuidFemale : uuidMale);
            }
            try {
                accessor.save(account);
                roleUser.getAccounts().add(account);
                accessor.save(roleUser);
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("Enable persion[%s]'s account sccuessfully, the eiim code: %s.",
                            person.getFullName(), account.getEiimCode()));
                }
                return PersonAccountTuple.valueOf(person, account, null, getOrgByPerson(person));
            } catch (Exception ex) {
                if (logger.isErrorEnabled()) {
                    logger.error("Save account info fail.", ex);
                }
                throw new UserInterfaceEiimErrorException(
                        UserInterfaceEiimErrorException.EiimErrors.ACCOUNT_SAVE_FAIL
                );
            }
        } else {
            // 账户已经存在
            if (logger.isWarnEnabled()) {
                logger.warn(String.format("The person[%s] has already enable the account, the eiim code: %s.",
                        person.getFullName(), account.getEiimCode()));
            }
            throw new UserInterfaceRbacErrorException(
                    UserInterfaceRbacErrorException.RbacErrors.ACCOUNT_HAS_EXIST
            );
        }
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
        Account account = accessor.findOne(GeneralAccessor.ConditionTuple.eq("person", person),
                Account.class);
        AccountState accountState = null;
        if (account != null) {
            accountState = accountService.getAccountStateByAccountId(account.getId());
        } else {
            if (logger.isWarnEnabled()) {
                logger.warn(String.format("The person[%s] not be enabled a account.", person.getFullName()));
            }
        }
        return PersonAccountTuple.valueOf(person, account, accountState, getOrgByPerson(person));
    }

    @Override
    public boolean validPersonAccount(List<String> personIds, boolean valid) {
        if (personIds == null || personIds.isEmpty()) {
            if (logger.isErrorEnabled()) {
                logger.error("The person's id is blank.");
            }
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }

        for (String personId : personIds) {
            Person person = accessor.getById(personId, Person.class);
            if (person == null) {
                if (logger.isErrorEnabled()) {
                    logger.error(String.format("The person[%s] not found.", personId));
                }
                throw new UserInterfaceRbacErrorException(
                        UserInterfaceRbacErrorException.RbacErrors.USER_NOT_FOUND
                );
            }
            if (valid == person.isValid()) {
                if (logger.isWarnEnabled()) {
                    logger.warn(String.format("The person[%s]'s valid: %s, input valid: %s.",
                            person.getId(), person.isValid(), valid));
                }
            } else {
                person.setValid(valid);
                person = accessor.save(person);
            }
            Account account = accessor.findOne(GeneralAccessor.ConditionTuple.eq("person", person), Account.class);
            if (account == null) {
                if (logger.isWarnEnabled()) {
                    logger.warn(String.format("The person[%s] not enable the account, please enable it at first.", person.getId()));
                }
            } else {
                if (valid == account.isValid()) {
                    if (logger.isWarnEnabled()) {
                        logger.warn(String.format("The account[%s]'s valid: %s, input valid: %s.",
                                account.getId(), account.isValid(), valid));
                    }
                } else {
                    account.setValid(valid);
                    account = accessor.save(account);
                }
            }
        }

        return true;
    }

    private boolean passwordStrength(String password) {
        if (StringUtils.isBlank(password)) {
            // 密码为空
            return false;
        }
        if (password.length() < 8) {
            // 密码长度太短
            return false;
        }
        if (StringUtils.isChinese(password)) {
            // 中文密码
            return true;
        }
        boolean isUpper = false, isLower = false, isNumeric = false, isSymbol = false;
        for (char c : password.toCharArray()) {
            if (!isUpper && Character.isUpperCase(c)) {
                isUpper = true;
            } else if (!isLower && Character.isLowerCase(c)) {
                isLower = true;
            } else if (!isNumeric && c >= '0' && c <= '9') {
                isNumeric = true;
            } else if (!isSymbol && "!@#$%^&*()_+-=,./<>?;':\"[]{}\\|".indexOf(c) >= 0) {
                isSymbol = true;
            }
            int num = (isUpper ? 1 : 0) + (isLower ? 1 : 0) + (isNumeric ? 1 : 0) + (isSymbol ? 1 : 0);
            if (num >= 2) {
                // 两种及以上组合即可
                return true;
            }
        }
        return false;
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
        if (passwordStrength(newPassword)) {
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
            try {
                accessor.save(account);
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("Modify account[%s]'s password successfully.", accountCode));
                }
            } catch (Exception ex) {
                if (logger.isErrorEnabled()) {
                    logger.error("Save changed password fail.", ex);
                }
                throw new UserInterfaceEiimErrorException(
                        UserInterfaceEiimErrorException.EiimErrors.PASSWORD_CHANGE_FAIL
                );
            }
        } else {
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.ACCOUNT_PASSWORD_STRENGTHEN_LOW
            );
        }
    }
}
