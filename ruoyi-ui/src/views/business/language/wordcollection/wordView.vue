<script>
import {getWordsOfCollection, removeWordOfCollection} from "@/api/bussiness/wordcollection";

export default {
  name: "wordView",
  props: {
    labelUUID: {
      type: String,
      default: "",
    }
  },
  data() {
    return {
      ids: [],
      loading: false,
      tableData: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        labelUUID: null,
      },
      // 总条数
      total: 0,
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    formatter(row, column) {
      return row.address;
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.uuid)
    },
    getList() {
      this.loading = true
      this.queryParams.labelUUID = this.labelUUID
      getWordsOfCollection(this.queryParams).then(response => {
        if (response.rows) {
          this.tableData = response.rows;
          this.total = response.total;
        }
        this.loading = false;
      });
    },
    handleDelete() {
      let params = {
        labelUUID: this.labelUUID,
        wordUUIDList: this.ids
      }
      removeWordOfCollection(params).then(res => {
        if (res.data === true) {
          if (res.msg) {
            this.$modal.msgSuccess(res.msg);
          } else {
            this.$modal.msgSuccess("删除成功！");
          }
          this.getList();
        } else {
          this.$modal.msgError("删除失败！");
        }
      })
    }
  }
}

</script>

<template>
  <div>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="handleDelete"
        >从单词集中移除
        </el-button>
      </el-col>
    </el-row>
    <el-table :data="tableData" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="单词" align="center" prop="word"/>
      <el-table-column label="创建时间" align="center" prop="createTime"/>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<style scoped lang="scss">

</style>
