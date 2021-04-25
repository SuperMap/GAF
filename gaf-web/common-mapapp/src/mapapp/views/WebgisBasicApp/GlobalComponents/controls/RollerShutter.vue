<template>
  <div class="RollerShutter">
    <div id="vertical-slider" style="display: none;"></div>
    <div id="horizontal-slider" style="display: none;"></div>
    <div id="pannel">
      <div class="quit" @click="quit">
        退出
      </div>
      <div class="operatorPanel">
        <div class="operatorIcon">
          <i class="iconfont icon-tuceng"></i>
        </div>
        <div class="panel">
          <span>卷帘模式</span>
          <div class="mode">
            <a-radio-group v-if="type === 'vertical'" v-model="mode">
              <a-radio :value="1">
                屏蔽左侧
              </a-radio>
              <a-radio :value="2">
                屏蔽右侧
              </a-radio>
            </a-radio-group>
            <a-radio-group v-if="type === 'horizontal'" v-model="mode">
              <a-radio :value="3">
                屏蔽上侧
              </a-radio>
              <a-radio :value="4">
                屏蔽下侧
              </a-radio>
            </a-radio-group>
          </div>

          <hr />
          <div class="twoRoller">
            <span>参与卷帘层</span>
            <gaf-map-layer-tree
              :allCheckable="true"
              :leafnodeCheckable="false"
              :someNodeCheckable="false"
              :replaceFields="replaceFields"
              :data-list="rollerTwoData"
              :checkedLayerKeys="rollerTwoChecked"
              :check="onTwoChecked"
            />
          </div>
          <div class="oneRoller">
            <span> 基本对比层</span>
            <gaf-map-layer-tree
              :allCheckable="true"
              :leafnodeCheckable="false"
              :someNodeCheckable="false"
              :replaceFields="replaceFields"
              :data-list="rollerOneData"
              :checkedLayerKeys="rollerOneChecked"
              :check="onOneChecked"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
let camerca = {};
import GafMapBus from "~/utils/GafMapBus";
export default {
  name: "RollerShutter",
  data() {
    return {
      mode: this.type === "vertical" ? 1 : 3,
      replaceFields: {
        title: "resourceName",
        key: "resourceId",
        value: "resourceId"
      },
      rollerShutterConfig: {
        // 卷帘配置参数，以对象方式实现地址传递
        startX: 0.5,
        startY: 0.5
      },
      oneRollerCheckedKeys: this.rollerOneChecked || [],
      twoRollerCheckedKeys: this.rollerTwoChecked || [],
      oneData: [],
      twoData: []
    };
  },
  props: {
    type: {
      type: String,
      default: () => ""
    },
    one: {
      type: Object,
      default: () => {}
    },
    two: {
      type: Object,
      default: () => {}
    }
  },
  watch: {
    type: {
      immediate: false,
      handler: "handleParams"
    },
    oneData(val) {
      this.$bus.$emit("onCheckedLayerList", [...val, ...this.twoData]);
    },
    twoData(val) {
      this.$bus.$emit("onCheckedLayerList", [...this.oneData, ...val]);
    },
    twoRollerCheckedKeys(val, old) {
      const lost = old.filter(item => {
        return !val.includes(item);
      });
      if (lost && lost.length) {
        lost.forEach(item => {
          const sceneLayer = window.sceneViewer.sceneLayers[item] || {};
          const { layers, layer } = sceneLayer;
          if (layer) {
            layer.swipeEnabled = false;
          }
          if (layers) {
            layers.forEach(layer => {
              layer.swipeEnabled = false;
            });
          }
        });
      }
      const newKeys = val.filter(item => {
        return !old.includes(item);
      });
      if (newKeys.length) {
        this.setRollerShutterSplit(this.type);
      }
    },
    mode() {
      this.setRollerShutterSplit("123");
    }
  },
  beforeMount() {
    const self = this;
    this.$bus.$on("layer-loaded", () => {
      self.setRollerShutterSplit(self.type);
    });
    this.initLayerInfo();
  },
  computed: {
    rollerOneData() {
      return this.one.RollerLayersData;
    },
    rollerOneChecked() {
      return this.one.checkedLayerKeys;
    },
    rollerTwoData() {
      return this.two.RollerLayersData;
    },
    rollerTwoChecked() {
      return this.two.checkedLayerKeys;
    }
  },
  methods: {
    quit() {
      GafMapBus && GafMapBus.$emit("closePannel");
      this.$bus && this.$bus.$emit("closePannel");
    },
    handleParams() {
      const type = this.type;
      let verticalSlider = document.getElementById("vertical-slider"); // 垂直分割条
      let horizontalSlider = document.getElementById("horizontal-slider"); // 水平分割条
      if (type === "vertical") {
        this.$bus && this.$bus.$emit("close-overview");
        verticalSlider.style.display = "block";
        horizontalSlider.style.display = "none";
      } else if (type === "horizontal") {
        this.$bus && this.$bus.$emit("close-overview");
        horizontalSlider.style.display = "block";
        verticalSlider.style.display = "none";
      } else {
        horizontalSlider.style.display = "none";
        verticalSlider.style.display = "none";
      }
      this.initRollerShutter(type);
    },
    initRollerShutter(type) {
      this.setRollerShutterSplit(type);
      this.bindSliderEvt(type);
    },
    /**
     * 注册卷帘分割条的拖拽事件。
     * @param viewer。
     * @param rollerShutterConfig 卷帘配置参数。
     */
    bindSliderEvt(type) {
      const rollerShutterConfig = this.rollerShutterConfig;
      const self = this;
      let verticalSlider = document.getElementById("vertical-slider"); // 垂直分割条
      let horizontalSlider = document.getElementById("horizontal-slider"); // 水平分割条
      verticalSlider.addEventListener("mousedown", mouseDown, false);
      horizontalSlider.addEventListener("mousedown", mouseDown, false);
      let windowHeight = document.body.clientHeight;
      document.addEventListener("mouseup", mouseUp, false);
      function mouseUp() {
        document.removeEventListener("mousemove", sliderMove, false);
      }
      function mouseDown() {
        document.addEventListener("mousemove", sliderMove, false);
      }
      function sliderMove(e) {
        // 鼠标拖拽时执行
        // 解决拖拽鼠标粘滞的问题
        if (e.preventDefault) {
          e.preventDefault();
        } else {
          e.returnValue = false;
        }
        if (type === "vertical") {
          verticalSlider.style.left = e.clientX + "px";
          rollerShutterConfig.startX = e.clientX / document.body.clientWidth;
        } else if (type === "horizontal") {
          let clientY = e.clientY;
          if (clientY < 0) {
            clientY = 0;
          } else if (clientY > windowHeight) {
            clientY = windowHeight - verticalSlider.clientHeight;
          }
          horizontalSlider.style.top = clientY + "px";
          rollerShutterConfig.startY = clientY / document.body.clientHeight;
        }
        this.rollerShutterConfig = rollerShutterConfig;
        self.setRollerShutterSplit(type);
      }
    },
    /**
     * 初始化卷帘。设置分割条初始位置及绑定相关事件。
     * @param scene 场景。
     * @param rollerShutterConfig 卷帘配置参数。
     */
    setRollerShutterSplit() {
      const rollerShutterConfig = this.rollerShutterConfig;
      const { startX, startY } = rollerShutterConfig;
      let scratchSwipeRegion = this.scratchSwipeRegion;
      // 左右卷帘使用left slider滑动，上下卷帘使用top slider滑动
      // if (type === "vertical") {
      //   Cesium.BoundingRectangle.unpack(
      //     [0, 0, startX, 1],
      //     0,
      //     scratchSwipeRegion
      //   );
      // } else if (type === "horizontal") {
      //   Cesium.BoundingRectangle.unpack(
      //     [0, startY, 1, 1],
      //     0,
      //     scratchSwipeRegion
      //   );
      // }
      switch (this.mode) {
        case 1:
          Cesium.BoundingRectangle.unpack(
            [startX, 0, 1, 1],
            0,
            scratchSwipeRegion
          );
          break;
        case 2:
          Cesium.BoundingRectangle.unpack(
            [0, 0, startX, 1],
            0,
            scratchSwipeRegion
          );
          break;
        case 3:
          Cesium.BoundingRectangle.unpack(
            [0, startY, 1, 1],
            0,
            scratchSwipeRegion
          );
          break;
        case 4:
          Cesium.BoundingRectangle.unpack(
            [0, 0, 1, startY],
            0,
            scratchSwipeRegion
          );
          break;
        default:
          Cesium.BoundingRectangle.unpack([0, 0, 1, 1], 0, scratchSwipeRegion);
          break;
      }
      const allSceneLayers = window.sceneViewer.sceneLayers;
      const allLayers = this.twoRollerCheckedKeys.map(item => {
        return allSceneLayers[item];
      });
      const sceneLayers = allLayers.filter(item => {
        return item && item.visible;
      });
      sceneLayers.forEach(sceneLayer => {
        const { layers, layer } = sceneLayer;
        if (!(layer === undefined)) {
          if (layer) {
            layer.swipeEnabled = true;
            layer.swipeRegion = scratchSwipeRegion;
          } else {
            layer.swipeEnabled = false;
          }
        }
        if (layers) {
          layers.forEach(layer => {
            if (layer) {
              layer.swipeEnabled = true;
              layer.swipeRegion = scratchSwipeRegion;
            } else {
              layer.swipeEnabled = false;
            }
          });
        }
      });
    },
    updateLayerInfo() {
      const checkedList = this.defaultCheckValue;
      const sceneLayers = window.viewer.scene.layers.layerQueue || [];
      const imgLayers = window.scene.imageryLayers._layers || [];
      const layerList = sceneLayers.map(layer => {
        // 添加图层列表 供选择参与卷帘的图层
        const item = { name: layer.name };
        if (checkedList.includes(layer.name)) {
          item.checked = true;
        } else {
          item.checked = false;
        }
        return item;
      });
      const imageryLayerList = imgLayers.map((item, index) => {
        const imgInfo = {};
        imgInfo.id = index;
        const imageryProvider = item.imageryProvider;
        if (imageryProvider && imageryProvider.url) {
          const url = imageryProvider.url;
          imgInfo.url = url;
          if (imageryProvider.name) {
            imgInfo.name = imageryProvider.name;
          } else {
            imgInfo.name = url.substring(url.lastIndexOf("/") + 1);
          }
        }
        if (checkedList.includes(imgInfo.name)) {
          imgInfo.checked = true;
        } else {
          imgInfo.checked = false;
        }
        return imgInfo;
      });
      this.layerList = layerList;
      this.imageryLayerList = imageryLayerList;
    },
    initLayerInfo() {
      const oneData = this.getCheckedLayerData(
        this.rollerOneData,
        this.rollerOneChecked
      );
      const twoData = this.getCheckedLayerData(
        this.rollerTwoData,
        this.rollerTwoChecked
      );
      this.$bus.$emit("onCheckedLayerList", [...oneData, ...twoData]);
      this.oneData = oneData;
      this.twoData = twoData;
    },
    getCheckedLayerData(layerData, checkedKeys) {
      return checkedKeys.map(item => {
        return layerData.find(layer => {
          return layer.resourceId === item;
        });
      });
    },
    onOneChecked(checkedKeys) {
      this.oneRollerCheckedKeys = checkedKeys;
      const data = this.getCheckedLayerData(this.rollerOneData, checkedKeys);
      this.oneData = data;
    },
    onTwoChecked(checkedKeys) {
      const data = this.getCheckedLayerData(this.rollerTwoData, checkedKeys);
      this.twoData = data;
      this.twoRollerCheckedKeys = checkedKeys;
    },
    getAddedLayer() {
      const addedLayers = [];
      const sceneLayers = window.sceneViewer.sceneLayers;
      Object.values(sceneLayers).forEach(item => {
        const { layers, layer, visible } = item;
        if (visible) {
          if (layers && layers.length && layers[0] && layers[0].layerInfo) {
            addedLayers.push(layers[0].layerInfo);
          }
          if (layer && layer.layerInfo) {
            addedLayers.push(layer.layerInfo);
          }
        }
      });
      this.addedLayers = addedLayers;
    }
  },
  mounted() {
    this.twoRollerCheckedKeys = this.rollerTwoChecked;
    this.oneRollerCheckedKeys = this.rollerOneChecked;
    const _camerca = window.viewer.camera;
    camerca = {
      position: { ..._camerca.position },
      _direction: { ..._camerca._direction },
      up: { ..._camerca.up },
      heading: { ..._camerca.heading },
      pitch: { ..._camerca.pitch },
      roll: { ..._camerca.roll }
    };
    document.getElementById("cesiumContainer").style.zIndex = 101;
    this.getAddedLayer();
    const self = this;
    this.initLayerInfo();
    this.scratchSwipeRegion = new Cesium.BoundingRectangle();
    this.$nextTick(() => {
      self.handleParams();
    });
  },
  beforeDestroy() {
    window.viewer.scene.camera.setView({
      destination: camerca.position,
      orientation: {
        direction: camerca._direction,
        up: camerca.up,
        heading: camerca.heading,
        pitch: camerca.pitch,
        roll: camerca.roll
      }
    });
    this.twoRollerCheckedKeys.forEach(item => {
      const sceneLayer = window.sceneViewer.sceneLayers[item] || {};
      const { layers, layer } = sceneLayer;
      if (layer) {
        layer.swipeEnabled = false;
      }
      if (layers) {
        layers.forEach(layer => {
          layer.swipeEnabled = false;
        });
      }
    });
    document.getElementById("cesiumContainer").style.zIndex = 0;
    this.$bus.$emit("LayerTreeShow", [...window.checkedLayerKeys]);
  }
};
</script>
<style lang="less">
#draggable {
  z-index: 100;
}
.RollerShutter {
  width: 100%;
  height: 100%;
  #draggable {
    z-index: 2001;
  }
  #vertical-slider {
    position: absolute;
    left: 50%;
    top: 0px;
    background-color: #d3d3d3;
    width: 5px;
    height: 100%;
    z-index: 200;
    margin-top: 60px;
    cursor: w-resize;
  }
  #horizontal-slider {
    position: absolute;
    left: 0;
    top: 50%;
    background-color: #d3d3d3;
    width: 100%;
    height: 5px;
    z-index: 200;
    cursor: s-resize;
  }
  .radioStyle {
    display: block;
    height: 30px;
    line-height: 30px;
    color: var(--font-color, #fff);
    margin: 0px;
  }
  .ant-radio-wrapper {
    color: var(--font-color, #fff);
  }
  #pannel {
    display: flex;
    flex-direction: row;
    align-items: flex-start;
    position: absolute;
    top: 70px;
    left: 10px;
    .quit {
      z-index: 9999;
      margin: 4px;
      padding: 0 10px;
      background: var(--bg-color, rgba(25, 40, 58, 0.3));
      border: 1px solid rgba(255, 255, 255, 0.4);
      border-radius: 6px;
      text-align: center;
      line-height: 40px;
      color: var(--font-color, #fff);
      cursor: pointer;
    }
  }
  .operatorPanel {
    z-index: 999;
    .operatorIcon {
      width: 42px;
      margin: 4px;
      height: 42px;
      background: var(--bg-color, rgba(25, 40, 58, 0.3));
      border: 1px solid rgba(255, 255, 255, 0.4);
      border-radius: 6px;
      text-align: center;
      line-height: 40px;
      color: var(--font-color, #fff);
    }
    .panel {
      hr {
        margin: 10px 0;
      }
      .mode {
        margin-top: 10px;
      }
      background-color: var(--bg-color, rgba(25, 40, 58, 0.3));
      opacity: 0.8;
      color: var(--font-color, #fff);
      padding: 15px;
      border-radius: 6px;
      display: none;
      width: 300px;
      min-height: 300px;
    }
  }
  .operatorPanel:hover {
    .operatorIcon {
      display: none;
    }
    .panel {
      display: block;
      position: absolute;
    }
  }
}
</style>
