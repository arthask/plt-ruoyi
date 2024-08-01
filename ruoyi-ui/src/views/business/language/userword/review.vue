<script>
import {addUserWord} from "@/api/bussiness/userword";
import {addRecord, listRecord} from "@/api/bussiness/record";
import {getWordInfo} from "@/api/bussiness/word";
import {getTotalAndNotStudyNum} from "@/api/statistics/statistics";
import {getReviewWord} from "@/api/review/review";

export default {
  name: "review",
  data() {
    return {
      UK: 'en-GB',
      US: 'en-US',
      showDetail: false,
      detail: {
        name: '',
        translation: '',
        labelNames:[]
      },
      showWordInfo: true,
      currentInputTxt: '',
      wordIndex: 0,
      oneWordTxt: '',
      okTxt: '',
      notInputtedTxt: '',
      oneWord: {
        word: "",
        uuid: "",
        phonetic: "",
        translation: "",
        pos: "",
        labelList: [],
      },
      // 学习记录
      record: {
        wordUuid: '',
        word: ''
      },
      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      // 用户学习记录表格数据
      recordList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      showWordDetail: false,
    }
  },
  async mounted() {
    this.clearPanelData()
    // this.speakCommon.speechInit()
    // 更新统计数据
    await getTotalAndNotStudyNum().then(res => {
      this.totalNum = res.data.total;
      this.remainNum = res.data.notStudy;
      this.starStudy()
    });
    await this.getStudyRecord();
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
      await this.getReviewWord(this.wordIndex);
    },
    async userInputs() {
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
        if (this.wordIndex >= this.remainNum) {
          this.$notify({
            type: "success",
            message: "请停下来，已经没有啦！"
          });
          this.studyEnd();
          return;
        }
        await this.getReviewWord(this.wordIndex);
        await this.getStudyRecord();
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
    async getStudyRecord() {
      this.loading = true;
      await listRecord(this.queryParams).then(response => {
        this.recordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    async getWordDetail(row) {
      await getWordInfo({wordUuid: row.wordUuid}).then(res => {
        this.showWordDetail = true;
        this.detail.name = res.data.word
        this.detail.translation = res.data.translation
        this.detail.labelNames = [... res.data.labelList]
      })
    },
    async getReviewWord(index) {
      console.log("================index:" + index + " wordIndex:" + this.wordIndex)
      this.clearPanelData();
      await getReviewWord().then(response => {
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
  }
}
</script>

<template>
    <el-container>
      <el-aside width="34%" style="background-color: white">
        <el-table v-loading="loading" :data="recordList" :stripe="true">
          <el-table-column label="单词" min-width="120px" align="center" prop="word"/>
          <el-table-column label="学习日期" min-width="150px" align="center" prop="studyTime"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-search"
                @click="getWordDetail(scope.row)"
                v-hasPermi="['system:word:edit']"
              >查看
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          small
          layout="prev, pager, next"
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getStudyRecord"
        />
      </el-aside>
      <el-container>
        <el-header></el-header>
        <el-main>
          <el-carousel style="border-radius: 2px;" height="500px" indicator-position="none" :autoplay="false"
                       arrow="never">
            <el-carousel-item>
              <el-row type="flex" justify="center">
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
        <el-footer></el-footer>
      </el-container>
      <el-dialog title="" :visible.sync="showWordDetail">
        <el-descriptions title="单词信息" border :column="2">
          <el-descriptions-item label="单词">
            {{ detail.name }}
          </el-descriptions-item>
          <el-descriptions-item label="标签">
            <el-tag size="small" v-for="item in detail.labelNames"> {{ item }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="美式发音">
            <template slot="label">
              美式发音
            </template>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-microphone"
              @click="playAudio(US,detail.name)"
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
              @click="playAudio(UK,detail.name)"
            >播放
            </el-button>
          </el-descriptions-item>
          <el-descriptions-item label="释义">{{ detail.translation }}</el-descriptions-item>
        </el-descriptions>
      </el-dialog>
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
