<template>
  <div id="session-options">
    <el-dialog title="聊天室成员" :visible.sync="visible" @close="closeDialog">
      <el-row>
        <el-col :span="8">
          <el-row>
            <input class="search" type="text" v-model="key" placeholder="search user...">
          </el-row>

          <ul style="height:400px; margin-top: 20px; overflow: auto">
            <li v-for="item in availableUserList" :key="item.id" class="user-wrap">
              <img class="avatar-small" :src="'/rest/v1/download/' + item.id">
              <p class="name">{{item.account.nickname}}</p>
              <el-checkbox v-model="item.checked" :name="item.id" width="10%" :key="item.id" @change="selectUser(item)"></el-checkbox>
            </li>
          </ul>
        </el-col>
        <el-col :span = "8">
          <div class="grid-content" style="height: 30px;"></div>
        </el-col>
        <el-col :span="8" class="selected-list">
          <el-row>
            <el-col :span="24"><div class="list-title">已选择联系人</div></el-col>
          </el-row>
          <ul style="margin-top: 20px">
            <li v-if="sessionMemberList" v-for="item in sessionMemberList" :key="item.id" class="user-wrap">
              <img class="avatar-small" :src="'/rest/v1/download/' + item.id">
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

  created () {
    this.sessionMemberList = []
  },

  methods: {
    /**
     *
     *
     **/
    selectUser (item) {
      if (item.checked) {
        this.sessionMemberList.push(item.account)
      } else {
        let index = this.sessionMemberList.findIndex(member => member.id === item.id)
        this.sessionMemberList.splice(index, 1)
      }
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
      let allMembers = this.availableUserList
      allMembers.forEach(item => {
        if (item.account.id === value) {
          item.checked = false
        }
      })
    },

    /**
     * 创建会话
     *
     * */
    createSession () {
      let session = this.session ? this.session : {}
      let names = ''
      let accountCodes = [this.GLOBAL.account.accountCode]

      this.sessionMemberList.forEach(member => {
        console.log(member)
        names += member.nickname
        accountCodes.push(member.code)
      })

      if (accountCodes.length === 1) {
        this.$message.error('请选择聊天用户')

        return
      }
      session.name = names
      session.accountCodes = accountCodes

      if (this.isNew || (!this.isGroup && session.accountCodes.length > 2)) {
        console.log('create new session')
        this.$store.dispatch('chatroom/api_new_chatroom', session).then((data) => {
          this.dialogVisible = false
        })
      } else {
        console.log('update session members')
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

    session: {
      type: Object,
      default: null
    }
  },

  /**
   * 监控显示
   */
  watch: {
    show () {
      this.visible = this.show
    },

    /**
     * 监控session属性的变化
     */
    session () {
      console.log(this.session)
      if (this.session) {
        this.sessionMemberList = []
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
    background-color: #fff;
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

  #session-options .selected-list .list-title{
    height: 30px;
    font-weight:bolder;
    font-size:16px;
  }
</style>
