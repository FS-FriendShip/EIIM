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
      return res.data
    })
  },

  api_account_enable: ({commit}, data) => {
    return data
    // return api.enableAccountInfo(data.personId, data.account).then(res => {
    //   return res.data
    // })
  },

  api_account_disable: ({commit}, data) => {
    return data
    // return api.disableAccountInfo(data.personId, data.account).then(res => {
    //   return res.data
    // })
  }
}
