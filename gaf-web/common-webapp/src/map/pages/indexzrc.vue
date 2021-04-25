<template>
  <div class="monaco">
    <EditorWebgisApp gis-app-alise="zrctest1" />
    <EditorResult />
  </div>
</template>

<script>
import EditorResult from '../views/monaco/EditorResult'
import EditorWebgisApp from '../views/monaco/EditorWebgisApp'
export default {
  components: {
    EditorResult,
    EditorWebgisApp,
  },
}
</script>
<style lang="less" scoped>
.monaco {
  width: 100%;
  height: 100%;
  display: flex;
  flex-flow: row nowrap;
  justify-content: stretch;
}
</style>
