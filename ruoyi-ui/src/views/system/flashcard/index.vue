<template>
  <div style="margin-top:10px">
    <el-row >
      <el-col :offset="1">
        <el-form ref="form" :model="form" label-width="100px">
        <el-form-item label="选择闪卡集">
        <el-select v-model="value1" multiple placeholder="请选择">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
        </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="18">
        <vue-flashcard
          :colorFront="colorFront"
          :colorTextFront="colorTextFront"
          :front="question"
          :back="answer">
        </vue-flashcard>
      </el-col>
      <el-col :span="6">
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
import vueFlashcard from 'vue-flashcard'

export default {
  components: { vueFlashcard },
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
      tag: [{name:'aa',index:1},{name:'bb',index:2},{name:'cc',index:3}],
      colorFront: '#409EFF',
      colorTextFront: 'white',
      question: 'hello this is a flashcard',
      answer: 'with animation',
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
  height: 390px;
  //margin: 10px;
  //padding: 0px;
}

.tag {
  border-radius: 10px;
  margin: 20px;
  padding: 17px;
  background: #409EFF;
  width: 200px;
  height: 100px;
  cursor: pointer;
  text-align: center;
  color: white;
}
</style>
