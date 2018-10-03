import * as types from './types.js'

export default {
  [types.UPDATE_ACCOUNT_CODE] (state, account) {
    state.account = account

    localStorage.setItem('account-key', JSON.stringify(state))
  }
}