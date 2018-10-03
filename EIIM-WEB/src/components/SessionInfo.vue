<template>
  <div id="SessionInfo" class="SessionInfo" v-if="session">
    <el-container>
      <el-header height="40px"><el-button class="button-add" icon="iconfont icon-add" @click="showSessionDialog">添加成员</el-button></el-header>
      <el-main style="padding:5px">
        <ul>
          <li v-for="item in session.members"  :key="item.account.id" class="user-wrap">
            <img class="avatar" width="30" height="30" :src="item.account.avatar">
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
import SessionDialog from '../components/SessionDialog'
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
      this.$store.dispatch('chatroom/api_quite_chatroom', {sessionId: this.session.id, accountCode: this.GLOBAL.account.accountCode}).then(res => this.$emit('toggleSessionInfo'))
    },

    showSessionDialog () {
      this.sessionDialogVisible = true
    },

    resetDialogVisible (visible) {
      this.sessionDialogVisible = visible
    }
  },

  watch: {
    session () {
      this.activeSession = this.session
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
