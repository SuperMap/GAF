<template>
  <div>
    <span v-show="false" id="popCluster">
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
let root = {}
let clusterLayerData = null
export default {
  name: 'ClusterLayer',
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
      clusterLayer: null,
      title: '',
      dataSource: [],
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
        if (!newAttributes.radius || !newAttributes.radius > 0) {
          newAttributes.radius = 12
        }
      }
    },
  },
  created() {},
  mounted() {},
  methods: {
    /**
     * 开始渲染聚合图
     */
    display() {
      // 1、创建聚合图图层，加到当前地图中
      this.CreatLayer()
      // 2、设置图层的样式和属性信息
      this.SetLayerPropertyStyle()
      // 3、根据url或者Data初始化热力图的内容，并显示
      this.LoadData()
    },
    /**
     * 创建聚合图图层 并将此图层增加到地图中
     */
    CreatLayer() {
      if (!this.map) {
        this.$message.error('当前底图不存在')
        return
      }
      // 如果当前聚合图没有被创建过
      if (!this.clusterLayer) {
        // 创建一个名为-Cluster 的热点渲染图层。
        const clusterLayer = new SuperMap.Layer.ClusterLayer('Cluster')
        this.map.addLayers([clusterLayer])
        this.clusterLayer = clusterLayer
      }
    },
    /**
     * 设置图层样式和属性
     */
    SetLayerPropertyStyle() {
      root = this
      // 图层判断空
      if (this.clusterLayer) {
        // 创建聚散选择控件。该控件实现了聚散图层的鼠标事件。
        const select = new SuperMap.Control.SelectCluster(this.clusterLayer, {
          callbacks: {
            click(f) {
              // 当点击聚散点的时候不弹出信息窗口
              root.closeInfoWin()
              if (!f.isCluster) {
                // 点击兴趣点弹出信息窗口
                if (root.attributes.property.visible) {
                  root.openInfoWin(f)
                }
              }
            },
            clickout() {
              // 点击空白处关闭信息窗口
              root.closeInfoWin()
            },
          },
        })
        // 将控件添加到map上
        this.map.addControl(select)
        this.clusterLayer.events.on({
          moveend(e) {
            // 注册moveend事件，当缩放的时候关闭信息窗口
            if (e && e.zoomChanged) {
              root.closeInfoWin()
            }
          },
        })
        this.clusterLayer.events.on({
          clusterend(e) {
            // e包含了聚散完成所需要的信息，其结构如下e={clusterPoints:[],displayedPoints:[],element:null,object:null,type:"clusterEnd"}
            // 其中，clusterMaps是包含了聚散点映射关系集合，clusterPoints[i]则表示第i个聚散点映射关系，其类型为{SuperMap.Feature.Vector}，其内的children属性包含有对应的实际点坐标
            // 而displayedPoints则是用户所设定的某一范围内不需要被聚散的点集合
          },
        })
        // 激活控件。
        select.activate()
      }
    },
    /**
     * 根据url或者Data初始化热力图的内容，并显示
     */
    LoadData() {
      // 读取数据
      if (this.clusterLayer && this.attributes.data) {
        clusterLayerData = this.attributes.data
      }
      // 读取Url地址内容
      else if (this.clusterLayer && this.attributes.url) {
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
        clusterLayerData = result.data
        if (clusterLayerData && clusterLayerData.length > 0) {
          this.loadClusterDataByAPI()
        }
      } else {
        this.spinning = false
        this.$message.error(result.message)
      }
    },
    loadClusterDataByAPI() {
      const ps = []
      for (let i = 0; i < clusterLayerData.length; i++) {
        const item = clusterLayerData[i]
        const point = item.centerPoint
        const feature = new SuperMap.Feature.Vector()
        feature.geometry = new SuperMap.Geometry.Point(point.x, point.y)
        feature.style = {
          pointRadius: this.attributes.radius,
          graphic: true,
          externalGraphic: './theme/images/marker.png',
          // 如果设置宽高，则radius配置不起作用
          // graphicWidth: 12,
          // graphicHeight: 12
        }
        let datasource = []
        if (root.attributes.property.fields) {
          root.title = root.attributes.property.title
          datasource = root.attributes.property.fields.map((field) => {
            const source = {
              name: field.name,
              value: item.attributes[field.name],
            }
            return source
          })
        }
        feature.info = datasource
        ps.push(feature)
      }
      if (this.clusterLayer) {
        this.clusterLayer.addFeatures(ps)
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
        clusterLayerData = features
        this.loadClusterDataBySql()
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
        this.$message.error('服务中不含有聚合图对应的数据')
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
    loadClusterDataBySql() {
      const clusterPoints = clusterLayerData.map((item) => {
        const clusterPoint = new SuperMap.Feature.Vector()
        clusterPoint.geometry = new SuperMap.Geometry.Point(
          item.geometry.getBounds().getCenterLonLat().lon,
          item.geometry.getBounds().getCenterLonLat().lat
        )
        clusterPoint.style = {
          pointRadius: root.attributes.radius,
          graphic: true,
          externalGraphic: './theme/images/marker.png',
          // 如果设置宽高，则radius配置不起作用
          // graphicWidth: 12,
          // graphicHeight: 12
        }
        const datasource = []
        if (root.attributes.property.fields) {
          for (let i = 0; i < root.attributes.property.fields.length; i++) {
            const field = root.attributes.property.fields[i]
            root.title = root.attributes.property.title
            datasource.push({
              name: field.name,
              value: item.attributes[field.name.toUpperCase()],
            })
          }
        }
        clusterPoint.info = datasource
        return clusterPoint
      })
      if (this.clusterLayer) {
        this.clusterLayer.addFeatures(clusterPoints)
      }
      this.spinning = false
    },
    openInfoWin(f) {
      let popwin
      const popup = document.getElementById('popCluster')
      const feature = this.clusterLayer.getFeatureById(f.id)
      const lonLat = feature.geometry.getBounds().getCenterLonLat()
      this.dataSource = feature.info
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
    },
    closeInfoWin() {
      this.map.removeAllPopup()
      this.dataSource = []
    },
    // 仅仅清空聚合图的要素
    ClearData() {
      if (this.map.getLayerIndex(this.clusterLayer) !== -1) {
        this.closeInfoWin()
        this.clusterLayer.removeAllFeatures()
      }
    },
    /**
     * 清空当前聚合图增加的图层和临时要素
     */
    deactivate() {
      // 此处清空临时图层和临时要素
      if (this.clusterLayer) {
        this.ClearData()
        // this.clusterLayer.destroy()
        // this.clusterLayer = null
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
