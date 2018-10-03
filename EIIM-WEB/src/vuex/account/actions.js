import * as types from './types.js'
import api from '../../api/restApi.js'

export default {
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
    return api.logout(account.accountCode, account.id).then(res => {
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
    console.log(data)
    return api.saveAccountInfo(data.personId, data.account).then(res => {
      return res.data
    })
  }
}
