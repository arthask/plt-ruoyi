<template>
  <div>
    <el-form ref="form" :model="sceneData" label-width="80px" v-if="showName">
      <el-row>
        <el-form-item label="场景名称">
          <el-input v-model="sceneData.name"></el-input>
        </el-form-item>
      </el-row>
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
        <el-input style="margin-top: 10px" v-model="item.senderContent" placeholder="请输入问题"></el-input>
      </el-col>
      <el-col :span="10">
        <el-input style="margin-top: 10px" v-model="item.reply" placeholder="请输入回复"></el-input>
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
      if (this.sceneData.uuid) {
        return true
      }
    }
  },
  components: {
    VueDraggable
  },
  data() {
    return {
      sceneData: {
        name: '',
        uuid: '',
        dialogueDataList: []
      },
      contentList: [
        {
          senderContent: "",
          reply: "",
          sortNum: null,
          uuid: null
        },
      ]
    }
  },
  methods: {
    submit() {
      for (let i = 0; i < this.contentList.length; i++) {
        this.contentList[i].sortNum = i + 1
      }
      var validList = this.contentList.filter(e => {
        if (e.senderContent || e.reply) {
          return true;
        }
        return false
      });
      if (this.sceneData.uuid) {
        const data = {
          name: this.sceneData.name,
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
          name: validList[0].senderContent,
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
      console.log(this.contentList)
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
      if (this.contentList.length === 1) {
        this.$modal.msgWarning("再删除就没有了呢");
        return
      }
      this.contentList.splice(index, 1)
    },
    reset() {
      this.sceneData.name = null
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
