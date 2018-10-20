import api from '../../api/restApi.js'
import * as types from './types'
import websocket from '../../api/websocket'

export default {
  api_clear_cache: ({commit}) => {
    commit(types.CLEAR_CACHE)
  },

  /**
   * 获取用户聊天室
   * @param commit
   * @param account
   */
  api_get_chatrooms: ({commit}, account) => {
    let accountCode = account.code
    api.getChatrooms(accountCode).then(res => {
      commit(types.UPDATE_CHATROOM, {chatrooms: res.data, accountCode: accountCode})
    })
  },

  /**
   * 选择聊天室
   * @param commit
   * @param roomId
   */
  api_select_chatroom: ({commit}, params) => {
    let session = params.session
    if (session && session.id) {
      let latestMessageId = 0
      if (session.latestMessage) {
        latestMessageId = session.latestMessage.id
      }

      let account = params.account
      let apiParams = {
        accountCode: account.code,
        sessionId: session.id,
        lastMessageId: latestMessageId,
        direction: 'FORWARD',
        pageable: false
      }
      api.getChatroom(apiParams).then(res => {
        if (res.data) {
          res.data.forEach(message => {
            message.chatRoomId = session.id
            if (account.code === message.sender.code) {
              message.owner = 'self'
            } else {
              message.owner = 'other'
            }

            commit(types.UPDATE_CHATROOM_MESSAGE, message)
          })
        }

        commit(types.SELECT_CHATROOM, session.id)
      })
    }
  },

  /**
   * 获取所未读消息
   * @param commit
   * @param params
   */
  api_get_unread: ({commit}, params) => {
    let latestMessageId = 0
    let account = params.account
    let apiParams = {
      accountCode: account.code,
      lastMessageId: latestMessageId,
      direction: 'FORWARD',
      pageable: false
    }
    api.getUnreadMessages(apiParams).then(res => {
      if (res.data) {
        res.data.forEach(room => {
          room.messages.forEach(message => {
            if (account.code === message.sender.code) {
              message.owner = 'self'
            } else {
              message.owner = 'other'
            }
          })
        })

        commit(types.UPDATE_CHATROOM, res.data)
      }
    })
  },

  /**
   * 新建聊天室
   * @param commit
   * @param contact
   * @returns {Promise<T>}
   */
  api_new_chatroom: ({commit}, session) => {
    let chatroom = {
      name: session.name,
      addAccountCodes: session.accountCodes
    }

    api.createChatrooms(chatroom).then(res => {
      commit(types.CREATE_CHATROOM, res.data)
    })
  },

  /**
   * 聊天室添加或者移除成员
   * @param commit
   * @param session
   */
  api_update_chatroom_members: ({commit}, session) => {
    api.updateChatroomMembers(session).then(res => {
      commit(types.UPDATE_CHATROOM_MEMBER, session)
    })
  },

  /**
   * 删除指定会话
   * @param commit
   * @param session
   */
  api_delete_chatroom: ({commit}, sessionId) => {
    commit(types.DELETE_CHATROOM, sessionId)
  },

  /**
   * 发送聊天信息
   * @param commit
   * @param sessionId
   * @param message
   */
  api_send_text_message: ({commit}, data) => {
    websocket.send({
      chatRoomId: data.sessionId,
      messageType: 'TEXT',
      message: data.message
    })

    return true
  },

  /**
   * 发送文件
   * @param commit
   * @param data
   */
  api_send_file_message: ({commit}, data) => {
    websocket.send({
      chatRoomId: data.sessionId,
      messageType: 'FILE',
      message: data.message.id
    })

    return true
  },

  /**
   * 退出群聊聊天室
   * @param commit
   * @param session
   */
  api_quite_chatroom: ({commit}, session) => {
    return api.quitChatroom(session).then(res => {
      commit(types.DELETE_CHATROOM, session.sessionId)

      return res.data
    })
  },

  /**
   * 更新聊天会话消息
   * @param commit
   * @param message
   */
  update_chatroom_message: ({commit}, message) => {
    let messageId = message.chatMessageId
    message.id = messageId
    delete message.chatMessageId
    commit(types.UPDATE_CHATROOM_MESSAGE, message)
  },

  /**
   * 聊天文件上传
   * @param commit
   * @param data
   */
  api_upload_file: ({commit}, data) => {
    let formData = new FormData()
    console.log(data)
    formData.append('file', data.file)
    formData.append('type', data.file.type)

    return api.uploadFile(formData).then(res => {
      return res.data
    })
  },

  /**
   * 置顶
   * @param commit
   * @param data
   */
  api_top_session: ({commit}, sessionId) => {
    commit(types.TOP_CHATROOM, sessionId)
  }
}
