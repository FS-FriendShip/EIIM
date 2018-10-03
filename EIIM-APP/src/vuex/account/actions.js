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
      let data = {accountCode: account.accountCode, token: res.data.token, deviceKey: account.accountCode}
      commit(types.UPDATE_ACCOUNT_CODE, data)

      return data
    })
  }
}
