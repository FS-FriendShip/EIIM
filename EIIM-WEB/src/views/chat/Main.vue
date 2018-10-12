<template>
  <el-container id="Main" v-upload>
    <el-aside>
      <Profile></Profile>
      <Session></Session>
    </el-aside>

    <el-main class="message-main">
      <el-row class="message-header" align="middle">
        <el-col :span="22" :class="'header-item'"><div class="session-name">{{session?session.name:''}}</div></el-col>
        <el-col :span="2" :class="'header-item'">
          <i class="iconfont icon-Set-up" @click="toggleSessionInfo"></i>
        </el-col>
      </el-row>
      <el-row class="message-body">
        <Message v-on:toggleSessionInfo="toggleSessionInfo"></Message>
        <SessionInfo v-show="showSessionInfo" :session="session"></SessionInfo>
      </el-row>
      <el-row class="message-footer">
        <MessageSend></MessageSend>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import Profile from '../../components/Profile'
import Session from '../../components/Session'
import MessageSend from '../../components/MessageSend'
import Message from '../../components/Message'
import SessionInfo from '../../components/SessionInfo'
import websocket from '../../api/websocket'
import {mapGetters} from 'vuex'

export default {
  name: 'Main',
  components: {Profile, MessageSend, Message, Session, SessionInfo},

  data () {
    return {
      showSessionInfo: false
    }
  },

  methods: {
    toggleSessionInfo () {
      this.showSessionInfo = !this.showSessionInfo
    }
  },

  created  () {
    let account = this.user
    this.GLOBAL.account = account
    websocket.initWebSocket()

    // 获取聊天室信息
    this.$store.dispatch('chatroom/api_get_chatrooms', account.account)
  },

  computed: {
    ...mapGetters({session: 'chatroom/api_get_chatroom', user: 'account/api_current_account'})
  },

  directives: {
    upload: {
      bind (el) {
        el.addEventListener('paste', function (event) {
        })
      }
    }
  }

  // mounted () {
  //   let session = this.session
  //   let store = this.$store
  //   document.addEventListener('paste', function (event) {
  //     console.log(event.dataTransfer.files)
  //     let clipboardData = event.clipboardData
  //     if (clipboardData) {
  //       let items = clipboardData.items
  //       if (items && items.length) {
  //         let item = null
  //         for (var i = 0; i < clipboardData.types.length; i++) {
  //           if (clipboardData.types[i] === 'Files') {
  //             item = items[i]
  //             if (item && item.kind === 'file') {
  //               console.log('sending file')
  //               store.dispatch('chatroom/api_upload_file', {
  //                 sessionId: session.id,
  //                 file: item.getAsFile()
  //               }).then(res => {
  //                 if (res) {
  //                   // this.message = ''
  //                 }
  //               })
  //             }
  //           }
  //         }
  //       }
  //     }
  //   })
  // }
}
</script>

<style scoped>
  #Main {
    overflow: none;
    border-radius: 3px;
    margin:50px 50px;
  }

  #Main .el-aside {
    text-align: left;
    color: #fff;
    background: #2e3238;
  }

  #Main>.el-main {
    background-color: #eee;
  }

  /*#Main .message {*/
    /*height: 800px;*/
  /*}*/

  #Main .message-main{
    padding: 0px;
    overflow: hidden;
  }

  #Main .message-main .message-header{
    padding:0px;
    width: 100%;
    height:50px;
    border-bottom: 1px solid silver;
  }

  #Main .message-main .message-header .header-item{
    height: 100%;
    line-height: 30px;
    padding: 10px 20px;
    text-align: left;
  }

  #Main .message-main .message-header .header-item .session-name {
    font-weight: bolder;
    font-size: 18px;
  }

  #Main .message-main .message-header .header-item .icon-Set-up {
    font-size: 24px;
  }

  #Main .message-main .message-body{
    padding:0px;
    width: 100%;
    height:600px;
    overflow: auto;
  }

  #Main .message-main .message-footer{
    padding:0px;
    width: 100%;
    height:150px;
    background: #fff;
  }
</style>
