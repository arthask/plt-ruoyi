<template>
  <div id="app">
    <ckeditor :editor="editor" @input="onEditorInput" v-model="editorData" :config="editorConfig" :disabled="editorDisabled"></ckeditor>
  </div>
</template>

<script>
import {ClassicEditor} from '@ckeditor/ckeditor5-editor-classic'
import { Bold, Code, Italic, Strikethrough, Subscript, Superscript, Underline } from '@ckeditor/ckeditor5-basic-styles';
import {Essentials} from '@ckeditor/ckeditor5-essentials'
import {Link} from '@ckeditor/ckeditor5-link'
import {Paragraph} from '@ckeditor/ckeditor5-paragraph'
import {SimpleUploadAdapter} from '@ckeditor/ckeditor5-upload';
import { Table, TableCaption, TableCellProperties, TableColumnResize, TableProperties, TableToolbar } from '@ckeditor/ckeditor5-table';
import { CodeBlock } from '@ckeditor/ckeditor5-code-block';
import {
  AutoImage,
  Image,
  ImageCaption,
  ImageInsert,
  ImageResize,
  ImageStyle,
  ImageToolbar,
  ImageUpload,
  PictureEditing
} from '@ckeditor/ckeditor5-image'

export default {
  name: 'CkEditor',
  props: {
    /* 编辑器的内容 */
    value: {
      type: String,
      default: "",
    },
    /* 高度 */
    height: {
      type: Number,
      default: null,
    },
    /* 最小高度 */
    minHeight: {
      type: Number,
      default: null,
    },
    /* 只读 */
    readOnly: {
      type: Boolean,
      default: false,
    },
  },
  watch: {
    value: {
      handler(val) {
        if (val !== this.editorData) {
          this.editorData = val === null ? "" : val;
        }
      },
      immediate: true,
    },
    readOnly: {
      handler(val) {
        if (val !== this.editorDisabled) {
          this.editorDisabled = val === null ? "" : val;
        }
      },
      immediate: true,
    }
  },
  mounted() {
    this.init();
  },
  data() {
    return {
      editor: ClassicEditor,
      editorData: '<p>Content of the editor.</p>',
      editorDisabled: false,
      editorConfig: {
        language: 'zh-cn',
        plugins: [
          Essentials,
          Bold,
          Italic,
          Link,
          Paragraph,
          Image,
          ImageInsert,
          ImageResize,
          ImageStyle,
          ImageToolbar,
          SimpleUploadAdapter,
          Table, TableToolbar,
          TableCaption, TableCellProperties, TableColumnResize,
          TableProperties,
          Code, CodeBlock,
        ],

        toolbar: {
          items: [
            'bold',
            'italic',
            'link',
            'undo',
            'redo',
            'code',
            'codeBlock',
            'insertTable',
            'insertImage',
          ]
        },
        image: {
          styles: [
            'alignCenter',
            'alignLeft',
            'alignRight'
          ],
          resizeOptions: [
            {
              name: 'resizeImage:original',
              label: 'Original',
              value: null
            },
            {
              name: 'resizeImage:50',
              label: '50%',
              value: '50'
            },
            {
              name: 'resizeImage:75',
              label: '75%',
              value: '75'
            }
          ],
          toolbar: [
            'imageTextAlternative', 'toggleImageCaption', '|',
            'imageStyle:inline', 'imageStyle:wrapText', 'imageStyle:breakText', '|',
            'resizeImage', '|', 'ckboxImageEdit'
          ]
        },
        simpleUpload: {
          // The URL that the images are uploaded to.
          uploadUrl: process.env.VUE_APP_BASE_API+'/common/upload',
        },
        table: {
          contentToolbar: [
            'tableColumn', 'tableRow', 'mergeTableCells', 'tableProperties', 'tableCellProperties', 'toggleTableCaption'
          ]
        },
      }
    }
  }, methods: {
    onEditorInput() {
      console.log(this.editorData)
      this.$emit("input", this.editorData);
    },
    init() {

    },
    getData() {
    }
  }
}
</script>
