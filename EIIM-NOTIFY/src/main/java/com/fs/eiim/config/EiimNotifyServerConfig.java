package com.fs.eiim.config;

import com.fs.eiim.service.ChatRoomService;
import com.fs.eiim.websocket.processor.SendChatMessageProcessor;
import org.mx.comps.notify.config.NotificationServerConfig;
import org.mx.comps.notify.online.OnlineManager;
import org.mx.comps.notify.processor.NotifyProcessor;
import org.mx.dal.session.SessionDataStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan({
        "com.fs.eiim.websocket",
        "com.fs.eiim.service.impl"
})
@Import({
        NotificationServerConfig.class
})
public class EiimNotifyServerConfig {
    /**
     * 创建一个发送聊天消息的处理器
     *
     * @param chatRoomService  聊天室服务接口
     * @param notifyProcessor  推送处理器接口
     * @param onlineManager    在线设备管理器接口
     * @param sessionDataStore 会话存储器接口
     * @return 处理器
     */
    @Bean(name = "sendChatMessageCommandProcessor")
    public SendChatMessageProcessor sendChatMessageProcessor(ChatRoomService chatRoomService,
                                                             NotifyProcessor notifyProcessor,
                                                             OnlineManager onlineManager,
                                                             SessionDataStore sessionDataStore) {
        return new SendChatMessageProcessor(chatRoomService, notifyProcessor, onlineManager, sessionDataStore);
    }
}
