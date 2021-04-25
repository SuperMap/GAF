<template>
  <div class="building-retreat-container">
    <gaf-map-draggable
      :visible="visible"
      :width="360"
      title="建筑退界"
      panel-func="tools"
      @cancel="handleCloseButton"
    >
      <div class="project-top">
        <dl>
          <dt class="name">退界值(m)</dt>
          <dd class="new">
            <input
              v-model="limitValue"
              placeholder="请输入数值"
              type="number"
            />
          </dd>
          <dd class="btn">
            <button @click="analyse">分析</button>
          </dd>
        </dl>
      </div>
      <div class="fold-box">
        <div
          v-for="(item, itemIndex) in buildingList"
          :key="itemIndex"
          :class="['fold-item', { active: currentItem == itemIndex }]"
          @click="changeItem(item, itemIndex)"
        >
          <div class="fold-top">
            <dl>
              <dt>
                <i
                  :class="[
                    'iconfont',
                    item.sfcy ? 'icon-chenggong' : 'icon-guanbi',
                  ]"
                ></i>
                {{ item.buildingNo }}
              </dt>
            </dl>
          </div>
        </div>
      </div>
      <div class="analyse-btn">
        <span :class="['delete', { disabled: disabled }]">
          <a :disabled="disabled" href="javascript:;" @click="clear">清除</a>
        </span>
      </div>
    </gaf-map-draggable>
  </div>
</template>
<script>
const profileInfo = {
  s3mInstanceColc: null,
  primitive: null,
}
export default {
  name: 'BuildingRetreat',
  props: {
    params: {
      type: String,
      default: () => undefined,
    },
  },
  data() {
    return {
      visible: true,
      limitValue: null,
      buildingList: [],
      currentItem: -1,
      intervalId: undefined,
      timeoutId: undefined,
      direction: window.SMWEBGIS.plan.cameraCoordinate.direction,
      up: window.SMWEBGIS.plan.cameraCoordinate.up,
    }
  },
  computed: {
    disabled() {
      return this.buildingList.length === 0
    },
  },
  destroyed() {
    this.clear()
  },
  methods: {
    changeItem(item, index) {
      if (index === this.currentItem) {
        this.currentItem = -1
      } else {
        this.currentItem = index
        this.setView(item)
      }
    },
    setView(item) {
      if (!item.position) return
      const scene = window.scene
      const position = item.position
      scene.camera.setView({
        // eslint-disable-next-line new-cap
        destination: new window.Cesium.Cartesian3.fromDegrees(
          position.x,
          position.y,
          position.z
        ),
        orientation: {
          direction: new window.Cesium.Cartesian3(
            this.direction.x,
            this.direction.y,
            this.direction.z
          ),
          up: new window.Cesium.Cartesian3(this.up.x, this.up.y, this.up.z),
        },
      })
    },
    async getResultData() {
      if (!this.params) return
      let thresholdValue = this.limitValue
      if (!thresholdValue) {
        // 初始化时没有退界值  默认给0
        thresholdValue = '0'
      }
      const params = {
        projectId: this.params,
        thresholdValue,
      }
      const result = await this.$api.indexRest.getRetreatAnalysisResult(params)
      if (result.status === 1) {
        return result.element
      } else {
        return null
      }
    },
    buildPanel(data) {
      if (!data) return
      const buildingNos = data.buildingNos
      const self = this
      this.buildingList = []
      if (buildingNos && buildingNos.length > 0) {
        const data = {}
        buildingNos.map((item) => {
          data.buildingNo = item.buildingNo
          data.position = item.postion
          data.sfcy = item.sfcy
          self.buildingList.push({ ...data })
        })
      }
    },
    async analyse() {
      if (!this.limitValue) {
        this.$message.warning('退界值不能为空!')
        return
      }
      this.clear()
      const resultData = await this.getResultData()
      this.buildPanel(resultData)
      this.drawLimit(resultData)
    },

    drawLimit(data) {
      if (!data) return
      const self = this
      const viewer = window.viewer
      const scene = window.scene
      const s3mInstanceColc = new window.Cesium.S3MInstanceCollection(
        scene._context
      )
      profileInfo.s3mInstanceColc = s3mInstanceColc
      const primitive = scene.primitives.add(s3mInstanceColc)
      profileInfo.primitive = primitive
      const geometries = data.geometrys
      if (geometries && geometries.length > 0) {
        geometries.forEach((item) => {
          const type = item.type
          switch (type) {
            case 'GEOREGION3D':
              self.buildPolygon(item, viewer)
              break
            case 'GEOLINE3D':
              self.buildPolyline(item, viewer)
              break
            case 'GEOLABLE':
              self.buildLabel(item, viewer)
              break
          }
          viewer.zoomTo(profileInfo.s3mInstanceColc)
          profileInfo.s3mInstanceColc.clampToObject = true
        })
      }
    },
    buildPolygon(item) {
      const self = this
      let geometry
      try {
        geometry = JSON.parse(item.geometry)
      } catch (error) {
        this.$message.error('退界分析失败,失败原因' + error)
      }
      const coordinates = geometry.coordinates[0]
      const height = item.height
      this.timeoutId = setTimeout(() => {
        self.bufferRegion(coordinates, height)
      }, 3000)
    },
    bufferRegion(postPositions, height) {
      if (!this.limitValue) return
      const self = this
      const viewer = window.viewer
      const num = this.limitValue * 1
      const val = num / 7
      let bufferPolygon = null
      let i = 1
      this.intervalId = window.setInterval(function () {
        if (bufferPolygon != null) {
          viewer.entities.remove(bufferPolygon)
        }
        const poly = window.turf.polygon([postPositions])
        const buffered = window.turf.buffer(poly, Number(i * val) / 1000.0, {
          units: 'kilometers',
        })
        console.log(buffered)
        const positions = []
        for (const pos of buffered.geometry.coordinates[0]) {
          positions.push(pos[0])
          positions.push(pos[1])
        }
        bufferPolygon = viewer.entities.add({
          polygon: {
            hierarchy: window.Cesium.Cartesian3.fromDegreesArray(positions),
            height,
            material: window.Cesium.Color.GOLD.withAlpha(0.55),
            outline: true,
            // outlineWidth: 2,
            outlineColor: window.Cesium.Color.RED,
          },
        })
        i++
        if (i === 8) {
          clearInterval(self.intervalId)
        }
      }, 700)
    },
    buildPolyline(item, viewer) {
      let geometry = JSON.parse(item.geometry)
      try {
        geometry = JSON.parse(item.geometry)
      } catch (error) {
        this.$message.error('退界分析失败,失败原因' + error)
      }
      const coordinates = geometry.coordinates
      const height = item.height
      const fromDegreesArray = []
      for (const position of coordinates) {
        fromDegreesArray.push(position[0])
        fromDegreesArray.push(position[1])
        fromDegreesArray.push(height)
      }
      viewer.entities.add({
        polyline: {
          width: 2,
          positions: window.Cesium.Cartesian3.fromDegreesArrayHeights(
            fromDegreesArray
          ),
          material: new window.Cesium.PolylineDashMaterialProperty({
            color: window.Cesium.Color.RED.withAlpha(0.99),
            dashLength: 10,
            dashPattern: 255,
          }),
        },
      })
    },
    buildLabel(item, viewer) {
      const position = item.geometry
      const endX = position.endX
      const endY = position.endY
      const endZ = position.endZ
      const startX = position.startX
      const startY = position.startY
      const startZ = position.startZ
      const text = item.text
      const labelOutlineColor = window.Cesium.Color.BLACK.withAlpha(0.99)
      const points = [startX, startY, startZ, endX, endY, endZ]
      viewer.entities.add({
        polyline: {
          //
          width: 2,
          positions: window.Cesium.Cartesian3.fromDegreesArrayHeights(points),
          material: new window.Cesium.PolylineDashMaterialProperty({
            color: window.Cesium.Color.RED.withAlpha(0.99),
            dashLength: 10,
            dashPattern: 255,
          }),
        },
        label: {
          font: '13px Microsoft YaHei',
          fillColor: labelOutlineColor,
          text,
          showBackground: true,
          backgroundColor: new window.Cesium.Color(1, 1, 0.2, 0.99),
          horizontalOrigin: window.Cesium.HorizontalOrigin.LEFT,
          disableDepthTestDistance: Number.POSITIVE_INFINITY,
        },
        position: window.Cesium.Cartesian3.fromDegrees(endX, endY, endZ),
      })
    },

    handleCloseButton() {
      this.visible = false
      this.$emit('draggableClose')
    },
    clear() {
      const viewer = window.viewer
      const scene = window.scene
      this.buildingList = []
      viewer.entities.removeAll()
      if (profileInfo.s3mInstanceColc) {
        scene.primitives.remove(profileInfo.s3mInstanceColc)
      }
      if (profileInfo.primitive) {
        scene.primitives.remove(profileInfo.primitive)
      }
      if (this.timeoutId) clearTimeout(this.timeoutId)
      if (this.intervalId) clearInterval(this.intervalId)
    },
  },
}
</script>

<style lang="less" scoped>
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
}
input[type='number'] {
  -moz-appearance: textfield;
}
.building-retreat-container {
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
  .project-top {
    float: left;
    width: 100%;
    margin-bottom: 10px;
    dl {
      float: left;
      width: 100%;
      height: 30px;
      dt {
        float: left;
        width: 25%;
        height: 100%;
        font-size: 14px;
        line-height: 30px;
        color: #0ffaff;
        font-weight: bold;
      }
      dt.name {
        line-height: 28px;
        border: 1px rgba(255, 255, 255, 0.2) solid;
        border-right: 0;
        text-align: center;
      }
      dd {
        float: left;
        width: 75%;
        height: 100%;
        input {
          width: 100%;
          height: 30px;
          background: rgba(25, 40, 58, 0.5);
          font-size: 13px;
          font-family: arial;
          line-height: 30px;
          color: #fff;
          border: 1px solid rgba(255, 255, 255, 0.2);
          padding-left: 10px;
          outline: none;
        }
        select {
          width: 100%;
          height: 30px;
          background: rgba(25, 40, 58, 0.5);
          font-size: 13px;
          font-family: arial;
          line-height: 30px;
          color: #fff;
          border-color: rgba(255, 255, 255, 0.1);
          padding-left: 10px;
        }
      }
      dd.new {
        width: 55%;
      }
      dd.btn {
        width: 20%;
        padding-left: 10px;
        button {
          width: 100%;
          height: 100%;
          cursor: pointer;
          font-size: 13px;
          font-weight: bold;
          background: #409eff;
          border: none;
          color: #fff;
          border-radius: 4px;
        }
      }
    }
  }
  .fold-box {
    float: left;
    width: 100%;
    max-height: 300px;
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
            i.iconfont.icon-chenggong {
              color: #1fb17a;
            }
            i.iconfont.icon-guanbi {
              color: #f56c6c;
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
            }
          }
        }
      }
    }
    .fold-item.active {
      background: rgba(0, 250, 255, 0.1);
    }
  }
  .analyse-btn {
    width: 100%;
    height: 30px;
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
    span.delete.disabled {
      cursor: not-allowed;
      a {
        opacity: 0.2;
      }
    }
  }
}
</style>
