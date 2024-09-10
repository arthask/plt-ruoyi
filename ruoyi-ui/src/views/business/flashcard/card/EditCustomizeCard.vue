<script>
import CkEditor from "@/components/Editor/CKEditor.vue";
import {batchAddCard} from "../../../../api/bussiness/flashcard";

export default {
  components: {CkEditor},
  name: "EditCustomizeCard",
  data() {
    return {
      cardList: [
        {
          front: '',
          back: ''
        }
      ]
    }
  },
  methods: {
    addNewCard() {
      const card = {
        front: '',
        back: ''
      }
      this.cardList.push(card)
    },
    submit() {
      for (const card of this.cardList) {
        if (!card.front || !card.back) {
          this.$modal.msgError("请填写必填项")
          return
        }
      }
      let data = {
        cardContents: this.cardList,
        cardType: 4
      }
      batchAddCard(data).then(res => {
        if (res.data) {
          this.$modal.msgSuccess("新增成功")
          // 刷新页面
          this.$emit("closeDialog")
        } else {
          this.$modal.msgError("新增失败")
        }
      })
    },
    cancel() {
      this.$emit("closeDialog")
    }
  }
}
</script>

<template>
  <div>
    <el-row v-for="card in cardList" :gutter="20">
      <el-col :span="12">
        <p>front</p>
        <ck-editor v-model="card.front"></ck-editor>
      </el-col>
      <el-col :span="12">
        <p>back</p>
        <ck-editor v-model="card.back"></ck-editor>
      </el-col>
    </el-row>
    <el-row style="margin-top: 20px">
      <el-col :span="10">
        <el-button type="primary" @click="addNewCard">添加</el-button>
      </el-col>
    </el-row>
    <el-row :gutter="20" justify="end" style="margin-top: 20px" type="flex">
      <el-col :span="10"></el-col>
      <el-col :span="6">
        <el-button type="primary" @click="submit">提交</el-button>
        <el-button type="danger" @click="cancel">取消</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<style lang="scss" scoped>
::v-deep .ck-editor__editable {
  min-height: 100px;
  height: 100%;
}
</style>
