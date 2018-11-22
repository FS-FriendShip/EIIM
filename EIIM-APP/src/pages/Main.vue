<template>
  <div id="wechat">
    <mt-header  class="app-header" fixed title="若信"></mt-header>

    <ul class="wechat-list">
      <mt-cell-swipe v-for="(chatroom, index) in sessions" :key="index" class="list-row line-bottom"
                     :right="[
              {
                  content: '删除',
                  style: { background: '#ff7900', color: '#fff'},
                  handler: () => deleteSection(chatroom.id)
              }
          ]" @click.native="selectSession(chatroom)">
        <div slot="title" class="slot_box">
          <div class="session-avatar">
            <mt-badge type="error" v-show="chatroom.unread > 0" class="session-badge">{{chatroom.unread}}</mt-badge><img class="avatar-large" :src="chatroom.creator.avatar">
          </div>
          <div class="session-content">
            <div>
              <span class="title">{{chatroom.name}}</span><span class="sentTime">{{(chatroom.latestMessage?item.latestMessage.sentTime:null) | formatDate('session')}}</span>
            </div>
            <div class="subtitle">
              <span v-html="chatroom.subtitle"></span>
            </div>
          </div>
        </div>
      </mt-cell-swipe>
    </ul>
  </div>
</template>

<script>
import constant from '../common/global'
import {mapGetters} from 'vuex'

export default {
  name: 'Main',
  data () {
    return {
      pageName: constant.appName
    }
  },

  computed: {
    ...mapGetters({sessions: 'chatroom/api_get_chatrooms', search: 'chatroom/api_search_chatroom', userList: 'contact/api_contact_List', findSession: 'chatroom/api_find_chatroom'})
  },

  created  () {
    let account = this.GLOBAL.getAccount()
    // 获取聊天室信息
    this.$store.dispatch('chatroom/api_get_chatrooms', account).then(() => {
      this.sessionDialogVisible = true

      let params = {session: this.session, account: account}
      if (this.session) {
        this.$store.dispatch('chatroom/api_select_chatroom', params)
      }
    })
  },

  methods: {
    selectSession (session) {
      console.log(session)
    },

    showPersonal: function () {
      this.$router.push({path: 'showPersonal'})
    },

    showContact: function () {
      this.$router.push({path: '/contacts'})
    }
  },

  beforeRouteEnter (to, from, next) {
    next()
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
  /*@import '../assets/css/wechat.css';*/
  #wechat .wechat-list {
    overflow-x: hidden;
  }

  #wechat .wechat-list .list-row {
    height: 4rem;
    display: flex;
    display: -webkit-flex;
    align-items:center;
  }

  #wechat .wechat-list .list-row .slot_box {
    display: flex;
    display: -webkit-flex;
    justify-content: flex-start;
  }

  #wechat .wechat-list .list-row .slot_box .session-badge{
    position: absolute;
    top: 0rem;
    left: 0rem;
    z-index: 100;
  }

  #wechat .wechat-list .list-row .slot_box .session-content{
    padding-left: 1rem;
  }
</style>
