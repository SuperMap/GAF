<template>
  <div id="editorContent" ref="container" class="monaco-editor"></div>
</template>
<script>
// import * as monaco from 'monaco-editor'
import * as monaco from 'monaco-editor/esm/vs/editor/editor.api'
import 'monaco-editor/esm/vs/basic-languages/javascript/javascript.contribution'
import 'monaco-editor/esm/vs/basic-languages/html/html.contribution'
import 'monaco-editor/esm/vs/basic-languages/css/css.contribution'
import 'monaco-editor/esm/vs/editor/contrib/find/findController.js'
export default {
  name: 'Monacoeditor',
  props: {
    // 编辑器中呈现的内容
    codes: {
      type: String,
      default() {
        return ['function x() {', '\tconsole.log("Hello world!");', '}'].join(
          '\n'
        )
      },
    },
    readOnly: {
      type: Boolean,
      default() {
        return false
      },
    },
    language: {
      type: String,
      default() {
        return ''
      },
    },
    // 主要配置
    editorOptions: {
      type: Object,
      default() {
        return {
          selectOnLineNumbers: true,
          roundedSelection: false,
          readOnly: this.readOnly, // 只读
          cursorStyle: 'line', // 光标样式
          automaticLayout: true, // 自动布局
          glyphMargin: true, // 字形边缘
          useTabStops: false,
          fontSize: 28, // 字体大小
          autoIndent: false, // 自动布局
        }
      },
    },
    current: {
      type: Object,
      default: () => {},
    }, // 类型随意，需要保证不同个组件编辑器切换时current值变化
  },
  data() {
    return {
      monacoEditor: '',
    }
  },
  watch: {
    codes() {
      this.monacoEditor.setValue(this.codes)
    },
  },
  mounted() {
    this.monacoEditor = monaco.editor.create(this.$refs.container, {
      value: this.codes, // 见props
      language: this.language,
      theme: 'vs', // 编辑器主题：vs, hc-black, or vs-dark，更多选择详见官网
      editorOptions: this.editorOptions, // 同codes
      automaticLayout: true,
    })
  },
  beforeDestroy() {
    this.dispose()
  },
  methods: {
    dispose() {
      if (this.monacoEditor) {
        if (this.monacoEditor.getModel()) {
          this.monacoEditor.getModel().dispose()
        }
        this.monacoEditor.dispose()
        this.monacoEditor = null
        if (this.provider) {
          this.provider.dispose()
          this.provider = null
        }
      }
    },
    getEditorContent() {
      const self = this
      const newContent = self.monacoEditor.getValue()
      return newContent
    },
  },
}
</script>
<style scoped>
#editorContent {
  width: 100%;
  height: 100%;
}
.monaco-editor {
  width: 100%;
  height: 100%;
}
</style>
