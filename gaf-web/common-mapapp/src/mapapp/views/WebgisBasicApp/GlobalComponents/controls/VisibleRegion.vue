<template>
  <div class="visibleRegionContanier">
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
              :min="1"
              :max="20"
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
              v-model="directionValue"
              :tip-formatter="null"
              :min="1"
              :max="20"
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
            <label>距离(米)：</label>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="18">
            <a-slider
              v-model="directionValue"
              :tip-formatter="null"
              :min="1"
              :max="20"
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
            <label>水平视场角(度)：</label>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="18">
            <a-slider
              v-model="directionValue"
              :tip-formatter="null"
              :min="1"
              :max="20"
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
            <label>垂直视场角(度)：</label>
          </a-col>
        </a-row>
        <a-row>
          <a-col :span="18">
            <a-slider
              v-model="directionValue"
              :tip-formatter="null"
              :min="1"
              :max="20"
            />
          </a-col>
          <a-col :span="6">
            <a-input-number v-model="directionValue" :min="1" :max="20" />
          </a-col>
        </a-row>
      </div>
      <div class="M4-item plan color">
        <dl>
          <dt>可见区域颜色：</dt>
          <dd>
            <span class="color">
              <input id="colorPicker11" type="text" />
            </span>
          </dd>
        </dl>
      </div>
      <div class="M4-item plan color">
        <dl>
          <dt>不可见区域颜色：</dt>
          <dd>
            <span class="color">
              <input id="colorPicker12" type="text" />
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
        <span class="dele">
          <a href="javascript:;" @click="cllearVisibleRegion">清除</a>
        </span>
        <span class="save">
          <a href="javascript:;" @click="drawVisibleRegion">绘制</a>
        </span>
      </div>
    </gaf-map-draggable>
  </div>
</template>
<script>
// const profileInfo = {
//   viewshed3D: null,
//   pointHandler: null,
//   handler: null
// }
export default {
  name: 'VisibleRegion',
  data() {
    return {
      visible: true,
      directionValue: 1,
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      // const viewer = window.viewer
      // const scene = window.scene
      // scene.viewFlag = true
      // const pointHandler = new window.Cesium.DrawHandler(viewer, window.Cesium.DrawMode.Point)
      // profileInfo.pointHandler = pointHandler
      // const viewshed3D = new window.Cesium.ViewShed3D(scene)
      // profileInfo.viewshed3D = viewshed3D
      // const handler = new window.Cesium.ScreenSpaceEventHandler(scene.canvas)
    },
    handleCloseButton() {
      this.visible = false
      this.$emit('draggableClose')
    },
    drawVisibleRegion() {},
    cllearVisibleRegion() {},
  },
}
</script>
<style lang="less" scoped>
.visibleRegionContanier {
  label {
    margin-bottom: 0 !important;
  }
  div:after {
    content: '';
    display: block;
    clear: both;
  }
  /deep/.draggable {
    .header {
      color: #fff;
    }
    .content:after {
      content: '';
      display: block;
      clear: both;
    }
  }
  .M1-main {
    width: 100%;
    // padding: 20px 0;
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
    span.dele {
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
