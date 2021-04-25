<template>
  <div class="viewpointPaneContanier">
    <gaf-map-draggable
      :visible="visible"
      :width="360"
      title="视点管理"
      panel-func="tools"
      @cancel="handleCloseButton"
    >
      <template #actions>
        <span class="r">
          <a-tooltip title="播放">
            <i class="iconfont icon-bofang" @click="pointFly"></i>
          </a-tooltip>
          <a-tooltip title="采集">
            <i class="iconfont icon-shidian" @click="pickPoint"></i>
          </a-tooltip>
        </span>
      </template>
      <template #minimize>
        <span />
      </template>
      <div class="list-main">
        <div v-if="noData" class="list-item">
          <dl>
            <dt>
              <span class="txt">{{ nodataTitle }}</span>
            </dt>
            <dd></dd>
          </dl>
        </div>
        <div
          v-for="(item, index) in pointList"
          :key="index"
          class="list-item"
          @click="flyToItem(item)"
        >
          <dl>
            <dt>
              <i class="iconfont icon-zhuanti"></i>
              <span v-if="item.editing" class="inp">
                <input
                  v-model="item.name"
                  type="text"
                  placeholder="请输入名称"
                  @click.stop
                />
              </span>
              <span v-else class="txt">{{ item.name }}</span>
            </dt>
            <dd>
              <a-tooltip title="编辑">
                <i
                  v-if="item.editing"
                  class="iconfont icon-duihao sure"
                  @click.stop="savePointName(item)"
                ></i>
                <i
                  v-else
                  class="iconfont icon-bianji edit"
                  @click.stop="editPointName(item)"
                ></i>
              </a-tooltip>
              <a-tooltip title="复制">
                <i
                  v-clipboard:copy="item.cameraCoordinate"
                  v-clipboard:success="onCopy"
                  v-clipboard:error="onError"
                  class="iconfont icon-select-copy"
                  @click.stop
                ></i>
              </a-tooltip>
              <a-tooltip title="删除">
                <i
                  class="iconfont icon-guanbi4"
                  @click.stop="removePoint(item)"
                ></i>
              </a-tooltip>
            </dd>
          </dl>
        </div>
      </div>
    </gaf-map-draggable>
  </div>
</template>
<script>
import { v4 as uuidv4 } from 'uuid'
const VIEW_3D_POSITION = 'view_3d_position'
const VIEW_3D_POSITION_INDEX = 'view_3d_position_index'
export default {
  name: 'ViewpointManage',
  data() {
    return {
      visible: true,
      pointList: [],
      pointIndex: 1,
      isFlying: false,
      nodataTitle: '没有查询到结果!',
    }
  },
  computed: {
    noData() {
      return this.pointList.length === 0
    },
  },
  mounted() {
    // 首先从cookie中获取已经设置的视点和索引
    const jsonViewPositions = this.$cookies.get(VIEW_3D_POSITION)
    const jsonIndex = this.$cookies.get(VIEW_3D_POSITION_INDEX)
    if (jsonViewPositions) {
      this.pointList = JSON.parse(jsonViewPositions)
      this.pointIndex = parseInt(JSON.parse(jsonIndex))
    }
  },
  methods: {
    onCopy() {
      this.$message.success('复制当前视点坐标成功!')
    },
    onError() {
      this.$message.error('获取当前视点坐标失败')
    },
    pickPoint() {
      const camera = window.viewer.camera
      const point = {
        name: '视点管理-' + this.pointIndex,
        id: uuidv4(),
        editing: false,
        view: {
          position: {
            x: camera.position.x,
            y: camera.position.y,
            z: camera.position.z,
          },
          heading: camera.heading,
          pitch: camera.pitch,
          roll: camera.roll,
        },
      }
      const position = point.view.position
      const cartographic = window.Cesium.Cartographic.fromCartesian(position)
      const longitude = window.Cesium.Math.toDegrees(cartographic.longitude)
      const latitude = window.Cesium.Math.toDegrees(cartographic.latitude)
      let height = cartographic.height
      if (height < 0) {
        height = 0
      }
      const tilt = window.Cesium.Math.toDegrees(camera.pitch) + 90
      const cameraCoordinate = {
        altitude: height,
        heading: camera.heading,
        latitude,
        tilt,
        longitude,
      }
      try {
        point.cameraCoordinate = JSON.stringify(cameraCoordinate)
      } catch (error) {
        this.$message.error('获取相机坐标失败,失败原因' + error)
      }
      // 只存储30个点的信息
      if (this.pointList.length >= 30) {
        this.pointList.splice(this.pointList.length - 1, 1)
      }
      this.pointList.push(point)
      this.pointIndex++
      this.savePointList()
      this.$cookies.set(VIEW_3D_POSITION_INDEX, this.pointIndex, '7d')
      this.$message.success('添加视点成功!')
    },
    pointFly() {
      if (this.pointList.length === 0) {
        this.$message.warning('请添加视点进行飞行!')
        return
      }
      let tmpPointList = [...this.pointList]
      const self = this
      if (this.isFlying) {
        this.$message.warning('飞行未结束,请勿重复点击!')
        return
      }
      this.isFlying = true
      this.flyToPoints(tmpPointList, () => {
        tmpPointList = []
        self.isFlying = false
      })
    },
    flyToPoints(points, endCallback) {
      const self = this
      this.flyTo(points[0], () => {
        points.splice(0, 1)
        if (points.length > 0 && self.isFlying) {
          self.flyToPoints(points, endCallback)
        } else if (points.length === 0) {
          if (endCallback) endCallback()
        }
      })
    },
    flyToItem(item) {
      if (this.isFlying) {
        this.$message.warning('飞行未结束,请勿重复点击!')
        return
      }
      const self = this
      this.isFlying = true
      this.flyTo(item, () => {
        self.isFlying = false
      })
    },
    flyTo(point, callback) {
      const camera = window.viewer.camera
      if (point) {
        camera.flyTo({
          destination: point.view.position,
          orientation: {
            heading: point.view.heading || 0,
            pitch: point.view.pitch || 0,
            roll: point.view.roll || 0,
          },
          complete() {
            if (callback) callback()
          },
        })
      }
    },
    savePointName(item) {
      item.editing = false
      this.savePointList()
    },
    editPointName(item) {
      item.editing = true
    },
    removePoint(item) {
      this.pointList = this.pointList.filter((point) => point.id !== item.id)
      this.savePointList()
      if (this.pointList.length === 0) {
        this.pointIndex = 1
        this.$cookies.set(VIEW_3D_POSITION_INDEX, this.pointIndex, '7d')
      }
    },
    savePointList() {
      const jsonViewPositions = JSON.stringify(this.pointList)
      this.$cookies.set(VIEW_3D_POSITION, jsonViewPositions, '7d')
    },
    handleCloseButton() {
      this.visible = false
      this.$emit('draggableClose')
    },
  },
}
</script>
<style lang="less" scoped>
.viewpointPaneContanier {
  color: #fff;
  /deep/ .draggable {
    left: 0;
    .header {
      color: #fff;
      .icon {
        line-height: 39px;
      }
    }
    z-index: 99;
  }
  span.r {
    line-height: 36px;
    i.iconfont {
      font-size: 16px;
      font-weight: normal;
      display: inline-block;
      width: 30px;
      text-align: center;
      position: relative;
      cursor: pointer;
    }
    i.iconfont.icon-shidian {
      margin-right: 2px;
    }
  }
  .header i:hover {
    color: #0ffaff;
  }
  .list-main {
    float: left;
    width: 100%;
    padding: 3px;
    max-height: 360px;
    overflow-y: auto;
    .list-item {
      float: left;
      width: 100%;
      background: rgba(25, 40, 58, 0.3);
      padding: 0 15px;
      margin-bottom: 4px;
      dl {
        float: left;
        width: 100%;
        height: 100%;
        line-height: 36px;
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
            font-size: 18px;
          }
          span.inp {
            padding: 5px 0;
            input {
              width: 150px;
              height: 26px;
              background: transparent;
              border: 1px rgba(0, 250, 255, 0.5) solid;
              font-size: 13px;
              line-height: 26px;
              color: #fff;
              padding: 0 10px;
              outline: none;
            }
          }
        }
        dd {
          float: right;
          height: 100%;
          font-weight: bold;
          i.iconfont {
            position: relative;
            top: 1px;
            margin-left: 5px;
            font-weight: normal;
            font-size: 16px;
            cursor: pointer;
          }
        }
      }
    }
    span.r i.iconfont:hover {
      color: #0ffaff;
    }
    .list-item:hover {
      background: rgba(255, 255, 255, 0.1);
      color: #0ffaff;
    }
    .list-item:hover dl dt,
    .list-item:hover dl dt i,
    .list-item:hover dl dd i {
      color: #0ffaff;
    }
    .list-item:last-child {
      margin-bottom: 0;
    }
  }
}
</style>
