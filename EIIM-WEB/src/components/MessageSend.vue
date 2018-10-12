<template>
  <div id="messageSender">
    <div class="toolbar">
      <i class="iconfont icon-biaoqing"></i>
      <el-upload
        class="avatar-uploader"
        action="upload"
        :http-request="sendFileMessage"
        :show-file-list="false">
        <i class="iconfont icon-iconset0196"></i>
      </el-upload>
    </div>
    <textarea ref="select_frame" placeholder="按 Ctrl + Enter 发送" v-model="message" @keyup="sendTextMessage"
              rows="5"></textarea>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'MessageSend',

  data () {
    return {
      sessionId: null,
      message: null,
      fileList: []
    }
  },

  computed: {
    ...mapGetters({session: 'chatroom/api_get_chatroom', user: 'account/api_current_account'})
  },

  methods: {
    /**
     * 文件上传
     *
     * */
    sendFileMessage (params) {
      this.$store.dispatch('chatroom/api_upload_file', params).then((data) => {
        this.$store.dispatch('chatroom/api_send_file_message', {
          sessionId: this.session.id,
          message: data,
          user: this.user
        }).then(res => {
          if (res) {
            this.message = ''
          }
        })
      })
    },

    sendTextMessage (e) {
      if (e.ctrlKey && (e.keyCode === 13 || e.keyCode === 91) && this.message.length) {
        this.$store.dispatch('chatroom/api_send_text_message', {
          sessionId: this.session.id,
          message: this.message,
          user: this.user
        }).then(res => {
          if (res) {
            this.message = ''
          }
        })
      }
    }
  },

  mounted () {
    // let sessionId = this.session.id
    // this.$refs.select_frame.onpaste = (e) => {
    //   console.log(e.dataTransfer)
    //   let clipboardData = e.clipboardData
    //   if (clipboardData) {
    //     let items = clipboardData.items
    //     if (items && items.length) {
    //       let item = null
    //       for (var i = 0; i < clipboardData.types.length; i++) {
    //         if (clipboardData.types[i] === 'Files') {
    //           item = items[i]
    //           if (item && item.kind === 'file') {
    //             console.log('sending file')
    //             this.$store.dispatch('chatroom/api_upload_file', {
    //               sessionId: sessionId,
    //               file: item.getAsFile()
    //             }).then(res => {
    //               if (res) {
    //                 // this.message = ''
    //               }
    //             })
    //           }
    //         }
    //       }
    //     }
    //   }
    // }
    //
    // this.$refs.select_frame.ondragleave = (e) => {
    //   // 阻止离开时的浏览器默认行为
    //   e.preventDefault()
    // }
    //
    // this.$refs.select_frame.ondrop = (e) => {
    //   // 阻止拖放后的浏览器默认行为
    //   e.preventDefault()
    //
    //   // 获取文件对象
    //   const data = e.dataTransfer.files
    //   if (data.length < 1) {
    //     // 检测是否有文件拖拽到页面
    //     return
    //   }
    //   console.log(e.dataTransfer.files)
    //   const formData = new FormData()
    //   for (let i = 0; i < e.dataTransfer.files.length; i++) {
    //     console.log(e.dataTransfer.files[i])
    //     if (e.dataTransfer.files[i].name.indexOf('map') === -1) {
    //       alert('只允许上传.map文件')
    //       return
    //     }
    //     formData.append('uploadfile', e.dataTransfer.files[i], e.dataTransfer.files[i].name)
    //   }
    //   this.fileList = this.fileList.concat(e.dataTransfer.files[0])
    //   console.log(formData, this.fileList, e.dataTransfer.files[0])
    // }
    //
    // this.$refs.select_frame.ondragenter = (e) => {
    //   //  阻止拖入时的浏览器默认行为
    //   e.preventDefault()
    //   // this.$refs.select_frame.border = '2px dashed red'
    // }
    //
    // this.$refs.select_frame.ondragover = (e) => {
    //   // 阻止拖来拖去的浏览器默认行为
    //   // e.preventDefault()
    // }
  }
}
</script>

<style lang="less" scoped>
  #messageSender {
    height: 100%;
  }

  #messageSender textarea {
    padding: 10px;
    height: 100px;
    width: 100%;
    border: none;
    outline: none;
    font-family: "Micrsofot Yahei";
    resize: none;
  }

  #messageSender .toolbar {
    padding: 10px;
    height: 50px;
    width: 100%;
    border: none;
    outline: none;
    font-family: "Micrsofot Yahei";
    resize: none;
    border-bottom: 1px solid silver;
    line-height:24px;
    display: flex;
    justify-content: flex-start;
    align-items: left;
  }

  #messageSender .toolbar .iconfont {
    font-size: 24px;
    padding-right: 20px
  }
</style>
