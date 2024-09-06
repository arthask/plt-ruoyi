<script>
import {getNoteInfo} from "@/api/bussiness/note";

export default {
  props: {
    noteUuid: {
      type: String,
      default: null
    },
  },
  watch: {
    noteUUId() {
      this.getNoteInfo(this.noteUuid);
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
    closeDialog() {
      this.$emit("closeDialog")
    }
  }
}
</script>

<template>
  <div>
    <div class="ck-content">
      <div class="title">
        <h1>{{ this.summaryForm.title }}</h1>
      </div>
      <div v-if="noteType===0">
        <h3>问题</h3>
        <div v-for="(data, index) in answerForm.data">
          <el-row :gutter="5" justify="center" type="flex">
            <el-col :span="2">Q{{ index + 1 }}:</el-col>
            <el-col :span="20"><span>{{ data.question }}</span></el-col>
          </el-row>
          <el-row :gutter="5" justify="center" style="margin-top: 10px" type="flex">
            <el-col :span="2">答:</el-col>
            <el-col :span="20"><span v-html="data.answer"></span></el-col>
          </el-row>
        </div>
      </div>
      <div v-if="noteType===1">
        <div v-html="this.summaryForm.content"></div>
      </div>
      <div v-if="this.summaryForm.summary">
        <h3>总结</h3>
        <div v-html="this.summaryForm.summary"></div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.title {
  text-align: center;
  padding-bottom: 10px
}
</style>
