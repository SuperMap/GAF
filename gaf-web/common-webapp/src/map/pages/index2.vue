<template>
  <div>
    <gaf-map-viewer>
      <!-- 测量 -->
      <sm3d-measure></sm3d-measure>

      <!-- 可视域 -->
      <!-- <sm3d-viewshed></sm3d-viewshed> -->

      <!-- 天际线 -->
      <!-- <sm3d-skyline></sm3d-skyline> -->

      <!-- 剖面 -->
      <!-- <sm3d-profile></sm3d-profile> -->

      <!-- 通视 -->
      <!-- <sm3d-sightline></sm3d-sightline> -->

      <!-- 阴影 -->
      <!-- <sm3d-shadowquery></sm3d-shadowquery> -->

      <!-- 地质体 -->
      <!-- <sm3d-geologic-body-analysis
        :model-urls="modelUrls"
      ></sm3d-geologic-body-analysis> -->
    </gaf-map-viewer>
  </div>
</template>

<script>
export default {
  data() {
    return {
      baseMap:
        'https://iserver.supermap.io/iserver/services/map-world/rest/maps/世界地图_Night',
      scene: {
        url:
          'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace',
        name: '',
      },
      layerList: [
        {
          url:
            'https://iserver.supermap.io/iserver/services/map-jingjin/rest/maps/京津地区地图',
          optional: { alpha: 1 },
        },
        {
          url:
            'http://www.supermapol.com/realspace/services/map-jingjin/rest/maps/京津地区土地利用现状图',
          optional: { alpha: 0.7 },
        },
        {
          url:
            'http://www.supermapol.com/realspace/services/map-jingjin/rest/maps/京津地区人口分布图_专题图',
          optional: { alpha: 0.5 },
        },
        {
          url:
            'http://www.supermapol.com/realspace/services/map-jingjin/rest/maps/京津地区地貌分布图',
          optional: { alpha: 0.2 },
        },
      ],
      modelUrls: [
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer1/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer2/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer3/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer4/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer5/features/1.json',
        'http://www.supermapol.com/realspace/services/data-dizhiti/rest/data/datasources/%E5%9C%B0%E8%B4%A8%E4%BD%93/datasets/Layer6/features/1.json',
      ],
    }
  },
}
</script>
