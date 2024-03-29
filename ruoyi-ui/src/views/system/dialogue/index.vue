<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="场景id" prop="sceneId">
        <el-input
          v-model="queryParams.sceneId"
          placeholder="请输入场景id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="排序号" prop="sortNum">
        <el-input
          v-model="queryParams.sortNum"
          placeholder="排序号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建人" prop="createUserId">
        <el-input
          v-model="queryParams.createUserId"
          placeholder="请输入创建人"
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
          v-hasPermi="['system:dialogue:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:dialogue:edit']"
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
          v-hasPermi="['system:dialogue:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:dialogue:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dialogueList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="场景id" align="center" prop="sceneId" />
      <el-table-column label="发送内容" align="center" prop="senderContent" />
      <el-table-column label="回复" align="center" prop="reply" />
      <el-table-column label="排序号" align="center" prop="sortNum" />
      <el-table-column label="创建人" align="center" prop="createUserId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:dialogue:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:dialogue:remove']"
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

    <!-- 添加或修改对话对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="场景id" prop="sceneId">
          <el-input v-model="form.sceneId" placeholder="请输入场景id" />
        </el-form-item>
        <el-form-item label="发送内容">
<!--          <editor v-model="form.senderContent" :min-height="192"/>-->
          <ck-editor v-model="form.senderContent" :min-height="192"></ck-editor>
        </el-form-item>
        <el-form-item label="回复" prop="reply">
          <el-input v-model="form.reply" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="在对话中的排序号" prop="sortNum">
          <el-input v-model="form.sortNum" placeholder="请输入在对话中的排序号" />
        </el-form-item>
        <el-form-item label="创建人" prop="createUserId">
          <el-input v-model="form.createUserId" placeholder="请输入创建人" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDialogue, getDialogue, delDialogue, addDialogue, updateDialogue } from "@/api/conversation/dialogue";
import CkEditor from "@/components/Editor/CKEditor.vue";

export default {
  name: "Dialogue",
  components: {CkEditor},
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
      // 对话表格数据
      dialogueList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sceneId: null,
        senderContent: null,
        reply: null,
        sortNum: null,
        createUserId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sceneId: [
          { required: true, message: "场景id不能为空", trigger: "blur" }
        ],
        senderContent: [
          { required: true, message: "发送内容不能为空", trigger: "blur" }
        ],
        reply: [
          { required: true, message: "回复不能为空", trigger: "blur" }
        ],
        sortNum: [
          { required: true, message: "在对话中的排序号不能为空", trigger: "blur" }
        ],
        createUserId: [
          { required: true, message: "创建人不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询对话列表 */
    getList() {
      this.loading = true;
      listDialogue(this.queryParams).then(response => {
        this.dialogueList = response.rows;
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
        sceneId: null,
        senderContent: null,
        reply: null,
        sortNum: null,
        createUserId: null,
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
      this.title = "添加对话";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDialogue(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改对话";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDialogue(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDialogue(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除对话编号为"' + ids + '"的数据项？').then(function() {
        return delDialogue(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/dialogue/export', {
        ...this.queryParams
      }, `dialogue_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
