<script>
import {getAllLabels} from "@/api/bussiness/wordcollection";
import {addExpression} from "@/api/bussiness/expression";

export default {
  name: "word2Collection",
  data() {
    return {
      options: [],
      value: ''
    }
  },
  methods: {
    getAllLabels() {
      getAllLabels().then(res => {
        this.options = res.data.data
      })
    },
    onSubmit() {
      if (this.form.uuid) {
        this.doUpdateExpression();
      } else {
        this.doAddExpression();
      }
    },
    doAddExpression() {
      let params = {
      }
      addExpression(params).then(res => {
        if (res.data === true) {
          this.$modal.msgSuccess("添加成功");
          this.reset()
          this.getList()
        } else {
          this.$modal.msgSuccess("添加失败");
        }
      })
    },
    doUpdateExpression() {
      let params = {
      }
      updateExpression(params).then(res => {
        if (res.data === true) {
          this.$modal.msgSuccess("修改成功");
          this.getList()
        } else {
          this.$modal.msgSuccess("修改失败！");
        }
      })
    },
    closeDialog() {
    },
  }
}
</script>

<template>
  <div>
    <el-select v-model="value" filterable placeholder="请选择">
      <el-option
        v-for="item in options"
        :key="item.value"
        :label="item.label"
        :value="item.value">
      </el-option>
    </el-select>
    <el-row style="margin-top: 20px" :gutter="20" type="flex" justify="end">
      <el-col :span="10"></el-col>
      <el-col :span="4">
        <el-button type="primary" @click="onSubmit">提交</el-button>
      </el-col>
      <el-col :span="4">
        <el-button type="primary" @click="closeDialog">取消</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">

</style>
