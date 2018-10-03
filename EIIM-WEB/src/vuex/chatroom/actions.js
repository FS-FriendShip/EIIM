import api from '../../api/restApi.js'
import * as types from './types'
import websocket from '../../api/websocket'

export default {
  /**
   * 获取用户聊天室
   * @param commit
   * @param account
   */
  api_get_chatrooms: ({commit}, account) => {
    let accountCode = account.accountCode
    api.getChatrooms(accountCode).then(res => {
      commit(types.UPDATE_CHATROOM, {chatrooms: res.data, accountCode: accountCode})
    })
  },

  /**
   * 获取聊天室消息
   * @param commit
   * @param account
   */
  api_get_chatroom: ({commit}, roomId, account) => {
    let accountCode = account.accountCode
    api.getUnreadChatMsg(roomId, accountCode).then(res => {
      res.data.forEach(room => (room.msgs = {}))
      commit(types.UPDATE_CHATROOM_MESSAGE, roomId, res.data)
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
    console.log(data)
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
   * 选择聊天室
   * @param commit
   * @param roomId
   */
  api_select_chatroom: ({commit}, roomId) => {
    commit(types.SELECT_CHATROOM, roomId)
  },

  /**
   * 聊天文件上传
   * @param commit
   * @param data
   */
  api_upload_file: ({commit}, data) => {
    let formData = new FormData()
    formData.append('file', data.file)
    formData.append('type', data.file.type)

    return api.uploadFile(formData).then(res => {
      return res.data
    })
  }
}