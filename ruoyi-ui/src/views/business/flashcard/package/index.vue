<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="卡包名" prop="name">
        <el-input
          v-model="queryParams.name"
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
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          @click="handleUpdate"
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
        >删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="包名" align="center" prop="name"/>
      <el-table-column label="类型" align="center" prop="type"/>
      <el-table-column label="创建时间" align="center" prop="createTime"/>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改用户单词对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body destroy-on-close>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="卡包名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入卡包名称"/>
        </el-form-item>
        <el-form-item label="卡包类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择卡包类型">
            <el-option v-for="item in typeOptions"
                       :key="item.value"
                       :label="item.label"
                       :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="卡包标签">
          <el-radio-group v-model="labelType" @input="changeType">
            <el-radio :label="3">选择已有标签</el-radio>
            <el-radio :label="6">新增标签</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="选择" v-if="showSelect">
          <el-select multiple v-model="form.labelInfos" placeholder="请选择卡包标签">
            <el-option v-for="item in labelOptions"
                       :key="item.value"
                       :label="item.label"
                       :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <LabelTag v-else @updateLabel="updateLabel">
        </LabelTag>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listPackages, getPackageInfo, add, del, update} from "@/api/bussiness/flashcardpackage";

import LabelTag from "@/components/Tag/index.vue";

export default {
  name: "card_package",
  components: {
    LabelTag
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
      // 用户单词表格数据
      dataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null
      },
      // 表单参数
      form: {
        labelInfos: []
      },
      typeOptions: [{
        value: 1,
        label: '单词卡包',
      }, {
        value: 1,
        label: '问题卡包',
      }],
      labelOptions: [],
      labelType: 3,
      showSelect: true,
      // 表单校验
      rules: {
        name: [
          {required: true, message: "卡包名不能为空", trigger: "blur"}
        ],
        type: [
          {required: true, message: "卡包类型不能为空", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户单词列表 */
    getList() {
      this.loading = true;
      listPackages(this.queryParams).then(response => {
        this.dataList = response.rows;
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
        name: null,
        type: null,
        labelInfos: [],
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加卡包";
    },
    /** 修改按钮操作 */
    handleUpdate() {
      this.reset();
      const id = this.ids
      getPackageInfo(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改卡包";
      });
    },
    updateLabel(labelList, deleteLableList) {
      this.form.labelInfos = [...labelList]
      // todo 删除的标签
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            let data = this.form;
            data.labelList = [...this.dynamicTags]
            data.deleteTags = [...this.deleteTags]
            update(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            // const formData = new FormData();
            // formData.append("name", this.form.name)
            // formData.append("type", this.form.type)
            // formData.append("labelInfos", this.form.labelInfos)
            add(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除编号为"' + ids + '"的数据项？').then(function () {
        return del(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    changeType(type) {
      if (type === 3) {
        this.showSelect = true;
      }
      if (type === 6) {
        this.showSelect = false;
      }
    }
  }
};
</script>
<style>
</style>
