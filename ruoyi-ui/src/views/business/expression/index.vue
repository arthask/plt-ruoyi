<script>
import {listExpression, getExpression, addExpression} from "@/api/bussiness/expression";

export default {
  name: "Expression.vue",
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
      // 对话场景表格数据
      expressionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        content: null,
      },
      // 表单参数
      form: {
        content: '',
        expressionDetailData: {
          content: ''
        }
      },
      showAdd: false,
    };
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询对话场景列表 */
    getList() {
      this.loading = true;
      listExpression(this.queryParams).then(response => {
        this.expressionList = response.rows;
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
        content: '',
          expressionDetailData: {
          content: ''
        }
      }
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.showAdd = true;
      this.title = "新增表达";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const id = row.uuid || this.ids
      let params = {
        sceneUUID: id
      }
      getExpression(params).then(response => {
        this.sceneData = response.data;
        this.showEditScene = true;
        this.title = "修改表达";
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.uuid || this.ids;
      this.$modal.confirm('是否确认删除选中数据项？').then(function () {
        return delScene(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    closeAdd() {
      this.showAdd = false;
      this.getList();
    },
    onSubmit() {
      let params = {
        content: this.form.content,
        expressionDetailData: [
          {
            content: this.form.expressionDetailData.content
          }
        ]
      }
      addExpression(params).then(res => {
        if (res.data === true) {
          this.$modal.msgSuccess("添加成功");
          this.showAdd = false
          this.reset()
          this.getList()
        }
      })
    }
  }
}
</script>

<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="表达内容" prop="content">
        <el-input
          v-model="queryParams.content"
          placeholder="请输入表达内容"
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
        >新增
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
        >删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="expressionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="表达内容" align="center" prop="content"/>
      <el-table-column label="创建时间" align="center" prop="createTime"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-chat-line-round"
            @click=""
          >添加表达
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
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
    <el-dialog :title="title"
               :visible.sync="showAdd" width="800px"
               :center="true"
               :destroy-on-close="true">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="想说">
          <el-input type="textarea" v-model="form.content" rows="3" resize="none"></el-input>
        </el-form-item>
        <el-form-item label="怎么说">
          <el-input type="textarea" v-model="form.expressionDetailData.content" rows="4" resize="none"></el-input>
        </el-form-item>
      </el-form>
      <el-row style="margin-top: 20px" :gutter="20" type="flex" justify="end">
        <el-col :span="10"></el-col>
        <el-col :span="4">
          <el-button type="primary" @click="onSubmit">立即创建</el-button>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="closeAdd">取消</el-button>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">

</style>
