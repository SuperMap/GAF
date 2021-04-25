;(function (root) {
  root.SMWEBGIS = root.SMWEBGIS || {}
  root.SMWEBGIS.panel = root.SMWEBGIS.panel || {}
  root.SMWEBGIS.panel.whiteModel = {
    whiteModelName: 'WuHanBaiMo',
    // 灯光配置
    dayLights: {
      pointLights: [],
      directionalLights: [
        {
          position: [106.282013734667, 29.7615756638933, 630],
          targetposition: [106.756840202248, 29.3980661015228, 500],
          options: {
            color: [0.6, 0.6, 0.6, 1],
            intensity: 0.4,
          },
        },
        {
          position: [106.756840202248, 29.3980661015228, 500],
          targetposition: [106.282013734667, 29.7615756638933, 630],
          options: {
            color: [0.7, 0.7, 0.7, 1],
            intensity: 0.6,
          },
        },
        {
          position: [106.452, 29.573, 1],
          targetposition: [106.454, 29.581, 91],
          options: {
            color: [0.5, 0.5, 0.5, 1],
            intensity: 0.6,
          },
        },
        {
          position: [106.571378546495, 29.5603965436718, 1000],
          targetposition: [106.571378546495, 29.5603965436718, 10],
          options: {
            color: [0.7, 0.7, 0.7, 1],
            intensity: 0.65,
          },
        },
      ],
    },
    nightLights: {
      pointLights: [
        {
          position: [106.571285893056, 29.560471562369, 360],
          options: {
            cutoffDistance: 2000,
            color: [0.6, 0.3, 0.1, 1.0],
            intensity: 1.5,
          },
        },
        {
          position: [106.440835888168, 29.5860776240763, 200],
          options: {
            cutoffDistance: 3000,
            color: [0.9, 0.3, 0.3, 1.0],
            intensity: 2.5,
          },
        },
        {
          position: [106.595770644757, 29.6701144056505, 200],
          options: {
            cutoffDistance: 3000,
            color: [0.6, 0.3, 0.1, 1.0],
            intensity: 2.5,
          },
        },
        {
          position: [106.434327803494, 29.504044534872, 200],
          options: {
            cutoffDistance: 3000,
            color: [0.6, 0.6, 0.1, 1.0],
            intensity: 2.5,
          },
        },
        {
          position: [106.596187330546, 29.4309579929797, 200],
          options: {
            cutoffDistance: 2000,
            color: [0.2, 0.6, 0.1, 1.0],
            intensity: 2.5,
          },
        },
      ],
      directionalLights: [
        {
          position: [106.437, 29.571, 71],
          targetposition: [106.436, 29.569, 1],
          options: {
            color: [1, 1.54, 2.3, 1],
            intensity: 0.15,
          },
        },
        {
          position: [106.452, 29.573, 1],
          targetposition: [106.454, 29.581, 91],
          options: {
            color: [1, 2, 3, 1],
            intensity: 0.15,
          },
        },
        {
          position: [106.492, 29.593, 100],
          targetposition: [106.492, 29.593, 10],
          options: {
            color: [0.25, 0.25, 0.25, 1],
            intensity: 0.35,
          },
        },
      ],
    },
    colorPlanList: [
      {
        color: [84, 140, 168, 1],
        edgeColor: [255, 255, 255, 0.8],
      },
      {
        color: [235, 242, 247, 1],
        edgeColor: [0, 0, 0, 0.4],
      },
      {
        color: [177, 191, 203, 1],
        edgeColor: [50, 50, 50, 0.3],
      },
      // {
      //   color: [62, 88, 117, 0.5],
      //   edgeColor: [50, 50, 50, 0.5]
      // },
      {
        color: [153, 242, 240, 1],
        edgeColor: [50, 50, 50, 0.5],
      },
      {
        color: [255, 255, 255, 1],
        edgeColor: [0, 0, 0, 0.6],
      },
    ],
  }
  root.SMWEBGIS.panel.roller = {
    one: {
      RollerLayersData: [
        {
          resourceId: 17169,
          pid: '',
          resourceName: '模型1',
          resourceLocation:
            host + '/iserver/services/3D-cim_gis/rest/realspace/datas/J_V01',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '三维服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          location: true,
          supportDataService: true, // 是否支持数据服务查询
          layerName: 'Building@CBD', // 可查询数据服务得图层名称
          dataSetName: 'Building', // 可查询数据服务得数据集名称
          cameraCoordinate: {
            altitude: 114.3764743079913,
            heading: 5.654236538948201,
            latitude: 30.466650989920087,
            tilt: 70.60882457049563,
            longitude: 114.25722359896619,
          },
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
        },
      ],
      checkedLayerKeys: [17169],
    },
    two: {
      RollerLayersData: [
        {
          resourceId: 7169,
          pid: '',
          resourceName: '模型2',
          resourceLocation:
            host + '/iserver/services/3D-cim_gis/rest/realspace/datas/G_V01',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '地图服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          cameraCoordinate: {
            altitude: 114.3764743079913,
            heading: 5.654236538948201,
            latitude: 30.466650989920087,
            tilt: 70.60882457049563,
            longitude: 114.25722359896619,
          },
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
      ],
      checkedLayerKeys: [7169],
    },
  }
  root.SMWEBGIS.panel.splitScreen = {
    one: {
      splitLayersData: [
        {
          resourceId: 7189,
          pid: '',
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
          cameraCoordinate: {
            altitude: 62.46821991822186,
            heading: 6.2730744988966975,
            latitude: 30.484909153821587,
            tilt: 72.7806564730819,
            longitude: 114.30882900103435,
          },
        },
      ],
      checkedLayerKeys: [7169, 7189],
      link: true,
    },
    two: {
      splitLayersData: [
        {
          resourceId: 7169,
          pid: '',
          resourceName: 'G_V01',
          resourceLocation:
            host + '/iserver/services/3D-cim_gis/rest/realspace/datas/G_V01',
          resourceTag: 'RESTREALSPACE-DATA',
          resourceTagCN: '地图服务',
          sourceType: 'SUPERMAP',
          sourceTypeName: 'SuperMap',
          opacity: 1,
          cameraCoordinate: {
            altitude: 114.3764743079913,
            heading: 5.654236538948201,
            latitude: 30.466650989920087,
            tilt: 70.60882457049563,
            longitude: 114.25722359896619,
          },
        },
        {
          resourceId: 7189,
          pid: '',
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
          cameraCoordinate: {
            altitude: 62.46821991822186,
            heading: 6.2730744988966975,
            latitude: 30.484909153821587,
            tilt: 72.7806564730819,
            longitude: 114.30882900103435,
          },
        },
      ],
      checkedLayerKeys: [7169, 7189],
      link: true,
    },
  }
  root.SMWEBGIS.panel.query = {
    dataServiceConfig: {
      url: host + '/iserver/services/data-cim_gis/rest/data',
      dataSourceName: 'cim-busi', // 数据源
      keyWord: 'SmID',
    },
  }
})(this)
