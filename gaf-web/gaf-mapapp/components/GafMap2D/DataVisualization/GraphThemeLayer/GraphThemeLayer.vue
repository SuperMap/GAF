<template>
  <div>
    <!--    <span v-show="false" id="popup">-->
    <!--      <popup-list :title="title" :data-source="dataSource" />-->
    <!--    </span>-->
    <div id="spinId" class="spin">
      <a-spin
        :delay="delay"
        size="large"
        tip="加载中..."
        :spinning="spinning"
      />
    </div>
  </div>
</template>

<script>
let graphThemeLayerData = null
let root = null
export default {
  name: 'GraphThemeLayer',
  props: {
    map: {
      type: Object,
      default: () => null,
    },
    attributes: {
      type: Object,
      required: false,
      default() {
        const defaultAttributes = {}
        return defaultAttributes
      },
    },
  },
  data() {
    return {
      spinning: false,
      delay: 500,
      Initialized: true,
      GraphThemeLayer: null,
      themeFields: [],
      yTick: 4,
      tempVector: {},
      title: '',
      dataSource: [],
      labels: [],
      labelVector: [],
      maxFeatures: 10,
      fromIndex: 0,
      toIndex: 9,
      featureMaxNum: 10,
      isLoadData: false,
      StrategyStyle: {
        fontColor: '#7a7a7a',
        fontWeight: 'bolder',
        fontSize: '14px',
        labelXOffset: 25,
        labelYOffset: -15,
        stroke: true,
        strokeColor: '#4c4c4c',
        strokeWidth: 1,
        strokeLinecap: 'square',
      },
      infowin: null,
      infowinPosition: null,
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
        if (!newAttributes.width || newAttributes.width <= 0) {
          newAttributes.width = 100
        }
        if (!newAttributes.height || newAttributes.height <= 0) {
          newAttributes.height = 100
        }
        if (
          !newAttributes.fillOpacity ||
          newAttributes.fillOpacity <= 0 ||
          newAttributes.fillOpacity > 1
        ) {
          newAttributes.fillOpacity = 0.8
        }
        if (!newAttributes.minScope) {
          newAttributes.minScope = 0
        }
        if (!newAttributes.maxScope) {
          newAttributes.maxScope = 100000
        }
      }
    },
  },
  created() {},
  mounted() {},
  methods: {
    /**
     * 开始渲染统计专题图
     */
    display() {
      // 1、创建统计专题图图层，加到当前地图中
      this.CreatLayer()
      // 2、设置图层的样式和属性信息
      this.SetLayerPropertyStyle()
      // 3、根据url或者Data初始化热力图的内容，并显示
      this.LoadData()
    },
    /**
     * 创建统计专题图图层 并将此图层增加到地图中
     */
    CreatLayer() {
      root = this
      if (!this.map) {
        this.$message.error('当前底图不存在')
        return
      }
      // 如果当前统计专题图没有被创建过
      if (!this.GraphThemeLayer) {
        if (this.attributes && this.attributes.chartsType) {
          const type = this.attributes.chartsType
          // 创建一个饼状图（Pie）统计专题图图层
          let graphThemeLayer = null
          switch (type) {
            case 'Pie':
              graphThemeLayer = new SuperMap.Layer.Graph(
                'graphThemeLayer',
                'Pie'
              )
              break
            case 'Line':
              graphThemeLayer = new SuperMap.Layer.Graph(
                'graphThemeLayer',
                'Line'
              )
              break
            case 'Bar':
              graphThemeLayer = new SuperMap.Layer.Graph(
                'graphThemeLayer',
                'Bar'
              )
              break
          }
          this.map.addLayers([graphThemeLayer])
          this.GraphThemeLayer = graphThemeLayer
        }
      }
    },
    /**
     * 设置图层样式和属性
     */
    SetLayerPropertyStyle() {
      // 图层判断空
      if (this.GraphThemeLayer) {
        if (!this.attributes) {
          this.$message.error('专题属性不存在')
          return
        }
        // 指定用于专题图制作的属性字段
        if (
          !this.attributes.themeFields ||
          this.attributes.themeFields.length <= 0
        ) {
          this.$message.error('专题字段不能为空')
          return
        }
        this.themeFields = this.attributes.themeFields.map((item) => {
          const field = item.name.toUpperCase()
          return field
        })
        this.GraphThemeLayer.themeFields = this.themeFields
        const styleFields = this.attributes.themeFields.map((item) => {
          const style = { fillColor: item.color }
          return style
        })
        this.GraphThemeLayer.chartsSetting = {
          width: this.attributes.width,
          height: this.attributes.height,
          axisYTick: this.yTick,
          axisYLabels: this.getYLabels(),
          axisXLabels: this.themeFields,
          sectorStyle: {
            fillOpacity: this.attributes.fillOpacity,
          },
          // 折线 style
          lineStyle: { strokeColor: '#D8361B', strokeOpacity: 0.7 },
          // 折线节点（表示字段值的图形）样式
          pointStyle: {
            stroke: true,
            strokeColor: '#D8361B',
            pointRadius: 6,
            strokeWidth: 1,
            fillColor: '#F3F3F3',
            fillOpacity: 1,
          },
          // 折线节点 hover 样式
          pointHoverStyle: {
            fillColor: '#D8361B',
          },
          codomain: [this.attributes.minScope, this.attributes.maxScope],
          sectorStyleByFields: styleFields,
          sectorHoverStyle: {
            fillOpacity: 1,
          },
        }
        // // 注册专题图 mousemove, mouseout事件(注意：专题图图层对象自带 on 函数，没有 events 对象)
        this.GraphThemeLayer.on('mousemove', this.openInfoWin)
        this.GraphThemeLayer.on('mouseout', this.closeInfoWin)
        // this.GraphThemeLayer.on('mousemove', this.openLabel)
        // this.GraphThemeLayer.on('mouseout', this.closeLabel)

        // // 注册地图 mousemove，用于获取当前鼠标在地图中的像素位置
        this.map.events.on({
          mousemove(e) {
            root.infowinPosition = e.xy.clone()
            // // 偏移
            // root.infowinPosition.x += 20
            // root.infowinPosition.y -= 25
          },
        })
      }
    },
    /**
     * 根据url或者Data初始化热力图的内容，并显示
     */
    LoadData() {
      // 读取数据
      if (this.GraphThemeLayer && this.data) {
        graphThemeLayerData = this.attributes.data
      }
      // 读取Url地址内容
      else if (this.GraphThemeLayer && this.attributes.url) {
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
        graphThemeLayerData = result.data
        if (graphThemeLayerData && graphThemeLayerData.length > 0) {
          this.loadGraphDataByAPI()
        }
      } else {
        this.spinning = false
        this.$message.error(result.message)
      }
    },
    loadGraphDataByAPI() {
      const graphDatas = []
      for (let i = 0; i < graphThemeLayerData.length; i++) {
        const feature = graphThemeLayerData[i]
        const fields = this.themeFields
        const GraphFieldData = {}
        for (let i = 0; i < fields.length; i++) {
          const field = fields[i]
          GraphFieldData[field] = feature.attributes[field] * 1
        }
        const graphData = new SuperMap.Feature.Vector(
          new SuperMap.Geometry.Point(
            feature.centerPoint.x,
            feature.centerPoint.y
          ),
          GraphFieldData
        )
        graphDatas.push(graphData)
      }
      if (this.GraphThemeLayer) {
        this.GraphThemeLayer.addFeatures(graphDatas)
      }
      this.spinning = false
    },
    // 通过服务地址获取数据
    queryDataBySql() {
      this.spinning = true
      const queryParam = new SuperMap.REST.FilterParameter({
        name:
          this.attributes.datasetName + '@' + this.attributes.dataSourceName,
        attributeFilter: "xzqdm like '140221%'",
      })
      const datasetStr =
        this.attributes.dataSourceName + ':' + this.attributes.datasetName
      const getFeatureBySQLParams = new SuperMap.REST.GetFeaturesBySQLParameters(
        {
          queryParameter: queryParam,
          datasetNames: [datasetStr],
          maxFeatures: this.maxFeatures,
          fromIndex: this.fromIndex,
          toIndex: this.toIndex,
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
        graphThemeLayerData = features
        this.loadGraphDataBySql()
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
        this.$message.error('服务中不含有专题字段对应的数据')
      }
    },
    processFailed(e) {
      this.spinning = false
      if (e && e.error && e.error.errorMsg) {
        this.$message.error('加载数据失败：' + e.error.errorMsg)
      } else {
        this.$message.error('加载数据失败')
      }
    },
    // 将数据加载到单值专题图层中
    loadGraphDataBySql() {
      // 如果有数据初始化要素信息
      if (graphThemeLayerData && graphThemeLayerData.length > 0) {
        const graphDatas = []
        for (let i = 0, len = graphThemeLayerData.length; i < len; i++) {
          const feature = graphThemeLayerData[i]
          const fields = this.themeFields
          const GraphFieldData = {}
          for (let i = 0; i < fields.length; i++) {
            const field = fields[i]
            GraphFieldData[field] = feature.attributes[field] * 1
          }
          const graphData = new SuperMap.Feature.Vector(
            feature.geometry.getCentroid(),
            GraphFieldData
          )
          graphDatas.push(graphData)
        }
        if (this.GraphThemeLayer) {
          this.GraphThemeLayer.addFeatures(graphDatas)
        }
      }
      this.spinning = false
    },
    getYLabels() {
      const labels = []
      if (!this.yTick || this.yTick <= 0) {
        return labels
      }
      if (this.attributes) {
        const min = this.attributes.minScope
        const max = this.attributes.maxScope
        const part = (max - min) / this.yTick
        for (let i = this.yTick; i >= 1; i--) {
          const partValue = min + i * part
          labels.push(partValue)
        }
        labels.push(min)
      }
      return labels
    },
    openInfoWin(e) {
      let popwin
      if (e.target && e.target.dataInfo) {
        // const popup = document.getElementById('popup')
        // const data = {}
        // data.name = e.target.dataInfo.field
        // data.value = e.target.dataInfo.value
        // root.dataSource.push(data)
        // this.dataSource = feature.info
        // 弹出窗地理位置
        const position = { x: e.event.layerX + 10, y: e.event.layerY + 10 }
        // root.infowinPosition
        let infoHtml = '<span>'
        infoHtml += e.target.dataInfo.field + ':' + e.target.dataInfo.value
        infoHtml += '</span>'
        const lonLat = this.map.getLonLatFromPixel(position)
        popwin = new SuperMap.Popup(
          'winOfPoint',
          lonLat,
          new SuperMap.Size(200, 100),
          infoHtml,
          true,
          null
        )
        if (popwin) this.map.removePopup(popwin)
        popwin.setOpacity(0.8)
        popwin.autoSize = true
        this.map.removeAllPopup()
        this.map.addPopup(popwin)
      }
      // else {
      //   this.closeInfoWin()
      // }
    },
    closeInfoWin() {
      if (this.map) {
        this.map.removeAllPopup()
      }
      this.dataSource = []
    },
    // openLabel(e) {
    //   if (e.target && e.target.dataInfo) {
    //     const strategy = new SuperMap.Strategy.GeoText()
    //     strategy.style = this.StrategyStyle
    //     if (Object.keys(this.tempVector).length === 0) {
    //       const vectorLayer = new SuperMap.Layer.Vector('Label', {
    //         strategies: [strategy]
    //       })
    //       this.map.addLayers([vectorLayer])
    //       this.tempVector = vectorLayer
    //     }
    //     // 跟随鼠标移动label
    //     const id = 'MouseMoveLabel'
    //     const position = { x: e.event.layerX, y: e.event.layerY }
    //     const lonLat = this.map.getLonLatFromPixel(position)
    //     const result = e.target.dataInfo.field + ':' + e.target.dataInfo.value
    //     const tempFeature = this.tempVector.getFeatureById(id)
    //     if (!tempFeature) {
    //       const MouseMoveLabel = new SuperMap.Geometry.GeoText(
    //         lonLat.lat,
    //         lonLat.lon,
    //         result
    //       )
    //       MouseMoveLabelVector = new SuperMap.Feature.Vector(MouseMoveLabel)
    //       MouseMoveLabelVector.id = id
    //       this.tempVector.addFeatures([MouseMoveLabelVector])
    //     } else {
    //       tempFeature.geometry.x = lonLat.lat
    //       tempFeature.geometry.y = lonLat.lon
    //       tempFeature.geometry.text = result
    //       this.tempVector.redraw()
    //     }
    //   }
    // },
    // closeLabel() {
    //   if (Object.keys(this.tempVector).length !== 0) {
    //     this.tempVector.removeAllFeatures()
    //     this.tempVector = {}
    //   }
    // },
    // 仅仅清空统计专题图的要素
    ClearData() {
      if (this.GraphThemeLayer) {
        this.GraphThemeLayer.removeAllFeatures()
      }
    },
    /**
     * 清空当前统计专题图增加的图层和临时要素
     */
    deactivate() {
      // 此处清空临时图层和临时要素
      if (this.GraphThemeLayer) {
        this.ClearData()
        this.GraphThemeLayer.destroy()
        this.GraphThemeLayer = null
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
