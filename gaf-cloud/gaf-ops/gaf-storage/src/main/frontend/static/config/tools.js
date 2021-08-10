/**
 * 工具条配置项说明
 * {
 *  title: '鼠标移入时提示信息',
 *  name: '要执行的方法或者要加载的组件',
 *  icon: '自定义图标class',
 *  params: {name:'参数1', type:'参数2'}, //为方法时可以传递的参数
 *  actions: ['func1', 'func2'] // 当加载组件或者执行方法时要执行的其他工具类方法
 *  toggle: true|false 启用单次激活功能并高亮图标；二次单击关闭功能并取消高亮
 *  closePanel: true|false 单击方法类工具按钮是否关闭打开的面板
 *  selectedName: '' 当包含子菜单时，单击子菜单时需要选中的父级菜单
 *  closedName: '' 当包含子菜单时，单击子菜单时需要关闭的父级菜单
 * }
 */
;(function (root) {
  root.SMWEBGIS = root.SMWEBGIS || {}
  root.SMWEBGIS.tools = {
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
      // {
      //   title: 'VR模式',
      //   name: 'gaf-map-measure',
      //   icon: 'icon-vr'
      // },
      // {
      //   title: '操作帮助',
      //   name: 'splitScreenHorizontal',
      //   icon: 'icon-bangzhu'
      // }
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
        name: 'GafMapSplitScreen',
        icon: 'icon-fenping1',
        children: [
          {
            title: '水平分屏',
            name: 'GafMapSplitScreen',
            icon: '',
            actions: [],
            selectedName: 'GafMapSplitScreen',
            params: {
              type: 'splitScreenHorizontal',
              ...window.SMWEBGIS.panel.splitScreen,
            },
          },
          {
            title: '垂直分屏',
            name: 'GafMapSplitScreen',
            icon: '',
            actions: [],
            selectedName: 'GafMapSplitScreen',
            params: {
              type: 'splitScreenVertical',
              ...window.SMWEBGIS.panel.splitScreen,
            },
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
        title: '地图基本要素',
        name: 'gaf-map-basic-element',
        icon: 'icon-jibenyaosu',
        actions: [],
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
        title: '卷帘',
        name: 'RollerShutterType',
        icon: 'icon-juanlian1',
        type: 'panel',
        children: [
          {
            title: '左右卷帘',
            name: 'RollerShutter',
            icon: '',
            params: {
              type: 'vertical',
              ...window.SMWEBGIS.panel.roller,
            },
            selectedName: 'RollerShutterType',
          },
          {
            title: '上下卷帘',
            name: 'RollerShutter',
            icon: '',
            params: {
              type: 'horizontal',
              ...window.SMWEBGIS.panel.roller,
            },
            selectedName: 'RollerShutterType',
          },
        ],
      },
      {
        title: '泛光扫描线',
        name: 'gaf-map-flood-light',
        icon: 'icon-dingwei',
        actions: ['mapClear'],
      },
      {
        title: '属性查询',
        name: 'gaf-map-attribute-select',
        icon: 'icon-dianxuan',
        type: 'panel',
        params: { color: 'red', ...window.SMWEBGIS.panel.query },
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
      // {
      //   title: '视点管理',
      //   name: 'gaf-map-viewpoint-manage',
      //   icon: 'icon-shidian',
      //   actions: ['mapClear'],
      // },
      {
        title: '白模',
        name: 'GafMapWhiteModel',
        icon: 'icon-moxing2',
        actions: ['mapClear'],
        params: {
          ...window.SMWEBGIS.panel.whiteModel,
        },
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
        title: '分层分户查询',
        name: 'gaf-map-hierarchical-household',
        icon: 'icon-jianzhu',
        actions: ['mapClear'],
        params: {
          ...window.SMWEBGIS.panel.query,
        },
      },
      // 对接Gaf漫游后端
      {
        title: '三维漫游',
        name: 'RoamingGaf',
        icon: 'icon-manyou1',
        // actions: ['clear'],
        actions: ['mapClear'],
        params: { mySpeed: 60, useMySpeed: true },
      },
    ],
  }
})(this)
