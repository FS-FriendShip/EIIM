export default {
  account_code: state => {
    return state.account.accountCode
  },

  api_get_account: state => {
    return state.account.account
  },

  api_current_account: state => {
    if (localStorage.getItem('account-key')) {
      let account = JSON.parse(localStorage.getItem('account-key')).account

      account.account.avatar = process.env.FILE_SERVER_ENV + account.account.avatar
      return account
    } else {
      return {}
    }
  }
}
