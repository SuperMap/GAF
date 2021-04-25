<template>
  <div class="householdContanier">
    <gaf-map-draggable
      :visible="draggableVisible"
      :width="360"
      title="分层分户查询"
      panel-func="tools"
      @cancel="handleCloseButton"
    >
      <div class="optionItem">
        <a-row>
          <a-col :span="6">
            <label style="margin-top: 8px">沿X轴偏移</label>
          </a-col>
          <a-col :span="16">
            <a-slider v-model="offsetX" :min="-10" :max="10" :step="1" />
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="6">
            <label style="margin-top: 8px">沿Y轴偏移</label>
          </a-col>
          <a-col :span="16">
            <a-slider v-model="offsetY" :min="-10" :max="10" :step="1" />
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="6">
            <label style="margin-top: 8px">沿z轴偏移</label>
          </a-col>
          <a-col :span="16">
            <a-slider v-model="offsetZ" :min="-10" :max="10" :step="1" />
          </a-col>
        </a-row>
      </div>
      <div class="optionItem">
        <span v-if="noData">{{ noDataTitle }}</span>
        <a-row
          v-for="(item, index) in dataList"
          :key="index"
          style="margin-bottom: 5px"
        >
          <a-col :span="10">
            <span>{{ item.field + ':' }}</span>
          </a-col>
          <a-col :span="12">
            <span>{{ item.value }}</span>
          </a-col>
        </a-row>
      </div>
    </gaf-map-draggable>
  </div>
</template>
<script>
const profileInfo = {
  handler: null,
  pickFunction: null,
  currentLayer: null,
}
export default {
  name: 'HierarchicalHousehold',
  props: {
    params: {
      type: String,
      default: () => '#fff',
    },
  },
  data() {
    return {
      draggableVisible: false,
      dataList: [],
      dataServiceUrl: '',
      dataSourceName: '',
      noDataTitle: '没有对应属性结果!',
      offsetX: 5,
      offsetY: 0,
      offsetZ: 0,
    }
  },
  computed: {
    noData() {
      return this.dataList.length === 0
    },
  },
  watch: {
    offsetX(val) {
      if (profileInfo.currentLayer) {
        profileInfo.currentLayer.selectedTranslate = new window.Cesium.Cartesian3(
          val,
          this.offsetY,
          this.offsetZ
        )
      }
    },
    offsetY(val) {
      if (profileInfo.currentLayer) {
        profileInfo.currentLayer.selectedTranslate = new window.Cesium.Cartesian3(
          this.offsetX,
          val,
          this.offsetZ
        )
      }
    },
    offsetZ(val) {
      if (profileInfo.currentLayer) {
        profileInfo.currentLayer.selectedTranslate = new window.Cesium.Cartesian3(
          this.offsetX,
          this.offsetY,
          val
        )
      }
    },
  },
  mounted() {
    const { isHierarchicalHousehold } =
      window.SMWEBGIS.currentLocateService || {}
    if (!isHierarchicalHousehold) {
      this.$message.warning('当前图层不支持分层分户查询!')
      this.$emit('draggableClose')
      return false
    }
    const layers = window.scene.layers._layerQueue
    let layerInfo = null
    layers.forEach((layer) => {
      layerInfo = layer.layerInfo
      if (layerInfo) {
        if (layerInfo.isHierarchicalHousehold) {
          layer.selectEnabled = true
          layer.minTransparentAlpha = 0
        }
      }
    })
    this.init()
  },
  destroyed() {
    this.clear()
    profileInfo.handler.destroy()
    profileInfo.pickFunction = undefined
    profileInfo.currentLayer = undefined
  },
  methods: {
    init() {
      const self = this
      const viewer = window.viewer
      const scene = window.scene
      const layers = scene.layers
      const handler = new window.Cesium.ScreenSpaceEventHandler(scene.canvas)
      profileInfo.handler = handler
      handler.setInputAction(() => {
        const vectorLayer = layers.getSelectedLayer()
        if (!vectorLayer) return
        self.draggableVisible = true
        profileInfo.currentLayer = vectorLayer
        vectorLayer.removeAllObjsOffset()
        vectorLayer.cullEnabled = false
        vectorLayer.selectionFiltrateByTransparency = 0
        vectorLayer.selectColorType = 1.0
        vectorLayer.selectedColor = window.Cesium.Color.RED
        vectorLayer.selectedTranslate = new window.Cesium.Cartesian3(
          self.offsetX,
          self.offsetY,
          self.offsetZ
        )
        if (vectorLayer.getSelection().length > 0) {
          const selectedId = Number(vectorLayer.getSelection()[0])
          vectorLayer.setObjsOffset([selectedId])
        }
      }, window.Cesium.ScreenSpaceEventType.LEFT_CLICK)
      profileInfo.pickFunction = function (feature) {
        self.dataList = []
        self.draggableVisible = true
        if (!feature) return
        const result = {}
        for (const key in feature) {
          result.field = key
          result.value = feature[key]
          self.dataList.push({ ...result })
        }
      }
      viewer.pickEvent.addEventListener(profileInfo.pickFunction)
    },
    handleCloseButton() {
      this.$emit('draggableClose')
      this.draggableVisible = false
    },
    unHighlightFeature() {
      const scene = window.scene
      const Cesium = window.Cesium
      if (!scene || !Cesium) {
        return
      }
      const layer = scene.layers.getSelectedLayer()
      layer && layer.releaseSelection()
    },
    clear() {
      if (profileInfo.currentLayer) {
        profileInfo.currentLayer.removeAllObjsOffset()
        profileInfo.currentLayer.releaseSelection()
        profileInfo.currentLayer.selectedColor = new window.Cesium.Color(
          0.7,
          0.7,
          1,
          1
        )
        profileInfo.currentLayer.selectedTranslate = new window.Cesium.Cartesian3(
          0,
          0,
          0
        )
      }
      if (profileInfo.handler)
        profileInfo.handler.removeInputAction(
          window.Cesium.ScreenSpaceEventType.LEFT_CLICK
        )
      if (profileInfo.pickFunction)
        window.viewer.pickEvent.removeEventListener(profileInfo.pickFunction)
    },
  },
}
</script>
<style lang="less" scope>
.householdContanier {
  .draggable {
    z-index: 99;
  }
  .optionItem {
    margin-bottom: 20px;
    padding-bottom: 10px;
    color: #fff;
    border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  }
  .optionItem:last-child {
    border: none;
    margin-bottom: 0;
    padding-bottom: 0;
    max-height: 220px;
    overflow-y: auto;
  }
}
::-webkit-scrollbar-thumb {
  /*滚动条里面小方块*/
  border-radius: 8px;
  // box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  background: rgba(207, 211, 216, 0.3);
}
</style>
