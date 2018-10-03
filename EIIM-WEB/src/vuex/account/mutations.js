import * as types from './types.js'

export default {
  [types.UPDATE_ACCOUNT_CODE] (state, account) {
    state.account = account

    if (account.accountCode !== 'Administrator') {
      localStorage.setItem('account-key', JSON.stringify(state))
    }
  }
}
