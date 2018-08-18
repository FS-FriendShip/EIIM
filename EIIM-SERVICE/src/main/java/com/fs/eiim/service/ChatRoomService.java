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

    ChatRoom saveChatRoom(String chatRoomId, String chatRoomName, List<String> addAccountCodes,
                               List<String> delAccountCodes);

    void deleteChatRoom(String chatRoomId, String accountId);

    List<ChatRoomMember> getChatRoomMembers(String chatRoomId);

    ChatRoomMember topChatRoom(String chatRoomId, String accountCode, boolean isTop);

    ChatRoomMember changeMemberStatus(String chatRoomId, String accountCode, String status);

    List<ChatMessage> getAllUnreadMessage(String chatRoomId, String accountId);

    List<ChatNotice> getAllChatRoomNotices(String chatRoomId);

    void saveChatRoomNotice(String chatRoomId, String noticeId, String noticeType, String notice);
}
