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

    List<ChatRoom> getAllChatRoomsByAccount(String accountId);

    ChatRoomTuple saveChatRoom(String chatRoomId, String chatRoomName, List<String> addAccountIds,
                               List<String> delAccountIds);

    void deleteChatRoom(String chatRoomId);

    List<ChatRoomMember> getChatRoomMembers(String chatRoomId);

    ChatRoomMember topChatRoom(String chatRoomId, String accountId, boolean isTop);

    ChatRoomMember changeMemberStatus(String chatRoomId, String accountId, String status);

    List<ChatMessage> getAllUnreadMessage(String chatRoomId, String accountId);

    List<ChatNotice> getAllChatRoomNotices(String chatRoomId);

    void saveChatRoomNotice(ChatNotice notice);

    class ChatRoomTuple {
        private ChatRoom chatRoom;
        private List<ChatRoomMember> members;

        public ChatRoomTuple(ChatRoom chatRoom, List<ChatRoomMember> members) {
            super();
            this.chatRoom = chatRoom;
            this.members = members;
        }

        public ChatRoom getChatRoom() {
            return chatRoom;
        }

        public List<ChatRoomMember> getMembers() {
            return members;
        }
    }
}
