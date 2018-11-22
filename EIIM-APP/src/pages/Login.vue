<template>
  <div id="Login" class="login">
    <div class="banner">
      <img style='width:100%;height:100px;' src="../assets/rhosoon.png">
    </div>

    <div class="banner">
      <mt-field label="用户名" placeholder="用户名" v-model="login.accountCode"></mt-field>
      <mt-field label="密码" placeholder="密码" type="password" v-model="login.password"></mt-field>
    </div>

    <div class="login-button">
      <mt-button type="primary" size="normal" @click.native="doLogin">登录</mt-button>
    </div>
  </div>
</template>

<script>
import websocket from '../api/websocket'
import {Toast} from 'mint-ui'
export default {
  name: 'Login',
  data () {
    return {
      login: {
        accountCode: '13601951704',
        password: 'ftp123456'
      }
    }
  },

  methods: {
    doLogin: function () {
      if (this.accountCode === '' || this.password === '') {
        Toast({message: '用户名或密码不能为空', duration: this.GLOBAL.toast.duration})
        return
      }

      this.$store.dispatch('account/api_clear_cache')
      this.$store.dispatch('chatroom/api_clear_cache')

      this.$store.dispatch('account/api_account_login', this.login).then((data) => {
        websocket.initWebSocket()
        this.$router.push({name: 'Main'})
      })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .banner{
    height: 100px;
    margin-top: 100px;
    margin-bottom: 50px;
  }

  .login-button {
    text-align: center;
    display: block;
  }

  .login-button button {
    width: 24rem;
  }
</style>
