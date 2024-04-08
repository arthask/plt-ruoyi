<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="语言" prop="language">
        <el-input
          v-model="queryParams.language"
          placeholder="请输入语言"
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
          v-hasPermi="['system:lexicon:add']"
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
          v-hasPermi="['system:lexicon:edit']"
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
          v-hasPermi="['system:lexicon:remove']"
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
          v-hasPermi="['system:lexicon:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-row :gutter="20">
      <el-col
        v-for="(item, index)  in lexiconList"
        :key="index"
        :span="8">
        <el-card class="box-card" style="margin-top: 20px">
          <div style="padding: 0 14px 14px 14px;">
            <p>{{ item.name }}</p>
            <el-button type="text">查看</el-button>
            <el-button type="text">修改</el-button>
            <el-button type="text">删除</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改词库对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" :destroy-on-close="true">
      <data-component></data-component>
    </el-dialog>
  </div>
</template>

<script>
import {listLexicon, getLexicon, delLexicon, addLexicon, updateLexicon} from "@/api/system/lexicon";
import DataComponent from '@/views/system/lexicon/DataComponent.vue'
export default {
  name: "Lexicon",
  components:{
    DataComponent
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
      // 词库表格数据
      lexiconList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        uuid: null,
        name: null,
        language: null,
        createUserId: null,
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询词库列表 */
    getList() {
      this.loading = true;
      listLexicon(this.queryParams).then(response => {
        this.lexiconList = response.rows;
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
        uuid: null,
        name: null,
        language: null,
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加词库";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getLexicon(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改词库";
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除词库编号为"' + ids + '"的数据项？').then(function () {
        return delLexicon(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/lexicon/export', {
        ...this.queryParams
      }, `lexicon_${new Date().getTime()}.xlsx`)
    },
  }
};
</script>
