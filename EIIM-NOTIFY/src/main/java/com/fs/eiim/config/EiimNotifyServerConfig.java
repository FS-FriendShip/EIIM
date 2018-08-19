package com.fs.eiim.config;

import org.mx.comps.notify.config.NotificationServerConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import({
        NotificationServerConfig.class
})
@ComponentScan({
        "com.fs.eiim.websocket",
        "com.fs.eiim.service.impl"
})
public class EiimNotifyServerConfig {
}
