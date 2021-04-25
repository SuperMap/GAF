<template>
  <div>
    <a-layout>
      <a-layout-sider style="min-height: 100vh">
        <a-menu
          theme="dark"
          mode="inline"
          :default-selected-keys="['mapConfig']"
          @click="menuClick"
        >
          <a-menu-item key="mapConfig">
            <a-icon type="setting" />
            <span>地图配置</span>
          </a-menu-item>
          <a-menu-item key="serviceManager">
            <a-icon type="video-camera" />
            <span>服务管理</span>
          </a-menu-item>
        </a-menu>
      </a-layout-sider>
      <a-layout>
        <div v-show="showType === 'mapConfig'">
          <map-manager />
        </div>
        <div v-show="showType === 'serviceManager'">
          <map-service-config />
        </div>
      </a-layout>
    </a-layout>
  </div>
</template>

<script>
// @ts-nocheck
import mapServiceConfig from './mapServiceConfig'
import mapManager from './mapManager'
export default {
  components: {
    mapServiceConfig,
    mapManager,
  },
  data() {
    return {
      showType: 'mapConfig',
    }
  },
  methods: {
    menuClick(e) {
      this.showType = e.key
    },
  },
}
</script>

<style scoped></style>
