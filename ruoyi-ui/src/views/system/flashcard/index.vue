<template>
  <div id="app">
    <ck-editor :value="editorValue"></ck-editor>
  </div>
</template>

<script>
import CkEditor   from "@/components/Editor/CKEditor.vue";
export default {
  components: {
    CkEditor
  },
  name: 'FlashCard',
  mounted() {
  },
  data() {
    return {
        editorValue:'hello world'
      }
  }, methods: {
    getData() {

    }
  }
}
</script>
