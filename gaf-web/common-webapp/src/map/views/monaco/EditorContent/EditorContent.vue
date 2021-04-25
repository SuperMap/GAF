<template>
  <div :class="fullScreen ? 'fullScreen' : 'EditorContent '">
    <div class="buttonGroup">
      <button class="runButton" @click="runAllCodes">运行</button>
      <button class="EditoeScreenButton" @click="handleScreen">
        <a-icon v-if="fullScreen" type="fullscreen-exit" />
        <a-icon v-else type="fullscreen" />
      </button>
    </div>
    <div id="online-box">
    <a-tabs v-model="activeKey">
      <a-tab-pane key="html" tab="html">
        <monacoeditor
          ref="htmlmonaco"
          language="html"
          :codes="htmlContent"
          :class="fullScreen ? '1' : '2'"
      /></a-tab-pane>
      <a-tab-pane key="javascript" tab="js">
        <monacoeditor
          ref="jsmonaco"
          language="javascript"
          :codes="jsContent"
          :class="fullScreen ? '1' : '2'"
        />
      </a-tab-pane>
      <a-tab-pane key="css" tab="css">
        <monacoeditor
          ref="cssmonaco"
          language="css"
          :codes="cssContent"
          :class="fullScreen ? '1' : '2'"
      /></a-tab-pane>
    </a-tabs>
    </div>
  </div>
</template>

<script>
// @ts-nocheck
import component from './componentData.js'
export default {
  props: {
    componentName: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      fullScreen: false,
      activeKey: 'javascript',
      htmlContent: '<gaf-map-viewer />',
      cssContent: '',
      jsContent: `export default {
    name:"GafMapViewer",
    data(){
        return {
        }
    },
    methods:{}
}`,
    }
  },
  watch: {
    componentName() {
      const self = this
      this.updataComponentData()
      this.$nextTick(function () {
        self.runAllCodes()
      })
    },
  },
  mounted() {
    this.runAllCodes()
  },

  methods: {
    handleScreen() {
      this.fullScreen = !this.fullScreen
    },
    runAllCodes() {
      this.getValue()
      let jsContent = this.jsContent
      jsContent = jsContent.replace('export default', '').trim()
      jsContent = 'START' + jsContent + 'END'
      jsContent = jsContent.replace('START{', '').replace('}END', '')
      const htmlContent = this.htmlContent.replace(/[\r\n]/g, '')
      const cssContent = this.cssContent
      const strContent =
        `
        new Vue({
          el: '#app',
          template: '` +
        htmlContent +
        `',
          ${jsContent}
        })
      `
      const ifr = document.getElementById('preview')
      ifr.srcdoc =
        `<html la` +
        `ng="en">
        <head>
          <meta charset="UTF-8" />
          <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
          <meta name="viewport" content="width=device-width, initial-scale=1.0" />
          <title>WebGIS</title>
          <link href="./iframe/antd.min.css" rel="stylesheet" />
          <link href="./webGL/Cesium/Widgets/widgets.css" rel="stylesheet" />
          <link href="./iframe/css/font_577982_beimm7aom3g.css" rel="stylesheet" />
          <link href="./iframe/css/geoFont/iconfont.css" rel="stylesheet" />
          <style>
         ${cssContent}
          </style>
          <link rel="stylesheet" href="./iframe/iview.css">
          <script src="./iframe/vue.min.js"></sc` +
        `ript>
          <script src="./iframe/iview.js"></sc` +
        `ript>
          <script src="./webGL/Cesium/Cesium.js"></s` +
        `cript>
          <script src="./iframe/antd.min.js"></sc` +
        `ript>
          <script src="./iframe/gafmapui.umd.min.js"></sc` +
        `ript>
          <script src="./iframe/vue-iclient3d-webgl.min.js"></s` +
        `cript>
          <script src="./iframe/echarts.min.js"></sc` +
        `ript>
          <script src="./iframe/tooltip.js"></scr` +
        `ipt>
          <script src="./iframe/axios.min.js"></sc` +
        `ript>
          <script src="./classic/SuperMap.Include.js"></sc` +
        `ript>
        </head>
        <body>
          <div id="app"></div>
          <script>
              ${strContent}
          </scr` +
        `ipt>
        </body>
      </html>
      `
    },
    getValue() {
      if (this.$refs.htmlmonaco) {
        this.htmlContent = this.$refs.htmlmonaco.getEditorContent()
      }
      if (this.$refs.cssmonaco) {
        this.cssContent = this.$refs.cssmonaco.getEditorContent()
      }
      if (this.$refs.jsmonaco) {
        this.jsContent = this.$refs.jsmonaco.getEditorContent()
      }
    },
    updataComponentData() {
      const componentDataS = component[this.componentName]

      if (componentDataS) {
        this.htmlContent = componentDataS.html
        this.jsContent = componentDataS.js
        this.cssContent = componentDataS.css
      }
    },
  },
}
</script>
<style lang="less" scoped>
.fullScreen {
  width: 100vw;
  height: 100vh;
  position: absolute;
  z-index: 1;
  background: white;
}
.EditorContent {
  position: relative;
  flex: 1;
  width: 40%;
  border-left: 2px solid #efefef;
  border-right: 2px solid #efefef;
}
#cesiumContainer {
  position: unset;
}

.buttonGroup {
  position: absolute;
  width: 50%;
  line-height: 40px;
  height: 39px;
  z-index: 1;
  left: 50%;
  .runButton {
    margin-top: 5px;
    line-height: 32px;
    color: white;
    height: 32px;
    width: 100px;
    border: 1px solid transparent;
    border-radius: 3px;
    background-color: rgb(190, 190, 190);
    cursor: pointer;
  }
  .EditoeScreenButton {
    margin-top: 5px;
    line-height: 32px;
    font-weight: bolder;
    color: white;
    height: 32px;
    width: 32px;
    border: 1px solid transparent;
    border-radius: 3px;
    background-color: rgb(170, 170, 170);
    cursor: pointer;
  }
  }
.ant-tabs {
  height: 100%;
}
.ant-tabs .ant-tabs-top-content,
.ant-tabs .ant-tabs-bottom-content {
  width: 100%;
  height: 100%;
}
.ant-tabs-nav .ant-tabs-tab {
  margin: 0 0 0 0;
}
.ant-tabs .ant-tabs-top-content > .ant-tabs-tabpane,
.ant-tabs .ant-tabs-bottom-content > .ant-tabs-tabpane {
  width: 100%;
  height: 100%;
}

/deep/#online-box .ant-tabs-top-content {
    height: 91vh;
}
</style>
