<script>
import {addWordToCollection, getAllLabels} from "@/api/bussiness/wordcollection";

export default {
  name: "word2Collection",
  props: {
    wordUUIDs: {
      type: Array,
      default: null,
    },
    labelUUID: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      options: [],
      value: ''
    }
  },
  mounted() {
    this.getLabels()
  },
  methods: {
    getLabels() {
      getAllLabels().then(res => {
        this.options = res.data
      })
    },
    onSubmit() {
      if (this.labelUUID) {
        this.doUpdateExpression();
      } else {
        this.doAddWord2Collection();
      }
    },
    doAddWord2Collection() {
      let params = {
        wordUUIDList: this.wordUUIDs,
        labelUUID: this.value
      }
      addWordToCollection(params).then(res => {
        if (res.data === true) {
          if (res.msg) {
            this.$message({
              message: res.msg,
              type: 'success'
            });
          } else {
            this.$modal.msgSuccess("添加成功")
          }
          this.closeDialog()
        } else {
          this.$modal.msgSuccess("添加失败");
        }
      })
    },
    doUpdateExpression() {
      let params = {}
    },
    closeDialog() {
      this.$emit("closeDialog")
    }
  }
}
</script>

<template>
  <div>
    <el-select v-model="value" filterable placeholder="请选择">
      <el-option
        v-for="item in options"
        :key="item.uuid"
        :label="item.name"
        :value="item.uuid">
      </el-option>
    </el-select>
    <el-row style="margin-top: 20px" :gutter="20" type="flex" justify="end">
      <el-col :span="10"></el-col>
      <el-col :span="4">
        <el-button type="primary" @click="onSubmit">提交</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">

</style>
