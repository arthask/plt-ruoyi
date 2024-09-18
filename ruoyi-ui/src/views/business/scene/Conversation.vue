<template>
  <div style="padding: 0 20px">
    <div class="ck-content">
      <h3 class="bold-text">背景介绍</h3>
      <div v-html="this.sceneData.introduce"></div>
      <el-divider></el-divider>
      <h3 class="bold-text">学习内容</h3>
      <div v-html="this.sceneData.studyInfo"></div>
      <el-divider></el-divider>
      <h3 class="bold-text">总结</h3>
      <div v-html="this.sceneData.summary"></div>
    </div>
    <el-divider></el-divider>
    <h3 class="title bold-text">对话内容</h3>
    <el-row :gutter="20" type="flex" justify="end">
      <el-col :span="10"></el-col>
      <el-col :span="4">
        <el-button type="primary" @click="autoPlay">自动播放</el-button>
      </el-col>
    </el-row>
    <div v-for="(item, index) in sceneData.dialogueDataList"
         :key="item.uuid"
         style="margin-top: 10px">
      <el-row :gutter="20" type="flex" justify="space-around">
        <el-col :span="11">
          <div class="chat-bubble-left" @click="playAudio(item.senderContent)">问：{{ item.senderContent }}
          </div>
        </el-col>
        <el-col :span="2">
        </el-col>
        <el-col :span="11">
        </el-col>
      </el-row>
      <el-row :gutter="20" type="flex" justify="space-around" v-if="item.reply">
        <el-col :span="11">
        </el-col>
        <el-col :span="2">
        </el-col>
        <el-col :span="11">
          <div class="chat-bubble-right" @click="playAudio(item.reply)">答：{{ item.reply }}
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>

export default {
  props: {
    sceneData: Object
  },
  watch: {
    sceneData(newValue) {
      if (newValue) {
        this.sceneData = newValue
      }
    }
  },
  created() {

  },
  components: {},
  computed: {
    getAllContent() {
      let allContent = "";
      for (let i = 0; i < this.sceneData.dialogueDataList.length; i++) {
        allContent += this.sceneData.dialogueDataList[i].senderContent
        allContent += this.sceneData.dialogueDataList[i].reply
      }
      return allContent;
    }
  },
  data() {
    return {
    }
  },
  methods: {
    // 播放音频
    playAudio(word) {
      this.speakCommon.speak(word)
    },
    autoPlay() {
      this.speakCommon.speak(this.getAllContent)
    }
  }
}
</script>
<style>
/* 气泡会话样式 */
.chat-bubble-left {
  display: flex; /* 使用 Flex 布局 */
  align-items: center; /* 垂直居中 */
  background-color: rgba(0, 123, 255, 0.89);
  color: #fff;
  border-radius: 10px;
  padding: 10px 15px;
  min-height: 66px;
  margin-bottom: 10px;
  font-size: 18px;
}

.chat-bubble-left:hover {
  cursor: pointer; /* 鼠标样式变为手型 */
  transform: scale(1.05); /* 放大效果 */
}

.chat-bubble-right {
  display: flex; /* 使用 Flex 布局 */
  align-items: center; /* 垂直居中 */
  background-color: #64fa00;
  color: #fff;
  border-radius: 10px;
  padding: 10px 15px;
  min-height: 66px;
  margin-bottom: 10px;
  font-size: 18px;
}

.chat-bubble-right:hover {
  cursor: pointer; /* 鼠标样式变为手型 */
  transform: scale(1.1); /* 放大效果 */
}

.title {
  text-align: center;
  padding-bottom: 10px
}

.bold-text {
  font-weight: bold;
}
</style>
