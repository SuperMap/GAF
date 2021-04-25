<template>
  <div :style="dStyle" class="designer-container">
    <div :class="lCollapsed ? 'control mini' : 'control'">
      <gaf-minimum @toggle="handleLeftToggle" />
      <gaf-controls v-show="!lCollapsed" :list="controls" />
    </div>
    <div class="designer">
      <gaf-designer />
    </div>
    <div
      v-if="showPropertyPanel"
      :class="rCollapsed ? 'property mini' : 'property'"
    >
      <gaf-minimum position="right" @toggle="handleRightToggle" />
      <gaf-properties v-show="!rCollapsed" :item="item" />
    </div>
  </div>
</template>

<script>
import extendCtrls from '../configInfo/componentAll.js'
import GafMinimum from './Designer/GafMinimum'
import GafControls from './Designer/GafControls'
import GafDesigner from './Designer/GafDesigner'
import GafProperties from './Designer/GafProperties'
import controls from './homeDesignerConfig'
export default {
  name: 'HomeDesigner',
  provide() {
    return {
      updateProperties: this.updateProperties,
    }
  },
  components: {
    GafMinimum,
    GafControls,
    GafDesigner,
    GafProperties,
  },
  props: {
    height: {
      type: Number,
      default: 0,
    },
    width: {
      type: Number,
      default: 0,
    },
  },
  data() {
    if (!this.$store.state.isDev) {
      
      const index = extendCtrls.findIndex(
        (ec) => ec.name.toLowerCase() === 'test'
      )
      extendCtrls.splice(index, 1)
    }
    if (extendCtrls.length > 0) {
      controls.push({
        type: 'extend',
        title: '扩展控件',
        controls: extendCtrls,
      })
    }
    return {
      lCollapsed: false,
      rCollapsed: false,
      item: {},
      controls,
    }
  },
  computed: {
    dStyle() {
      let style = ''
      style += this.width !== 0 ? `width:${this.width}px;` : ''
      style += this.height !== 0 ? `height:${this.height}px;` : ''
      return style
    },
    showPropertyPanel() {
      return Object.keys(this.item || {}).length > 0
    },
  },
  methods: {
    handleLeftToggle(collapsed) {
      this.lCollapsed = collapsed
    },
    handleRightToggle(collapsed) {
      this.rCollapsed = collapsed
    },
    updateProperties(item) {
      this.item = item
    },
  },
}
</script>

<style scoped lang="less">
.animated {
  transition-property: width;
  transition-duration: 0.2s;
  transition-timing-function: cubic-bezier(0, 0, 0.2, 1);
}
.designer-container {
  display: flex;
  position: relative;
  width: 100%;
  height: 100%;
  & > .designer {
    flex: 1;
    position: relative;
    height: 95%;
    height: 85vh;
    overflow: auto;
  }
  & > .control:extend(.animated) {
    width: 200px;
    border-right: 1px solid #e8e8e8;
    position: relative;
    height: 95%;
    height: calc(100% - 50px);
  }
  & > .property:extend(.animated) {
    width: 300px;
    border-left: 1px solid #e8e8e8;
    position: relative;
    height: 95%;
    height: calc(100% - 50px);
  }
  & > .mini {
    width: 14px;
    box-sizing: content-box;
  }
}
</style>
