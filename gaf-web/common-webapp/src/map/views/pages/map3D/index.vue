<template>
  <gaf-map3D map-mode="Map3D" :map-code="mapCode" @map-loaded="getMapViewer" />
</template>

<script>
// @ts-nocheck
import GafMap3D from '../../map3d/webgl/GafMap'
export default {
  components: {
    GafMap3D,
  },
  data() {
    return {
      mapCode: `${this.$route.query.code}`,
    }
  },
  mounted() {},
  methods: {
    // 子组件传递给父组件方法 地图的viewer
    getMapViewer(mviewer) {
      this.$emit('map-loaded', mviewer)
    },
  },
}
</script>
