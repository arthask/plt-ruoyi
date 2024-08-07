<script>
import {getCollectionsOfPackage} from "@/api/bussiness/wordcollection";
import {removeCollectionOfPackage} from "@/api/bussiness/flashcardpackage";

export default {
  name: "wordCollectionView",
  props: {
    packageUUId: {
      type: String,
      default: "",
    }
  },
  data() {
    return {
      ids: [],
      loading: false,
      // 非多个禁用
      multiple: true,
      tableData: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        packageUUId: null,
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
      this.ids = selection.map(item => item.labelUUID)
      this.multiple = !selection.length
    },
    getList() {
      this.loading = true
      this.queryParams.packageUUId = this.packageUUId
      getCollectionsOfPackage(this.queryParams).then(response => {
        if (response.rows) {
          this.tableData = response.rows;
          this.total = response.total;
        }
        this.loading = false;
      });
    },
    handleDelete() {
      let params = {
        packageUUId: this.packageUUId,
        collectionUUIdList: this.ids
      }
      removeCollectionOfPackage(params).then(res => {
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
          :disabled="multiple"
        >从卡包中移除
        </el-button>
      </el-col>
    </el-row>
    <el-table :data="tableData" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="单词集" align="center" prop="name"/>
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
