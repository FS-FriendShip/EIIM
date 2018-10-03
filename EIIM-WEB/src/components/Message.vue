<template>
  <div class="message">
    <ul v-if="session">
      <li v-for="item in session.messages" :key="item.id">
        <!--<p class="time">-->
          <!--<span>{{ item.sentTime | time }}</span>-->
        <!--</p>-->
        <div class="main" v-if = "item.sender" :class="{ self: item.owner == 'self' }">
          <img class="avatar" width="30" height="30" :src="item.owner == 'self' ? user.account.avatar : item.sender.avatar"/>
          <div v-if="item.messageType=='TEXT'" class="text">{{ item.message.text }}</div>
          <div v-else-if="item.messageType=='FILE' && ['png','jpg'].includes(item.message.fileType)" class="file-img">
            <img :src="'/rest/v1/download/'+item.message.id"/>
          </div>
          <div v-else-if="item.messageType=='FILE' && ['xls','xlsx'].includes(item.message.fileType)" class="file">
            <div class="file-excel">
              <div class="file-name">
                {{item.message.fileName}}
              </div>
              <div class="file-size">
                {{item.message.fileSize}}
              </div>
            </div>
          </div>
          <div v-else-if="item.messageType=='FILE' && ['doc','docx'].includes(item.message.fileType)" class="file">
            <div class="file-word">
              <div class="file-name">
                {{item.message.fileName}}
              </div>
              <div class="file-size">
                {{item.message.fileSize}}
              </div>
            </div>
          </div>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'Message',

  computed: {
    ...mapGetters({session: 'chatroom/api_get_chatroom', user: 'account/getCurrentUser'})
  },

  created () {
  },

  filters: {
    // 将日期过滤为 hour:minutes
    time (date) {
      if (typeof date === 'string') {
        date = new Date(date)
      }
      return date.getHours() + ':' + date.getMinutes()
    }
  },
  directives: {
    // 发送消息后滚动到底部
    'scroll-bottom' () {
      this.vm.$nextTick(() => {
        this.el.scrollTop = this.el.scrollHeight - this.el.clientHeight
      })
    }
  }
}
</script>

<style lang="less" scoped>
  .message {
    padding: 20px;
    overflow-y: scroll;

    li {
      margin-bottom: 15px;
    }
    .time {
      margin: 7px 0;
      text-align: center;

      > span {
        display: inline-block;
        padding: 0 18px;
        font-size: 12px;
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
      font-size: 12px;
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
        background-color: #b2e281;

        &:before {
          right: inherit;
          left: 100%;
          border-right-color: transparent;
          border-left-color: #b2e281;
        }

        .file-excel {
          background-image: url('../assets/file_excel.png');
          background-repeat: no-repeat;
          background-attachment: fixed;
          background-position: right;
          background-size: 80px 40px;
        }

        .file-word {
          background: #fff url(../assets/file_word.png) no-repeat fixed right;
        }

        .file-name {
          font-size: 12px;
        }

        .file-size {
          font-sie: 10px;
        }
      }
    }
  }
</style>
