<template>
  <div>
    <!--    <span @click="deactivate">清除</span>-->
    <gaf-heat-map-layer
      ref="GafHeatMapLayer"
      :map="map"
      :attributes="Attributes"
    />
    <gaf-cluster-layer
      ref="GafClusterLayer"
      :map="map"
      :attributes="Attributes"
    />
    <gaf-unique-theme-layer
      ref="GafUniqueThemeLayer"
      :map="map"
      :attributes="Attributes"
    />
    <gaf-range-theme-layer
      ref="GafRangeThemeLayer"
      :map="map"
      :attributes="Attributes"
    />
    <gaf-graph-theme-layer
      ref="GafGraphThemeLayer"
      :map="map"
      :attributes="Attributes"
    />
  </div>
</template>

<script>
import GafHeatMapLayer from './HeatMapLayer'
import GafClusterLayer from './ClusterLayer'
import GafUniqueThemeLayer from './UniqueThemeLayer'
import GafRangeThemeLayer from './RangeThemeLayer'
import GafGraphThemeLayer from './GraphThemeLayer'
export default {
  name: 'GafDataVisualization',
  components: {
    GafHeatMapLayer,
    GafClusterLayer,
    GafUniqueThemeLayer,
    GafRangeThemeLayer,
    GafGraphThemeLayer,
  },
  props: {
    map: {
      type: Object,
      default: () => null,
    },
  },
  data() {
    return {
      Initialized: true,
      dataVisualizations: [
        this.$refs.GafHeatMapLayer,
        this.$refs.GafClusterLayer,
        this.$refs.GafUniqueThemeLayer,
        this.$refs.GafRangeThemeLayer,
        this.$refs.GafGraphThemeLayer,
      ],
      Attributes: {},
    }
  },
  watch: {
    map(newMap, oldMap) {
      if (newMap instanceof SuperMap.Map && this.Initialized) {
        this.Initialized = false
      }
    },
  },
  created() {
    this.dataVisualizations = [
      this.$refs.GafHeatMapLayer,
      this.$refs.GafClusterLayer,
      this.$refs.GafUniqueThemeLayer,
      this.$refs.GafRangeThemeLayer,
      this.$refs.GafGraphThemeLayer,
    ]
  },
  mounted() {},
  methods: {
    display(functionData) {
      this.deactivate()
      let type = null
      let dataVisualizationObject = null
      let attributes = null
      let settingData = null
      if (
        functionData &&
        functionData.visualizationInfo &&
        functionData.visualizationInfo.visualizationType
      ) {
        type = functionData.visualizationInfo.visualizationType
        switch (type) {
          case 'HeatMap':
            dataVisualizationObject = this.$refs.GafHeatMapLayer
            settingData =
              // await this.$axios.$get(settingUrl)
              functionData.visualizationInfo.dataVisualizationSetting
            if (settingData) {
              settingData = JSON.parse(settingData)
              if (settingData.featureWeight) {
                settingData.featureWeight = settingData.featureWeight.toUpperCase()
              }
              attributes = {
                ...settingData,
                url: functionData.actionURL,
                actionType: functionData.actionOptionType,
              }
            }
            break
          case 'ClusterLayer':
            dataVisualizationObject = this.$refs.GafClusterLayer
            settingData =
              functionData.visualizationInfo.dataVisualizationSetting
            if (settingData) {
              settingData = JSON.parse(settingData)
              if (settingData.featureWeight) {
                settingData.featureWeight = settingData.featureWeight.toUpperCase()
              }
              attributes = {
                ...settingData,
                url: functionData.actionURL,
                actionType: functionData.actionOptionType,
              }
            }
            break
          case 'UniqueThemeLayer':
            dataVisualizationObject = this.$refs.GafUniqueThemeLayer
            settingData =
              functionData.visualizationInfo.dataVisualizationSetting
            if (settingData) {
              settingData = JSON.parse(settingData)
              if (settingData.themeField) {
                settingData.themeField = settingData.themeField.toUpperCase()
              }
              attributes = {
                ...settingData,
                url: functionData.actionURL,
                actionType: functionData.actionOptionType,
              }
            }
            break
          case 'RangeThemeLayer':
            dataVisualizationObject = this.$refs.GafRangeThemeLayer
            settingData =
              functionData.visualizationInfo.dataVisualizationSetting
            // await this.$axios.$get(settingUrl)
            if (settingData) {
              settingData = JSON.parse(settingData)
              if (settingData.themeField) {
                settingData.themeField = settingData.themeField.toUpperCase()
              }
              attributes = {
                ...settingData,
                url: functionData.actionURL,
                actionType: functionData.actionOptionType,
              }
            }
            break
          case 'GraphThemeLayer':
            dataVisualizationObject = this.$refs.GafGraphThemeLayer
            settingData =
              functionData.visualizationInfo.dataVisualizationSetting
            if (settingData) {
              settingData = JSON.parse(settingData)
              if (
                settingData.themeFields &&
                settingData.themeFields.length > 0
              ) {
                settingData.themeField = settingData.themeFields.map((item) => {
                  return item.name.toUpperCase()
                })
              }
              attributes = {
                ...settingData,
                url: functionData.actionURL,
                actionType: functionData.actionOptionType,
              }
            }
            break
          default:
            break
        }
      } else {
        this.$message.error('功能类型不存在，请检查配置是否正确')
        return
      }
      this.Attributes = attributes
      // 延时0.5s执行，留出更新子组件属性更新的时间
      setTimeout(() => {
        dataVisualizationObject.display()
      }, 500)
    },
    deactivate() {
      if (this.$refs.GafHeatMapLayer) {
        this.$refs.GafHeatMapLayer.deactivate()
      }
      if (this.$refs.GafClusterLayer) {
        this.$refs.GafClusterLayer.deactivate()
      }
      if (this.$refs.GafUniqueThemeLayer) {
        this.$refs.GafUniqueThemeLayer.deactivate()
      }
      if (this.$refs.GafRangeThemeLayer) {
        this.$refs.GafRangeThemeLayer.deactivate()
      }
      if (this.$refs.GafGraphThemeLayer) {
        this.$refs.GafGraphThemeLayer.deactivate()
      }
    },
  },
}
</script>

<style scoped></style>
