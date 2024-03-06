<script>
import { getNoteInfo } from "@/api/system/note";
import { updateNoteInfo } from "@/api/system/note";

export default {
  props: {
    noteId: {
      type: Number,
      default: 0
    },
  },
  watch: {
    noteId(){
      this.getNoteInfo(this.noteId);
    }
  },
  data() {
    return {
      labelPosition: 'top',
      answerForm: {
        data: []
      },
      summaryForm: {
        title: '',
        summary: '',
        uuid: '',
      },
      questionData: {
        value: ''
      },
      noteUUID: '',
      questionList: []
    };
  },
  created() {
    this.getNoteInfo(this.noteId);
  },
  methods: {
    getNoteInfo(noteId) {
      getNoteInfo(noteId).then(res => {
        this.summaryForm.title = res.data.note.title;
        this.summaryForm.summary = res.data.note.summary;
        this.summaryForm.uuid = res.data.note.uuid;
        this.answerForm.data.splice(0, this.answerForm.data.length)
        for (let i = 0; i < res.data.questionList.length; i++) {
          this.answerForm.data.push(res.data.questionList[i]);
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    removeDomain(item) {
      if (this.dynamicValidateForm.questions.length === 1) {
        this.$notify({
          type: "success",
          message: "只有一个了，不能删除"
        });
        return;
      }
      const index = this.dynamicValidateForm.questions.indexOf(item);
      if (index !== -1) {
        this.dynamicValidateForm.questions.splice(index, 1)
      }
    },
    closeDialog() {
      this.$emit("closeDialog")
    },
    submitData() {
      var data = {
        note: this.summaryForm,
        questionList:this.answerForm.data
      }
      updateNoteInfo(data).then(res => {
        if (res.code === 200) {
          this.$notify({
            type: "success",
            message: "操作成功"
          });
          this.closeDialog();
        } else {
          this.$notify({
            type: "error",
            message: "操作失败"
          });
        }
      })
    }
  }
}
</script>

<template>
  <el-container>
    <el-header>
    </el-header>
    <el-main>
      <el-form :model="summaryForm" ref="summaryForm1" label-width="115px" title="标题"
      >
        <el-form-item
          prop="title"
          :label="`标题`"
          :rules="{required: true, message: '问题不能为空', trigger: 'blur'}"
        >
          <el-row :gutter="20" type="flex" justify="center">
            <el-col :span="16">
              <el-input v-model="summaryForm.title"></el-input>
            </el-col>
            <el-col :span="6">
            </el-col>
            <el-col :span="2"></el-col>
          </el-row>
        </el-form-item>
      </el-form>
      <el-row :gutter="20" type="flex" justify="center">
        <el-col :span="8">
          <el-form :label-position="labelPosition" label-width="100px" :model="answerForm" ref="answerForm">
            <el-form-item v-for="(data, index) in answerForm.data"
                          :prop="data.answer"
                          :label="data.question"
                          :rules="{required: true, message: '问题不能为空', trigger: 'blur'}">
              <editor class="bb" v-model="data.answer" :min-height="192"/>
            </el-form-item>
            <el-form-item>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="12"></el-col>
      </el-row>
      <el-row :gutter="20" type="flex" justify="center">
        <el-col :span="8">
          <el-form :label-position="labelPosition" label-width="100px" :model="summaryForm" ref="summaryForm2">
            <el-form-item label="总结" prop="summary">
              <editor class="bb" v-model="summaryForm.summary" :min-height="192"/>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="12"></el-col>
      </el-row>
    </el-main>
    <el-footer>
      <el-row :gutter="20" type="flex" justify="end">
        <el-col :span="4">
          <el-button type="primary" style="margin-top: 12px;" @click="submitData()">提交</el-button>
        </el-col>
      </el-row>
    </el-footer>
  </el-container>
</template>

<style scoped lang="scss">
.aa {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  margin: 0 auto;
}

.bb {
  width: 800px;
}
</style>
