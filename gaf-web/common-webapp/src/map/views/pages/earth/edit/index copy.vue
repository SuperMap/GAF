<template>
  <div id="app">
    <div class="buttonGroup">
      <div class="buttonItem" @click="clickBaseExchange('baseMap')">
        <a-icon type="picture" theme="filled" :style="{ fontSize: '32px' }" />
        <p>底图更换</p>
      </div>
      <div class="buttonItem" @click="clickBaseExchange('scene')">
        <a-icon type="book" theme="filled" :style="{ fontSize: '32px' }" />
        <p>场景更换</p>
      </div>
      <div class="buttonItem" @click="clickToolEdite('tool')">
        <a-icon type="appstore" theme="filled" :style="{ fontSize: '32px' }" />
        <p>工具条</p>
      </div>

      <!-- <a-button style="color:black" @click="clickBaseExchange('baseMap')">
        底图更换
      </a-button>
      <a-button style="color:black" @click="clickBaseExchange('scene')">
        场景更换
      </a-button>
      <a-button style="color:black">工具配置</a-button> -->
    </div>
    <div v-show="type" class="operationPanel">
      <MapSceneSwitch
        v-show="type === 'baseMap' || type === 'scene'"
        ref="MapSceneSwitch"
        @getExchangeValue="getExchangeValue"
      />
      <div>
        <ToolbarConfiguration
          v-show="type === 'tool'"
          @getToolbarItem="getToolbarItem"
        />
      </div>
    </div>
    <div class="baseMap">
      <BaseMap
        :base-image="baseImage"
        :base-scene="baseScene"
        :tool-data="toolData"
      >
        <!-- <AnalysisToolBar
          :checkedToolData="[
            '测量',
            '可视域分析',
            '天际线分析',
            '剖面分析',
            '通视分析',
            '阴影分析',
            '地质体分析'
          ]"
        ></AnalysisToolBar> -->
        <!-- vue量算 -->
        <sm3d-measure v-show="'测量' === toolbarItem"></sm3d-measure>
        <!-- vue可视域 -->
        <sm3d-viewshed
          v-show="'可视域分析' === toolbarItem"
          :spatial-analysis-url="spatialAnalysisUrl"
        ></sm3d-viewshed>
        <!-- vue天际线 -->
        <sm3d-skyline v-show="'天际线分析' === toolbarItem"></sm3d-skyline>
        <!-- vue三维剖面分析 -->
        <sm3d-profile v-show="'剖面分析' === toolbarItem"></sm3d-profile>
        <!-- vue通视分析 -->
        <sm3d-sightline v-show="'通视分析' === toolbarItem"></sm3d-sightline>
        <!-- vue阴影分析 -->
        <sm3d-shadowquery
          v-show="'阴影分析' === toolbarItem"
        ></sm3d-shadowquery>
        <!-- 地质体分析 -->
        <!-- <sm3d-geologic-body-analysis
          :model-urls="modelUrls"
          v-show="'地质体分析' === toolbarItem"
        ></sm3d-geologic-body-analysis> -->
        <!-- 贴线分析 -->
        <sm3d-on-line-analysis
          v-show="'贴线分析' === toolbarItem"
        ></sm3d-on-line-analysis>
        <!-- 退线分析 -->
        <!-- <sm3d-back-line-analysis
          :spatial-analysis-url="spatialAnalysisUrl"
          :query-url="queryUrl"
          v-show="'退线分析' === toolbarItem"
        ></sm3d-back-line-analysis> -->
        <!-- 限高分析 -->
        <sm3d-limit-height-analysis
          v-show="'限高分析' === toolbarItem"
        ></sm3d-limit-height-analysis>
        <!-- 地形分析 -->
        <!-- <div v-show="'地形分析' === toolbarItem">
          <sm3d-terrain-operation slot="combination"></sm3d-terrain-operation>
          <sm3d-terrain-flood slot="combination"></sm3d-terrain-flood>
          <sm3d-terrain-slope slot="combination"></sm3d-terrain-slope>
          <sm3d-terrain-isoline slot="combination"></sm3d-terrain-isoline>
        </div> -->
        <!-- 裁剪分析 -->
        <!-- <div v-show="'裁剪分析' === toolbarItem">
          <sm3d-clip-box slot="combination"></sm3d-clip-box>
          <sm3d-clip-plane slot="combination"></sm3d-clip-plane>
          <sm3d-clip-cross slot="combination"></sm3d-clip-cross>
          <sm3d-clip-polygon slot="combination"></sm3d-clip-polygon>
        </div> -->
        <!-- BOX交互裁剪 -->
        <!-- <sm3d-clip-box-byeditor
          v-show="'BOX交互裁剪' === toolbarItem"
        ></sm3d-clip-box-byeditor> -->
      </BaseMap>
    </div>
  </div>
</template>

<script>
import MapSceneSwitch from '../../../map3d/MapSceneSwitch'
import ToolbarConfiguration from '../../../map3d/ToolbarConfiguration'
export default {
  components: { MapSceneSwitch, ToolbarConfiguration },
  data() {
    return {
      type: '',
      baseImage: '',
      baseScene: '',
      toolData: [],
      spatialAnalysisUrl:
        'http://www.supermapol.com/realspace/services/spatialAnalysis-data_all/restjsr/spatialanalyst/geometry/3d/viewshedbody.json',
      // urlInput: '',
      // listData: [],
      toolbarItem: '',
      modelUrls: [
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer1/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer2/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer3/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer4/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer5/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer6/features/1.json',
      ],
    }
  },
  created() {
    // this.init()
  },
  methods: {
    // 页面初始化  加载默认的底图场景
    init() {
      this.baseImage =
        'https://iserver.supermap.io/iserver/services/map-world/rest/maps/世界地图_Night'
      this.baseScene = ''
    },
    handlebaseImage(location) {
      this.baseImage = location
    },
    clickBaseExchange(type) {
      this.$refs.MapSceneSwitch.handleModleShow(type)
      this.type = type
    },
    clickToolEdite(type) {
      this.type = type
    },
    getExchangeValue(type, value) {
      if (type === 'baseMap') {
        this.handlebaseImage(value)
      } else {
        this.baseScene = value
      }
    },
    getToolbarItem(value) {
      if (this.toolbarItem === value) {
        this.toolbarItem = ''
      } else {
        this.toolbarItem = value
      }
    },
  },
}
</script>
<style scoped lang="less">
#app {
  display: flex;
  flex-flow: row nowrap;
}
.buttonGroup {
  background: rgba(67, 74, 81, 0.8);
  color: white;

  .buttonItem {
    padding: 10px;
    font-size: 8px;
    text-align: center;
  }
  button {
    display: block;
  }
}
.operationPanel {
  padding: 20px;
  width: 200px;
  color: black;
}
.baseMap {
  flex: 1;
}
</style>
