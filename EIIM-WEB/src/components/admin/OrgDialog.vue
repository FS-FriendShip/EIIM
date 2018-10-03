<template>
  <div id="org_form">
    <el-dialog title="组织信息" :visible.sync="visible" center  @close="closeDialog">
      <el-form ref="OrgForm" :model="org" label-width="100px">
        <el-form-item label="组织名称" prop="orgName" :rules="[{required: true, message: '组织名称不能为空'}]">
          <el-input v-model="org.name"></el-input>
        </el-form-item>

        <el-form-item label="组织类型">
          <el-radio-group v-model="org.type">
            <el-radio label="internal">内部</el-radio>
            <el-radio label="external">外部</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="visible = false">取 消</el-button>
        <el-button type="primary" @click="saveOrgInfo('OrgForm')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
export default {
  name: 'OrgForm',

  data () {
    return {
      visible: this.show,
      org: {
        id: null,
        code: null,
        name: null,
        type: 'internal'
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
    saveOrgInfo (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.context.action === 'create') {
            this.org.code = this.org.name
          }
          this.$store.dispatch('contact/api_save_org', this.org).then(this.closeDialog)
        } else {
          return false
        }
      })
    },

    /**
     * 关闭对话框
     */
    closeDialog () {
      this.$emit('setOrgDialogVisible', false)
    }
  },

  props: {
    show: {
      type: Boolean,
      default: false
    },

    ßcontext: null
  },

  watch: {
    show () {
      this.visible = this.show
    },

    context () {
      console.log(this.context)
      if (this.context.type === 'ORG') {
        if (this.context.action === 'edit') {
          let existed = this.orgs.filter(org => org.id === this.context.data.orgId)
          this.org.id = existed[0].id
          this.org.code = existed[0].code
          this.org.name = existed[0].name
          this.org.type = existed[0].type
        }
      }
    }
  }
}
</script>

<style scoped>

</style>
