import sm3dComponentData from "./sm3dComponentData";
export default {
  MAPWORLD: {
    name: "MAPWORLD",
    html: `<gaf-map-viewer 
      :serviceList="serviceList"></gaf-map-viewer>`,
    js: `export default {
    name: 'MAPWORLD',
    data() {
      return {
        serviceList: [
          {
            pid: '',
            resourceId: 9993,
            resourceName: '天地图',
            resourceTag: 'MAPWORLD',
            sourceType: 'MAPWORLD',
            serviceType: 'VEC_C',
            location: true,
            token: 'de8a949b283cb793ef2d5c98f2f50fe5',
            opacity: 1,
        }
        ],
      }
    },
    methods: {}
  }`,
    css: ``
  },
  REST: {
    name: "REST",
    html: `<gaf-map-viewer 
      :serviceList="serviceList"></gaf-map-viewer>`,
    js: `export default {
    name: 'REST',
    data() {
      return {
        serviceList:[
          {
            resourceId: 9996,
            resourceName: '地图',
            resourceTag: 'RESTMAP',
            sourceType: 'SUPERMAP',
            resourceLocation:
            'http://www.supermapol.com/realspace/services/map-sample/rest/maps/WorldMap',
          },
       ]
      }
    },
    methods: {}
  }`,
    css: ``
  },
  WMS: {
    name: "WMS",
    html: `<gaf-map-viewer 
      :serviceList="serviceList"></gaf-map-viewer>`,
    js: `export default {
    name: 'WMS',
    data() {
      return {
        serviceList:[
          {
            resourceId: 2003,
            pid: '',
            resourceName: 'wms',
            resourceLocation: 'https://iserver.supermap.io/iserver/services/map-china400/wms111/China',
            resourceTag: 'WMS',
            resourceTagCN: '地图服务',
            sourceType: 'WMS',
            sourceTypeName: 'WMS',
            opacity: 1,
            location: false,
            optionCon: {
              layers: '0'
            }
          },
       ]
      }
    },
    methods: {}
  }`,
    css: ``
  },
  WMTS: {
    name: "WMTS",
    html: `<gaf-map-viewer 
      :serviceList="serviceList"></gaf-map-viewer>`,
    js: `export default {
    name: 'WMTS',
    data() {
      return {
        serviceList:[
          {
            resourceId: 2004,
            pid: '',
            resourceName: 'wmts',
            resourceLocation: 'https://iserver.supermap.io/iserver/services/map-china400/wmts100',
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
            }
          }
        ]
      }
    },
    methods: {}
  }`,
    css: ``
  },
  MVT: {
    name: "MVT",
    html: `<gaf-map-viewer 
      :serviceList="serviceList"></gaf-map-viewer>`,
    js: `export default {
    name: 'MVT',
    data() {
      return {
        serviceList:[
          {
          resourceId: 680,
          pid: '',
          resourceName: '京津地区地图',
          resourceLocation: 'http://www.supermapol.com/realspace/services/map-mvt-JingJinDiQuDiTu/restjsr/v1/vectortile/maps/京津地区地图',
          resourceTag: 'MVT',
          resourceTagCN: '地图服务',
          sourceType: 'SUPERMAP',
          location: true,
          sourceTypeName: 'SuperMap',
          opacity: 1,
          zIndex: 6
        }
       ]
      }
    },
    methods: {}
  }`,
    css: ``
  },
  DATA_3D: {
    name: "DATA_3D",
    html: `<gaf-map-viewer 
      :serviceList="serviceList"></gaf-map-viewer>`,
    js: `export default {
    name: 'DATA_3D',
    data() {
      return {
        serviceList:[
          {
            resourceId: 7175,
            pid: '',
            resourceName: 'Building',
            resourceLocation: 'http://www.supermapol.com/realspace/services/3D-OlympicGreen20200416/rest/realspace/datas/Building@OlympicGreen',
            resourceTag: 'RESTREALSPACE-DATA',
            resourceTagCN: '三维服务',
            sourceType: 'SUPERMAP',
            sourceTypeName: 'SuperMap',
            location: true,
            opacity: 1,
            dataSetName: 'Building@OlympicGreen',
            isWhiteModel: true,
            isAttributesSave: true,
          },
       ]
      }
    },
    methods: {}
  }`,
    css: ``
  },
  SCENE_3D: {
    name: "SCENE_3D",
    html: `<gaf-map-viewer 
      :serviceList="serviceList"></gaf-map-viewer>`,
    js: `export default {
    name: 'SCENE_3D',
    data() {
      return {
        serviceList:[
          {
            resourceId: 7176,
            pid: 25,
            resourceName: 'Building',
            resourceLocation: 
              'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/datas/Building@CBD',
            resourceTag: 'RESTREALSPACE-DATA',
            resourceTagCN: '三维服务',
            sourceType: 'SUPERMAP',
            sourceTypeName: 'SuperMap',
            opacity: 1,
            location: true,
            dataSetName: 'Building@CBD',
            isAttributesSave: true
          },
          {
            resourceId: 7177,
            pid: 25,
            resourceName: 'Ground_2',
            resourceLocation: 
              'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/datas/Ground_2@CBD',
            resourceTag: 'RESTREALSPACE-DATA',
            resourceTagCN: '三维服务',
            sourceType: 'SUPERMAP',
            sourceTypeName: 'SuperMap',
            opacity: 1,
            dataSetName: 'Ground_2@CBD',
            isAttributesSave: true,
          },
          {
            resourceId: 7178,
            pid: 25,
            resourceName: 'Tree',
            resourceLocation: 
              'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/datas/Tree@CBD',
            resourceTag: 'RESTREALSPACE-DATA',
            resourceTagCN: '三维服务',
            sourceType: 'SUPERMAP',
            sourceTypeName: 'SuperMap',
            opacity: 1,
            dataSetName: 'Tree@CBD',
            isAttributesSave: true
          },
          {
            resourceId: 7179,
            pid: 25,
            resourceName: 'Ground_1',
            resourceLocation: 
              'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/datas/Ground_1@CBD',
            resourceTag: 'RESTREALSPACE-DATA',
            resourceTagCN: '三维服务',
            sourceType: 'SUPERMAP',
            sourceTypeName: 'SuperMap',
            opacity: 1,
            dataSetName: 'Ground_1@CBD',
            isAttributesSave: true
          },
          {
            resourceId: 7180,
            pid: 25,
            resourceName: 'Lake',
            resourceLocation: 
              'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/datas/Lake@CBD',
            resourceTag: 'RESTREALSPACE-DATA',
            resourceTagCN: '三维服务',
            sourceType: 'SUPERMAP',
            sourceTypeName: 'SuperMap',
            opacity: 1,
            dataSetName: 'Lake@CBD ',
            isAttributesSave: true
          },
       ]
      }
    },
    methods: {}
  }`,
    css: ``
  },
  ARCCGIS: {
    name: "ARCCGIS",
    html: `<gaf-map-viewer 
      :serviceList="serviceList"></gaf-map-viewer>`,
    js: `export default {
    name: 'ARCCGIS',
    data() {
      return {
        serviceList:[
          {
            resourceId: 2002,
            pid: '',
            resourceName: 'arcgis',
            resourceLocation: 'http://cache1.arcgisonline.cn/arcgis/rest/services/ChinaOnlineStreetPurplishBlue/MapServer',
            resourceTag: 'ARCGIS',
            resourceTagCN: '地图服务',
            sourceType: 'arcgis',
            sourceTypeName: 'arcgis',
            opacity: 1,
            location: false,
            // suggest: true //4490坐标系，按照suggest切片方案切的瓦片,需要设置该参数
          },
       ]
      }
    },
    methods: {}
  }`,
    css: ``
  },
  BottomChange: {
    name: "BottomChange",
    html: `<gaf-map-viewer>
          <gaf-map-bottom class="bottomChangeDiv"
        :bottomList="bottomList"
        :token="token"
        v-if="isBottomShow"
      /></gaf-map-viewer>
    `,
    js: `export default {
      name: 'BottomChange',
      data() {
        return {
          token: 'de8a949b283cb793ef2d5c98f2f50fe5',
          bottomList: [
          // 天地图底图
            {
              resourceId: 9999,
              resourceName: '地图',
              sourceType: 'MAPWORLD',
              serviceType: 'VEC_C'
            },
            {
              resourceId: 9998,
              resourceName: '地形',
              sourceType: 'MAPWORLD',
              serviceType: 'TER_C'
            },
            {
              resourceId: 9997,
              resourceName: '影像',
              sourceType: 'MAPWORLD',
              serviceType: 'IMG_C'
            },
            // 超图底图
            // {
            //   resourceId: 9996,
            //   resourceName: '地图',
            //   resourceTag: 'RESTMAP',
            //   sourceType: 'SUPERMAP',
            //   resourceLocation:
            //     'http://www.supermapol.com/realspace/services/map-sample/rest/maps/WorldMap',
            // },
            // {
            //   resourceId: 9995,
            //   resourceName: '地形',
            //   resourceTag: 'RESTMAP',
            //   sourceType: 'SUPERMAP',
            //   resourceLocation:
            //     'http://www.supermapol.com/realspace/services/map-China400/rest/maps/China400'
            // },
            // {
            //   resourceId: 9994,
            //   resourceTag: 'RESTMAP',
            //   resourceName: '影像',
            //   sourceType: 'SUPERMAP',
            //   resourceLocation:
            //     'http://www.supermapol.com/realspace/services/map-World/rest/maps/World_Image'
            // },
          ]
        }
      },
      computed: {
        isBottomShow() {
          return this.bottomList.length > 0
        }
      },
    }`,
    css: `.bottomChangeDiv{
      z-index: 999;
    }`
  },
  Earth: {
    name: "Earth",
    html: `<gaf-map-viewer />`,
    js: `export default {
      name: 'Earth',
      data() {
        return {}
      },
      methods: {}
    }`,
    css: ``
  },
  SceneList3D: {
    html: `<gaf-map-viewer :serviceList="serviceList" />`,
    js: `export default {
      data() {
        return {
          serviceList: [
            {
              resourceId: 2001,
              resourceName: '3D-CBD',
              location:true,
              resourceLocation:
                'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace',
              resourceTag: 'RESTREALSPACE'
            },
          ],
        }
      },
   
    }`,
    css: ""
  },
  LayerList2D: {
    html: `<gaf-map-viewer :serviceList="serviceList" />`,
    js: `export default {
      data() {
        return {
          serviceList: [
            // SuperMap 发布的二维地图服务
            {
              resourceId: 7000,
              location: true,
              resourceTag: 'RESTMAP',
              resourceLocation:
                'http://www.supermapol.com/realspace/services/map-World/rest/maps/World_Google',
              resourceName: 'China',
              optionCon: [{ minimumTerrainLevel: 0, maximumTerrainLevel: 1 }],
            },
            {
              resourceId: 7001,
              location: true,
              resourceTag: 'RESTMAP',
              resourceLocation:
                'http://www.supermapol.com/realspace/services/map-World/rest/maps/World_AirLine',
              resourceName: 'China_4326',
              optionCon: [{ minimumTerrainLevel: 1, maximumTerrainLevel: 2 }],
            },
          ],
        }
      },
}`,
    css: ""
  },
  Initialize: {
    html: `<gaf-map-viewer :serviceList="serviceList">
    </gaf-map-viewer>`,
    js: `export default {
      beforeCreate() {
        Vue.prototype.$bus = new Vue()
        this.$bus.$on('initialize', () => {
          console.log('Viewer加载完成')
        })
      },
      data() {
        return {
          serviceList: [
            // SuperMap 发布的二维地图服务
            {
              resourceId: 7000,
              location: true,
              resourceTag: 'RESTMAP',
              resourceLocation:
                'http://www.supermapol.com/realspace/services/map-World/rest/maps/World_Google',
              resourceName: 'China',
              optionCon: [{ minimumTerrainLevel: 0, maximumTerrainLevel: 1 }],
            },
            {
              resourceId: 7001,
              location: true,
              resourceTag: 'RESTMAP',
              resourceLocation:
                'http://www.supermapol.com/realspace/services/map-World/rest/maps/World_AirLine',
              resourceName: 'China_4326',
              optionCon: [{ minimumTerrainLevel: 1, maximumTerrainLevel: 2 }],
            },
          ],
        }
      },
}`,
    css: ``
  },
  Clear: {
    html: `<gaf-map-viewer :serviceList="serviceList">
    <gaf-map-tool-bar :content="right" type="vertical" ></gaf-map-tool-bar>
    <gaf-map-tool-bar :content="home" type="horizontal" ></gaf-map-tool-bar>
  </gaf-map-viewer>`,
    js: `export default {
      beforeCreate() {
        Vue.prototype.$bus = new Vue()
        this.$bus.$on('common-tools-item-click', (name) => {
          console.log('common-tools-item-click', name)
        })
      },
      data() {
        return {
          positionH: 'bottom',
          positionV: 'bottomRight',
          right: [
            {
              title: '初始化视角',
              name: 'flyTo',
              icon: 'icon-chushihuashijiao',
              params: [
                {
                  altitude: 237.02558519727847,
                  heading: 4.577567816832631,
                  latitude: 30.624635512479735,
                  longitude: 114.2644153328619,
                  tilt: 76.79984572685618,
                },
              ],
            },
            {
              title: '放大',
              name: 'zoomIn',
              icon: 'icon-jiahao',
            },
            {
              title: '缩小',
              name: 'zoomOut',
              icon: 'icon-jianhaocu',
            },
            {
              title: '二维视图|三维视图',
              name: 'switchMode',
              icon: 'icon-diqiu',
            },
            {
              title: '全屏|取消全屏',
              name: 'fullScreen',
              icon: 'icon-quanping1',
            },
            {
              title: '清除',
              name: 'mapClear',
              icon: 'icon-clear',
            },
            {
              title: '鹰眼',
              name: 'GafMapBirdEye',
              icon: 'icon-quyudingwei',
              toggle: true,
            },
          ],
          home: [
            {
              name: 'measure',
              icon: 'icon-xiankuang',
              title: '量算',
              children: [
                {
                  title: '距离量算',
                  name: 'measureDistance',
                  icon: 'icon-ceju',
                  closePanel: true,
                  selectedName: 'measure',
                  actions: ['splitScreenNone'],
                },
                {
                  title: '面积量算',
                  name: 'measureArea',
                  icon: 'icon-xiankuang',
                  closePanel: true,
                  selectedName: 'measure',
                  actions: ['splitScreenNone'],
                },
                {
                  title: '高度量算',
                  name: 'measureHeight',
                  icon: 'icon-cegao',
                  closePanel: true,
                  selectedName: 'measure',
                  actions: ['splitScreenNone'],
                },
                {
                  title: '清除量算',
                  name: 'mapClear',
                  icon: 'icon-clear',
                  closePanel: false,
                  closedName: 'measure',
                  actions: ['splitScreenNone', 'mapClear'],
                },
              ],
            },
            {
              title: '剖切',
              name: 'gaf-map-profile',
              icon: 'icon-pouqie',
              type: 'panel',
              actions: ['mapClear'],
              // actions: ['clear']
            },
            {
              title: '综合查询',
              name: 'GafMapQuery',
              icon: 'icon-dianxuan1',
              params: {
                serverDatas: [
                  {
                    url:
                      'http://support.supermap.com.cn:8090/iserver/services/data-jingjin/rest/data/datasources/Jingjin/datasets/BaseMap_P',
                    title: 'BaseMap_P',
                    fields: ['SmID', 'NAME', 'CODE'],
                    alias: ['id', '名称', '编号'],
                  },
                  {
                    url:
                      'http://support.supermap.com.cn:8090/iserver/services/data-jingjin/rest/data/datasources/Jingjin/datasets/BaseMap_L',
                    title: 'BaseMap_L',
                    fields: ['SmID', 'NAME', 'CODE'],
                    alias: ['id', '名称', '编号'],
                  },
                  {
                    url:
                      'http://support.supermap.com.cn:8090/iserver/services/data-jingjin/rest/data/datasources/Jingjin/datasets/BaseMap_R',
                    title: 'BaseMap_R',
                    fields: ['SmID', 'NAME', 'CITY'],
                    alias: ['id', '名称', '城市'],
                  },
                ],
              },
            },
            {
              title: '点选',
              name: 'pointSelect',
              icon: 'icon-dianxuan1',
              actions: ['releaseSelection'],
              params: ['red'],
              toggle: true,
            },
            {
              title: '模型编辑',
              name: 'modelEditor',
              icon: 'icon-dianxuan1',
              actions: ['clearModel'],
              params: [
                './gltf/客机模型/客机模型.gltf', //模型路径
                [114.279080101051, 30.6001413808849, 20], //模型添加位置
              ],
              toggle: true,
            },
            {
              title: '泛光扫描线',
              name: 'gaf-map-flood-light',
              icon: 'icon-dingwei',
              actions: ['mapClear'],
            },

            {
              title: '区域查询定位',
              name: 'gaf-map-locate',
              icon: 'icon-quyudingwei',
              type: 'panel',
              // actions: ['clear'],
              actions: ['mapClear'],
              params: {
                info: {
                  city: '武汉市',
                  code: 420100,
                  // 'dataUrl': 'http://10.10.137.19:8090/iserver/services/data-xzqh/rest',
                  // 'dataSourceName': 'xzqh',
                  // 'dataSetName': 'BJDT_2019_XZQH_ALL_L12',
                  dataUrl:
                    'http://1.202.165.56:8090/iserver/services/data-cim_gis/rest',
                  // 'dataUrl': 'http://1.202.165.56:8090/iserver/services/data-cim_gis/rest',
                  // 'dataUrl': 'http://192.168.105.60:8090/iserver/services/data-cim_gis/rest',
                  dataSourceName: 'cim-base',
                  dataSetName: 'XZQH_ALL_L12',
                  fillColor: 'blue',
                  borderColor: 'red',
                  filterField: 'QHBM',
                },
                districts: [
                  {
                    name: '江岸区',
                    code: 420102,
                  },
                  {
                    name: '江汉区',
                    code: 420103,
                  },
                  {
                    name: '硚口区',
                    code: 420104,
                  },
                  {
                    name: '汉阳区',
                    code: 420105,
                  },
                  {
                    name: '武昌区',
                    code: 420106,
                  },
                  {
                    name: '青山区',
                    code: 420107,
                  },
                  {
                    name: '洪山区',
                    code: 420111,
                  },
                  {
                    name: '东西湖区',
                    code: 420112,
                  },
                  {
                    name: '汉南区',
                    code: 420113,
                  },
                  {
                    name: '蔡甸区',
                    code: 420114,
                  },
                  {
                    name: '江夏区',
                    code: 420115,
                  },
                  {
                    name: '黄陂区',
                    code: 420116,
                  },
                  {
                    name: '新洲区',
                    code: 420117,
                  },
                ],
              },
            },
            {
              title: '视点管理',
              name: 'gaf-map-viewpoint-manage',
              icon: 'icon-shidian',
              actions: ['mapClear'],
            },

            {
              title: '环境模拟',
              name: 'GafMapWeather',
              icon: 'icon-tianqi1',
              children: [
                {
                  title: '光影变化',
                  name: 'gaf-map-light-shadow',
                  icon: 'icon-tianqi1',
                  actions: ['mapClear'],
                  selectedName: 'environment',
                },
                {
                  title: '天气变化',
                  name: 'GafMapWeather',
                  icon: 'icon-tianqi1',
                  actions: ['mapClear'],
                  selectedName: 'environment',
                },
              ],
            },
            {
              title: '压平',
              name: 'GafMapFlatten',
              icon: 'icon-xuanze',
              // actions: ['clear'],
              actions: ['mapClear'],
            },

            {
              title: '三维漫游',
              name: 'RoamingGaf',
              icon: 'icon-manyou1',
              // actions: ['clear'],
              actions: ['mapClear'],
              params: { mySpeed: 60, useMySpeed: true },
            },
          ],
          serviceList: [
            {
              resourceId: 2001,
              resourceName: '3D-CBD',
              location: true,
              resourceLocation:
                'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace',
              resourceTag: 'RESTREALSPACE',
            },
          ],
        }
      },
   
    }`,
    css: `#draggable {
      background-color: var(--bg-color, rgba(25, 40, 58, 0.6));
      border-width: 0;
      color: var(--font-color, #fff);
      margin-left: 30px;
      padding: 0 15px 0;
      font-size: 14px;
    }
    .ant-tree li .ant-tree-node-content-wrapper:hover,
    .ant-tree li .ant-tree-node-content-wrapper.ant-tree-node-selected {
      background-color: transparent;
    }`
  },
  layerLoaded: {
    html: `<gaf-map-viewer :serviceList="serviceList">
    </gaf-map-viewer>`,
    js: `export default {
      beforeCreate() {
        Vue.prototype.$bus = new Vue()
        this.$bus.$on('layer-loaded', (layer) => {
          console.log('layer-loaded',layer)
        })
      },
      data() {
        return {
          serviceList: [
            // SuperMap 发布的二维地图服务
            {
              resourceId: 7000,
              location: true,
              resourceTag: 'RESTMAP',
              resourceLocation:
                'http://www.supermapol.com/realspace/services/map-World/rest/maps/World_Google',
              resourceName: 'China',
              optionCon: [{ minimumTerrainLevel: 0, maximumTerrainLevel: 1 }],
            },
            {
              resourceId: 7001,
              location: true,
              resourceTag: 'RESTMAP',
              resourceLocation:
                'http://www.supermapol.com/realspace/services/map-World/rest/maps/World_AirLine',
              resourceName: 'China_4326',
              optionCon: [{ minimumTerrainLevel: 1, maximumTerrainLevel: 2 }],
            },
          ],
        }
      },
}`,
    css: ``
  },
  panelToggle: {
    html: `<gaf-map-viewer :serviceList="serviceList">
    <gaf-map-tool-bar :content="content" type="horizontal" ></gaf-map-tool-bar>
  </gaf-map-viewer>`,
    js: `export default {
      beforeCreate() {
        Vue.prototype.$bus = new Vue()
        this.$bus.$on('panelToggle', (visible) => {
          console.log('panelToggle', visible)
        })
      },
      data() {
        return {
          positionH: 'bottom',
          positionV: 'bottomRight',
          content: [
            {
              name: 'measure',
              icon: 'icon-xiankuang',
              title: '量算',
              children: [
                {
                  title: '距离量算',
                  name: 'measureDistance',
                  icon: 'icon-ceju',
                  closePanel: true,
                  selectedName: 'measure',
                  actions: ['splitScreenNone'],
                },
                {
                  title: '面积量算',
                  name: 'measureArea',
                  icon: 'icon-xiankuang',
                  closePanel: true,
                  selectedName: 'measure',
                  actions: ['splitScreenNone'],
                },
                {
                  title: '高度量算',
                  name: 'measureHeight',
                  icon: 'icon-cegao',
                  closePanel: true,
                  selectedName: 'measure',
                  actions: ['splitScreenNone'],
                },
                {
                  title: '清除量算',
                  name: 'mapClear',
                  icon: 'icon-clear',
                  closePanel: false,
                  closedName: 'measure',
                  actions: ['splitScreenNone', 'mapClear'],
                },
              ],
            },
            {
              title: '剖切',
              name: 'gaf-map-profile',
              icon: 'icon-pouqie',
              type: 'panel',
              actions: ['mapClear'],
              // actions: ['clear']
            },
            {
              title: '综合查询',
              name: 'GafMapQuery',
              icon: 'icon-dianxuan1',
              params: {
                serverDatas: [
                  {
                    url:
                      'http://support.supermap.com.cn:8090/iserver/services/data-jingjin/rest/data/datasources/Jingjin/datasets/BaseMap_P',
                    title: 'BaseMap_P',
                    fields: ['SmID', 'NAME', 'CODE'],
                    alias: ['id', '名称', '编号'],
                  },
                  {
                    url:
                      'http://support.supermap.com.cn:8090/iserver/services/data-jingjin/rest/data/datasources/Jingjin/datasets/BaseMap_L',
                    title: 'BaseMap_L',
                    fields: ['SmID', 'NAME', 'CODE'],
                    alias: ['id', '名称', '编号'],
                  },
                  {
                    url:
                      'http://support.supermap.com.cn:8090/iserver/services/data-jingjin/rest/data/datasources/Jingjin/datasets/BaseMap_R',
                    title: 'BaseMap_R',
                    fields: ['SmID', 'NAME', 'CITY'],
                    alias: ['id', '名称', '城市'],
                  },
                ],
              },
            },
            {
              title: '点选',
              name: 'pointSelect',
              icon: 'icon-dianxuan1',
              actions: ['releaseSelection'],
              params: ['red'],
              toggle: true,
            },
            {
              title: '模型编辑',
              name: 'modelEditor',
              icon: 'icon-dianxuan1',
              actions: ['clearModel'],
              params: [
                './gltf/客机模型/客机模型.gltf', //模型路径
                [114.279080101051, 30.6001413808849, 20], //模型添加位置
              ],
              toggle: true,
            },
            {
              title: '泛光扫描线',
              name: 'gaf-map-flood-light',
              icon: 'icon-dingwei',
              actions: ['mapClear'],
            },

            {
              title: '区域查询定位',
              name: 'gaf-map-locate',
              icon: 'icon-quyudingwei',
              type: 'panel',
              // actions: ['clear'],
              actions: ['mapClear'],
              params: {
                info: {
                  city: '武汉市',
                  code: 420100,
                  // 'dataUrl': 'http://10.10.137.19:8090/iserver/services/data-xzqh/rest',
                  // 'dataSourceName': 'xzqh',
                  // 'dataSetName': 'BJDT_2019_XZQH_ALL_L12',
                  dataUrl:
                    'http://1.202.165.56:8090/iserver/services/data-cim_gis/rest',
                  // 'dataUrl': 'http://1.202.165.56:8090/iserver/services/data-cim_gis/rest',
                  // 'dataUrl': 'http://192.168.105.60:8090/iserver/services/data-cim_gis/rest',
                  dataSourceName: 'cim-base',
                  dataSetName: 'XZQH_ALL_L12',
                  fillColor: 'blue',
                  borderColor: 'red',
                  filterField: 'QHBM',
                },
                districts: [
                  {
                    name: '江岸区',
                    code: 420102,
                  },
                  {
                    name: '江汉区',
                    code: 420103,
                  },
                  {
                    name: '硚口区',
                    code: 420104,
                  },
                  {
                    name: '汉阳区',
                    code: 420105,
                  },
                  {
                    name: '武昌区',
                    code: 420106,
                  },
                  {
                    name: '青山区',
                    code: 420107,
                  },
                  {
                    name: '洪山区',
                    code: 420111,
                  },
                  {
                    name: '东西湖区',
                    code: 420112,
                  },
                  {
                    name: '汉南区',
                    code: 420113,
                  },
                  {
                    name: '蔡甸区',
                    code: 420114,
                  },
                  {
                    name: '江夏区',
                    code: 420115,
                  },
                  {
                    name: '黄陂区',
                    code: 420116,
                  },
                  {
                    name: '新洲区',
                    code: 420117,
                  },
                ],
              },
            },
            {
              title: '视点管理',
              name: 'gaf-map-viewpoint-manage',
              icon: 'icon-shidian',
              actions: ['mapClear'],
            },

            {
              title: '环境模拟',
              name: 'GafMapWeather',
              icon: 'icon-tianqi1',
              children: [
                {
                  title: '光影变化',
                  name: 'gaf-map-light-shadow',
                  icon: 'icon-tianqi1',
                  actions: ['mapClear'],
                  selectedName: 'environment',
                },
                {
                  title: '天气变化',
                  name: 'GafMapWeather',
                  icon: 'icon-tianqi1',
                  actions: ['mapClear'],
                  selectedName: 'environment',
                },
              ],
            },
            {
              title: '压平',
              name: 'GafMapFlatten',
              icon: 'icon-xuanze',
              // actions: ['clear'],
              actions: ['mapClear'],
            },

            {
              title: '三维漫游',
              name: 'RoamingGaf',
              icon: 'icon-manyou1',
              // actions: ['clear'],
              actions: ['mapClear'],
              params: { mySpeed: 60, useMySpeed: true },
            },
          ],
          serviceList: [
            {
              resourceId: 2001,
              resourceName: '3D-CBD',
              location: true,
              resourceLocation:
                'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace',
              resourceTag: 'RESTREALSPACE',
            },
          ],
        }
      },
    }`,
    css: `#draggable {
      background-color: var(--bg-color, rgba(25, 40, 58, 0.6));
      border-width: 0;
      color: var(--font-color, #fff);
      margin-left: 30px;
      padding: 0 15px 0;
      font-size: 14px;
    }
    .ant-tree li .ant-tree-node-content-wrapper:hover,
    .ant-tree li .ant-tree-node-content-wrapper.ant-tree-node-selected {
      background-color: transparent;
    }`
  },
  BasicAnalysis: {
    html: ` <gaf-map-viewer :serviceList="serviceList">
      <gaf-map-query :serverDatas="serverDatas" class="search"/>
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          serviceList: [
            {
              resourceId: 7000,
              resourceTag: 'RESTMAP',
              resourceLocation:
                'https://iserver.supermap.io/iserver/services/map-world/rest/maps/World',
              resourceName: 'World'
            },
            {
              resourceId: 7001,
              resourceTag: 'RESTMAP',
              resourceLocation:
                'http://support.supermap.com.cn:8090/iserver/services/map-jingjin/rest/maps/%E4%BA%AC%E6%B4%A5%E5%9C%B0%E5%8C%BA%E5%9C%B0%E5%9B%BE',
              resourceName: '京津地区地图',
              location: true
            }
          ],
          serverDatas: [
            {
              url:
                'http://support.supermap.com.cn:8090/iserver/services/data-jingjin/rest/data/datasources/Jingjin/datasets/BaseMap_P',
              title: 'BaseMap_P',
              fields: ['SmID', 'NAME', 'CODE'],
              alias: ['id', '名称', '编号']
            },
            {
              url:
                'http://support.supermap.com.cn:8090/iserver/services/data-jingjin/rest/data/datasources/Jingjin/datasets/BaseMap_L',
              title: 'BaseMap_L',
              fields: ['SmID', 'NAME', 'CODE'],
              alias: ['id', '名称', '编号']
            },
            {
              url:
                'http://support.supermap.com.cn:8090/iserver/services/data-jingjin/rest/data/datasources/Jingjin/datasets/BaseMap_R',
              title: 'BaseMap_R',
              fields: ['SmID', 'NAME', 'CITY'],
              alias: ['id', '名称', '城市']
            }
          ]
        }
      }
    }`,
    css: `.search {
      color: rgba(0, 0, 0, 0.65);
    }
    .ant-btn.ant-btn-loading:not(.ant-btn-circle):not(.ant-btn-circle-outline):not(.ant-btn-icon-only) {
      position: absolute;
      padding-left: auto;
      pointer-events: none;
    }`
  },
  QueryAnalysis: {
    html: `<gaf-map-viewer :smLayerList="smLayerList">
    <div class="search">
      <gaf-map-query:sourceUrl="sourceUrl" />
    </div>
  </gaf-map-viewer>`,
    js: `export default {
  data() {
    return {
      sourceUrl:
        'http://www.supermapol.com/realspace/services/data-jinjiang/rest/data/datasources/jinjiang',
      arcProjection: '2000', // ArcGIS 发布的二维地图服务对应的投影2000|wgs84，不同的投影不同的加载策略
      arcLayerList: [], // ArcGIS 发布的二维地图服务
      smLayerList: [
        // SuperMap 发布的二维地图服务
        {
          resourceId: 7000,
          location: true,
          resourceLocation:
            'http://www.supermapol.com/realspace/services/map-World/rest/maps/World_Google',
          resourceName: 'China',
        },
      ]
    }
  }
}`,
    css: `.search {
      width: 438px;
      min-height: 172px;
      position: absolute;
      top: 30px;
      left: 30px;
      z-index: 1;
      background: rgba(255, 255, 255, 0.9);
      border-radius: 5px;
      color: rgba(0,0,0,.65);
      padding: 10px;
    }
    .ant-btn.ant-btn-loading:not(.ant-btn-circle):not(.ant-btn-circle-outline):not(.ant-btn-icon-only){
      position: absolute;
      padding-left: auto;
      pointer-events: none;
    }
    `
  },
  MapTools: {
    html: `<gaf-map-viewer :smSceneList="smSceneList">
    <gaf-map-tools :showIconOnly="false" :content="content" />
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          content: [
            {
              name: 'zoomOut'
            },
            {
              name: 'zoomIn'
            },
            {
              name: 'splitScreen'
            },
            {
              name: 'measureTool'
            },
            {
              name: 'switchMode'
            },

            {
              name: 'fullMap',
              icon: ''
            },
            {
              name: 'sm3d-measure',
              icon: '',
              type: 'panel'
            }
          ],
          smSceneList: [
            {
              resourceId: '8000',
              location: true,
              resourceLocation:
                'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD'
            }
          ]
        }
      }
    }
    `,
    css: `
    .toolButtonGroup {
      z-index: 1;
  }
    `
  },
  MapToolsPosition: {
    html: `
    <gaf-map-viewer :smSceneList="smSceneList">
    <div class="wrap-btn">
    <button @click="DirectionHandler" class="btn">上下切换</button>
    <button @click="verticalHandler($event)" class="btn" id="topLeft">左上</button>
    <button @click="verticalHandler($event)" class="btn" id="topRight">右上</button>
    <button @click="verticalHandler($event)" class="btn" id="bottomRight">右下</button>
    <button @click="verticalHandler($event)" class="btn" id="bottomLeft">左下</button>
    </div>
    <gaf-map-tool-bar :content="content" :position="positionH" type="horizontal" ></gaf-map-tool-bar>
    <gaf-map-tool-bar :content="content" :position="positionV" type="vertical" ></gaf-map-tool-bar>
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          positionH: 'bottom',
          positionV: 'bottomRight',
          content: [
            {
              title: '初始化视角',
              name: 'flyTo',
              icon: 'icon-chushihuashijiao',
              params: [
                {
                  altitude: 237.02558519727847,
                  heading: 4.577567816832631,
                  latitude: 30.624635512479735,
                  longitude: 114.2644153328619,
                  tilt: 76.79984572685618
                }
              ]
            },
            {
              title: '放大',
              name: 'zoomIn',
              icon: 'icon-jiahao'
            },
            {
              title: '缩小',
              name: 'zoomOut',
              icon: 'icon-jianhaocu'
            },
            {
              title: '二维视图|三维视图',
              name: 'switchMode',
              icon: 'icon-diqiu'
            },
            {
              title: '全屏|取消全屏',
              name: 'fullScreen',
              icon: 'icon-quanping1'
            },
            {
              title: '清除',
              name: 'mapClear',
              icon: 'icon-clear'
            },
            {
              title: '鹰眼',
              name: 'GafMapBirdEye',
              icon: 'icon-quyudingwei',
              toggle: true
            }
          ],
          smSceneList: [
            {
              resourceId: '8000',
              location: true,
              resourceLocation:
                'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD'
            }
          ]
        }
      },
      methods: {
        DirectionHandler() {
          if (this.positionH === 'top') {
            this.positionH = 'bottom'
          } else {
            this.positionH = 'top'
          }
        },
        verticalHandler(e) {
          this.positionV = e.target.id
        }
      },
      beforeMount() {
        Vue.prototype.$mapActions = gafmapui.mapActions
      },
    }`,
    css: `
    .toolButtonGroup {
      z-index: 1;
    }
    .topLeft {
      position: absolute;
      top: 10px;
      left: 20px;
    }
    .topRight {
      position: absolute;
      top: 10px;
      right: 20px;
    }
    .bottomLeft {
      position: absolute;
      bottom: 10px;
      left:20px;
    }
    .bottomRight {
      position: absolute;
      bottom: 100px;
      right: 20px;
    }
    .title{
      color:black;
    }
    .btn{
      background-color: #00b8ff;
      color: #fff;
      border: none;
      padding: 5px;
      outline:none;
    }
    .wrap-btn{
      position: absolute;
      top: 20px;
      left: 50px;
      z-index: 99;
    }
    `
  },
  MapTreeSwitch: {
    html: `<gaf-map-viewer :sm-scene-list="smSceneList" :sm-layer-list="smLayerList">
    <gaf-map-draggable title="资源目录树" :visible="true">
      <div class="resourceTree">
        <gaf-map-tree-switch
          :replace-fields="replaceFields"
          :data-list="allDataList"
          :check="onCheck"
          :select="onSelect"
          :all-checkable="false"
          :leafnode-checkable="true"
          :somen-node-checkable="false"
          @switchFun="switchFun"
          @checkKeys="checkKeys"
        />
      </div>
    </gaf-map-draggable>
    <gaf-map-draggable
      v-if="openTable"
      title="图层控制"
      :visible="openTable"
      class="tree-draggable"
    >
      <div class="resourceTree">
        <gaf-switch-table
          :choose-list="chooseList"
          @onSelectedChange="onSelectedChange"
        ></gaf-switch-table>
      </div>
    </gaf-map-draggable>
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          allDataList: [
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
              resourceId: 7169,
              pid: 2,
              location: true,
              resourceName: 'CBD',
              resourceLocation:
                'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD',
              resourceTag: 'RESTREALSPACE',
              resourceTagCN: '三维服务',
              sourceType: 'SUPERMAP',
              sourceTypeName: 'SuperMap',
              opacity: 0.2,
            },
            {
              resourceId: 7170,
              pid: 2,
              location: true,
              resourceName: 'BIM',
              resourceLocation:
                'http://www.supermapol.com/realspace/services/3D-wireFrame/rest/realspace/scenes/wireFrame',
              resourceTag: 'RESTREALSPACE',
              resourceTagCN: '三维服务',
              sourceType: 'SUPERMAP',
              sourceTypeName: 'SuperMap',
              opacity: 0.5,
            },
            {
              resourceId: 7171,
              pid: 2,
              location: true,
              resourceName: 'niaocao',
              resourceLocation:
                'http://www.supermapol.com/realspace/services/3D-OlympicGreen20200416/rest/realspace/scenes/%E6%9C%AA%E5%91%BD%E5%90%8D%E5%9C%BA%E6%99%AF',
              resourceTag: 'RESTREALSPACE',
              resourceTagCN: '三维服务',
              sourceType: 'SUPERMAP',
              sourceTypeName: 'SuperMap',
              opacity: 0.4,
            },
            {
              resourceId: 7166,
              pid: 1,
              location: true,
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
              resourceId: 7165,
              pid: 1,
              location: true,
              resourceName: 'zhufeng',
              resourceLocation:
                'http://www.supermapol.com/realspace/services/map-zhufeng/rest/maps/pol1_P_1%40zhufeng',
              resourceTag: 'RESTMAP',
              resourceTagCN: '地图服务',
              sourceType: 'SUPERMAP',
              sourceTypeName: 'SuperMap',
              opacity: 0.7,
            },
            {
              resourceId: 7167,
              pid: 1,
              location: true,
              resourceName: 'WorldMap',
              resourceLocation:
                'http://www.supermapol.com/realspace/services/map-sample/rest/maps/WorldMap',
              resourceTag: 'RESTMAP',
              resourceTagCN: '地图服务',
              sourceType: 'SUPERMAP',
              sourceTypeName: 'SuperMap',
              opacity: 0.5,
            },
          ],
          replaceFields: {
            title: 'resourceName',
            key: 'resourceId',
          },
          layerList: [],
          chooseList: [],
          openTable: false,
          checkedKeys: [],
        }
      },
      computed: {
        smSceneList() {
          return this.layerList.filter(
            (item) => item.resourceTag === 'RESTREALSPACE'
          )
        },
        smLayerList() {
          return this.layerList.filter((item) => item.resourceTag === 'RESTMAP')
        },
      },
      methods: {
        checkKeys(checkedKeys) {
          this.checkedKeys = checkedKeys
        },
        switchFun(openIf) {
          this.openTable = openIf
        },
        onSelectedChange(row) {
          for (let i = 0; i < this.checkedKeys.length; i++) {
            if (this.checkedKeys[i] < 10) {
              this.checkedKeys.splice(i, 1)
            }

            if (row.resourceId === this.checkedKeys[i]) {
              this.checkedKeys.splice(i, 1)
            }
          }
          this.onCheck(this.checkedKeys)
        },
        onCheck(checkedKeys) {
          const layerList = []
          checkedKeys.forEach((key) => {
            const data = this.allDataList.find((item) => {
              return item.resourceId.toString() === key.toString()
            })
            if (data) {
              data.isVisible = true
              layerList.unshift(data)
            }
          })
          for (let i = 0; i < layerList.length; i++) {
            if (layerList[i].pid === '') {
              layerList.splice(i, 1)
            }
          }
          this.layerList = layerList
          this.chooseList = this.layerList.map((ll) => ({ ...ll }))
        },
        onSelect() {},
      },
    }`,
    css: `.draggable > .header {
      background: rgba(38, 38, 38, 0.5);
    }
    .resourceTree {
      position: relative;
      z-index: 1;
      background: rgba(38, 38, 38, 0.5);
      max-height: 500px;
      overflow: auto;
    }
    .draggable {
      width: 400px !important;
    }`
  },
  GafMapDraggable: {
    html: `<gaf-map-viewer :smSceneList="smSceneList">
      <gaf-map-draggable title="拖拽盒子" :visible="true" placement="right">
      </gaf-map-draggable>
    </gaf-map-viewer>
    `,
    js: `export default {
      data() {
        return {
          smSceneList: [
            {
              resourceId: '8000',
              location: true,
              resourceLocation:
                'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD'
            }
          ]
        }
      }
    }`,
    css: `
    .draggable .header .title{
      color:#fff;
    }
    #draggable {
      background-color: var(--bg-color ,rgba(25, 40, 58, 0.6));
      border-width: 0;
      color:var(--font-color,#fff);;
      margin-left: 30px;
      padding: 0 15px 0;
      font-size: 14px;
      .ant-slider-mark-text {
        color: var(--font-color,#fff);
      }
      .sm-content,
      .sm-profile3d-content {
        background-color: transparent;
      }
      .GafMapProfile {
        .draggable {
          .header {
            background: transparent;
          }
        }
      }
    }
    `
  },
  GafMapDrawer: {
    html: `<gaf-map-viewer :smSceneList="smSceneList">
    <div class="wrap-btn">
      <button id="bottom" class="btn" @click="showDrawer($event)">下</button>
      <button id="left" class="btn" @click="showDrawer($event)">左</button>
      <button id="right" class="btn" @click="showDrawer($event)">右</button>
    </div>
    <gaf-map-drawer
      :title="title"
      :visible="visible"
      :placement="placement"
      @toggle="onToggle"
    >
    </gaf-map-drawer>
    </gaf-map-viewer>
    `,
    js: `export default {
      data() {
        return {
          title: "弹窗",
          visible:false,
          placement:'left',
          smSceneList: [
            {
              resourceId: '8000',
              location: true,
              resourceLocation:
                'https://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/scenes/CBD'
            }
          ]
        }
      },
      methods: {
        showDrawer(e) {
          if(this.visible) {
              this.visible = false
          }
          this.$nextTick(() => {
            this.placement = e.target.id
            this.visible = true
          })
        },
        onToggle() {
          this.visible = !this.visible
        }
      }
    }`,
    css: `
    .btn{
      background-color: #00b8ff;
      color: #fff;
      border: none;
      padding: 5px 10px;
      outline:none;
    }
    .wrap-btn{
      position: absolute;
      top: 20px;
      left: 50px;
      z-index: 99;
    }
    `
  },
  MapTree: {
    html: `<gaf-map-viewer :serviceList="layerList">
    <gaf-map-draggable title="资源目录树" :visible="true">
        <gaf-map-layer-tree
          :replaceFields="replaceFields"
          :dataList="allDataList"
          :check="onTreeNodeChecked"
          :select="onSelect"
          :allCheckable="false"
          :leafnodeCheckable="true"
          :somenNodeCheckable="false"
        />
    </gaf-map-draggable>
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          allDataList: [
            {
              resourceId: 1,
              resourceName: '2维图层',
              pid: ''
            },
            {
              resourceId: 2,
              resourceName: '3维数据',
              pid: ''
            },
            {
              resourceId: 7173,
              pid: 2,
              resourceName: 'Building',
              resourceLocation: 'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/datas/Building@CBD',
              resourceTag: 'RESTREALSPACE-DATA',
              resourceTagCN: '三维服务',
              sourceType: 'SUPERMAP',
              sourceTypeName: 'SuperMap',
              opacity: 1
            },
            {
              resourceId: 7175,
              pid: 2,
              resourceName: 'Tree',
              resourceLocation: 'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/datas/Tree@CBD',
              resourceTag: 'RESTREALSPACE-DATA',
              resourceTagCN: '三维服务',
              sourceType: 'SUPERMAP',
              sourceTypeName: 'SuperMap',
              location: false,
              opacity: 1,
            },
            {
              resourceId: 7174,
              pid: 2,
              resourceName: 'ground',
              resourceLocation: 'http://www.supermapol.com/realspace/services/3D-CBD/rest/realspace/datas/Ground_1@CBD',
              resourceTag: 'RESTREALSPACE-DATA',
              resourceTagCN: '三维服务',
              sourceType: 'SUPERMAP',
              sourceTypeName: 'SuperMap',
              opacity: 1
            },
            {
              resourceId: 7165,
              pid: 1,
              location: true,
              resourceName: 'zhufeng',
              resourceLocation:
                'http://www.supermapol.com/realspace/services/map-zhufeng/rest/maps/pol1_P_1%40zhufeng',
              resourceTag: 'RESTMAP',
              resourceTagCN: '地图服务',
              sourceType: 'SUPERMAP',
              sourceTypeName: 'SuperMap',
              opacity: 0.6
            },
            {
              resourceId: 7166,
              pid: 1,
              location: true,
              resourceName: 'China400',
              resourceLocation:
                'http://www.supermapol.com/realspace/services/map-China400/rest/maps/China400',
              resourceTag: 'RESTMAP',
              resourceTagCN: '地图服务',
              sourceType: 'SUPERMAP',
              sourceTypeName: 'SuperMap',
              opacity: 0
            },
            {
              resourceId: 7167,
              pid: 1,
              location: true,
              resourceName: 'WorldMap',
              resourceLocation:
                'http://www.supermapol.com/realspace/services/map-sample/rest/maps/WorldMap',
              resourceTag: 'RESTMAP',
              resourceTagCN: '地图服务',
              sourceType: 'SUPERMAP',
              sourceTypeName: 'SuperMap',
              opacity: 0.6
            }
          ],
          replaceFields: {
            title: 'resourceName',
            key: 'resourceId'
          },
          layerList: []
        }
      },
      methods: {
        onTreeNodeChecked(checkedKeys, info) {
          const layerList = []
          checkedKeys.forEach(key => {
            const data = this.allDataList.find(item => {
              return item.resourceId.toString() === key.toString()
            })
            if (data) {
              layerList.push(data)
            }
          })
          this.layerList = layerList
        },
        onSelect() {}
      }
    }`,
    css: `#draggable {
      background-color: var(--bg-color, rgba(25, 40, 58, 0.6));
      border-width: 0;
      color: var(--font-color, #fff);
      margin-left: 30px;
      padding: 0 15px 0;
      font-size: 14px;
    }
    .ant-tree li .ant-tree-node-content-wrapper:hover,
    .ant-tree li .ant-tree-node-content-wrapper.ant-tree-node-selected {
      background-color: transparent;
    }
    `
  },
  OverlayAnalysis: {
    html: `<gaf-map-viewer :sm-layer-list="smLayerList">
    <div class="search">
      <gaf-map-app-query
        :server-datas="serverDatas"
      />
    </div>
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          smLayerList: [
            {
              resourceId: 7000,
              resourceLocation:
                'https://iserver.supermap.io/iserver/services/map-world/rest/maps/World',
              resourceName: 'World',
            },
            {
              resourceId: 7001,
              resourceLocation:
                'http://iserver.gaf-dev.chengdu/iserver/services/map-Ws_Rec322/rest/maps/rec_1%40Ds_Rec322',
              resourceName: '京津地区地图',
              location: true,
            },
          ],
          serverDatas: [
            {
              host:
                'http://192.168.11.172:30712/iserver/services/spatialAnalysis-Ws_Rec322/restjsr/spatialanalyst',
              title: '叠加分析',
              overLay: 'rec_1@Ds_Rec322',
              // 叠加操作，可选值为 clip（裁剪）、erase（擦除）、identity（同一）、intersect（相交）、union（合并）、update（更新）、XOR（对称差）
              operation: 'CLIP',
              tolerance: 0,
              returnContent: false,
              dataReturnOption: {
                dataReturnMode: 'RECORDSET_ONLY',
                expectCount: 100,
                deleteExistResultDataset: false,
              },
            },
          ],
        }
      },
    }`,
    css: `.search {
      margin-left: 15%;
      background: rgba(255, 255, 255, 0.9);
      border-radius: 5px;
      color: rgba(0, 0, 0, 0.65);
      padding: 10px;
    }
    .ant-btn.ant-btn-loading:not(.ant-btn-circle):not(.ant-btn-circle-outline):not(.ant-btn-icon-only) {
      position: absolute;
      padding-left: auto;
      pointer-events: none;
    }`
  },
  RecordsetDisplay: {
    html: `<gaf-map-viewer>
    <gaf-map-draggable
      id="close-style"
      title="分析结果"
      :visible="draggableVisible"
      :width="720"
      :height="580"
      @cancel="handleCloseButton"
    >
      <gaf-recordset-table :recordset="recordset"></gaf-recordset-table>
    </gaf-map-draggable>
  </gaf-map-viewer>`,
    js: `export default {
      data() {
        return {
          draggableVisible: true,
          recordset: {
            features: [
              {
                stringID: null,
                fieldNames: [
                  'SmID',
                  'SmUserID',
                  'SmArea',
                  'SmPerimeter',
                  'SmGeometry',
                  'F_1',
                  'F_2',
                  'F_3',
                ],
                geometry: {
                  center: {
                    x: -35.25968747465302,
                    y: -364.38706534025,
                  },
                  parts: [6],
                  style: null,
                  prjCoordSys: null,
                  id: 1,
                  type: 'REGION',
                  partTopo: [1],
                  points: [
                    {
                      x: 57.78609257251628,
                      y: -320.3197375638,
                    },
                    {
                      x: -144.36459134939997,
                      y: -320.3197375638,
                    },
                    {
                      x: -144.36459134939997,
                      y: -408.4543931167,
                    },
                    {
                      x: 83.39704355915455,
                      y: -408.4543931167,
                    },
                    {
                      x: 73.88259154188108,
                      y: -364.4896252707613,
                    },
                    {
                      x: 57.78609257251628,
                      y: -320.3197375638,
                    },
                  ],
                },
                fieldValues: [
                  '1',
                  '0',
                  '2.121957325913705E14',
                  '0.0',
                  '',
                  '10',
                  '绿化区',
                  '13.0',
                ],
                ID: 1,
              },
              {
                stringID: null,
                fieldNames: [
                  'SmID',
                  'SmUserID',
                  'SmArea',
                  'SmPerimeter',
                  'SmGeometry',
                  'F_1',
                  'F_2',
                  'F_3',
                ],
                geometry: {
                  center: {
                    x: 739.9808389975277,
                    y: -364.38706534025,
                  },
                  parts: [6],
                  style: null,
                  prjCoordSys: null,
                  id: 2,
                  type: 'REGION',
                  partTopo: [1],
                  points: [
                    {
                      x: 380.6629742691254,
                      y: -408.4543931167,
                    },
                    {
                      x: 1100.2035749439,
                      y: -408.4543931167,
                    },
                    {
                      x: 1100.2035749439,
                      y: -320.3197375638,
                    },
                    {
                      x: 381.6831808034282,
                      y: -320.3197375638,
                    },
                    {
                      x: 379.24799978400415,
                      y: -376.06393805193994,
                    },
                    {
                      x: 380.6629742691254,
                      y: -408.4543931167,
                    },
                  ],
                },
                fieldValues: [
                  '2',
                  '0',
                  '1.1639249128831946E11',
                  '0.0',
                  '',
                  '10',
                  '绿化区',
                  '13.0',
                ],
                ID: 2,
              },
              {
                stringID: null,
                fieldNames: [
                  'SmID',
                  'SmUserID',
                  'SmArea',
                  'SmPerimeter',
                  'SmGeometry',
                  'F_1',
                  'F_2',
                  'F_3',
                ],
                geometry: {
                  center: {
                    x: 968.0015916143964,
                    y: 386.0928804289,
                  },
                  parts: [6],
                  style: null,
                  prjCoordSys: null,
                  id: 3,
                  type: 'REGION',
                  partTopo: [1],
                  points: [
                    {
                      x: 933.1270845008979,
                      y: 1028.4075671103,
                    },
                    {
                      x: 899.8975395961963,
                      y: 992.163787309977,
                    },
                    {
                      x: 899.8975395961963,
                      y: -256.2218062525001,
                    },
                    {
                      x: 1036.1056436325964,
                      y: -256.2218062525001,
                    },
                    {
                      x: 1036.1056436325964,
                      y: 1028.4075671103,
                    },
                    {
                      x: 933.1270845008979,
                      y: 1028.4075671103,
                    },
                  ],
                },
                fieldValues: [
                  '3',
                  '0',
                  '1.7116531688756862E14',
                  '0.0',
                  '',
                  '1',
                  '休息区',
                  '16.0',
                ],
                ID: 3,
              },
              {
                stringID: null,
                fieldNames: [
                  'SmID',
                  'SmUserID',
                  'SmArea',
                  'SmPerimeter',
                  'SmGeometry',
                  'F_1',
                  'F_2',
                  'F_3',
                ],
                geometry: {
                  center: {
                    x: -280.57269538585,
                    y: -39.43144857139333,
                  },
                  parts: [8],
                  style: null,
                  prjCoordSys: null,
                  id: 4,
                  type: 'REGION',
                  partTopo: [1],
                  points: [
                    {
                      x: -379.3903394907,
                      y: 161.33442628191335,
                    },
                    {
                      x: -379.3903394907,
                      y: -240.19732342470002,
                    },
                    {
                      x: -181.75505128100002,
                      y: -240.19732342470002,
                    },
                    {
                      x: -181.75505128100002,
                      y: 16.59214079887215,
                    },
                    {
                      x: -192.71936042271204,
                      y: 27.06207726855996,
                    },
                    {
                      x: -278.1893405713554,
                      y: 94.82361240844443,
                    },
                    {
                      x: -373.04206757075485,
                      y: 157.79560941013335,
                    },
                    {
                      x: -379.3903394907,
                      y: 161.33442628191335,
                    },
                  ],
                },
                fieldValues: [
                  '4',
                  '0',
                  '2.1246277578159492E13',
                  '0.0',
                  '',
                  '1',
                  '休息区',
                  '55.0',
                ],
                ID: 4,
              },
              {
                stringID: null,
                fieldNames: [
                  'SmID',
                  'SmUserID',
                  'SmArea',
                  'SmPerimeter',
                  'SmGeometry',
                  'F_1',
                  'F_2',
                  'F_3',
                ],
                geometry: {
                  center: {
                    x: -280.57269538585,
                    y: 910.9707876266502,
                  },
                  parts: [7],
                  style: null,
                  prjCoordSys: null,
                  id: 5,
                  type: 'REGION',
                  partTopo: [1],
                  points: [
                    {
                      x: -181.75505128100002,
                      y: 835.7902089384561,
                    },
                    {
                      x: -181.75505128100002,
                      y: 1073.8102684558,
                    },
                    {
                      x: -379.3903394907,
                      y: 1073.8102684558,
                    },
                    {
                      x: -379.3903394907,
                      y: 748.1313067975004,
                    },
                    {
                      x: -364.8776037603625,
                      y: 753.3203223162368,
                    },
                    {
                      x: -183.57618033300832,
                      y: 834.7887355290673,
                    },
                    {
                      x: -181.75505128100002,
                      y: 835.7902089384561,
                    },
                  ],
                },
                fieldValues: [
                  '5',
                  '0',
                  '1.0453857394239995E14',
                  '0.0',
                  '',
                  '1',
                  '休息区',
                  '55.0',
                ],
                ID: 5,
              },
              {
                stringID: null,
                fieldNames: [
                  'SmID',
                  'SmUserID',
                  'SmArea',
                  'SmPerimeter',
                  'SmGeometry',
                  'F_1',
                  'F_2',
                  'F_3',
                ],
                geometry: {
                  center: {
                    x: -32.47920097084013,
                    y: -161.4169225418507,
                  },
                  parts: [4],
                  style: null,
                  prjCoordSys: null,
                  id: 6,
                  type: 'REGION',
                  partTopo: [1],
                  points: [
                    {
                      x: -40.2054529687,
                      y: -138.72221155630143,
                    },
                    {
                      x: -40.2054529687,
                      y: -184.1116335274,
                    },
                    {
                      x: -9.3004449772605,
                      y: -184.1116335274,
                    },
                    {
                      x: -40.2054529687,
                      y: -138.72221155630143,
                    },
                  ],
                },
                fieldValues: [
                  '6',
                  '0',
                  '9.063793885974645E12',
                  '0.0',
                  '',
                  '0',
                  '休息区',
                  '88.0',
                ],
                ID: 6,
              },
              {
                stringID: null,
                fieldNames: [
                  'SmID',
                  'SmUserID',
                  'SmArea',
                  'SmPerimeter',
                  'SmGeometry',
                  'F_1',
                  'F_2',
                  'F_3',
                ],
                geometry: {
                  center: {
                    x: 606.35038016785,
                    y: 45.87174401735001,
                  },
                  parts: [6],
                  style: null,
                  prjCoordSys: null,
                  id: 7,
                  type: 'REGION',
                  partTopo: [1],
                  points: [
                    {
                      x: 432.68771826859995,
                      y: 81.98935470078169,
                    },
                    {
                      x: 432.68771826859995,
                      y: -51.21525594209999,
                    },
                    {
                      x: 780.0130420671,
                      y: -51.21525594209999,
                    },
                    {
                      x: 780.0130420671,
                      y: 142.9587439768,
                    },
                    {
                      x: 446.21172974987974,
                      y: 142.9587439768,
                    },
                    {
                      x: 432.68771826859995,
                      y: 81.98935470078169,
                    },
                  ],
                },
                fieldValues: [
                  '7',
                  '0',
                  '3.295841556941845E14',
                  '0.0',
                  '',
                  '1',
                  '绿化区',
                  '36.0',
                ],
                ID: 7,
              },
              {
                stringID: null,
                fieldNames: [
                  'SmID',
                  'SmUserID',
                  'SmArea',
                  'SmPerimeter',
                  'SmGeometry',
                  'F_1',
                  'F_2',
                  'F_3',
                ],
                geometry: {
                  center: {
                    x: 696.5826653664453,
                    y: 495.07004664655,
                  },
                  parts: [8],
                  style: null,
                  prjCoordSys: null,
                  id: 8,
                  type: 'REGION',
                  partTopo: [1],
                  points: [
                    {
                      x: 529.774718228,
                      y: 392.8724129756853,
                    },
                    {
                      x: 529.774718228,
                      y: 240.04574393619998,
                    },
                    {
                      x: 821.0357181063,
                      y: 240.04574393619998,
                    },
                    {
                      x: 821.0357181063,
                      y: 750.0943493569,
                    },
                    {
                      x: 715.6499539243247,
                      y: 750.0943493569,
                    },
                    {
                      x: 656.1353027444916,
                      y: 656.7263373740805,
                    },
                    {
                      x: 572.8830984416738,
                      y: 496.88812383454626,
                    },
                    {
                      x: 529.774718228,
                      y: 392.8724129756853,
                    },
                  ],
                },
                fieldValues: [
                  '8',
                  '0',
                  '2.135217282760523E14',
                  '0.0',
                  '',
                  '0',
                  '绿化区',
                  '15.4',
                ],
                ID: 8,
              },
            ],
            fieldCaptions: [
              'SmID',
              'SmUserID',
              'SmArea',
              'SmPerimeter',
              'SmGeometry',
              '人口数',
              '区域',
              '绿化率',
            ],
            fieldTypes: [
              'INT32',
              'INT32',
              'DOUBLE',
              'DOUBLE',
              'LONGBINARY',
              'INT32',
              'TEXT',
              'DOUBLE',
            ],
            datasetName: null,
            fields: [
              'SmID',
              'SmUserID',
              'SmArea',
              'SmPerimeter',
              'SmGeometry',
              'F_1',
              'F_2',
              'F_3',
            ],
          },
        }
      },
      methods: {
        handleCloseButton() {
          this.draggableVisible = false
        },
      },
    }`,
    css: ``
  },
  ...sm3dComponentData
};
