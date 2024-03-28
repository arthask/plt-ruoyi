<script>
import {addQuestion} from "@/api/system/question";
import {updateNote} from "@/api/system/note";
import CkEditor from "@/components/Editor/CKEditor.vue";

export default {
  components: {CkEditor},
  data() {
    return {
      active1: 0,
      showStep1: true,
      showStep2: false,
      showStep3: false,
      showStep4: false,
      dynamicValidateForm: {
        questions: [{value: ''}],
      },
      labelPosition: 'top',
      answerForm: {
        data: []
      },
      summaryForm: {
        title: '',
        summary: '',
      },
      questionData: {
        value: ''
      },
      answerData: {
        key: '',
        question: '',
        answer: '',
        tag: ''
      },
      noteUUID: '',
      questionList: []
    };
  },

  methods: {
    updateShowStep: function () {
      if (this.active1 === 0) {
        this.showStep1 = true;
        this.showStep2 = false;
        this.showStep3 = false;
        this.showStep4 = false;
      }
      if (this.active1 === 1) {
        this.showStep1 = false;
        this.showStep2 = true;
        this.showStep3 = false;
        this.showStep4 = false;
      }
      if (this.active1 === 2) {
        this.showStep1 = false;
        this.showStep2 = false;
        this.showStep3 = true;
        this.showStep4 = false;
        this.questionList.splice(0, this.questionList.length)
        for (let i = 0; i < this.dynamicValidateForm.questions.length; i++) {
          this.questionList.push(this.dynamicValidateForm.questions[i].value)
          // 如果有重复的，则不再添加
          const filterArray = this.answerForm.data.filter(e => e.question === this.dynamicValidateForm.questions[i].value)
          if (filterArray.length > 0) {
            continue;
          }

          this.answerForm.data.push({
            key: Date.now() + i,
            question: this.dynamicValidateForm.questions[i].value
          })
        }
        var elements = this.answerForm.data.filter(item => !this.questionList.includes(item.question));
        for (let i = 0; i < elements.length; i++) {
          this.answerForm.data.splice(this.answerForm.data.indexOf(elements[i]), 1)
        }
        console.log(elements)
        console.log(this.answerForm.data)
        console.log(this.questionList)
      }
      if (this.active1 === 3) {
        this.showStep1 = false;
        this.showStep2 = false;
        this.showStep3 = false;
        this.showStep4 = true;
      }
    },
    getFormName: function () {
      let formName = '';
      if (this.active1 === 0) {
        formName = "summaryForm1"
      }
      if (this.active1 === 1) {
        formName = "dynamicValidateForm"
      }
      if (this.active1 === 2) {
        formName = "answerForm"
      }
      if (this.active1 === 3) {
        formName = "summaryForm2"
      }
      return formName;
    },
    next() {
      let formName = this.getFormName();
      console.log(formName)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.active1++ > 3) this.active1 = 0;
          this.updateShowStep();
        } else {
          this.$notify({
            type: "error",
            message: "必填内容不能为空!!"
          });
        }
      });
    },
    forward() {
      let formName = this.getFormName();
      console.log(formName)
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.active1-- < 0) this.active1 = 4;
          this.updateShowStep();
        } else {
          this.$notify({
            type: "error",
            message: "必填内容不能为空!!"
          });
        }
      });
    },
    submitSomeForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var addData = {
            questions: this.answerForm.data,
            noteTitle: this.summaryForm.title
          };
          addQuestion(addData).then(res => {
            if (res.data.isSuccess) {
              this.noteUUID = res.data.noteUUID
              this.$confirm('是否继续补全总结信息?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                this.next();
              }).catch(() => {
                this.closeDialog();
              });
            } else {
              this.$notify({
                type: "error",
                message: "提交失败"
              });
            }
          })
        } else {
          this.$notify({
            type: "error", message: "提交失败"
          });
        }
      });
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
    addDomain() {
      this.dynamicValidateForm.questions.push({
        value: '',
        key: Date.now()
      });
    },
    closeDialog() {
      this.$emit("closeDialog")
    },
    editNoteSummary(formName) {
      this.active1++
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var noteData = {
            uuid: this.noteUUID,
            summary: this.summaryForm.summary
          };
          updateNote(noteData).then(res => {
            if (res.code === 200) {
              this.$notify({
                type: "success",
                message: "操作成功"
              });
              this.closeDialog();
            } else {
              this.$notify({
                type: "error",
                message: "操作交失败"
              });
            }
          })
        } else {
          this.$notify({
            type: "error", message: "提交失败"
          });
        }
      });
    }
  }
}
</script>

<template>
  <el-container>
    <el-header style="margin-top: 50px">
      <el-row :gutter="20">
        <el-steps :active="active1" finish-status="success" align-center>
          <el-step title="步骤 1"></el-step>
          <el-step title="步骤 2"></el-step>
          <el-step title="步骤 3"></el-step>
          <el-step title="步骤 4"></el-step>
        </el-steps>
      </el-row>
    </el-header>
    <el-main>
      <el-row :gutter="20" type="flex" justify="center" v-show="showStep2">
        <el-col :span="3"><h3>问题列表</h3></el-col>
        <el-col :span="17"></el-col>
      </el-row>
      <el-row :gutter="20" type="flex" justify="center" v-show="showStep3">
        <el-col :span="3"><h3>详细列表</h3></el-col>
        <el-col :span="17"></el-col>
      </el-row>
      <el-form :model="summaryForm" ref="summaryForm1" label-width="115px" v-show="showStep1"
               title="标题"
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
            <el-col :span="2">
              <el-button @click="resetForm('dynamicValidateForm')">重置</el-button>
            </el-col>
            <el-col :span="2"></el-col>
          </el-row>
        </el-form-item>
      </el-form>
      <el-form :model="dynamicValidateForm" ref="dynamicValidateForm" label-width="115px" v-show="showStep2"
               title="问题列表">
        <el-form-item
          v-for="(question, index) in dynamicValidateForm.questions"
          :prop="'questions.'+index+'.value'"
          :label="'问题'"
          :rules="{required: true, message: '问题不能为空', trigger: 'blur'}"
        >
          <el-row :gutter="20" type="flex" justify="center">
            <el-col :span="16">
              <el-input v-model="question.value"></el-input>
            </el-col>
            <el-col :span="6">
              <el-button @click.prevent="removeDomain(question)">删除</el-button>
              <el-button @click="addDomain">新增</el-button>
            </el-col>
            <el-col :span="2"></el-col>
          </el-row>
        </el-form-item>

        <el-form-item>
          <el-button @click="resetForm('dynamicValidateForm')">重置</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20" type="flex" justify="center">
        <el-col :span="8">
          <el-form :label-position="labelPosition" label-width="100px" :model="answerForm" ref="answerForm"
                   v-show="showStep3">
            <el-form-item v-for="(data, index) in answerForm.data"
                          :prop="data.answer"
                          :label="data.question"
                          :key="data.key"
                          :rules="{required: true, message: '答案不能为空', trigger: 'blur'}">
<!--              <editor class="bb" v-model="data.answer" :min-height="192"/>-->
              <ck-editor class="bb" v-model="data.answer" :min-height="192"></ck-editor>
            </el-form-item>
            <el-form-item>
              <el-button @click="resetForm('answerForm')">重置</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="12"></el-col>
      </el-row>
      <el-row :gutter="20" type="flex" justify="center">
        <el-col :span="8">
          <el-form :label-position="labelPosition" label-width="100px" :model="summaryForm" v-show="showStep4"
                   ref="summaryForm2">
            <el-form-item label="总结" prop="summary">
<!--              <editor class="bb" v-model="summaryForm.summary" :min-height="192"/>-->
              <ck-editor class="bb" v-model="summaryForm.summary" :min-height="192"></ck-editor>
            </el-form-item>
            <el-form-item>
              <el-button @click="resetForm('summaryForm2')">重置</el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="12"></el-col>
      </el-row>
    </el-main>
    <el-footer>
      <el-row :gutter="20" type="flex" justify="end">
        <el-col :span="4" v-show="showStep3">
          <el-button type="primary" style="margin-top: 12px;" @click="submitSomeForm('answerForm')">提交</el-button>
        </el-col>
        <el-col :span="4" v-show="!showStep1 && !showStep4">
          <el-button style="margin-top: 12px;" @click="forward">上一步</el-button>
        </el-col>
        <el-col :span="4" v-show="!showStep4 && !showStep3">
          <el-button style="margin-top: 12px;" @click="next">下一步</el-button>
        </el-col>
        <el-col :span="4" v-show="showStep4">
          <el-button type="primary" style="margin-top: 12px;" @click="editNoteSummary('summaryForm2')">完成</el-button>
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
