/**
 * SMWEBGIS window对象上的全局变量，SM为SuperMap的简写，WEBGIS代表二三维一体化
 */
const host = 'http://1.202.165.56:8090'

  ; (function (root) {
    root.SMWEBGIS = root.SMWEBGIS || {}
    root.SMWEBGIS.TreeLayers = {}
    root.SMWEBGIS.isDebug = true
    root.SMWEBGIS.bottomInDir = false
    root.SMWEBGIS.supportBackEnd = true
    root.SMWEBGIS.host = host
    root.SMWEBGIS.backEndUrl = 'http://10.80.253.150:8080'
    root.SMWEBGIS.dirs = {
      // 天地图token
      // TOKEN_TIANDITU: '4a00a1dc5387b8ed8adba3374bd87e5e',
      TOKEN_TIANDITU: 'de8a949b283cb793ef2d5c98f2f50fe5',
      // 默认底图，地图 map，地形 terrain，影像 image
      // default_bottom: 'image',
      whiteModelName: 'WuHanBaiMo',
      currentCity: '武汉',
      // whiteModelName: 'CQmodel',
      // 数据服务统一配置
      setMouseOperation: false,
      disableUndergroundMode: false,
      dataServiceConfig: {
        url: host + '/iserver/services/data-cim_gis/rest/data',
        dataSourceName: 'cim-busi', // 数据源
        keyWord: 'SmID',
      },
      baseLayers: [
        // 天地图底图
        {
          resourceId: 9999,
          isDefault: false,
          resourceName: '地图',
          isBaseLayer: true,
          sourceType: 'MAPWORLD',
          serviceType: 'VEC_C',
          // thumbnailAddr: "./img/map_thumb_btn1.png"
        },
        {
          resourceId: 9998,
          isDefault: false,
          resourceName: '地形',
          isBaseLayer: true,
          sourceType: 'MAPWORLD',
          serviceType: 'TER_C',
          // thumbnailAddr: "./img/map_thumb_btn2.png"
        },
        {
          resourceId: 9997,
          isDefault: true,
          resourceName: '影像',
          isBaseLayer: true,
          sourceType: 'MAPWORLD',
          serviceType: 'IMG_C',
          // thumbnailAddr: "./img/map_thumb_btn3.png"
        },
        // 超图底图
        // {
        //   resourceId: 9996,
        //   type: 'map',
        //   resourceName: '地图',
        //   resourceTag: 'RESTMAP',
        //   isBaseLayer: true,
        //   sourceType: 'SUPERMAP',
        //   resourceLocation:
        //     'http://www.supermapol.com/realspace/services/map-sample/rest/maps/WorldMap',
        // },
        // {
        //   resourceId: 9995,
        //   type: 'terrain',
        //   resourceName: '地形',
        //   resourceTag: 'RESTMAP',
        //   isBaseLayer: true,
        //   sourceType: 'SUPERMAP',
        //   location: true,
        //   resourceLocation:
        //     'http://www.supermapol.com/realspace/services/map-China400/rest/maps/China400'
        // },
        // {
        //   resourceId: 9994,
        //   type: 'image',
        //   resourceTag: 'RESTMAP',
        //   resourceName: '影像',
        //   isBaseLayer: true,
        //   location: true,
        //   sourceType: 'SUPERMAP',
        //   resourceLocation:
        //     'http://www.supermapol.com/realspace/services/map-World/rest/maps/World_Image'
        // },
      ],
      branch: [
        {
          resourceId: 1,
          resourceName: '2维图层',
          pid: '',
        },
        {
          resourceId: 2,
          resourceName: '3维数据',
          pid: '',
        },
        {
          resourceId: 24,
          resourceName: '倾斜',
          pid: 2,
        },
        {
          resourceId: 25,
          resourceName: 'BIM',
          pid: 2,
        },
        {
          resourceId: 7181,
          pid: 1,
          resourceName: '武汉行政区',
          resourceLocation:
            host + '/iserver/services/3D-cim_gis/rest/realspace/datas/wuh_xzqh',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
        },
        {
          resourceId: 7170,
          pid: 1,
          resourceName: '退界红线',
          resourceLocation:
            host + '/iserver/services/3D-cim_gis/rest/realspace/datas/dlhx',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          dataSetName: 'dlhx',
          isAttributesSave: true,
        },
        {
          resourceId: 7191,
          pid: 1,
          resourceName: '武汉底图',
          resourceLocation:
            host +
            '/iserver/services/map-mvt-DiTuMVT/rest/maps/%E5%BA%95%E5%9B%BEMVT',
          resourceTag: 'RESTMAP',
          resourceTagCN: '地图服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          zIndex: 9,
        },
      ],

      home: [
        {
          resourceId: 2005,
          pid: "",
          resourceName: '白模分层分户',
          resourceLocation: 'http://192.168.192.100:31824/iserver/services/3D-wh100-wh100/rest/realspace/datas/wh100@wh100',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          isAttributesSave: true,
          isHierarchicalHousehold: true, //是否支持分层分户
          dataSetName: 'BMFCFH',
        },
        {
          resourceId: 680,
          pid: '',
          resourceName: '京津地区地图',
          resourceLocation:
            'http://www.supermapol.com/realspace/services/map-mvt-JingJinDiQuDiTu/restjsr/v1/vectortile/maps/京津地区地图',
          resourceTag: 'MVT',
          resourceTagCN: '地图服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          zIndex: 6,
        },
        {
          resourceId: 688,
          pid: '',
          resourceName: 'China',
          resourceLocation:
            'http://support.supermap.com.cn:8090/iserver/services/map-china400/rest/maps/China',
          resourceTag: 'RESTMAP',
          resourceTagCN: '地图服务',
          sourceType: 'SUPERMAP',
          location: true,
          sourceTypeName: 'SuperMap',
          opacity: 1,
        },
        {
          resourceId: 689,
          pid: '',
          resourceName: 'ChinaDark',
          resourceLocation:
            'http://support.supermap.com.cn:8090/iserver/services/map-china400/rest/maps/ChinaDark',
          resourceTag: 'RESTMAP',
          resourceTagCN: '地图服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          zIndex: 5,
        },
        {
          resourceId: 1,
          resourceName: '2维图层',
          pid: '',
        },
        {
          resourceId: 2,
          resourceName: '3维数据',
          pid: '',
        },
        {
          resourceId: 23,
          resourceName: '地形',
          pid: 2,
        },
        {
          resourceId: 24,
          resourceName: '倾斜',
          pid: 2,
        },
        {
          resourceId: 25,
          resourceName: 'BIM',
          pid: 2,
        },
        {
          resourceId: 7166,
          pid: 1,
          resourceName: 'China400',
          resourceLocation:
            'http://www.supermapol.com/realspace/services/map-China400/rest/maps/China400',
          resourceTag: 'RESTMAP',
          resourceTagCN: '地图服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
        },
        {
          resourceId: 7180,
          pid: 1,
          resourceName: '湖北行政区',
          resourceLocation:
            host + '/iserver/services/3D-cim_gis/rest/realspace/datas/hub_xzqh',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
        },
        {
          resourceId: 7181,
          pid: 1,
          resourceName: '武汉行政区',
          resourceLocation:
            host + '/iserver/services/3D-cim_gis/rest/realspace/datas/wuh_xzqh',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
        },
        {
          resourceId: 7169,
          pid: 1,
          resourceName: '规划用地',
          resourceLocation:
            host + '/iserver/services/3D-cim_gis/rest/realspace/datas/ghyd_4490',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          isAttributesSave: true,
          queryField: ['city', 'class_name'],
          dataSetName: 'ghyd_4490',
          legendData: [
            {
              name: '中小学用地',
              rgb: '#FF80FF',
            },
            {
              name: '体育用地',
              rgb: '#004D00',
            },
            {
              name: '公园绿地',
              rgb: '#00CC00',
            },
            {
              name: '医疗卫生用地',
              rgb: '#FFBF80',
            },
            {
              name: '商业用地',
              rgb: '#FF0000',
            },
            {
              name: '居住用地',
              rgb: '#FFFF00',
            },
            {
              name: '文化设施用地',
              rgb: '#FF809F',
            },
            {
              name: '水域',
              rgb: '#00FFFF',
            },
            {
              name: '物流仓储用地',
              rgb: '#39004D',
            },
            {
              name: '行政办公用地',
              rgb: '#FF80BF',
            },
            {
              name: '耕地',
              rgb: '#90EE90',
            },
            {
              name: '防护绿地',
              rgb: '#00FF80',
            },
          ],
        },
        {
          resourceId: 7170,
          pid: 1,
          resourceName: '退界红线',
          resourceLocation:
            host + '/iserver/services/3D-cim_gis/rest/realspace/datas/dlhx',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          dataSetName: 'dlhx',
          isAttributesSave: true,
        },
        {
          resourceId: 7191,
          pid: 1,
          resourceName: '武汉底图',
          resourceLocation:
            host +
            '/iserver/services/map-mvt-DiTuMVT/rest/maps/%E5%BA%95%E5%9B%BEMVT',
          resourceTag: 'RESTMAP',
          resourceTagCN: '地图服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          zIndex: 9,
        },
        {
          resourceId: 7171,
          pid: 1,
          resourceName: '影像1-17',
          resourceLocation:
            host + '/iserver/services/3D-cim_gis/rest/realspace/datas/wuh-img',
          resourceTag: 'RESTMAP',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          zIndex: 10,
          opacity: 1,
          optionCon: {
            transparentBackColor: '#ffffff',
            transparentBackColorTolerance: 0.02,
          },
        },
        {
          resourceId: 7172,
          pid: 23,
          resourceName: '地形2W',
          resourceLocation:
            host + '/iserver/services/3D-cim_gis/rest/realspace/datas/Dem30',
          resourceTag: 'DEM',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
        },
        {
          resourceId: 7173,
          pid: 24,
          resourceName: '万达广场',
          resourceLocation:
            host + '/iserver/services/3D-cim_gis/rest/realspace/datas/WDGC',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
        },
        {
          resourceId: 7174,
          pid: 24,
          resourceName: '体育馆',
          resourceLocation:
            host + '/iserver/services/3D-cim_gis/rest/realspace/datas/wuh_qingx',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
        },
        {
          resourceId: 7175,
          pid: '',
          resourceName: '武汉白模',
          resourceLocation:
            host + '/iserver/services/3D-cim_gis/rest/realspace/datas/WuHanBaiMo',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          location: false,
          queryField: [
            'SmArea',
            'SmPerimeter',
            'SmGeometrySize',
            'SmGeoPosition',
            'Floor',
            'Height',
          ],
          opacity: 1,
          dataSetName: 'vec_building',
          isWhiteModel: true,
          isAttributesSave: true,
          fillColor: [255, 255, 255, 1],
          borderColor: [0, 0, 0, 0.6],
        },
        {
          resourceId: 7185,
          pid: '',
          resourceName: '武汉白模2',
          resourceLocation:
            host +
            '/iserver/services/3D-cim_gis/rest/realspace/datas/WuHanBaiMo2',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          isAttributesSave: true,
          disabled: true,
        },
        {
          resourceId: 7176,
          pid: 25,
          resourceName: '不动产档案馆（J_V01）',
          resourceLocation:
            host +
            '/iserver/services/3D-cim_gis/rest/realspace/datas/J_V01@BIM_DAG',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          dataSetName: 'J_V01',
          isAttributesSave: true,
        },
        {
          resourceId: 7177,
          pid: 25,
          resourceName: '不动产档案馆（G_V01）',
          resourceLocation:
            host +
            '/iserver/services/3D-cim_gis/rest/realspace/datas/G_V01@BIM_DAG',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          dataSetName: 'G_V01',
          isAttributesSave: true,
          cameraCoordinate: {
            altitude: 114.3764743079913,
            heading: 5.654236538948201,
            latitude: 30.466650989920087,
            tilt: 70.60882457049563,
            longitude: 114.25722359896619,
          },
        },
        {
          resourceId: 7178,
          pid: 25,
          resourceName: '不动产档案馆（外立面）',
          resourceLocation:
            host +
            '/iserver/services/3D-cim_gis/rest/realspace/datas/J_WaiLiMian@BIM_DAG',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          queryField: ['SmUserID'],
          dataSetName: 'J_WaiLiMian',
          isAttributesSave: true,
        },
        {
          resourceId: 7189,
          pid: 25,
          resourceName: '分层分户楼层',
          resourceLocation:
            host + '/iserver/services/3D-cim_gis/rest/realspace/datas/wuh_fcfh',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          isAttributesSave: true,
          isHierarchicalHousehold: true, // 是否支持分层分户
          dataSetName: 'FCFH',
          cameraCoordinate: {
            altitude: 62.46821991822186,
            heading: 6.2730744988966975,
            latitude: 30.484909153821587,
            tilt: 72.7806564730819,
            longitude: 114.30882900103435,
          },
        },
        {
          resourceId: 7199,
          pid: '',
          resourceName: '底图路网',
          resourceLocation:
            host + '/iserver/services/3D-3Dmvt-DiTu/rest/realspace/datas/DiTu',
          resourceTag: 'MVT',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          location: false,
          selectEnabled: false,
          zIndex: 8,
        },
        {
          resourceId: 2001,
          pid: '',
          resourceName: '路网',
          resourceLocation:
            host + '/iserver/services/3D-wuh_bjdt1/rest/realspace',
          resourceTag: 'RESTREALSPACE',
          resourceTagCN: '地图服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          location: false,
        },
        {
          resourceId: 2002,
          pid: '',
          resourceName: 'arcgis',
          resourceLocation:
            'http://cache1.arcgisonline.cn/arcgis/rest/services/ChinaOnlineStreetPurplishBlue/MapServer',
          resourceTag: 'ARCGIS',
          resourceTagCN: '地图服务',
          sourceType: 'arcgis',
          sourceTypeName: 'arcgis',
          opacity: 1,
          location: false,
          // suggest: true //4490坐标系，按照suggest切片方案切的瓦片,需要设置该参数
        },
        {
          resourceId: 2003,
          pid: '',
          resourceName: 'wms',
          resourceLocation:
            'https://iserver.supermap.io/iserver/services/map-china400/wms111/China',
          resourceTag: 'WMS',
          resourceTagCN: '地图服务',
          sourceType: 'WMS',
          sourceTypeName: 'WMS',
          opacity: 1,
          location: false,
          optionCon: {
            layers: '0',
          },
        },
        {
          resourceId: 2004,
          pid: '',
          resourceName: 'wmts',
          resourceLocation:
            'https://iserver.supermap.io/iserver/services/map-china400/wmts100',
          resourceTag: 'WMTS',
          resourceTagCN: '地图服务',
          sourceType: 'WMTS',
          sourceTypeName: 'WMTS',
          opacity: 1,
          location: false,
          optionCon: {
            layer: 'China',
            tileMatrixSetID: 'GoogleMapsCompatible_China',
            format: 'image/png',
          },
        },
        // {
        //   resourceId: 2005,
        //   pid: '',
        //   resourceName: '白模分层分户',
        //   resourceLocation:
        //     'http://www.supermapol.com/realspace/services/3D-individualHouse/rest/realspace/datas/%E4%B8%93%E9%A2%98%E6%88%B7%E5%9E%8B%E9%9D%A2_%E6%8B%89%E4%BC%B8',
        //   resourceTag: 'RESTREALSPACE-DATA',
        //   resourceTagCN: '三维服务',
        //   sourceType: 'SUPERMAP',
        //   sourceTypeName: 'SuperMap',
        //   opacity: 1,
        //   isAttributesSave: true,
        //   isHierarchicalHousehold: true, // 是否支持分层分户
        //   dataSetName: 'BMFCFH',
        // },
      ],
      checkedLayerKeys: [7175, 2001],
      defaultExpandedKeys: [23],
    }
  })(this)
