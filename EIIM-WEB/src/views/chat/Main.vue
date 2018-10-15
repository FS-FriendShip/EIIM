<template>
  <el-container id="Main" v-upload>
    <el-aside class="nav">
      <NavBar>

      </NavBar>
    </el-aside>
    <el-container class="chat">
      <el-aside class="session">
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
  </el-container>
</template>

<script>
import NavBar from '../../components/chat/NavBar'
import Session from '../../components/chat/Session'
import MessageSend from '../../components/chat/MessageSend'
import Message from '../../components/chat/Message'
import SessionInfo from '../../components/chat/SessionInfo'
import websocket from '../../api/websocket'
import {mapGetters} from 'vuex'

export default {
  name: 'Main',
  components: {NavBar, MessageSend, Message, Session, SessionInfo},

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
    this.$store.dispatch('chatroom/api_get_chatrooms', account.account).then(() => {
      this.sessionDialogVisible = true

      let params = {session: this.session, account: this.GLOBAL.account.account}
      if (this.session) {
        this.$store.dispatch('chatroom/api_select_chatroom', params)
      }
    })
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
}
</script>

<style scoped>
  #Main {
    width: 100%;
    height: 100%;
    padding: 0;
  }

  #Main .nav {
    width:75px!important;
    background: #4F4F4F;
  }

  #Main .chat {
    text-align: left;
    color: #fff;
    background: #fff;
  }

  #Main .message-main{
    padding: 0px;
    overflow: hidden;
    color: #000!important;
    background-color: #eee;
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
    height:700px;
    overflow: auto;
  }

  #Main .message-main .message-footer{
    padding:0px;
    width: 100%;
    height:200px;
  }
</style>
