export default {
  /**
   * 初始化聊天室信息。在系统启动时候调用
   * @param state
   * @param getters
   */
  getChatrooms: (state, getters) => {
    return function (roomId) {
      return state.chatrooms.filter(room => room.id === roomId)
    }
  },

  /**
   *
   * @param state
   * @returns {chatrooms|{}|*}
   */
  chatroomsList: state => {
    return state.chatrooms
  },

  /**
   * 获取指定聊天室的聊天信息，在进入聊天室调用
   * @param state
   * @param getters
   * @returns {function(*): T[]}
   */
  getChatroom: (state, getters) => {
    return function (roomId) {
      return state.chatrooms.filter(room => room.id === roomId)
    }
  }
}
