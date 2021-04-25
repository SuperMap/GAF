<template>
  <gaf-map-viewer :sm-layer-list="smLayerList" :sm-scene-list="smSceneList">
    <gaf-map-tools :show-icon-only="false" :content="content" />
    <!-- <component v-bind:is="currentTabComponent"></component> -->
  </gaf-map-viewer>
</template>
<script>
export default {
  data() {
    return {
      // currentTabComponent: 'gaf-map-measure',
      smSceneList: [
        // {
        //   resourceLocation:
        //     'http://www.supermapol.com/realspace/services/3D-dixingyingxiang/rest/realspace/datas/DatasetDEM'
        // },
        // {
        //   resourceLocation:
        //     'http://www.supermapol.com/realspace/services/3D-dixingyingxiang/rest/realspace/datas/MosaicResult'
        // }
        {
          resourceId: '8000',
          resourceLocation:
            'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD',
          optionCon: [
            {
              visibleDistanceMin: 0,
              visibleDistanceMax: 500,
              name: 'Building@CBD',
            },
            {
              visibleDistanceMin: 0,
              visibleDistanceMax: 700,
              name: 'Lake@CBD',
            },
            {
              visibleDistanceMin: 0,
              visibleDistanceMax: 1000,
              name: 'Tree@CBD',
            },
          ],
        },
      ],
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
      content: [
        {
          name: 'zoomOut',
        },
        {
          name: 'zoomIn',
        },
        {
          name: 'splitScreen',
        },
        {
          name: 'measureTool',
        },
        {
          name: 'switchMode',
        },

        {
          name: 'fullMap',
          icon: '',
        },
        {
          name: 'sm3d-measure',
          icon: '',
          type: 'panel',
        },
        {
          name: 'sm3d-viewshed',
          type: 'panel',
        },
        {
          name: 'sm3d-skyline',
          type: 'panel',
        },
        {
          name: 'sm3d-profile',
          type: 'panel',
        },
        {
          name: 'sm3d-sightline',
          type: 'panel',
        },
        {
          name: 'sm3d-shadowquery',
          type: 'panel',
        },
      ],
    }
  },
}
</script>
<style scoped>
.search {
  width: 360px;
  position: absolute;
  top: 30px;
  left: 30px;
  z-index: 1;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 5px;
}
</style>
