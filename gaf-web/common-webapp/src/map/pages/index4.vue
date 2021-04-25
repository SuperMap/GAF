<template>
  <div>
    <gaf-map-viewer>
      <!-- <gaf-map-measure /> -->
      <!-- <gaf-map-viewshed /> -->
      <!-- <gaf-map-profile /> -->
      <!-- <gaf-map-skyline /> -->
      <!-- <gaf-map-sightline /> -->
      <!-- <gaf-map-shadowquery /> -->
      <!-- <gaf-map-terrain-operation /> -->
      <!-- <gaf-map-terrain-flood /> -->
      <!-- <gaf-map-terrain-slope /> -->
      <!-- <gaf-map-terrain-isoline /> -->
      <gaf-map-clip-box />
      <!-- <gaf-map-clip-cross /> -->
      <!-- <gaf-map-clip-plane />
      <gaf-map-clip-polygon /> -->
      <!-- <GafMapGeologicBodyAnalysics :modelUrls="modelUrls" /> -->
    </gaf-map-viewer>
  </div>
</template>

<script>
export default {
  data() {
    return {
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
      modelUrls: [
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer1/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer2/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer3/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer4/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer5/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer6/features/1.json',
      ],
      smLayerList: [
        {
          resourceLocation:
            'http://support.supermap.com.cn:8090/iserver/services/map-china400/rest/maps/China',
          resourceName: 'China',
          optionCon: [{ minimumTerrainLevel: 0, maximumTerrainLevel: 1 }],
        },
        {
          resourceLocation:
            'http://support.supermap.com.cn:8090/iserver/services/map-china400/rest/maps/China_4326',
          resourceName: 'China_4326',
          optionCon: [{ minimumTerrainLevel: 1, maximumTerrainLevel: 2 }],
        },
        {
          resourceLocation:
            'http://support.supermap.com.cn:8090/iserver/services/map-china400/rest/maps/ChinaDark',
          resourceName: 'ChinaDark',
          optionCon: [{ minimumTerrainLevel: 2, maximumTerrainLevel: 3 }],
        },
        // {
        //   resourceLocation:
        //     'http://support.supermap.com.cn:8090/iserver/services/map-jingjin/rest/maps/%E4%BA%AC%E6%B4%A5%E5%9C%B0%E5%8C%BA%E5%9C%9F%E5%9C%B0%E5%88%A9%E7%94%A8%E7%8E%B0%E7%8A%B6%E5%9B%BE',
        //   resourceName: '京津地区地图',
        //   optionCon: [{ minimumTerrainLevel: 2, maximumTerrainLevel: 3 }]
        // },
        // {
        //   resourceLocation:
        //     'http://support.supermap.com.cn:8090/iserver/services/map-jingjin/rest/maps/%E4%BA%AC%E6%B4%A5%E5%9C%B0%E5%8C%BA%E4%BA%BA%E5%8F%A3%E5%88%86%E5%B8%83%E5%9B%BE_%E4%B8%93%E9%A2%98%E5%9B%BE',
        //   resourceName: '京津地区土地利用现状图',
        //   optionCon: [{ minimumTerrainLevel: 2, maximumTerrainLevel: 8 }]
        // },
        // {
        //   resourceLocation:
        //     'http://support.supermap.com.cn:8090/iserver/services/map-jingjin/rest/maps/%E4%BA%AC%E6%B4%A5%E5%9C%B0%E5%8C%BA%E5%9F%8E%E4%B9%A1%E5%BB%BA%E8%AE%BE%E7%94%A8%E5%9C%B0%E8%A7%84%E6%A8%A1%E6%8E%A7%E5%88%B6%E5%9B%BE',
        //   resourceName: '京津地区城乡建设用地规模控制图',
        //   optionCon: [{ minimumTerrainLevel: 3, maximumTerrainLevel: 7 }]
        // },
        // {
        //   resourceLocation:
        //     'http://support.supermap.com.cn:8090/iserver/services/map-jingjin/rest/maps/%E4%BA%AC%E6%B4%A5%E5%9C%B0%E5%8C%BA%E5%9C%B0%E8%B2%8C%E5%88%86%E5%B8%83%E5%9B%BE',
        //   resourceName: '京津地区地貌分布图',
        //   optionCon: [{ minimumTerrainLevel: 2, maximumTerrainLevel: 6 }]
        // },
        // {
        //   resourceLocation:
        //     'http://support.supermap.com.cn:8090/iserver/services/map-jingjin/rest/maps/%E4%BA%AC%E6%B4%A5%E5%9C%B0%E5%8C%BA%E5%9F%8E%E9%95%87%E5%B7%A5%E7%9F%BF%E7%94%A8%E5%9C%B0%E8%A7%84%E6%A8%A1%E6%8E%A7%E5%88%B6%E5%9B%BE',
        //   resourceName: '京津地区城镇工矿用地规模控制图',
        //   optionCon: [{ minimumTerrainLevel: 4, maximumTerrainLevel: 5 }]
        // }
      ],
      smSceneList: [
        {
          resourceLocation:
            'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD',
        },
        // {
        //   resourceLocation:
        //     'http://www.supermapol.com/realspace/services/3D-ZF_normal/rest/realspace/datas/srtm_54_07@zhufeng'
        // },
        // {
        //   resourceLocation:
        //     'http://www.supermapol.com/realspace/services/3D-ZF_normal/rest/realspace/datas/image'
        // }
      ],
      arcLayerList: [],
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
    }
  },
  mounted() {
    window.scene.camera.setView(this.cameraPosition)
  },
}
</script>
