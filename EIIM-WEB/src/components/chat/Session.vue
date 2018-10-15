<template>
  <div class="session">
    <el-container>
      <el-header style="padding:10px">
        <el-row>
          <el-input
            placeholder="检索"
            class="search"
            @keyup.native="searchSession"
            prefix-icon="el-icon-search"
            v-model="key">
          </el-input>

          <el-button icon="iconfont icon-add" @click="showSessionDialog" size="mini"></el-button>
        </el-row>
      </el-header>

      <el-main class="session-list">
        <vue-context-menu :contextMenuData="contextMenuData"
                          @topSession="topSession"
                          @deleteSession="deleteSession">
        </vue-context-menu>

        <el-row align="middle" class="session-item" v-bind:class="item.active ? 'active':''" :gutter="50" v-for="item in sessions"  @click.native="selectSession(item)" :key="item.id" @contextmenu.native="showMenu(item.id)">
          <el-col :span="4"><el-badge :value="item.unread" class="item"><img class="avatar-large" :src="'/rest/v1/download/' + item.creator.avatar"></el-badge></el-col>
          <el-col :span="20">
            <div>
              <span class="title">{{item.name}}</span><span class="sentTime">{{(item.latestMessage?item.latestMessage.sentTime:null) | formatDate('session')}}</span>
            </div>
            <div class="subtitle">
              <span v-html="item.subtitle"></span>
            </div>
          </el-col>
        </el-row>
      </el-main>
    </el-container>

    <SessionDialog v-on:resetDialogVisible="resetDialogVisible" :show.sync="sessionDialogVisible"></SessionDialog>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import SessionDialog from './SessionDialog'
export default {
  name: 'Profile',
  components: {SessionDialog},
  data () {
    return {
      key: null,
      sessionDialogVisible: false,
      contextMenuItemId: null,
      contextMenuData: {
        menuName: 'session-menu',
        // The coordinates of the display(菜单显示的位置)
        axis: {
          x: null,
          y: null
        },
        // Menu options (菜单选项)
        menulists: [
          {
            fnHandler: 'topSession', // Binding events(绑定事件)
            icoName: 'iconfont icon-zhiding', // icon (icon图标 )
            btnName: '置顶' // The name of the menu option (菜单名称)
          },
          {
            fnHandler: 'deleteSession',
            icoName: 'iconfont icon-shangchu',
            btnName: '删除'
          }
        ]
      }
    }
  },

  computed: {
    ...mapGetters({sessions: 'chatroom/api_get_chatrooms', search: 'chatroom/api_search_chatroom', userList: 'contact/api_contact_List'})
  },

  methods: {
    showMenu (itemId) {
      this.contextMenuItemId = itemId
      event.preventDefault()
      var x = event.clientX
      var y = event.clientY
      // Get the current location
      this.contextMenuData.axis = {
        x, y
      }
    },

    /**
     *
     * */
    topSession () {
      this.$store.dispatch('chatroom/api_top_session', this.contextMenuItemId)
    },

    /**
     *
     */
    deleteSession () {
      this.$store.dispatch('chatroom/api_delete_chatroom', this.contextMenuItemId)
    },

    /**
     *
     * @param sessionId
     */
    selectSession (session) {
      let params = {session: session, account: this.GLOBAL.account.account}
      this.$store.dispatch('chatroom/api_select_chatroom', params)
    },

    showSessionDialog () {
      this.$store.dispatch('contact/api_get_contacts').then((data) => {
        this.sessionDialogVisible = true
      })
    },

    resetDialogVisible (visible) {
      this.sessionDialogVisible = visible
    },

    searchSession (e) {
      let key = this.key
      this.search(key)
    }
  }
}
</script>

<style scoped lang="less">
  .session {
    .session-item {
      margin-top:10px;
      padding:5px 0px;
      border-bottom: 1px solid #EAEAEA;
      cursor: pointer;
      transition: background-color .1s;

      &.active {
        background-color: #EAEAEA;
      }
    }
    .avatar, .name {
      vertical-align: middle;
    }
    .avatar {
      border-radius: 2px;
    }
    .name {
      display: inline-block;
      margin: 0 0 0 15px;
    }
  }

  .session .session-list {
    overflow-x: hidden;
    overflow-y: scroll;
    height: 800px;
    padding-top: 0px;
  }

  .search {
    padding: 0 10px;
    width: 75%;
    font-size: 12px;
    height: 20px;
    line-height: 20px;
    border-radius: 4px;
    outline: none;
  }

  .session-item .title {
    color: #000;
    font-size: 14px;
    padding-left:10px;
    overflow: hidden;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
    white-space:nowrap;
    width:150px;
    height:24px;
    display:block;
    float:left;
  }

  .session-item .sentTime {
    color: #A1A1A1;
    font-size:12px;
    padding-left:10px;
    float:right;
  }

  .session-item .subtitle {
    color: #A1A1A1;
    font-size:12px;
    width: 200px;
    height: 28px;
    padding-left:10px;
    margin-top:10px;
    line-height: 28px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }
</style>
