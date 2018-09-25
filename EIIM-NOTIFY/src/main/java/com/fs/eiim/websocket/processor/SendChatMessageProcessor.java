package com.fs.eiim.websocket.processor;

import com.alibaba.fastjson.JSONObject;
import com.fs.eiim.dal.entity.Account;
import com.fs.eiim.dal.entity.Attachment;
import com.fs.eiim.dal.entity.ChatMessage;
import com.fs.eiim.dal.entity.ChatRoomMember;
import com.fs.eiim.error.UserInterfaceEiimErrorException;
import com.fs.eiim.service.ChatRoomService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.mx.StringUtils;
import org.mx.comps.notify.client.command.Command;
import org.mx.comps.notify.online.OnlineDevice;
import org.mx.comps.notify.online.OnlineManager;
import org.mx.comps.notify.processor.MessageProcessor;
import org.mx.comps.notify.processor.NotifyProcessor;
import org.mx.dal.session.SessionDataStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mx.comps.notify.client.NotifyBean.TarType.Devices;

/**
 * 描述： 发送聊天消息命令处理器
 *
 * @author john peng
 * Date time 2018/8/24 下午6:35
 */
public class SendChatMessageProcessor implements MessageProcessor {
    private static final Log logger = LogFactory.getLog(SendChatMessageProcessor.class);

    private String command = "sendChatMessage";

    private ChatRoomService chatRoomService;
    private NotifyProcessor notifyProcessor;
    private OnlineManager onlineManager;
    private SessionDataStore sessionDataStore;

    public SendChatMessageProcessor(ChatRoomService chatRoomService, NotifyProcessor notifyProcessor,
                                    OnlineManager onlineManager, SessionDataStore sessionDataStore) {
        super();
        this.chatRoomService = chatRoomService;
        this.notifyProcessor = notifyProcessor;
        this.onlineManager = onlineManager;
        this.sessionDataStore = sessionDataStore;
    }

    @Override
    public String getCommand() {
        return command;
    }

    @Override
    public boolean processJsonCommand(Session session, JSONObject json) {
        String command = json.getString("command");
        String type = json.getString("type");
        if (this.command.equals(command) && type.equalsIgnoreCase(Command.CommandType.USER.name())) {
            JSONObject payload = json.getJSONObject("payload");
            if (payload == null) {
                if (logger.isWarnEnabled()) {
                    logger.warn(String.format("The command[%s]'s payload is null.", command));
                }
            } else {
                String deviceId = payload.getString("deviceId"),
                        accountCode = payload.getString("accountCode"),
                        eiimCode = payload.getString("eiimCode"),
                        chatRoomId = payload.getString("chatRoomId"),
                        messageType = payload.getString("messageType"),
                        message = payload.getString("message");
                if (StringUtils.isBlank(accountCode) || StringUtils.isBlank(chatRoomId) ||
                        StringUtils.isBlank(message) || StringUtils.isBlank(messageType)) {
                    if (logger.isWarnEnabled()) {
                        logger.warn(String.format("The parameter invalid, account code: %s, chat room id: %s, " +
                                "message type: %s, message: %s.", accountCode, chatRoomId, messageType, message));
                    }
                } else {
                    sessionDataStore.setCurrentUserCode(accountCode);
                    // 调用消息保存服务
                    ChatMessage chatMessage = chatRoomService.saveChatMessage(accountCode, eiimCode, chatRoomId,
                            ChatMessage.MessageType.valueOf(messageType), message);
                    Account sender = chatRoomService.getMessageSender(accountCode);
                    if (sender == null) {
                        if (logger.isErrorEnabled()) {
                            logger.error(String.format("The sender[%s] not found.", accountCode));
                        }
                        throw new UserInterfaceEiimErrorException(
                                UserInterfaceEiimErrorException.EiimErrors.ACCOUNT_NOT_FOUND
                        );
                    }
                    Set<ChatRoomMember> members = chatMessage.getChatRoom().getMembers();
                    // 获取在本聊天室中的在线用户
                    Set<OnlineDevice> onlineDevices = onlineManager.getOnlineDevices(Collections.singletonList(
                            onlineDevice -> {
                                for (ChatRoomMember member : members) {
                                    if (onlineDevice.getDeviceId().startsWith(member.getAccount().getCode())) {
                                        return true;
                                    }
                                }
                                return false;
                            }
                    ));
                    if (onlineDevices != null && !onlineDevices.isEmpty()) {
                        // 封装聊天数据推送对象
                        List<String> deviceIds = new ArrayList<>();
                        onlineDevices.forEach(onlineDevice -> deviceIds.add(onlineDevice.getDeviceId()));
                        JSONObject notifyMessage = new JSONObject();
                        notifyMessage.put("src", deviceId);
                        notifyMessage.put("deviceId", deviceId);
                        notifyMessage.put("tarType", Devices.name());
                        notifyMessage.put("tar", StringUtils.merge(deviceIds));
                        notifyMessage.put("messageId", "chatMessage");
                        notifyMessage.put("version", "1.0");
                        JSONObject data = new JSONObject();
                        data.put("chatRoomId", chatMessage.getId());
                        data.put("chatMessageId", chatMessage.getId());
                        data.put("messageType", messageType);
                        JSONObject msg = new JSONObject();
                        if (ChatMessage.MessageType.valueOf(messageType) == ChatMessage.MessageType.FILE) {
                            Attachment attachment = chatRoomService.getMessageAttachmentById(message);
                            msg.put("id", attachment.getId());
                            msg.put("fileName", attachment.getFileName());
                            msg.put("fileType", attachment.getFileType());
                            msg.put("fileSize", attachment.getFileSize());
                        } else {
                            msg.put("text", message);
                        }
                        data.put("message", msg);
                        data.put("sentTime", System.currentTimeMillis());
                        JSONObject senderData = new JSONObject();
                        senderData.put("id", sender.getId());
                        senderData.put("code", sender.getCode());
                        senderData.put("nickname", sender.getNickName());
                        senderData.put("avatar", sender.getAvatar());
                        data.put("sender", senderData);
                        notifyMessage.put("message", data);

                        JSONObject retPayload = new JSONObject();
                        retPayload.put("chatMessageId", chatMessage.getId());
                        // 推送聊天数据
                        notifyProcessor.notifyProcess(notifyMessage, retPayload);
                    } else {
                        if (logger.isWarnEnabled()) {
                            logger.warn(String.format("The chat room[%s] has none member online.", chatRoomId));
                        }
                    }
                    sessionDataStore.removeCurrentUserCode();
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean processBinaryData(Session session, byte[] buffer) {
        // Do nothing
        return false;
    }
}
