/**
 * 聊天状态
 */
export default {
  selectedSessionId: null,
  chatrooms: localStorage.getItem('Chatrooms') ? JSON.parse(localStorage.getItem('Chatrooms')) : []
}
