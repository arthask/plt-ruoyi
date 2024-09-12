<script>
import {cancelAssociation2Package, getPackageInfoOfCard} from "@/api/bussiness/flashcard";

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
      multiple: true,
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
      if (type === 1) {
        return "单词卡包"
      } else if (type === 2) {
        return "问题卡包"
      } else if (type === 3) {
        return "表达卡包"
      } else if (type === 4) {
        return "自定义卡包"
      }
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.uuid)
      this.multiple = !selection.length
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
    }, handleDelete() {
      let params = {
        cardUUId: this.cardUUId,
        packageUUIdList: this.ids
      }
      cancelAssociation2Package(params).then(res => {
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
          :disabled="multiple"
          icon="el-icon-delete"
          plain
          size="mini"
          type="danger"
          @click="handleDelete"
        >取消卡包关联
        </el-button>
      </el-col>
    </el-row>
    <el-table :data="tableData" @selection-change="handleSelectionChange">
      <el-table-column align="center" type="selection" width="55"/>
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
