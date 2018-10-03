export default {
  // 切换语言 后期需要
  switchLang (state, lang) {
    state.currentLang = lang
    // Vue.config.lang = lang
    document.cookie = 'VR_LANG=' + lang + '; path=/;domain=.snail.com'
    // location.reload()
  },
  // 设置当前页面名字
  setPageName (state, name) {
    state.currentPageName = name
  },
  toggleHeaderStatus (state, status) {
    state.headerStatus = status
  },
  // 切换“微信”页中右上角菜单
  toggleTipsStatus (state, status) {
    if (status === -1) {
      state.tipsStatus = false
    } else {
      state.tipsStatus = !state.tipsStatus
    }
  },
  // 增加未读消息数
  addNewMsg (state) {
    state.newMsgCount > 99 ? state.newMsgCount = '99+' : state.newMsgCount++
  },
  // 减少未读消息数
  minusNewMsg (state) {
    state.newMsgCount < 1 ? state.newMsgCount = 0 : state.newMsgCount--
  },
  // 将消息置顶 待完成
  setMsgStick (state, mid) {

  }
}