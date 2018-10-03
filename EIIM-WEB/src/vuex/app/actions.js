export default {
  /**
   * 登录api
   * @param commit
   * @param account
   */
  change_page_title: ({commit}, pageName) => {
    commit('setPageName', pageName)
  }
}
