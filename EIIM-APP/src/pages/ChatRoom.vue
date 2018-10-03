<template>
  <div class="dialogue">
    <header>
      <wx-header :pageName="this.GLOBAL.appName"></wx-header>
    </header>

    <!--<header id="wx-header">-->
      <!--<div class="right">-->
        <!--<router-link :to="{name:'groupChatRoomInfo', params: {roomId: chatroom.id}}" tag="span" class="iconfont icon-chat-group" v-show="chatroom.members.length > 2"></router-link>-->
        <!--<router-link :to="{name:'chatRoomInfo', params: {roomId: chatroom.id}}" tag="span" class="iconfont icon-chat-friends" v-show="chatroom.members.length <= 2"></router-link>-->
      <!--</div>-->
      <!--<div class="center">-->
        <!--<router-link :to="prepage" tag="div" class="iconfont icon-return-arrow">-->
          <!--<span>{{this.GLOBAL.appName}}</span>-->
        <!--</router-link>-->
        <!--<span>{{chatroom.name}}</span>-->
        <!--<span class="parentheses" v-show='$route.query.group_num&&$route.query.group_num!=1'>{{$route.query.group_num}}</span>-->
      <!--</div>-->
    <!--</header>-->
    <section class="dialogue-section clearfix" v-on:click="MenuOutsideClick">
      <div class="dialogue-message clearfix" v-for="msg in chatroom.messages" :key="msg.id">
        <img class="message-portrait" :class="msg.owner == 'other'?'message-other':'message-self'" :src="msg.sender.avatar">
        <p class="message-text" :class="msg.owner == 'other'?'message-other':'message-self'" v-more>{{msg.message}}</p>
      </div>

      <span class="msg-more" id="msg-more">
        <ul>
            <li>复制</li>
            <li>转发</li>
            <li>收藏</li>
            <li>删除</li>
        </ul>
      </span>
    </section>

    <footer class="dialogue-footer">
      <div class="component-dialogue-bar-person" v-if = "this.GLOBAL.client == 'mobile'">
        <span class="iconfont icon-dialogue-jianpan" v-show="!currentChatWay" v-on:click="currentChatWay=true"></span>
        <span class="iconfont icon-dialogue-voice" v-show="currentChatWay" v-on:click="currentChatWay=false"></span>
        <div class="chat-way" v-show="!currentChatWay">
          <div class="chat-say" v-press>
            <span class="one">按住 说话</span>
            <span class="two">松开 结束</span>
          </div>
        </div>
        <div class="chat-way" v-show="currentChatWay">
          <textarea rows="1" v-model="message" class="chat-txt" type="text" v-on:focus="focusIpt" v-on:blur="blurIpt"/>
        </div>
        <span class="expression iconfont icon-dialogue-smile"></span>
        <span class="more iconfont icon-dialogue-jia" v-if="message.length == 0"></span>
        <span class="message-send" v-if="message.length > 0"  @click="send()">发送</span>
        <div class="recording" style="display: none;" id="recording">
          <div class="recording-voice" style="display: none;" id="recording-voice">
            <div class="voice-inner">
              <div class="voice-icon"></div>
              <div class="voice-volume">
                <span></span>
                <span></span>
                <span></span>
                <span></span>
                <span></span>
                <span></span>
                <span></span>
                <span></span>
                <span></span>
              </div>
            </div>
            <p>手指上划,取消发送</p>
          </div>
          <div class="recording-cancel" style="display: none;">
            <div class="cancel-inner"></div>
            <p>松开手指,取消发送</p>
          </div>
        </div>
      </div>

      <div class="component-dialogue-bar-person" v-if = "this.GLOBAL.client == 'pc'">

      </div>
    </footer>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import websocket from '../api/websocket'
import WxHeader from '../components/Header'

export default {
  name: 'ChatRoom',
  data () {
    return {
      currentChatWay: true, // ture为键盘打字 false为语音输入
      timer: null,
      roomId: null,
      message: '',
      prepage: '/Main'
    }
  },
  components: {
    WxHeader
  },

  created  () {
    this.roomId = this.$route.params.roomId
  },

  computed: {
    ...mapGetters({getChatroom: 'chatroom/getChatroom'}),

    chatroom () {
      return this.getChatroom(this.roomId)[0]
    }
  },

  beforeRouteEnter (to, from, next) {
    next(vm => {
    })
  },

  directives: {
    press: {
      inserted (element, binding) {
        let recording = document.querySelector('.recording')
        let recordingVoice = document.querySelector('.recording-voice')
        let recordingCancel = document.querySelector('.recording-cancel')
        // var startTx
        let startTy

        element.addEventListener('touchstart', function (e) {
          // 用bind时，vue还没插入到dom,故dom获取为 undefine，用 inserted 代替 bind,也可以开个0秒的定时器
          element.className = 'chat-say say-active'
          recording.style.display = recordingVoice.style.display = 'block'
          // console.log('start')
          var touches = e.touches[0]
          // startTx = touches.clientX
          startTy = touches.clientY
          e.preventDefault()
        }, false)
        element.addEventListener('touchend', function (e) {
          /* var touches = e.changedTouches[0];
          var distanceY = startTy - touches.clientY;
          if (distanceY > 50) {
              console.log("取消发送信息");
          }else{
              console.log("发送信息");
          } */

          element.className = 'chat-say'
          recordingCancel.style.display = recording.style.display = recordingVoice.style.display = 'none'
          // console.log('end')
          e.preventDefault()
        }, false)
        element.addEventListener('touchmove', function (e) {
          let touches = e.changedTouches[0]
          // let endTx = touches.clientX
          let endTy = touches.clientY
          // let distanceX = startTx - endTx
          let distanceY = startTy - endTy

          if (distanceY > 50) {
            element.className = 'chat-say'
            recordingVoice.style.display = 'none'
            recordingCancel.style.display = 'block'
          } else {
            element.className = 'chat-say say-active'
            recordingVoice.style.display = 'block'
            recordingCancel.style.display = 'none'
          }
          // 阻断事件冒泡 防止页面被一同向上滑动
          e.preventDefault()
        }, false)
      }
    },
    more: {
      bind (element, binding) {
        var startTx, startTy
        element.addEventListener('touchstart', function (e) {
          let msgMore = document.getElementById('msg-more')
          let touches = e.touches[0]
          startTx = touches.clientX
          startTy = touches.clientY

          clearTimeout(this.timer)
          this.timer = setTimeout(() => {
            // 控制菜单的位置
            msgMore.style.left = ((startTx - 18) > 180 ? 180 : (startTx - 18)) + 'px'
            msgMore.style.top = (element.offsetTop - 33) + 'px'
            msgMore.style.display = 'block'
            element.style.backgroundColor = '#e5e5e5'
          }, 500)
        }, false)

        element.addEventListener('touchmove', function (e) {
          let touches = e.changedTouches[0]
          let disY = touches.clientY
          if (Math.abs(disY - startTy) > 10) {
            clearTimeout(this.timer)
          }
        }, false)
        element.addEventListener('touchend', function (e) {
          clearTimeout(this.timer)
        }, false)
      }
    }
  },
  methods: {
    send () {
      let msg = this.message
      websocket.send({
        deviceId: null,
        eiimCode: null,
        accountCode: null,
        chatRoomId: this.chatroom.id,
        messageType: 'text',
        message: msg
      })
    },

    // 解决输入法被激活时 底部输入框被遮住问题
    focusIpt () {
      this.timer = setInterval(function () {
        document.body.scrollTop = document.body.scrollHeight
      }, 100)
    },
    blurIpt () {
      clearInterval(this.timer)
    },
    // 点击空白区域，菜单被隐藏
    MenuOutsideClick (e) {
      let container = document.querySelectorAll('.text')
      let msgMore = document.getElementById('msg-more')
      if (e.target.className === 'text') {

      } else {
        msgMore.style.display = 'none'
        container.forEach(item => { item.style.backgroundColor = '#fff' })
      }
    }
  }
}
</script>

<style>
  @import "../assets/css/dialogue.css";
  .say-active {
    background: #c6c7ca;
  }

</style>
