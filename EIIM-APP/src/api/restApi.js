import {post, get} from './http'

export default {
  /**
   * 登录接口
   * @param accountCode
   * @param password
   */
  login (accountCode, password) {
    return post('/v1/login', {accountCode: accountCode, password: password})
  },

  /**
   * 获取所有聊天室列表信息
   * @param accountCode
   * @returns {*}
   */
  getChatrooms (accountCode) {
    return get('/v1/chatRooms/accounts/' + accountCode + '/unread')
  },

  /**
   * 创建聊天室
   * @param chatRoom
   * @returns {Promise}
   */
  createChatrooms (chatRoom) {
    return post('/v1/chatRooms/new', chatRoom)
  },

  /**
   * 获取通讯录
   * @returns {Promise}
   */
  getContacts () {
    return get('/v1/persons')
  },

  /**
   *
   * @param personId
   * @returns {Promise}
   */
  getContact (personId) {
    return get('/v1/persons/' + personId)
  },

  /**
   * 获取某个用户在聊天室中未读消息
   * @param accountCode
   * @param roomId
   * @param fromIndex
   * @returns {Promise}
   */
  getUnreadChatMsg (accountCode, roomId, fromIndex) {
    return get('/v1/chatRooms/' + roomId + '/accounts/' + accountCode + '/unread')
  }
}
