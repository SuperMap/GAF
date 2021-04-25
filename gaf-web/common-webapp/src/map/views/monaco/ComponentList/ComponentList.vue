<template>
  <div class="componentList">
    <div class="List" :class="showlist ? 'showlist' : ''">
      <a-tree :tree-data="treeData" @select="onSelect">
        <div slot="title" slot-scope="{ name }" on="{}">
          {{ name }}
        </div>
      </a-tree>
    </div>
    <a-button class="showListButton" @click="handleShowList">
      <a-icon type="menu" />
    </a-button>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showlist: false,
      treeData: [
        {
          title: "基础组件",
          key: "基础组件",
          children: [
            { title: "三维球", key: "Earth", name: "Earth" },
            { title: "叠加二维地球", key: "LayerList2D", name: "LayerList2D" },
            { title: "三维场景展示", key: "SceneList3D", name: "SceneList3D" }
          ]
        },
        {
          title: "服务加载",
          key: "服务加载",
          children: [
            { title: "天地图", key: "MAPWORLD", name: "MAPWORLD" },
            { title: "REST", key: "REST", name: "REST" },
            { title: "WMS", key: "WMS", name: "WMS" },
            { title: "WMTS", key: "WMTS", name: "WMTS" },
            { title: "MVT", key: "MVT", name: "MVT" },
            { title: "ARCCGIS", key: "ARCCGIS", name: "ARCCGIS" },
            { title: "三维（数据集）", key: "DATA_3D", name: "DATA_3D" },
            { title: "三维（场景）", key: "SCENE_3D", name: "SCENE_3D" }
          ]
        },
        {
          title: "底图切换",
          key: "BottomChange",
          name: "BottomChange",
          children: []
        },
        {
          title: "工具条",
          key: "MapToolsPosition",
          name: "MapToolsPosition",
          children: []
        },
        {
          title: "资源目录树",
          key: "MapTree",
          name: "MapTree",
          children: []
        },
        {
          title: "资源目录树(含图层控制)",
          key: "MapTreeSwitch",
          name: "MapTreeSwitch",
          children: []
        },
        {
          title: "拖拽盒子",
          key: "GafMapDraggable",
          name: "GafMapDraggable",
          children: []
        },
        {
          title: "弹窗",
          key: "GafMapDrawer",
          name: "GafMapDrawer",
          children: []
        },
        {
          title: "事件处理",
          key: "事件处理",
          children: [
            {
              title: "initialize事件",
              key: "Initialize",
              name: "Initialize"
            },
             {
              title: "Clear事件",
              key: "Clear",
              name: "Clear"
            }, {
              title: "layer-loaded事件",
              key: "layerLoaded",
              name: "layerLoaded"
            }
            , {
              title: "panelToggle事件",
              key: "panelToggle",
              name: "panelToggle"
            }
            
          ]
        },
        {
          title: "基础分析",
          key: "基础分析",
          children: [
            {
              title: "测量",
              key: "Sm3dMeasure",
              name: "Sm3dMeasure"
            },
            {
              title: "可视域分析",
              key: "Sm3dViewshed",
              name: "Sm3dViewshed"
            },
            {
              title: "天际线分析",
              key: "Sm3dSkyline",
              name: "Sm3dSkyline"
            },
            {
              title: "剖面分析",
              key: "Sm3dProfile",
              name: "Sm3dProfile"
            },
            {
              title: "通视分析",
              key: "Sm3dSightline",
              name: "Sm3dSightline"
            },
            {
              title: "阴影分析",
              key: "Sm3Shadowquery",
              name: "Sm3Shadowquery"
            },
            {
              title: "地质体分析",
              key: "Sm3dGeologicBodyAnalysis",
              name: "Sm3dGeologicBodyAnalysis"
            }
          ]
        },
        {
          title: "地形分析",
          key: "地形分析",
          children: [
            {
              title: "地形操作",
              key: "Sm3dTerrainOperation",
              name: "Sm3dTerrainOperation"
            },
            {
              title: "淹没分析",
              key: "Sm3dTerrainFlood",
              name: "Sm3dTerrainFlood"
            },
            {
              title: "坡度坡向",
              key: "Sm3dTerrainSlope",
              name: "Sm3dTerrainSlope"
            },
            {
              title: "等值线",
              key: "Sm3dTerrainIsoline",
              name: "Sm3dTerrainIsoline"
            }
          ]
        },
        {
          title: "裁剪分析",
          key: "裁剪分析",
          children: [
            {
              title: "裁剪Box",
              key: "Sm3dClipBox",
              name: "Sm3dClipBox"
            },
            {
              title: "平面裁剪",
              key: "Sm3dClipPlane",
              name: "Sm3dClipPlane"
            },
            {
              title: "裁剪Cross",
              key: "Sm3dClipCross",
              name: "Sm3dClipCross"
            },
            {
              title: "多边形裁剪",
              key: "Sm3dClipPolygon",
              name: "Sm3dClipPolygon"
            }
          ]
        },
        {
          title: "查询分析",
          key: "查询分析",
          children: [
            {
              title: "基础分析组件",
              key: "BasicAnalysis",
              name: "BasicAnalysis"
            },
            {
              title: "查询分析组件",
              key: "QueryAnalysis",
              name: "QueryAnalysis"
            }
          ]
        },
        {
          title: "空间分析",
          key: "空间分析",
          children: [
            {
              title: "叠加分析(ISERVER )",
              key: "OverlayAnalysis",
              name: "OverlayAnalysis"
            },
            {
              title: "记录集展示组件",
              key: "RecordsetDisplay",
              name: "RecordsetDisplay"
            }
          ]
        }
      ]
      // data: [
      //   { title: '三维球', name: 'Earth' },
      //   { title: '叠加二维地球', name: 'LayerList2D' },
      //   { title: '三维场景展示', name: 'SceneList3D' },
      //   { title: '基础分析组件', name: 'BasicAnalysis' },
      //   { title: '工具条', name: 'MapTools' },
      // ],
    };
  },
  methods: {
    handleShowList() {
      this.showlist = !this.showlist;
    },
    onSelect(selectedKeys, { selected: bool, selectedNodes }) {
      if (bool && selectedNodes.length) {
        const selectedKey = selectedKeys[0];
        this.$emit("componentChange", selectedKey);
      }
    }
  }
};
</script>
<style lang="less" scoped>
.componentList {
  position: relative;
  // display: flex;
  // flex-direction: row;
  .List {
    width: 200px;
    height: 100%;
    padding: 10px;
  }
  .showlist {
    display: none;
  }
  .showListButton {
    position: absolute;
    top: 50%;
    left: 100%;
    transform: translate(-50%, -100%);
    z-index: 1;
  }
}
</style>
