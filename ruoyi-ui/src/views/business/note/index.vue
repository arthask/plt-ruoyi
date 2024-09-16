<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="笔记标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入笔记标题"
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
          @click="addNormalNote"
        >新增自定义笔记
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          @click="addQuestionNote"
        >新增问题笔记
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
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="noteList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="笔记标题" align="center" prop="title" />
      <el-table-column label="创建时间" align="center" prop="createTime" />
      <el-table-column label="修改时间" align="center" prop="updateTime" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="showNoteInfo(scope.row)"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="editNote(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
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

    <!-- 添加或修改笔记对话框 -->
    <el-dialog v-if="open" :before-close="closeQuestionDialog" :destroy-on-close="true" :title="title"
               :visible.sync="open" width="1000px">
      <question-note @closeDialog="closeQuestionDialog"/>
    </el-dialog>

    <el-dialog v-if="showNormalNote" :before-close="closeNormalNoteDialog" :destroy-on-close="true" :title="title"
               :visible.sync="showNormalNote" width="1000px">
      <normal-note @closeDialog="closeNormalNoteDialog"/>
    </el-dialog>

    <el-dialog v-if="editOpen" :destroy-on-close="true" :title="editTitle" :visible.sync="editOpen" width="800px">
      <edit-note :note-uuid="noteUUId" @closeDialog="closeQuestionDialog"/>
    </el-dialog>

    <el-dialog
      v-if="noteInfoOpen"
      :visible.sync="noteInfoOpen"
      :destroy-on-close="true">
      <note-info :note-uuid="noteUUId"/>
    </el-dialog>
  </div>
</template>

<script>
import {delNote, listNote} from "@/api/bussiness/note";
import NoteComponent from "@/views/business/note/QuestionNote.vue";
import QuestionNote from "@/views/business/note/QuestionNote.vue";
import EditNote from "@/views/business/note/EditNote.vue";
import NoteInfo from "@/views/business/note/NoteInfo.vue";
import NormalNote from "@/views/business/note/NormalNote.vue";


export default {
  name: "Note",
  components: {
    NormalNote,
    QuestionNote,
    NoteComponent,
    EditNote,
    NoteInfo
  },
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
      // 笔记表格数据
      noteList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        summary: null,
        userId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: "笔记标题不能为空", trigger: "blur" }
        ],
        userId: [
          { required: true, message: "$comment不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ]
      },
      editOpen: false,
      editTitle: "",
      noteUUId: null,
      noteInfoOpen: false,
      showNormalNote: false
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询笔记列表 */
    getList() {
      this.loading = true;
      listNote(this.queryParams).then(response => {
        this.noteList = response.rows;
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
        title: null,
        summary: null,
        userId: null,
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
      this.ids = selection.map(item => item.uuid)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.uuid || this.ids;
      this.$modal.confirm('是否确认删除笔记编号为"' + ids + '"的数据项？').then(function() {
        return delNote(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    addQuestionNote() {
      this.reset();
      this.open = true;
      this.title = "添加问题笔记";
    },
    closeQuestionDialog() {
      this.open = false;
      this.editOpen = false;
      this.getList();
    },
    addNormalNote() {
      this.showNormalNote = true;
      this.title = "添加问题笔记";
    },
    closeNormalNoteDialog() {
      this.showNormalNote = false;
      this.getList();
    },
    editNote(row) {
      this.editOpen = true;
      this.editTitle = "修改问题笔记"
      this.noteUUId = row.uuid
    },
    showNoteInfo(row) {
      this.noteInfoOpen = true
      this.noteUUId = row.uuid
    }
  },
};
</script>
