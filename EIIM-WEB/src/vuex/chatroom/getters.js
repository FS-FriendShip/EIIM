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
      let account = JSON.parse(localStorage.getItem('account-key')).account.account
      chatrooms.forEach(chatroom => {
        let latestMessage = chatroom.latestMessage
        if (latestMessage) {
          let subtitle = ''

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

        if (chatroom.members.length === 2) {
          chatroom.members.forEach(member => {
            if (member.account.id !== account.id) {
              chatroom.name = member.account.nickname
            }
          })
        }

        if (!chatroom.creator.avatar.startsWith('http')) {
          chatroom.creator.avatar = process.env.FILE_SERVER_ENV + chatroom.creator.avatar
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
    let chatroom = {}

    if (state.selectedSessionId) {
      let roomId = state.selectedSessionId
      if (roomId) {
        if (roomId) {
          chatroom = state.chatrooms.find(room => room.id === roomId)
        }

        if (!chatroom) {
          chatroom = state.chatrooms[0]
        }

        if (chatroom && chatroom.members) {
          chatroom.members.forEach(member => {
            if (!member.account.avatar.startsWith('http')) {
              member.account.avatar = process.env.FILE_SERVER_ENV + member.account.avatar
            }
          })
        }

        let sentTime = ''
        if (chatroom && chatroom.messages) {
          chatroom.messages.forEach(message => {
            let dateTime = new Date(message.sentTime)
            if (sentTime === dateFormat(dateTime, 'yyyy-MM-dd hh:mm')) {
              message.showTime = false
            } else {
              message.showTime = true
              sentTime = dateFormat(dateTime, 'yyyy-MM-dd hh:mm')
            }

            if (message.sender && !message.sender.avatar.startsWith('http')) {
              message.sender.avatar = process.env.FILE_SERVER_ENV + message.sender.avatar
            }

            if (message.messageType === 'FILE') {
              if (!message.message.download || !message.message.download.startsWith('http')) {
                message.message.download = process.env.FILE_SERVER_ENV + message.message.id
              }
            }
          })
        }
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
