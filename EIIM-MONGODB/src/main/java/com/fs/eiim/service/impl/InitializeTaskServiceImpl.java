package com.fs.eiim.service.impl;

import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.BaseData;
import com.fs.eiim.error.UserInterfaceEiimErrorException;
import com.fs.eiim.service.InitializeTaskService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.DigestUtils;
import org.mx.FileUtils;
import org.mx.comps.rbac.dal.entity.Role;
import org.mx.dal.EntityFactory;
import org.mx.dal.service.GeneralDictAccessor;
import org.mx.dal.session.SessionDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component("initializeTaskService")
public class InitializeTaskServiceImpl implements InitializeTaskService {
    private static final Log logger = LogFactory.getLog(InitializeTaskServiceImpl.class);

    @Value("${upload.root:upload}")
    private String uploadRootPath;

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

    private void initializeAvatar(String uuid) {
        String path = String.format("%s/%s", uploadRootPath, uuid.substring(0, 2));
        String name = uuid.substring(2);
        if (Files.exists(Paths.get(path, name))) {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("The file[%s/%s] has existed.", path, name));
            }
            return;
        }
        InputStream in = InitializeTaskServiceImpl.class.getResourceAsStream(String.format("/avatar/%s", uuid));
        try {
            FileUtils.saveFile(path, name, in);
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Upload the file[%s] successfully, path: %s/%s.",
                        uuid, path, name));
            }
        } catch (IOException ex) {
            if (logger.isErrorEnabled()) {
                logger.error(String.format("Save the file[%s] from input stream fail.", path));
            }
            throw new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.FILE_UPLOAD_FAIL
            );
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
            admin.setAvatar("5ba75abf96a54e2a9005968b");
            admin.setPassword(DigestUtils.md5("edmund-EIIM"));
            admin.getRoles().add(roleAdmin);
            accessor.save(admin);
            roleAdmin.getAccounts().add(admin);
            accessor.save(roleAdmin);
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

        // 初始化 默认头像：男性-5ba75ab696a54e2a9005968b | 女性-5ba75abf96a54e2a9005968c
        initializeAvatar("5ba75ab696a54e2a9005968b");
        initializeAvatar("5ba75abf96a54e2a9005968c");

        // 初始化 管理员
        initializeAdministratorAccount();
        sessionDataStore.clean();
    }
}
