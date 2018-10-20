package com.fs.eiim.service;

import com.fs.eiim.dal.entity.*;
import org.mx.dal.Pagination;

import java.util.List;

/**
 * 描述： 聊天室服务接口定义
 *
 * @author john peng
 * Date time 2018/8/15 下午3:16
 */
public interface ChatRoomService {
    List<ChatRoom> getAllChatRooms();

    List<ChatRoom> getAllChatRoomsByAccount(String accountCode);

    List<ChatRoomSummary> getAllChatRoomsByAccount(String accountCode, List<ChatRoomSummaryRequest> summaryRequests);

    ChatRoom saveChatRoom(String chatRoomId, String chatRoomName, List<String> addAccountCodes,
                          List<String> delAccountCodes, String creatorCode);

    void deleteChatRoom(String chatRoomId, String accountCode);

    Account getMessageSender(String accountCode);

    Attachment getMessageAttachmentById(String id);

    List<ChatRoomMember> getChatRoomMembers(String chatRoomId);

    ChatRoomMember topChatRoom(String chatRoomId, String accountCode, boolean isTop);

    ChatRoomMember changeMemberStatus(String chatRoomId, String accountCode, String status);

    List<ChatMessage> getAllUnreadMessages(String chatRoomId, String accountCode);

    List<ChatRoomMessageTuple> getAllUnreadMessages(String accountCode);

    List<ChatMessage> getMessagesByRequest(String chatRoomId, String accountCode, String lastMessageId,
                                           Direction direction, Pagination pagination);

    List<ChatMessage> getMessagesByRequest(String accountCode, String lastMessageId,
                                           Direction direction, Pagination pagination);

    ChatMessage saveChatMessage(String accoutnCode, String eiimCode, String chatRoomId, ChatMessage.MessageType messageType, String message);

    List<ChatNotice> getAllChatRoomNotices(String chatRoomId);

    void saveChatRoomNotice(String chatRoomId, String noticeId, String noticeType, String notice);

    enum Direction {
        FORWARD, BACKWARD;
    }

    class ChatRoomMessageTuple {
        private ChatRoom chatRoom;
        private List<ChatMessage> messages;

        public ChatRoomMessageTuple() {
            super();
        }

        public ChatRoomMessageTuple(ChatRoom chatRoom, List<ChatMessage> messages) {
            this();
            this.chatRoom = chatRoom;
            this.messages = messages;
        }

        public ChatRoom getChatRoom() {
            return chatRoom;
        }

        public List<ChatMessage> getMessages() {
            return messages;
        }
    }

    class ChatRoomSummaryRequest {
        private String chatRoomId, lastMessageId;
        private Direction direction = Direction.FORWARD;

        public String getChatRoomId() {
            return chatRoomId;
        }

        public void setChatRoomId(String chatRoomId) {
            this.chatRoomId = chatRoomId;
        }

        public String getLastMessageId() {
            return lastMessageId;
        }

        public void setLastMessageId(String lastMessageId) {
            this.lastMessageId = lastMessageId;
        }

        public Direction getDirection() {
            return direction;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }
    }

    class ChatRoomSummary {
        private ChatRoom chatRoom;
        private int unread;
        private ChatMessage latestMessage;

        public ChatRoomSummary(ChatRoom chatRoom, int unread, ChatMessage latestMessage) {
            super();
            this.chatRoom = chatRoom;
            this.unread = unread;
            this.latestMessage = latestMessage;
        }

        public ChatRoom getChatRoom() {
            return chatRoom;
        }

        public int getUnread() {
            return unread;
        }

        public ChatMessage getLatestMessage() {
            return latestMessage;
        }
    }
}
