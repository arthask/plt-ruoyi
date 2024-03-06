<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="单词" prop="word">
        <el-input
          v-model="queryParams.word"
          placeholder="请输入单词"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="音标" prop="phonetic">
        <el-input
          v-model="queryParams.phonetic"
          placeholder="请输入音标"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="翻译" prop="translation">
        <el-input
          v-model="queryParams.translation"
          placeholder="请输入翻译"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="词性" prop="pos">
        <el-input
          v-model="queryParams.pos"
          placeholder="请输入词性"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="标签" prop="tag">
        <el-input
          v-model="queryParams.tag"
          placeholder="请输入标签"
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:word:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:word:edit']"
        >修改
        </el-button>
      </el-col>
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
          type="info"
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
        >导入
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-upload2"
          size="mini"
          @click="beginWordStudy"
        >开始学习
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="编号" align="center" prop="id"/>
      <el-table-column label="单词" align="center" prop="word"/>
      <el-table-column label="音标" align="center" prop="phonetic"/>
      <el-table-column label="翻译" align="center" prop="translation"/>
      <el-table-column label="词性" align="center" prop="pos"/>
      <el-table-column label="标签" align="center" prop="tag"/>
      <el-table-column label="例句" align="center" prop="sentence"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:word:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:word:remove']"
          >删除
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="playAudio(scope.row)"
            v-hasPermi="['system:word:edit']"
          >播放
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

    <!-- 添加或修改单词对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="单词" prop="word">
          <el-input v-model="form.word" placeholder="请输入单词"/>
        </el-form-item>
        <el-form-item label="音标" prop="phonetic">
          <el-input v-model="form.phonetic" placeholder="请输入音标"/>
        </el-form-item>
        <el-form-item label="翻译" prop="translation">
          <el-input v-model="form.translation" placeholder="请输入翻译"/>
        </el-form-item>
        <el-form-item label="词性" prop="pos">
          <el-input v-model="form.pos" placeholder="请输入词性"/>
        </el-form-item>
        <el-form-item label="标签" prop="tag">
          <el-input v-model="form.tag" placeholder="请输入标签"/>
        </el-form-item>
        <el-form-item label="例句" prop="sentence">
          <el-input v-model="form.sentence" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <audio></audio>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px">
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" slot="tip">
          <el-checkbox v-model="upload.updateSupport"/>
          是否更新已经存在的用户数据
          <el-link type="info" style="font-size:12px" @click="importTemplate">下载模板</el-link>
        </div>
        <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“xls”或“xlsx”格式文件！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
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
                  :value="totalNum"
                  title="总数">
                </el-statistic>
              </div>
            </el-col>
            <el-col :span="6">
              <div>
                <el-statistic title="剩余未学">
                  <template slot="formatter">
                    {{ this.remainNum }}
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

          <!--          <vue-typer class="vue-typer" :text="wordInput" :repeat="0" caret-animation="smooth" initial-action='erasing'></vue-typer>-->
        </el-main>
      </el-container>
    </el-dialog>
  </div>
</template>

<script>
import {listWord, getWord, delWord, addWord, updateWord, getOneWord} from "@/api/system/word";
import {importTemplate} from "@/api/system/word";
import {getToken} from "@/utils/auth";
import {addRecord} from "@/api/system/record";
import {addUserWord} from "@/api/system/userword";
import {getTotalAndNotStudyNum} from "@/api/statistics/statistics";

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
      // 单词表格数据
      wordList: [],
      // 学习弹出层标题
      studyTitle: '',
      title: '',
      // 是否显示弹出层
      open: false,
      studyOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        word: null,
        phonetic: null,
        translation: null,
        pos: null,
        tag: null,
        sentence: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: {Authorization: "Bearer " + getToken()},
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/word/importData"
      },
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
      // 学习记录
      record: {
        wordId: '',
        word: ''
      },
      totalNum: 0,
      remainNum: 0,
      showWordInfo: true,
      wordIndex: 0
    };
  },
  created() {
    this.getList();
    this.speakCommon.speechInit();
  },
  methods: {
    /** 查询单词列表 */
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
        word: null,
        phonetic: null,
        translation: null,
        pos: null,
        tag: null,
        sentence: null
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
      this.title = "添加单词";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getWord(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改单词";
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
            addWord(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除单词编号为"' + ids + '"的数据项？').then(function () {
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
    handleImport() {
      this.upload.title = "用户导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      importTemplate().then(response => {
        this.download(response.msg);
      });
    },
// 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
// 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert(response.msg, "导入结果", {dangerouslyUseHTMLString: true});
      this.getList();
    },
// 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    // 播放音频
    playAudio(row) {
      this.speakCommon.speak(row.word)
    },
    // 开始学习
    async beginWordStudy() {
      // 更新统计数据
      await getTotalAndNotStudyNum().then(res => {
        this.totalNum = res.data.total;
        this.remainNum = res.data.notStudy;
      });
      this.studyOpen = true;
    },
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
      let currentIndex = this.wordIndex - 1;
      console.log("================currentIndex:" + currentIndex)
      if (currentIndex < 0) {
        this.$notify({
          type: "success",
          message: "请停下来，已经没有啦！"
        });
        this.wordIndex = 0;
        return;
      }
      this.showWordInfo = true;
      await this.getOneWord(currentIndex);
      this.wordIndex--;
    },
    async nextWord() {
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
      await getOneWord(index).then(response => {
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
    closeStudyPanel() {
      this.clearPanelData();
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
