<script>
export default {
  name: "index.vue",
  components: {},
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
      dataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        xxx: null,
      },
      // 表单参数
      form: {
        uuid: '',

      },
      showEditor: false,
      showDialog: false,
    };
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询对话场景列表 */
    getList() {
      this.loading = true;
      listXxx(this.queryParams).then(response => {
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
        uuid: '',

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
      this.showDialog = true;
      this.title = "新增XXX";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.showDialog = true;
      this.showEditor = true;
      this.title = "编辑XXX";
      const id = row.uuid || this.ids
      let params = {

      }
      getXX(params).then(response => {

      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.uuid || this.ids;
      this.$modal.confirm('是否确认删除选中数据项？').then(function () {
        return delExpression(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    closeDialog() {
      this.showDialog = false
      this.showEditor = false
      this.getList();
    },
    doUpdateXXX() {
      let params = {
      }
      updateXX(params).then(res => {
        if (res.data === true) {
          this.$modal.msgSuccess("修改成功");
          this.showDialog = false
          this.showEditor = false
          this.reset()
          this.getList()
        } else {
          this.$modal.msgSuccess("修改失败！");
        }
      })
    }, doAddXXX() {
      let params = {
      }
      addXX(params).then(res => {
        if (res.data === true) {
          this.$modal.msgSuccess("添加成功");
          this.showDialog = false
          this.reset()
          this.getList()
        } else {
          this.$modal.msgSuccess("添加失败");
        }
      })
    }, onSubmit() {
      if (this.form.uuid) {
        this.doUpdateXXX();
      } else {
        this.doAddXXX();
      }
    }
  }
}
</script>

<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="xxx" prop="content">
        <el-input
          v-model="queryParams.xxx"
          placeholder="请输入名称"
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

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
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
               :visible.sync="showDialog" width="800px"
               :center="true"
               :destroy-on-close="true">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="xxx" v-show="!showEditor">
        </el-form-item>
      </el-form>
      <el-row style="margin-top: 20px" :gutter="20" type="flex" justify="end">
        <el-col :span="10"></el-col>
        <el-col :span="4">
          <el-button type="primary" @click="onSubmit">提交</el-button>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="closeDialog">取消</el-button>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">

</style>
