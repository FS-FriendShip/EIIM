<template>
  <el-table
    :data="fileList"
    style="width: 100%"
    max-height="250">
    <el-table-column
      prop="sentTime"
      label="发布日期"
      width="150">
    </el-table-column>
    <el-table-column
      prop="author"
      label="发布用户"
      width="150">
    </el-table-column>
    <el-table-column
      prop="fileName"
      label="文件名"
      width="300">
    </el-table-column>
    <el-table-column label="操作">
      <template slot-scope="scope">
        <a :href="scope.row.download">
        <i class="el-icon-download"></i>
        </a>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import {mapGetters} from 'vuex'

export default {
  name: 'FileList',

  computed: {
    ...mapGetters({fileList: 'chatroom/api_get_files'})
  }
}
</script>

<style scoped>

</style>
