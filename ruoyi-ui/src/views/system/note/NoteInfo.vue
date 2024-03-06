<script>
import { getNoteInfo } from "@/api/system/note";

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
  <div class="ql-container ql-snow">
    <div class="ql-editor">
      <el-row :gutter="20" type="flex" justify="start" class="cc">
        <el-col :span="12"><h1>{{this.summaryForm.title}}</h1></el-col>
      </el-row>
      <h2 class="dd">问题</h2>
      <div class="ee" v-for="(data, index) in answerForm.data">
        <el-row style="margin-bottom: 10px;" :gutter="0" type="flex" justify="start">
          <el-col :span="2"><h3>Q{{index+1}}:</h3></el-col>
          <el-col :span="15"><span>{{data.question}}</span> </el-col>
        </el-row>
        <el-row :gutter="0" type="flex" justify="start">
          <el-col :span="2"><h3>答:</h3></el-col>
          <el-col :span="15"><span v-html="data.answer"></span> </el-col>
        </el-row>
      </div>
      <h2>总结</h2>
      <div class="ee" v-html="this.summaryForm.summary"></div>
    </div>
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
  width: 800px;
}
.cc {
 margin: 40px 0;
}
.dd {
  margin: 30px 0;
}
.ee {
  margin: 20px 0;
}

</style>
