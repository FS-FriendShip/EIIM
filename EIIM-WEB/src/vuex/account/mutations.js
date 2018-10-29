import * as types from './types.js'

export default {
  [types.UPDATE_ACCOUNT_CODE] (state, account) {
    if (!account.account.roles) {
      account.account.roles = [{code: 'User'}]
    }
    state.account = account
    localStorage.setItem('account-key', JSON.stringify(account))
  },

  [types.CLEAR_CACHE] (state) {
    state.account = {}

    localStorage.removeItem('account-key')
  },

  [types.UPDATE_ACCOUNT_PORTRAIT] (state, fileId) {
    state.account.avatar = fileId
    localStorage.setItem('account-key', JSON.stringify(state.account))
  }
}
