import * as types from './types.js'

export default {
  /**
   * 创建会话
   * @param state
   * @param chatroom
   */
  [types.CREATE_CHATROOM] (state, chatroom) {
    state.chatrooms.unshift(chatroom)
    localStorage.setItem('Chatrooms', JSON.stringify(state.chatrooms))
  },

  /**
   * 更新会话列表
   * @param state
   * @param payload
   */
  [types.UPDATE_CHATROOM] (state, payload) {
    let localChatrooms = state.chatrooms

    // 添加到vuex
    let chatrooms = payload.chatrooms
    let accountCode = payload.accountCode
    let found = false

    chatrooms.forEach(item => {
      item.read = false
      item.quiet = false
      if (!item.name) {
        item.name = item.creator.nickname
      }
      if (item.members.length > 2) {
        item.group = true
      } else {
        item.group = false
      }

      if (!item.messages) {
        item.messages = []
      }
      item.messages.forEach(message => {
        if (accountCode === message.sender.code) {
          message.owner = 'self'
        } else {
          message.owner = 'other'
        }
      })

      // 更新本地聊天会话
      localChatrooms.forEach(localRoom => {
        if (localRoom.id === item.id) {
          found = true
          item.messages.forEach(message => localRoom.messages.push(message))
        }
      })

      if (!found) {
        if (item.messages.length === 0) {
          item.messages.push({message: '未知消息', sentTime: Date()})
        }
        localChatrooms.push(item)
      }
    })

    state.chatrooms = localChatrooms
    if (!state.selectedSessionId && localChatrooms) {
      state.selectedSessionId = localChatrooms[0].id
    }
    localStorage.setItem('Chatrooms', JSON.stringify(state.chatrooms))
  },

  [types.DELETE_CHATROOM] (state, id) {
    // 删除数组中的值
    let index = state.chatrooms.findIndex(item => item.id === id)
    state.chatrooms.splice(index, 1)
    localStorage.setItem('Chatrooms', JSON.stringify(state.chatrooms))
  },

  /**
   * 添加聊天信息
   * @param state
   * @param message
   */
  [types.UPDATE_CHATROOM_MESSAGE] (state, message) {
    let roomId = message.chatRoomId
    let chatrooms = state.chatrooms
    chatrooms.forEach(chatroom => {
      if (chatroom.id === roomId) {
        chatroom.messages.push(message)
      }
    })

    state.chatrooms = chatrooms
    localStorage.setItem('Chatrooms', JSON.stringify(state.chatrooms))
  },

  [types.SELECT_CHATROOM] (state, roomId) {
    state.selectedSessionId = roomId
  }
}
