<template>
  <div class="lightShadowContainer1">
    <gaf-map-draggable
      :visible="visible"
      :width="360"
      title="光影变化"
      panel-func="tools"
      @cancel="handleCloseButton"
    >
      <div class="param-item">
        <a-row>
          <a-col :span="8">
            <label>是否开启阴影:</label>
          </a-col>
          <a-col :span="2" style="margin-top: 3px">
            <a-checkbox checked @change="shadowChange"></a-checkbox>
          </a-col>
          <a-col :span="14" style="margin-top: 3px">
            <span>当前时间:</span>
            <span>{{ currentTime }}</span>
          </a-col>
        </a-row>
      </div>
      <div class="param-item" style="position: relative">
        <a-row>
          <a-col :span="20">
            <a-slider
              v-model="lightTime"
              :min="0"
              :max="24"
              :step="1"
              @change="timeChange"
            />
          </a-col>
          <a-col :span="4">
            <span class="play">
              <i
                v-if="timePlay"
                class="iconfont icon-tingzhi-shixin"
                @click="suspendTime"
              ></i>
              <i v-else class="iconfont icon-bofang" @click="playTime"></i>
            </span>
          </a-col>
        </a-row>
        <div class="time s1">00:00</div>
        <div class="time s2">06:00</div>
        <div class="time s3">12:00</div>
        <div class="time s4">18:00</div>
        <div class="time s5">24:00</div>
      </div>
      <div class="param-item" style="margin-top: 30px">
        <a-row>
          <a-col :span="20">
            <a-locale-provider :locale="zhCN">
              <a-date-picker
                v-model="dateShadow"
                :allow-clear="false"
                :input-read-only="true"
                style="width: 99%"
                @change="dateChange"
              />
            </a-locale-provider>
          </a-col>
          <a-col :span="4">
            <span class="play">
              <i
                v-if="datePlay"
                class="iconfont icon-tingzhi-shixin"
                @click="suspendDate"
              ></i>
              <i v-else class="iconfont icon-bofang" @click="playDate"></i>
            </span>
          </a-col>
        </a-row>
      </div>
    </gaf-map-draggable>
  </div>
</template>
<script>
import zhCN from 'ant-design-vue/es/locale-provider/zh_CN'
import moment from 'moment'
export default {
  name: 'LightShadow',
  data() {
    return {
      zhCN,
      dateShadow: moment(new Date(), 'YYYY-MM-DD'),
      visible: true,
      lightTime: 0,
      timePlay: false,
      datePlay: false,
      dateString: moment(Date.now()).format('YYYY-MM-DD'),
      nIntervId: '',
      dateIntervId: '',
      monthIndex: 1,
    }
  },
  computed: {
    currentTime() {
      const currentDate = this.datePlay ? this.getDate() : this.dateString
      const time = this.lightTime + ':00'
      return `${currentDate} ${time}`
    },
  },
  watch: {
    lightTime() {
      const viewer = window.viewer
      const dateVal = this.dateShadow
      const startTime = new Date(dateVal)
      startTime.setHours(this.lightTime)
      startTime.setMinutes(0.0)
      viewer.clock.currentTime = window.Cesium.JulianDate.fromDate(startTime)
    },
  },
  beforeDestroy() {
    this.clear()
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      const layers = window.scene.layers._layerQueue
      layers.forEach((layer) => {
        layer.shadowType = 2
      })
      window.viewer.shadows = true
      // 阴影有效距离
      window.scene.shadowMap.maximumDistance = 50000 // 默认5000
      // window.scene.shadowMap.softShadows = true
      window.scene.shadowMap.size = 8192 // 默认是2048
    },
    playTime() {
      if (this.dateShadow === '') {
        this.$message.warn('请选择日期!')
        return
      }
      if (this.timePlay === true) {
        this.$message.warn('当前播放未结束,请勿重复点击!')
        return
      }
      this.timePlay = true
      const viewer = window.viewer
      const dateVal = this.dateShadow
      const startTime = new Date(dateVal)
      const ehour = 24
      if (this.lightTime > ehour) {
        return
      }
      let nTimer = 0.0
      const _this = this
      const nIntervId = setInterval(function () {
        if (_this.lightTime < ehour) {
          startTime.setHours(_this.lightTime)
          startTime.setMinutes(nTimer)
          viewer.clock.currentTime = window.Cesium.JulianDate.fromDate(
            startTime
          )
          nTimer += 10.0
          if (nTimer > 60.0) {
            _this.lightTime += 1
            nTimer = 0.0
          }
        } else {
          clearInterval(nIntervId)
          _this.timePlay = false
          _this.lightTime = 0
        }
      }, 100)
      this.nIntervId = nIntervId
    },
    suspendTime() {
      this.timePlay = false
      clearInterval(this.nIntervId)
    },
    playDate() {
      if (this.dateShadow === '') {
        this.$message.warn('请选择日期!')
        return
      }
      if (this.datePlay === true) {
        this.$message.warn('当前播放未结束,请勿重复点击!')
        return
      }
      this.datePlay = true
      const viewer = window.viewer

      let dateVal
      let startTime
      const ehour = 24
      if (this.lightTime > ehour) {
        return
      }
      const nTimer = 0.0
      const _this = this
      const dateIntervId = setInterval(function () {
        dateVal = _this.getDate()
        startTime = new Date(dateVal)
        if (_this.monthIndex < 12) {
          startTime.setHours(_this.lightTime)
          startTime.setMinutes(nTimer)
          viewer.clock.currentTime = window.Cesium.JulianDate.fromDate(
            startTime
          )
          _this.monthIndex += 1
        } else {
          clearInterval(dateIntervId)
          _this.datePlay = false
          _this.monthIndex = 1
        }
      }, 500)
      this.dateIntervId = dateIntervId
    },
    suspendDate() {
      this.datePlay = false
      clearInterval(this.dateIntervId)
    },
    getDate() {
      const dateVal = this.dateString
      // 获取年和日 然后从当年的1月开始
      const year = dateVal.substring(0, 4)
      let month
      if (this.monthIndex < 10) {
        month = '0' + this.monthIndex
      } else {
        month = this.monthIndex
      }
      const day = dateVal.substring(8)
      return `${year}-${month}-${day}`
    },
    timeChange(_value) {
      // 调节时间
      this.lightTime = _value
    },
    shadowChange(e) {
      const viewer = window.viewer
      viewer.shadows = e.target.checked
    },
    handleCloseButton() {
      this.visible = false
      this.$emit('draggableClose')
      this.clear()
    },
    dateChange(date, dateString) {
      this.dateString = dateString
    },
    clear() {
      const viewer = window.viewer
      viewer.shadows = false
      const layers = viewer.scene.layers._layerQueue
      layers.forEach((layer) => {
        layer.shadowType = 0
      })
      clearInterval(this.nIntervId)
      clearInterval(this.dateIntervId)
      const startTime = new Date()
      viewer.clock.currentTime = window.Cesium.JulianDate.fromDate(startTime)
    },
  },
}
</script>
<style lang="less" scoped>
.lightShadowContainer1 {
  width: 100%;
  /deep/.draggable {
    z-index: 999;
  }
  .param-item {
    width: 100%;
    margin-bottom: 8px;
    .play {
      display: block;
      width: 30px;
      height: 30px;
      background: rgba(0, 250, 255, 0.4);
      line-height: 30px;
      border-radius: 50%;
      position: relative;
      left: 10px;
      margin-left: 10px;
      text-align: center;
      i.iconfont {
        font-size: 16px;
        position: relative;
        top: 1px;
        color: #fff;
      }
    }
    // /deep/ .ant-calendar-picker {
    //   background: rgba(255, 255, 255, 0.1);
    //   .ant-input {
    //     background: rgba(255, 255, 255, 0.1);
    //     border: 1px rgba(255, 255, 255, 0.2) solid;
    //     color: #fff;
    //   }
    // }
    /deep/ .ant-slider {
      margin: 8px 6px;
    }
    .time {
      width: auto;
      height: 20px;
      color: #fff;
      line-height: 20px;
      position: absolute;
      top: 22px;
    }
    .time.s1 {
      left: 2px;
    }
    .time.s2 {
      left: 55px;
    }
    .time.s3 {
      left: 110px;
    }
    .time.s4 {
      left: 175px;
    }
    .time.s5 {
      left: 235px;
    }
  }
}
</style>
