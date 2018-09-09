package com.fs.eiim.service;

import com.fs.eiim.dal.entity.ChatMessage;
import com.fs.eiim.dal.entity.ChatNotice;
import com.fs.eiim.dal.entity.ChatRoom;
import com.fs.eiim.dal.entity.ChatRoomMember;

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

    ChatRoom saveChatRoom(String chatRoomId, String chatRoomName, List<String> addAccountCodes,
                          List<String> delAccountCodes, String creatorCode);

    void deleteChatRoom(String chatRoomId, String accountCode);

    List<ChatRoomMember> getChatRoomMembers(String chatRoomId);

    ChatRoomMember topChatRoom(String chatRoomId, String accountCode, boolean isTop);

    ChatRoomMember changeMemberStatus(String chatRoomId, String accountCode, String status);

    List<ChatMessage> getAllUnreadMessages(String chatRoomId, String accountCode);

    List<ChatRoomMessageTuple> getAllUnreadMessages(String accountCode);

    ChatRoom saveChatMessage(String accoutnCode, String eiimCode, String chatRoomId, String messageType, String message);

    List<ChatNotice> getAllChatRoomNotices(String chatRoomId);

    void saveChatRoomNotice(String chatRoomId, String noticeId, String noticeType, String notice);

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
}
