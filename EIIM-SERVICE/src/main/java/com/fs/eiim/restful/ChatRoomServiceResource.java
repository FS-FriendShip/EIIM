package com.fs.eiim.restful;

import com.fs.eiim.dal.entity.ChatMessage;
import com.fs.eiim.dal.entity.ChatNotice;
import com.fs.eiim.dal.entity.ChatRoom;
import com.fs.eiim.dal.entity.ChatRoomMember;
import com.fs.eiim.error.UserInterfaceEiimErrorException;
import com.fs.eiim.restful.vo.chat.*;
import com.fs.eiim.service.ChatRoomService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mx.StringUtils;
import org.mx.error.UserInterfaceException;
import org.mx.error.UserInterfaceSystemErrorException;
import org.mx.service.rest.vo.DataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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

    @Autowired
    public ChatRoomServiceResource(ChatRoomService chatRoomService) {
        super();
        this.chatRoomService = chatRoomService;
    }

    @Path("chatRooms")
    @GET
    public DataVO<List<ChatRoomInfoVO>> getAllChatRooms() {
        try {
            List<ChatRoom> chatRooms = chatRoomService.getAllChatRooms();
            return new DataVO<>(ChatRoomInfoVO.valueOf(chatRooms));
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            return new DataVO<>(new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.EIIM_OTHER_FAIL
            ));
        }
    }

    @Path("chatRooms/accounts/{accountId}")
    @GET
    public DataVO<List<ChatRoomInfoVO>> getAllChatRoomsByAccount(@PathParam("accountId") String accountId) {
        try {
            List<ChatRoom> chatRooms = chatRoomService.getAllChatRoomsByAccount(accountId);
            return new DataVO<>(ChatRoomInfoVO.valueOf(chatRooms));
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Get all chat rooms fail.", ex);
            }
            return new DataVO<>(new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.EIIM_OTHER_FAIL
            ));
        }
    }

    private DataVO<ChatRoomInfoVO> saveChatRoom(String id, String name, List<String> addAccountIds, List<String> delAccountIds) {
        ChatRoom chatRoom = chatRoomService.saveChatRoom(id, name, addAccountIds, delAccountIds);
        return new DataVO<>(ChatRoomInfoVO.valueOf(chatRoom));
    }

    @Path("chatRooms/new")
    @POST
    public DataVO<ChatRoomInfoVO> newChatRoom(ChatRoomFormVO chatRoomFormVO) {
        if (chatRoomFormVO == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        try {
            return saveChatRoom(null, chatRoomFormVO.getName(),
                    chatRoomFormVO.getAddAccountIds(), chatRoomFormVO.getDelAccountIds());
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Create a chat room fail.", ex);
            }
            return new DataVO<>(new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.EIIM_OTHER_FAIL
            ));
        }
    }

    @Path("chatRooms/{chatRoomId}")
    @POST
    public DataVO<ChatRoomInfoVO> modifyChatRoom(@PathParam("chatRoomId") String chatRoomId,
                                                 ChatRoomFormVO chatRoomFormVO) {
        if (StringUtils.isBlank(chatRoomId) || chatRoomFormVO == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        try {
            return saveChatRoom(chatRoomId, chatRoomFormVO.getName(),
                    chatRoomFormVO.getAddAccountIds(), chatRoomFormVO.getDelAccountIds());
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Modify the chat room fail.", ex);
            }
            return new DataVO<>(new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.EIIM_OTHER_FAIL
            ));
        }
    }

    @Path("chatRooms/{chatRoomId}/accounts/{accountId}")
    @DELETE
    public DataVO<Boolean> deleteChatRoom(@PathParam("chatRoomId") String chatRoomId,
                                          @PathParam("accountId") String accountId) {
        try {
            chatRoomService.deleteChatRoom(chatRoomId, accountId);
            return new DataVO<>(true);
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Delete the chat room fail.", ex);
            }
            return new DataVO<>(new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.EIIM_OTHER_FAIL
            ));
        }
    }

    @Path("chatRooms/{chatRoomId}/members")
    @PUT
    public DataVO<ChatRoomInfoVO> modifyChatRoomMembers(@PathParam("chatRoomId") String chatRoomId,
                                                        ChatRoomFormVO chatRoomFormVO) {
        if (StringUtils.isBlank(chatRoomId) || chatRoomFormVO == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        try {
            return saveChatRoom(chatRoomId, chatRoomFormVO.getName(),
                    chatRoomFormVO.getAddAccountIds(), chatRoomFormVO.getDelAccountIds());
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Modify the chat room member fail.", ex);
            }
            return new DataVO<>(new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.EIIM_OTHER_FAIL
            ));
        }
    }

    @Path("chatRooms/{chatRoomId}/members")
    @GET
    public DataVO<List<ChatRoomMemberVO>> getChatRoomMemberStatus(@PathParam("chatRoomId") String chatRoomId) {
        if (StringUtils.isBlank(chatRoomId)) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        try {
            List<ChatRoomMember> members = chatRoomService.getChatRoomMembers(chatRoomId);
            return new DataVO<>(ChatRoomMemberVO.valueOf(members));
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Get chat room member's status fail.", ex);
            }
            return new DataVO<>(new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.EIIM_OTHER_FAIL
            ));
        }
    }

    @Path("chatRooms/{chatRoomId}/members/status")
    @PUT
    public DataVO<ChatRoomMemberVO> changeChatRoomMemberStatus(@PathParam("chatRoomId") String chatRoomId,
                                                               ChatRoomMemberStatusVO statusVO) {
        if (StringUtils.isBlank(chatRoomId) || statusVO == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        try {
            ChatRoomMember member = chatRoomService.changeMemberStatus(chatRoomId, statusVO.getAccountId(),
                    statusVO.getStatus());
            return new DataVO<>(ChatRoomMemberVO.valueOf(member));
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Change the chat room member's status fail.", ex);
            }
            return new DataVO<>(new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.EIIM_OTHER_FAIL
            ));
        }
    }

    @Path("chatRooms/{chatRoomId}/top")
    @PUT
    public DataVO<ChatRoomMemberVO> topChatRoom(@PathParam("chatRoomId") String chatRoomId,
                                                ChatRoomMemberStatusVO statusVO) {
        if (StringUtils.isBlank(chatRoomId) || statusVO == null) {
            return new DataVO<>(new UserInterfaceSystemErrorException(
                    UserInterfaceSystemErrorException.SystemErrors.SYSTEM_ILLEGAL_PARAM
            ));
        }
        try {
            ChatRoomMember member = chatRoomService.topChatRoom(chatRoomId, statusVO.getAccountId(),
                    statusVO.isTop());
            return new DataVO<>(ChatRoomMemberVO.valueOf(member));
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Top the chat room fail.", ex);
            }
            return new DataVO<>(new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.EIIM_OTHER_FAIL
            ));
        }
    }

    @Path("chatRooms/{chatRoomId}/accounts/{accountId}/unread")
    @GET
    public DataVO<List<ChatMessageVO>> getAllUnreadMessages(@PathParam("chatRoomId") String chatRoomId,
                                                            @PathParam("accountId") String accountId) {
        try {
            List<ChatMessage> messages = chatRoomService.getAllUnreadMessage(chatRoomId, accountId);
            return new DataVO<>(ChatMessageVO.valueOf(messages));
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Get all unread message fail for the chat room.", ex);
            }
            return new DataVO<>(new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.EIIM_OTHER_FAIL
            ));
        }
    }

    @Path("chatRooms/{chatRoomId}/notices")
    @GET
    public DataVO<List<ChatNoticeVO>> getAllChatRoomNotices(@PathParam("chatRoomId") String chatRoomId) {
        try {
            List<ChatNotice> notices = chatRoomService.getAllChatRoomNotices(chatRoomId);
            return new DataVO<>(ChatNoticeVO.valueOf(notices));
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Get all notices fail for the chat room.", ex);
            }
            return new DataVO<>(new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.EIIM_OTHER_FAIL
            ));
        }
    }

    @Path("chatRooms/{chatRoomId}/notices/new")
    @POST
    public DataVO<Boolean> newChatRoomNotice(@PathParam("chatRoomId") String chatRoomId,
                                             ChatNoticeFormVO chatRoomNoticeFormVO) {
        chatRoomNoticeFormVO.setChatRoomId(chatRoomId);
        try {
            chatRoomService.saveChatRoomNotice(chatRoomNoticeFormVO.getChatRoomId(), null,
                    chatRoomNoticeFormVO.getNoticeType(), chatRoomNoticeFormVO.getNotice());
            return new DataVO<>(true);
        } catch (UserInterfaceException ex) {
            return new DataVO<>(ex);
        } catch (Exception ex) {
            if (logger.isErrorEnabled()) {
                logger.error("Create a new chat room notice fail.", ex);
            }
            return new DataVO<>(new UserInterfaceEiimErrorException(
                    UserInterfaceEiimErrorException.EiimErrors.EIIM_OTHER_FAIL
            ));
        }
    }
}
