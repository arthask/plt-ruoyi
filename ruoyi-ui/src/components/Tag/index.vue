<script>
export default {
  props:{
    tags:Array
  },
  watch:{
    tags(newValue,oldValue) {
      this.dynamicTags = newValue;
    }
  },
  name: "label_tag",
  data() {
    return {
      dynamicTags: [],
      editTags: [],
      deleteTags: [],
      inputVisible: false,
      inputValue: '',
    }
  },
  methods:{
    handleClose(tag, index) {
      const deletedTag = this.dynamicTags.splice(index, 1)[0];
      if (this.form.id != null) {
        this.deleteTags.push(deletedTag)
      }
      this.updateLabel(this.dynamicTags, this.deleteTags)
    },

    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },

    handleInputConfirm() {
      this.inputVisible = false;
      let inputValue = this.inputValue;
      if (inputValue) {
        const newTag = {
          name: inputValue
        };
        this.dynamicTags.push(newTag);
        this.updateLabel(this.dynamicTags, [])
      }
      this.inputValue = '';
    },

    showEditTagInput(index) {
      this.resetEditTagsFlag()
      this.$set(this.editTags, index, true)
      this.$nextTick(() => {
        var editInput = 'editInput' + index;
        this.$refs[editInput][0].$refs.input.focus()
      })
    },

    handleEditableInputConfirm(item, index) {
      if (item.name) {
        this.$set(this.editTags, index, false)
      } else {
        this.$message({message: "请填入标签", type: "info"})
      }
    },

    handleEditableInputBlur(item, index) {
      this.$set(this.editTags, index, false)
    },
    resetEditTagsFlag() {
      for (let i = 0; i < this.editTags.length; i++) {
        this.editTags[i] = false
      }
    },
    updateLabel(labelList, deleteList) {
      this.$emit('updateLabel', labelList, deleteList);
    }
  }
}
</script>

<template>
  <el-form-item label="新增" prop="label">
    <el-row :gutter="20">
      <el-col :span="20" v-for="(tag, index) in dynamicTags" :key="index">
        <el-input
          class="input-new-tag"
          v-if="editTags[index]"
          v-model="tag.name"
          :ref="'editInput'+index"
          @keyup.enter.native="handleEditableInputConfirm(tag, index)"
          @change="handleEditableInputConfirm(tag, index)"
          @blur="handleEditableInputBlur(tag, index)"
        >
        </el-input>
        <el-tag
          class="input-new-tag"
          :key="tag.name"
          v-else
          closable
          :disable-transitions="false"
          @close="handleClose(tag, index)"
          @click="showEditTagInput(index)"
        >
          {{ tag.name }}
        </el-tag>
      </el-col>
      <el-col :span="4">
        <el-input
          class="input-new-tag"
          v-if="inputVisible"
          v-model="inputValue"
          ref="saveTagInput"
          @keyup.enter.native="handleInputConfirm"
          @blur="handleInputConfirm"
        >
        </el-input>
        <el-button v-else class="button-new-tag" size="small" @click="showInput">+标签</el-button>
      </el-col>
    </el-row>
  </el-form-item>
</template>

<style scoped lang="scss">
.input-new-tag {
}


.el-tag + .el-tag {
  margin-left: 10px;
}

.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
</style>
