<template>
  <!-- <gaf-map-viewer>
    <GafMapGeologicBodyAnalysics :modelUrls="modelUrls"
  /></gaf-map-viewer> -->
  <!-- <sm-viewer>
    <sm3d-geologic-body-analysis
      :model-urls="modelUrls"
    ></sm3d-geologic-body-analysis>
  </sm-viewer> -->
  <div>
    <gaf-map-viewer
      :wmts-layer-list="wmtsLayerList"
      :sm-scene-list="smSceneList"
    >
      <gaf-map-skyline />
    </gaf-map-viewer>
    <a-button @click="handleClick">删除</a-button>
  </div>
</template>

<script>
// import zz from 'gaf-map-ui'
export default {
  data() {
    return {
      modelUrls: [
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer1/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer2/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer3/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer4/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer5/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer6/features/1.json',
      ],
      cameraPosition: {
        // eslint-disable-next-line no-undef
        destination: new Cesium.Cartesian3(
          -2167835.4408299956,
          4423497.534529096,
          4095839.2845661934
        ),
        orientation: {
          heading: 4.029329438295484,
          pitch: -0.23796647219353817,
          roll: 8.994289757424667e-10,
        },
      },
      wmtsLayerList: [
        {
          resourceLocation:
            'http://basemap.nationalmap.gov/arcgis/rest/services/USGSShadedReliefOnly/MapServer/WMTS',
          optionCon: [{ layer: '', tileMatrixSetID: 'default028mm' }],
        },
        // {
        //   resourceLocation:
        //     'https://iserver.supermap.io/iserver/services/maps/wmts100',
        //   layer: '',
        //   tileMatrixSetID: 'Custom_世界地图_Day'
        // }
      ],
      wmsLayerList: [
        {
          resourceLocation:
            'https://nationalmap.gov.au/proxy/http://geoserver.nationalmap.nicta.com.au/geotopo_250k/ows',
          optionCon: [
            {
              layers: 'Hydrography:bores',
              parameters: { format: 'image/png', transparent: true },
            },
          ],
        },
        {
          resourceLocation:
            'https://iserver.supermap.io/iserver/services/map-china400/wms111/China',
          optionCon: [
            {
              layers: '0.1,0.2,0.3,0.4,0.5,0.6,0.7',
              parameters: {
                format: 'image/png',
                transparent: true,
              },
            },
          ],
        },
      ],
      smSceneList: [
        {
          resourceId: '8000',
          resourceLocation:
            'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD',
        },
      ],
    }
  },
  mounted() {
    window.scene.camera.setView(this.cameraPosition)
  },
  // mounted() {
  //   console.log('1', window.scene.layers.layerQueue)
  // },
  methods: {
    handleClick() {
      this.wmtsLayerList = []
      // zz.zoomOut()
      // console.log('zz', zz.zoomOut)
    },
  },
}
</script>
