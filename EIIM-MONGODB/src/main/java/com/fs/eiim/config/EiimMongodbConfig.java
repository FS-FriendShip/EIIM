package com.fs.eiim.config;

import org.mx.comps.rbac.mongodb.config.CompsRbacMongodbConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * 描述： EIIM数据访问配置类
 *
 * @author john peng
 * Date time 2018/8/17 下午12:57
 */
@Import({
        CompsRbacMongodbConfig.class
})
@ComponentScan({
        "com.fs.eiim.service.impl"
})
public class EiimMongodbConfig {
}
