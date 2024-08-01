<template>
  <div>
    <el-row type="flex" justify="center">
      <el-col :span="10">
        <div style="margin: 20px;">
          <i class="el-icon-search" style="margin-right: 10px"></i>
          <el-select
            size="medium"
            v-model="value"
            filterable
            remote
            placeholder="搜索单词"
            :remote-method="remoteMethod"
            :loading="loading"
            @change="showWordDetail"
          >
            <el-option
              v-for="item in options"
              :key="item.id"
              :label="item.word+' '+item.translation"
              :value="item">
            </el-option>
          </el-select>
          <el-button v-show="showDetail" style="margin-left: 10px" type="primary"
                     size="small" @click="collectWord(detail.uuid)">收藏</el-button>
        </div>
      </el-col>
    </el-row>
    <el-row type="flex" justify="center" v-show="showDetail">
      <el-col :span="20">
        <el-descriptions class="margin-top" title="详情" :column="2" size="medium" border>
          <el-descriptions-item>
            <template slot="label">
              词典
            </template>
            {{ detail.lexiconName }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              标签
            </template>
            {{ detail.labelNames }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              单词
            </template>
            {{ detail.name }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              解释
            </template>
            {{ detail.translation }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              美式发音
            </template>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-microphone"
              @click="playAudio(US,detail.name)"
            >播放</el-button>
          </el-descriptions-item>
          <el-descriptions-item>
            <template slot="label">
              英式发音
            </template>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-microphone"
              @click="playAudio(UK,detail.name)"
            >播放</el-button>
          </el-descriptions-item>
        </el-descriptions>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { searchWordByCN} from "@/api/bussiness/word";
import {collect} from "@/api/bussiness/userword";

export default {
  name: 'searchWord',
  mounted() {
    // this.speakCommon.speechInit();
  },
  data() {
    return {
      UK: 'en-GB',
      US: 'en-US',
      options: [],
      value: '',
      list: [],
      loading: false,
      showDetail: false,
      detail: {
        name: '',
        translation: ''
      }
    }
  }, methods: {
    remoteMethod(query) {
      if (query !== '') {
        this.loading = true;
        let params = {
          searchCn: query
        }
        searchWordByCN(params).then((res) => {
          this.options = []
          this.options = [...res.data]
          this.loading = false;
        })
      } else {
        this.options = [];
      }
    },
    showWordDetail(selected) {
      this.showDetail = true;
      this.value = selected.word;
      this.detail.name = selected.word
      this.detail.translation = selected.translation
      this.detail.labelNames = [... selected.labelList]
      this.detail.lexiconName = [... selected.lexiconName]
      this.detail.uuid = selected.uuid
    },
    // 播放音频
    playAudio(language, word) {
      this.speakCommon.changeVoice(language)
      this.speakCommon.speak(word)
    },
    collectWord(uuid) {
      var data = new FormData()
      data.append("wordUUID", uuid)
      collect(data).then((res) => {
        console.log(res)
        this.$notify({
          title: '成功',
          message: '已收藏',
          type: 'success'
        });
      })
    }
  }
}
</script>
