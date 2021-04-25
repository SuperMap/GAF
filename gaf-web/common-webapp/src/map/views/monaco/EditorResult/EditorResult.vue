<template>
  <div :class="fullScreen ? 'fullScreenResult' : 'EditorResult'">
    <button class="screenButton" @click="handleScreen">
      <a-icon v-if="fullScreen" type="fullscreen-exit" />
      <a-icon v-else type="fullscreen" />
    </button>
    <iframe id="preview" class="view-panel" frameborder="0"> </iframe>
  </div>
</template>

<script>
export default {
  data() {
    return {
      fullScreen: false,
    }
  },
  methods: {
    handleScreen() {
      this.fullScreen = !this.fullScreen
    },
  },
}
</script>
<style lang="less" scoped>
.fullScreenResult {
  position: absolute;
  width: 100vw;
  height: 100vh;
  background: white;
  z-index: 3;
}
.EditorResult {
  flex: 1;
  background: white;
  position: relative;
}
.screenButton {
  position: absolute;
  top: 10px;
  left: 10px;
}
.view-panel {
  width: 100%;
  height: 100%;
}
</style>
