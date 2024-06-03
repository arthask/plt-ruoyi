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
        <el-select v-model="value1" filterable  placeholder="请选择闪卡集" style="margin:20px;padding: 0 17px;">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
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

export default {
  components: {WordCard},
  data() {
    return {
      options: [{
        value: '选项1',
        label: '黄金糕'
      }, {
        value: '选项2',
        label: '双皮奶'
      }, {
        value: '选项3',
        label: '蚵仔煎'
      }, {
        value: '选项4',
        label: '龙须面'
      }, {
        value: '选项5',
        label: '北京烤鸭'
      }],
      value1: [],
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
