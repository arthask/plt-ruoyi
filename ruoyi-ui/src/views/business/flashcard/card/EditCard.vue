<script>
import {getCardInfo, update} from "@/api/bussiness/flashcard";
import CkEditor from "@/components/Editor/CKEditor.vue";

export default {
  components: {CkEditor},
  name: "EditCard",
  props: {
    cardUuid: {
      type: String,
      default: ''
    },
  },
  data() {
    return {
      card: {
        uuid: "",
        front: '',
        back: '',
        type: null
      }
    }
  },
  created() {
    this.getCardInfo()
  },
  methods: {
    editSubmit() {
      this.card.uuid = this.cardUuid
      update(this.card).then(res => {
        if (res.data === true) {
          this.$modal.msgSuccess("修改成功");
          this.editorCancel()
          this.$emit("refreshList");
        } else {
          this.$modal.msgError("修改失败");
        }
      })
    },
    editorCancel() {
      // 通知父组件关闭dialog
      this.$emit("closeEditCard")
    },
    getCardInfo() {
      getCardInfo(this.cardUuid).then(response => {
        this.card = response.data;
      });
    }
  }
}
</script>

<template>
  <div>
    <div v-if="this.card.type !== 4">
      <el-form ref="editForm" :model="card" label-width="80px">
        <el-form-item label="正面">
          <el-input v-model="card.front" resize="none" rows="3" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="反面">
          <el-input v-model="card.back" resize="none" rows="4" type="textarea"></el-input>
        </el-form-item>
      </el-form>
      <el-row :gutter="20" justify="end" style="margin-top: 20px" type="flex">
        <el-col :span="10"></el-col>
        <el-col :span="6">
          <el-button type="primary" @click="editSubmit">提交</el-button>
          <el-button type="danger" @click="editorCancel">取消</el-button>
        </el-col>
      </el-row>
    </div>
    <div v-if="this.card.type === 4">
      <el-row :gutter="20">
        <el-col :span="12">
          <p>front</p>
          <ck-editor v-model="card.front"></ck-editor>
        </el-col>
        <el-col :span="12">
          <p>back</p>
          <ck-editor v-model="card.back"></ck-editor>
        </el-col>
      </el-row>
      <el-row :gutter="20" justify="end" style="margin-top: 20px" type="flex">
        <el-col :span="10"></el-col>
        <el-col :span="6">
          <el-button type="primary" @click="editSubmit">提交</el-button>
          <el-button type="danger" @click="editorCancel">取消</el-button>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<style lang="scss" scoped>
::v-deep .ck-editor__editable {
  min-height: 100px;
  height: 100%;
}
</style>
