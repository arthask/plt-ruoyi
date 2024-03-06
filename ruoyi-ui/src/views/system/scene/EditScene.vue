<template>
  <div>
    <el-form ref="form" :model="sceneData" label-width="80px">
      <el-row>
        <el-form-item label="场景名称">
          <el-input v-model="sceneData.name"></el-input>
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item label="场景分类">
          <el-input v-model="sceneData.tag"></el-input>
        </el-form-item>
      </el-row>
    </el-form>
    <el-row :gutter="20" type="flex" justify="center">
      <h2>对话设置</h2>
    </el-row>
    <el-row :gutter="20" type="flex" justify="center">
      <el-col :span="2">
        编号
        <el-input style="margin-top: 10px" v-for="n in senderContentList.length" :value="n" readonly></el-input>
      </el-col>
      <el-col :span="10">
        问题
        <VueDraggable
          v-model="senderContentList"
          animation="500"
          ghostClass="ghost"
          @end="onEnd"
        >
          <el-input style="margin-top: 10px" v-for="(item,index) in senderContentList"
                    :key="`${new Date() + '-'+index}`"
                    v-model="item.senderContent" placeholder="请输入内容"></el-input>
        </VueDraggable>
      </el-col>
      <el-col :span="10">
        回复
        <VueDraggable
          v-model="replyContentList"
          animation="500"
          ghostClass="ghost"
        >
          <el-input style="margin-top: 10px" v-for="(item,index) in replyContentList"
                    :key="`${new Date() + index}`"
                    v-model="item.reply" placeholder="请输入内容"></el-input>
        </VueDraggable>

      </el-col>
    </el-row>
    <el-row style="margin-top: 20px" :gutter="20" type="flex" justify="end">
      <el-col :span="10"></el-col>
      <el-col :span="4">
        <el-button type="primary" @click="add">立即创建</el-button>
      </el-col>
    </el-row>

  </div>
</template>
<script>

import * as VueDraggable from "vuedraggable";
import {addDialogueScene} from "@/api/conversation/scene"

export default {
  components: {
    VueDraggable
  },
  data() {
    return {
      sceneData: {
        name: '',
        tag: 1,
      },
      senderContentList: [
        {senderContent: "hello", sort: 1},
        {senderContent: "hello2", sort: 2},
        {
          senderContent: "nice to meet you1",
          sortNum: 1
        },
        {senderContent: "nice to meet you1", sort: 1}
      ],
      replyContentList: [
        {reply: "nice to meet you", sort: 1},
        {
          reply: "nice to meet you1",
          sort: 1
        },
        {reply: "nice to meet you1", sort: 1}, {reply: "nice to meet you1", sort: 1}
      ]
    }
  },
  methods: {
    async add() {
      for (let i = 0; i < this.senderContentList.length; i++) {
        this.senderContentList[i].sortNum = i + 1
        this.senderContentList[i].reply = this.replyContentList[i].reply
      }
      var data = {
        name: this.sceneData.name,
        tagId: this.sceneData.tag,
        dialogueDataList: this.senderContentList
      }
      await addDialogueScene(data).then(res => {
        this.$modal.msgSuccess("新增成功");
        this.$emit("closeAdd")
      })
    },
    onEnd(event) {
      console.log(this.senderContentList)
      for (let i = 0; i < this.senderContentList.length; i++) {
        this.senderContentList[i].sortNum = i + 1;
      }
    }
  }
}
</script>
