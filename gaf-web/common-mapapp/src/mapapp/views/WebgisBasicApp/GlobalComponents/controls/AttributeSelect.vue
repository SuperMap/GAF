<template>
  <div class="AttributeSelectContanier">
    <gaf-map-draggable
      :visible="draggableVisible"
      :width="360"
      title="属性查询"
      panel-func="tools"
      @cancel="handleCloseButton"
    >
      <span v-if="noData" style="padding: 0 5px">{{ nodataTitle }}</span>
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
      <template v-if="showBimViewerBtn" #actions>
        <span class="bim-viewer-btn" @click="linkToBimViewer">BIM管理</span>
      </template>
    </gaf-map-draggable>
  </div>
</template>
<script>
const profileInfo = {
  pickFunction: null,
}
export default {
  name: 'AttributeSelect',
  props: {
    params: {
      type: String,
      default: () => '#fff',
    },
  },
  data() {
    return {
      draggableVisible: false,
      selectEnabled: true,
      dataList: [],
      dataServiceUrl: '',
      dataSourceName: '',
      nodataTitle: '没有对应属性结果!',
      showBimViewerBtn: false,
    }
  },
  computed: {
    noData() {
      return this.dataList.length === 0
    },
  },
  mounted() {
    this.init()
  },
  destroyed() {
    this.clear()
  },
  methods: {
    init() {
      this.highlightFeature(this.params)
      this.showAttribute()
    },
    highlightFeature(color) {
      const self = this
      const layers = window.scene.layers._layerQueue
      if (!layers || layers.length === 0) return false
      // eslint-disable-next-line new-cap
      const selectedColor = new window.Cesium.Color.fromCssColorString(
        color || '#3498db'
      )
      layers.forEach((layer) => {
        if (layer.name === 'QuanS') return
        layer.selectedColor = selectedColor
        layer.selectEnabled = self.selectEnabled
        layer.selectColorType = window.Cesium.SelectColorType.REPLACE
      })
    },
    showAttribute() {
      const viewer = window.viewer
      const scene = window.scene
      profileInfo.pickFunction = (feature) => {
        this.dataList = []
        this.draggableVisible = true
        if (!feature) return
        const result = {}
        for (const key in feature) {
          result.field = key
          result.value = feature[key]
          this.dataList.push({ ...result })
        }

        const selectedLayer = scene.layers.getSelectedLayer()
        const resourceName = selectedLayer.layerInfo.resourceName
        // TODO暂时用名字判断
        this.showBimViewerBtn = resourceName.includes('不动产档案馆')
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
      try {
        layer && layer.releaseSelection()
      } catch (error) {
        this.$message.warn(error)
      }
    },
    clear() {
      this.selectEnabled = false
      this.highlightFeature()
      this.unHighlightFeature()
      if (profileInfo.pickFunction)
        window.viewer.pickEvent.removeEventListener(profileInfo.pickFunction)
      profileInfo.pickFunction = undefined
    },
    linkToBimViewer() {
      this.$bus.$emit('link-to-bim-viewer')
    },
  },
}
</script>
<style lang="less">
.AttributeSelectContanier {
  .content {
    max-height: 350px;
    overflow-y: auto;
    .ant-col-12 {
      padding: 0 10px 0;
    }
  }
  .bim-viewer-btn {
    cursor: pointer;
    font-size: 10px;
    margin-right: 5px;
  }
}
</style>
