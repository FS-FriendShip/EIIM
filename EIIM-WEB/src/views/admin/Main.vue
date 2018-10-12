<template>
  <el-container id="admin">
    <el-header id="main-header">
      <el-row type="flex" class="row-bg" align="middle">
        <el-col :span="23" class="title">若森通信软件用户管理平台</el-col>
        <el-col :span="1"><i class="iconfont icon-tuichu quit" @click="logout">退出</i></el-col>
      </el-row>
    </el-header>

    <el-container id="main-body">
      <el-aside width="400px" :class="'org-list'">
        <el-row>
          <el-button type="primary" icon="iconfont icon-add" @click="showOrg">新增</el-button>
        </el-row>
        <el-table v-if="orgs && orgs.length > 0"  :data="orgs" @show-header="false" @row-click="selectOrg" :row-style="selectedHighlight" :row-class-name="tableRowClassName" >
          <el-table-column align="left" prop="name" min-width="300px">
          </el-table-column>

          <el-table-column>
            <template slot-scope="scope">
              <i class="iconfont u-btn icon-bianji" @click="handleOrgEdit(scope.$index, scope.row)"></i>

              <i class="iconfont u-btn icon-shangchu" @click="handleOrgDelete(scope.$index, scope.row)"></i>
            </template>
          </el-table-column>
        </el-table>
      </el-aside>

      <el-main style="background:#fff">
        <el-container>
          <el-header style="text-align: right; font-size: 12px">
            <label id="orgName"></label>
            <el-button type="primary" icon="el-icon-plus" circle  @click="showUser">新增用户</el-button>
            <el-button type="danger" icon="iconfont icon-shangchu" circle>删除用户</el-button>
          </el-header>
          <el-main>
            <el-table v-if="org" :data="org.employees" tooltip-effect="dark" @row-dblclick="handleUserEdit">
              <el-table-column type="selection" mini-width="5%"></el-table-column>
              <el-table-column prop="fullName" label="姓名" mini-width="15%"></el-table-column>
              <el-table-column prop="title" label="职务" mini-width="20%"></el-table-column>
              <el-table-column prop="mobile" label="手机" mini-width="15%"></el-table-column>
              <el-table-column prop="phone" label="固定电话" mini-width="10%"></el-table-column>
              <el-table-column prop="email" label="邮件地址" mini-width="35%"></el-table-column>
              <el-table-column label="操作" mini-width="5%">
                <template slot-scope="scope">
                  <el-button
                    size="mini" v-if="!scope.row.account"
                    @click="showAccount(scope.$index, scope.row)">开通帐号</el-button>
                  <el-button
                    icon="iconfont icon-ban"
                    size="mini"
                    type="danger" v-else-if="scope.row.account.state"
                    @click="disableAccount(scope.$index, scope.row)">禁用</el-button>
                  <el-button
                    icon="iconfont icon-qiyong"
                    size="mini"
                    type="danger" v-else
                    @click="enableAccount(scope.$index, scope.row)">启用</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-main>
        </el-container>
      </el-main>
    </el-container>

    <OrgDialog v-on:setOrgDialogVisible="setOrgDialogVisible" :context="context" :show.sync="showOrgDialog"></OrgDialog>
    <UserDialog v-on:setUserDialogVisible="setUserDialogVisible" :context="context" :show.sync="showUserDialog"></UserDialog>
    <AccountDialog v-on:setAccountDialogVisible="setAccountDialogVisible" :context="context" :show.sync="showAccountDialog"></AccountDialog>
  </el-container>
</template>

<script>
import {mapGetters} from 'vuex'
import OrgDialog from '../../components/admin/OrgDialog'
import UserDialog from '../../components/admin/UserDialog'
import AccountDialog from '../../components/admin/AccountDialog'

export default {
  name: 'admin',
  components: {OrgDialog, UserDialog, AccountDialog},

  data () {
    return {
      showOrgDialog: false,
      showUserDialog: false,
      showAccountDialog: false,
      context: {
        type: null,
        action: null,
        data: null
      },
      getIndex: 0
    }
  },

  /**
   *
   */
  created  () {
    this.$store.dispatch('contact/api_init_orgs').then(res => {
      let org = res.data[0]
      this.selectOrg({id: org.id, index: 0})
    })
  },

  /**
   *
   */
  computed: {
    ...mapGetters({orgs: 'contact/api_get_orgs', org: 'contact/api_get_org', account: 'account/api_get_account', currentUser: 'account/api_current_account'})
  },

  methods: {
    selectOrg: function (row) {
      this.getIndex = row.index
      this.selectedOrgId = row.id
      this.$store.dispatch('contact/api_get_org', row.id)
    },

    showOrg: function () {
      this.context = {type: 'ORG', action: 'create'}
      this.showOrgDialog = true
    },

    handleOrgEdit: function (index, row) {
      this.context = {type: 'ORG', action: 'edit', data: row}
      this.showOrgDialog = true
    },

    handleOrgDelete: function (index, row) {
      this.$confirm('此操作将永久删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('contact/api_delete_org', row.id)
      })
    },

    setOrgDialogVisible (visible) {
      this.showOrgDialog = visible
    },

    tableRowClassName ({row, rowIndex}) {
      row.index = rowIndex
    },

    selectedHighlight ({row, rowIndex}) {
      if ((this.getIndex) === rowIndex) {
        return {
          'background-color': 'rgb(250, 195, 100)'
        }
      }
    },

    /**
     *
     */
    showUser: function () {
      this.context = {type: 'USER', action: 'create', data: {orgId: this.org.id}}
      this.showUserDialog = true
    },

    handleUserEdit: function (row, column) {
      this.context = {type: 'USER', action: 'edit', data: {orgId: this.org.id, userId: row.id}}
      this.showUserDialog = true
    },

    handleUserDelete: function (index, row) {
      this.$store.dispatch('contact/api_delete_org', row.id)
    },

    setUserDialogVisible (visible) {
      this.showUserDialog = visible
    },

    /**
     *
     * @param row
     * @param column
     */
    showAccount: function (index, row) {
      this.context = {type: 'ACCOUNT', action: 'create', data: row}
      this.showAccountDialog = true
    },

    setAccountDialogVisible (visible) {
      this.showAccountDialog = visible
    },

    /**
     * 禁用帐号
     *
     * */
    disableAccount (index, row) {
      let org = this.org
      row.orgId = org.id
      row.account.state = false

      this.$store.dispatch('account/api_account_disable', row).then(res => {
        this.$store.dispatch('contact/api_update_employee', row)
      })
    },

    /**
     * 启用帐号ß
     *
     * */
    enableAccount (index, row) {
      let org = this.org
      row.orgId = org.id
      row.account.state = true

      this.$store.dispatch('account/api_account_enable', row).then(res => {
        this.$store.dispatch('contact/api_update_employee', row)
      })
    },

    /**
     *
     */
    logout: function () {
      this.$store.dispatch('account/api_account_logout', this.currentUser.account).then(res =>
        this.$router.push({name: 'Login'})
      )
    }
  }
}
</script>

<style scoped lang="less">
  #admin {
    width: 100%;
    height: 100%;
    margin: 10px 0px 0px 0px;
    padding: 0;
  }

  #admin #main-header{
    height:80px!important;
  }

  #main-header .title {
    font-weight: bolder;
    font-size: 40px;
    color: #8B5A00;
  }

  #main-header .quit {
    font-weight: bolder;
    font-size: 18px;
    color: #fff;
    cursor: pointer;
  }

  #admin  #main-body {
    height:850px;
    background:#fff;
  }

  #main-body .org-list{
    border-right: solid 1px silver;
    padding: 20px;

    .item-org {
      margin-top: 20px;
      tex-align: left;
      .name {
        width: 100%
      }
    }

    .name {
      text-align:left;
    }
  }

  .org-list {
    .u-btn {
      display:none;
    }
    .el-table__body tr:hover{
      .u-btn{
        display: inline;
      }
    }
  }

  #main-header .row-bg {
    height:100%;
  }
</style>
