<template>
  <div>
    <span v-show="false" id="popUnique">
      <gaf-map-popup-list :title="title" :data-source="dataSource" />
    </span>
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
let uniqueThemeLayerData = null
export default {
  name: 'Unique',
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
      uniqueThemeLayer: null,
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
        if (!newAttributes.radius || newAttributes.radius <= 0) {
          newAttributes.radius = 10
        }
        if (
          !newAttributes.fillOpacity ||
          newAttributes.fillOpacity <= 0 ||
          newAttributes.fillOpacity > 1
        ) {
          newAttributes.fillOpacity = 0.8
        }
      }
    },
  },
  created() {},
  mounted() {},
  methods: {
    /**
     * 开始渲染单值专题图
     */
    display() {
      // 1、创建单值专题图图层，加到当前地图中
      this.CreatLayer()
      // 2、设置图层的样式和属性信息
      this.SetLayerPropertyStyle()
      // 3、根据url或者Data初始化热力图的内容，并显示
      this.LoadData()
    },
    /**
     * 创建单值专题图图层 并将此图层增加到地图中
     */
    CreatLayer() {
      if (!this.map) {
        this.$message.error('当前底图不存在')
        return
      }
      // 如果当前单值专题图没有被创建过
      if (!this.uniqueThemeLayer) {
        // 创建一个名为“uniqueThemeLayer” 的热点渲染图层。
        const uniqueThemeLayer = new SuperMap.Layer.Unique('ThemeLayer')
        this.map.addLayers([uniqueThemeLayer])
        this.uniqueThemeLayer = uniqueThemeLayer
      }
      if (
        this.labelVector.length === 0 &&
        this.attributes &&
        this.attributes.label &&
        this.attributes.label.visible
      ) {
        const fieldColor = this.attributes.label.color
        const fieldFamily = this.attributes.label.font
        const strategy = new SuperMap.Strategy.GeoText()
        strategy.style = this.StrategyStyle
        if (fieldColor) {
          strategy.style.fontColor = fieldColor
        }
        if (fieldFamily) {
          strategy.style.fontFamily = fieldFamily
        }
        const vector = new SuperMap.Layer.Vector('labelLayer', {
          strategies: [strategy],
        })
        this.map.addLayer(vector)
        this.labelVector = vector
      }
    },
    /**
     * 设置图层样式和属性
     */
    SetLayerPropertyStyle() {
      // 图层判断空
      if (this.uniqueThemeLayer) {
        if (this.attributes.layerStyle) {
          this.uniqueThemeLayer.style = {
            shadowBlur: this.attributes.layerStyle.shadowBlur,
            shadowColor: this.attributes.layerStyle.shadowColor,
            shadowOffsetX: this.attributes.layerStyle.shadowOffsetX,
            shadowOffsetY: this.attributes.layerStyle.shadowOffsetY,
          }
        }
        // 透明度
        if (this.attributes.fillOpacity) {
          this.uniqueThemeLayer.setOpacity(this.attributes.fillOpacity)
        }
        // 开启 hover 高亮效果
        if (this.attributes.isHoverAble) {
          this.uniqueThemeLayer.isHoverAble = this.attributes.isHoverAble
          this.uniqueThemeLayer.highlightStyle = {
            stroke: this.attributes.layerHighlightStyle.stroke,
            strokeWidth: this.attributes.layerHighlightStyle.strokeWidth,
            strokeColor: this.attributes.layerHighlightStyle.strokeColor,
            fillColor: this.attributes.layerHighlightStyle.fillColor,
            fillOpacity: this.attributes.layerHighlightStyle.fillOpacity,
          }
        }

        // 用于单值专题图的属性字段名称
        if (this.attributes.themeField) {
          this.uniqueThemeLayer.themeField = this.attributes.themeField
        }

        // 风格数组，设定值对应的样式
        if (this.attributes && this.attributes.customColors) {
          this.uniqueThemeLayer.styleGroups = this.attributes.customColors.map(
            (item) => {
              const styleGroups = {
                value: item.fieldValue,
                style: {
                  fillColor: item.color,
                  pointRadius: this.attributes.radius,
                },
              }
              return styleGroups
            }
          )
        }
        // 专题图层 mousemove 事件
        this.uniqueThemeLayer.on('click', this.openInfoWin)
        this.uniqueThemeLayer.on('clickout', this.closeInfoWin())
      }
    },
    /**
     * 根据url或者Data初始化热力图的内容，并显示
     */
    LoadData() {
      if (this.uniqueThemeLayer && this.attributes.data) {
        uniqueThemeLayerData = this.attributes.data
        this.loadUniqueData()
      }
      // 读取Url地址内容
      else if (this.uniqueThemeLayer && this.attributes.url) {
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
        uniqueThemeLayerData = result.data
        if (uniqueThemeLayerData && uniqueThemeLayerData.length > 0) {
          this.loadUniqueDataByAPI()
        }
      } else {
        this.spinning = false
        this.$message.error(result.message)
      }
    },
    loadUniqueDataByAPI() {
      const IHFeas = []
      let feas = []
      this.labels = []
      for (let i = 0, len = uniqueThemeLayerData.length; i < len; i++) {
        const feature = uniqueThemeLayerData[i]
        let uniqueFeatrue
        switch (feature.type) {
          case 'POINT':
            uniqueFeatrue = new SuperMap.Feature.Vector(
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
            uniqueFeatrue = new SuperMap.Feature.Vector(
              new SuperMap.Geometry.LineString(linePoints)
            )
            break
          case 'REGION':
            const regionPoints = feature.points.map((point) => {
              return new SuperMap.Geometry.Point(point.x, point.y)
            })
            const linearRings = new SuperMap.Geometry.LinearRing(regionPoints)
            const region = new SuperMap.Geometry.Polygon([linearRings])
            uniqueFeatrue = new SuperMap.Feature.Vector(
              new SuperMap.Geometry.MultiPolygon([region])
            )
            // 处理岛洞
            if (uniqueFeatrue.geometry.components.length > 1) {
              IHFeas.push(islandHoleHandlerForFeature(uniqueFeatrue))
            }
            break
          default:
            break
        }
        uniqueFeatrue.info = this.getDatasource(feature)
        const label = this.getLabel(feature)
        label.centerPoint = feature.centerPoint
        this.labels.push(label)
        uniqueFeatrue.attributes = feature.attributes
        feas.push(uniqueFeatrue)
      }
      // 岛洞多面要素必需在其他要素之前添加
      if (IHFeas && IHFeas.length > 1) {
        feas = IHFeas.concat(feas)
      }
      if (this.attributes.label.visible) {
        this.addLabel(this.labels)
      }
      if (this.uniqueThemeLayer) {
        this.uniqueThemeLayer.addFeatures(feas)
      }
      this.spinning = false
    },
    // 通过服务地址获取数据
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
        uniqueThemeLayerData = features
        this.loadUniqueDataBySql()
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
        this.$message.error('服务中不含有单值专题图对应的数据')
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
    loadUniqueDataBySql() {
      // 如果有数据初始化要素信息
      if (uniqueThemeLayerData && uniqueThemeLayerData.length > 0) {
        const IHFeas = []
        let feas = []
        this.labels = []
        for (let i = 0, len = uniqueThemeLayerData.length; i < len; i++) {
          const feature = uniqueThemeLayerData[i]
          if (feature.geometry) {
            let uniqueFeatrue
            if (
              feature.geometry.componentTypes &&
              feature.geometry.componentTypes[0] === 'SuperMap.Geometry.Polygon'
            ) {
              uniqueFeatrue = feature
              // 处理岛洞
              if (
                feature.geometry.components &&
                feature.geometry.components.length > 1
              ) {
                IHFeas.push(islandHoleHandlerForFeature(feature))
              }
              // if (
              //   feature.geometry.partTopo &&
              //   feature.geometry.partTopo.length > 1
              // ) {
              //   IHFeas.push(islandHoleHandlerForFeature(feature))
              // }
            } else {
              uniqueFeatrue = feature
            }
            uniqueFeatrue.info = this.getDatasource(feature)
            const label = this.getLabel(feature)
            label.centerPoint = feature.geometry.getCentroid()
            // label.y = feature.geometry.getCentroid.y
            this.labels.push(label)
            uniqueFeatrue.attributes = feature.attributes
            feas.push(uniqueFeatrue)
          }
        }
        // 岛洞多面要素必需在其他要素之前添加
        if (IHFeas && IHFeas.length > 1) {
          feas = IHFeas.concat(feas)
        }
        if (this.attributes.label.visible) {
          this.addLabel(this.labels)
        }
        if (this.uniqueThemeLayer) {
          this.uniqueThemeLayer.addFeatures(feas)
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
    openInfoWin(e) {
      let popwin
      if (e.target && e.target.refDataID) {
        const fid = e.target.refDataID
        const feature = this.uniqueThemeLayer.getFeatureById(fid)
        this.dataSource = feature.info
        if (feature && feature.geometry && this.dataSource.length > 0) {
          const popup = document.getElementById('popUnique')
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
    // 仅仅清空单值专题图的要素
    ClearData() {
      if (this.map && this.map.getLayerIndex(this.uniqueThemeLayer) !== -1) {
        this.closeInfoWin()
        this.uniqueThemeLayer.removeAllFeatures()
        if (this.map.getLayerIndex(this.labelVector) !== -1) {
          this.labelVector.removeAllFeatures()
        }
      }
    },
    /**
     * 清空当前单值专题图增加的图层和临时要素
     */
    deactivate() {
      // 此处清空临时图层和临时要素
      if (this.uniqueThemeLayer) {
        this.ClearData()
        this.uniqueThemeLayer.destroy()
        this.uniqueThemeLayer = null
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
