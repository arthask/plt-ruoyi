<script>
import {getNoteInfo, updateNoteInfo} from "@/api/bussiness/note";
import CkEditor from "@/components/Editor/CKEditor.vue";

export default {
  components: {CkEditor},
  props: {
    noteUuid: {
      type: String,
      default: null
    },
  },
  watch: {
    noteUuid() {
      this.getNoteInfo(this.noteUuid);
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
        content: ''
      },
      questionData: {
        value: ''
      },
      questionList: [],
      noteType: null
    };
  },
  created() {
    this.getNoteInfo(this.noteUuid);
  },
  methods: {
    getNoteInfo(noteUuid) {
      getNoteInfo(noteUuid).then(res => {
        this.summaryForm.title = res.data.note.title;
        this.summaryForm.summary = res.data.note.summary;
        this.summaryForm.uuid = res.data.note.uuid;
        this.summaryForm.content = res.data.note.content;
        this.noteType = res.data.note.type
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
      const data = {
        note: this.summaryForm,
        questionList: this.answerForm.data
      };
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
  <div>
    <el-row :gutter="20">
      <el-col>
        <el-form ref="summaryForm1" :label-position="labelPosition" :model="summaryForm" label-width="100px"
                 title="标题">
          <el-form-item
            :label="`标题`"
            :rules="{required: true, message: '问题不能为空', trigger: 'blur'}"
            prop="title"
          >
            <el-input v-model="summaryForm.title"></el-input>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <div v-if="answerForm.data && answerForm.data.length > 0">
      <el-row :gutter="20">
        <el-col>
          <el-form ref="answerForm" :label-position="labelPosition" :model="answerForm" label-width="100px">
            <el-form-item v-for="(data, index) in answerForm.data"
                          :label="data.question"
                          :prop="data.answer"
                          :rules="{required: true, message: '问题不能为空', trigger: 'blur'}">
              <ck-editor v-model="data.answer" :min-height="192" class="bb"></ck-editor>
            </el-form-item>
            <el-form-item>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </div>
    <div v-if="summaryForm.content">
      <el-row :gutter="20">
        <el-col>
          <el-form ref="summaryForm2" :label-position="labelPosition" :model="summaryForm" label-width="100px">
            <el-form-item label="内容" prop="content">
              <ck-editor v-model="summaryForm.content" :min-height="192" class="bb"></ck-editor>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </div>
    <div v-if="summaryForm.summary">
      <el-row :gutter="20">
        <el-col>
          <el-form ref="summaryForm2" :label-position="labelPosition" :model="summaryForm" label-width="100px">
            <el-form-item label="总结" prop="summary">
              <ck-editor v-model="summaryForm.summary" :min-height="192" class="bb"></ck-editor>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </div>
    <el-row :gutter="20" justify="end" style="margin-top: 20px" type="flex">
      <el-col :span="10"></el-col>
      <el-col :span="6">
        <el-button type="primary" @click="submitData()">提交</el-button>
        <el-button type="danger" @click="closeDialog">取消</el-button>
      </el-col>
    </el-row>
  </div>
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
}
</style>
