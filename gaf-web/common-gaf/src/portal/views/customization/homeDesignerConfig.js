/**
 * Created by huanl on 2019/11/6
 */
export default [
  {
    type: 'basic',
    title: '基础控件',
    controls: [
      {
        icon: 'radar-chart',
        title: '平台概况',
        name: 'PlatResource',
        isShowMore: true,
        href: '/bpm/formDesgin',
        grafanaUrl: '/grafana/d/oWe9aYxmx/gafshou-ye-ding-zhi-jian-kong?refresh=30s&orgId=1&kiosk'
      },
      {
        icon: 'ordered-list',
        title: '进度控制',
        name: 'PlatStep',
        attr: {
          direction: 'vertical',
          mode: 'define',
          url: '',
          nodes: [],
        },
      },
      {
        icon: 'bar-chart',
        title: '统计图',
        name: 'PlatChart',
        attr: {
          width: '100%',
          height: 200,
          url: '',
        },
      },
      {
        icon: 'bars',
        title: '列表',
        name: 'PlatList',
        attr: {
          mode: 'define',
          url: '',
          data: [],
        },
      },
      {
        icon: 'table',
        title: '表格',
        name: 'PlatTable',
        attr: {
          showHeader: true,
          showBorder: true,
          showPage: false,
          pageIndex: 1,
          pageSize: 10,
          url: '',
          columns: [],
        },
      },
      {
        icon: 'link',
        title: '快速导航',
        name: 'PlatQuickLink',
        attr: {
          icon: 'user',
          link: '',
          embed: true,
          size: 128,
          title: '安全中心',
        },
      },
    ],
  },
  {
    type: 'grid',
    title: '布局控件',
    controls: [
      {
        icon: 'table',
        title: '栅格布局',
        name: 'grid',
        columns: [
          {
            title: 'col1',
            name: '栅格1',
            span: 12,
            list: [],
          },
          {
            title: 'col2',
            name: '栅格2',
            span: 12,
            list: [],
          },
        ],
      },
      {
        type: "table",
        // label: "表格布局",
        // icon: "icon-biaoge",
        icon: 'appstore',
        title: '表格布局',
        name: 'gridTable',
        trs: [
          {
            tds: [
              {
                colspan: 1,
                rowspan: 1,
                list: []
              },
              {
                colspan: 1,
                rowspan: 1,
                list: []
              }
            ]
          },
          {
            tds: [
              {
                colspan: 1,
                rowspan: 1,
                width: '50%',
                list: []
              },
              {
                colspan: 1,
                rowspan: 1,
                width: '50%',
                list: []
              }
            ]
          }
        ],
        options: {
          width: "100%",
          bordered: true,
          bright: false,
          small: true,
          customStyle: ""
        },
        key: "",
        model: ""
      }
    ],
  },
]
