import store from '../vuex'
import { Message } from 'element-ui'

export default {
  name: 'websocket',

  data () {
    return {
      websocket: null,
      socketStatus: false,
      account: localStorage.getItem('account-key') ? JSON.parse(localStorage.getItem('account-key')).account : {}
    }
  },

  destroyed () {
    this.websocket.close()
  },

  initWebSocket () { // 初始化weosocket
    if (this.socketStatus) {
      console.log('Closing websocket......')
      this.websocket.close()
    }

    if (localStorage.getItem('account-key')) {
      let account = JSON.parse(localStorage.getItem('account-key'))
      this.account = account
      if (!this.websocket || this.websocket.readyState !== 1) {
        let websocketPrefix = 'ws://'
        let http = process.env.API_SERVER_ENV
        if (http.substr(0, 5) === 'https') {
          websocketPrefix = 'wss://'
        }

        const wsuri = websocketPrefix + process.env.WEBSOCKET_ENV + '/notify'// ws地址
        console.log(wsuri)
        this.websocket = new WebSocket(wsuri)
        this.websocket.onopen = () => {
          console.log('WebSocket连接成功')
          this.socketStatus = true
          this.register()
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
    this.socketStatus = false
    console.log('WebSocket连接发生错误')
    Message.error('WebSocket连接发生错误。异常码=' + e.code)
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
    this.socketStatus = false
    console.log('WebSocket连接关闭')
  }
}
