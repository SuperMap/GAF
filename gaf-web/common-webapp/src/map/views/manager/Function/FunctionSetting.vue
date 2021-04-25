<template>
  <div ref="FunctionSetting" class="gutter-box settingStyle">
    <div>
      <a-row :gutter="16">
        <a-col :span="18">
          <a-divider orientation="left" style="margin: 0px">设置</a-divider>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="7" :push="1">
          <span>名称：</span>
        </a-col>
        <a-col :span="16">
          <a-input v-model="showFunction.name" size="small" />
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="7" :push="1">
          <span>上级目录：</span>
        </a-col>
        <a-col :span="16">
          <a-select
            v-model="showFunction.parentId"
            allow-clear
            style="width: 100%"
            size="small"
          >
            <a-select-option v-for="item in directoryCatalog" :key="item.id">
              {{ item.name }}
            </a-select-option>
          </a-select>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="7" :push="1">
          <span>顺序：</span>
        </a-col>
        <a-col :span="16">
          <a-input-number
            v-model="showFunction.index"
            size="small"
            style="width: 100%"
          />
        </a-col>
      </a-row>
      <a-row :gutter="0">
        <a-col :span="7" :push="1">
          <span>图标：</span>
        </a-col>
        <a-col :span="13">
          <a-input
            v-model="showFunction.icon"
            disabled
            size="small"
            style="width: 100%"
          />
        </a-col>
        <a-col :span="3">
          <a-button icon="setting" size="small" @click="showIcon = true" />
        </a-col>
      </a-row>
      <div v-show="showFunction.type === 'leaf'">
        <a-row :gutter="16">
          <a-col :span="7" :push="1">
            <span>服务地址：</span>
          </a-col>
          <a-col :span="16">
            <a-input
              v-model="showFunction.actionURL"
              size="small"
              style="width: 100%"
            />
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="23" :push="1">
            <a-radio-group
              v-model="groupType"
              button-style="solid"
              name="dataVisualizationType"
              @change="onChange"
            >
              <a-radio-button value="PropertyTable" class="rbutton">
                属性表
              </a-radio-button>
              <a-radio-button value="HeatMap" class="rbutton">
                热图
              </a-radio-button>
              <a-radio-button value="ClusterLayer" class="rbutton">
                聚合
              </a-radio-button>
              <a-radio-button value="GraphThemeLayer" class="rbutton">
                图表
              </a-radio-button>
              <a-radio-button value="RangeThemeLayer" class="rbutton">
                分段
              </a-radio-button>
              <a-radio-button value="UniqueThemeLayer" class="rbutton">
                单值
              </a-radio-button>
            </a-radio-group>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="24">
            <ClusterLayerSetting
              v-if="dataVisualizationType === 'ClusterLayer'"
              :functioninfo="showFunction"
              @clusterLayerInfo="getVisualizationSetting"
            />
            <GraphThemeLayerSetting
              v-if="dataVisualizationType === 'GraphThemeLayer'"
              :functioninfo="showFunction"
              @graphThemeLayer="getVisualizationSetting"
            />
            <HeatMapSetting
              v-if="dataVisualizationType === 'HeatMap'"
              :functioninfo="showFunction"
              @heatMapInfo="getVisualizationSetting"
            />
            <PropertyTableSetting
              v-if="dataVisualizationType === 'PropertyTable'"
              :functioninfo="showFunction"
              @propertyTableInfo="getVisualizationSetting"
            />
            <RangeThemeLayerSetting
              v-if="dataVisualizationType === 'RangeThemeLayer'"
              :functioninfo="showFunction"
              @rangeThemeLayer="getVisualizationSetting"
            />
            <UniqueThemeLayerSetting
              v-if="dataVisualizationType === 'UniqueThemeLayer'"
              :functioninfo="showFunction"
              @uniqueThemeLayer="getVisualizationSetting"
            />
          </a-col>
        </a-row>
      </div>
    </div>
    <div
      :style="{
        position: 'fixed',
        bottom: 0,
        padding: '10px 0',
        textAlign: 'right',
        right: '16px',
        background: 'rgba(0, 0, 0, 0.0)',
        borderRadius: '0 0 0 0',
        zIndex: 9,
      }"
    >
      <a-button type="primary" size="small" @click="onSubmit"> 确定 </a-button>
    </div>
    <system-icon
      :visible="showIcon"
      @selectedIcon="selectedIcon"
      @closeIconSetting="showIcon = false"
    />
  </div>
</template>

<script>
// @ts-nocheck
import { getFunctionUrl } from '~/utils/GAFMapDataUtils'
import systemIcon from '../systemIcon'
import ClusterLayerSetting from '../DataVisualization/ClusterLayerSetting'
import GraphThemeLayerSetting from '../DataVisualization/GraphThemeLayerSetting'
import HeatMapSetting from '../DataVisualization/HeatMapSetting'
import PropertyTableSetting from '../DataVisualization/PropertyTableSetting'
import RangeThemeLayerSetting from '../DataVisualization/RangeThemeLayerSetting'
import UniqueThemeLayerSetting from '../DataVisualization/UniqueThemeLayerSetting'
export default {
  components: {
    ClusterLayerSetting,
    GraphThemeLayerSetting,
    HeatMapSetting,
    PropertyTableSetting,
    RangeThemeLayerSetting,
    UniqueThemeLayerSetting,
    systemIcon,
  },
  model: {
    prop: 'functioninfo',
    event: 'change',
  },
  props: {
    functioninfo: {
      type: Object,
      required: true,
    },
    mapCode: {
      type: String,
      required: true,
      validator(value) {
        return value
      },
    },
  },
  data() {
    return {
      showFunction: {},
      dataVisualizationType: 'HeatMap',
      dataVisualizationSetting: '',
      directoryCatalog: [],
      showIcon: false,
      groupType: 'HeatMap',
    }
  },
  watch: {
    functioninfo: {
      handler(val) {
        this.showFunction = val
        this.loadVisualizationInfo()
        this.loadDirectoryCatalog()
      },
      immediate: true,
    },
  },
  methods: {
    async onSubmit() {
      if (!this.showFunction.visualizationInfo) {
        this.showFunction.visualizationInfo = {}
        this.showFunction.visualizationInfo.actionId = this.showFunction.id
        this.showFunction.visualizationInfo.name = this.showFunction.name
      }
      this.showFunction.visualizationInfo.visualizationType = this.dataVisualizationType
      this.showFunction.visualizationInfo.dataVisualizationSetting = this.dataVisualizationSetting
      const url = getFunctionUrl(this.mapCode) + `/${this.showFunction.id}`
      const result = await this.$axios.$put(url, this.showFunction)
      if (result.isSuccessed) {
        this.$emit('refresh')
        this.$message.success('操作成功')
      }
    },
    onChange(key) {
      if (key && key.target && key.target.name && key.target.value) {
        this[key.target.name] = key.target.value
      }
    },
    getVisualizationSetting(value) {
      const visualization = { ...value }
      if (visualization.fillOpacity) {
        visualization.fillOpacity = visualization.fillOpacity / 100
      }
      this.dataVisualizationSetting = JSON.stringify(visualization)
    },
    loadVisualizationInfo() {
      if (this.showFunction.visualizationInfo) {
        this.dataVisualizationType = this.showFunction.visualizationInfo.visualizationType
        this.groupType = this.dataVisualizationType
      }
    },
    // 获取所有目录 1
    async loadDirectoryCatalog() {
      const url = getFunctionUrl(this.mapCode)
      const res = await this.$axios.$get(url)
      this.directoryCatalog = []
      if (res && res.data) {
        this.setCatalogToDirectory(this.directoryCatalog, res.data)
      }
    },
    // 获取所有目录 2
    setCatalogToDirectory(list, tree) {
      if (tree !== undefined && tree.length > 0) {
        for (let i = 0; i < tree.length; i++) {
          const item = tree[i]
          if (item.type === 'catalog') {
            list.push(item)
            this.setCatalogToDirectory(list, item.children)
          }
        }
      }
    },
    selectedIcon(icon) {
      this.showFunction.icon = icon
    },
  },
}
</script>

<style scoped>
.gutter-box >>> .ant-row {
  padding: 5px 5px 5px 5px;
}
.settingStyle {
  overflow-y: auto;
  overflow-x: hidden;
}
.span-line {
  word-wrap: break-word;
}
.rbutton {
  width: 80px;
  text-align: center;
}
</style>
