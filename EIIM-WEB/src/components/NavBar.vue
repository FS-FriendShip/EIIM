<template>
  <div class="card">
    <el-row>
      <el-col :span="24" justify="center" align="middle">
        <el-popover
          placement="right-start"
          width="300"
          trigger="click">
          <div>
            <el-row>
              <el-col :span="12">
                <span>{{currentUser.account.nickname}}</span>
              </el-col>

              <el-col :span="12">
                <img class="avatar-large" :src="currentUser.account.avatar">
              </el-col>
            </el-row>
            <el-row style="margin-top:50px" justify="center">
              <el-col :span="12" style="text-align:center">
                <i class="iconfont icon-zhongzhi quit" @click="setPassword">修改密码</i>
              </el-col>
              <el-col :span="12"  style="text-align:center">
                <i class="iconfont icon-tuichu quit" @click="logout">退出</i>
              </el-col>
            </el-row>
          </div>
          <img class="avatar-large" :src="currentUser.account.avatar" slot="reference">
        </el-popover>

        <AccountDialog v-on:setAccountDialogVisible="setAccountDialogVisible" :context="context" :show.sync="showAccountDialog"></AccountDialog>
      </el-col>
    </el-row>

    <el-row style="margin-top:20px">
      <el-col :span="24" justify="center" align="middle">
        <img class="avatar-large" src="../assets/chat.png"  title="聊天" @click="showChat">
      </el-col>
    </el-row>

    <el-row style="margin-top:20px">
      <el-col :span="24" justify="center" align="middle">
        <img class="avatar-large" src="../assets/contacts.png"  title="通讯录" @click="showContact">
      </el-col>
    </el-row>

    <el-row style="margin-top:20px">
      <el-col :span="24" justify="center" align="middle">
        <img class="avatar-large" src="../assets/users.png"  title="用户管理" @click="showUsers">
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import AccountDialog from './AccountDialog'

export default {
  name: 'Profile',

  components: {AccountDialog},

  data () {
    return {
      showAccountDialog: false,
      context: {
        type: null,
        action: null,
        data: null
      }
    }
  },

  methods: {
    logout () {
      this.$store.dispatch('account/api_account_logout', this.currentUser.account).then(res =>
        this.$router.push({name: 'Login'})
      )
    },

    showChat () {
     this.$router.push({name: 'Chat'})
    },

    showContact () {
      this.$router.push({name: 'Contact'})
    },

    showUsers () {
      this.$router.push({name: 'Admin'})
    },

    /**
     *  设置密码
     *
     * */
    setPassword () {
      let account = this.GLOBAL.account.account
      let row = {fullName: account.nickname, mobile: account.code}
      this.context = {type: 'ACCOUNT', action: 'reset', data: row}
      this.showAccountDialog = true
    },

    setAccountDialogVisible (visible) {
      this.showAccountDialog = visible
    }
  },

  computed: {
    ...mapGetters({currentUser: 'account/api_current_account'})
  }
}
</script>

<style scoped lang="less">
  .card {
    padding:12px;
    width:75px!important;
  }
  ///*.card {*/
    /*padding: 12px;*/

    /*footer {*/
      /*margin-top: 10px;*/
    /*}*/

    /*.avatar, .name {*/
      /*vertical-align: middle;*/
    /*}*/
    /*.avatar {*/
      /*border-radius: 2px;*/
    /*}*/
    /*.name {*/
      /*display: inline-block;*/
      /*margin: 0 0 0 15px;*/
      /*font-size: 16px;*/
    /*}*/
  ///*}*/
</style>
