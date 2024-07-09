<script>

export default {
  name: "ExpressionDetail.vue",
  props: {
    items: {
      type: Array,
      default: [],
    },
  },
  data() {
    return {}
  },
  methods: {
    // 播放音频
    playAudio(word) {
      this.speakCommon.speak(word)
    },
  }
}
</script>

<template>
  <div class="block">
    <el-timeline :reverse="false">
      <el-timeline-item
        v-for="(activity, index) in items"
        :key="activity.uuid"
        :timestamp="activity.createTime" placement="top">
        <el-card>
          <h4>{{ activity.content }}
            <el-button
              size="mini"
              type="text"
              icon="el-icon-play"
              @click="playAudio(activity.content)"
            >播放
            </el-button>
          </h4>
          <p>修改于 {{ activity.updateTime }}</p>
        </el-card>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<style scoped lang="scss">

</style>
