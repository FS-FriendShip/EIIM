package com.fs.eiim.service.impl;

import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.BaseData;
import com.fs.eiim.service.InitializeTaskService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.DigestUtils;
import org.mx.comps.rbac.dal.entity.Role;
import org.mx.dal.EntityFactory;
import org.mx.dal.service.GeneralDictAccessor;
import org.mx.dal.session.SessionDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("initializeTaskService")
public class InitializeTaskServiceImpl implements InitializeTaskService {
    private static final Log logger = LogFactory.getLog(InitializeTaskServiceImpl.class);

    private SessionDataStore sessionDataStore;
    private GeneralDictAccessor accessor;

    @Autowired
    public InitializeTaskServiceImpl(@Qualifier("generalDictAccessorMongodb") GeneralDictAccessor accessor,
                                     SessionDataStore sessionDataStore) {
        super();
        this.accessor = accessor;
        this.sessionDataStore = sessionDataStore;
    }

    private void initializeBaseData() {
        // 初始化 账户状态 字典项
        BaseData baseData = accessor.getByCode("account.status", BaseData.class);
        if (baseData == null) {
            baseData = EntityFactory.createEntity(BaseData.class);
            baseData.setCode("account.status");
            baseData.setName("账户状态");
            baseData.addItem("online", "在线", "online");
            baseData.addItem("offline", "离线", "offline");
            baseData.addItem("hidden", "隐身", "hidden");
            baseData.addItem("stepOut", "暂时离开", "stepOut");
            accessor.save(baseData);
        }
    }

    private void initializeRoles() {
        Role admin = accessor.getByCode("Administrator", Role.class);
        if (admin == null) {
            admin = EntityFactory.createEntity(Role.class);
            admin.setCode("Administrator");
            admin.setName("系统管理员");
            admin.setDesc("系统管理员 角色");
            admin.setValid(true);
            accessor.save(admin);
            if (logger.isInfoEnabled()) {
                logger.info("Create role[Administrator] successfully.");
            }
        }

        Role user = accessor.getByCode("User", Role.class);
        if (user == null) {
            user = EntityFactory.createEntity(Role.class);
            user.setCode("User");
            user.setName("一般用户");
            user.setDesc("一般用户 角色");
            user.setValid(true);
            accessor.save(user);
            if (logger.isInfoEnabled()) {
                logger.info("Create role[User] successfully.");
            }
        }
    }

    private void initializeAdministratorAccount() {
        Role roleAdmin = accessor.getByCode("Administrator", Role.class);

        Account admin = accessor.getByCode("Administrator", Account.class);
        if (admin == null) {
            admin = EntityFactory.createEntity(Account.class);
            admin.setCode("Administrator");
            admin.setNickName("Administrator");
            admin.setName("系统管理员");
            admin.setPassword(DigestUtils.md5("edmund-EIIM"));
            admin.getRoles().add(roleAdmin);
            accessor.save(admin);
            if (logger.isInfoEnabled()) {
                logger.info("Create account[Administrator] successfully.");
            }
        } else {
            boolean foundAdminRole = false;
            for (Role role : admin.getRoles()) {
                if ("Administrator".equals(role.getCode())) {
                    foundAdminRole = true;
                    break;
                }
            }
            if (!foundAdminRole) {
                admin.getRoles().add(roleAdmin);
                accessor.save(admin);
                if (logger.isInfoEnabled()) {
                    logger.info("Create account[Administrator] successfully.");
                }
            }
        }
    }

    @Override
    public void initializeInternalData() {
        sessionDataStore.setCurrentUserCode("System");
        // 初始化 内置字典
        initializeBaseData();

        // 初始化 角色
        initializeRoles();

        // 初始化 管理员
        initializeAdministratorAccount();
        sessionDataStore.clean();
    }
}
