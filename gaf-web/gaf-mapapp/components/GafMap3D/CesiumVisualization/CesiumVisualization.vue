<template>
  <div>
    <unique-theme-map
      ref="UniqueThemeMap"
      :map="map"
      :source-data="sourceData"
    />
    <range-theme-map ref="RangeThemeMap" :map="map" :source-data="sourceData" />
  </div>
</template>

<script>
import UniqueThemeMap from './UniqueThemeMap/UniqueThemeMap'
import RangeThemeMap from './RangeThemeMap/RangeThemeMap'
export default {
  name: 'CesiumVisualization',
  components: {
    UniqueThemeMap,
    RangeThemeMap,
  },
  props: {
    map: {
      type: Object,
      required: false,
      default() {
        return {}
      },
    },
  },
  data() {
    return {
      // 后端获取的元数据
      sourceData: {},
    }
  },
  methods: {
    // 根据传入的ID获取对应的数据
    display(functionData) {
      this.clear()
      let type = null
      let dataVisualizationObject = null
      if (
        functionData.visualizationInfo &&
        functionData.visualizationInfo.visualizationType
      ) {
        type = functionData.visualizationInfo.visualizationType
        switch (type) {
          case 'HeatMap':
            break
          case 'ClusterLayer':
            dataVisualizationObject = this.$refs.GafClusterLayer
            break
          case 'UniqueThemeLayer':
            dataVisualizationObject = this.$refs.UniqueThemeMap
            break
          case 'RangeThemeLayer':
            dataVisualizationObject = this.$refs.RangeThemeMap
            break
          case 'GraphThemeLayer':
            dataVisualizationObject = this.$refs.GafGraphThemeLayer
            break
          default:
            break
        }
        if (dataVisualizationObject) {
          this.sourceData = functionData
          // 延时0.5s执行，留出更新子组件属性更新的时间
          setTimeout(() => {
            dataVisualizationObject.display()
          }, 500)
        }
      }
    },
    /**
     * 清空绘制层
     */
    clear() {
      // todo:将当前场景中添加的图层都清空删除
      if (this.map && this.map.entities) {
        this.map.entities.removeAll()
      }
    },
  },
}
</script>

<style scoped></style>
