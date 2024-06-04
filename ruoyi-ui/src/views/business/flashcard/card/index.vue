<template>
  <div style="margin-top:10px">
    <el-row>
      <el-col :span="18">
        <word-card
          :headerFront="headerFront"
          :footerFront="footerFront"
          :colorFront="colorFront"
          :colorTextFront="colorTextFront"
          :front="question"
          :back="answer">
        </word-card>
      </el-col>
      <el-col :span="6">
        <el-select v-model="packageList" filterable placeholder="请选择闪卡集" style="margin:20px;padding: 0 17px;">
          <el-option
            v-for="item in options"
            :key="item.uuid"
            :label="item.name"
            :value="item.uuid">
          </el-option>
        </el-select>
        <div v-for="item in tag" :key="item.index" class="tag">
          {{ item.name }}
          <p>{{ item.index }}</p>
        </div>
      </el-col>
    </el-row>
    <el-row :gutter="20" type="flex">
      <el-col :span="5">
      </el-col>
      <el-col :span="3">
        <el-button type="primary" plain>不会</el-button>
      </el-col>
      <el-col :span="3">
        <el-button type="success" plain>跳过</el-button>
      </el-col>
      <el-col :span="3">
        <el-button type="info" plain>会</el-button>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import WordCard from './WordCard.vue'
import {getPackageList, listPackages} from "@/api/bussiness/flashcardpackage";

export default {
  components: {WordCard},
  data() {
    return {
      options: [],
      packageList: [],
      form: {
        flashcardId: [],
      },
      tag: [{name: '待定', index: 1}, {name: '会', index: 2}, {name: '不会', index: 3}],
      colorFront: '#E6A23C',
      colorTextFront: 'white',
      question: 'hello this is a flashcard',
      answer: 'with animation',
      footerFront: "",
      headerFront: "",

    }
  },
  created() {
    getPackageList().then(res => {
      this.options = res.data;
      console.log(this.options)
    })
  },
  methods: {
    load() {
      this.count += 2
    }
  }
}
</script>
<style scoped>
::v-deep .flashcard {
  height: 430px;
  //margin: 10px;
  //padding: 0px;
}

.tag {
  border-radius: 10px;
  margin: 20px;
  padding: 17px;
  background: #F56C6C;
  width: 80%;
  height: 100px;
  cursor: pointer;
  text-align: center;
  color: white;
}
</style>
