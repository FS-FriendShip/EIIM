<template>
  <div id="Login" class="login">
    <div class="banner">
      <img style='width:100%;height:100px;' src="../assets/rhosoon.png">
    </div>

    <div class="banner">
      <mt-field label="用户名" placeholder="用户名" v-model="accountCode"></mt-field>
      <mt-field label="密码" placeholder="密码" type="password" v-model="password"></mt-field>
    </div>

    <div v-show="error">
        <div>{{error}}</div>
    </div>

    <mt-button type="primary" size="large" @click.native="login">登录</mt-button>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data () {
    return {
      accountCode: 'zhanglp',
      password: '12345678',
      error: ''
    }
  },

  methods: {
    login: function () {
      this.$store.dispatch('account/api_account_login', {accountCode: this.accountCode, password: this.password}).then((data) => {
        this.GLOBAL.account = data
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
</style>
