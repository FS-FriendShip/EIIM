package com.fs.eiim.config;

import com.fs.eiim.task.EiimInitializeTask;
import org.mx.service.server.config.ServerConfig;
import org.mx.spring.config.SpringConfig;
import org.mx.spring.task.TaskFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

/**
 * 描述： EIIM相关RESTful服务定义配置类
 *
 * @author john peng
 * Date time 2018/8/8 下午2:38
 */
@PropertySource({
        "classpath:server.properties",
        "classpath:eiim.properties"
})
@Import({
        SpringConfig.class,
        ServerConfig.class
})
@ComponentScan({
        "com.fs.eiim.service.impl",
        "com.fs.eiim.task",
        "com.fs.eiim.restful"
})
public class EiimServiceConfig {
    @Bean
    public TaskFactory eiimTaskFactory(EiimInitializeTask eiimInitializeTask) {
        TaskFactory taskFactory = new TaskFactory();
        taskFactory.addSerialTask(eiimInitializeTask).runSerialTasks();
        return taskFactory;
    }
}
