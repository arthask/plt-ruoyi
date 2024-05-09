<script>
import {addUserWord} from "@/api/system/userword";
import {addRecord} from "@/api/system/record";
import {getOneWord} from "@/api/system/word";
import {getTotalAndNotStudyNum} from "@/api/statistics/statistics";

export default {
  name: "review",
  data() {
    return {
      UK: 'en-GB',
      US: 'en-US',
      showDetail: false,
      detail: {
        name: '',
        translation: ''
      },
      tableData: [{
        date: '2016-05-02',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1518 弄'
      }, {
        date: '2016-05-04',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1517 弄'
      }, {
        date: '2016-05-01',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1519 弄'
      }, {
        date: '2016-05-03',
        name: '王小虎',
        address: '上海市普陀区金沙江路 1516 弄'
      }],
      showWordInfo: true,
      currentInputTxt: '',
      wordIndex: 0,
      oneWordTxt: '',
      okTxt: '',
      notInputtedTxt: '',
      oneWord: {
        id: 0,
        word: "",
        phonetic: "",
        translation: "",
        pos: "",
        labelList: [],
      },
      currentLexiconUUID: "11cf6577-4d92-4cab-802a-c7c812ae3754"
    }
  },
  async mounted() {
    this.clearPanelData()
    this.speakCommon.speechInit()
    // 更新统计数据
    await getTotalAndNotStudyNum().then(res => {
      this.totalNum = res.data.total;
      this.remainNum = res.data.notStudy;
      this.starStudy()
    });
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
    async userInputs() {
      console.log("====================" + this.currentInputTxt)
      if ((this.currentInputTxt.length === this.oneWordTxt.length) &&
        (this.currentInputTxt[this.currentInputTxt.length - 1] === this.oneWordTxt[this.currentInputTxt.length - 1])) {
        this.clearPanelData();
        this.okTxt = this.oneWordTxt;
        this.record.wordId = this.oneWord.id;
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
        if (this.wordIndex >= this.remainNum) {
          this.$notify({
            type: "success",
            message: "请停下来，已经没有啦！"
          });
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
    },
    async nextWord() {
      this.clearPanelData()
      let currentIndex = this.wordIndex + 1;
      console.log("================currentIndex:" + currentIndex)
      if (currentIndex >= this.remainNum) {
        this.$notify({
          type: "success",
          message: "请停下来，已经没有啦！"
        });
        this.wordIndex = this.remainNum - 2;
        if (this.wordIndex < 0) {
          this.wordIndex = 0;
        }
        return;
      }
      await this.getOneWord(currentIndex);
      this.wordIndex++;
    },
    async getOneWord(index) {
      if (this.remainNum === 0) {
        this.studyEnd();
        return;
      }
      this.clearPanelData();
      await getOneWord(this.currentLexiconUUID, index).then(response => {
        if (!response.data) {
          this.showWordInfo = false;
          return;
        }
        this.oneWord = response.data;
        this.oneWordTxt = this.oneWord.word;
        this.notInputtedTxt = this.oneWordTxt;
        if (this.showWordInfo) {
          this.okTxt = '';
          this.playOneWordAudio(this.oneWordTxt)
        }
      });
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
  }
}
</script>

<template>
  <el-container>
    <el-header>
    </el-header>
    <el-container>
      <el-aside width="350px">
        <el-table
          :data="tableData"
          style="width: 100%; height: 100%">
          <el-table-column
            prop="date"
            label="日期"
          >
          </el-table-column>
          <el-table-column
            prop="name"
            label="姓名"
            >
          </el-table-column>
          <el-table-column prop="address" label="地址">
          </el-table-column>
        </el-table>
      </el-aside>
      <el-container>
        <el-main>
          <el-carousel style="border-radius: 2px;"  height="500px" indicator-position="none" :autoplay="false" arrow="never">
            <el-carousel-item v-for="item in 4" :key="item" >
              <el-row type="flex" justify="end">
                <el-col :span="4">
                  <el-button type="primary" @click="nextWord()">下一个</el-button>
                </el-col>
              </el-row>
              <el-row type="flex" justify="center" >
                <el-col :span="20">
                  <el-descriptions title="单词信息" border v-show="showWordInfo" :column="2">
                  <el-descriptions-item label="单词">
                    <span class="ok-content">{{ okTxt }}</span>
                    <span class="error-content">{{ notInputtedTxt }}</span>
                  </el-descriptions-item>
                  <el-descriptions-item label="标签">
                    <el-tag size="small" v-for="item in oneWord.labelList"> {{ item }}</el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="美式发音">
                    <template slot="label">
                      美式发音
                    </template>
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-microphone"
                      @click="playAudio(US,oneWord.word)"
                    >播放
                    </el-button>
                  </el-descriptions-item>
                  <el-descriptions-item label="英式发音">
                    <template slot="label">
                      英式发音
                    </template>
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-microphone"
                      @click="playAudio(UK,oneWord.word)"
                    >播放
                    </el-button>
                  </el-descriptions-item>
                  <el-descriptions-item label="释义">{{ oneWord.translation }}</el-descriptions-item>
                  </el-descriptions>
                  <el-descriptions title="内容填写区" :colon="false" v-show="showWordInfo">
                    <el-descriptions-item>
                      <el-input
                        type="textarea"
                        :autosize="{ minRows: 4, maxRows: 6}"
                        placeholder="请输入内容"
                        v-model="currentInputTxt"
                        @input="userInputs"
                        />
                    </el-descriptions-item>
                  </el-descriptions>
                </el-col>
              </el-row>
            </el-carousel-item>
          </el-carousel>
        </el-main>
        <el-footer>
        </el-footer>
      </el-container>
    </el-container>
  </el-container>
</template>

<style scoped lang="scss">
.ok-content {
  color: #42b983;
  font-size: large;
}

.error-content {
  color: #E65D6E;
  font-size: large;
}
</style>
