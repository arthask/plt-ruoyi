<script>
import {listRecord} from "@/api/bussiness/record";
import {getWordInfo} from "@/api/bussiness/word";
import WordPanel from "../word/wordPanel.vue";

export default {
  name: "review",
  components: {WordPanel},
  data() {
    return {
      showDetail: false,
      detail: {
        name: '',
        translation: '',
        labelNames: []
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
        this.showWordDetail = true;
        this.detail.name = res.data.word
        this.detail.translation = res.data.translation
        this.detail.labelNames = [...res.data.labelList]
      })
    }
  }
}
</script>

<template>
  <div>
    <el-container>
      <el-container style="border-style:solid;border-width:5px; border-color: #97a8be; margin-left: 20px">
        <!--        <el-header></el-header>-->
        <el-main>
          <word-panel :review="true"></word-panel>
        </el-main>
        <!--        <el-footer></el-footer>-->
      </el-container>
      <el-aside style="background-color:white" width="35%">
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
