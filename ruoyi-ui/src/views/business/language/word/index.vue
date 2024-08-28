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
          type="primary"
          icon="el-icon-plus"
          size="mini"
          :disabled="multiple"
          @click="handle2Collection"
        >添加到单词集
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
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="单词" align="center" prop="word"/>
      <el-table-column align="center" label="释义" prop="translation"/>
      <!--      <el-table-column label="标签" align="center" prop="tag"/>-->
      <el-table-column label="创建时间" align="center" prop="createTime"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            @click="handleAddSentence(scope.row)"
          >添加例句
          </el-button>
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
            icon="el-icon-play"
            @click="playAudio(scope.row)"
            v-hasPermi="['system:word:edit']"
          >播放
          </el-button>
          <el-button
            size="mini"
            type="text"
            @click="viewWordCollection(scope.row)"
          >查看单词集
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
        <el-form-item label="释义" prop="translation">
          <el-input v-model="form.translation" placeholder="请输入释义"/>
        </el-form-item>
        <el-form-item label="音标" prop="phonetic">
          <el-input v-model="form.phonetic" placeholder="请输入音标"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog v-if="showSentence"
               :before-close="closeSentence"
               :center="true"
               :destroy-on-close="true"
               :visible.sync="showSentence"
               title="添加例句"
    >
      <el-row justify="start" type="flex">
        <el-button style="margin-top: 10px" type="primary" @click.prevent="addSentence">添加</el-button>
      </el-row>
      <el-row v-for="(item,index) in sentenceList" :key="index"
              :gutter="20">
        <el-col :span="10">
          <el-input v-model="item.sentenceContent" placeholder="请输入句子"
                    resize="none" rows="2"
                    style="margin-top: 10px" type="textarea"></el-input>
        </el-col>
        <el-col :span="10">
          <el-input v-model="item.translateContent" placeholder="请输入翻译"
                    resize="none" rows="2"
                    style="margin-top: 10px" type="textarea"></el-input>
        </el-col>
        <el-col :span="4">
          <el-button style="margin-top: 10px" type="danger" @click.prevent="removeSentence(index)">删除</el-button>
        </el-col>
      </el-row>
      <el-row :gutter="20" justify="end" style="margin-top: 20px" type="flex">
        <el-col :span="10"></el-col>
        <el-col :span="4">
          <el-button type="primary" @click="">提交</el-button>
        </el-col>
      </el-row>
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
    <el-dialog title="添加到单词集"
               v-if="showCollectionDialog"
               :visible.sync="showCollectionDialog"
               :center="true"
               :destroy-on-close="true"
               :before-close="closeCollection">
      <word2-collection
        :wordUUIDs="ids"
        @closeDialog="closeCollection">
      </word2-collection>
    </el-dialog>
    <el-dialog title="查看单词集"
               v-if="showCollectionView"
               :visible.sync="showCollectionView"
               :center="true"
               :destroy-on-close="true">
      <CollectionView :word-u-u-id="wordUUId"></CollectionView>
    </el-dialog>
  </div>
</template>

<script>
import {addWord, delWord, getWord, importTemplate, listWord, updateWord} from "@/api/bussiness/word";
import {getToken} from "@/utils/auth";
import WordPanel from "@/views/business/language/word/wordPanel.vue";
import Word2Collection from "@/views/business/language/word/word2Collection.vue";
import CollectionView from "./CollectionView.vue";

export default {
  name: "Word",
  components: {CollectionView, Word2Collection, WordPanel},
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
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        word: null,
        phonetic: null,
        translation: null
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
      showCollectionDialog: false,
      showCollectionView: false,
      wordUUId: '',
      showSentence: false,
      sentenceList: [{
        sentenceContent: "",
        translateContent: "",
      }]
    }
  },
  created() {
    this.getList();
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
        uuid: null,
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
      this.ids = selection.map(item => item.uuid)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /**
     * 添加例句
     */
    handleAddSentence() {
      this.showSentence = true
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
      const id = row.uuid || this.ids
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
          if (this.form.uuid != null) {
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
    playAudio(word) {
      this.speakCommon.speak(word.word)
    },
    handle2Collection() {
      if (this.ids.length === 0) {
        this.$modal.msgSuccess("请选择记录");
        return
      }
      this.showCollectionDialog = true
    },
    closeCollection() {
      this.showCollectionDialog = false
    },
    viewWordCollection(row) {
      this.showCollectionView = true
      this.wordUUId = row.uuid
    },
    addSentence() {
      this.sentenceList.push({
        sentenceContent: "",
        translateContent: "",
      })
    },
    removeSentence() {
      if (this.sentenceList.length === 1) {
        this.$modal.msgWarning("再删除就没有了呢");
        return
      }
      this.sentenceList.splice(index, 1)
    },
    closeSentence() {
      this.showSentence = false
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
