import {dateFormat} from '../../common/utils'
export default {
  /**
   * 初始化聊天室信息。在系统启动时候调用
   * @param state
   * @param getters
   */
  api_get_chatrooms: (state, getters) => {
    let chatrooms = state.chatrooms
    if (chatrooms) {
      chatrooms.forEach(chatroom => {
        let latestMessage = chatroom.latestMessage
        if (latestMessage) {
          let subtitle = ''

          let account = JSON.parse(localStorage.getItem('account-key')).account.account
          if (account.code !== latestMessage.sender.code) {
            subtitle += latestMessage.sender.nickname
            subtitle += ':'
          }

          if (latestMessage.messageType === 'FILE') {
            subtitle += latestMessage.message.fileName
          } else if (latestMessage.messageType === 'TEXT') {
            subtitle += latestMessage.message.text
          }

          chatroom.subtitle = subtitle
        }
      })
    }
    return chatrooms
  },

  /**
   * 获取指定聊天室的聊天信息，在进入聊天室调用
   * @param state
   * @param getters
   * @returns {function(*): T[]}
   */
  api_get_chatroom: (state, getters) => {
    let roomId = state.selectedSessionId
    let chatroom = {}
    if (roomId) {
      chatroom = state.chatrooms[0]

      if (roomId) {
        chatroom = state.chatrooms.find(room => room.id === roomId)
      }

      let sentTime = ''
      if (chatroom.messages) {
        chatroom.messages.forEach(message => {
          let dateTime = new Date(message.sentTime)
          if (sentTime === dateFormat(dateTime, 'yyyy-MM-dd hh:mm')) {
            message.showTime = false
          } else {
            message.showTime = true
            sentTime = dateFormat(dateTime, 'yyyy-MM-dd hh:mm')
          }
        })
      }
    }

    return chatroom
  },

  /**
   * 选择聊天室
   * @param commit
   * @param roomId
   */
  api_search_chatroom: (state, getters) => {
    return function (key) {
      let sessions = localStorage.getItem('Chatrooms') ? JSON.parse(localStorage.getItem('Chatrooms')) : []

      if (key) {
        state.chatrooms = sessions.filter(session => {
          return session.name.includes(key)
        })
      } else {
        state.chatrooms = sessions
      }
    }
  },

  /**
   * 获取当前会话成员清单
   * @param state
   * @param getters
   */
  api_session_member_list: (state, getters) => {
    let roomId = state.selectedSessionId

    if (!roomId) {
      return state.chatrooms[0].members
    } else {
      return state.chatrooms.find(room => room.id === roomId).members
    }
  }
}
