<template>
  <el-container v-show="shown">
    <el-progress type="circle" :percentage="downloadPercent"></el-progress>
  </el-container>
</template>

<script>
export default {
  name: 'Updater',

  data () {
    return {
      shown: true,
      downloadPercent: 0
    }
  },

  created () {
    if (window.require) {
      let ipc = window.require('electron').ipcRenderer
      ipc.send('checkForUpdate')

      ipc.on('update-available', (event, text) => {
        console.log(text)
      })

      ipc.on('update-not-available', (event, text) => {
        console.log(text)
        // 无须更新， 进入
        this.$router.push({name: 'Login'})
      })

      ipc.on('downloadProgress', (event, progressObj) => {
        this.downloadPercent = progressObj.percent || 0
        console.log(this.downloadPercent)
      })

      ipc.on('isUpdateNow', () => {
        ipc.send('isUpdateNow')
      })
    }
  }
}
</script>

<style scoped>

</style>
