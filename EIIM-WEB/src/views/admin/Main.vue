<template>
  <el-container id="admin" v-bind:style="{height:adminHeight}">
    <el-container>
      <el-aside width="400px" :class="'org-list'">
        <el-row>
          <el-button type="primary" icon="iconfont icon-add" @click="showOrg">新增</el-button>
        </el-row>

        <el-row><el-col class="org-item"/></el-row>

        <el-row align="middle" v-bind:class="org.active ? 'active':''" :gutter="50" v-for="org in orgs"  @click.native="selectOrg(org)" :key="org.id">
          <el-col class="org-item" :span="24">
            <span class="title">{{org.name}}</span>
          </el-col>
        </el-row>

        <!--<el-table v-if="orgs && orgs.length > 0"  :data="orgs" @show-header="false" @row-click="selectOrg" :row-style="selectedHighlight" >-->
          <!--<el-table-column align="left" prop="name" min-width="300px">-->
          <!--</el-table-column>-->

          <!--<el-table-column>-->
            <!--<template slot-scope="scope">-->
              <!--<i class="iconfont u-btn icon-bianji" @click="handleOrgEdit(scope.$index, scope.row)"></i>-->

              <!--<i class="iconfont u-btn icon-shangchu" @click="handleOrgDelete(scope.$index, scope.row)"></i>-->
            <!--</template>-->
          <!--</el-table-column>-->
        <!--</el-table>-->
      </el-aside>

      <el-main style="background:#fff">
        <el-container>
          <el-header style="text-align: right; font-size: 12px">
            <label id="orgName"></label>
            <el-button type="primary" icon="el-icon-plus" circle  @click="showUser">新增用户</el-button>
            <el-button type="danger" icon="iconfont icon-shangchu" circle @click="handleUserDelete">删除用户</el-button>
          </el-header>
          <el-main>
            <el-table v-if="org" :data="org.employees" tooltip-effect="dark" @row-dblclick="handleUserEdit" @selection-change="handleSelectionChange">>
              <el-table-column type="selection" mini-width="5%"></el-table-column>
              <el-table-column prop="fullName" label="姓名" mini-width="10%"></el-table-column>
              <el-table-column prop="title" label="职务" mini-width="20%"></el-table-column>
              <el-table-column prop="mobile" label="手机" mini-width="10%"></el-table-column>
              <el-table-column prop="phone" label="固定电话" mini-width="10%"></el-table-column>
              <el-table-column prop="email" label="邮件地址" mini-width="30%"></el-table-column>
              <el-table-column label="操作" mini-width="15%">
                <template slot-scope="scope">
                  <el-button
                    v-if="!scope.row.account"
                    @click="showAccount(scope.$index, scope.row)">开通帐号</el-button>
                  <el-button
                    icon="iconfont icon-ban"
                    type="danger" v-else-if="scope.row.account.valid"
                    @click="disableAccount(scope.$index, scope.row)">禁用</el-button>
                  <el-button
                    icon="iconfont icon-qiyong"
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
import AccountDialog from '../../components/AccountDialog'

export default {
  name: 'admin',
  components: {OrgDialog, UserDialog, AccountDialog},

  data () {
    return {
      selectedOrgId: null,
      adminHeight: (window.innerHeight) + 'px',
      showOrgDialog: false,
      showUserDialog: false,
      showAccountDialog: false,
      context: {
        type: null,
        action: null,
        data: null
      },
      getIndex: 0,
      multipleSelection: []
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
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

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

    handleUserDelete: function () {
      if(! this.multipleSelection || this.multipleSelection.length === 0) {
        this.$message({
          message: '选择需要删除的用户。',
          type: 'error'
        });
        return
      }

      let personIds = []
      this.multipleSelection.forEach(person =>
        personIds.push(person.id)
      )
      let orgId= this.selectedOrgId
      this.$confirm('此操作将永久删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('contact/api_del_user', personIds).then(res => {
          this.$store.dispatch('contact/api_get_org', orgId)
        })
      })
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
     *  设置密码
     *
     * */
    setPassword () {
      let row = {fullName: '系统管理员', mobile: 'Administrtor'}
      this.context = {type: 'ACCOUNT', action: 'reset', data: row}
      this.showAccountDialog = true
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
        this.$store.dispatch('contact/api_get_org', row.orgId)
      })
    },

    /**
     * 启用帐号
     *
     * */
    enableAccount (index, row) {
      let org = this.org
      row.orgId = org.id
      row.account.state = true

      this.$store.dispatch('account/api_account_enable', row).then(res => {
        this.$store.dispatch('contact/api_get_org', row.orgId)
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
    height: 850px;
    padding: 0px;
    background:#fff;
  }

  #admin .org-list{
    border-right: solid 1px silver;
    padding: 20px;
    background-color:#eee;

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

  .org-list .org-item {
    padding:15px 0px;
    display:table-cell;
    vertical-align:middle;
  }

  .active {
    background-color: #fff;
  }

  .org-list  .org-item .title {
    color: #000;
    font-size: 14px;
    padding-left:10px;
    overflow: hidden;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
    white-space:nowrap;
    width:150px;
    height:24px;
    line-height: 24px;
    display:block;
    float:left;
  }
</style>
