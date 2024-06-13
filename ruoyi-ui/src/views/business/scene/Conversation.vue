<template>
  <div>
    <div v-for="(item, index) in contentList"
         :key="item.uuid"
         style="margin-top: 10px">
      <el-row :gutter="20" type="flex" justify="space-around">
        <el-col :span="10">
          <div class="chat-bubble-left" @click="playAudio(item.senderContent)">{{ item.senderContent }}
          </div>
        </el-col>
        <el-col :span="4">
        </el-col>
        <el-col :span="10">
        </el-col>
      </el-row>
      <el-row :gutter="20" type="flex" justify="space-around" v-if="item.reply">
        <el-col :span="10">
        </el-col>
        <el-col :span="4">
        </el-col>
        <el-col :span="10">
          <div class="chat-bubble-right" @click="playAudio(item.reply)">{{ item.reply }}
          </div>
        </el-col>
      </el-row>
    </div>
    <el-row style="margin-top: 20px" :gutter="20" type="flex" justify="end">
      <el-col :span="10"></el-col>
      <el-col :span="4">
        <el-button type="primary" @click="autoPlay">自动播放</el-button>
      </el-col>
    </el-row>
  </div>
</template>
<script>

export default {
  props: {
    diagContentList: Array
  },
  watch: {
    diagContentList(newValue) {
      if (newValue) {
        this.contentList = newValue
      }
    }
  },
  created() {

  },
  components: {},
  computed: {
    getAllContent() {
      let allContent = "";
      for (let i = 0; i < this.contentList.length; i++) {
          allContent += this.contentList[i].senderContent
          allContent += this.contentList[i].reply
      }
      return allContent;
    }
  },
  data() {
    return {
      contentList: []
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
  transform: scale(1.1); /* 放大效果 */
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
</style>
