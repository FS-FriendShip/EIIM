<template>
  <div>
    <el-dialog title="聊天室成员" :visible.sync="visible" @close="closeDialog">
      <el-row>
        <el-col :span="8">
          <el-row>
            <input class="search" type="text" v-model="key" placeholder="search user...">
          </el-row>

          <ul style="height:400px; margin-top: 20px; overflow: auto">
            <li v-for="item in availableUserList" :key="item.id" class="user-wrap">
              <img class="avatar" width="30" height="30" :src="item.avatar">
              <p class="name">{{item.fullName}}</p>
              <el-checkbox width="10%" :key="item.id" @change="selectUser(item.id)"></el-checkbox>
            </li>
          </ul>
        </el-col>
        <el-col :span = "8">
          <div class="grid-content" style="height: 30px;"></div>
        </el-col>
        <el-col :span="8">
          <el-row>
            <el-col :span="24"><div class="grid-content" style="height: 30px;"></div></el-col>
          </el-row>
          <ul style="margin-top: 20px">
            <li v-for="item in sessionMemberList" :key="item.id" class="user-wrap">
              <img class="avatar" width="30" height="30" :src="item.avatar">
              <p class="name">{{item.nickname}}</p>
              <i class="el-icon-remove" @click="unselectUser(item.id)"></i>
            </li>
          </ul>
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="visible = false">取 消</el-button>
        <el-button type="primary" @click="createSession">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'SessionDialog',
  computed: {
    ...mapGetters({availableUserList: 'contact/api_get_contacts'})
  },
  data () {
    return {
      key: null,
      visible: this.show,
      sessionMemberList: [],
      isNew: true,
      isGroup: false
    }
  },

  methods: {
    /**
     *
     *
     **/
    selectUser (value) {
      this.sessionMemberList.push(this.availableUserList.find(item => {
        if (item.id === value) {
          return item.account
        }
      }))
    },

    /**
     *
     *
     * */
    unselectUser (value) {
      // 删除数组中的值
      let index = this.sessionMemberList.findIndex(item => item.id === value)
      this.sessionMemberList.splice(index, 1)

      // 重置checkbox选中状态
    },

    /**
     * 创建会话
     *
     * */
    createSession () {
      let session = this.session ? this.session : {}
      let name = ''
      let accountCodes = [this.GLOBAL.account.accountCode]

      console.log(this.sessionMemberList)
      this.sessionMemberList.forEach(member => {
        name += member.name
        accountCodes.push(member.account.code)
      })

      if (accountCodes.length === 1) {
        this.$message.error('请选择聊天用户')

        return
      }
      session.name = name
      session.accountCodes = accountCodes

      if (this.isNew || (!this.isGroup() && session.accountCodes.length > 2)) {
        this.$store.dispatch('chatroom/api_new_chatroom', session).then((data) => {
          this.dialogVisible = false
        })
      } else {
        this.$store.dispatch('chatroom/api_update_chatroom_members', session).then((data) => {
          this.dialogVisible = false
        })
      }
    },

    /**
     * 关闭对话框
     */
    closeDialog () {
      this.$emit('resetDialogVisible', false)
    }
  },

  props: {
    show: {
      type: Boolean,
      default: false
    },

    session: null
  },

  /**
   * 监控显示
   */
  watch: {
    show () {
      this.visible = this.show
    },

    session () {
      if (this.sessionMemberList) {
        this.sessionMemberList.splice(0, this.sessionMemberList.length)
      }

      if (this.session) {
        this.isNew = false
        this.isGroup = this.session.members.length > 2
        this.session.members.forEach(item => {
          this.sessionMemberList.push(item.account)
        })
      }
    }
  }
}
</script>

<style scoped>
  .el-dialog .search {
    padding: 0 10px;
    width: 80%;
    font-size: 12px;
    color: #fff;
    height: 30px;
    line-height: 30px;
    border: solid 1px #3a3a3a;
    border-radius: 4px;
    outline: none;
    background-color: #26292E;
  }

  .el-dialog .el-dialog__header {
    border-bottom: solid 1px silver;
  }

  .user-wrap {
    display: flex;
    justify-content: flex-start;
    align-items: center;
  }

  .user-wrap .name{
    padding-left:20px;
    width: 70%;
  }
</style>
