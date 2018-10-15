import {post, get, remove, update} from './http'

export default {
  /**
   * 登录接口
   * @param accountCode
   * @param password
   */
  login (accountCode, password) {
    return post('/v1/login', {accountCode: accountCode, password: password})
  },

  logout (accountCode, personId) {
    return get('/v1/logout/' + personId)
  },

  /**
   * 设置帐号
   * @param personId
   * @param account
   * @returns {Promise}
   */
  saveAccountInfo (personId, account) {
    return post('/v1/persons/' + personId + '/enable', account)
  },

  /**
   * 启用帐号
   * @param personId
   * @param account
   * @returns {Promise}
   */
  enableAccountInfo (personId, account) {
    return get('/v1/persons/' + personId + '/valid?valid=true')
  },

  /**
   *
   * @param data
   */
  setAccountPassword (data) {
    return update('/v1/persons/account/password', data)
  },

  /**
   * 禁用帐号
   * @param personId
   * @param account
   * @returns {Promise}
   */
  disableAccountInfo  (personId, account) {
    return get('/v1/persons/' + personId + '/valid?valid=false')
  },

  /**
   * 获取所有聊天室列表信息
   * @param accountCode
   * @returns {*}
   */
  getChatrooms (accountCode) {
    return get('/v1/chatRooms/accounts/' + accountCode)
  },

  /**
   *
   * @param params
   * @returns {Promise}
   */
  getChatroom (params) {
    let sessionId = params.sessionId
    let accountCode = params.accountCode
    delete params['sessionId']
    delete params['accountCode']
    return post('/v1/chatRooms/' + sessionId + '/accounts/' + accountCode + '/messages', params)
  },

  /**
   * 新开一个聊天室
   * @param chatRoom
   * @returns {Promise}
   */
  createChatrooms (chatRoom) {
    return post('/v1/chatRooms/new', chatRoom)
  },

  /**
   * 添加或者移除聊天室中的成员
   * @param chatRoom
   * @returns {Promise}
   */
  updateChatroomMembers (chatRoom) {
    return update('v1/chatRooms/' + chatRoom.id + '/members', chatRoom)
  },

  /**
   *
   * @param session
   */
  quitChatroom (session) {
    return remove('/v1/chatRooms/' + session.sessionId + '/accounts/' + session.accountCode)
  },

  /**
   * 获取组织架构信息
   */
  getOrgs () {
    return get('/v1/orgs')
  },

  /**
   *
   * @param orgId
   * @returns {Promise}
   */
  getOrg (orgId) {
    return get('/v1/orgs/' + orgId)
  },

  /**
   * 保存机构信息
   * @param org
   * @returns {Promise}
   */
  saveOrg (org) {
    if (!org.id) {
      return post('/v1/orgs/new', org)
    } else {
      return update('/v1/orgs/' + org.id, org)
    }
  },

  /**
   *
   * @param orgId
   * @returns {boolean}
   */
  delOrg (orgId) {
    return get('/v1/orgs/' + orgId + '/valid?valid=false')
  },

  /**
   *
   * @param user
   * @returns {Promise}
   */
  saveUser (user) {
    if (!user.id) {
      return post('/v1/persons/new', user)
    } else {
      return update('/v1/persons/' + user.id, user)
    }
  },

  /**
   *
   * @param personId
   * @returns {Promise}
   */
  delUser (personId) {
    return get('/v1/persons/' + personId + '/valid?valid=false')
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
  },

  /**
   * 文件上传
   * @param FormData
   */
  uploadFile (file) {
    return post('/v1/upload', file)
  }
}
