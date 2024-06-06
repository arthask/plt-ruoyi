<script>
import { getNoteInfo } from "@/api/bussiness/note";

export default {
  props: {
    noteId: {
      type: Number,
      default: null
    },
  },
  watch: {
    noteId(){
      this.getNoteInfo(this.noteId);
    },
    immediate: true
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
    closeDialog() {
      this.$emit("closeDialog")
    }
  }
}
</script>

<template>
  <div style="padding: 20px">
    <div class="ck-content">
      <el-row :gutter="20" type="flex" justify="center">
        <el-col :span="10"><h1>{{this.summaryForm.title}}</h1></el-col>
      </el-row>
      <h2>问题</h2>
      <div v-for="(data, index) in answerForm.data">
        <el-row  :gutter="5" type="flex" justify="center">
          <el-col :span="2">Q{{index+1}}:</el-col>
          <el-col :span="20"><span>{{data.question}}</span> </el-col>
        </el-row>
        <el-row :gutter="5" type="flex" justify="center" style="margin-top: 10px">
          <el-col :span="2">答:</el-col>
          <el-col :span="20"><span v-html="data.answer"></span> </el-col>
        </el-row>
      </div>
      <h2>总结</h2>
      <div v-html="this.summaryForm.summary"></div>
    </div>
  </div>
</template>

<style scoped lang="scss">
</style>
