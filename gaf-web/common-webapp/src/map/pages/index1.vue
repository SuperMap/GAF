<template>
  <gaf-map-viewer :sm-layer-list="smLayerList">
    <div class="search">
      <gaf-query-features :source-url="sourceUrl" />
    </div>
  </gaf-map-viewer>
</template>

<script>
export default {
  data() {
    return {
      sourceUrl:
        'http://www.supermapol.com/realspace/services/data-jinjiang/rest/data/datasources/jinjiang',

      smLayerList: [
        {
          resourceId: 7000,
          resourceLocation:
            'http://www.supermapol.com/realspace/services/map-World/rest/maps/World_Google',
          resourceName: 'China',
          optionCon: [{ minimumTerrainLevel: 0, maximumTerrainLevel: 1 }],
        },
        {
          resourceId: 7001,
          resourceLocation:
            'http://www.supermapol.com/realspace/services/map-World/rest/maps/World_AirLine',
          resourceName: 'China_4326',
          optionCon: [{ minimumTerrainLevel: 1, maximumTerrainLevel: 2 }],
        },
        {
          resourceId: 7002,
          resourceLocation:
            'http://www.supermapol.com/realspace/services/map-World/rest/maps/World_Google',
          resourceName: 'ChinaDark',
          optionCon: [{ minimumTerrainLevel: 2, maximumTerrainLevel: 3 }],
        },
      ],
    }
  },
  mounted() {
    setTimeout(() => {
      this.smLayerList = []
    }, 500)
  },
}
</script>
<style scoped>
.search {
  width: 438px;
  min-height: 172px;
  position: absolute;
  top: 30px;
  left: 30px;
  z-index: 1;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 5px;
  color: rgba(0, 0, 0, 0.65);
  padding: 10px;
}
.ant-btn.ant-btn-loading:not(.ant-btn-circle):not(.ant-btn-circle-outline):not(.ant-btn-icon-only) {
  position: absolute;
  padding-left: auto;
  pointer-events: none;
}
</style>
