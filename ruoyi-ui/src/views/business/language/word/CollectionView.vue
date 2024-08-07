<script>
import {getCollectionOfWord} from "@/api/bussiness/wordcollection";

export default {
  name: "CollectionView",
  props: {
    wordUUId: {
      type: String,
      default: "",
    }
  },
  data() {
    return {
      ids: [],
      // 非多个禁用
      multiple: true,
      loading: false,
      tableData: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        wordUUId: null,
      },
      // 总条数
      total: 0,
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.uuid)
      this.multiple = !selection.length
    },
    getList() {
      this.loading = true
      this.queryParams.wordUUId = this.wordUUId
      getCollectionOfWord(this.queryParams).then(response => {
        if (response.rows) {
          this.tableData = response.rows;
          this.total = response.total;
        }
        this.loading = false;
      });
    }
  }
}
</script>

<template>
  <div>
    <el-table :data="tableData" @selection-change="handleSelectionChange">
      <el-table-column label="名称" align="center" prop="name"/>
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
