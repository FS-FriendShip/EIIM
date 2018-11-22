<template>
  <div id="messageSender">
    <div class="toolbar">
      <el-popover
        placement="top-start"
        width="560"
        trigger="click">
        <emotion @emotion="handleEmotion" :height="200" ></emotion>
        <i class="iconfont icon-biaoqing" slot="reference"></i>
      </el-popover>

      <el-upload
        class="avatar-uploader"
        action="upload"
        :beforeUpload = "sendFileMessage"
        :show-file-list="false">
        <i class="iconfont icon-iconset0196"></i>
      </el-upload>

      <el-popover
        placement="top-start"
        trigger="click">
        <FileList></FileList>
        <i class="iconfont icon-fujian" slot="reference"></i>
      </el-popover>
  </div>
    <MessageInput class="message-input" ref="select_frame" :content="message" @send="sendTextMessage" placeholder='按Enter发送'></MessageInput>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import Emotion from './Emotion/index'
import MessageInput from './MessageInput'
import FileList from './FileList'

export default {
  name: 'MessageSend',

  data () {
    return {
      sessionId: null,
      message: {txt: ''},
      fileList: []
    }
  },

  components: {
    Emotion,
    MessageInput,
    FileList
  },

  computed: {
    ...mapGetters({session: 'chatroom/api_get_chatroom', user: 'account/api_current_account'})
  },

  height: {
    type: Number,
    default: 200
  },

  methods: {
    handleEmotion (i) {
      this.message.txt += this.emotion(i)
    },

    /**
     * 将匹配结果替换表情图片
     *
     *
     * */
    emotion (res) {
      let word = res.replace(/#|;/gi, '')
      const list = ['微笑', '撇嘴', '色', '发呆', '得意', '流泪', '害羞', '闭嘴', '睡', '大哭', '尴尬', '发怒', '调皮', '呲牙', '惊讶', '难过', '酷', '冷汗', '抓狂', '吐', '偷笑', '可爱', '白眼', '傲慢', '饥饿', '困', '惊恐', '流汗', '憨笑', '大兵', '奋斗', '咒骂', '疑问', '嘘', '晕', '折磨', '衰', '骷髅', '敲打', '再见', '擦汗', '抠鼻', '鼓掌', '糗大了', '坏笑', '左哼哼', '右哼哼', '哈欠', '鄙视', '委屈', '快哭了', '阴险', '亲亲', '吓', '可怜', '菜刀', '西瓜', '啤酒', '篮球', '乒乓', '咖啡', '饭', '猪头', '玫瑰', '凋谢', '示爱', '爱心', '心碎', '蛋糕', '闪电', '炸弹', '刀', '足球', '瓢虫', '便便', '月亮', '太阳', '礼物', '拥抱', '强', '弱', '握手', '胜利', '抱拳', '勾引', '拳头', '差劲', '爱你', 'NO', 'OK', '爱情', '飞吻', '跳跳', '发抖', '怄火', '转圈', '磕头', '回头', '跳绳', '挥手', '激动', '街舞', '献吻', '左太极', '右太极']
      let index = list.indexOf(word)
      return `<img src="https://res.wx.qq.com/mpres/htmledition/images/icon/emotion/${index}.gif" align="middle">`
    },

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
            this.message.txt = ''
          }
        })
      })

      return false
    },

    sendTextMessage () {
      this.$store.dispatch('chatroom/api_send_text_message', {
        sessionId: this.session.id,
        message: this.message.txt,
        user: this.user
      }).then(res => {
        if (res) {
          this.message.txt = ''
        }
      })
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
    border-top: 1px solid silver;
  }

  #messageSender .message-input {
    padding: 10px;
    height: 100px;
    max-height:100px;
    width: 100%;
    border: none;
    outline: none;
    font-family: "Micrsofot Yahei";
    resize: none;
    overflow-y:auto;
    overflow-x:hidden;
  }

  #messageSender .message-input:before {
    content: attr(placeholder);
    display: block;
    color: #adadad;
  }

  #messageSender .toolbar {
    padding: 10px;
    height: 50px;
    width: 100%;
    border: none;
    outline: none;
    font-family: "Micrsofot Yahei";
    resize: none;
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
