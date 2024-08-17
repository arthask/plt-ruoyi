<script>
import {listRecord} from "@/api/bussiness/record";
import {getWordInfo} from "@/api/bussiness/word";
import WordPanel from "../word/wordPanel.vue";

export default {
  name: "review",
  components: {WordPanel},
  data() {
    return {
      UK: 'en-GB',
      US: 'en-US',
      showDetail: false,
      detail: {
        name: '',
        translation: '',
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
      }
    }
  },
  async mounted() {
    // 更新统计数据
    await this.getStudyRecord();
  },
  methods: {
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
        this.showDetail = true;
        this.detail.name = res.data.word
        this.detail.translation = res.data.translation
      })
    },
    // 播放音频
    playAudio(language, word) {
      this.speakCommon.changeVoice(language)
      this.speakCommon.speak(word)
    },
  }
}
</script>

<template>
  <div>
    <el-container>
      <el-container style="border-style:solid;border-width:1px;
       border-color: lightgrey; margin: 10px 0 30px 50px">
        <!--        <el-header></el-header>-->
        <el-main>
          <word-panel :review="true"></word-panel>
        </el-main>
        <!--        <el-footer></el-footer>-->
      </el-container>
      <el-aside style="background-color:white;width:35%">
        <el-table v-loading="loading" :data="recordList" :stripe="true">
          <el-table-column align="center" label="单词" min-width="120px" prop="word"/>
          <el-table-column align="center" label="学习日期" min-width="150px" prop="studyTime"/>
          <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
            <template slot-scope="scope">
              <el-button
                v-hasPermi="['system:word:edit']"
                icon="el-icon-search"
                size="mini"
                type="text"
                @click="getWordDetail(scope.row)"
              >查看
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="total>0"
          :limit.sync="queryParams.pageSize"
          :page.sync="queryParams.pageNum"
          :total="total"
          layout="prev, pager, next"
          small
          @pagination="getStudyRecord"
        />
      </el-aside>
    </el-container>
    <el-dialog :visible.sync="showDetail" title="">
      <el-descriptions :column="2" border title="单词信息">
        <el-descriptions-item label="单词">
          {{ detail.name }}
        </el-descriptions-item>
        <el-descriptions-item label="释义">{{ detail.translation }}</el-descriptions-item>
        <el-descriptions-item label="美式发音">
          <template slot="label">
            美式发音
          </template>
          <el-button
            icon="el-icon-microphone"
            size="mini"
            type="text"
            @click="playAudio(US,detail.name)"
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
            @click="playAudio(UK,detail.name)"
          >播放
          </el-button>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
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
