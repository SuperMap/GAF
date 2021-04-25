<template>
  <div class="background-color">
    <a-layout class="background-color">
      <a-layout-content class="background-color">
        <a-form layout="horizontal" :form="form">
          <a-form-item
            label="底图地址"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-row>
              <a-col :span="20">
                <a-input
                  v-decorator="[
                    'address',
                    {
                      rules: [
                        {
                          required: true,
                          message: '请选择底图!',
                        },
                      ],
                    },
                  ]"
                  size="small"
                  style="width: 100%"
                  disabled
                />
              </a-col>
              <a-col :offset="1" :span="3">
                <button @click="visible = true">
                  选择底图
                </button>
              </a-col>
            </a-row>
          </a-form-item>
          <a-form-item
            label="中心点"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="X"
                  :label-col="{ span: 4 }"
                  :wrapper-col="{ span: 20 }"
                >
                  <a-input
                    v-decorator="[
                      'centerX',
                      {
                        rules: [
                          {
                            required: true,
                            message: '请输入中心点!',
                          },
                        ],
                      },
                    ]"
                    size="small"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="Y"
                  :label-col="{ span: 4 }"
                  :wrapper-col="{ span: 20 }"
                >
                  <a-input
                    v-decorator="[
                      'centerY',
                      {
                        rules: [
                          {
                            required: true,
                            message: '请输入中心点!',
                          },
                        ],
                      },
                    ]"
                    size="small"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </a-form-item>
          <a-form-item
            label="范围"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="left"
                  :label-col="{ span: 4 }"
                  :wrapper-col="{ span: 20 }"
                >
                  <a-input
                    v-decorator="[
                      'boundsLeft',
                      {
                        rules: [
                          {
                            required: true,
                            message: '请输入范围!',
                          },
                        ],
                      },
                    ]"
                    size="small"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="top"
                  :label-col="{ span: 4 }"
                  :wrapper-col="{ span: 20 }"
                >
                  <a-input
                    v-decorator="[
                      'boundsTop',
                      {
                        rules: [
                          {
                            required: true,
                            message: '请输入范围!',
                          },
                        ],
                      },
                    ]"
                    size="small"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row>
              <a-col :span="12">
                <a-form-item
                  label="right"
                  :label-col="{ span: 4 }"
                  :wrapper-col="{ span: 20 }"
                >
                  <a-input
                    v-decorator="[
                      'boundsRight',
                      {
                        rules: [
                          {
                            required: true,
                            message: '请输入范围!',
                          },
                        ],
                      },
                    ]"
                    size="small"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="12">
                <a-form-item
                  label="bottom"
                  :label-col="{ span: 4 }"
                  :wrapper-col="{ span: 20 }"
                >
                  <a-input
                    v-decorator="[
                      'boundsBottom',
                      {
                        rules: [
                          {
                            required: true,
                            message: '请输入范围!',
                          },
                        ],
                      },
                    ]"
                    size="small"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </a-form-item>
          <a-form-item
            label="比例尺方案"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-select v-decorator="['scaleType']" size="small" @change="change">
              <a-select-option value="superMap">
                SuperMap Cloud/Google Maps/Bing Maps
              </a-select-option>
              <a-select-option value="TD">天地图</a-select-option>
              <a-select-option value="custom">自定义</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item
            v-show="addScaleFM"
            label="比例尺设置"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-row>
              <a-col :span="18">
                <a-input-number
                  v-model="scaleNum"
                  size="small"
                  style="width: 100%"
                />
              </a-col>
              <a-col :offset="1" :span="4">
                <button @click="scaleClick">
                  添加比例尺分母
                </button>
              </a-col>
            </a-row>
          </a-form-item>
          <a-form-item
            label="选择比例尺"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-row>
              <a-col :span="8">
                <a-select
                  v-model="minScale"
                  size="small"
                  style="width: 100%"
                  :disabled="scaleDisabled"
                  @change="scaleChange"
                >
                  <a-select-option v-for="item in scaleOptions" :key="item">
                    {{ item }}
                  </a-select-option>
                </a-select>
              </a-col>
              <a-col :offset="1" :span="8">
                <a-select
                  v-model="maxScale"
                  size="small"
                  style="width: 100%"
                  :disabled="scaleDisabled"
                  @change="scaleChange"
                >
                  <a-select-option v-for="item in scaleOptions" :key="item">
                    {{ item }}
                  </a-select-option>
                </a-select>
              </a-col>
              <a-col :offset="1" :span="2">
                <button style="width: 100%" @click="deleteScale">
                  移除
                </button>
              </a-col>
              <a-col :offset="1" :span="2">
                <button style="width: 100%" @click="clearScale">
                  全部移除
                </button>
              </a-col>
            </a-row>
            <a-row>
              <a-table
                :row-key="(record) => record.scale"
                :show-header="false"
                :pagination="false"
                size="small"
                :columns="columns"
                :data-source="scales"
                :row-class-name="setRowClassName"
                :custom-row="click"
              />
            </a-row>
          </a-form-item>
          <a-form-item
            label="dpi"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <span>{{ dpi }}</span>
          </a-form-item>
          <a-form-item
            label="投影信息"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-row>
              <a-col :offset="1" :span="8">
                EPSG Code：{{
                  prjCoordSys !== null ? prjCoordSys.epsgCode : ''
                }}
              </a-col>
              <a-col :offset="1" :span="8">
                大地基准面：{{
                  prjCoordSys !== null ? prjCoordSys.coordSystem.datum.name : ''
                }}
              </a-col>
            </a-row>
            <a-row>
              <a-col :offset="1" :span="8">
                参考椭球体：{{
                  prjCoordSys !== null
                    ? prjCoordSys.coordSystem.datum.spheroid.name
                    : ''
                }}
              </a-col>
              <a-col :offset="1" :span="8">
                椭球长半径：{{
                  prjCoordSys !== null
                    ? prjCoordSys.coordSystem.datum.spheroid.axis
                    : ''
                }}
              </a-col>
            </a-row>
            <a-row>
              <a-col :offset="1" :span="8">
                椭球扁率：{{
                  prjCoordSys !== null
                    ? prjCoordSys.coordSystem.datum.spheroid.flatten
                    : ''
                }}
              </a-col>
              <a-col :offset="1" :span="8">
                中央子午线：{{
                  prjCoordSys !== null
                    ? prjCoordSys.coordSystem.primeMeridian.longitudeValue
                    : ''
                }}
              </a-col>
            </a-row>
            <a-row>
              <a-col :offset="1" :span="8">
                投影方式：{{
                  prjCoordSys !== null && prjCoordSys.projection
                    ? prjCoordSys.projection.name
                    : ''
                }}
              </a-col>
              <a-col :offset="1" :span="8">
                中央经线：{{
                  prjCoordSys !== null && prjCoordSys.projectionParam
                    ? prjCoordSys.projectionParam.centralMeridian
                    : ''
                }}
              </a-col>
            </a-row>
            <a-row>
              <a-col :offset="1" :span="8">
                水平偏移量：{{
                  prjCoordSys !== null && prjCoordSys.projectionParam
                    ? prjCoordSys.projectionParam.falseEasting
                    : ''
                }}
              </a-col>
              <a-col :offset="1" :span="8">
                比例因子：{{
                  prjCoordSys !== null && prjCoordSys.projectionParam
                    ? prjCoordSys.projectionParam.scaleFactor
                    : ''
                }}
              </a-col>
            </a-row>
          </a-form-item>
          <a-form-item>
            <a-row>
              <a-col :offset="19" :span="2">
                <button @click="onSubmit">
                  确定
                </button>
              </a-col>
            </a-row>
          </a-form-item>
        </a-form>
      </a-layout-content>
      <a-layout-sider class="sider-style background-color" :width="400">
        <span style="font-size: medium">地图预览</span>
        <a-card hoverable style="width: 250px; height: 250px">
          <img slot="cover" alt="example" :src="imageUrl" />
        </a-card>
        <a-row>
          <span style="font-size: medium">表述格式</span>
        </a-row>
        <a-row>
          <a :href="serviceUrl + '.xml'" target="_blank">
            <span>xml</span>
          </a>
        </a-row>
        <a-row>
          <a :href="serviceUrl + '.json'" target="_blank">
            <span>json</span>
          </a>
        </a-row>
        <a-row>
          <a :href="serviceUrl + '.fastjson'" target="_blank">
            <span>fastjson</span>
          </a>
        </a-row>
        <a-row>
          <a :href="serviceUrl + '.rjson'" target="_blank">
            <span>rjson</span>
          </a>
        </a-row>
        <a-row>
          <a :href="serviceUrl + '.html'" target="_blank">
            <span>html</span>
          </a>
        </a-row>
        <a-row>
          <a :href="serviceUrl + '.jsonp'" target="_blank">
            <span>jsonp</span>
          </a>
        </a-row>
      </a-layout-sider>
    </a-layout>
    <gaf-modal
      v-model="visible"
      title="新增查询"
      :width="800"
      :mask-closable="false"
      @ok="handleOk"
    >
      <select-layer-service @onServiceClick="onServiceClick" />
    </gaf-modal>
  </div>
</template>

<script>
// @ts-nocheck
import { getBaseLayerUrl } from '~/utils/GAFMapDataUtils'
import exampleImage from '~/assets/example.png'
import selectLayerService from '../BaseLayer/selectLayerService'
import { getScaleValuesByType } from '~/utils/ScaleValues'
const columns = [
  {
    title: 'Scale',
    dataIndex: 'scale',
  },
]
export default {
  components: {
    selectLayerService,
  },
  props: {
    mapCode: {
      type: String,
      required: true,
      validator(value) {
        return value
      },
    },
  },
  data() {
    return {
      labelCol: { span: 4 },
      valueCol: { span: 16 },
      imageUrl: exampleImage,
      serviceUrl: '',
      visible: false,
      scaleNum: 0,
      minScale: '',
      maxScale: '',
      scaleOptions: [],
      scales: [],
      columns,
      prjCoordSys: null,
      addScaleFM: false,
      scaleDisabled: false,
      layerServiceInfo: {},
      rowId: '',
      baseLayerId: '',
      dpi: '',
    }
  },
  beforeCreate() {
    this.form = this.$form.createForm(this)
  },
  created() {
    this.loadBaseLayerInfo()
  },
  methods: {
    async loadBaseLayerInfo() {
      const url = getBaseLayerUrl(this.mapCode)
      const res = await this.$axios.$get(url)
      if (res && res.data) {
        const baseInfo = res.data
        this.baseLayerId = baseInfo.id
        this.dpi = baseInfo.dpi
        this.form.setFieldsValue({
          centerX: baseInfo.centerX,
          centerY: baseInfo.centerY,
          boundsLeft: baseInfo.boundsLeft,
          boundsRight: baseInfo.boundsRight,
          boundsTop: baseInfo.boundsTop,
          boundsBottom: baseInfo.boundsBottom,
          scaleType: baseInfo.scaleType,
        })
        this.change(baseInfo.scaleType)
        if (baseInfo.layerServiceInfo) {
          this.layerServiceInfo = baseInfo.layerServiceInfo
          this.form.setFieldsValue({
            address: baseInfo.layerServiceInfo.address,
          })
          this.imageUrl =
            baseInfo.layerServiceInfo.address + '/zxyTileImage.png'
          this.serviceUrl = baseInfo.layerServiceInfo.address
        }
        if (baseInfo.prjCoordSys) {
          this.prjCoordSys = JSON.parse(baseInfo.prjCoordSys)
        }
        if (baseInfo.scale) {
          const scaleList = JSON.parse(baseInfo.scale)
          if (scaleList && scaleList.length) {
            this.scales = []
            if (baseInfo.scaleType === 'custom') {
              for (let i = 0; i < scaleList.length; i++) {
                const item = {
                  scale: scaleList[i],
                }
                this.scales.push(item)
              }
            } else {
              const allScales = getScaleValuesByType(baseInfo.scaleType)
              allScales.forEach((item) => {
                const arrScale = item.split(':')
                if (
                  arrScale.length === 2 &&
                  scaleList.includes(arrScale[1].trim())
                ) {
                  const scaleItem = {
                    scale: item,
                  }
                  this.scales.push(scaleItem)
                }
              })
            }
          }
        }
      }
    },
    handleOk() {
      this.form.setFieldsValue({
        address: this.layerServiceInfo.address,
      })
      this.imageUrl = this.layerServiceInfo.address + '/zxyTileImage.png'
      this.serviceUrl = this.layerServiceInfo.address
      this.showLayerServiceInfo(this.layerServiceInfo.address)
      this.visible = false
    },
    async showLayerServiceInfo(url) {
      if (url.includes('/maps/')) {
        const mapLayerJsonUrl = url + '.jsonp'
        const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
        if (mapLayerJson.dpi) {
          this.dpi = mapLayerJson.dpi
        }
        if (
          mapLayerJson.center &&
          mapLayerJson.bounds &&
          mapLayerJson.prjCoordSys
        ) {
          this.form.setFieldsValue({
            centerX: mapLayerJson.center.x,
            centerY: mapLayerJson.center.y,
            boundsLeft: mapLayerJson.bounds.left,
            boundsRight: mapLayerJson.bounds.right,
            boundsTop: mapLayerJson.bounds.top,
            boundsBottom: mapLayerJson.bounds.bottom,
          })
          this.prjCoordSys = mapLayerJson.prjCoordSys
        }
      }
    },
    change(value) {
      if (value === 'superMap' || value === 'TD') {
        this.addScaleFM = false
        this.scaleDisabled = false
        this.scaleOptions = getScaleValuesByType(value)
      } else {
        this.addScaleFM = true
        this.scaleDisabled = true
      }
    },
    scaleClick() {
      const newScale = '1/' + this.scaleNum
      const item = {
        scale: newScale,
      }
      this.scales.push(item)
    },
    scaleChange() {
      const showScales = []
      let s = ''
      if (this.minScale && this.maxScale) {
        for (let i = 0; i < this.scaleOptions.length; i++) {
          if (
            this.scaleOptions[i] === this.minScale ||
            this.scaleOptions[i] === this.maxScale
          ) {
            if (s === '') {
              s = 'add'
            } else if (s === 'add') {
              showScales.push(this.scaleOptions[i])
              s = ''
            }
          }
          if (s === 'add') {
            showScales.push(this.scaleOptions[i])
          }
        }
      }
      if (showScales.length > 0) {
        this.scales = []
        for (let i = 0; i < showScales.length; i++) {
          const item = {
            scale: showScales[i],
          }
          this.scales.push(item)
        }
      }
    },
    deleteScale() {
      if (this.scales.length > 0 && this.rowId) {
        for (let i = 0; i < this.scales.length; i++) {
          if (this.scales[i].scale === this.rowId) {
            this.scales.splice(i, 1)
            break
          }
        }
        this.scales = [...this.scales]
      }
    },
    clearScale() {
      this.scales = []
    },
    onServiceClick(layerInfo) {
      this.layerServiceInfo = layerInfo
    },
    setRowClassName(record) {
      return record.scale === this.rowId ? 'clickRowStyl' : ''
    },
    click(record) {
      return {
        on: {
          click: () => {
            this.rowId = record.scale
          },
        },
      }
    },
    onSubmit() {
      this.form.validateFields(async (err, values) => {
        if (err) {
          return
        }
        const scaleList = []
        if (this.scales.length) {
          for (let i = 0; i < this.scales.length; i++) {
            const arrScale = this.scales[i].scale.split(':')
            if (arrScale.length === 2) {
              scaleList.push(arrScale[1].trim())
            } else {
              scaleList.push(this.scales[i].scale)
            }
          }
        }
        const baseLayer = {
          serviceId: this.layerServiceInfo.id,
          prjCoordSys: JSON.stringify(this.prjCoordSys),
          centerX: values.centerX,
          centerY: values.centerY,
          boundsLeft: values.boundsLeft,
          boundsTop: values.boundsTop,
          boundsRight: values.boundsRight,
          boundsBottom: values.boundsBottom,
          dpi: this.dpi,
          scaleType: values.scaleType,
          scale: JSON.stringify(scaleList),
          mapConfigCode: this.mapCode,
        }
        // const url = `/manager/map/${this.mapCode}/baselayer`
        const url = getBaseLayerUrl(this.mapCode)
        if (this.baseLayerId) {
          const result = await this.$axios.$put(
            url + `/${this.baseLayerId}`,
            baseLayer
          )
          if (result.isSuccessed) {
            this.$message.success('编辑成功')
          } else {
            this.$message.error('编辑失败')
          }
        } else {
          const result = await this.$axios.$post(url, baseLayer)
          if (result.isSuccessed) {
            this.$message.success('添加成功')
            this.baseLayerId = result.data
          } else {
            this.$message.error('添加失败')
          }
        }
      })
    },
  },
}
</script>

<style lang="less" scoped>
.clickRowStyl {
  background-color: #00b4ed;
}
.ant-table-tbody > .clickRowStyl :hover > td {
  background-color: #00b4ed;
}
</style>
<style scoped>
.sider-style {
  min-width: 400px;
  max-width: 400px;
  height: 90vh;
  text-align: left;
}
.background-color {
  background: #ffffff;
}
.editable-row-operations a {
  margin-right: 8px;
}
</style>
