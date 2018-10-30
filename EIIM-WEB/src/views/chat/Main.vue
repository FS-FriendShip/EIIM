<template>
  <el-container id = "Chat" v-upload>
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
        <Message v-bind:style="{height:messageHeight}" v-on:toggleSessionInfo="toggleSessionInfo"></Message>
        <SessionInfo v-show="showSessionInfo" :session="session"></SessionInfo>
      </el-row>
      <el-row class="message-footer">
        <MessageSend></MessageSend>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
import Session from '../../components/chat/Session'
import MessageSend from '../../components/chat/MessageSend'
import Message from '../../components/chat/Message'
import SessionInfo from '../../components/chat/SessionInfo'
import {mapGetters} from 'vuex'

export default {
  name: 'Main',
  components: {MessageSend, Message, Session, SessionInfo},

  data () {
    return {
      showSessionInfo: false,
      messageHeight: (window.innerHeight - 200) + 'px'
    }
  },

  methods: {
    toggleSessionInfo () {
      this.showSessionInfo = !this.showSessionInfo
    }
  },

  created  () {
    let account = this.GLOBAL.getAccount()
    // 获取聊天室信息
    this.$store.dispatch('chatroom/api_get_chatrooms', account).then(() => {
      this.sessionDialogVisible = true

      let params = {session: this.session, account: account}
      if (this.session) {
        this.$store.dispatch('chatroom/api_select_chatroom', params)
      }
    })
  },

  computed: {
    ...mapGetters({session: 'chatroom/api_get_chatroom'})
  },

  mounted () {
    const that = this

    window.onresize = function windowResize () {
      that.messageHeight = (window.innerHeight - 200) + 'px'
    }
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
  #Chat {
    width: 100%;
    height: 100%;
    padding: 0;

    text-align: left;
    color: #fff;
    background: #fff;
  }

  #Chat .session {
    width:400px!important;
    background: #fff;
  }

  #Chat .message-main{
    padding: 0px;
    overflow: hidden;
    color: #000!important;
    background-color: #eee;
  }

  #Chat .message-main .message-header{
    padding:0px;
    width: 100%;
    height:50px;
    border-bottom: 1px solid silver;
  }

  #Chat .message-main .message-header .header-item{
    height: 100%;
    line-height: 30px;
    padding: 10px 20px;
    text-align: left;
  }

  #Chat .message-main .message-header .header-item .session-name {
    font-weight: bolder;
    font-size: 18px;
  }

  #Chat .message-main .message-header .header-item .icon-Set-up {
    font-size: 24px;
  }

  #Chat .message-main .message-body{
    padding:0px;
    width: 100%;
    /*height:650px;*/
  }

  #Chat .message-main .message-footer{
    bottom:0px;
    padding:0px;
    width: 100%;
    height: 150px;
  }
</style>
