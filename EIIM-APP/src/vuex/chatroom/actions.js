import api from '../../api/restApi.js'
import * as types from './types'

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
    console.log(account)
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
  api_new_chatroom: ({commit}, contact) => {
    let chatroom = {
      name: contact.nickname,
      addAccountCodes: [contact[0].account.code]
    }

    return api.createChatrooms(chatroom).then(res => {
      commit(types.CREATE_CHATROOM, res.data)

      return res.data
    })
  },

  update_chatroom_message: ({commit}, message) => {
    commit(types.UPDATE_CHATROOM_MESSAGE, message)
  }
}
