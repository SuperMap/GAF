<template>
  <a-tabs default-active-key="1" class="home-designer-tabs">
    <a-tab-pane key="1" tab="首页设置">
      <home-setting />
    </a-tab-pane>
    <a-tab-pane key="2" tab="门户设置">
      <portal-setting />
    </a-tab-pane>
  </a-tabs>
</template>

<script>
export default {
  components: {
    HomeSetting: () =>
      import('../../views/customization/HomeDesigner'),
    PortalSetting: () =>
      import('../../views/customization/PortalSetting'),
  },
}
</script>

<style lang="less" scoped>
.home-designer-tabs {
  height: 100%;
  .ant-tabs-content,
  .ant-tabs-tabpane {
    height: 100%;
  }
}
</style>
