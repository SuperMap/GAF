<template>
  <div>
    <div class="gaf-map-control-position-rightbottom">
      <a-radio-group
        v-model="value"
        :default-value="map2DMode"
        button-style="solid"
        @change="onChange"
      >
        <a-radio-button :value="map2DMode">
          二维<a-icon type="environment" />
        </a-radio-button>
        <a-radio-button :value="map3DMode">
          三维<a-icon type="dribbble" />
        </a-radio-button>
      </a-radio-group>
    </div>
    <div v-show="mapMode === map2DMode">
      <gaf-map2D :map-code="mapCode" @map-loaded="getMap2DViewer" />
    </div>
    <div v-show="mapMode === map3DMode">
      <gaf-map3D :map-code="mapCode" @map-loaded="getMap3DViewer" />
    </div>
  </div>
</template>

<script>
// @ts-nocheck
import gafMap2D from '../../map/classic/GafMap'
import gafMap3D from '../../map3d/webgl/GafMap'
export default {
  components: {
    gafMap2D,
    gafMap3D,
  },
  data() {
    return {
      mapMode: 'Map2D',
      map2DMode: 'Map2D',
      map3DMode: 'Map3D',
      mapCode: `${this.$route.query.code}`,
      map: {},
      map3d: {},
    }
  },
  computed: {},
  created() {},
  methods: {
    getMap2DViewer(gafmap2dViewer) {
      this.map = gafmap2dViewer
    },
    getMap3DViewer(gafmap3dViewer) {
      this.map3d = gafmap3dViewer
    },
    onChange(e) {
      this.mapMode = e.target.value
      switch (this.mapMode) {
        case this.map2DMode:
          // eslint-disable-next-line no-case-declarations
          const centerPostion = this.getCenterPosition()
          if (centerPostion && centerPostion.lon > 0 && centerPostion.lat > 0) {
            this.map.setCenter(
              new SuperMap.LonLat(centerPostion.lon, centerPostion.lat)
            )
          }
          break
        case this.map3DMode:
          // eslint-disable-next-line no-case-declarations
          const bound = this.map.getExtent()
          if (
            bound.left > 0 &&
            bound.bottom > 0 &&
            bound.right > 0 &&
            bound.top > 0
          ) {
            // 设置相机的位置和视角
            this.map3d.scene.camera.setView({
              destination: Cesium.Rectangle.fromDegrees(
                bound.left,
                bound.bottom,
                bound.right,
                bound.top
              ),
            })
          }
          break
        default:
          break
      }
    },
    /* 获取camera中心点坐标 */
    getCenterPosition() {
      const result = this.map3d.camera.pickEllipsoid(
        new Cesium.Cartesian2(
          this.map3d.canvas.clientWidth / 2,
          this.map3d.canvas.clientHeight / 2
        )
      )
      const curPosition = Cesium.Ellipsoid.WGS84.cartesianToCartographic(result)
      const lon = (curPosition.longitude * 180) / Math.PI
      const lat = (curPosition.latitude * 180) / Math.PI
      return {
        lon,
        lat,
      }
    },
  },
}
</script>

<style scoped></style>
