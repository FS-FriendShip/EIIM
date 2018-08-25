package com.fs.eiim.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({
        "classpath:cache.properties",
        "classpath:mongodb.properties",
        "classpath:notify.properties",
        "classpath:server.properties",
        "classpath:cache-ehcache.properties",
        "classpath:cache-redis.properties",
        "classpath:redis.properties"
})
@Import({
        EiimNotifyServerConfig.class,
        EiimMongodbConfig.class,
        EiimServiceConfig.class
})
public class EiimAppConfig {
}
