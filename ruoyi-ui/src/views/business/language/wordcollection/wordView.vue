<script>
import {getWordsOfCollection, listWordCollection} from "@/api/bussiness/wordcollection";

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
      loading : false,
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
  methods: {
    formatter(row, column) {
      return row.address;
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.uuid)
    },
    getList(){
      this.loading = true
      this.queryParams.labelUUID = this.labelUUID
      getWordsOfCollection(this.queryParams).then(response => {
        this.tableData = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    }
  }
}

</script>

<template>
  <div>
    <el-table :data="tableData" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="单词" align="center" prop="name"/>
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
