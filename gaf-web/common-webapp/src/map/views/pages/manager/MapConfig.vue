<template>
  <div>
    <a-layout>
      <a-layout-sider
        v-model="collapsed"
        :collapsed-width="80"
        :width="300"
        :style="siderStyle"
        :trigger="null"
        collapsible
        theme="light"
      >
        <a-layout>
          <a-layout-content class="layout-overflow">
            <a-row>
              <a-col :offset="2" :span="22">
                <a href="javascript:;" @click="() => goback()">
                  <a-icon type="arrow-left" width="10px" />
                  <span style="font-size: small">返回</span>
                </a>
              </a-col>
            </a-row>
            <a-tabs
              tab-position="left"
              :style="tabStyle"
              @tabClick="onTabClick"
              @change="tabsChange"
            >
              <a-tab-pane
                key="baseLayer"
                style="background: #ffffff"
                class="closeDiv"
              >
                <span slot="tab">
                  <a-row>
                    <a-col :span="12" :offset="6">
                      <a-icon type="gateway" />
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12" :offset="3">
                      <span>基础</span>
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12" :offset="3">
                      <span>底图</span>
                    </a-col>
                  </a-row>
                </span>
              </a-tab-pane>
              <a-tab-pane
                key="catalog"
                style="background: #ffffff"
                :class="getDivClass"
              >
                <span slot="tab">
                  <a-row>
                    <a-col :span="12" :offset="6">
                      <a-icon type="share-alt" />
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12" :offset="3">
                      <span>图层</span>
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12" :offset="3">
                      <span>目录</span>
                    </a-col>
                  </a-row>
                </span>
                <map-catalog-config
                  :refresh="catalogRefresh"
                  :map-code="mapCode"
                  @onTreeNodeClick="treeNodeClick"
                  @editCatalogStyle="editCatalog"
                />
              </a-tab-pane>
              <a-tab-pane
                key="toolbar"
                style="background: #ffffff"
                :class="getDivClass"
              >
                <span slot="tab">
                  <a-row>
                    <a-col :span="12" :offset="6">
                      <a-icon type="bars" />
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12" :offset="3">
                      <span>地图</span>
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12" :offset="3">
                      <span>工具</span>
                    </a-col>
                  </a-row>
                </span>
                <div>
                  <map-tool-config
                    :refresh="toolRefresh"
                    :map-code="mapCode"
                    @onToolMenuClick="toolMenuClick"
                    @editToolbarStyle="editToolbar"
                  />
                </div>
              </a-tab-pane>
              <a-tab-pane
                key="location"
                style="background: #ffffff"
                :class="getDivClass"
              >
                <span slot="tab">
                  <a-row>
                    <a-col :span="12" :offset="6">
                      <a-icon type="environment" />
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12" :offset="3">
                      <span>定位</span>
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12" :offset="3">
                      <span>配置</span>
                    </a-col>
                  </a-row>
                </span>
                <layer-location-setting
                  :refresh="locRefresh"
                  :map-code="mapCode"
                  @selectedDataService="locationChanged"
                />
              </a-tab-pane>
              <a-tab-pane
                key="functionCatalog"
                style="background: #ffffff"
                :class="getDivClass"
              >
                <span slot="tab">
                  <a-row>
                    <a-col :span="12" :offset="6">
                      <a-icon type="bars" />
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12" :offset="3">
                      <span>功能</span>
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12" :offset="3">
                      <span>目录</span>
                    </a-col>
                  </a-row>
                </span>
                <div>
                  <function-catalog
                    :refresh="functRefresh"
                    :map-code="mapCode"
                    @onMenuClick="showFunctionSetting"
                    @editFunctionStyle="editFunction"
                  />
                </div>
              </a-tab-pane>
              <a-tab-pane
                key="baseControl"
                style="background: #ffffff"
                :class="getDivClass"
              >
                <span slot="tab">
                  <a-row>
                    <a-col :span="12" :offset="6">
                      <a-icon type="gateway" />
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12" :offset="3">
                      <span>基础</span>
                    </a-col>
                  </a-row>
                  <a-row>
                    <a-col :span="12" :offset="3">
                      <span>控件</span>
                    </a-col>
                  </a-row>
                </span>
                <div>
                  <base-control-catalog
                    :refresh="baseControlRefresh"
                    :map-code="mapCode"
                    @onBaseControlClick="baseControlClick"
                  />
                </div>
              </a-tab-pane>
            </a-tabs>
          </a-layout-content>
          <a-layout-sider
            :width="0"
            :style="layoutStyle"
            :trigger="null"
            theme="light"
          >
            <div class="toggle-wrap">
              <a-icon
                v-show="!editBaseLayer"
                class="toggle-left-btn"
                :type="collapsed ? 'right' : 'left'"
                @click="expendedChanger"
              />
            </div>
          </a-layout-sider>
        </a-layout>
      </a-layout-sider>
      <a-layout-content :style="mapLayoutStyle">
        <div v-if="editBaseLayer">
          <base-layer-setting :map-code="mapCode" />
        </div>
        <div v-if="!editBaseLayer">
          <gaf-map
            :map-code="mapCode"
            :width="width"
            :height="height"
            :center-point="centerPoint"
          />
        </div>
      </a-layout-content>
      <a-layout-sider
        v-model="rightCollapsed"
        :collapsed-width="1"
        :width="300"
        :style="siderStyle"
        :trigger="null"
        collapsible
        theme="light"
      >
        <a-layout>
          <a-layout-sider :width="1" :style="layoutStyle" theme="light">
            <div class="toggle-wrap">
              <a-icon
                v-show="!editBaseLayer"
                class="toggle-right-btn"
                :type="rightCollapsed ? 'left' : 'right'"
                @click="expendedEdit"
              />
            </div>
          </a-layout-sider>
          <a-layout-content v-show="!rightCollapsed" :style="contentStyle">
            <div>
              <controls-config
                v-show="
                  editType === 'catalog' ||
                  editType === 'toolbar' ||
                  editType === 'functionEdit'
                "
                :control-info="controlInfo"
                :map-code="mapCode"
              />
              <layer-catalog-info
                v-show="editType === 'treeNode'"
                :layer-catalog="layerCatalogInfo"
                :map-code="mapCode"
                @refresh="layerCatalogRefresh"
              />
              <tool-edit
                v-show="editType === 'toolItem'"
                :map-tool-info="toolInfo"
                :map-code="mapCode"
                @refresh="mapToolRefresh"
              />
              <layer-loc-det-setting
                v-show="editType === 'location'"
                :location-info="locationInfo"
                :map-code="mapCode"
                @refresh="locationRefresh"
              />
              <functoin-setting
                v-show="editType === 'function'"
                :functioninfo="functioninfo"
                :map-code="mapCode"
                @refresh="functionRefresh"
              />
              <base-control-setting
                v-show="editType === 'baseControl'"
                :control-info="baseControlInfo"
                :map-code="mapCode"
                @refresh="BaseConRefresh"
              />
            </div>
          </a-layout-content>
        </a-layout>
      </a-layout-sider>
    </a-layout>
  </div>
</template>

<script>
// @ts-nocheck
import {
  getMapControlUrl,
  getFunctionUrl,
  getMapToolUrl,
} from '~/utils/GAFMapDataUtils'
import gafMap from '../../map/classic/GafMap'
import controlsConfig from '../../manager/controlsConfig'
import layerCatalogInfo from '../../manager/MapCatalog/catalogLayerConfig'
import toolEdit from '../../manager/MapTool/toolEdit'
import layerLocationSetting from '../../manager/Location/layerLocationSetting'
import layerLocDetSetting from '../../manager/Location/layerLocationDetailedSetting'
import FunctionCatalog from '../../manager/Function/functionCatalog'
import FunctoinSetting from '../../manager/Function/FunctionSetting'
import mapCatalogConfig from '../../manager/MapCatalog/mapCatalogConfig'
import mapToolConfig from '../../manager/MapTool/mapToolConfig'
import BaseLayerSetting from '../../manager/BaseLayer/baseLayerSetting'
import BaseControlCatalog from '../../manager/BaseControl/BaseControlCatalog'
import BaseControlSetting from '../../manager/BaseControl/BaseControlSetting'
export default {
  components: {
    gafMap,
    controlsConfig,
    layerCatalogInfo,
    toolEdit,
    layerLocationSetting,
    layerLocDetSetting,
    FunctionCatalog,
    FunctoinSetting,
    mapCatalogConfig,
    mapToolConfig,
    BaseLayerSetting,
    BaseControlCatalog,
    BaseControlSetting,
  },
  data() {
    const { contentWidth, contentHeight } = this.$store.state
    return {
      mapCode: `${this.$route.query.code}`,
      treeData: [],
      showPane: false,
      showRight: false,
      collapsed: true,
      rightCollapsed: true,
      controlInfo: {},
      layerCatalogInfo: {},
      editType: '',
      selectedKeys: [],
      toolInfo: {},
      newMapToolInfo: {},
      selectedToolKey: '',
      selectionInfo: {},
      locationInfo: {},
      baseControlInfo: {},
      expandTitle: '展开',
      refresh: false,
      catalogRefresh: false,
      toolRefresh: false,
      selectRefresh: false,
      locRefresh: false,
      functRefresh: false,
      baseControlRefresh: false,
      baseRefresh: false,
      width: contentWidth,
      height: contentHeight,
      centerPoint: { x: 0, y: 0 },
      functioninfo: {},
      editBaseLayer: false,
    }
  },
  computed: {
    getDivClass() {
      return this.showPane ? 'openDiv' : 'closeDiv'
    },
    mapStyle() {
      const { contentWidth, contentHeight } = this.$store.state
      return `${contentWidth}-${contentHeight}`
    },
    siderStyle() {
      return `height:${this.contentHeight}px;`
    },
    tabStyle() {
      return `width:${this.collapsed ? 80 : 300}px;height:${this.height - 22}px`
    },
    layoutStyle() {
      return `height:${this.height}px;`
    },
    contentStyle() {
      return `height:${this.height}px;background-color:#ffffff;overflow-y:auto;overflow-x:hidden;`
    },
    mapLayoutStyle() {
      return `max-width:${this.width}px;background-color:#ffffff;`
    },
  },
  watch: {
    mapStyle: {
      handler() {
        this.updateMapWidthHeight(this.collapsed, this.rightCollapsed)
      },
      immediate: true,
    },
    collapsed: {
      handler(value) {
        this.updateMapWidthHeight(value, this.rightCollapsed)
      },
      immediate: true,
    },
    rightCollapsed: {
      handler(value) {
        this.updateMapWidthHeight(this.collapsed, value)
      },
      immediate: true,
    },
  },
  methods: {
    updateMapWidthHeight(lCollapsed, rCollapsed) {
      const { contentWidth, contentHeight } = this.$store.state
      this.height = contentHeight
      if (lCollapsed && rCollapsed) {
        this.width = contentWidth - 81
      } else if (lCollapsed && !rCollapsed) {
        this.width = contentWidth - 380
      } else if (!lCollapsed && rCollapsed) {
        this.width = contentWidth - 301
      } else if (!lCollapsed && !rCollapsed) {
        this.width = contentWidth - 600
      }
    },
    tabsChange(activeKey) {
      if (!this.rightCollapsed) {
        if (activeKey === 'catalog') {
          this.editCatalog()
        } else if (activeKey === 'toolbar') {
          this.editToolbar()
        } else if (activeKey === 'functionCatalog') {
          this.editFunction()
        }
      }
      if (activeKey === 'baseLayer') {
        this.editBaseLayer = true
      } else {
        this.editBaseLayer = false
      }
    },
    expendedChanger() {
      this.collapsed = !this.collapsed
      if (this.collapsed) {
        this.showPane = false
      } else {
        this.showPane = true
      }
    },
    onTabClick(activeKey) {
      if (activeKey === 'baseLayer') {
        this.editBaseLayer = true
        this.rightCollapsed = true
        this.expandTitle = '展开'
      } else {
        this.openPane()
        this.editBaseLayer = false
      }
    },
    openPane() {
      this.showPane = true
      this.collapsed = false
    },
    // 图层目录设置
    editCatalog() {
      const configCode = 'layercatalog'
      this.getMapControls(configCode)
      this.editType = 'catalog'
      this.openEdit()
    },
    // 地图工具条设置
    editToolbar() {
      const configCode = 'maptool'
      this.getMapControls(configCode)
      this.editType = 'toolbar'
      this.openEdit()
    },
    editFunction() {
      const configCode = 'FunctionMenu'
      this.getMapControls(configCode)
      this.editType = 'functionEdit'
      this.openEdit()
    },
    async getMapControls(configCode) {
      const url = getMapControlUrl(this.mapCode) + `/${configCode}`
      const res = await this.$axios.$get(url)
      if (res && res.data) {
        const info = res.data
        if (!info.type) {
          info.type = configCode
        }
        this.controlInfo = info
      } else {
        const info = {}
        info.type = configCode
        this.controlInfo = info
      }
    },
    openEdit() {
      this.rightCollapsed = false
    },
    expendedEdit() {
      this.rightCollapsed = !this.rightCollapsed
    },
    treeNodeClick(value) {
      this.layerCatalogInfo = value
      this.editType = 'treeNode'
      this.openEdit()
    },
    async toolMenuClick(value) {
      const url = getMapToolUrl(this.mapCode) + `/${value}`
      const res = await this.$axios.$get(url)
      this.toolInfo = res.data
      this.editType = 'toolItem'
      this.openEdit()
    },
    // 点查询切换
    selectionChanged(value) {
      this.selectionInfo = value
      this.editType = 'selection'
      this.openEdit()
    },
    locationChanged(value) {
      this.locationInfo = value
      this.editType = 'location'
      this.openEdit()
    },
    baseControlClick(value) {
      this.baseControlInfo = value
      this.editType = 'baseControl'
      this.openEdit()
    },
    // tool编辑后刷新左侧导航栏
    async showFunctionSetting(item) {
      this.editType = 'function'
      const url = getFunctionUrl(this.mapCode) + `/${item.key}`
      const res = await this.$axios.$get(url)
      this.functioninfo = { ...res.data }
      this.openEdit()
    },
    layerCatalogRefresh() {
      this.catalogRefresh = !this.catalogRefresh
    },
    mapToolRefresh() {
      this.toolRefresh = !this.toolRefresh
    },
    selectionRefresh() {
      this.selectRefresh = !this.selectRefresh
    },
    locationRefresh() {
      this.locRefresh = !this.locRefresh
    },
    functionRefresh() {
      this.functRefresh = !this.functRefresh
    },
    BaseConRefresh() {
      this.baseRefresh = !this.baseRefresh
    },
    goback() {
      this.$router.replace('../manager')
    },
  },
}
</script>

<style scoped>
.closeDiv {
  left: -350px;
  background: #f5f5f5;
  width: 0;
  height: 0;
  /*overflow: hidden;*/
}
.openDiv {
  left: 0px;
  background: #f5f5f5;
}
.layout-overflow {
  background-color: #ffffff;
  overflow-y: auto;
  overflow-x: hidden;
}
.Absolute-Center {
  background-color: #f5f5f5;
  position: relative;
  top: 45%;
}
.trigger {
  line-height: 128px;
  cursor: pointer;
  transition: color 0.3s;
}
.trigger:hover {
  color: #1890ff;
}
.toggle-wrap {
  width: 1px;
  height: 100%;
  position: absolute;
  top: 0;
}
.toggle-wrap > .toggle-left-btn {
  width: 15px;
  height: 156px;
  position: absolute;
  top: calc(50% - 78px);
  line-height: 150px;
  text-align: center;
  background-color: #bdcbd5;
  border-radius: 0 10px 10px 0;
  cursor: pointer;
  z-index: 9;
}
.toggle-wrap > .toggle-right-btn {
  width: 15px;
  height: 156px;
  position: absolute;
  top: calc(50% - 78px);
  line-height: 150px;
  text-align: center;
  background-color: #bdcbd5;
  border-radius: 10px 0 0 10px;
  left: -15px;
  cursor: pointer;
  z-index: 9;
}
</style>
