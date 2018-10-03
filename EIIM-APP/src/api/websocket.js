import store from '../vuex'
export default {
  name: 'websocket',

  data () {
    return {
      websocket: null,
      account: null
    }
  },

  destroyed () {
    this.websocket.close()
  },

  initWebSocket (account) { // 初始化weosocket
    if (!this.websocket || this.websocket.readyState !== 1) {
      this.account = account

      const wsuri = 'ws://localhost:9997/notify' // ws地址
      this.websocket = new WebSocket(wsuri)
      this.websocket.onopen = () => {
        this.register()
        console.log('WebSocket连接成功')
      }

      this.websocket.onerror = this.websocketonerror

      this.websocket.onmessage = this.websocketonmessage
      this.websocket.onclose = this.websocketclose
    }
  },

  /**
   * 注册
   */
  register () {
    let accountCode = this.account.accountCode
    let token = this.account.token

    let register = {
      command: 'registry',
      type: 'SYSTEM',
      payload: {
        deviceId: accountCode + '.pc',
        state: '',
        longtitude: 0.0,
        latitude: 0.0,
        extraData: {
          accountCode: accountCode,
          eiimCode: '',
          token: token
        }
      }
    }
    this.websocket.send(JSON.stringify(register))

    let ping = {
      command: 'ping',
      type: 'SYSTEM',
      payload: {
        deviceId: accountCode + '.pc',
        state: '',
        longtitude: 0.0,
        latitude: 0.0,
        extraData: {
          accountCode: accountCode,
          eiimCode: '',
          token: token
        }
      }
    }
    let websocket = this.websocket
    setInterval(function () {
      websocket.send(JSON.stringify(ping))
    }, 10000)
  },

  websocketonerror (e) { // 错误
    console.log('WebSocket连接发生错误')
  },

  websocketonmessage (e) { // 数据接收
    console.log(e)
    let data = JSON.parse(e.data)
    let messageId = data.messageId

    // 聊天信息处理
    if (messageId === 'chatMessage') {
      let message = data.message
      store.dispatch('chatroom/update_chatroom_message', message)
    }
  },

  send (content) { // 数据发送
    let accountCode = this.account.accountCode

    content.deviceId = accountCode + '.pc'
    content.accountCode = accountCode

    let message = {
      command: 'sendChatMessage',
      type: 'USER',
      payload: content
    }
    console.log(message)
    this.websocket.send(JSON.stringify(message))
  },

  websocketclose (e) { // 关闭
    console.log('connection closed (' + e.code + ')')
  }
}
