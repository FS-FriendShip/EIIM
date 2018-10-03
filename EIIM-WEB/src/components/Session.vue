<template>
  <div class="list">
    <header>
      <el-row>
        <input class="search" type="text" v-model="key" placeholder="search user..." @keyup="onKeyup">
        <el-button icon="iconfont icon-add" circle="true" @click="showSessionDialog"></el-button>
      </el-row>
    </header>

    <ul>
      <li v-for="item in sessions"  @click="selectSession(item.id)" :key="item.id" @contextmenu="showMenu">
        <vue-context-menu v-if="item.members.length > 2" :contextMenuData="contextMenuData"
                          @topSession="topSession(item.id)"
                          @quietSession="quietSession(item.id)"
                          @deleteSession="deleteSession(item.id)">
        </vue-context-menu>

        <vue-context-menu v-if="item.members.length <= 2" :contextMenuData="contextMenuData"
                          @topSession="topSession(item.id)"
                          @quietSession="quietSession(item.id)"
                          @showSession="showSession(item.id)"
                          @deleteSession="deleteSession(item.id)">
        </vue-context-menu>

        <img class="avatar" width="30" height="30" :src="'/rest/v1/download/' + item.id">
        <p class="name">{{item.name}}</p>
      </li>
    </ul>

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
            icoName: 'fa fa-home fa-fw', // icon (icon图标 )
            btnName: '置顶' // The name of the menu option (菜单名称)
          },
          {
            fnHandler: 'quietSession',
            icoName: 'fa fa-home fa-fw',
            btnName: '消息免打扰'
          },
          {
            fnHandler: 'showSession',
            icoName: 'fa fa-home fa-fw',
            btnName: '查看详细资料'
          },
          {
            fnHandler: 'deleteSession',
            icoName: 'fa fa-home fa-fw',
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
    showMenu () {
      event.preventDefault()
      var x = event.clientX
      var y = event.clientY
      // Get the current location
      this.contextMenuData.axis = {
        x, y
      }
    },
    topSession (roomId) {
      alert(1)
    },
    quietSession (roomId) {
      alert(1)
    },
    showSession (roomId) {
      alert(1)
    },

    /**
     *
     */
    deleteSession (sessionId) {
      this.$store.dispatch('chatroom/api_delete_chatroom', sessionId)
    },

    selectSession (sessionId) {
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

    onKeyup (e) {
      let key = this.key
      if (key) {
        this.sessions.filter(session => {
          return session.name.includes(key)
        })
      }
    }
  }
}
</script>

<style scoped lang="less">
  .list {
    li {
      padding: 12px 15px;
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
</style>
