<template>
  <div id="SessionInfo" class="SessionInfo" v-if="activeSession">
    <el-container>
      <el-header height="40px"><el-button class="button-add" icon="iconfont icon-add" @click="showSessionDialog">添加成员</el-button></el-header>
      <el-main style="padding:0px 5px">
        <ul style="padding:0px;">
          <li v-for="item in activeSession.members"  :key="item.account.id" class="user-wrap">
            <img class="avatar-small" :src="item.account.avatar">
            <p class="name">{{item.account.nickname}}</p>
          </li>
        </ul>
      </el-main>
      <el-footer height="40px"><el-button class="button-quit" @click="quitSession" v-if="session.members.length>2" type="primary" plain>退出群聊</el-button></el-footer>
    </el-container>

    <SessionDialog v-on:resetDialogVisible="resetDialogVisible" :session="activeSession" :show.sync="sessionDialogVisible"></SessionDialog>
  </div>
</template>

<script>
import SessionDialog from './SessionDialog'
export default {
  name: 'SessionInfo',
  components: {SessionDialog},
  data () {
    return {
      sessionDialogVisible: false,
      activeSession: null
    }
  },

  props: {
    session: null
  },

  methods: {
    quitSession  () {
      this.$store.dispatch('chatroom/api_quite_chatroom', {sessionId: this.session.id, accountCode: this.GLOBAL.getAccount()}).then(res => this.$emit('toggleSessionInfo'))
    },

    showSessionDialog () {
      this.$store.dispatch('contact/api_get_contacts').then((data) => {
        this.sessionDialogVisible = true
      })
    },

    resetDialogVisible (visible) {
      this.sessionDialogVisible = visible
    }
  },

  watch: {
    session () {
      if (this.session) {
        this.activeSession = this.session
      } else {
        this.activeSession = {}
      }
    }
  }
}
</script>

<style scoped>
  .SessionInfo {
    height: 100%;
    width: 150px;
    position: absolute;
    top: 0px;
    right: 0px;
    background: #fff;
  }

  .SessionInfo .button-quit {
    position:absolute;
    bottom: 0px;
    left: 0px;
    width: 100%
  }

  .SessionInfo .button-add {
    position:absolute;
    top: 0px;
    left: 0px;
    width: 100%
  }

  .user-wrap {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    padding:0px 5px
  }

  .user-wrap .name{
    padding-left:10px;
    width: 70%;
    text-align:left;
  }
</style>
