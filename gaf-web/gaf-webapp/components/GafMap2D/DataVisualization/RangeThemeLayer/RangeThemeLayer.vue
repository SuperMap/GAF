<template>
  <div>
    <div v-show="false" id="popRange">
      <gaf-map-popup-list :title="title" :data-source="dataSource" />
    </div>
    <div id="spin" class="spin">
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
import { islandHoleHandlerForFeature } from '~/utils/GeometryFunction'
let rangeThemeLayerData = null
export default {
  name: 'RangeThemeLayer',
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
      rangeThemeLayer: null,
      title: '',
      dataSource: [],
      labels: [],
      labelVector: [],
      maxFeatures: 500,
      fromIndex: 0,
      toIndex: 499,
      featureMaxNum: 5000,
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
      }
    },
  },
  created() {},
  mounted() {},
  methods: {
    /**
     * 开始渲染分段专题图
     */
    display() {
      // 1、创建分段专题图图层，加到当前地图中
      this.CreatLayer()
      // 2、设置图层的样式和属性信息
      this.SetLayerPropertyStyle()
      // 3、根据url或者Data初始化热力图的内容，并显示
      this.LoadData()
    },
    /**
     * 创建分段专题图图层 并将此图层增加到地图中
     */
    CreatLayer() {
      if (!this.map) {
        this.$message.error('当前底图不存在')
        return
      }
      // 如果当前分段专题图没有被创建过
      if (!this.rangeThemeLayer) {
        // 创建一个名为“rangeThemeLayer” 的热点渲染图层。
        const rangeThemeLayer = new SuperMap.Layer.Range('rangeThemeLayer')
        this.map.addLayers([rangeThemeLayer])
        this.rangeThemeLayer = rangeThemeLayer
      }
    },
    /**
     * 设置图层样式和属性
     */
    SetLayerPropertyStyle() {
      // 图层判断空
      if (this.rangeThemeLayer) {
        if (this.attributes.layerStyle) {
          this.rangeThemeLayer.style = {
            shadowBlur: this.attributes.layerStyle.shadowBlur,
            shadowColor: this.attributes.layerStyle.shadowColor,
            shadowOffsetX: this.attributes.layerStyle.shadowOffsetX,
            shadowOffsetY: this.attributes.layerStyle.shadowOffsetY,
          }
        }
        // if (this.attributes.color) {
        //   this.rangeThemeLayer.style.fillColor = this.attributes.color
        // }
        // 透明度
        if (this.attributes.fillOpacity) {
          this.rangeThemeLayer.setOpacity(this.attributes.fillOpacity)
        }
        // 开启 hover 高亮效果
        if (this.attributes.isHoverAble) {
          this.rangeThemeLayer.isHoverAble = this.attributes.isHoverAble
          this.rangeThemeLayer.highlightStyle = {
            stroke: this.attributes.layerHighlightStyle.stroke,
            strokeWidth: this.attributes.layerHighlightStyle.strokeWidth,
            strokeColor: this.attributes.layerHighlightStyle.strokeColor,
            fillColor: this.attributes.layerHighlightStyle.fillColor,
            fillOpacity: this.attributes.layerHighlightStyle.fillOpacity,
          }
        }
        // 用于单值专题图的属性字段名称
        if (this.attributes.themeField) {
          this.rangeThemeLayer.themeField = this.attributes.themeField
        }
        // 专题图层 mousemove 事件
        this.rangeThemeLayer.on('click', this.openInfoWin)
        this.rangeThemeLayer.on('clickout', this.closeInfoWin)
      }
    },
    /**
     * 根据url或者Data初始化热力图的内容，并显示
     */
    LoadData() {
      // 读取数据
      if (this.rangeThemeLayer && this.data) {
        rangeThemeLayerData = this.attributes.data
        this.loadRangeData()
      }
      // 读取Url地址内容
      else if (this.rangeThemeLayer && this.attributes.url) {
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
        rangeThemeLayerData = result.data
        if (rangeThemeLayerData && rangeThemeLayerData.length > 0) {
          this.loadRangeDataByAPI()
        }
      } else {
        this.spinning = false
        this.$message.error(result.message)
      }
    },
    loadRangeDataByAPI() {
      const IHFeas = []
      let feas = []
      const themeDatas = []
      const themeField = this.attributes.themeField
      for (let i = 0, len = rangeThemeLayerData.length; i < len; i++) {
        const feature = rangeThemeLayerData[i]
        let rangeFeatrue
        switch (feature.type) {
          case 'POINT':
            rangeFeatrue = new SuperMap.Feature.Vector(
              new SuperMap.Geometry.Point(
                feature.points[0].x,
                feature.points[0].y
              )
            )
            break
          case 'LINE':
            const linePoints = feature.points.map((point) => {
              return new SuperMap.Geometry.Point(point.x, point.y)
            })
            rangeFeatrue = new SuperMap.Feature.Vector(
              new SuperMap.Geometry.LineString(linePoints)
            )
            break
          case 'REGION':
            const regionPoints = feature.points.map((point) => {
              return new SuperMap.Geometry.Point(point.x, point.y)
            })
            const linearRings = new SuperMap.Geometry.LinearRing(regionPoints)
            const region = new SuperMap.Geometry.Polygon([linearRings])
            rangeFeatrue = new SuperMap.Feature.Vector(
              new SuperMap.Geometry.MultiPolygon([region])
            )
            // 处理岛洞
            if (rangeFeatrue.geometry.components.length > 1) {
              IHFeas.push(islandHoleHandlerForFeature(rangeFeatrue))
            }
            break
          default:
            break
        }
        rangeFeatrue.info = this.getDatasource(feature)
        const label = this.getLabel(feature)
        label.centerPoint = feature.centerPoint
        this.labels.push(label)
        rangeFeatrue.attributes = feature.attributes
        if (feature.attributes[themeField]) {
          themeDatas.push(feature.attributes[themeField])
        } else {
          console.log(feature.attributes)
        }
        feas.push(rangeFeatrue)
      }
      // 岛洞多面要素必需在其他要素之前添加
      if (IHFeas && IHFeas.length > 1) {
        feas = IHFeas.concat(feas)
      }
      this.setGroupStyle(themeDatas)
      if (this.attributes.label.visible) {
        this.addLabel(this.labels)
      }
      if (this.rangeThemeLayer) {
        this.rangeThemeLayer.addFeatures(feas)
      }
      this.spinning = false
    },
    // 通过服务地址获取数据
    queryDataBySql() {
      this.spinning = true
      const queryParam = new SuperMap.REST.FilterParameter({
        name:
          this.attributes.datasetName + '@' + this.attributes.dataSourceName,
        attributeFilter: "TBXHDM != ''",
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
        rangeThemeLayerData = features
        this.loadRangeDataBySql()
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
        this.$message.error('服务中不含有分段专题图对应的数据')
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
    // 将数据加载到分段专题图层中
    loadRangeDataBySql() {
      // 如果有数据初始化要素信息
      if (rangeThemeLayerData && rangeThemeLayerData.length > 0) {
        const IHFeas = []
        let feas = []
        const themeDatas = []
        const themeField = this.attributes.themeField
        for (let i = 0, len = rangeThemeLayerData.length; i < len; i++) {
          const feature = rangeThemeLayerData[i]
          let rangeFeatrue
          if (
            feature.geometry.componentTypes &&
            feature.geometry.componentTypes[0] === 'SuperMap.Geometry.Polygon'
          ) {
            rangeFeatrue = feature
            // 处理岛洞
            if (
              feature.geometry.partTopo &&
              feature.geometry.partTopo.length > 1
            ) {
              IHFeas.push(islandHoleHandlerForFeature(feature))
            }
          } else {
            rangeFeatrue = feature
          }
          rangeFeatrue.attributes = feature.attributes
          if (feature.attributes[themeField]) {
            themeDatas.push(feature.attributes[themeField])
          }
          rangeFeatrue.info = this.getDatasource(feature)
          const label = this.getLabel(feature)
          label.centerPoint = feature.geometry.getCentroid()
          this.labels.push(label)
          feas.push(rangeFeatrue)
        }
        // 岛洞多面要素必需在其他要素之前添加
        if (IHFeas && IHFeas.length > 1) {
          feas = IHFeas.concat(feas)
        }
        this.setGroupStyle(themeDatas)
        if (this.attributes.label.visible) {
          this.addLabel(this.labels)
        }
        if (this.rangeThemeLayer) {
          this.rangeThemeLayer.addFeatures(feas)
        }
        this.spinning = false
      }
    },
    getDatasource(feature) {
      const datas = []
      if (this.attributes.property.fields) {
        const fields = this.attributes.property.fields
        for (let j = 0; j < fields.length; j++) {
          const field = fields[j]
          this.title = this.attributes.property.title
          datas.push({
            name: field.name,
            value: feature.attributes[field.name.toUpperCase()],
          })
        }
      }
      return datas
    },
    getLabel(feature) {
      let label = {}
      if (this.attributes.label.field) {
        label = {
          value: feature.attributes[this.attributes.label.field.toUpperCase()],
        }
      }
      return label
    },
    addLabel(labelDatas) {
      const labels = []
      // 根据feature信息添加标签。
      if (labelDatas && labelDatas.length > 0) {
        labelDatas.forEach((label, idx) => {
          const x = label.centerPoint.x
          const y = label.centerPoint.y
          const text = label.value
          if (text) {
            const lable = new SuperMap.Geometry.GeoText(x, y, text)
            const vector = new SuperMap.Feature.Vector(lable)
            labels.push(vector)
          }
        })
        this.labelVector.addFeatures(labels)
      }
    },
    setGroupStyle(themeDatas) {
      const groupMode = this.attributes.groupMode
      const groupCount = this.attributes.groupCount
      const groupColors = this.attributes.color
      const rangeSection = this.getSection(groupMode, themeDatas, groupCount)
      const styleGroups = []
      const max = Math.max(...themeDatas)
      const min = Math.min(...themeDatas)
      for (let i = 0; i < groupCount; i++) {
        let style = {}
        style = {
          start: min + rangeSection * i,
          end: min + rangeSection * (i + 1),
          style: {
            fillColor: groupColors[i],
          },
        }
        // 区间不包含end数值，所以加一
        if (i === groupCount - 1) {
          style = {
            start: min + rangeSection * i,
            end: max + 1,
            style: {
              fillColor: groupColors[i],
            },
          }
        }
        styleGroups.push(style)
      }
      this.rangeThemeLayer.styleGroups = styleGroups
    },
    // 获得每个分段的区间大小
    getSection(groupMode, themeDatas, groupCount) {
      const max = Math.max(...themeDatas)
      const min = Math.min(...themeDatas)
      let rangeSection = 0 // 分段区间
      switch (groupMode) {
        case 'EQUALINTERVAL':
          rangeSection = (max - min) / groupCount
          break
        case 'LOGARITHM':
          rangeSection = (Math.log10(max) - Math.log10(min)) / groupCount
          break
        case 'QUANTILE':
          rangeSection = (max - min) / (themeDatas.length / groupCount)
          break
        case 'SQUAREROOT':
          rangeSection = (Math.sqrt(max) - Math.sqrt(min)) / groupCount
          break
      }
      rangeSection = Math.floor(rangeSection)
      return rangeSection
    },
    // addLabel(features) {
    //   const labels = []
    //   // 根据feature信息添加标签。
    //   features.forEach((feature, idx) => {
    //     const x = feature.geometry.getBounds().getCenterLonLat().lon
    //     const y = feature.geometry.getBounds().getCenterLonLat().lat
    //     const text = feature.label.value
    //     if (text) {
    //       const lable = new SuperMap.Geometry.GeoText(x, y, text)
    //       const vector = new SuperMap.Feature.Vector(lable)
    //       labels.push(vector)
    //     }
    //   })
    //   const fieldColor = features[0].label.color
    //   const fieldFamily = features[0].label.font
    //   const strategy = new SuperMap.Strategy.GeoText()
    //   strategy.style = this.StrategyStyle
    //   if (fieldColor) {
    //     strategy.style.fontColor = fieldColor
    //   }
    //   if (fieldFamily) {
    //     strategy.style.fontFamily = fieldFamily
    //   }
    //   this.labelVector = new SuperMap.Layer.Vector('labelLayer', {
    //     strategies: [strategy]
    //   })
    //   this.map.addLayer(this.labelVector)
    //   this.labelVector.addFeatures(labels)
    // },
    openInfoWin(e) {
      let popwin
      if (e.target && e.target.refDataID) {
        const popup = document.getElementById('popRange')
        const fid = e.target.refDataID
        const feature = this.rangeThemeLayer.getFeatureById(fid)
        this.dataSource = feature.info
        if (feature && feature.geometry) {
          const lonLat = feature.geometry.bounds.centerLonLat
          this.$nextTick(function () {
            popwin = new SuperMap.Popup.FramedCloud(
              'winOfPoint',
              lonLat,
              new SuperMap.Size(200, 200),
              popup.innerHTML,
              null,
              true,
              () => {
                this.map.removePopup(popwin)
              },
              true
            )
            this.map.removeAllPopup()
            this.map.addPopup(popwin)
          })
        }
      } else {
        this.closeInfoWin()
      }
    },
    closeInfoWin() {
      this.map.removeAllPopup()
      this.dataSource = []
    },
    // 仅仅清空分段专题图的要素
    ClearData() {
      if (this.map.getLayerIndex(this.rangeThemeLayer) !== -1) {
        this.closeInfoWin()
        this.rangeThemeLayer.removeAllFeatures()
        if (this.map.getLayerIndex(this.labelVector) !== -1) {
          this.labelVector.removeAllFeatures()
        }
      }
    },
    /**
     * 清空当前分段专题图增加的图层和临时要素
     */
    deactivate() {
      // 此处清空临时图层和临时要素
      if (this.rangeThemeLayer) {
        this.ClearData()
        this.rangeThemeLayer.destroy()
        this.rangeThemeLayer = null
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
}
</style>
