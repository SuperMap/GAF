<template>
  <div class="planAttributeSelectContanier">
    <gaf-map-draggable
      :visible="draggableVisible"
      :width="320"
      :placement="placement"
      title="属性查询"
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
    </gaf-map-draggable>
  </div>
</template>
<script>
const profileInfo = {
  handler: null,
  pickFunction: null,
}
export default {
  name: 'PlanAttributeSelect',
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
      placement: 'right',
      dataList: [],
      dataServiceUrl: '',
      dataSourceName: '',
      nodataTitle: '没有对应属性结果!',
    }
  },
  computed: {
    noData() {
      return this.dataList.length === 0
    },
    projectId() {
      return this.$store.state.projectId
    },
  },
  mounted() {
    this.init()
  },
  destroyed() {
    this.clear()
    profileInfo.handler.destroy()
    profileInfo.pickFunction = undefined
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
        layer.selectedColor = selectedColor
        layer.selectEnabled = self.selectEnabled
        layer.selectColorType = window.Cesium.SelectColorType.REPLACE
      })
    },
    showAttribute() {
      const self = this
      const scene = window.scene
      const layers = scene.layers
      const handler = new window.Cesium.ScreenSpaceEventHandler(scene.canvas)
      profileInfo.handler = handler
      handler.setInputAction(async () => {
        const layerclick = layers.getSelectedLayer()
        if (!layerclick) return
        if (layerclick.getSelection().length > 0) {
          const smid = layerclick.getSelection()[0]
          let layerName = layerclick.name
          if (layerName.length > 33) layerName = layerName.substring(33)
          let feature = await self.getAttributeData(layerName, smid)
          if (!feature) return
          try {
            feature = JSON.parse(feature)
          } catch (error) {
            self.$message.error('属性查询失败,失败原因:' + error)
          }
          self.buildData(feature)
        }
      }, window.Cesium.ScreenSpaceEventType.LEFT_CLICK)
    },
    async getAttributeData(layerName, smid) {
      let attributeData = null
      if (!this.projectId) return attributeData
      const params = {
        projectId: this.projectId,
        layerName,
        smid,
      }
      const result = await this.$api.indexRest.getPlanAttribute(params)
      if (result.status === 1) {
        attributeData = result.element
      }
      return attributeData
    },
    buildData(feature) {
      this.dataList = []
      this.draggableVisible = true
      if (!feature) return
      const result = {}
      for (const key in feature) {
        result.field = key
        result.value = feature[key]
        this.dataList.push({ ...result })
      }
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
      if (profileInfo.handler)
        profileInfo.handler.removeInputAction(
          window.Cesium.ScreenSpaceEventType.LEFT_CLICK
        )
    },
  },
}
</script>
<style lang="less">
.planAttributeSelectContanier {
  /deep/.draggable {
    right: 280px;
    top: 128px;
    .header {
      color: #fff;
    }
    .content:after {
      content: '';
      display: block;
      clear: both;
    }
  }
  .content {
    max-height: 350px;
    overflow-y: auto;
    .ant-col-12 {
      padding: 0 10px 0;
    }
  }
}
</style>
