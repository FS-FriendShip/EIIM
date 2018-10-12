<template>
  <div id="org_form">
    <el-dialog title="组织信息" :visible.sync="visible" center  @close="closeDialog">
      <el-form ref="OrgForm" :model="org" label-width="100px">
        <el-form-item label="组织名称" :rules="[{required: true, message: '组织名称不能为空'}]">
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

    context: null
  },

  watch: {
    show () {
      this.visible = this.show
    },

    context () {
      if (this.context.type === 'ORG') {
        if (this.context.action === 'edit') {
          let existed = this.context.data
          this.org.id = existed.id
          this.org.code = existed.code
          this.org.name = existed.name
          this.org.type = existed.type
        }
      }
    }
  }
}
</script>

<style scoped>

</style>
