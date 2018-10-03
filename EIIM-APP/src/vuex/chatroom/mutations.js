import * as types from './types.js'

export default {
  [types.CREATE_CHATROOM] (state, chatroom) {
    state.chatrooms.unshift(chatroom)
  },

  [types.UPDATE_CHATROOM] (state, payload) {
    let localChatrooms = state.chatrooms
    if (localChatrooms && localChatrooms.length > 0) {
      return
    }

    // 添加到vuex
    let chatrooms = payload.chatrooms
    let accountCode = payload.accountCode
    let found = false

    chatrooms.forEach(item => {
      item.read = false
      item.quiet = false

      if (item.members.length > 2) {
        item.group = true
      } else {
        item.group = false
      }

      item.messages.forEach(message => {
        if (accountCode === message.sender.code) {
          message.owner = 'self'
        } else {
          message.owner = 'other'
        }
      })

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
    localStorage.setItem('Chatrooms', JSON.stringify(state.chatrooms))
  },

  [types.DELETE_CHATROOM] (state, id) {
    let chatroom = state.chatrooms[`id`]
    state.chatrooms[`id`] = Object.assign({}, chatroom, {is_delete: 1})
  },

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
  }
}
