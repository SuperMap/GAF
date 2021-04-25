<template>
  <div class="monaco">
    <ComponentList @componentChange="handleComponentChange" />
    <EditorContent :component-name="componentName" />
    <EditorResult />
  </div>
</template>

<script>
import ComponentList from '../views/monaco/ComponentList'
import EditorResult from '../views/monaco/EditorResult'
import EditorContent from '../views/monaco/EditorContent'
export default {
  components: {
    ComponentList,
    EditorResult,
    EditorContent,
  },
  data() {
    return {
      componentName: '',
    }
  },
  mounted() {
    try {
      // eslint-disable-next-line no-debugger
      const user = JSON.parse(window.localStorage.getItem('luser'))
      if (user) {
        this.$store.commit('initUser', user)
      }
    // eslint-disable-next-line no-empty
    } catch {}
  },
  methods: {
    handleComponentChange(value) {
      this.componentName = value
    },
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
