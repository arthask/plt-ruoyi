<script>
import {addLabel, getInfo, listLabel, updateLabel} from "@/api/bussiness/label";

export default {
  name: "index",
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
      // 表格数据
      tableDataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        target: null
      },
      // 表单参数
      form: {
        uuid: '',
        name: '',
        target: null
      },
      showAdd: false,
      showEditor: false,
      targetOptions: [{
        value: 1,
        label: '词库'
      },
        {
          value: 2,
          label: '单词'
        },
      ],
      targetDisable: false
    };
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询对话场景列表 */
    getList() {
      this.loading = true;
      listLabel(this.queryParams).then(response => {
        this.tableDataList = response.rows;
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
        uuid: '',
        name: '',
        target: null
      }
      this.targetDisable = false
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
      this.title = "新增标签";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.showAdd = true;
      this.title = "修改标签";
      const id = row.uuid || this.ids
      let params = {
        uuid: id
      }
      getInfo(params).then(res => {
        let {uuid, name, target} = res.data
        this.form.uuid = uuid
        this.form.name = name
        this.form.target = target
        if (target) {
          this.targetDisable = true
        }
      })
    },
    onSubmit() {
      if (this.form.uuid) {
        // 修改
        let paramTarget = null
        if (!this.targetDisable) {
          paramTarget = this.form.target
        }
        let data = {
          uuid: this.form.uuid,
          name: this.form.name,
          target: paramTarget
        }
        updateLabel(data).then(res => {
          if (res.data === true) {
            this.$modal.msgSuccess("修改成功");
            this.showAdd = false
            this.reset()
            this.getList()
          } else {
            this.$modal.msgError("修改失败");
          }
        })
      } else {
        // 新增
        let data = {
          name: this.form.name,
          target: this.form.target
        }
        addLabel(data).then(res => {
          if (res.data === true) {
            this.$modal.msgSuccess("添加成功");
            this.showAdd = false
            this.reset()
            this.getList()
          } else {
            this.$modal.msgError("添加失败");
          }
        })
      }
    },
    closeDialog() {
      this.showAdd = false;
      this.showEditor = false
      this.targetDisable = false
      this.getList();
    },
    formatType(row, column) {
      // 获取当前行的金额值
      const target = row[column.property];
      if (target === 1) {
        return "词库"
      } else if (target === 2) {
        return "单词"
      }
    },
  }
}
</script>

<template>
  <div class="app-container">
    <el-form v-show="showSearch" ref="queryForm" :inline="true"
             :model="queryParams" label-width="68px" size="small">
      <el-form-item label="标签名称" prop="name">
        <el-input
          v-model="queryParams.name"
          clearable
          placeholder="请输入标签名称"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="作用对象" prop="target">
        <el-select v-model="queryParams.target" clearable placeholder="请选择">
          <el-option
            v-for="item in targetOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" size="mini" type="primary" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          @click="handleAdd"
        >新增
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="tableDataList" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55"/>
      <el-table-column align="center" label="名称" prop="name"/>
      <el-table-column :formatter="formatType" align="center" label="作用对象" prop="target"/>
      <el-table-column align="center" label="创建时间" prop="createTime"/>
      <el-table-column align="center" class-name="small-padding fixed-width" label="操作">
        <template slot-scope="scope">
          <el-button
            icon="el-icon-edit"
            size="mini"
            type="text"
            @click="handleUpdate(scope.row)"
          >修改
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :limit.sync="queryParams.pageSize"
      :page.sync="queryParams.pageNum"
      :total="total"
      @pagination="getList"
    />
    <el-dialog v-if="showAdd"
               :center="true"
               :destroy-on-close="true" :title="title"
               :visible.sync="showAdd"
               width="800px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="名称">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="作用对象" label-width="80px">
          <el-select v-model="form.target" :disabled="targetDisable" clearable placeholder="请选择">
            <el-option
              v-for="item in targetOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <el-row :gutter="20" justify="end" style="margin-top: 20px" type="flex">
        <el-col :span="10"></el-col>
        <el-col :span="6">
          <el-button type="primary" @click="onSubmit">提交</el-button>
          <el-button type="danger" @click="closeDialog">取消</el-button>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>

</style>
