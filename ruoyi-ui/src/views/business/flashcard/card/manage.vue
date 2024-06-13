<template>
  <div class="app-container">
    <!--    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">-->
    <!--      <el-form-item label="卡包名" prop="name">-->
    <!--        <el-input-->
    <!--          v-model="queryParams.name"-->
    <!--          placeholder="请输入单词"-->
    <!--          clearable-->
    <!--          @keyup.enter.native="handleQuery"-->
    <!--        />-->
    <!--      </el-form-item>-->
    <!--      <el-form-item>-->
    <!--        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>-->
    <!--        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
    <!--      </el-form-item>-->
    <!--    </el-form>-->

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
      <el-table-column label="正面" align="center" prop="front"/>
      <el-table-column label="反面" align="center" prop="back"/>
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
        <el-form-item label="卡片类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择卡片类型">
            <el-option v-for="item in typeOptions"
                       :key="item.value"
                       :label="item.label"
                       :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择单词">
          <el-select filterable remote :remote-method="queryWord" v-model="form.wordUUID" placeholder="请选择单词">
            <el-option v-for="item in wordOptions"
                       :key="item.value"
                       :label="item.word"
                       :value="item.uuid">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择问题">
          <el-select filterable remote :remote-method="searchQuestion" v-model="form.questionUUID" placeholder="请选择问题">
            <el-option v-for="item in questionOptions"
                       :key="item.value"
                       :label="item.question"
                       :value="item.uuid">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择卡包">
          <el-select filterable v-model="form.packageUUID" placeholder="请选择卡包">
            <el-option v-for="item in packageOptions"
                       :key="item.value"
                       :label="item.name"
                       :value="item.uuid">
            </el-option>
          </el-select>
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
import LabelTag from "@/components/Tag/index.vue";
import {getCardInfo, listCards, add, del, update} from "@/api/bussiness/flashcard";
import {searchWord, searchWordByCN} from "@/api/bussiness/word";
import {getPackageList} from "@/api/bussiness/flashcardpackage";

export default {
  name: "card_manage",
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
        type: null,
        wordUUID: "",
        questionUUID: "",
        packageUUID: ""
      },
      typeOptions: [{
        value: 1,
        label: '单词卡片',
      }, {
        value: 1,
        label: '问题卡片',
      }],
      wordOptions: [],
      questionOptions: [],
      packageOptions: [],
      labelType: 3,
      showSelect: true,
      // 表单校验
      rules: {
        wordUUID: [
          {required: true, message: "单词不能为空", trigger: "blur"}
        ],
        packageUUID: [
          {required: true, message: "卡包不能为空", trigger: "blur"}
        ],
        type: [
          {required: true, message: "卡片类型不能为空", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.getList();
    getPackageList().then(res => {
      this.packageOptions = res.data;
      console.log(this.options)
    })
  },
  methods: {
    /** 查询用户单词列表 */
    getList() {
      this.loading = true;
      listCards(this.queryParams).then(response => {
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
      this.title = "添加卡片";
    },
    /** 修改按钮操作 */
    handleUpdate() {
      this.reset();
      const id = this.ids
      getCardInfo(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改卡片";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            update(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
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
    queryWord(query) {
      if (query !== '') {
        this.loading = true;
        let params = {
          word: query
        }
        searchWord(params).then(res => {
          this.wordOptions = []
          this.wordOptions = res.data
          this.loading = false;
        })
      } else {
        this.options = [];
      }
    },
    searchQuestion(query) {
      if (query !== '') {
        this.loading = true;
        let params = {
          word: query
        }
        searchQuestion(params).then(res => {
          this.questionOptions = []
          this.questionOptions = res.data
          this.loading = false;
        })
      } else {
        this.options = [];
      }
    }
  }
};
</script>
<style>
</style>
