<template>
  <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
    <el-container>
      <el-header>
        <el-row type="flex" class="row-bg" justify="space-between">
          <el-col :span="6">
            <div class="grid-content bg-purple">
              <el-button type="primary">上一个</el-button>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="grid-content bg-purple">
              <el-button type="primary">下一个</el-button>
            </div>
          </el-col>
        </el-row>
      </el-header>
      <el-main>
        <h2>HELLO WORD</h2>
      </el-main>
      <el-footer>
        <el-input
          type="textarea"
          :rows="2"
          placeholder="请输入内容"
          v-model="textarea">
        </el-input>
      </el-footer>
    </el-container>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      title: '',
      open: true,
      textarea: ''
    }
  }
}
</script>
