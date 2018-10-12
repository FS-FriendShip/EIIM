<template>
  <div class="message">
    <ul v-if="session">
      <li v-for="item in session.messages" :key="item.id">
        <p v-if="item.showTime" class="time">
          <span>{{ item.sentTime | formatDate}}</span>
        </p>
        <div class="main" v-if = "item.sender" :class="item.owner==='self'?'self':'other'">
          <img class="avatar avatar-medium" :src="item.sender.avatar"/>
          <div v-if="item.messageType=='TEXT'" class="text">{{ item.message.text }}</div>
          <div v-else-if="item.messageType=='FILE' && ['png','jpg'].includes(item.message.fileType)" class="file-img">
            <img :src="'/rest/v1/download/'+item.message.id"/>
          </div>
          <div v-else-if="item.messageType=='FILE'" class="file">
            <a style=" text-decoration: none;"  :href="'/rest/v1/download/' + item.message.id">
              <div style="float:left;">
                <img v-if="['xls','xlsx'].includes(item.message.fileType)" src="../assets/file_excel.png" height="60px" width="60px">
                <img v-else-if="['doc','docx'].includes(item.message.fileType)" src="../assets/file_word.png" height="60px" width="60px">
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
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import {dateFormat} from '../common/utils'

export default {
  name: 'Message',

  computed: {
    ...mapGetters({session: 'chatroom/api_get_chatroom', user: 'account/api_current_account'})
  },

  created () {
  },

  filters: {
    // 将日期过滤为 hour:minutes
    formatDate (time) {
      var date = new Date(time)
      return dateFormat(date, 'yyyy-MM-dd hh:mm')
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
  a:link {
    font-size: 12px;
    color: #000000;
    text-decoration: none;
  }
  a:visited {
    font-size: 12px;
    color: #000000;
    text-decoration: none;
  }
  a:hover {
    font-size: 12px;
    color: #000000;
    text-decoration: none;
  }
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

    .file {
      display: inline-block;
      position: relative;
      padding: 10px;
      width: 300px;
      height: 80px;
      line-height: 2.5;
      font-size: 12px;
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
