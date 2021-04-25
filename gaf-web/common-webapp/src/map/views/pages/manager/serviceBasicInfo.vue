<template>
  <div class="background-color card-container">
    <a-breadcrumb separator=">">
      <a-breadcrumb-item>
        <a @click="$emit('back') && $emit('refresh')">服务列表</a>
      </a-breadcrumb-item>
      <a-breadcrumb-item>服务操作</a-breadcrumb-item>
    </a-breadcrumb>
    <a-tabs v-model="actionKey" type="card" class="background-color">
      <a-tab-pane key="addService" tab="新增" class="background-color">
        <add-map-service />
      </a-tab-pane>
      <a-tab-pane key="editService" tab="编辑" class="background-color">
        <edit-map-service :map-service-info="layerServiceInfo" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script>
// @ts-nocheck
import addMapService from '../../manager/addMapService'
import editMapService from '../../manager/editMapService'
export default {
  components: {
    addMapService,
    editMapService,
  },
  props: {
    layerServiceInfo: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      actionKey: '',
    }
  },
  watch: {
    layerServiceInfo(val) {
      if (val.id) {
        this.actionKey = 'editService'
      } else {
        this.actionKey = 'addService'
      }
    },
  },
}
</script>

<style lang="less" scoped>
.card-container {
  background: #ffffff;
  overflow: hidden;
  padding: 4px;
}
.card-container > .ant-tabs-card > .ant-tabs-content {
  height: 100%;
  margin-top: -16px;
}
.card-container > .ant-tabs-card > .ant-tabs-content > .ant-tabs-tabpane {
  background: #ffffff;
  padding: 6px;
}
.card-container > .ant-tabs-card > .ant-tabs-bar {
  border-color: #000000;
}
.card-container > .ant-tabs-card > .ant-tabs-bar .ant-tabs-tab {
  border-color: transparent;
  background: transparent;
}
.card-container > .ant-tabs-card > .ant-tabs-bar .ant-tabs-tab-active {
  border-color: #f5f5f5;
  background: #f5f5f5;
}
</style>
<style scoped>
.background-color {
  background: #ffffff;
}
</style>
