<script>
import {getWordCollection, listWordCollection, updateWordCollection} from "@/api/bussiness/wordcollection";
import WordView from "@/views/business/language/wordcollection/wordView.vue";

export default {
  name: "index.vue",
  components: {WordView},
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
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
      },
      // 表单参数
      form: {
        uuid: '',
        name: '',
      },
      showEditor: false,
      showDialog: false,
      showView: false,
      labelUUID: ''
    };
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询对话场景列表 */
    getList() {
      this.loading = true;
      listWordCollection(this.queryParams).then(response => {
        this.dataList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        uuid: '',
        name: '',
      }
      this.title = ''
      this.showDialog = false;
      this.showEditor = false;
      this.showView = false
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
      this.ids = selection.map(item => item.labelUUID)
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
      this.title = "编辑名称";
      const id = row.labelUUID || this.ids
      let params = {
        uuid: id
      }
      getWordCollection(params).then(response => {
        this.form.name = response.data.name
        this.form.uuid = response.data.labelUUID
      });
    },
    handleWatch(row) {
      this.reset()
      this.showView = true;
      this.title = "查看单词集";
      this.labelUUID = row.labelUUID
      this.$refs.wordview.getList();
    },
    closeDialog() {
      this.showDialog = false
      this.showEditor = false
      this.getList();
    },
    doUpdateXXX() {
      let params = {
        labelUUID: this.form.uuid,
        name: this.form.name
      }
      updateWordCollection(params).then(res => {
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
      <el-form-item label="名称" prop="content">
        <el-input
          v-model="queryParams.name"
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
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="名称" align="center" prop="name"/>
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
            icon="el-icon-search"
            @click="handleWatch(scope.row)"
          >查看单词
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
        <el-form-item label="名称">
          <el-input style="margin-top: 10px" v-model="form.name" placeholder="请输入名称"></el-input>
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

    <el-dialog :title="title"
               :visible.sync="showView"
               :center="true"
               :destroy-on-close="true">
      <word-view ref="wordview" :labelUUID="labelUUID"></word-view>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">

</style>
