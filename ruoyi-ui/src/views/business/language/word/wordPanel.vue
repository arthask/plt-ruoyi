<template>
  <div>
    <el-row justify="space-between" type="flex">
      <el-col :span="6">
        <el-button type="primary" @click="forward()">上一个</el-button>
      </el-col>
      <el-col :span="6">
        <el-button type="primary" @click="nextWord()">下一个</el-button>
      </el-col>
    </el-row>
    <el-row justify="center" type="flex">
      <el-col :span="6">
        <div>
          <el-statistic
            :precision="0"
            :value="totalNum"
            group-separator=","
            title="总数">
          </el-statistic>
        </div>
      </el-col>
    </el-row>
    <el-descriptions v-show="showWordInfo" border column="1" direction="vertical" title="单词信息">
      <el-descriptions-item label="单词">
        <span class="ok-content">{{ okTxt }}</span>
        <span class="error-content">{{ notInputtedTxt }}</span>
      </el-descriptions-item>
      <el-descriptions-item label="释义">{{ oneWord.translation }}</el-descriptions-item>
    </el-descriptions>
    <el-descriptions v-show="showWordInfo" border class="margin-top" column="2" title="发音">
      <el-descriptions-item label="美式发音">
        <template slot="label">
          美式发音
        </template>
        <el-button
          icon="el-icon-microphone"
          size="mini"
          type="text"
          @click="playAudio(US,oneWord.word)"
        >播放
        </el-button>
      </el-descriptions-item>
      <el-descriptions-item label="英式发音">
        <template slot="label">
          英式发音
        </template>
        <el-button
          icon="el-icon-microphone"
          size="mini"
          type="text"
          @click="playAudio(UK,oneWord.word)"
        >播放
        </el-button>
      </el-descriptions-item>
    </el-descriptions>
    <el-descriptions v-show="showWordInfo" :colon="false" class="margin-top" title="内容填写区">
      <el-descriptions-item>
        <el-input
          v-model="currentInputTxt"
          placeholder="请输入内容"
          resize="none"
          rows="2" type="textarea"
          @input="userInputs"/>
      </el-descriptions-item>
    </el-descriptions>
    <el-empty v-show="!showWordInfo" description="没有啦"></el-empty>
  </div>
</template>

<script>
import {getOneWord, getOneWordInCollection} from "@/api/bussiness/word";
import {addUserWord} from "@/api/bussiness/userword";
import {addRecord} from "@/api/bussiness/record";
import {getCollectionTotalAndNotStudyNum, getTotalAndNotStudyNum} from "@/api/statistics/statistics";

export default {
  props: {
    lexiconuuid: {
      type: String,
      default: "",
    },
    wordCollectionId: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
      UK: 'en-GB',
      US: 'en-US',
      title: '',
      oneWord: {
        word: "",
        uuid: "",
        phonetic: "",
        translation: "",
        pos: "",
        labelList: [],
      },
      currentInputTxt: '',
      oneWordTxt: '',
      okTxt: '',
      notInputtedTxt: '',
      enterCount: 0,
      // 学习记录
      record: {
        word: '',
        wordUuid: ''
      },
      totalNum: 0,
      remainNum: 0,
      showWordInfo: true,
      wordIndex: 0,
      currentLexiconUUID: ""
    }
  },
  async mounted() {
    this.clearPanelData()
    // 更新统计数据
    if (this.wordCollectionId) {
      let params = {
        wordCollectionId: this.wordCollectionId
      }
      await getCollectionTotalAndNotStudyNum(params).then(res => {
        this.totalNum = res.data.total;
        this.remainNum = res.data.notStudy;
        this.starStudy()
      });
    } else {
      await getTotalAndNotStudyNum().then(res => {
        this.totalNum = res.data.total;
        this.remainNum = res.data.notStudy;
        this.starStudy()
      });
    }
  },
  methods: {
    studyEnd: function () {
      this.showWordInfo = false;
      this.$notify({
        type: "warning",
        message: "新单词已学完，赶紧去复习一下吧^-^"
      });
    },
    async starStudy() {
      await this.getOneWord(this.wordIndex);
    },
    async forward() {
      this.clearPanelData()
      let currentIndex = this.wordIndex - 1;
      console.log("================currentIndex:" + currentIndex)
      if (currentIndex < 0) {
        this.$notify({
          type: "success",
          message: "请停下来，已经没有啦！"
        });
        this.wordIndex = -1;
        return;
      }
      this.showWordInfo = true;
      await this.getOneWord(currentIndex);
      this.wordIndex--;
    },
    async nextWord() {
      this.clearPanelData()
      let currentIndex = this.wordIndex + 1;
      console.log("================currentIndex:" + currentIndex)
      if (currentIndex >= this.totalNum) {
        this.$notify({
          type: "success",
          message: "请停下来，已经没有啦！"
        });
        this.wordIndex = this.totalNum - 1;
        if (this.wordIndex < 0) {
          this.wordIndex = 0;
        }
        return;
      }
      await this.getOneWord(currentIndex);
      this.wordIndex++;
    },
    async getOneWord(index) {
      if (this.wordIndex >= this.totalNum) {
        this.studyEnd();
        return;
      }
      this.clearPanelData();
      if (this.wordCollectionId) {
        await getOneWordInCollection(this.wordCollectionId, index).then(response => {
          if (!response.data) {
            this.showWordInfo = false;
            return;
          }
          this.oneWord = response.data;
          console.log(this.oneWord)
          this.oneWordTxt = this.oneWord.word;
          this.notInputtedTxt = this.oneWordTxt;
          if (this.showWordInfo) {
            this.okTxt = '';
            this.playOneWordAudio(this.oneWordTxt)
          }
        });
      } else {
        await getOneWord(this.currentLexiconUUID, index).then(response => {
          if (!response.data) {
            this.showWordInfo = false;
            return;
          }
          this.oneWord = response.data;
          console.log(this.oneWord)
          this.oneWordTxt = this.oneWord.word;
          this.notInputtedTxt = this.oneWordTxt;
          if (this.showWordInfo) {
            this.okTxt = '';
            this.playOneWordAudio(this.oneWordTxt)
          }
        });
      }

    },
    // 播放音频
    playAudio(language, word) {
      this.speakCommon.changeVoice(language)
      this.speakCommon.speak(word)
    },
    playOneWordAudio(word) {
      console.log("====================" + word)
      this.speakCommon.speak(word)
      // this.audio.src = "http://dict.youdao.com/dictvoice?type=0&audio=" + this.oneWordTxt;
      // this.audio.play();
    },
    clearPanelData: function () {
      this.enterCount = 0;
      this.currentInputTxt = '';
      this.notInputtedTxt = '';
    },
    async userInputs() {
      console.log("====================" + this.currentInputTxt)
      if ((this.currentInputTxt.length === this.oneWordTxt.length) &&
        (this.currentInputTxt[this.currentInputTxt.length - 1] === this.oneWordTxt[this.currentInputTxt.length - 1])) {
        this.clearPanelData();
        this.okTxt = this.oneWordTxt;
        this.record.wordUuid = this.oneWord.uuid;
        this.record.word = this.oneWord.word;
        // 我的单词+1
        await addUserWord(this.record).then(() => {
          this.$notify({
            type: "success",
            message: "单词量+1"
          });
        });
        // 新增学习记录
        await addRecord(this.record).then(() => {
          this.$notify({
            type: "success",
            message: "学习记录+1"
          });
        });
        this.wordIndex++;
        if (this.wordIndex >= this.totalNum) {
          this.studyEnd();
          return;
        }
        await this.getOneWord(this.wordIndex);
        return;
      }
      for (let i = 0; i < this.currentInputTxt.length; i++) {
        if (this.currentInputTxt[i] === this.oneWordTxt[i]) {
          this.enterCount = i + 1;
          this.okTxt = this.oneWordTxt.slice(0, this.enterCount);
          this.notInputtedTxt = this.oneWordTxt.slice(this.enterCount, this.oneWordTxt.length);
        } else {
          this.currentInputTxt = this.okTxt;
          break;
        }
      }
    }
  }
}
</script>
<style>
.margin-top {
  margin-top: 20px;
}
.ok-content {
  color: #42b983;
  font-size: large;
}

.error-content {
  color: #E65D6E;
  font-size: large;
}
</style>
