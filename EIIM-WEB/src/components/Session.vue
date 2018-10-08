<template>
  <div class="list">
    <el-container>
      <el-header style="padding:10px">
        <el-row>
          <input class="search" type="text" v-model="key" placeholder="search user..." @keyup="searchSession">
          <el-button icon="iconfont icon-add" circle="true" @click="showSessionDialog"></el-button>
        </el-row>
      </el-header>

      <el-main style="padding:10px">
        <vue-context-menu :contextMenuData="contextMenuData"
                          @topSession="topSession"
                          @deleteSession="deleteSession">
        </vue-context-menu>

        <el-row class="session-item" :gutter="50" v-for="item in sessions"  @click.native="selectSession(item.id)" :key="item.id" @contextmenu.native="showMenu(item.id)">
          <el-col :span="4"><img class="avatar-large" :src="'/rest/v1/download/' + item.id"></el-col>
          <el-col :span="20">
            <span>{{item.name}}</span>
            <p>{{item.latestMessage}}</p>
          </el-col>
        </el-row>
      </el-main>
    </el-container>

    <SessionDialog v-on:resetDialogVisible="resetDialogVisible" :show.sync="sessionDialogVisible"></SessionDialog>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import SessionDialog from '../components/SessionDialog'

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
    ...mapGetters({sessions: 'chatroom/api_get_chatrooms', userList: 'contact/api_contact_List'})
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

    selectSession (sessionId) {
      console.log(sessionId)
      this.$store.dispatch('chatroom/api_select_chatroom', sessionId)
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
      if (key) {
        this.sessions = this.sessions.filter(session => {
          return session.name.includes(key)
        })
      }
    }
  }
}
</script>

<style scoped lang="less">
  .list {
    .session-item {
      padding:5px 0px;
      border-bottom: 1px solid #292C33;
      cursor: pointer;
      transition: background-color .1s;

      &:hover {
        background-color: rgba(255, 255, 255, 0.03);
      }
      &.active {
        background-color: rgba(255, 255, 255, 0.1);
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

  .search {
    padding: 0 10px;
    width: 75%;
    font-size: 12px;
    color: #fff;
    height: 50px;
    line-height: 30px;
    border: solid 1px #3a3a3a;
    border-radius: 4px;
    outline: none;
    background-color: #26292E;
  }

  .session-item .title {
    /*font-weight: bolder;*/
  }

  .session-item .subtitle {
    /*font-weight: lighter;*/
  }
</style>
