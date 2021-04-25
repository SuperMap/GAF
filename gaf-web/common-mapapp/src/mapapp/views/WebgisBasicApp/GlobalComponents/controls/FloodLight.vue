<template>
  <div class="lightPaneContanier">
    <gaf-map-draggable
      :visible="visible"
      :width="360"
      title="泛光"
      panel-func="tools"
      @cancel="handleCloseButton"
    >
      <div class="param-setting">
        <div class="param-item M1-btns">
          <span class="fl" @click="directionClick">{{ scanTitle }}</span>
          <span class="fl" style="margin-left: 25px" @click="remove">移除</span>
        </div>
        <div class="param-item">
          <label>扫描模式:</label>
          <a-radio-group v-model="value" @change="scanModeChange">
            <label for="line-scan-mode">环状扫描</label>
            <a-radio id="ring-scan-mode" :value="'circleScanChange'"></a-radio>
          </a-radio-group>
        </div>
        <div class="param-item">
          <label>扫描范围</label>
          <a-input
            v-model="scanArea"
            placeholder="请输入扫描范围(单位:米)"
            style="color: #000; width: 195px"
          />
        </div>
        <div class="param-item">
          <a-row>
            <a-col :span="6">
              <label style="margin-top: 8px">扫描速率</label>
            </a-col>
            <a-col :span="12">
              <a-slider
                v-model="scanPeriod"
                :min="0"
                :max="5"
                :default-value="1"
                :step="0.1"
                style="width: 170px"
                @change="scanPeriodChange"
              />
            </a-col>
          </a-row>
        </div>
        <div class="param-item">
          <label>扫描颜色</label>
          <input v-model="color" type="color" @change="handlerChangeColor" />
        </div>
        <div v-if="scanType" id="circleScanTexture" class="param-item">
          <label>扫描纹理</label>
          <a-select
            :value="circleScanTexture"
            style="width: 200px; height: 30px"
            @change="circleScanChange"
          >
            <a-select-option
              v-for="(item, index) in circleScanTextures"
              :key="index"
              :value="item.value"
              >{{ item.label }}</a-select-option
            >
          </a-select>
        </div>
        <div v-else id="lineScanTexture" class="param-item">
          <label>扫描纹理</label>
          <a-select
            :value="lineScanTexture"
            style="width: 170px; height: 30px"
            default-value="none-texture"
            @change="lineScanChange"
          >
            <a-select-option
              v-for="(item, index) in lineScanTextures"
              :key="index"
              :value="item.value"
              >{{ item.label }}</a-select-option
            >
          </a-select>
        </div>

        <a-divider />
        <div class="param-item M1-btns">
          <span class="fl" @click="setPointLight">设置点光源</span>
          <span class="fl" style="margin-left: 25px" @click="removePointLight"
            >移除点光源</span
          >
        </div>
        <div class="param-item">
          <label>设置颜色</label>
          <input
            v-model="pointLightColor"
            type="color"
            style="margin-top: 10px"
            @change="pointLightChangeColor"
          />
        </div>
        <div class="param-item">
          <a-row>
            <a-col :span="6">
              <label style="margin-top: 8px">扩散距离</label>
            </a-col>
            <a-col :span="12">
              <a-slider
                v-model="pDistance"
                :min="0"
                :max="2000"
                :default-value="1000"
                :step="10"
                style="width: 170px"
                @change="pDistanceChange"
              />
            </a-col>
          </a-row>
        </div>
        <div class="param-item">
          <a-row>
            <a-col :span="6">
              <label style="margin-top: 8px">衰减因子</label>
            </a-col>
            <a-col :span="12">
              <a-slider
                v-model="pDacay"
                :min="0"
                :max="100"
                :default-value="1"
                :step="0.1"
                style="width: 170px"
                @change="pDacayChange"
              />
            </a-col>
          </a-row>
        </div>
        <div class="param-item">
          <a-row>
            <a-col :span="6">
              <label style="margin-top: 8px">光源强度</label>
            </a-col>
            <a-col :span="12">
              <a-slider
                v-model="pIntensity"
                :min="0"
                :max="20"
                :default-value="3"
                :step="0.1"
                style="width: 170px"
                @change="pIntensityChange"
              />
            </a-col>
          </a-row>
        </div>
      </div>
    </gaf-map-draggable>
  </div>
</template>
<script>
import colorRgba from './js/ColorRgbaUtil.js'
const profileInfo = {
  handler: null,
  pointEntities: [],
  pointLights: [],
  baseAmbientLightColor: null,
}
export default {
  name: 'FloodLight',
  components: {},
  data() {
    return {
      visible: true,
      color: '#2263e6',
      pointLightColor: '#2263e6',
      value: 'circleScanChange',
      scanType: true,
      scanTitle: '自定义扫描方向',
      circleScanTexture: 'none-texture',
      scanArea: null,
      circleScanTextures: [
        { label: '无', value: 'none-texture' },
        {
          label: '圆形纹理1',
          value: './img/floodLight/huanzhuang1.jpg',
        },
        {
          label: '圆形纹理2',
          value: './img/floodLight/huanzhuang2.jpg',
        },
        {
          label: '六边形纹理3',
          value: './img/floodLight/ring-2.jpg',
        },
      ],
      lineScanTexture: 'none-texture',
      lineScanTextures: [
        { label: '无', value: 'none-texture' },
        {
          label: '线状纹理1',
          value: './img/floodLight/线状扫描纹理1.jpg',
        },
        {
          label: '线状纹理2',
          value: './img/floodLight/线状扫描纹理2.jpg',
        },
      ],
      circleScanValue: 'none-texture',
      lineScanValue: 'none-texture',
      scanSpeedTotal: 1000,
      scanPeriod: 1,
      scanSpeed: 1000,
      bloomThreshold: 0.5,
      bloomIntensity: 1,
      pDistance: 1000,
      pDacay: 1,
      pIntensity: 3,
      tooltip: null,
      tooltipMsg: '',
      clickType: '',
      mouseClickCount: 0,
      handlerActive: false,
      removeFlag: false,
    }
  },
  watch: {
    scanArea: {
      handler(newName) {
        this.scanSpeedTotal = newName
        this.setSpeed()
      },
    },
  },
  mounted() {
    this.init()
    window.floodLightClear = this.clear
  },
  beforeDestroy() {
    this.clear()
    profileInfo.handler.destroy()
  },
  methods: {
    init() {
      const viewer = window.viewer
      const scene = viewer.scene
      profileInfo.baseAmbientLightColor = scene.lightSource._ambientLightColor
      if (scene == null) return false
      const layers = scene.layers._layerQueue
      this.tooltip = window.createTooltip(document.body)
      const s3mLayer = layers[0] // 获取模型图层
      if (!s3mLayer) return false
      scene.scanEffect.mode = window.Cesium.ScanEffectMode.CIRCLE
      const colorObj = colorRgba(this.pointLightColor)
      scene.scanEffect.color = new window.Cesium.Color(
        colorObj.r / 255,
        colorObj.g / 255,
        colorObj.b / 255,
        colorObj.a
      )
      if (scene.scanEffect.count > 0) this.removeFlag = true
      this.handlerOption()
    },
    handlerOption() {
      const viewer = window.viewer
      const scene = viewer.scene
      const _this = this
      this.handlerActive = false // true表示正在进行扫描点选设置
      const handler = new window.Cesium.ScreenSpaceEventHandler(scene.canvas)
      profileInfo.handler = handler
      // 鼠标左击事件
      handler.setInputAction((e) => {
        if (!_this.handlerActive) {
          return
        }
        _this.$mapActions.releaseSelection()
        const position = e.position // 获取鼠标屏幕坐标
        const centerPosition = scene.pickPosition(position)
        if (_this.clickType === 'scan') {
          // 代表是扫描
          _this.mouseClickCount++
          const scanMode = _this.value
          if (!viewer.scene.scanEffect.show) {
            _this.showScanEffect()
          }
          const count = viewer.scene.scanEffect.count
          // 获取当前扫描模式
          if (scanMode === 'lineScanChange') {
            if (_this.mouseClickCount % 2 === 1) {
              viewer.scene.scanEffect.add(centerPosition)
              // viewer.scene.scanEffect.centerPostion = centerPosition; // 设置扫描中心点
              _this.tooltipMsg = '点选设置扫描方向'
            } else {
              const dir = new window.Cesium.Cartesian3()
              window.Cesium.Cartesian3.subtract(
                centerPosition,
                viewer.scene.scanEffect.centerPostion,
                dir
              ) // 获取扫描方向向量
              viewer.scene.scanEffect.lineMoveDirection = dir
              _this.mouseClickCount = 0
              _this.handlerActive = false
              _this.tooltip.setVisible(false)
            }
          } else {
            if (count === 0 || (count === 1 && _this.removeFlag)) {
              _this.removeFlag = false
              viewer.scene.scanEffect.centerPostion = centerPosition
            } else {
              viewer.scene.scanEffect.add(centerPosition)
            }
            _this.mouseClickCount = 0
            _this.handlerActive = false
            _this.tooltip.setVisible(false)
          }
        } else {
          // 代表设置点光源
          scene.lightSource._ambientLightColor = new window.Cesium.Color(
            0.4,
            0.4,
            0.4,
            0.4
          )
          const posDeg = window.Cesium.Cartographic.fromCartesian(
            centerPosition
          )
          const pointPosition = window.Cesium.Cartesian3.fromRadians(
            posDeg.longitude,
            posDeg.latitude,
            posDeg.height
          )
          const pointEntity = viewer.entities.add(
            new window.Cesium.Entity({
              point: new window.Cesium.PointGraphics({
                color: new window.Cesium.Color(1, 1, 1),
                pixelSize: 10,
                outlineColor: new window.Cesium.Color(1, 1, 1),
              }),
              position: pointPosition,
            })
          )
          profileInfo.pointEntities.push(pointEntity)
          const lightColorObj = colorRgba(_this.pointLightColor)
          const options = {
            color: new window.Cesium.Color(
              lightColorObj.r / 255,
              lightColorObj.g / 255,
              lightColorObj.b / 255,
              lightColorObj.a
            ),
            cutoffDistance: _this.pDistance,
            decay: _this.pDacay,
            intensity: _this.pIntensity,
          }
          const pointLight = new window.Cesium.PointLight(
            centerPosition,
            options
          )
          scene.addLightSource(pointLight)
          profileInfo.pointLights.push(pointLight)
          _this.handlerActive = false
          _this.tooltip.setVisible(false)
        }
      }, window.Cesium.ScreenSpaceEventType.LEFT_CLICK)
      // 鼠标移动事件
      handler.setInputAction(function (e) {
        if (!_this.handlerActive) {
          return
        }
        _this.tooltip.showAt(e.endPosition, _this.tooltipMsg)
      }, window.Cesium.ScreenSpaceEventType.MOUSE_MOVE)
    },
    handlerChangeColor() {
      const viewer = window.viewer
      const scene = viewer.scene
      const colorObj = colorRgba(this.color)
      scene.scanEffect.color = new window.Cesium.Color(
        colorObj.r / 255,
        colorObj.g / 255,
        colorObj.b / 255,
        colorObj.a
      )
    },
    scanPeriodChange(_value) {
      // 调节扫描速率
      this.scanPeriod = _value
      this.setSpeed()
    },
    setSpeed() {
      const scene = window.viewer.scene
      this.scanSpeed = (this.scanSpeedTotal / this.scanPeriod).toFixed(1)
      scene.scanEffect.period = this.scanPeriod
      scene.scanEffect.speed = this.scanSpeed
    },
    // bloomThresholdChange(_value) {
    //   // 调节泛光阈值
    //   const scene = window.viewer.scene
    //   scene.bloomEffect.threshold = _value
    // },
    // bloomIntensityChange(_value) {
    //   // 调节泛光强度
    //   const scene = window.viewer.scene
    //   scene.bloomEffect.bloomIntensity = _value
    // },
    pDistanceChange(value) {
      // 调节扩散距离
      this.setLinePointEffect('cutoffDistance', value)
    },
    pDacayChange(value) {
      // 调节衰减因子
      this.setLinePointEffect('decay', value)
    },
    pIntensityChange(value) {
      // 调节光源强度
      this.setLinePointEffect('intensity', value)
    },
    pointLightChangeColor() {
      // 调节光源颜色
      if (profileInfo.pointLights.length > 0) {
        const colorObj = colorRgba(this.pointLightColor)
        profileInfo.pointLights.forEach((item) => {
          item.color = new window.Cesium.Color(
            colorObj.r / 255,
            colorObj.g / 255,
            colorObj.b / 255,
            colorObj.a
          )
        })
      }
    },
    setLinePointEffect(effect, value) {
      if (profileInfo.pointLights.length > 0) {
        profileInfo.pointLights.forEach((item) => {
          item[effect] = value
        })
      }
    },
    scanModeChange(e) {
      const scene = window.viewer.scene
      if (e.target.value === 'lineScanChange') {
        this.scanType = false
        this.scanTitle = '自定义扫描方向'
        scene.scanEffect.mode = window.Cesium.ScanEffectMode.LINE
        this.lineScanChange(this.lineScanValue)
      } else if (e.target.value === 'circleScanChange') {
        this.scanType = true
        this.scanTitle = '自定义扫描中心'
        scene.scanEffect.mode = window.Cesium.ScanEffectMode.CIRCLE
        this.circleScanChange(this.circleScanValue)
      }
    },
    scanAreaChange(value) {
      this.scanArea = value
    },
    circleScanChange(_value) {
      this.circleScanTexture = _value
      const scene = window.viewer.scene
      this.circleScanValue = _value
      if (_value === 'none-texture') {
        scene.scanEffect.textureUrl = ''
        return
      }
      scene.scanEffect.textureUrl = _value
    },
    lineScanChange(_value) {
      this.lineScanTexture = _value
      const scene = window.viewer.scene
      this.lineScanValue = _value
      if (_value === 'none-texture') {
        scene.scanEffect.textureUrl = ''
        return
      }
      scene.scanEffect.textureUrl = _value
    },
    handleCloseButton() {
      this.visible = false
      this.$emit('draggableClose')
    },
    clear() {
      if (!window.viewer) return
      const scene = window.viewer.scene
      scene.scanEffect.show = false
      scene.scanEffect.textureUrl = ''
      this.circleScanTexture = 'none-texture'
      this.lineScanTexture = 'none-texture'
      if (profileInfo.handler) {
        profileInfo.handler.removeInputAction(
          window.Cesium.ScreenSpaceEventType.RIGHT_CLICK
        )
        profileInfo.handler.removeInputAction(
          window.Cesium.ScreenSpaceEventType.MOUSE_MOVE
        )
      }
      this.remove()
      this.removePointLight()
    },
    showScanEffect() {
      const scene = window.viewer.scene
      // 设置环境光
      scene.lightSource._ambientLightColor = new window.Cesium.Color(
        0.4,
        0.4,
        0.4,
        0.4
      )
      scene.scanEffect.period = this.scanPeriod
      scene.scanEffect.speed = this.scanSpeed
      scene.scanEffect.show = true
    },
    directionClick() {
      this.handlerActive = true
      this.tooltip.setVisible(true)
      this.tooltipMsg = '点选设置扫描中心'
      this.clickType = 'scan'
    },
    setPointLight() {
      this.handlerActive = true
      this.tooltip.setVisible(true)
      this.tooltipMsg = '点选设置点光源'
      this.clickType = 'lightPoint'
    },
    remove() {
      const scene = window.viewer.scene
      if (profileInfo.pointLights.length === 0) {
        scene.lightSource._ambientLightColor = profileInfo.baseAmbientLightColor
      }
      scene.scanEffect.show = false
      let index = scene.scanEffect.count - 1
      for (index; index >= 0; index--) {
        scene.scanEffect.remove(index)
      }
      this.removeFlag = true
    },
    removePointLight() {
      const viewer = window.viewer
      if (profileInfo.pointLights.length > 0) {
        if (!viewer.scene.scanEffect.show) {
          viewer.scene.lightSource._ambientLightColor =
            profileInfo.baseAmbientLightColor
        }
        for (let i = 0; i < profileInfo.pointLights.length; i++) {
          viewer.entities.remove(profileInfo.pointEntities[i])
          viewer.scene.removeLightSource(profileInfo.pointLights[i])
        }
        profileInfo.pointEntities = []
        profileInfo.pointLights = []
      }
    },
  },
}
</script>
<style lang="less" scope>
.lightPaneContanier {
  color: #fff;
  .draggable {
    .header {
      // background: rgba(38, 38, 38, 0.75);
      color: #fff;
    }
    z-index: 99;
  }
  .param-setting {
    overflow: auto;
    max-width: 340px;
    padding-top: 10px;
    .param-item {
      margin-bottom: 8px;
      label,
      input {
        color: #fff !important;
        margin-right: 5px;
      }
      input.ant-input {
        background: transparent;
        border: 1px solid rgba(255, 255, 255, 0.8);
      }
      .ant-select-selection {
        border: 1px solid rgba(255, 255, 255, 0.8);
      }
      .button {
        line-height: 30px;
        text-align: center;
        font-weight: bold;
        text-shadow: 1px 1px 1px #333;
        border-radius: 3px;
        margin: 0 8px 8px 0;
        position: relative;
        overflow: hidden;
        padding: 0 15px 0 15px;
        font-size: 0.8rem;
      }
      .black {
        border: 1px solid #333;
        box-shadow: 0 1px 2px #8b8b8b inset, 0 -1px 0 #3d3d3d inset,
          0 -2px 3px #8b8b8b inset;
        background: rgba(38, 38, 38, 0.75);
      }

      .ant-divider-horizontal {
        margin: 10px 0;
      }
    }
  }
  .M1-btns {
    height: 28px;
    span {
      display: block;
      height: 28px;
      line-height: 28px;
      background: #1fb17a;
      color: #fff;
      font-size: 13px;
      width: 120px;
      text-align: center;
      cursor: pointer;
    }
    span.fl {
      float: left;
    }
  }
}
</style>
