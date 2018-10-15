import store from '../vuex'
export default {
  name: 'websocket',

  data () {
    return {
      websocket: null,
      account: localStorage.getItem('account-key') ? JSON.parse(localStorage.getItem('account-key')).account : {}
    }
  },

  destroyed () {
    this.websocket.close()
  },

  initWebSocket () { // 初始化weosocket
    if (localStorage.getItem('account-key')) {
      let account = JSON.parse(localStorage.getItem('account-key')).account
      this.account = account
      if (!this.websocket || this.websocket.readyState !== 1) {
        //const wsuri = 'ws://localhost:9997/notify' // ws地址
        // const wsuri = 'ws://60.173.195.18:60993/notify'
        this.websocket = new WebSocket(wsuri)
        this.websocket.onopen = () => {
          this.register()
          console.log('WebSocket连接成功')
        }

        this.websocket.onerror = this.websocketonerror

        this.websocket.onmessage = (e) => {
          let data = JSON.parse(e.data)
          let messageId = data.messageId

          // 聊天信息处理
          if (messageId === 'chatMessage') {
            let message = data.message
            if (message.sender.code === this.account.account.code) {
              message.owner = 'self'
            }

            store.dispatch('chatroom/update_chatroom_message', message)
          }
        }

        this.websocket.onclose = this.websocketclose
      }
    }
  },

  /**
   * 注册
   */
  register () {
    console.log(this.account)
    let accountCode = this.account.account.code
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
    let data = JSON.parse(e.data)
    let messageId = data.messageId

    // 聊天信息处理
    if (messageId === 'chatMessage') {
      let message = data.message
      if (message.sender.code === this.account.account.code) {
        message.owner = 'self'
      }
      store.dispatch('chatroom/update_chatroom_message', message)
    }
  },

  send (content) { // 数据发送
    let accountCode = this.account.account.code

    content.deviceId = accountCode + '.pc'
    content.accountCode = accountCode
    let message = {
      command: 'sendChatMessage',
      type: 'USER',
      payload: content
    }
    this.websocket.send(JSON.stringify(message))
  },

  websocketclose (e) { // 关闭
    console.log('connection closed (' + e.code + ')')
  }
}
