export default {
  account_code: state => {
    return state.account.accountCode
  },

  account: state => {
    return state.account.account
  },

  getCurrentUser: state => {
    if (localStorage.getItem('account-key')) {
      let account = JSON.parse(localStorage.getItem('account-key')).account
      return account
    } else {
      return {}
    }
  }
}
