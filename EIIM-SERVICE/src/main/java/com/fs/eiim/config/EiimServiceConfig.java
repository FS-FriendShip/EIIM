package com.fs.eiim.config;

import com.fs.eiim.restful.*;
import com.fs.eiim.task.EiimInitializeTask;
import org.mx.service.rest.graphql.GraphQLTypeExecution;
import org.mx.service.rest.graphql.GraphQLUtils;
import org.mx.service.server.config.ServerConfig;
import org.mx.spring.config.SpringConfig;
import org.mx.spring.task.TaskFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 描述： EIIM相关RESTful服务定义配置类
 *
 * @author john peng
 * Date time 2018/8/8 下午2:38
 */
@Import({
        SpringConfig.class,
        ServerConfig.class,
})
@ComponentScan({
        "com.fs.eiim.service.impl",
        "com.fs.eiim.task",
        "com.fs.eiim.graphql",
        "com.fs.eiim.restful"
})
public class EiimServiceConfig {
    @Bean("taskFactoryEiim")
    public TaskFactory eiimTaskFactory(EiimInitializeTask eiimInitializeTask) {
        TaskFactory taskFactory = new TaskFactory();
        taskFactory.addSerialTask(eiimInitializeTask).runSerialTasks();
        return taskFactory;
    }

    @Bean("graphQLUtils")
    public GraphQLUtils eiimGraphQLUtils(ApplicationContext context) {
        GraphQLUtils graphQLUtils = new GraphQLUtils();
        InputStream in = EiimServiceConfig.class.getResourceAsStream("/eiim.graphqls");
        List<GraphQLTypeExecution> typeExecutions = new ArrayList<>(
                context.getBeansOfType(GraphQLTypeExecution.class).values()
        );
        graphQLUtils.init(in, typeExecutions);
        return graphQLUtils;
    }

    @Bean("restfulClassesEiim")
    public List<Class<?>> restfulClassesEiim() {
        return Arrays.asList(PersonServiceResource.class, AccountServiceResource.class, ChatRoomServiceResource.class,
                OrgServiceResource.class, BaseDataServiceResource.class, FileTransformResource.class,
                GraphQLResource.class);
    }
}
