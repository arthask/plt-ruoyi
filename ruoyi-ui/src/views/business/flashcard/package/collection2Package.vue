<script>
import {addWordToCollection, getAllLabels} from "@/api/bussiness/wordcollection";
import {addCollectionToPackage} from "@/api/bussiness/flashcardpackage";

export default {
  name: "collection2Package",
  props: {
    packageUUID: {
      type: String,
      default: null
    },
  },
  data() {
    return {
      options: [],
      typeOptions: [{
        value: 1,
        label: '单词卡片',
      }, {
        value: 2,
        label: '问题卡片',
      }],
      form : {
        collectionUUID: '',
        cardType: null
      },
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
      this.doAddCollection2Package();
    },
    doAddCollection2Package() {
      let params = {
        cardType: this.form.cardType,
        collectionUUID: this.form.collectionUUID,
        packageUUID: this.packageUUID
      }
      addCollectionToPackage(params).then(res => {
        if (res.data === true) {
          if (res.msg) {
            this.$modal.msgSuccess(res.msg);
          } else {
            this.$modal.msgSuccess("添加成功")
          }
          this.closeDialog()
        } else {
          this.$modal.msgError("添加失败");
        }
      })
    },
    closeDialog() {
      this.$emit("closeDialog")
    }
  }
}
</script>

<template>
  <div>
    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item label="卡片类型" prop="cardType">
        <el-select v-model="form.cardType" placeholder="请选择卡片类型">
          <el-option v-for="item in typeOptions"
                     :key="item.value"
                     :label="item.label"
                     :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="单词集" prop="collectionUUID">
        <el-select v-model="form.collectionUUID" filterable placeholder="请选择单词集">
          <el-option
            v-for="item in options"
            :key="item.uuid"
            :label="item.name"
            :value="item.uuid">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>

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
