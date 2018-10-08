<template>
  <div class="card">
    <vue-context-menu :contextMenuData="contextMenuData" @quit="quit">
    </vue-context-menu>

    <el-row @contextmenu.native="showMenu">
      <el-col :span="4"><img class="avatar-large" :src="'/rest/v1/download/'"></el-col>
      <el-col :span="20">
        <!--<p class="name">{{currentUser.nickname}}</p>-->
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'Profile',

  data () {
    return {
      contextMenuData: {
        menuName: 'session-menu',
        axis: {
          x: null,
          y: null
        },
        // Menu options (菜单选项)
        menulists: [
          {
            fnHandler: 'quit',
            icoName: 'iconfont icon-tuichu',
            btnName: '登出'
          }
        ]
      }
    }
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

    quit () {
      this.$store.dispatch('account/api_account_logout', this.currentUser.account).then(res =>
        this.$router.push({name: 'Login'})
      )
    }
  },

  computed: {
    ...mapGetters({currentUser: 'account/api_current_account'})
  }
}
</script>

<style scoped lang="less">
  .card {
    padding: 12px;
    border-bottom: solid 1px #24272C;

    footer {
      margin-top: 10px;
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
      font-size: 16px;
    }
  }
</style>
