package com.fs.eiim;

import com.fs.eiim.config.EiimMongodbConfig;
import com.fs.eiim.config.EiimServiceConfig;
import org.mx.spring.config.SpringCacheConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({
        "classpath:server.properties",
        "classpath:mongodb.properties",
        "classpath:cache.properties"
})
@Import({
        EiimServiceConfig.class,
        EiimMongodbConfig.class,
        SpringCacheConfig.class
})
public class TestConfig {
}
