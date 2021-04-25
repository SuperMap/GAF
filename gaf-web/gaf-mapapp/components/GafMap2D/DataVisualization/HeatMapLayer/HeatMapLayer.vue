<template>
  <div id="spin" class="spin">
    <a-spin :delay="delay" size="large" tip="加载中..." :spinning="spinning" />
  </div>
</template>

<script>
let heatMapLayerData = null
let root = {}
export default {
  name: 'HeatMapLayer',
  props: {
    map: {
      type: Object,
      default: () => null,
    },
    attributes: {
      type: Object,
      required: false,
      default() {
        const defaultAttributes = {
          color: ['blue', 'cyan', 'lime', 'yellow', 'red'],
          featureWeight: 'Value',
          useGeoUnit: false,
          radius: 15.0,
          data: null,
          url: null,
        }
        return defaultAttributes
      },
    },
  },
  data() {
    return {
      spinning: false,
      delay: 500,
      Initialized: true,
      HeatMapLayer: null,
      maxFeatures: 500,
      fromIndex: 0,
      toIndex: 499,
      featureMaxNum: 5000,
      isLoadData: false,
    }
  },
  watch: {
    map(newMap, oldMap) {
      if (newMap instanceof SuperMap.Map && this.Initialized) {
        this.Initialized = false
      }
    },
    attributes(newAttributes, oldAttributes) {
      // 判定为空的给默认值
      if (newAttributes) {
        if (!newAttributes.color) {
          this.attributes.color = ['blue', 'cyan', 'lime', 'yellow', 'red']
        }
        if (!newAttributes.radius || newAttributes.radius < 0) {
          this.attributes.radius = 5
        }
      }
    },
  },
  created() {},
  mounted() {},
  methods: {
    /**
     * 开始渲染热力图
     */
    display() {
      // 1、创建热力图图层，加到当前地图中
      this.CreatLayer()
      // 2、设置图层的样式和属性信息
      this.SetLayerPropertyStyle()
      // 3、根据url或者Data初始化热力图的内容，并显示
      this.LoadData()
    },
    /**
     * 创建热力图图层 并将此图层增加到地图中
     */
    CreatLayer() {
      if (!this.map) {
        this.$message.error('当前底图不存在')
        return
      }
      // 如果当前热力图没有被创建过
      if (!this.HeatMapLayer) {
        // 创建一个名为“heatMapLayer” 的热点渲染图层。
        const heatMapLayer = new SuperMap.Layer.HeatMapLayer('heatMapLayer')
        this.map.addLayers([heatMapLayer])
        this.HeatMapLayer = heatMapLayer
      }
    },
    /**
     * 设置图层样式和属性
     */
    SetLayerPropertyStyle() {
      // 图层判断空
      if (this.HeatMapLayer) {
        if (this.attributes.color && this.attributes.color.length > 0) {
          const colors = this.transferColorToRgb(this.attributes.color)
          if (colors && colors.length > 0) {
            this.HeatMapLayer.colors = colors
          }
        }
        this.HeatMapLayer.featureWeight = 'value'
        this.HeatMapLayer.useGeoUnit = this.attributes.useGeoUnit
        this.HeatMapLayer.radius = this.attributes.radius
      }
    },
    transferColorToRgb(colors) {
      const serverColors = []
      for (let i = 0; i < colors.length; i++) {
        let color = colors[i]
        if (typeof color !== 'string' && !(color instanceof String)) continue
        color = color.charAt(0) === '#' ? color.substring(1) : color
        if (color.length !== 6 && color.length !== 3) continue
        if (color.length === 3) {
          color = color.replace(/(\w)(\w)(\w)/, '$1$1$2$2$3$3')
        }
        const reg = /\w{2}/g
        const colorArray = color.match(reg)
        const r = parseInt(colorArray[0], 16)
        const g = parseInt(colorArray[1], 16)
        const b = parseInt(colorArray[2], 16)
        const serverColor = new SuperMap.REST.ServerColor(r, g, b)
        serverColors.push(serverColor)
      }
      return serverColors
    },
    /**
     * 根据url或者Data初始化热力图的内容，并显示
     */
    LoadData() {
      // 读取数据
      root = this
      if (this.HeatMapLayer && this.attributes.data) {
        heatMapLayerData = this.attributes.data
      }
      // 读取Url地址内容
      else if (this.HeatMapLayer && this.attributes.url) {
        if (
          this.attributes.actionType &&
          this.attributes.actionType === 'RESTAPI'
        ) {
          this.queryDataByAPI()
        } else if (
          this.attributes.actionType &&
          this.attributes.actionType === 'RESTDATA'
        ) {
          this.isLoadData = true
          this.queryDataBySql()
        }
      }
    },
    async queryDataByAPI() {
      this.spinning = true
      const result = await this.$axios.$get(this.attributes.url)
      if (result.isSuccessed) {
        heatMapLayerData = result.data
        if (heatMapLayerData && heatMapLayerData.length > 0) {
          this.loadHeatMapLayerDataByAPI()
        }
      } else {
        this.spinning = false
        this.$message.error(result.message)
      }
    },
    loadHeatMapLayerDataByAPI() {
      const heatPoints = heatMapLayerData.map((item) => {
        const point = item.centerPoint
        const heatPoint = new SuperMap.Feature.Vector(
          new SuperMap.Geometry.Point(point.x, point.y),
          {
            value: item.attributes[this.attributes.featureWeight],
          }
        )
        return heatPoint
      })
      if (this.HeatMapLayer) {
        this.HeatMapLayer.addFeatures(heatPoints)
      }
      this.spinning = false
    },
    queryDataBySql() {
      this.spinning = true
      const queryParam = new SuperMap.REST.FilterParameter({
        name:
          this.attributes.datasetName + '@' + this.attributes.dataSourceName,
      })
      const datasetStr =
        this.attributes.dataSourceName + ':' + this.attributes.datasetName
      const getFeatureBySQLParams = new SuperMap.REST.GetFeaturesBySQLParameters(
        {
          queryParameter: queryParam,
          datasetNames: [datasetStr],
          maxFeatures: this.maxFeatures > 0 ? this.maxFeatures : 5000,
          fromIndex: this.fromIndex,
          toIndex: this.toIndex > 0 ? this.toIndex : -1,
        }
      )
      const queryBySQLService = new SuperMap.REST.GetFeaturesBySQLService(
        this.attributes.url + '/data',
        {
          eventListeners: {
            processCompleted: this.processCompleted,
            processFailed: this.processFailed,
          },
        }
      )
      queryBySQLService.processAsync(getFeatureBySQLParams)
    },
    processCompleted(queryEventArgs) {
      const features = queryEventArgs.result.features
      if (features && features.length > 0) {
        heatMapLayerData = features
        this.loadHeatMapLayerDataBySql()
        if (
          features.length === this.maxFeatures &&
          this.isLoadData &&
          this.toIndex < this.featureMaxNum
        ) {
          this.fromIndex = this.fromIndex + this.maxFeatures
          this.toIndex = this.toIndex + this.maxFeatures
          this.queryDataBySql()
        }
      } else {
        this.spinning = false
        this.$message.error('服务中不含有热力图对应的数据')
      }
    },
    processFailed(e) {
      this.spinning = false
      if (e && e.error && e.error.errorMsg) {
        this.$message.error('加载数据失败：' + e.error.errorMsg)
      } else {
        this.$message.error('数据加载加载数据失败')
      }
    },
    loadHeatMapLayerDataBySql() {
      const heatPoints = heatMapLayerData.map((item) => {
        const feature = new SuperMap.Feature.Vector(
          new SuperMap.Geometry.Point(
            item.geometry.getBounds().getCenterLonLat().lon,
            item.geometry.getBounds().getCenterLonLat().lat
          ),
          {
            value: item.attributes[root.attributes.featureWeight],
          }
        )
        return feature
      })
      if (this.HeatMapLayer) {
        this.HeatMapLayer.addFeatures(heatPoints)
      }
      this.spinning = false
    },
    // 仅仅清空热力图的要素
    ClearData() {
      this.spinning = false
      if (this.HeatMapLayer) {
        this.HeatMapLayer.removeAllFeatures()
      }
    },
    /**
     * 清空当前热力图增加的图层和临时要素
     */
    deactivate() {
      // 此处清空临时图层和临时要素
      if (this.HeatMapLayer) {
        this.ClearData()
        // this.HeatMapLayer.destroy()
        // this.HeatMapLayer = null
        this.isLoadData = false
        Object.assign(this.$data, this.$options.data())
      }
    },
  },
}
</script>

<style scoped>
.spin {
  position: fixed;
  top: 50%;
  left: 50%;
  text-align: center;
  color: #1890ff;
  border-radius: 4px;
  margin-top: 20px;
  padding: 30px 50px;
  margin: 20px 0;
  z-index: 2501;
}
</style>
