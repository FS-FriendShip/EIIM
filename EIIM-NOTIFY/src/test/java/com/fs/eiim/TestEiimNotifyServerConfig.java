package com.fs.eiim;

import com.fs.eiim.config.EiimNotifyServerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource({
        "classpath:server.properties"
})
@Import({
        EiimNotifyServerConfig.class
})
public class TestEiimNotifyServerConfig {
}
