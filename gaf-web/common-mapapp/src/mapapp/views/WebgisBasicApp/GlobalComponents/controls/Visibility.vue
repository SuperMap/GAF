<template>
  <div class="visibilityContanier">
    <gaf-map-draggable
      :visible="visible"
      :width="360"
      title="可视域分析"
      panel-func="tools"
      @cancel="handleCloseButton"
    >
      <div class="M1-item plan">
        <dl>
          <dt><i class="iconfont icon-bangzhu"></i>请先在地图上绘制可视区域</dt>
        </dl>
      </div>
      <div class="M4-item plan">
        <a-row>
          <a-col :span="18">
            <label>方向(度)：</label>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="18">
            <a-slider
              v-model="directionValue"
              :tip-formatter="null"
              :min="0"
              :max="360"
              :step="1.0"
            />
          </a-col>
          <a-col :span="6">
            <a-input-number v-model="directionValue" :min="1" :max="20" />
          </a-col>
        </a-row>
      </div>
      <div class="M4-item plan">
        <a-row>
          <a-col :span="18">
            <label>翻转(度)：</label>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="18">
            <a-slider
              v-model="pitchValue"
              :tip-formatter="null"
              :min="-90"
              :max="90"
              :step="1.0"
            />
          </a-col>
          <a-col :span="6">
            <a-input-number v-model="pitchValue" :min="-90" :max="90" />
          </a-col>
        </a-row>
      </div>
      <div class="M4-item plan">
        <a-row>
          <a-col :span="18">
            <label>距离(米)：</label>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="18">
            <a-slider
              v-model="distanceValue"
              :tip-formatter="null"
              :min="1"
              :max="2000"
              :step="1.0"
            />
          </a-col>
          <a-col :span="6">
            <a-input-number v-model="distanceValue" :min="1" :max="2000" />
          </a-col>
        </a-row>
      </div>
      <div class="M4-item plan">
        <a-row>
          <a-col :span="18">
            <label>水平视场角(度)：</label>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="18">
            <a-slider
              v-model="horizontalFovValue"
              :tip-formatter="null"
              :min="1"
              :max="120"
              :step="1.0"
            />
          </a-col>
          <a-col :span="6">
            <a-input-number v-model="horizontalFovValue" :min="1" :max="120" />
          </a-col>
        </a-row>
      </div>
      <div class="M4-item plan">
        <a-row>
          <a-col :span="18">
            <label>垂直视场角(度)：</label>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="18">
            <a-slider
              v-model="verticalFovValue"
              :tip-formatter="null"
              :min="1"
              :max="90"
              :step="1.0"
            />
          </a-col>
          <a-col :span="6">
            <a-input-number v-model="verticalFovValue" :min="1" :max="90" />
          </a-col>
        </a-row>
      </div>
      <div class="M4-item plan color">
        <dl>
          <dt>可见区域颜色：</dt>
          <dd>
            <span class="color">
              <input
                v-model="visibleColor"
                type="color"
                @change="visibleColorChange"
              />
            </span>
          </dd>
        </dl>
      </div>
      <div class="M4-item plan color">
        <dl>
          <dt>不可见区域颜色：</dt>
          <dd>
            <span class="color">
              <input
                v-model="invisibleColor"
                type="color"
                @change="invisibleColorChange"
              />
            </span>
          </dd>
        </dl>
      </div>
      <div class="M4-item plan">
        <dl class="last">
          <dt>本例中观察者附加高度：1.8米</dt>
        </dl>
      </div>
      <div class="analyse-btn">
        <span class="delete">
          <a href="javascript:;" @click="clearVisibility">清除</a>
        </span>
        <span class="save">
          <a href="javascript:;" @click="drawVisibility">绘制</a>
        </span>
      </div>
    </gaf-map-draggable>
  </div>
</template>
<script>
const profileInfo = {
  visibilityShed3D: null,
  pointHandler: null,
  handler: null,
  visibilityPosition: null,
  drawFunction: null,
}
export default {
  name: 'Visibility',
  data() {
    return {
      visible: true,
      directionValue: 1.0,
      pitchValue: 1.0,
      distanceValue: 1.0,
      horizontalFovValue: 1.0,
      verticalFovValue: 1.0,
      visibleColor: '#00ff00',
      invisibleColor: '#ff0000',
    }
  },
  watch: {
    directionValue(newValue) {
      if (profileInfo.visibilityShed3D) {
        profileInfo.visibilityShed3D.direction = parseFloat(newValue)
      }
    },
    pitchValue(newValue) {
      if (profileInfo.visibilityShed3D) {
        profileInfo.visibilityShed3D.pitch = parseFloat(newValue)
      }
    },
    distanceValue(newValue) {
      if (profileInfo.visibilityShed3D) {
        profileInfo.visibilityShed3D.distance = parseFloat(newValue)
      }
    },
    horizontalFovValue(newValue) {
      if (profileInfo.visibilityShed3D) {
        profileInfo.visibilityShed3D.horizontalFov = parseFloat(newValue)
      }
    },
    verticalFovValue(newValue) {
      if (profileInfo.visibilityShed3D) {
        profileInfo.visibilityShed3D.verticalFov = parseFloat(newValue)
      }
    },
  },
  destroyed() {
    this.clear()
    profileInfo.handler.destroy()
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      const viewer = window.viewer
      const scene = window.scene
      const self = this
      scene.viewFlag = true
      const pointHandler = new window.Cesium.DrawHandler(
        viewer,
        window.Cesium.DrawMode.Point
      )
      profileInfo.pointHandler = pointHandler
      const visibilityShed3D = new window.Cesium.ViewShed3D(scene)
      profileInfo.visibilityShed3D = visibilityShed3D
      const handler = new window.Cesium.ScreenSpaceEventHandler(scene.canvas)
      profileInfo.handler = handler
      const drawFunction = function (result) {
        const point = result.object
        const position = point.position
        profileInfo.visibilityPosition = position

        // 将获取的点的位置转化成经纬度
        const cartographic = window.Cesium.Cartographic.fromCartesian(position)
        const longitude = window.Cesium.Math.toDegrees(cartographic.longitude)
        const latitude = window.Cesium.Math.toDegrees(cartographic.latitude)
        const height = cartographic.height + 1.8
        point.position = window.Cesium.Cartesian3.fromDegrees(
          longitude,
          latitude,
          height
        )

        if (scene.viewFlag) {
          // 设置视口位置
          visibilityShed3D.viewPosition = [longitude, latitude, height]
          visibilityShed3D.build()
          // 将标记置为false以激活鼠标移动回调里面的设置可视域操作
          scene.viewFlag = false
        }
      }
      profileInfo.drawFunction = drawFunction
      pointHandler.drawEvt.addEventListener(drawFunction)
      // 鼠标移动事件回调
      handler.setInputAction(function (e) {
        // 若此标记为false，则激活对可视域分析对象的操作
        if (!scene.viewFlag) {
          const position = e.endPosition
          const last = scene.pickPosition(position)
          // 计算该点与视口位置点坐标的距离
          const distance = window.Cesium.Cartesian3.distance(
            profileInfo.visibilityPosition,
            last
          )

          if (distance > 0) {
            // 将鼠标当前点坐标转化成经纬度
            const cartographic = window.Cesium.Cartographic.fromCartesian(last)
            const longitude = window.Cesium.Math.toDegrees(
              cartographic.longitude
            )
            const latitude = window.Cesium.Math.toDegrees(cartographic.latitude)
            const height = cartographic.height
            // 通过该点设置可视域分析对象的距离及方向
            visibilityShed3D.setDistDirByPoint([longitude, latitude, height])
          }
        }
      }, window.Cesium.ScreenSpaceEventType.MOUSE_MOVE)
      handler.setInputAction(function () {
        // 鼠标右键事件回调，不再执行鼠标移动事件中对可视域的操作
        scene.viewFlag = true
        self.directionValue = Number(visibilityShed3D.direction.toFixed(1))
        self.pitchValue = Number(visibilityShed3D.pitch.toFixed(1))
        self.distanceValue = Number(visibilityShed3D.distance.toFixed(1))
        self.horizontalFovValue = Number(
          visibilityShed3D.horizontalFov.toFixed(1)
        )
        self.verticalFovValue = Number(visibilityShed3D.verticalFov.toFixed(1))
      }, window.Cesium.ScreenSpaceEventType.RIGHT_CLICK)
    },
    handleCloseButton() {
      this.visible = false
      this.$emit('draggableClose')
    },
    visibleColorChange() {
      if (profileInfo.visibilityShed3D) {
        profileInfo.visibilityShed3D.visibleAreaColor = window.Cesium.Color.fromCssColorString(
          this.visibleColor
        )
      }
    },
    invisibleColorChange() {
      if (profileInfo.visibilityShed3D) {
        profileInfo.visibilityShed3D.hiddenAreaColor = window.Cesium.Color.fromCssColorString(
          this.invisibleColor
        )
      }
    },
    drawVisibility() {
      if (profileInfo.pointHandler.active) {
        return
      }
      const viewer = window.viewer
      const scene = window.scene
      // 先清除之前的可视域分析
      viewer.entities.removeAll()
      profileInfo.visibilityShed3D.distance = 0.00001
      scene.viewFlag = true
      // 激活绘制点类
      profileInfo.pointHandler.activate()
    },
    clearVisibility() {
      const viewer = window.viewer
      const scene = window.scene
      viewer.entities.removeAll()
      profileInfo.visibilityShed3D.distance = 0.00001
      profileInfo.pointHandler.clear()
      scene.viewFlag = true
    },
    clear() {
      this.clearVisibility()
      if (profileInfo.handler) {
        profileInfo.handler.removeInputAction(
          window.Cesium.ScreenSpaceEventType.RIGHT_CLICK
        )
        profileInfo.handler.removeInputAction(
          window.Cesium.ScreenSpaceEventType.MOUSE_MOVE
        )
      }
      if (profileInfo.drawFunction && profileInfo.pointHandler) {
        profileInfo.pointHandler.drawEvt.removeEventListener(
          profileInfo.drawFunction
        )
      }
    },
  },
}
</script>
<style lang="less" scoped>
.visibilityContanier {
  label {
    margin-bottom: 0 !important;
  }
  div:after {
    content: '';
    display: block;
    clear: both;
  }
  /deep/.draggable {
    left: 460px;
    top: 75px;
    .header {
      color: #fff;
    }
    .content:after {
      content: '';
      display: block;
      clear: both;
    }
  }
  .M1-item {
    float: left;
    width: 100%;
    > dl {
      float: left;
      width: 100%;
      margin-bottom: 20px;
      color: #fff;
      line-height: 24px;
      dt {
        float: left;
        width: 100%;
        height: 30px;
        border-bottom: 1px solid rgba(255, 255, 255, 0.1);
      }
      dd {
        float: left;
        width: 100%;
        margin-top: 15px;
        label {
          float: left;
          margin-right: 20px;
          :last-child {
            margin-right: 0;
          }
          input[type='radio'] {
            position: relative;
            top: 2px;
            margin-right: 4px;
            color: #0ffaff;
          }
          input:checked + span {
            color: #0ffaff;
          }
        }
        span.color {
          float: left;
          height: 24px;
          input {
            width: 80px;
            height: 24px;
            background: rgba(255, 255, 255, 0.1);
            border: 1px rgba(255, 255, 255, 0.2) solid;
            cursor: pointer;
            color: #fff;
            font-size: 13px;
            padding: 0 5px;
            line-height: 24px;
            border-radius: 2px;
          }
        }
        select {
          width: 100%;
          height: 30px;
          background: rgba(255, 255, 255, 0.1);
          border: 1px rgba(255, 255, 255, 0.2) solid;
          cursor: pointer;
          color: #fff;
          font-size: 13px;
          padding: 0 5px;
          line-height: 30px;
          border-radius: 2px;
        }
      }
    }
  }
  .M4-item {
    float: left;
    width: 100%;
    /deep/ .ant-slider {
      margin: 8px 6px;
    }
    /deep/ .ant-input-number {
      margin-left: 20px;
      width: 77%;
      height: 24px;
      background: rgba(255, 255, 255, 0.1);
      border: 1px rgba(255, 255, 255, 0.2) solid;
      cursor: pointer;
      color: #fff;
      font-size: 13px;
      padding: 0 5px;
      line-height: 24px;
      border-radius: 2px;
      top: 2px;
      .ant-input-number-handler-wrap {
        display: none;
      }
      .ant-input-number-input {
        height: 24px;
        padding: 0 0 0 5px;
      }
    }
    > dl {
      float: left;
      width: 100%;
      margin-bottom: 5px;
      color: #fff;
      line-height: 24px;
      dt {
        float: left;
        width: auto;
        height: 30px;
        border-bottom: 0;
      }
      dd {
        float: left;
        width: auto;
        margin-top: 0;
        label {
          float: left;
          margin-right: 20px;
          :last-child {
            margin-right: 0;
          }
          input[type='radio'] {
            position: relative;
            top: 2px;
            margin-right: 4px;
            color: #0ffaff;
          }
          input:checked + span {
            color: #0ffaff;
          }
        }
        span.color {
          float: left;
          height: 24px;
          input {
            width: 80px;
            height: 24px;
            background: rgba(255, 255, 255, 0.1);
            border: 1px rgba(255, 255, 255, 0.2) solid;
            cursor: pointer;
            color: #fff;
            font-size: 13px;
            padding: 0 5px;
            line-height: 24px;
            border-radius: 2px;
            outline: none;
          }
        }
        select {
          width: 100%;
          height: 30px;
          background: rgba(255, 255, 255, 0.1);
          border: 1px rgba(255, 255, 255, 0.2) solid;
          cursor: pointer;
          color: #fff;
          font-size: 13px;
          padding: 0 5px;
          line-height: 30px;
          border-radius: 2px;
        }
      }
    }
  }
  .M4-item.plan.color {
    width: 50%;
  }
  .analyse-btn {
    width: 100%;
    height: 50px;
    padding: 0 15px;
    span {
      float: right;
      height: 28px;
      font-size: 13px;
      line-height: 28px;
      border-radius: 4px;
      margin-left: 15px;
      :last-child {
        margin-right: 0;
      }
      a {
        display: block;
        padding: 0 15px;
        height: 100%;
        color: #fff;
      }
    }
    span.save {
      background: #1fb17a;
    }
    span.delete {
      background: #f56c6c;
    }
  }
  .M4-item.plan {
    > dl {
      margin-bottom: 0;

      dt {
        width: 100%;
      }
      dd {
        width: 100%;
        .progress {
          float: left;
          width: 75%;
          margin-top: 5px;
        }
        .result {
          float: left;
          width: 25%;
          padding-left: 20px;
          position: relative;
          top: -5px;
          input {
            width: 100%;
            height: 24px;
            background: rgba(255, 255, 255, 0.1);
            border: 1px rgba(255, 255, 255, 0.2) solid;
            cursor: pointer;
            color: #fff;
            font-size: 13px;
            padding: 0 5px;
            line-height: 24px;
            border-radius: 2px;
            outline: none;
          }
        }
        span.color input {
          width: 130px;
        }
      }
    }
    dl.last {
      margin-top: 10px;
    }
  }
  .M1-item.plan {
    > dl {
      margin-bottom: 10px;
      dt {
        color: #f56c6c;
        font-size: 14px;
        i {
          margin-right: 5px;
          position: relative;
          top: 1px;
        }
      }
    }
  }
}
</style>
