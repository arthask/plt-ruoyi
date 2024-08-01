<script>
import {getPackageInfoOfCard} from "@/api/bussiness/flashcard";

export default {
  name: "PackageView",
  props: {
    cardUUId: {
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
    formatType(row, column) {
      // 获取当前行的金额值
      const type = row[column.property];
      if (type  === 1) {
        return "单词卡包"
      } else if (type === 2) {
        return "问题卡包"
      }
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.uuid)
    },
    getList() {
      this.loading = true
      this.queryParams.cardUUId = this.cardUUId
      getPackageInfoOfCard(this.queryParams).then(response => {
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
      <el-table-column label="包名" align="center" prop="name"/>
      <el-table-column label="类型" align="center" :formatter="formatType" prop="type"/>
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
