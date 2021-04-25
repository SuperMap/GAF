<template>
  <div>
    <gaf-map
      ref="gafmap"
      :base-layer="BaseLayer"
      :width="width"
      :height="height"
      :center-point="centerPoint"
      :map-controls="MapControls"
      :map-code="mapCode"
      @map-loaded="getMapViewer"
    >
      <template slot="dataVisulization" slot-scope="scope">
        <data-visualization ref="DataVisualization" :map="scope.map" />
      </template>
    </gaf-map>
  </div>
</template>

<script>
import DataVisualization from '~/components/GafMap2D/DataVisualization'
import { getMapControls, getBaseLayer } from '~/utils/GAFMapDataUtils'
export default {
  name: 'GAFMap2D',
  components: {
    DataVisualization,
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
    width: {
      type: Number,
      default: 0,
    },
    height: {
      type: Number,
      default: 0,
    },
    centerPoint: {
      type: Object,
      require: false,
      default() {
        return { x: 0, y: 0 }
      },
    },
  },
  data() {
    return {
      MapControls: [],
      BaseLayer: {},
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
  created() {
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
    getMapViewer(map) {
      this.sendMapViewer(map)
    },
    // 将初始化的地图对象传递给外层使用
    sendMapViewer(value) {
      this.$emit('map-loaded', value)
    },
  },
}
</script>
<style scoped></style>
