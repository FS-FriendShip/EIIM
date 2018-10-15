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
                <img class="avatar-large" :src="'/rest/v1/download/' + currentUser.account.avatar">
              </el-col>
            </el-row>
            <el-row style="margin-top:50px">
              <el-col :span="12">
                <i class="iconfont icon-zhongzhi quit" @click="setPassword">修改密码</i>
              </el-col>
              <el-col :span="12">
                <i class="iconfont icon-tuichu quit" @click="logout">退出</i>
              </el-col>
            </el-row>
          </div>
          <img class="avatar-large" :src="'/rest/v1/download/' + currentUser.account.avatar" slot="reference">
        </el-popover>

        <AccountDialog v-on:setAccountDialogVisible="setAccountDialogVisible" :context="context" :show.sync="showAccountDialog"></AccountDialog>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import AccountDialog from '../AccountDialog'

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

    /**
     *  设置密码
     *
     * */
    setPassword () {
      let account = this.GLOBAL.account.account
      console.log(account)
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
