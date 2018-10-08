<template>
  <div v-if="admin" id="admin">
    <router-view/>
  </div>

  <div v-else id="app">
    <el-row align="middle">
    <router-view/>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'App',
  data () {
    return {
      admin: false // 管理页
    }
  },

  /**
   *
   */
  created () {
    let path = this.$route.path
    let admin = '/admin'
    if (path.substr(0, admin.length) === admin) {
      this.admin = true
    } else {
      this.admin = false
    }
  }
}
</script>

<style>
  * {
    box-sizing: border-box;
  }
  *:before, *:after {
    box-sizing: inherit;
  }
  body, html {
    height: 100%;
    overflow: hidden;
  }
  body, ul {
    margin: 0;
    padding: 0;
  }
  body {
    color: #4d4d4d;
    font: 14px/1.4em 'Helvetica Neue', Helvetica, 'Microsoft Yahei', Arial, sans-serif;
    background: #f5f5f5 url('./assets/bg.jpg') no-repeat center;
    background-size: cover;
    font-smoothing: antialiased;
  }
  ul {
    list-style: none;
  }

  #app {
     margin: 5% 10%;

     border-color: #fff;
     overflow: hidden;
     border-radius: 3px;

     font-family: 'Avenir', Helvetica, Arial, sans-serif;
     -webkit-font-smoothing: antialiased;
     -moz-osx-font-smoothing: grayscale;
     text-align: center;
     color: #2c3e50;
   }

  #admin {
    height:100%;
    border-color: #fff;
    overflow: hidden;
    border-radius: 3px;

    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }

  .avatar-small {
    width: 30px;
    height: 30px;
  }

  .avatar-medium {
    width: 40px;
    height: 40px;
  }

  .avatar-large {
    width: 50px;
    height: 50px;
  }
</style>
