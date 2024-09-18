<template>
  <div style="overflow-y: auto; max-height: 450px;padding: 0 20px">
    <el-form v-if="showName" ref="form" :model="sceneData" label-position="top" label-width="80px">
      <el-form-item label="场景名称" prop="name">
        <el-input v-model="sceneData.name"></el-input>
      </el-form-item>
      <el-form-item label="背景介绍" prop="introduce">
        <ck-editor v-model="sceneData.introduce" :min-height="192" class="bb"></ck-editor>
      </el-form-item>
      <el-form-item label="学习内容" prop="studyInfo">
        <ck-editor v-model="sceneData.studyInfo" :min-height="192" class="bb"></ck-editor>
      </el-form-item>
      <el-form-item label="总结" prop="summary">
        <ck-editor v-model="sceneData.summary" :min-height="192" class="bb"></ck-editor>
      </el-form-item>
    </el-form>
    <el-row :gutter="20" type="flex" justify="center">
      <h2>对话设置</h2>
    </el-row>
    <el-row type="flex" justify="start">
      <el-button style="margin-top: 10px" @click.prevent="handleAdd">添加</el-button>
    </el-row>
    <el-row :gutter="20" v-for="(item,index) in contentList"
            :key="index">
      <el-col :span="10">
        <el-input maxlength="100" rows="2" show-word-limit type="textarea"
                  resize="none" style="margin-top: 10px"
                  v-model="item.senderContent" placeholder="请输入问题"></el-input>
      </el-col>
      <el-col :span="10">
        <el-input maxlength="100" rows="2" show-word-limit type="textarea"
                  resize="none" style="margin-top: 10px"
                  v-model="item.reply" placeholder="请输入回复"></el-input>
      </el-col>
      <el-col :span="4">
        <el-button style="margin-top: 10px" @click.prevent="remove(index)">删除</el-button>
      </el-col>
    </el-row>
    <el-row style="margin-top: 20px" :gutter="20" type="flex" justify="end">
      <el-col :span="10"></el-col>
      <el-col :span="4">
        <el-button type="primary" @click="submit">提交</el-button>
      </el-col>
    </el-row>
  </div>
</template>
<script>

import {VueDraggable} from "vue-draggable-plus";
import {addDialogueScene, updateScene} from "@/api/conversation/scene"
import CkEditor from "@/components/Editor/CKEditor.vue";

export default {
  props: {
    sceneAndDialoguesData: {
      type: Object
    },
  },
  watch: {
    sceneAndDialoguesData(newValue) {
      if (newValue) {
        this.sceneData = newValue
        if (this.sceneData.dialogueDataList && this.sceneData.dialogueDataList.length > 0) {
          this.contentList = this.sceneData.dialogueDataList
        }
      }
    }
  },
  created() {
    this.sceneData = this.sceneAndDialoguesData
    if (this.sceneData.dialogueDataList && this.sceneData.dialogueDataList.length > 0) {
      this.contentList = this.sceneData.dialogueDataList
    }
  },
  computed: {
    showName() {
      return true
    }
  },
  components: {
    CkEditor,
    VueDraggable
  },
  data() {
    return {
      sceneData: {
        name: '',
        introduce: '',
        studyInfo: '',
        summary: '',
        uuid: '',
        dialogueDataList: []
      },
      contentList: []
    }
  },
  methods: {
    submit() {
      for (let i = 0; i < this.contentList.length; i++) {
        this.contentList[i].sortNum = i + 1
      }
      const validList = this.contentList.filter(e => {
        if (e.senderContent || e.reply) {
          return true;
        }
        return false
      });
      if (this.sceneData.uuid) {
        const data = {
          name: this.sceneData.name,
          introduce: this.sceneData.introduce,
          studyInfo: this.sceneData.studyInfo,
          summary: this.sceneData.summary,
          uuid: this.sceneData.uuid,
          dialogueDataList: validList
        };
        updateScene(data).then(res => {
          this.$modal.msgSuccess("修改成功");
          this.$emit("closeAdd")
          this.reset()
        })
      } else {
        const data = {
          name: this.sceneData.name,
          introduce: this.sceneData.introduce,
          studyInfo: this.sceneData.studyInfo,
          summary: this.sceneData.summary,
          dialogueDataList: validList
        }
        addDialogueScene(data).then(res => {
          this.$modal.msgSuccess("新增成功");
          this.$emit("closeAdd")
          this.reset()
        })
      }
    },
    onEnd(event) {
      for (let i = 0; i < this.contentList.length; i++) {
        this.contentList[i].sortNum = i + 1;
      }
    },
    handleAdd() {
      this.contentList.push({
        senderContent: "",
        reply: "",
        sortNum: null,
        uuid: null
      },)
    },
    remove(index) {
      this.contentList.splice(index, 1)
    },
    reset() {
      this.sceneData.name = null
      this.sceneData.introduce = ''
      this.sceneData.studyInfo = ''
      this.sceneData.summary = ''
      this.uuid = null
      this.dialogueDataList = []
      this.contentList = [{
        senderContent: "",
        reply: "",
        sortNum: null,
        uuid: null
      }]
    }
  }
}
</script>
