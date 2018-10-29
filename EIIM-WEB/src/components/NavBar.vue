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
                <span>{{currentUser.nickname}}</span>
              </el-col>

              <el-col :span="12">
                <el-upload
                class="avatar-uploader"
                action="upload"
                :beforeUpload = "sendPortrait"
                :show-file-list="false">
                  <img class="avatar-large" :src="currentUser.avatar">
                <!--<img class="avatar-large" :src="currentUser.avatar" slot="reference">-->
                </el-upload>
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

          <img class="avatar-large" :src="currentUser.avatar" slot="reference">

        </el-popover>
      </el-col>
    </el-row>

    <el-row style="margin-top:20px">
      <el-col :span="24" justify="center" align="middle">
        <img class="avatar-small" src="../assets/chat.png"  title="聊天" @click="showChat">
      </el-col>
    </el-row>

    <el-row style="margin-top:20px">
      <el-col :span="24" justify="center" align="middle">
        <img class="avatar-small" src="../assets/contacts.png"  title="通讯录" @click="showContact">
      </el-col>
    </el-row>

    <el-row v-if="currentUser.roles[0].code === 'Administrator'" style="margin-top:20px">
      <el-col :span="24" justify="center" align="middle">
        <img class="avatar-small" src="../assets/users.png"  title="用户管理" @click="showUsers">
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'NavBar',

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
    beforeAvatarUpload(file) {
      const isJPG = (file.type === 'image/jpeg' || file.type === 'image/png') ;
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 或 PNG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    },

    /**
     * 文件上传
     *
     * */
    sendPortrait (params) {
      let _this = this
      if(this.beforeAvatarUpload(params)){
        this.$store.dispatch('chatroom/api_upload_file', params).then((data) => {
          this.$store.dispatch('account/api_portrait_set', {
            fileId: data.id,
            account: _this.GLOBAL.getAccount()
          })
        })
      }

      return false
    },

    logout () {
      this.$store.dispatch('account/api_account_logout', this.currentUser).then(res =>
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
      let account = this.GLOBAL.getAccount()
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
