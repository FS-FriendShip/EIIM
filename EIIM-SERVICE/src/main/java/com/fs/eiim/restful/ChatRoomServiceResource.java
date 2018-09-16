package com.fs.eiim.restful;

import com.fs.eiim.dal.entity.ChatMessage;
import com.fs.eiim.dal.entity.ChatNotice;
import com.fs.eiim.dal.entity.ChatRoom;
import com.fs.eiim.dal.entity.ChatRoomMember;
import com.fs.eiim.restful.vo.chat.*;
import com.fs.eiim.service.ChatRoomService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.StringUtils;
import org.mx.dal.Pagination;
import org.mx.dal.session.SessionDataStore;
import org.mx.error.UserInterfaceSystemErrorException;
import org.mx.service.rest.auth.RestAuthenticate;
import org.mx.service.rest.vo.DataVO;
import org.mx.service.rest.vo.PaginationDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述： 聊天室服务RESTful资源定义
 *
 * @author john peng
 * Date time 2018/8/15 下午3:14
 */
@Component("chatRoomServiceResource")
@Path("rest/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChatRoomServiceResource {
    private static final Log logger = LogFactory.getLog(ChatRoomServiceResource.class);

    private ChatRoomService chatRoomService;
    private SessionDataStore sessionDataStore;

    @Autowired
    public ChatRoomServiceResource(ChatRoomService chatRoomService, SessionDataStore sessionDataStore) {
        super();
        this.chatRoomService = chatRoomService;
        this.sessionDataStore = sessionDataStore;
    }

    @Path("chatRooms")
    @GET
    @RestAuthenticate
    public DataVO<List<ChatRoomInfoVO>> getAllChatRooms() {
        List<ChatRoom> chatRooms = chatRoomService.getAllChatRooms();
        return new DataVO<>(ChatRoomInfoVO.valueOf(chatRooms));
    }

    @Path("chatRooms/accounts/{accountCode}/summary")
    @POST
    @RestAuthenticate
    public DataVO<List<ChatRoomSummaryInfoVO>> getAllChatRoomsByAccount(@PathParam("accountCode") String accountCode,
                                                                        List<ChatRoomService.ChatRoomSummaryRequest> summaryReqeusts) {
        if (summaryReqeusts == null) {
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        List<ChatRoomSummaryInfoVO> list = new ArrayList<>();
        List<ChatRoomService.ChatRoomSummary> summaries = chatRoomService.getAllChatRoomsByAccount(accountCode, summaryReqeusts);
        if (summaries != null && !summaries.isEmpty()) {
            summaries.forEach(summary -> list.add(ChatRoomSummaryInfoVO.valueOf(summary)));
        }
        return new DataVO<>(list);
    }

    @Path("chatRooms/accounts/{accountCode}")
    @GET
    @RestAuthenticate
    public DataVO<List<ChatRoomInfoVO>> getAllChatRoomsByAccount(@PathParam("accountCode") String accountCode) {
        List<ChatRoom> chatRooms = chatRoomService.getAllChatRoomsByAccount(accountCode);
        return new DataVO<>(ChatRoomInfoVO.valueOf(chatRooms));
    }

    private DataVO<ChatRoomInfoVO> saveChatRoom(String id, String name, List<String> addAccountIds,
                                                List<String> delAccountIds, String creatorCode) {
        ChatRoom chatRoom = chatRoomService.saveChatRoom(id, name, addAccountIds, delAccountIds, creatorCode);
        sessionDataStore.removeCurrentUserCode();
        return new DataVO<>(ChatRoomInfoVO.valueOf(chatRoom));
    }

    @Path("chatRooms/new")
    @POST
    @RestAuthenticate
    public DataVO<ChatRoomInfoVO> newChatRoom(@QueryParam("accountCode") String accountCode, ChatRoomFormVO chatRoomFormVO) {
        if (chatRoomFormVO == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        sessionDataStore.setCurrentUserCode(accountCode);
        return saveChatRoom(null, chatRoomFormVO.getName(),
                chatRoomFormVO.getAddAccountCodes(), chatRoomFormVO.getDelAccountCodes(), accountCode);
    }

    @Path("chatRooms/{chatRoomId}")
    @PUT
    @RestAuthenticate
    public DataVO<ChatRoomInfoVO> modifyChatRoom(@PathParam("chatRoomId") String chatRoomId,
                                                 @QueryParam("accountCode") String accountCode,
                                                 ChatRoomFormVO chatRoomFormVO) {
        if (StringUtils.isBlank(chatRoomId) || chatRoomFormVO == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        sessionDataStore.setCurrentUserCode(accountCode);
        return saveChatRoom(chatRoomId, chatRoomFormVO.getName(),
                chatRoomFormVO.getAddAccountCodes(), chatRoomFormVO.getDelAccountCodes(), null);
    }

    @Path("chatRooms/{chatRoomId}/accounts/{accountCode}")
    @DELETE
    @RestAuthenticate
    public DataVO<Boolean> deleteChatRoom(@PathParam("chatRoomId") String chatRoomId,
                                          @PathParam("accountCode") String accountCode) {
        sessionDataStore.setCurrentUserCode(accountCode);
        chatRoomService.deleteChatRoom(chatRoomId, accountCode);
        sessionDataStore.removeCurrentUserCode();
        return new DataVO<>(true);
    }

    @Path("chatRooms/{chatRoomId}/members")
    @PUT
    @RestAuthenticate
    public DataVO<ChatRoomInfoVO> modifyChatRoomMembers(@PathParam("chatRoomId") String chatRoomId,
                                                        @QueryParam("accountCode") String accountCode,
                                                        ChatRoomFormVO chatRoomFormVO) {
        if (StringUtils.isBlank(chatRoomId) || chatRoomFormVO == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        sessionDataStore.setCurrentUserCode(accountCode);
        return saveChatRoom(chatRoomId, chatRoomFormVO.getName(),
                chatRoomFormVO.getAddAccountCodes(), chatRoomFormVO.getDelAccountCodes(), null);
    }

    @Path("chatRooms/{chatRoomId}/members")
    @GET
    @RestAuthenticate
    public DataVO<List<ChatRoomMemberVO>> getChatRoomMemberStatus(@PathParam("chatRoomId") String chatRoomId) {
        if (StringUtils.isBlank(chatRoomId)) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        List<ChatRoomMember> members = chatRoomService.getChatRoomMembers(chatRoomId);
        return new DataVO<>(ChatRoomMemberVO.valueOf(members));
    }

    @Path("chatRooms/{chatRoomId}/member/status")
    @PUT
    @RestAuthenticate
    public DataVO<ChatRoomMemberVO> changeChatRoomMemberStatus(@PathParam("chatRoomId") String chatRoomId,
                                                               @QueryParam("accountCode") String accountCode,
                                                               ChatRoomMemberStatusVO statusVO) {
        if (StringUtils.isBlank(chatRoomId) || statusVO == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        sessionDataStore.setCurrentUserCode(accountCode);
        ChatRoomMember member = chatRoomService.changeMemberStatus(chatRoomId, statusVO.getAccountCode(),
                statusVO.getStatus());
        sessionDataStore.removeCurrentUserCode();
        return new DataVO<>(ChatRoomMemberVO.valueOf(member));
    }

    @Path("chatRooms/{chatRoomId}/top")
    @PUT
    @RestAuthenticate
    public DataVO<ChatRoomMemberVO> topChatRoom(@PathParam("chatRoomId") String chatRoomId,
                                                @QueryParam("accountCode") String accountCode,
                                                ChatRoomMemberStatusVO statusVO) {
        if (StringUtils.isBlank(chatRoomId) || statusVO == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        sessionDataStore.setCurrentUserCode(accountCode);
        ChatRoomMember member = chatRoomService.topChatRoom(chatRoomId, statusVO.getAccountCode(),
                statusVO.isTop());
        sessionDataStore.removeCurrentUserCode();
        return new DataVO<>(ChatRoomMemberVO.valueOf(member));
    }

    @Path("chatRooms/{chatRoomId}/accounts/{accountCode}/unread")
    @GET
    @RestAuthenticate
    public DataVO<List<ChatMessageVO>> getAllUnreadMessages(@PathParam("chatRoomId") String chatRoomId,
                                                            @PathParam("accountCode") String accountCode) {
        List<ChatMessage> messages = chatRoomService.getAllUnreadMessages(chatRoomId, accountCode);
        return new DataVO<>(ChatMessageVO.valueOf(messages));
    }

    @Path("chatRooms/{chatRoomId}/accounts/{accountCode}/messages")
    @POST
    @RestAuthenticate
    @SuppressWarnings("unchecked")
    public DataVO<List<ChatMessageVO>> getAllMessagesByRequest(@PathParam("chatRoomId") String chatRoomId,
                                                               @PathParam("accountCode") String accountCode,
                                                               MessageRequestVO messageRequestVO) {
        if (messageRequestVO == null) {
            throw new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            );
        }
        String lastMessageId = messageRequestVO.getLastMessageId();
        ChatRoomService.Direction direction = messageRequestVO.getDirection();
        Pagination pagination = messageRequestVO.getPagination();
        List<ChatMessage> messages = chatRoomService.getMessagesByRequest(chatRoomId, accountCode, lastMessageId,
                direction, pagination);
        if (pagination != null) {
            return new PaginationDataVO<>(pagination, ChatMessageVO.valueOf(messages));
        } else {
            return new DataVO<>(ChatMessageVO.valueOf(messages));
        }
    }

    @Path("chatRooms/accounts/{accountCode}/unread")
    @GET
    @RestAuthenticate
    public DataVO<List<ChatRoomMessageInfoVO>> getAllUnreadMessage(@PathParam("accountCode") String accountCode) {
        List<ChatRoomService.ChatRoomMessageTuple> tuples = chatRoomService.getAllUnreadMessages(accountCode);
        return new DataVO<>(ChatRoomMessageInfoVO.valueOf(tuples));
    }

    @Path("chatRooms/{chatRoomId}/notices")
    @GET
    @RestAuthenticate
    public DataVO<List<ChatNoticeVO>> getAllChatRoomNotices(@PathParam("chatRoomId") String chatRoomId) {
        List<ChatNotice> notices = chatRoomService.getAllChatRoomNotices(chatRoomId);
        return new DataVO<>(ChatNoticeVO.valueOf(notices));
    }

    @Path("chatRooms/{chatRoomId}/notices/new")
    @POST
    @RestAuthenticate
    public DataVO<Boolean> newChatRoomNotice(@PathParam("chatRoomId") String chatRoomId,
                                             @QueryParam("accountCode") String accountCode,
                                             ChatNoticeFormVO chatRoomNoticeFormVO) {
        chatRoomNoticeFormVO.setChatRoomId(chatRoomId);
        sessionDataStore.setCurrentUserCode(accountCode);
        chatRoomService.saveChatRoomNotice(chatRoomNoticeFormVO.getChatRoomId(), null,
                chatRoomNoticeFormVO.getNoticeType(), chatRoomNoticeFormVO.getNotice());
        sessionDataStore.removeCurrentUserCode();
        return new DataVO<>(true);
    }
}
