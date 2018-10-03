<template>
  <li >
    <!--自定义指令 v-swiper 用于对每个消息进行滑动处理-->
    <router-link :to="{name: 'chatroom', params: {roomId: item.id}}" tag="div" class="list-info" v-swiper v-on:click.native="toggleMsgRead($event,'enter')">
      <div class="header-box">
        <!--未读并且未屏蔽 才显示新信息数量-->
        <i class="new-msg-count" v-show="!read&&!item.quiet">{{item.messages.length}}</i>
        <!--未读并且屏蔽 只显示小红点-->
        <i class="new-msg-dot" v-show="!read&&item.quiet"></i>
        <!--如果是私聊，只显示一个头像； 如果是群聊，则显示多个头像，flex 控制样式-->
        <div class="header" :class="[item.type=='group'?'multi-header':'']">
          <img v-for="memberInfo in item.members" :src="memberInfo.account.avatar" :key="memberInfo.account.id">
        </div>
      </div>
      <div class="desc-box">
        <!--使用过滤器 fmtDate 格式化时间-->
        <div class="desc-time">{{item.messages[item.messages.length-1].sentTime | fmtDate('hh:ss')}}</div>
        <div class="desc-author" v-if="item.type=='group'">{{item.group_name}}</div>
        <!--如果没有备注好友，则显示微信昵称-->
        <div class="desc-author" v-else>{{item.name}}</div>
        <div class="desc-msg">
          <div class="desc-mute iconfont icon-mute" v-show="item.quiet">
          </div>
          <span v-show="item.type=='group'">{{item.messages[item.messages.length-1].name}}:</span>
          <span>{{item.messages[item.messages.length-1].text}}</span>
        </div>
      </div>
    </router-link>
    <div class="operate-box">
      <div class="operate-unread" v-if="read" v-on:click="toggleMsgRead">标为未读</div>
      <div class="operate-read" v-else v-on:click="toggleMsgRead">标为已读</div>
      <div class="operate-del" v-on:click="deleteMsgEvent">删除</div>
    </div>
  </li>
</template>

<script>
export default {
  name: 'SessionItem',

  props: ['item'],
  data () {
    return {
      read: this.item.read,
      deleteMsg: false
    }
  },

  created  () {
  },

  methods: {
    // 切换消息未读/已读状态
    toggleMsgRead (event, status) {
      if (status === 'enter') {
        if (this.read) {
          return ''
        }
        this.read = true
      } else {
        this.read = !this.read
      }
      event.target.parentNode.parentNode.firstChild.style.marginLeft = 0 + 'px'
    },
    deleteMsgEvent () {
      this.deleteMsg = true
      if (!this.item.quiet) {
        if (!this.read) {
          this.$store.commit('minusNewMsg')
        }
      }
    }
  },
  directives: {
    swiper: {
      bind: function (element, binding) {
        var isTouchMove, startTx, startTy
        element.addEventListener('touchstart', function (e) {
          // let touches = e.touches[0]
          // let startTx = touches.clientX
          // let startTy = touches.clientY
          // let isTouchMove = false
        }, false)
        element.addEventListener('touchmove', function (e) {
          let touches = e.changedTouches[0]
          let endTx = touches.clientX
          let endTy = touches.clientY
          let distanceX = startTx - endTx
          let distanceY = startTy - endTy
          if (distanceX < 0) { // 右滑
            if (Math.abs(distanceX) >= Math.abs(distanceY)) {
              if (Math.abs(distanceX) > 20) {
                element.style.transition = '0.3s'
                element.style.marginLeft = 0 + 'px'
              }
            }
          } else { // 左滑
            if (Math.abs(distanceX) >= Math.abs(distanceY)) {
              if (distanceX < 156 && distanceX > 20) {
                e.preventDefault()
                element.style.transition = '0s'
                element.style.marginLeft = -distanceX + 'px'
                isTouchMove = true
              }
            }
          }
          // e.preventDefault()
        }, false)
        element.addEventListener('touchend', function (e) {
          if (!isTouchMove) {
            return
          }
          let touches = e.changedTouches[0]
          let endTx = touches.clientX
          let endTy = touches.clientY
          let distanceX = startTx - endTx
          let distanceY = startTy - endTy
          if (Math.abs(distanceX) >= Math.abs(distanceY)) {
            if (distanceX < 0) {
              return
            }
            if (Math.abs(distanceX) < 60) {
              element.style.transition = '0.3s'
              element.style.marginLeft = 0 + 'px'
            } else {
              element.style.transition = '0.3s'
              element.style.marginLeft = '-156px'
            }
          }
        }, false)
      }
    }
  }
}
</script>
