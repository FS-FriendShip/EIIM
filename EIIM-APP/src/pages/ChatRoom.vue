<template>
  <div class="dialogue">
    <mt-header  class="app-header" fixed :title="chatroom.name">
      <router-link to="/Main" slot="left">
        <mt-button icon="back"></mt-button>
      </router-link>
    </mt-header>

    <section class="message dialogue-section clearfix">
      <ul id="message-list" v-if="chatroom">
        <li v-for="item in chatroom.messages" :key="item.id">
          <p v-if="item.showTime" class="time">
            <span>{{ item.sentTime | formatDate('message')}}</span>
          </p>
          <div class="main" v-if = "item.sender" :class="item.owner==='self'?'self':'other'">
            <img class="avatar avatar-medium" :src="item.sender.avatar"/>
            <div v-if="item.messageType=='TEXT'" class="text" v-html="item.message.text"></div>
            <div v-else-if="item.messageType=='FILE' && ['png','jpg'].includes(item.message.fileType)" class="file-img">
              <img :src="item.message.id"/>
            </div>

            <div v-else-if="item.messageType=='FILE'" class="file">
              <a style=" text-decoration: none;"  :href="item.message.download">
                <div style="float:left;">
                  <img v-if="['xls','xlsx'].includes(item.message.fileType)" src="../assets/file_excel.png" height="60px" width="60px">
                  <img v-else-if="['doc','docx'].includes(item.message.fileType)" src="../assets/file_word.png" height="60px" width="60px">
                  <img v-else src="../assets/file.png" height="60px" width="60px">
                </div>
                <div style="padding-left:20px">
                <span class="file-name">
                  {{item.message.fileName}}
                </span>
                  <br/>
                  <span class="file-size">
                  {{item.message.fileSize}}
                </span>
                </div>
              </a>
            </div>
          </div>
        </li>
      </ul>
    </section>

    <footer class="dialogue-footer">
      <div class="component-dialogue-bar-person">
        <span class="iconfont icon-dialogue-jianpan" v-show="!currentChatWay"></span>

        <div class="chat-way">
          <textarea rows="1" v-model="message" class="chat-txt" type="text"/>
        </div>
        <span class="expression iconfont icon-dialogue-smile"></span>
        <mt-button size="small"  type="primary" @click="send()">发送</mt-button>
      </div>
    </footer>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'ChatRoom',
  data () {
    return {
      currentChatWay: true, // ture为键盘打字 false为语音输入
      timer: null,
      roomId: null,
      message: ''
    }
  },

  created  () {
    this.roomId = this.$route.params.roomId
  },

  computed: {
    ...mapGetters({chatroom: 'chatroom/api_get_chatroom', user: 'account/api_current_account'})
  },

  methods: {
    send () {
      this.$store.dispatch('chatroom/api_send_text_message', {
        sessionId: this.roomId,
        message: this.message,
        user: this.user
      }).then(res => {
        if (res) {
          this.message = ''
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
  @import "../assets/css/dialogue.css";

  a:link {
    font-size: 14px;
    color: #000000;
    text-decoration: none;
  }
  a:visited {
    font-size: 14px;
    color: #000000;
    text-decoration: none;
  }
  a:hover {
    font-size: 14px;
    color: #000000;
    text-decoration: none;
  }

  .message {
    overflow-y: scroll;
    padding: 20px 0px;

    ul {
      padding-left:0px;
      padding: 0px 20px;
    }

    li {
      margin-bottom: 15px;
    }
    .time {
      margin: 7px 0;
      text-align: center;

      > span {
        display: inline-block;
        padding: 0 18px;
        font-size: 14px;
        color: #fff;
        border-radius: 2px;
        background-color: #dcdcdc;
      }
    }
    .avatar {
      float: left;
      margin: 0 10px 0 0;
      border-radius: 3px;
    }
    .text {
      display: inline-block;
      position: relative;
      padding: 0 10px;
      max-width: ~'calc(100% - 40px)';
      min-height: 30px;
      line-height: 2.5;
      font-size: 14px;
      text-align: left;
      word-break: break-all;
      background-color: #fafafa;
      border-radius: 4px;

      &:before {
        content: " ";
        position: absolute;
        top: 9px;
        right: 100%;
        border: 6px solid transparent;
        border-right-color: #fafafa;
      }
    }

    .file {
      display: inline-block;
      position: relative;
      padding: 10px;
      width: 200px;
      height: 80px;
      line-height: 2.5;
      font-size: 14px;
      text-align: left;
      word-break: break-all;
      background-color: #fff;
      border-radius: 4px;

      .file-excel {
        background-image: url('../assets/file_excel.png');
        background-repeat: no-repeat;
        background-attachment: fixed;
        background-position: right;
        background-size: 80px 40px;
      }

      &:before {
        content: " ";
        position: absolute;
        top: 9px;
        right: 100%;
        border: 6px solid transparent;
        border-right-color: #fff;
      }
    }

    .self {
      text-align: right;

      .avatar {
        float: right;
        margin: 0 0 0 10px;
      }

      .text {
        background-color: #b2e281;

        &:before {
          right: inherit;
          left: 100%;
          border-right-color: transparent;
          border-left-color: #b2e281;
        }
      }

      .file {
        background-color: #fff;

        &:before {
          right: inherit;
          left: 100%;
          border-right-color: transparent;
          border-left-color: #fff;
        }
      }
    }

    .other {
      text-align: left;
    }
  }
</style>
