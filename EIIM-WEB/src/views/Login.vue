<template>
  <div>
  <el-form ref="LoginForm" :model="login" class="login-container">
    <div class="banner">
      <img style='width:100%;height:100px;' src="../assets/rhosoon.png">
    </div>

    <el-form-item prop="accountCode"  :rules="[{required: true, message: '用户名不能为空'}]">
      <el-input v-model="login.accountCode" placeholder="用户名"></el-input>
    </el-form-item>

    <el-form-item prop="password" :rules="[{required: true, message: '密码不能为空'}]">
      <el-input type="password"  v-model="login.password" placeholder="密码"></el-input>
    </el-form-item>

    <div v-show="error">
      <div>{{error}}</div>
    </div>

    <el-form-item style="width:100%;">
      <el-button  style="width:100%;" type="primary" @click="doLogin">登录</el-button>
    </el-form-item>
  </el-form>

  </div>
</template>

<script>
import websocket from '../api/websocket'
export default {
  name: 'Login',
  data () {
    return {
      login: {
        accountCode: '',
        password: ''
      },
      error: ''
    }
  },

  methods: {
    /**
     * 文件上传
     *
     * */
    sendFileMessage (params) {
      this.$store.dispatch('chatroom/api_upload_file', params).then((data) => {

      })

      return false
    },

    doLogin: function () {
      this.$store.dispatch('account/api_clear_cache')
      this.$store.dispatch('chatroom/api_clear_cache')

      this.$refs['LoginForm'].validate((valid) => {
        if (valid) {
          this.$store.dispatch('account/api_account_login', this.login).then((data) => {
            websocket.initWebSocket()
            this.$router.push({name: 'Chat'})
          })
        } else {
          return false
        }
      })
    }
  },

  beforeRouteEnter (to, from, next) {
    if (from.name) {
      next()
    } else {
      let account = localStorage.getItem('account-key') ? JSON.parse(localStorage.getItem('account-key')) : {}
      if (account) {
        next(vm => {
          if (account.accountCode === 'Administrator') {
            vm.$router.push({name: 'Admin'})
          } else {
            vm.$router.push({name: 'Chat'})
          }
        })
      } else {
        next()
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .login-container {
    /*box-shadow: 0 0px 8px 0 rgba(0, 0, 0, 0.06), 0 1px 0px 0 rgba(0, 0, 0, 0.02);*/
    -webkit-border-radius: 5px;
    border-radius: 5px;
    -moz-border-radius: 5px;
    background-clip: padding-box;
    margin: 160px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
    background: -ms-linear-gradient(top, #ace, #00C1DE); /* IE 10 */
    background: -moz-linear-gradient(top, #ace, #00C1DE); /*火狐*/
    background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#ace), to(#00C1DE)); /*谷歌*/
    background: -webkit-linear-gradient(top, #ace, #00C1DE); /*Safari5.1 Chrome 10+*/
    background: -o-linear-gradient(top,#ace, #00C1DE); /*Opera 11.10+*/
  }

  .login-container .title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }

  .login-container .remember {
    margin: 0px 0px 35px 0px;
  }

</style>
