/**
 *
 */
export default {
  account: localStorage.getItem('account-key') ? JSON.parse(localStorage.getItem('account-key')) : {}
}
