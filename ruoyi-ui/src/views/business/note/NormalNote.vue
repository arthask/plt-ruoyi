<script>
import CkEditor from "@/components/Editor/CKEditor.vue";
import {saveOrUpdateNote} from "@/api/bussiness/note";

export default {
  name: "NormalNote",
  components: {CkEditor},
  data() {
    return {
      labelPosition: 'top',
      note: {
        title: '',
        content: '',
        type: 2
      },
    }
  },
  methods: {
    closeDialog() {
      this.$emit("closeDialog")
    },
    submitData() {
      let params = {
        type: this.note.type,
        content: this.note.content,
        title: this.note.title
      }
      saveOrUpdateNote(params).then(res => {
        if (res.data) {
          this.$modal.msgSuccess("操作成功");
        } else {
          this.$modal.msgError("操作失败！");
        }
      })
      this.closeDialog()
    }
  }
}
</script>

<template>
  <div>
    <el-row :gutter="20">
      <el-col>
        <el-form :label-position="labelPosition" :model="note" label-width="100px"
                 title="标题">
          <el-form-item
            :label="`标题`"
            :rules="{required: true, message: '问题不能为空', trigger: 'blur'}"
            prop="title"
          >
            <el-input v-model="note.title"></el-input>
          </el-form-item>
          <el-form-item label="内容" prop="content">
            <ck-editor v-model="note.content" :min-height="192" class="bb"></ck-editor>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <el-row :gutter="20" justify="end" style="margin-top: 20px" type="flex">
      <el-col :span="10"></el-col>
      <el-col :span="6">
        <el-button type="primary" @click="submitData()">提交</el-button>
        <el-button type="danger" @click="closeDialog">取消</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<style lang="scss" scoped>

</style>
