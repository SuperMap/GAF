<template>
  <div class="skyLineContainer">
    <gaf-map-draggable
      :visible="visible"
      :width="360"
      title="天际线分析"
      panel-func="tools"
      @cancel="handleCloseButton"
    >
      <div class="M1-item plan">
        <dl>
          <dt><i class="iconfont icon-bangzhu"></i>请调整视角以提取天际线</dt>
        </dl>
      </div>
      <div class="modal-btn">
        <ul>
          <li>
            <a href="javascript:;" @click="chooseView">提取天际线</a>
          </li>
          <li>
            <a href="javascript:;" @click="showSkyline2D">二维查看</a>
          </li>
        </ul>
      </div>
      <div class="modal-tabs">
        <div v-show="!chartVisible" class="modal-tabs-item">
          <div class="fold-head">历史记录时间</div>
          <div class="fold-box">
            <div
              :class="['fold-item', { active: historyActived }]"
              @click="toggleHistoryActivity"
            >
              <div
                v-for="(item, index) in historyData"
                :key="index"
                class="fold-top new"
                @click="loadSkyLine(item.cameraCoordinate)"
              >
                <dl>
                  <dt>
                    <i class="iconfont icon-chenggong"></i>
                    {{ item.time }}
                  </dt>
                  <dd></dd>
                </dl>
              </div>
            </div>
          </div>
        </div>
        <div v-show="chartVisible" class="modal-tabs-item">
          <div class="fold-head">
            二维效果
            <i class="iconfont icon-guanbi4" @click="closeChart"></i>
          </div>
          <div id="id" ref="myEchart" class="modal-chart"></div>
        </div>
      </div>

      <div class="analyse-btn">
        <span class="delete">
          <a href="javascript:;" @click="clear">清除</a>
        </span>
        <span class="save">
          <a href="javascript:;" @click="saveResultData">结果保存</a>
        </span>
      </div>
    </gaf-map-draggable>
    <!-- <div v-show="chartVisible" class="modal-chart">
      
    </div>-->
  </div>
</template>
<script>
import { v4 as uuidv4 } from 'uuid'
import echarts from 'echarts'
import moment from 'moment'
import setSkylineChart from './js/SkyLineChart.js'
const profileInfo = {
  skyline: null,
  echarts: null,
}
export default {
  name: 'Skyline',
  data() {
    return {
      visible: true,
      chartVisible: false,
      skylineVisible: false,
      chartOptions: null,
      iconClass: 'icon-count',
      current: '',
      id: uuidv4(),
      historyActived: true,
      dataSaved: false,
      historyData: [],
      cameraCoordinate: null,
    }
  },
  destroyed() {
    this.clear()
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      const scene = window.scene
      const skyline = new window.Cesium.Skyline(scene) // 创建天际线分析对象
      profileInfo.skyline = skyline
      this.getHistoryData()
    },
    getHistoryData() {
      const historyData = localStorage.getItem('skyLineHistory')
      if (historyData) {
        try {
          this.historyData = JSON.parse(historyData)
        } catch (error) {
          this.$message.error('获取历史数据失败,失败原因' + error)
        }
      }
    },
    toggleHistoryActivity() {
      this.historyActived = !this.historyActived
    },
    saveResultData() {
      if (!this.cameraCoordinate) return
      const currentTime = moment(new Date()).format('YYYY-MM-DD HH:mm:ss')
      const data = {}
      data.time = currentTime
      data.cameraCoordinate = JSON.stringify(this.cameraCoordinate)
      this.historyData.unshift(data)
      localStorage.setItem('skyLineHistory', JSON.stringify(this.historyData))
      this.dataSaved = true
    },

    chooseView() {
      const scene = window.scene
      const cartographic = scene.camera.positionCartographic
      const longitude = window.Cesium.Math.toDegrees(cartographic.longitude)
      const latitude = window.Cesium.Math.toDegrees(cartographic.latitude)
      const height = cartographic.height
      const skyline = profileInfo.skyline
      if (!skyline) return
      // 设置俯仰和方向
      const pitch = window.Cesium.Math.toDegrees(scene.camera.pitch)
      const tilt = pitch + 90
      const direction = window.Cesium.Math.toDegrees(scene.camera.heading)
      this.cameraCoordinate = {}
      this.cameraCoordinate.longitude = longitude
      this.cameraCoordinate.latitude = latitude
      this.cameraCoordinate.height = height
      this.cameraCoordinate.pitch = pitch
      this.cameraCoordinate.tilt = tilt
      this.cameraCoordinate.direction = direction
      this.cameraCoordinate.heading = scene.camera.heading
      this.buildSkyline(skyline, this.cameraCoordinate)
    },
    loadSkyLine(cameraCoordinateJson) {
      const skyline = profileInfo.skyline
      if (!skyline) return
      this.clear()
      if (!cameraCoordinateJson || cameraCoordinateJson === '') return
      const self = this
      const scene = window.scene
      let cameraCoordinate
      try {
        cameraCoordinate = JSON.parse(cameraCoordinateJson)
      } catch (error) {
        this.$message.error('获取历史数据失败,失败原因' + error)
      }
      if (!cameraCoordinate) return
      // eslint-disable-next-line new-cap
      const destination = new window.Cesium.Cartesian3.fromDegrees(
        cameraCoordinate.longitude,
        cameraCoordinate.latitude,
        cameraCoordinate.height
      )
      const tilt = window.Cesium.Math.toRadians(cameraCoordinate.tilt - 90)
      scene.camera.flyTo({
        destination,
        orientation: {
          heading: cameraCoordinate.heading,
          pitch: tilt,
          roll: 0,
        },
        complete() {
          self.buildSkyline(skyline, cameraCoordinate)
        },
      })
    },
    buildSkyline(skyline, cameraCoordinate) {
      skyline.viewPosition = [
        cameraCoordinate.longitude,
        cameraCoordinate.latitude,
        cameraCoordinate.height,
      ]
      skyline.pitch = cameraCoordinate.pitch
      skyline.direction = cameraCoordinate.direction
      skyline.radius = 10000 // 天际线分析半径设置为10000米
      skyline.build()
      this.skylineVisible = true
    },
    showSkyline2D() {
      if (!this.skylineVisible) return
      const self = this
      this.chartVisible = true
      const object = profileInfo.skyline.getSkyline2D()
      const chartOptions = setSkylineChart(object)
      setTimeout(() => {
        profileInfo.echarts = echarts.init(self.$refs.myEchart)
        profileInfo.echarts.setOption(chartOptions, true)
      }, 200)
    },
    closeChart() {
      this.chartVisible = false
    },
    handleCloseButton() {
      const self = this
      if (this.cameraCoordinate && !this.dataSaved) {
        this.$confirm({
          content: '是否保存结果',
          onOk() {
            self.saveResultData()
            self.visible = false
            self.$emit('draggableClose')
          },
          okText: '确定',
          cancelText: '取消',
          onCancel() {
            self.destroyAll()
          },
        })
      } else {
        this.visible = false
        this.$emit('draggableClose')
      }
    },
    destroyAll() {
      this.$destroyAll()
      this.visible = false
      this.$emit('draggableClose')
    },
    clear() {
      window.viewer.entities.removeAll()
      if (profileInfo.skyline) {
        profileInfo.skyline.clear()
        this.cameraCoordinate = null
      }
      if (profileInfo.echarts) {
        profileInfo.echarts.clear()
        profileInfo.echarts = null
      }
      this.chartVisible = false
      this.skylineVisible = false
    },
  },
}
</script>
<style lang="less" scoped>
.skyLineContainer {
  div:after {
    content: '';
    display: block;
    clear: both;
  }
  ul {
    margin: 0;
    padding: 0;
    list-style-type: none;
    word-break: break-all;
    word-wrap: break-word;
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
  .modal-btn {
    float: left;
    width: 100%;
    border-bottom: 1px rgba(255, 255, 255, 0.1) solid;
    ul {
      float: left;
      width: 100%;
      height: 100%;
      li {
        float: left;
        color: #fff;
        font-size: 13px;
        height: 26px;
        margin-bottom: 10px;
        margin-right: 10px;
        a {
          display: block;
          color: #fff;
          padding: 0 15px;
          border: 1px rgba(255, 255, 255, 0.2) solid;
          border-radius: 20px;
          height: 26px;
          line-height: 24px;
        }
        i.iconfont {
          position: relative;
          top: 1px;
          margin-right: 2px;
        }
      }
    }
  }
  .modal-btn ul li a:hover,
  .modal-btn ul li.active a {
    color: #0ffaff;
    border-color: rgba(0, 250, 255, 0.4);
  }
  .fold-head {
    float: left;
    width: 100%;
    height: 40px;
    font-size: 14px;
    color: #0ffaff;
    margin-top: 10px;
    padding: 0 12px;
    position: relative;
    line-height: 40px;
  }
  .fold-head:after {
    content: '';
    display: block;
    position: absolute;
    left: 0;
    top: 13px;
    width: 4px;
    height: 14px;
    background: #0ffaff;
    border-radius: 2px;
  }
  .fold-box {
    float: left;
    width: 100%;
    max-height: 200px;
    overflow-y: auto;
    .fold-item {
      float: left;
      width: 100%;
      background: rgba(25, 40, 58, 0.5);
      padding: 0 15px;
      margin-bottom: 5px;
      .fold-top {
        float: left;
        width: 100%;
        height: 40px;
        dl {
          float: left;
          width: 100%;
          height: 100%;
          line-height: 40px;
          color: #fff;
          font-size: 14px;
          dt {
            float: left;
            height: 100%;
            i.iconfont {
              position: relative;
              top: 1px;
              margin-right: 6px;
              color: #fff;
              float: left;
            }
          }
          dd {
            float: right;
            height: 100%;
            font-weight: bold;
            i.iconfont {
              position: relative;
              top: 1px;
              margin-left: 2px;
              font-weight: normal;
            }
            i.icon-xia {
              font-size: 20px;
              top: 2px;
              margin-right: 4px;
            }
            i.icon-guanbi4 {
              font-size: 16px;
              top: 0;
              position: absolute;
              margin-left: 5px;
              top: 1px;
              cursor: pointer;
            }
          }
        }
      }
      .fold-top.new {
        border-bottom: 1px solid rgba(255, 255, 255, 0.2);
      }
      .fold-top.new:last-child {
        border-bottom: 0;
      }
    }
    .fold-item.active {
      background: rgba(0, 250, 255, 0.1);
    }
    // .fold-item:last-child {
    //   margin-bottom: 0;
    // }
  }
  .analyse-btn {
    width: 100%;
    height: 30px;
    padding: 0 15px;
    float: left;
    margin-top: 15px;
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
  .modal-chart {
    float: left;
    width: 100%;
    height: 180px;
  }
  // .modal-chart {
  //   position: absolute;
  //   right: 5%;
  //   bottom: 2%;
  //   width: 350px;
  //   height: 280px;
  //   z-index: 9999;
  // }
}
</style>
