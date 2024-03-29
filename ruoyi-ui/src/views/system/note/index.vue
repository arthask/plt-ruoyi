<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户id"
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
          v-hasPermi="['system:note:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="editNote"
          v-hasPermi="['system:note:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:note:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:note:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="addMyNote"
          v-hasPermi="['system:note:add']"
        >新增笔记</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="noteList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="用户id" align="center" prop="userId" />
      <el-table-column label="笔记标题" align="center" prop="title" />
      <el-table-column label="总结" align="center" prop="summary" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-search"
            @click="showNoteInfo(scope.row)"
            v-hasPermi="['system:note:edit']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="editNote(scope.row)"
            v-hasPermi="['system:note:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:note:remove']"
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
    <el-dialog :title="title" :visible.sync="open" width="1000px" :destroy-on-close="true">
      <note-component @closeDialog = "closeMyDialog"/>
    </el-dialog>

    <el-dialog :title="editTitle" :visible.sync="editOpen" width="1000px" :destroy-on-close="true">
      <edit-note :note-id="noteId" @closeDialog = "closeMyDialog"/>
    </el-dialog>

    <el-dialog
      :visible.sync="noteInfoOpen"
      :destroy-on-close="true">
      <note-info :note-id="noteId"/>
    </el-dialog>
  </div>
</template>

<script>
import { listNote, getNote, delNote, addNote, updateNote } from "@/api/system/note";
import  NoteComponent  from "./NoteComponent.vue";
import  EditNote  from "./EditNote.vue";
import  NoteInfo  from "./NoteInfo.vue";

export default {
  name: "Note",
  components: {
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
      noteId: null,
      noteInfoOpen: false
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加笔记";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getNote(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改笔记";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateNote(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addNote(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除笔记编号为"' + ids + '"的数据项？').then(function() {
        return delNote(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/note/export', {
        ...this.queryParams
      }, `note_${new Date().getTime()}.xlsx`)
    },
    addMyNote() {
      this.reset();
      this.open = true;
      this.title = "添加笔记";
    },
    closeMyDialog() {
      this.open = false;
      this.editOpen = false;
      this.getList();
    },
    editNote(row) {
      this.editOpen = true;
      this.editTitle = "修改笔记";
      this.noteId = row.id
    },
    showNoteInfo(row) {
      this.noteInfoOpen = true;
      this.noteId = row.id
    }
  },
};
</script>
