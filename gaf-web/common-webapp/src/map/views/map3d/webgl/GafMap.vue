<template>
  <div class="mymap">
    <gaf-map3D
      :map-code="mapCode"
      :base-layer="BaseLayer"
      :map-controls="MapControls"
      @map-loaded="getMapViewer"
    >
      <template slot="dataVisulization" slot-scope="scope">
        <cesium-visualization
          ref="DataVisualization"
          :mapcode="mapCode"
          :map="scope.map"
        />
      </template>
    </gaf-map3D>
  </div>
</template>

<script>
// @ts-nocheck
import CesiumVisualization from '~/components/GafMap3D/CesiumVisualization'
import { getMapControls, getBaseLayer } from '~/utils/GAFMapDataUtils'
export default {
  components: {
    CesiumVisualization,
  },
  props: {
    mapCode: {
      type: String,
      required: true,
      default() {
        return 'map2d'
      },
      validator(value) {
        return value
      },
    },
  },
  data() {
    return {
      MapControls: [],
      BaseLayer: {},
      mapTools: [],
    }
  },
  computed: {
    /**
     * 功能目录操作
     */
    FunctionMenuOperation() {
      return this.$store.state.mapOperation.functionMenuOperation
    },
  },
  watch: {
    /**
     * 功能目录数据可视化
     */
    FunctionMenuOperation(value) {
      this.datavisualizationDisplay(value)
    },
  },
  mounted() {
    this.InitializedData()
  },
  methods: {
    async InitializedData() {
      // 初始化控件
      this.MapControls = await getMapControls.call(this, this.mapCode)
      // 基础图层数据
      this.BaseLayer = await getBaseLayer.call(this, this.mapCode)
    },
    datavisualizationDisplay(operationData) {
      if (this.$refs.DataVisualization) {
        this.$refs.DataVisualization.display(operationData)
      }
    },
    // 子组件传递给父组件方法 地图的viewer
    getMapViewer(mviewer) {
      this.mapLoaded(mviewer)
    },
    // 将初始化的地图对象传递给外层使用
    mapLoaded(value) {
      this.$emit('map-loaded', value)
    },
  },
}
</script>
