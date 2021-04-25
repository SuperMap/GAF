<template>
  <span />
</template>

<script>
export default {
  name: 'DataVisualizationTemplateLayer',
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
      Initialized: true,
      dataVisualizationLayer: null,
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
     * 开始渲染XXX专题图
     */
    display() {
      // 1、创建XXX专题图图层，加到当前地图中
      this.CreatLayer()
      // 2、设置图层的样式和属性信息
      this.SetLayerPropertyStyle()
      // 3、根据url或者Data初始化热力图的内容，并显示
      this.LoadData()
    },
    /**
     * 创建XXX专题图图层 并将此图层增加到地图中
     */
    CreatLayer() {
      // 如果当前XXX专题图没有被创建过
      if (!this.dataVisualizationLayer) {
        // 创建一个名为“dataVisualizationLayer” 的热点渲染图层。
        const dataVisualizationLayer = '这里创建图层'
        this.map.addLayers([dataVisualizationLayer])
        this.dataVisualizationLayer = dataVisualizationLayer
      }
    },
    /**
     * 设置图层样式和属性
     */
    SetLayerPropertyStyle() {
      // 图层判断空
      if (this.dataVisualizationLayer) {
      }
    },
    /**
     * 根据url或者Data初始化热力图的内容，并显示
     */
    LoadData() {
      // // 读取数据
      // let dataVisualizationLayerData = null
      // if (this.dataVisualizationLayer && this.data) {
      //   dataVisualizationLayerData = this.attributes.data
      // }
      // // 读取Url地址内容
      // else if (this.dataVisualizationLayer && this.attributes.url) {
      //   dataVisualizationLayerData = await this.$axios.$get(this.attributes.url)
      // }
      // 这里使用读取的数据 初始化可视化图层
    },
    // 仅仅清空XXX专题图的要素
    ClearData() {
      if (this.dataVisualizationLayer) {
        this.dataVisualizationLayer.removeAllFeatures()
      }
    },
    /**
     * 清空当前XXX专题图增加的图层和临时要素
     */
    deactivate() {
      // 此处清空临时图层和临时要素
      if (this.dataVisualizationLayer) {
        this.ClearData()
        this.dataVisualizationLayer.destroy()
        this.dataVisualizationLayer = null
      }
    },
  },
}
</script>

<style scoped></style>
