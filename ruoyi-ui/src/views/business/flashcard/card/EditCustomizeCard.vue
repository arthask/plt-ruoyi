<script>
import CkEditor from "@/components/Editor/CKEditor.vue";

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
    }
  }
}
</script>

<template>
  <div>
    <el-row v-for="card in cardList" :gutter="20">
      <el-col :span="12">
        <p>front</p>
        <ck-editor v-model="card.front" class="bb"></ck-editor>
      </el-col>
      <el-col :span="12">
        <p>back</p>
        <ck-editor v-model="card.back" class="bb"></ck-editor>
      </el-col>
    </el-row>
    <el-row style="margin-top: 20px">
      <el-col :span="10">
        <el-button type="primary" @click="addNewCard">添加</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<style lang="scss" scoped>
.bb {
  height: 200px;
}
</style>
