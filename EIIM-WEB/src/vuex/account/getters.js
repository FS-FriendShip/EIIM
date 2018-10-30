export default {
  account_code: state => {
    return state.account.accountCode
  },

  api_get_account: state => {
    return state.account.account
  },

  api_current_account: state => {
    let account = state.account.account
    if (account) {
      account.avatar = process.env.FILE_SERVER_ENV + account.avatar
      return account
    } else {
      return {}
    }
  }
}
