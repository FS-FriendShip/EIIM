<template>
  <div id="UserDialog">
    <el-dialog title="人员信息" :visible.sync="visible" center  @close="closeDialog">
      <el-form ref="UserForm"  :model="user" label-width="100px" :rules="validateRules">
        <el-form-item label="姓名" prop="fullName">
          <el-input v-model="user.fullName"></el-input>
        </el-form-item>

        <el-form-item label="性别">
          <el-radio-group v-model="user.sex">
            <el-radio label="MALE">男</el-radio>
            <el-radio label="FEMALE">女</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="职务">
          <el-input v-model="user.title"></el-input>
        </el-form-item>

        <el-form-item label="固定电话">
          <el-input v-model="user.phone"></el-input>
        </el-form-item>

        <el-form-item label="移动电话" prop="mobile">
          <el-input v-model="user.mobile"></el-input>
        </el-form-item>

        <el-form-item label="邮箱">
          <el-input v-model="user.email"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="visible = false">取 消</el-button>
        <el-button type="primary" @click="saveUserInfo('UserForm')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
export default {
  name: 'UserForm',

  data () {
    var checkMobile = (str) => {
      var mobilereg = /^[1][3,4,5,7,8][0-9]{9}$/
      if (!mobilereg.test(str)) {
        return false
      } else {
        return true
      }
    }

    var validateName = (rule, value, callback) => {
      if (!value || value === '') {
        callback(new Error('请输入姓名'))
      } else {
        callback()
      }
    }

    var validateMobile = (rule, value, callback) => {
      if (!value || value === '') {
        callback(new Error('请输入手机号'))
      } else if (!checkMobile(value)) {
        callback(new Error('请检查手机号'))
      } else {
        callback()
      }
    }

    return {
      visible: this.show,
      user: {
        id: null,
        orgId: null,
        fullName: null,
        title: null,
        sex: 'MALE',
        mobile: null,
        phone: null,
        email: null
      },

      validateRules: {
        fullName: [
          { validator: validateName, trigger: 'blur' }
        ],
        mobile: [
          { validator: validateMobile, trigger: 'blur' }
        ]
      }
    }
  },

  /**
   *
   */
  computed: {
    ...mapGetters({orgs: 'contact/api_get_orgs'})
  },

  methods: {
    saveUserInfo (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$store.dispatch('contact/api_save_user', this.user).then(this.closeDialog)
        } else {
          return false
        }
      })
    },

    /**
     * 关闭对话框
     */
    closeDialog () {
      this.$emit('setUserDialogVisible', false)
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
      if (this.context.type === 'USER') {
        if (this.context.action === 'edit') {
          let org = this.orgs.filter(org => org.id === this.context.data.orgId)
          let user = org[0].employees.filter(employee => employee.id === this.context.data.userId)[0]
          this.user.id = user.id
          this.user.fullName = user.fullName
          this.user.orgId = this.context.data.orgId
          this.user.title = user.title
          this.user.sex = user.sex
          this.user.mobile = user.mobile
          this.user.phone = user.phone
          this.user.email = user.email
        } else {
          this.user.orgId = this.context.data.orgId
          this.user.id = null
          this.user.fullName = null
          this.user.title = null
          this.user.sex = 'MALE'
          this.user.mobile = null
          this.user.phone = null
          this.user.email = null
        }
      }
    }
  }
}
</script>

<style scoped>

</style>
