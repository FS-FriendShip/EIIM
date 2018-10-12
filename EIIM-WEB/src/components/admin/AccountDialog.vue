<template>
  <div id="AccountDialog">
    <el-dialog title="帐号信息" :visible.sync="visible" center  @close="closeDialog">
      <el-form ref="accountForm" :model="account" status-icon label-width="100px" :rules="validateRules">
        <el-form-item label="姓名">
          <el-input v-model="account.nickname" :disabled="true"></el-input>
        </el-form-item>

        <el-form-item label="用户名">
          <el-input v-model="account.accountCode" :disabled="true"></el-input>
        </el-form-item>

        <el-form-item label="密码"  prop="password">
          <el-input type="password" placeholder="密码" v-model="account.password"></el-input>
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input type="password" placeholder="确认密码" v-model="account.confirmPassword"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="visible = false">取 消</el-button>
        <el-button type="primary" @click="saveAccountInfo('accountForm')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'UserForm',

  data () {
    function checkStrong (value) {
      var strength = 0
      if (value.length > 7 && value.match(/[\da-zA-Z]+/)) {
        if (value.match(/\d+/)) {
          strength++
        }
        if (value.match(/[a-z]+/)) {
          strength++
        }
        if (value.match(/[A-Z]+/)) {
          strength++
        }
      }
      if (strength >= 2) {
        return true
      }
      return false
    }

    var validatePassword = (rule, value, callback) => {
      if (!value || value === '') {
        callback(new Error('请输入密码'))
      } else if (!checkStrong(value)) {
        callback(new Error('8-20位数字、大小写字母'))
      } else {
        if (this.account.confirmPassword !== '') {
          this.$refs.accountForm.validateField('confirmPassword')
        }
        callback()
      }
    }

    var validateConfirmPassword = (rule, value, callback) => {
      if (!value || value === '') {
        callback(new Error('请再次输入密码'))
      } else if (!checkStrong(value)) {
        callback(new Error('8-20位数字、大小写字母'))
      } else if (value !== this.account.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }

    return {
      visible: this.show,
      account: {
        personId: null,
        accountCode: null,
        password: null,
        confirmPassword: null,
        nickname: null,
        avatar: null
      },

      validateRules: {
        password: [
          { validator: validatePassword, trigger: 'blur' }
        ],
        confirmPassword: [
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      }
    }
  },

  methods: {
    saveAccountInfo (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$store.dispatch('account/api_account_save', {personId: this.account.personId, account: {accountCode: this.account.accountCode, password: this.account.password, nickname: this.account.nickname, avatar: this.account.accountCode + '.png'}}).then(this.closeDialog)
        } else {
          return false
        }
      })
    },

    /**
     * 关闭对话框
     */
    closeDialog () {
      this.$emit('setAccountDialogVisible', false)
    }
  },

  props: {
    show: {
      type: Boolean,
      default: false
    },

    context: null
  },

  watch: {
    show () {
      this.visible = this.show
    },

    context () {
      if (this.context.type === 'ACCOUNT') {
        console.log(this.context.data)
        if (!this.context.data.account) {
          this.account.accountCode = this.context.data.mobile
          this.account.nickname = this.context.data.fullName
          this.account.avatar = this.context.data.fullName + '.png'
        } else {
          this.account.accountCode = this.context.data.accountCode
        }

        this.account.personId = this.context.data.id
      }
    }
  }
}
</script>

<style scoped>

</style>
