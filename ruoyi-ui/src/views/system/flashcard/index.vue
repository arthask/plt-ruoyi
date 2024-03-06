<script>
import { getNoteInfo } from "@/api/system/note";
import { updateNoteInfo } from "@/api/system/note";
import CardComponent from "@/views/system/flashcard/CardComponent.vue";

export default {
  components: {
    CardComponent
  },
  props: {
    noteId: {
      type: Number,
      default: 0
    },
  },
  watch: {

  },
  data() {
    return {

    };
  },
  created() {
  },
  methods: {

  }
}
</script>

<template>
  <el-container>
    <el-header>
    </el-header>
    <el-main style="width: 80%;height: 500px">
      <card-component></card-component>
    </el-main>
    <el-footer>
    </el-footer>
  </el-container>
</template>

<style scoped lang="scss">

</style>
