import * as types from './types.js'
import api from '../../api/restApi.js'

export default {
  api_clear_cache: ({commit}) => {
    commit(types.CLEAR_CACHE)
  },

  /**
   * 登录api
   * @param commit
   * @param account
   */
  api_account_login: ({commit}, account) => {
    return api.login(account.accountCode, account.password).then(res => {
      commit(types.UPDATE_ACCOUNT_CODE, res.data)

      return res.data
    })
  },

  api_account_logout: ({commit}, account) => {
    return api.logout(account.code, account.id).then(res => {
      return res.data
    })
  },

  /**
   *
   * @param commit
   * @param account
   * @returns {*|PromiseLike<T>|Promise<T>}
   */
  api_account_save: ({commit}, data) => {
    return api.saveAccountInfo(data.personId, data.account).then(res => {
      if (res.errorCode === 0) {
        return res.data
      }
    })
  },

  /**
   * 设置密码
   * @param commit
   * @param data
   * @returns {Promise<T>}
   */
  api_account_password: ({commit}, data) => {
    return api.setAccountPassword(data).then(res => {
      return res.data
    })
  },

  /**
   * 激活帐号
   * @param commit
   * @param data
   * @returns {Promise<T>}
   */
  api_account_enable: ({commit}, data) => {
    console.log(data)
    // return data
    return api.enableAccountInfo(data.id, data.account.code).then(res => {
      return res.data
    })
  },

  /**
   * 禁用帐号
   * @param commit
   * @param data
   * @returns {Promise<T>}
   */
  api_account_disable: ({commit}, data) => {
    console.log(data)
    // return data
    return api.disableAccountInfo(data.id, data.account.code).then(res => {
      return res.data
    })
  }
}
