<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="单词id" prop="wordId">
        <el-input
          v-model="queryParams.wordId"
          placeholder="请输入单词id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单词" prop="word">
        <el-input
          v-model="queryParams.word"
          placeholder="请输入单词"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户名称" prop="userName">
        <el-input
          v-model="queryParams.userName"
          placeholder="请输入用户名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="阶段" prop="period">
        <el-input
          v-model="queryParams.period"
          placeholder="请输入阶段"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否收藏" prop="collectFlag">
        <el-input
          v-model="queryParams.collectFlag"
          placeholder="请输入是否收藏"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:word:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:word:export']"
        >导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          @click="beginWordStudy"
          v-hasPermi="['system:word:edit']"
        >我要复习
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="编号" align="center" prop="id"/>
      <el-table-column label="单词id" align="center" prop="wordId"/>
      <el-table-column label="单词" align="center" prop="word"/>
      <el-table-column label="用户id" align="center" prop="userId"/>
      <el-table-column label="用户名称" align="center" prop="userName"/>
      <el-table-column label="阶段" align="center" prop="period"/>
      <el-table-column label="是否收藏" align="center" prop="collectFlag"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:word:edit']"
          >学习详情
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:word:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改用户单词对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="单词id" prop="wordId">
          <el-input v-model="form.wordId" placeholder="请输入单词id"/>
        </el-form-item>
        <el-form-item label="单词" prop="word">
          <el-input v-model="form.word" placeholder="请输入单词"/>
        </el-form-item>
        <el-form-item label="用户id" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户id"/>
        </el-form-item>
        <el-form-item label="用户名称" prop="userName">
          <el-input v-model="form.userName" placeholder="请输入用户名称"/>
        </el-form-item>
        <el-form-item label="阶段，拥有标识单词的单词的熟悉程度" prop="period">
          <el-input v-model="form.period" placeholder="请输入阶段，拥有标识单词的单词的熟悉程度"/>
        </el-form-item>
        <el-form-item label="是否收藏" prop="collectFlag">
          <el-input v-model="form.collectFlag" placeholder="请输入是否收藏"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="studyTitle" :visible.sync="studyOpen" :destroy-on-close="true" @open="starStudy()"
               @close="closeStudyPanel()">
      <el-container>
        <el-header>
          <el-row type="flex" justify="space-between">
            <el-col :span="6">
              <el-button type="primary" @click="forward()">上一个</el-button>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" @click="nextWord()">下一个</el-button>
            </el-col>
          </el-row>
          <el-row type="flex" class="row-bg" justify="center">
            <el-col :span="6">
              <div>
                <el-statistic
                  group-separator=","
                  :precision="0"
                  :value="totalReviewNum"
                  title="需复习总数量">
                </el-statistic>
              </div>
            </el-col>
            <el-col :span="6">
              <div>
                <el-statistic title="今日已复习数量">
                  <template slot="formatter">
                    {{ this.haveReview }}
                  </template>
                </el-statistic>
              </div>
            </el-col>
          </el-row>
        </el-header>
        <el-main>
          <el-empty description="没有啦" v-show="!showWordInfo"></el-empty>
          <el-descriptions title="单词信息" border v-show="showWordInfo">
            <el-descriptions-item label="单词">
              <span class="ok-content">{{ okTxt }}</span>
              <span class="error-content">{{ notInputtedTxt }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="词性">{{ oneWord.pos }}</el-descriptions-item>
            <el-descriptions-item label="发音">{{ oneWord.phonetic }}</el-descriptions-item>
            <el-descriptions-item label="释义">{{ oneWord.translation }}</el-descriptions-item>
            <el-descriptions-item label="分类">
              <el-tag size="small"> {{ oneWord.tag }}</el-tag>
            </el-descriptions-item>
          </el-descriptions>
          <el-descriptions title="内容填写区" :colon="false" v-show="showWordInfo">
            <el-descriptions-item>
              <el-input
                type="textarea"
                :autosize="{ minRows: 4, maxRows: 6}"
                placeholder="请输入内容"
                v-model="currentInputTxt"
                @input="userInputs"/>
            </el-descriptions-item>
          </el-descriptions>
        </el-main>
      </el-container>
    </el-dialog>

  </div>
</template>

<script>
import {listWord, getWord, delWord, addUserWord, updateWord} from "@/api/system/userword";
import {getReviewWordByIndex} from "@/api/review/review";
import {addRecord} from "@/api/system/record";
import {getNeedReviewAnHaveReviewNum} from "@/api/statistics/statistics";



export default {
  name: "Word",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户单词表格数据
      wordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        wordId: null,
        word: null,
        userId: null,
        userName: null,
        period: null,
        collectFlag: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        wordId: [
          {required: true, message: "单词id不能为空", trigger: "blur"}
        ],
        word: [
          {required: true, message: "单词不能为空", trigger: "blur"}
        ],
        userId: [
          {required: true, message: "用户id不能为空", trigger: "blur"}
        ],
        userName: [
          {required: true, message: "用户名称不能为空", trigger: "blur"}
        ],
        period: [
          {required: true, message: "阶段，拥有标识单词的单词的熟悉程度不能为空", trigger: "blur"}
        ],
        collectFlag: [
          {required: true, message: "是否收藏不能为空", trigger: "blur"}
        ],
        createTime: [
          {required: true, message: "创建时间不能为空", trigger: "blur"}
        ],
        updateTime: [
          {required: true, message: "更新时间时间不能为空", trigger: "blur"}
        ]
      },
      studyOpen: false,
      oneWord: {
        id: 0,
        word: "",
        phonetic: "",
        translation: "",
        pos: "",
        tag: "",
      },
      currentInputTxt: '',
      oneWordTxt: '',
      okTxt: '',
      notInputtedTxt: '',
      enterCount: 0,
      studyTitle: '',
      // 学习记录
      record: {
        wordId: '',
        word: ''
      },
      needReview: 0,
      haveReview: 0,
      totalReviewNum: 0,
      showWordInfo: true,
      wordIndex: 0,
      conversationTitle:'对话',
      showConversation: false
    };
  },
  created() {
    this.getList();
    getNeedReviewAnHaveReviewNum().then(res => {
      this.needReview = res.data.needReview;
      this.haveReview = res.data.haveReview;
      this.totalReviewNum = res.data.totalReviewNum;
    });
    this.speakCommon.speechInit();
  },
  methods: {
    /** 查询用户单词列表 */
    getList() {
      this.loading = true;
      listWord(this.queryParams).then(response => {
        this.wordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        wordId: null,
        word: null,
        userId: null,
        userName: null,
        period: null,
        collectFlag: null,
        createTime: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户单词";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getWord(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户单词";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUserWord(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除用户单词编号为"' + ids + '"的数据项？').then(function () {
        return delWord(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/word/export', {
        ...this.queryParams
      }, `word_${new Date().getTime()}.xlsx`)
    },
    // 开始学习
    async beginWordStudy() {
      await getNeedReviewAnHaveReviewNum().then(res => {
        this.needReview = res.data.needReview;
        this.haveReview = res.data.haveReview;
        this.totalReviewNum = res.data.totalReviewNum;
      });
      // 更新统计数据
      this.studyOpen = true;
    },
    async starStudy() {
      await this.getReviewWord(this.wordIndex);
    },
    async forward() {
      let currentIndex = this.wordIndex - 1;
      console.log("================currentIndex:" + currentIndex)
      if (currentIndex < 0) {
        this.$notify({
          type: "success",
          message: "请停下来，已经没有啦！"
        });
        this.wordIndex = 0;
        this.studyEnd();
        return;
      }
      this.showWordInfo = true;
      await this.getReviewWord(currentIndex);
      this.wordIndex--;
    },
    async nextWord() {
      let currentIndex = this.wordIndex + 1;
      if (currentIndex >= this.totalReviewNum) {
        this.$notify({
          type: "success",
          message: "请停下来，已经没有啦！"
        });
        this.wordIndex = this.totalReviewNum;
        if (this.wordIndex < 0) {
          this.wordIndex = 0;
        }
        this.studyEnd();
        return;
      }
      await this.getReviewWord(currentIndex);
      this.wordIndex++;
    },
    async getReviewWord(index) {
      console.log("================index:" + index + " wordIndex:" + this.wordIndex)
      this.clearPanelData();
      await getReviewWordByIndex(index).then(response => {
        if (!response.data) {
          this.showWordInfo = false;
          return;
        }
        this.oneWord = response.data;
        this.oneWordTxt = this.oneWord.word;
        this.notInputtedTxt = this.oneWordTxt;
      });
      if (this.showWordInfo) {
        this.okTxt = '';
        this.playOneWordAudio()
      }
    },
    playOneWordAudio() {
      console.log("====================" + this.oneWordTxt)
      this.speakCommon.speak(this.oneWordTxt)
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
        this.record.wordId = this.oneWord.id;
        this.record.word = this.oneWord.word;
        // 我的单词+1
        await addUserWord(this.record).then(() => {
          this.$notify({
            type: "success",
            message: "熟悉度+1"
          });
        });
        // 新增学习记录
        await addRecord(this.record).then(() => {
          this.$notify({
            type: "success",
            message: "学习记录+1"
          });
        });
        // 更新统计数据
        await getNeedReviewAnHaveReviewNum().then(res => {
          this.haveReview = res.data.haveReview;
        });

        this.wordIndex++;
        if (this.wordIndex >= this.totalReviewNum) {
          this.$notify({
            type: "success",
            message: "请停下来，已经没有啦！"
          });
          this.studyEnd();
          return;
        }
        await this.getReviewWord(this.wordIndex);
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
    closeStudyPanel() {
      this.clearPanelData();
    },
    studyEnd() {
      this.showWordInfo = false;
      this.$notify({
        type: "warning",
        message: "今日已完成复习，好好休息一下吧^-^"
      });
    }
  }
};
</script>
<style>
.ok-content {
  color: #42b983;
  font-size: large;
}

.error-content {
  color: #E65D6E;
  font-size: large;
}
</style>
