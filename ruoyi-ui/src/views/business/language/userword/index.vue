<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="单词" prop="word">
        <el-input
          v-model="queryParams.word"
          placeholder="请输入单词"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="阶段" prop="period">
        <el-select v-model="queryParams.period" clearable placeholder="请选择">
          <el-option
            v-for="item in periodOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="是否收藏" prop="collectFlag">
        <el-select v-model="queryParams.collectFlag" clearable placeholder="请选择">
          <el-option
            v-for="item in collectOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:word:remove']"
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
          v-hasPermi="['system:word:export']"
        >导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          @click="review"
          v-hasPermi="['system:word:edit']"
        >我要复习
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="wordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="单词" align="center" prop="word"/>
      <el-table-column :formatter="formatPeriod" align="center" label="阶段" prop="period"/>
      <el-table-column :formatter="formatCollect" align="center" label="是否收藏" prop="collectFlag"/>
      <el-table-column align="center" label="创建日期" prop="createTime"/>
      <el-table-column align="center" label="下次学习时间" prop="nextStudyTime"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="showStudyRecordDetail(scope.row)"
          >学习详情
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:word:remove']"
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

    <el-dialog
      :visible.sync="showStudyRecord"
      title="学习详情"
      width="60%">
      <el-table v-loading="loading" :data="studyRecordList">
        <el-table-column align="center" type="selection" width="55"/>
        <el-table-column align="center" label="单词" prop="word"/>
        <el-table-column align="center" label="学习时间" prop="studyTime"/>
      </el-table>
      <pagination
        v-show="studyRecordTotal>0"
        :limit.sync="studyRecordQueryParams.pageSize"
        :page.sync="studyRecordQueryParams.pageNum"
        :total="studyRecordTotal"
        @pagination="getStudyRecordList()"
      />
    </el-dialog>

  </div>
</template>

<script>
import {delWord, listWord} from "@/api/bussiness/userword";
import {getStudyRecordsOfWord} from "@/api/bussiness/record";


export default {
  name: "Word",
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
      studyRecordTotal: 0,
      // 用户单词表格数据
      wordList: [],
      studyRecordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        word: null,
        period: null,
        collectFlag: null,
      },
      studyRecordQueryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      showStudyRecord: false,
      wordUUId: '',
      periodOptions: [
        {
          value: 1,
          label: '阶段1'
        }, {
          value: 2,
          label: '阶段2'
        },
        {
          value: 3,
          label: '阶段3'
        },
        {
          value: 4,
          label: '阶段4'
        },
        {
          value: 5,
          label: '阶段5'
        },
        {
          value: 6,
          label: '阶段6'
        },
        {
          value: 7,
          label: '阶段7'
        },
        {
          value: 8,
          label: '阶段8'
        },
      ],
      collectOptions: [
        {
          value: 0,
          label: '否'
        },
        {
          value: 1,
          label: '是'
        }
      ]
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户单词列表 */
    getList() {
      this.loading = true;
      listWord(this.queryParams).then(response => {
        this.wordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    getStudyRecordList() {
      let params = {
        wordUUId: this.wordUUId
      }
      getStudyRecordsOfWord(params).then(res => {
        this.studyRecordList = res.rows;
        this.studyRecordTotal = res.total;
        this.loading = false;
      })
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除用户单词编号为"' + ids + '"的数据项？').then(function () {
        return delWord(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/word/export', {
        ...this.queryParams
      }, `word_${new Date().getTime()}.xlsx`)
    },
    review() {
      // this.$router.push("/review")
      this.$tab.openPage("复习", "/review");
    },
    formatPeriod(row, column) {
      const target = row[column.property];
      if (target === 1) {
        return "阶段1"
      } else if (target === 2) {
        return "阶段2"
      } else if (target === 3) {
        return "阶段3"
      } else if (target === 4) {
        return "阶段4"
      } else if (target === 5) {
        return "阶段5"
      } else if (target === 6) {
        return "阶段6"
      } else if (target === 7) {
        return "阶段7"
      } else if (target === 8) {
        return "阶段8"
      }
    },
    formatCollect(row, column) {
      const target = row[column.property];
      if (target === 0) {
        return "否"
      } else if (target === 1) {
        return "是"
      }
    },
    showStudyRecordDetail(row) {
      this.wordUUId = row.wordUuid
      this.getStudyRecordList()
      this.showStudyRecord = true
    }
  }
};
</script>
<style>
.ok-content {
  color: #42b983;
  font-size: large;
}

.error-content {
  color: #E65D6E;
  font-size: large;
}
</style>
