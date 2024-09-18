<script>
import {
  addExpression,
  delExpression,
  getExpression,
  listExpression,
  updateExpression
} from "@/api/bussiness/expression";
import ExpressionDetail from "@/views/business/expression/ExpressionDetail.vue";
import {batchAddCard} from "@/api/bussiness/flashcard";


export default {
  name: "Expression.vue",
  components: {ExpressionDetail},
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
        uuid: '',
        content: '',
        expressionDetailData: [{
          uuid: '',
          content: ''
        }]
      },
      addContent: '',
      appendContent: '',
      showEditor: false,
      showAppend: false,
      showDialog: false,
      showHistory: false,
      expressionUUID: '',
      details: [],
      showBatchAdd: false,
      multiExpressionList: []
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
        uuid: '',
        content: '',
        expressionDetailData: []
      }
      this.appendContent = ''
      this.addContent = ''
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
      this.title = "新增表达";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      this.showDialog = true;
      this.showEditor = true;
      this.title = "编辑表达";
      const id = row.uuid || this.ids
      let params = {
        expressionUUID: id
      }
      getExpression(params).then(response => {
        this.form.uuid = response.data.uuid
        this.form.content = response.data.content
        if (response.data.expressionDetailData && response.data.expressionDetailData.length > 0) {
          this.form.expressionDetailData = response.data.expressionDetailData
        }
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
      this.showDialog = false;
      this.showAppend = false
      this.showHistory = false
      this.showEditor = false
      this.getList();
    },
    doUpdateExpression() {
      let params = {
        uuid: this.form.uuid,
        content: this.form.content,
        expressionDetailData: this.form.expressionDetailData
      }
      updateExpression(params).then(res => {
        if (res.data === true) {
          this.$modal.msgSuccess("修改成功");
          this.showDialog = false
          this.showEditor = false
          this.reset()
          this.getList()
        } else {
          this.$modal.msgError("修改失败！");
        }
      })
    }, doAddExpression() {
      let params = {
        content: this.form.content,
        expressionDetailData: [
          {
            content: this.addContent
          }
        ]
      }
      addExpression(params).then(res => {
        if (res.data === true) {
          this.$modal.msgSuccess("添加成功");
          this.showDialog = false
          this.reset()
          this.getList()
        } else {
          this.$modal.msgError("添加失败");
        }
      })
    }, doAppendExpression() {
      let params = {
        uuid: this.form.uuid,
        content: this.form.content,
        expressionDetailData: [
          {
            content: this.appendContent
          }
        ]
      }
      addExpression(params).then(res => {
        if (res.data === true) {
          this.$modal.msgSuccess("添加成功");
          this.showDialog = false
          this.showAppend = false
          this.reset()
          this.getList()
        } else {
          this.$modal.msgError("添加失败！");
        }
      })
    }, onSubmit() {
      if (this.form.uuid) {
        if (this.showAppend) {
          this.doAppendExpression();
        } else {
          this.doUpdateExpression();
        }
      } else {
        this.doAddExpression();
      }
    },
    handleAppend(row) {
      this.reset()
      const id = row.uuid || this.ids
      let params = {
        expressionUUID: id
      }
      getExpression(params).then(response => {
        this.form.uuid = response.data.uuid
        this.form.content = response.data.content
      })
      this.showDialog = true;
      this.title = "追加表达";
      this.showAppend = true;
    },
    handleShowHistory(row) {
      this.showHistory = true
      let params = {
        expressionUUID: row.uuid
      }
      getExpression(params).then(response => {
        if (response.data.expressionDetailData && response.data.expressionDetailData.length > 0) {
          this.details = response.data.expressionDetailData
        }
      });
    },
    closeHistory() {
      this.showHistory = false
    },
    createCard() {
      let params = {
        uuidList: this.ids,
        cardType: 3
      }
      batchAddCard(params).then(res => {
        if (res.data === true) {
          this.$modal.msgSuccess("生成卡片成功");
        } else {
          this.$modal.msgError("生成卡片失败！");
        }
      })
    },
    handleBatchAdd() {
      this.showBatchAdd = true
    },
    handleExpressionAdd() {
      this.multiExpressionList.push({
        content: "",
        expression: "",
      },)
    },
    removeExpression(index) {
      this.multiExpressionList.splice(index, 1)
    },
    submitBatchExpression() {
      let params = {
        batchAddExpressionDtoList: this.multiExpressionList
      }
      addExpression(params).then(res => {
        if (res.data === true) {
          this.$modal.msgSuccess("添加成功");
          this.showDialog = false
          this.reset()
          this.getList()
        } else {
          this.$modal.msgError("添加失败");
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
          type="success"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          icon="el-icon-plus"
          plain
          size="mini"
          type="primary"
          @click="handleBatchAdd"
        >批量新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :disabled="multiple"
          icon="el-icon-plus"
          plain
          size="mini"
          type="warning"
          @click="createCard"
        >生成卡片
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
            @click="handleAppend(scope.row)"
          >添加表达
          </el-button>
          <el-button
            size="mini"
            type="text"
            @click="handleShowHistory(scope.row)"
          >查看历史
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
               :before-close="closeDialog"
               :destroy-on-close="true">
      <el-form ref="form" :model="form" label-width="80px" style="overflow-y: auto; max-height: 420px;padding: 0 20px">
        <el-form-item label="想说">
          <el-input type="textarea" v-if="showAppend" disabled v-model="form.content" rows="3" resize="none"></el-input>
          <el-input type="textarea" v-else v-model="form.content" rows="3" resize="none"></el-input>
        </el-form-item>
        <el-form-item label="怎么说" v-if="!showEditor">
          <el-input type="textarea" v-if="showAppend" v-model="appendContent" rows="4" resize="none"></el-input>
          <el-input type="textarea" v-else v-model="addContent" rows="4"
                    resize="none"></el-input>
        </el-form-item>
        <el-form-item v-else
                      v-for="(item, index) in form.expressionDetailData"
                      :key="item.uuid"
                      label="表达">
          <el-input type="textarea" v-model="item.content" rows="4" resize="none"></el-input>
        </el-form-item>
      </el-form>
      <el-row style="margin-top: 20px" :gutter="20" type="flex" justify="end">
        <el-col :span="10"></el-col>
        <el-col :span="6">
          <el-button type="primary" @click="onSubmit">提交</el-button>
          <el-button type="danger" @click="closeDialog">取消</el-button>
        </el-col>
      </el-row>
    </el-dialog>
    <el-dialog
      :visible.sync="showHistory" width="800px"
      :center="true"
      :destroy-on-close="true"
      :before-close="closeHistory"
    >
      <expression-detail :items="details"></expression-detail>
    </el-dialog>
    <el-dialog v-if="showBatchAdd" :center="true" :destroy-on-close="true" :visible.sync="showBatchAdd">
      <el-row justify="start" type="flex">
        <el-button style="margin-top: 10px" @click.prevent="handleExpressionAdd">添加</el-button>
      </el-row>
      <el-row v-for="(item,index) in multiExpressionList" :key="index"
              :gutter="20">
        <el-col :span="10">
          <el-input v-model="item.senderContent" maxlength="100" placeholder="想表达" resize="none"
                    rows="2" show-word-limit
                    style="margin-top: 10px" type="textarea"></el-input>
        </el-col>
        <el-col :span="10">
          <el-input v-model="item.reply" maxlength="100" placeholder="如何说" resize="none"
                    rows="2" show-word-limit
                    style="margin-top: 10px" type="textarea"></el-input>
        </el-col>
        <el-col :span="4">
          <el-button style="margin-top: 10px" @click.prevent="removeExpression(index)">删除</el-button>
        </el-col>
      </el-row>
      <el-row :gutter="20" justify="end" style="margin-top: 20px" type="flex">
        <el-col :span="10"></el-col>
        <el-col :span="4">
          <el-button type="primary" @click="submitBatchExpression">提交</el-button>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">

</style>
