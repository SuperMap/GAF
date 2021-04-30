<template>
  <div>
    <div class="map-btn">
      <button @click="openSuperposition">叠加分析</button>
    </div>
    <gaf-map-viewer
      :service-list="layerList"
      :token="token"
      :support-bird-eye="false"
      :enable-scale="true"
      :debug-show-frames-per-second="isDev"
      :reset-mouse-operation="false"
      :is-disable-underground-mode="false"
      @initialize="onViewerLoaded"
    ></gaf-map-viewer>
    <gaf-map-viewer-origin v-if="isSuperposition" :sm-layer-list="smLayerList">
      <div class="search">
        <gaf-map-app-query :server-datas="serverDatas" />
      </div>
    </gaf-map-viewer-origin>
    <div class="map-content">
      <tools-box :contentRight="contentRight" :content="content"></tools-box>
    </div>
    <div class="map-bottom">
      <layer-list
        v-if="initCheckLayerStatus"
        :data-list="dataList"
        :visible="layerListVisible"
        :checked-layer-keys="checkedLayerKeys"
        :default-expanded-keys="defaultExpandedKeys"
      />
    </div>
    <!-- <div class="map-bottom-branch">
      <layer-list-branch
        v-if="initCheckLayerStatusBranch"
        :data-list="dataListBranch"
        :visible="layerListVisibleBranch"
        :checked-layer-keys="checkedLayerKeys"
        :default-expanded-keys="defaultExpandedKeys"
      />
    </div> -->
    <Legend
      v-if="legendList && legendList.length > 0"
      :data-list="legendList"
    ></Legend>
  </div>
</template>

<script>
import Vue from "vue";
import "../../views/WebgisBasicApp/GlobalComponents/componentAll";
import { bus } from "~/utils/cimBus";
import LayerList from "../../views/WebgisBasicApp/LayerList/LayerList";
import ToolsBox from "../../views/WebgisBasicApp/ToolsBox/ToolsBox";
// import LayerListBranch from '../../views/WebgisBasicApp/LayerListBranch/LayerListBranch.vue';
export default {
  // middleware: 'auth',
  components: {
    LayerList,
    // LayerListBranch,
    ToolsBox,
  },
  data() {
    return {
      contentRight: [],
      businessTools: [],
      layerListVisible: false,
      layerListVisibleBranch: false,
      layerList: [],
      loaded: false,
      dataList: [],
      dataListBranch: [],
      checkedLayerKeys: [],
      defaultExpandedKeys: [],
      token: window.SMWEBGIS.dirs.TOKEN_TIANDITU || "",
      translateXNumber: 0,
      initCheckLayerStatus: false,
      // initCheckLayerStatusBranch: false,
      supportBackEnd: window.SMWEBGIS.supportBackEnd || false,
      currentPath: this.$nuxt.$route.path,
      isDev: window.SMWEBGIS.isDebug || false,
      commonTools: [],
      content: [],
      // 叠加分析
      isSuperposition: false,
      smLayerList: [
        {
          resourceId: 7000,
          resourceLocation:
            "https://iserver.supermap.io/iserver/services/map-world/rest/maps/World",
          resourceName: "World",
        },
        {
          resourceId: 7001,
          resourceLocation:
            "http://192.168.11.172:30712/iserver/services/map-Ws_Rec322/rest/maps/rec_1%40Ds_Rec322",
          resourceName: "京津地区地图",
          location: true,
        },
      ],
      serverDatas: [
        {
          host:
            "http://192.168.11.172:30712/iserver/services/spatialAnalysis-Ws_Rec322/restjsr/spatialanalyst",
          title: "叠加分析",
          overLay: "rec_1@Ds_Rec322",
          // 叠加方式
          operation: "CLIP",
          tolerance: 0,
          returnContent: false,
          dataReturnOption: {
            dataReturnMode: "RECORDSET_ONLY",
            expectCount: 100,
            deleteExistResultDataset: false,
          },
        },
      ],
    };
  },
  computed: {
    mvtLayerList() {
      return this.layerList.filter(
        (item) => item && item.resourceTag === "MVT"
      );
    },
    smSceneList() {
      return this.layerList.filter(
        (item) => item && item.resourceTag === "RESTREALSPACE"
      );
    },
    smSceneDataList() {
      return this.layerList.filter(
        (item) => item && item.resourceTag === "RESTREALSPACE-DATA"
      );
    },
    tinDemLayerList() {
      return this.layerList.filter(
        (item) => item && item.resourceTag === "TIN_DEM"
      );
    },
    gridDemLayerList() {
      return this.layerList.filter(
        (item) => item && item.resourceTag === "GRID_DEM"
      );
    },
    smLayerList() {
      return this.layerList.filter(
        (item) => item && item.resourceTag === "RESTMAP"
      );
    },
    tiandituLayerList() {
      return this.layerList.filter(
        (item) =>
          item && item.sourceType === "MAPWORLD" && item.isBaseLayer === true
      );
    },
    arcLayerList() {
      return this.layerList.filter(
        (item) => item && item.resourceTag === "ARCGIS"
      );
    },
    wmsLayerList() {
      return this.layerList.filter(
        (item) => item && item.resourceTag === "WMS"
      );
    },
    wmtsLayerList() {
      return this.layerList.filter(
        (item) => item && item.resourceTag === "WMTS"
      );
    },
    currentTools() {
      return this.supportBackEnd;
    },
    legendList() {
      return this.$store.state.legendList;
    },
  },
  watch: {
    currentTools: {
      handler() {
        this.initLayerAndTools();
        // this.initLayerAndToolsBranch();
      },
      immediate: true,
    },
  },
  beforeCreate() {
    Vue.prototype.$bus = bus;
  },
  beforeMount() {
    const self = this;
    bus.$on("onCheckedLayerList", (layers) => {
      const layerList = self.layerList;
      let checkedLayerList = [...layers];
      if (layerList.length) {
        const selectLayer = layerList.filter(
          (item) => item && item.location === true
        )[0];
        const newLayers =
          selectLayer &&
          layers.filter(
            (item) => item && item.resourceId !== selectLayer.resourceId
          );
        if (selectLayer && newLayers.length !== layers.length) {
          checkedLayerList = [...newLayers, selectLayer];
        } else {
          checkedLayerList = [...layers];
        }
      }
      self.layerList = checkedLayerList;
    });
  },
  mounted() {
    bus.$on("business-tools-item-click", (item) => {
      const names = ["GafMapSplitScreen", "RollerShutterType"];
      if (names.includes(item.selectedName)) {
        this.layerListVisible = false;
      }
    });
    // bus.$on("business-tools-item-click", (item) => {
    //   const names = ["GafMapSplitScreen", "RollerShutterType"];
    //   if (names.includes(item.selectedName)) {
    //     this.layerListVisibleBranch = false;
    //   }
    // });
    // 资源目录树打开关闭事件
    bus.$on("onLayerListToggle", (visible) => {
      this.layerListVisible = visible;
    });
    // bus.$on("onLayerListToggle", (visible) => {
    //   this.layerListVisibleBranch = visible;
    // });
    // 组件的左侧面板打开时图表面板需要关闭
    bus.$on("closeChartPanel", () => {
      this.onToggleLayerList();
    });
    this.$nextTick(function () {
      this.onToggleLayerList();
    });
  },
  created() {
    this.initLayerAndTools();
    // this.initLayerAndToolsBranch()
  },
  beforeDestroy() {
    this.$store.commit("setViewerLoaded", false);
  },
  methods: {
    openSuperposition() {
      this.isSuperposition = !this.isSuperposition;
    },
    async initLayerAndTools() {
      const dirs = window.SMWEBGIS.dirs;
      const tools = window.SMWEBGIS.tools;
      if (this.supportBackEnd) {
        const toolsbasicUrl = `/map/webgis-toolbars/815c6d97-0545-4bf6-9cf4-9410668fd311/config`;
        const toolscommonUrl = `/map/webgis-toolbars/a2ce8256-98c9-4f35-90e8-f5b08a5deba9/config`;
        const resourceUrl = `/map/resource-catalog/814a899b-195d-4f85-9340-5348abdbe07f/config`;
        const toolsRes = await this.$axios.$get(toolsbasicUrl);
        const toolscommonRes = await this.$axios.$get(toolscommonUrl);
        const resourceRes = await this.$axios.$get(resourceUrl);
        console.log('业务类', toolscommonRes);
        this.dataList = resourceRes.data.resourceTree.allDataList;
        if (toolscommonRes.data.horizontalToolbars[0] === undefined) {
          this.businessTools = [];
        } else {
          this.businessTools = toolscommonRes.data.horizontalToolbars[0].buttons;
        }
        if (toolsRes.data.verticalToolbars[0] === undefined) {
          this.commonTools = [];
        } else {
          this.commonTools = toolsRes.data.verticalToolbars[0].buttons;
        }
        this.checkedLayerKeys = [7175, 2001];
        this.defaultExpandedKeys = [23];
      } else {
        this.dataList = dirs.home || [];
        this.businessTools = tools.home || [];
        this.commonTools = window.SMWEBGIS.tools.right || [];
        this.checkedLayerKeys = dirs.checkedLayerKeys || [];
        this.defaultExpandedKeys = dirs.defaultExpandedKeys || [];
      }
      this.initCheckLayerStatus = true;
      this.contentRight = this.commonTools || [];
      this.content = this.businessTools;
      this.$emit("contentRight", this.contentRight);
      this.$emit("content", this.content);
    },
    // 打开叠加分析
    openSuperposition() {
      this.isSuperposition = !this.isSuperposition;
    },
    // async initLayerAndToolsBranch() {
    //   const dirs = window.SMWEBGIS.dirs;
    //   const tools = window.SMWEBGIS.tools;
    //   if (this.supportBackEnd) {
    //     const toolsbasicUrl = `/map/webgis-toolbars/815c6d97-0545-4bf6-9cf4-9410668fd311/config`;
    //     const toolscommonUrl = `/map/webgis-toolbars/7dde4f71-8551-4c93-bbfb-cdfe3b1edf16/config`;
    //     const resourceUrl = `/map/resource-catalog/8f015b32-2d09-4fce-86c2-c3bb97b010ff/config`;
    //     const toolsRes = await this.$axios.$get(toolsbasicUrl);
    //     const toolscommonRes = await this.$axios.$get(toolscommonUrl);
    //     const resourceRes = await this.$axios.$get(resourceUrl);
    //     console.log('业务类', toolscommonRes);
    //     this.dataList = resourceRes.data.resourceTree.allDataList;
    //     if (toolscommonRes.data.horizontalToolbars[0] === undefined) {
    //       this.businessTools = [];
    //     } else {
    //       this.businessTools = toolscommonRes.data.horizontalToolbars[0].buttons;
    //     }
    //     if (toolsRes.data.verticalToolbars[0] === undefined) {
    //       this.commonTools = [];
    //     } else {
    //       this.commonTools = toolsRes.data.verticalToolbars[0].buttons;
    //     }
    //     this.checkedLayerKeys = [7175, 2001];
    //     this.defaultExpandedKeys = [23];
    //   } else {
    //     this.dataListBranch = dirs.branch || [];
    //     this.businessTools = tools.home || [];
    //     this.commonTools = window.SMWEBGIS.tools.right || [];
    //     this.checkedLayerKeys = dirs.checkedLayerKeys || [];
    //     this.defaultExpandedKeys = dirs.defaultExpandedKeys || [];
    //   }
    //   this.initCheckLayerStatusBranch = true;
    //   this.contentRight = this.commonTools || [];
    //   this.content = this.businessTools;
    //   this.$emit("contentRight", this.contentRight);
    //   this.$emit("content", this.content);
    // },
    onViewerLoaded() {
      // 此处初始化飞行
      this.$store.commit("setViewerLoaded", true);
      const camera = window.SMWEBGIS.cameraCoordinate;
      const toolMethods = this.$mapActions;
      toolMethods.setView(camera);
    },
    onToggleLayerList() {
      const layerList = document.getElementsByClassName("layer-list")[0];
      if (layerList) {
        // layerList.style.right = this.visibleR ? '390px' : '100px'  map-type-box
        layerList.style.right = this.visibleR ? "390px" : "20px";
      }
      const bottomRight = document.getElementsByClassName("bottomRight")[0];
      if (bottomRight) {
        bottomRight.style.right = this.visibleR ? "380px" : "20px";
      }
    },
  },
};
</script>
<style scoped>
.GafMapBasicElement:hover {
  color: #0ffaff;
  cursor: pointer;
}
.map-content {
  z-index: 100;
}
.map-bottom {
  z-index: 100;
}
.map-bottom-branch {
      z-index: 100;
    position: relative;
    margin-right: 250px;
}
.tree {
  display: inline-block;
  width: 200px;
  height: 30px;
  background: azure;
  color: red;
  font-size: 18px;
}
.tool {
  display: inline-block;
  width: 400px;
  height: 30px;
  background: azure;
  color: blue;
  font-size: 18px;
  text-align: center;
}
.bottomRight {
  bottom: 90px;
}
.map-type-box {
  position: absolute;
  bottom: 22px;
  right: 52px;
}
/* 叠加分析 */
.search {
  margin-left: 15%;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 5px;
  color: rgba(0, 0, 0, 0.65);
  padding: 10px;
}
.ant-btn.ant-btn-loading:not(.ant-btn-circle):not(.ant-btn-circle-outline):not(.ant-btn-icon-only) {
  position: absolute;
  padding-left: auto;
  pointer-events: none;
}
.map-btn {
  z-index: 110;
  position: absolute;
  right: 5;
  top: 50;
  margin-top: 41%;
  margin-left: 96%;
}
.map-btn button {
  border: 1px solid transparent;
  background: #19283a4d;
  color: white;
  padding: 5px;
  cursor: pointer;
  border-radius: 6px;
}
</style>
