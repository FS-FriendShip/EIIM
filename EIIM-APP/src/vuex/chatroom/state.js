/**
 * 聊天状态
 */
export default {
  chatrooms: localStorage.getItem('Chatrooms') ? JSON.parse(localStorage.getItem('Chatrooms')) : []
}
