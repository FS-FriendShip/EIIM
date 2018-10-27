<template>
  <el-container>
    <el-aside v-if="showNav" class="nav">
      <NavBar/>
    </el-aside>
    <el-main style="padding:0px;"><router-view /></el-main>
  </el-container>
</template>

<script>
import NavBar from './components/NavBar'
export default {
  name: 'App',
  components: {NavBar},

  data () {
    return {
      showNav: true
    }
  },

  watch: {
    // 监听 $route 为店内页设置不同的过渡效果
    '$route' (to, from) {
      let path = to.path
      this.showNav = (path !== '/')
    }
  },

  mounted () {
    let href = window.location.href
    this.showNav = !href.endsWith('/')

    if (window.require) {
      let ipc = window.require('electron').ipcRenderer
      ipc.send('checkForUpdate')

      ipc.on('message', (event, text) => {
        console.log('message', text)
      })

      ipc.on('downloadProgress', (event, progressObj) => {
        this.downloadPercent = progressObj.percent || 0
        console.log('message', this.downloadPercent)
      })

      ipc.on('isUpdateNow', () => {
        ipc.send('isUpdateNow')
      })
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
    width: 100%;
    margin: 0;
    padding: 0;
    overflow: hidden;
  }

  body {
    color: #4d4d4d;
    font: 14px/1.4em 'Helvetica Neue', Helvetica, 'Microsoft Yahei', Arial, sans-serif;
    background: #f5f5f5 url('./assets/bg.jpg') no-repeat center;
    background-size: cover;
    font-smoothing: antialiased;
  }

  li {
    list-style:none;
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

  ::-webkit-scrollbar {
    width: 0px;  /* remove scrollbar space */
    background: transparent;  /* optional: just make scrollbar invisible */
  }

  .nav {
    width:75px!important;
    background: #4F4F4F;
  }
</style>
