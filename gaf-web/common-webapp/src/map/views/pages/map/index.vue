<template>
  <gaf-map
    map-mode="Map2d"
    :map-code="mapCode"
    :center-point="centerPoint"
    @map-loaded="getMapViewer"
  ></gaf-map>
</template>

<script>
// @ts-nocheck
import GafMap from '../../map/classic/GafMap'
export default {
  components: {
    GafMap,
  },
  data() {
    return {
      mapCode: `${this.$route.query.code}`,
      centerPoint: { x: 0, y: 0 },
    }
  },
  methods: {
    // 子组件传递给父组件方法 地图的viewer
    getMapViewer(mviewer) {
      this.$emit('map-loaded', mviewer)
    },
  },
}
</script>
<style scoped></style>
