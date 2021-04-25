<template>
  <div class="ToolbarConfiguration">
    <DragTree @checkedKeys="getCheckedKeys" />
    <div class="buttonGroup">
      <a-button-group>
        <a-button
          v-for="(item, index) in checkedData"
          :key="index"
          :value="item"
          @click="handleToolButtonClick"
          >{{ item }}</a-button
        >
      </a-button-group>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ToolbarConfiguration',
  data() {
    return {
      checkedData: [],
      value: '',
    }
  },
  methods: {
    getCheckedKeys(value) {
      // const a = value.filter(item => {
      //   if (item.length) return item
      // })
      this.checkedData = value
    },
    handleToolButtonClick(e) {
      this.$emit('getToolbarItem', e.target.value)
    },
  },
}
</script>
<style lang="less" scoped>
.ToolbarConfiguration {
  .buttonGroup {
    position: fixed;
    z-index: 1;
    top: 120px;
    right: 20px;
  }
  .ant-checkbox-wrapper {
    display: block;
  }
  .ant-checkbox-wrapper + .ant-checkbox-wrapper {
    margin-left: 0px;
  }
}
</style>
