<template>
  <div class="background-color gutter-box">
    <a-breadcrumb separator=">">
      <a-breadcrumb-item>
        <a @click="$emit('back') && $emit('refresh')">服务列表</a>
      </a-breadcrumb-item>
      <a-breadcrumb-item>添加服务</a-breadcrumb-item>
    </a-breadcrumb>
    <a-layout class="background-color">
      <a-layout-content class="background-color">
        <a-form layout="horizontal" :form="form">
          <div v-if="activeCard === 'manual'">
            <a-form-item
              label="服务地址"
              :label-col="labelCol"
              :wrapper-col="valueCol"
            >
              <a-input
                v-decorator="['address']"
                size="small"
                @blur="serviceCheck"
              />
            </a-form-item>
            <a-form-item>
              <a-row style="margin: 0px">
                <a-col :offset="4" :span="20">
                  地址示例1：http://server:port/iserver/services/**/rest
                </a-col>
              </a-row>
              <a-row style="margin: 0px">
                <a-col :offset="4" :span="20">
                  地址示例2：http://server:port/iserver/services/**/rest/maps/mapName
                </a-col>
              </a-row>
              <a-row style="margin: 0px">
                <a-col :offset="4" :span="20">
                  地址示例3：http://server:port/iserver/services/**/rest/realspace/datas/layerName
                </a-col>
              </a-row>
            </a-form-item>
          </div>
          <a-form-item
            v-if="activeCard === 'manual'"
            label="服务类型"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-select
              v-decorator="[
                'serviceType',
                {
                  rules: [
                    {
                      required: true,
                      message: '请选择地图类型!',
                    },
                  ],
                },
              ]"
              size="small"
              disabled
              @change="change"
            >
              <a-select-option value="RESTDATA">数据服务</a-select-option>
              <a-select-option value="RESTMAP">地图服务</a-select-option>
              <a-select-option value="RESTMAP3D">三维服务</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item
            label="服务名称"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-input
              v-decorator="[
                'serviceName',
                {
                  rules: [
                    {
                      required: true,
                      message: '请输入服务名称!',
                    },
                  ],
                },
              ]"
              size="small"
            />
          </a-form-item>
          <a-form-item
            v-if="visible && activeCard === 'manual'"
            label="图层名称"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-input
              v-decorator="[
                'layerName',
                {
                  rules: [
                    {
                      required: true,
                      message: '请输入图层名称!',
                    },
                  ],
                },
              ]"
              size="small"
            />
          </a-form-item>
          <a-form-item
            label="备注"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-textarea v-decorator="['description']" size="small" autosize />
          </a-form-item>
          <a-form-item>
            <div v-show="mapServices.length > 0">
              <a-row>
                <a-col :offset="4">
                  <span>将该服务中的以下地图添加到地图列表</span>
                </a-col>
              </a-row>
              <a-row>
                <a-col :offset="4" :span="16">
                  <a-table
                    :row-key="(record) => record.name"
                    :row-selection="{
                      selectedRowKeys: selectedRowKeys,
                      onChange: onSelectChange,
                    }"
                    :custom-row="click"
                    :pagination="false"
                    size="small"
                    :columns="columns"
                    :data-source="mapServices"
                  >
                    <template slot="name" slot-scope="text, record">
                      <a-input
                        style="margin: -5px 0"
                        :value="text"
                        @change="
                          (e) => handleChange(e.target.value, record.name)
                        "
                      />
                    </template>
                  </a-table>
                </a-col>
              </a-row>
            </div>
          </a-form-item>
          <a-form-item>
            <a-row>
              <a-col :offset="19" :span="2">
                <a-button type="primary" size="small" @click="onSubmit">
                  确定
                </a-button>
              </a-col>
            </a-row>
          </a-form-item>
          <a-form-item :label-col="labelCol" :wrapper-col="valueCol">
            <div v-show="mapLayerInfo !== null">
              <a-row>
                <a-col :offset="4">
                  <span style="font-size: medium">描述: </span>
                  <span>
                    包含当前地图的所有相关状态信息，如中心点、比例尺、全幅范围等。
                  </span>
                </a-col>
              </a-row>
              <a-row>
                <a-col :offset="4" :span="20">
                  中心点：{{ mapLayerInfo !== null ? mapLayerInfo.center : '' }}
                </a-col>
              </a-row>
              <a-row>
                <a-col :offset="4" :span="20"> 地图范围： </a-col>
              </a-row>
              <a-row>
                <a-col :offset="5" :span="8">
                  左：{{ mapLayerInfo !== null ? mapLayerInfo.left : '' }}
                </a-col>
                <a-col :offset="2" :span="8">
                  上：{{ mapLayerInfo !== null ? mapLayerInfo.top : '' }}
                </a-col>
              </a-row>
              <a-row>
                <a-col :offset="5" :span="8">
                  右：{{ mapLayerInfo !== null ? mapLayerInfo.right : '' }}
                </a-col>
                <a-col :offset="2" :span="8">
                  下：{{ mapLayerInfo !== null ? mapLayerInfo.bottom : '' }}
                </a-col>
              </a-row>
              <a-row>
                <a-col :offset="4" :span="20">
                  坐标系信息：{{
                    mapLayerInfo !== null ? mapLayerInfo.prjcoordsys : ''
                  }}
                </a-col>
              </a-row>
              <a-row>
                <a-col :offset="4" :span="20">
                  比例尺：{{ mapLayerInfo !== null ? mapLayerInfo.scale : '' }}
                </a-col>
              </a-row>
            </div>
          </a-form-item>
        </a-form>
      </a-layout-content>
      <a-layout-sider class="sider-style background-color" :width="400">
        <span style="font-size: medium">地图预览</span>
        <a-card hoverable style="width: 250px; height: 250px">
          <img slot="cover" alt="" :src="imageUrl" />
        </a-card>
        <div v-show="activeCard === 'manual'">
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
        </div>
      </a-layout-sider>
    </a-layout>
  </div>
</template>

<script>
// @ts-nocheck
import { dataUrl } from '~/utils/GAFMapDataUtils'
import exampleImage from '~/assets/example.png'
const columns = [
  {
    title: '图层名称',
    dataIndex: 'name',
    scopedSlots: { customRender: 'name' },
  },
]
const serveColumns = [
  {
    title: '名称',
    dataIndex: 'name',
    key: 'name',
    width: 150,
    scopedSlots: { customRender: 'name' },
  },
  {
    title: '类型',
    width: 100,
    dataIndex: 'type',
    key: 'type',
    scopedSlots: { customRender: 'type' },
  },
  {
    title: '路径',
    dataIndex: 'url',
    key: 'url',
    scopedSlots: { customRender: 'url' },
  },
]
export default {
  data() {
    return {
      labelCol: { span: 4 },
      valueCol: { span: 16 },
      mapLayerInfo: null,
      type: '',
      imageUrl: exampleImage,
      serviceUrl: '',
      mapServices: [],
      selectedRowKeys: [],
      selectedRows: [],
      serviceSelectedRowKeys: [],
      serviceSelectedRows: [],
      // serveData: [],
      visible: false,
      columns,
      serveColumns,
      activeCard: 'manual',
    }
  },
  beforeCreate() {
    this.form = this.$form.createForm(this)
  },
  methods: {
    serviceCheck(e) {
      this.resolverURL(e.target.value)
    },
    async resolverURL(serviceURL) {
      this.mapServices = []
      this.visible = false
      this.serviceUrl = serviceURL
      this.imageUrl = exampleImage
      this.mapLayerInfo = null
      const u = /rest$/
      const u1 = /rest\/maps\/./
      const u2 = /rest\/realspace\/datas\/./
      if (!u.test(serviceURL) && !u1.test(serviceURL) && !u2.test(serviceURL)) {
        const arr = [
          {
            message: '服务地址格式不正确！',
            field: 'address',
          },
        ]
        this.form.setFields({
          address: { value: serviceURL, errors: arr },
        })
        return
      } else {
        this.form.setFields({
          address: { value: serviceURL },
        })
      }
      if (u.test(serviceURL)) {
        const jsonUrl = serviceURL + '.jsonp'
        const jsonInfo = await this.$axios.jsonp(jsonUrl)
        if (jsonInfo && jsonInfo.length) {
          for (let i = 0; i < jsonInfo.length; i++) {
            const jsonItem = jsonInfo[i]
            if (jsonItem.resourceConfigID === 'maps') {
              this.form.setFieldsValue({
                serviceType: 'RESTMAP',
              })
              const mapsInfo = await this.$axios.jsonp(
                serviceURL + '/maps.jsonp'
              )
              if (mapsInfo && mapsInfo.length) {
                this.mapServices = mapsInfo
              }
            } else if (jsonItem.resourceConfigID === 'data') {
              this.form.setFieldsValue({
                serviceType: 'RESTDATA',
              })
            } else if (jsonItem.resourceConfigID === 'realspace') {
              this.form.setFieldsValue({
                serviceType: 'RESTMAP3D',
              })
              const mapsInfo = await this.$axios.jsonp(
                serviceURL + '/realspace/datas.jsonp'
              )
              if (mapsInfo && mapsInfo.length) {
                this.mapServices = mapsInfo
              }
            }
          }
        }
      } else if (u1.test(serviceURL)) {
        this.form.setFieldsValue({
          serviceType: 'RESTMAP',
        })
        this.visible = true
        this.showLayerServiceInfo(serviceURL)
        this.imageUrl = serviceURL + '/zxyTileImage.png'
      } else if (u2.test(serviceURL)) {
        this.form.setFieldsValue({
          serviceType: 'RESTMAP3D',
        })
        this.visible = true
      }
    },
    change(value) {
      this.type = value
    },
    handleChange(value, key) {
      const newData = [...this.mapServices]
      const target = newData.filter((item) => key === item.name)[0]
      const column = 'name'
      if (target) {
        target[column] = value
        this.mapServices = newData
      }
    },
    click(record) {
      return {
        on: {
          click: () => {
            // 显示地图信息
            this.showLayerServiceInfo(record.path)
            this.imageUrl = record.path + '/zxyTileImage.png'
          },
          // 失去焦点后保存编辑
          blur: () => {
            const newData = [...this.mapServices]
            const target = newData.filter(
              (item) => record.path === item.path
            )[0]
            if (target) {
              newData.map((item) => ({ ...item }))
              this.mapServices = newData
            }
          },
        },
      }
    },
    serveClick(record) {
      return {
        on: {
          click: () => {
            // this.imageUrl = record.url + '/zxyTileImage.png'
            this.resolverURL(record.hostAddr + record.serverUrl)
          },
        },
      }
    },
    async showLayerServiceInfo(url) {
      if (url.includes('/maps/')) {
        const mapLayerJsonUrl = url + '.jsonp'
        const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
        if (
          mapLayerJson.center &&
          mapLayerJson.bounds &&
          mapLayerJson.prjCoordSys &&
          mapLayerJson.scale
        ) {
          this.mapLayerInfo = {}
          this.mapLayerInfo.center =
            '(' + mapLayerJson.center.x + ',' + mapLayerJson.center.y + ')'
          this.mapLayerInfo.left = mapLayerJson.bounds.left
          this.mapLayerInfo.bottom = mapLayerJson.bounds.bottom
          this.mapLayerInfo.right = mapLayerJson.bounds.right
          this.mapLayerInfo.top = mapLayerJson.bounds.top
          this.mapLayerInfo.prjcoordsys = mapLayerJson.prjCoordSys.name
          this.mapLayerInfo.scale = mapLayerJson.scale.toFixed(10)
        }
      } else {
        this.mapLayerInfo = null
      }
    },
    onSelectChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
    onServiceSelectChange(selectedRowKeys, selectedRows) {
      this.serviceSelectedRowKeys = selectedRowKeys
      this.serviceSelectedRows = selectedRows
    },
    onSubmit() {
      this.onManualSubmit()
      // if (this.activeCard === 'manual') {
      //   this.onManualSubmit()
      // } else if (this.activeCard === 'myServe') {
      //   this.onMyServeSubmit()
      // }
    },
    onManualSubmit() {
      this.form.validateFields(async (err, values) => {
        if (err) {
          return
        }
        const mapServiceInfo = []
        const serviceType = values.serviceType
        const serviceurl = values.address
        const u1 = /rest\/maps\/./
        const u2 = /rest\/realspace\/datas\/./
        if (
          serviceType === 'RESTDATA' ||
          u1.test(serviceurl) ||
          u2.test(serviceurl)
        ) {
          const url1 = decodeURIComponent(values.address)
          const mapService = {
            name: values.serviceName,
            address: url1,
            serverUrl: url1,
            layerName: values.layerName,
            serviceType: values.serviceType,
            description: values.description,
          }
          mapServiceInfo.push(mapService)
        } else {
          for (let i = 0; i < this.selectedRows.length; i++) {
            const url1 = decodeURIComponent(this.selectedRows[i].path)
            const mapService = {
              name: values.serviceName,
              address: url1,
              serverUrl: url1,
              layerName: this.selectedRows[i].name,
              serviceType: values.serviceType,
              description: values.description,
            }
            mapServiceInfo.push(mapService)
          }
        }
        // const url = `/manager/map/service`
        const url = dataUrl.ServiceInfoUrl
        const result = await this.$axios.$post(url, mapServiceInfo)
        if (result.isSuccessed) {
          this.$message.success('操作成功')
          this.$emit('back')
          this.$emit('refresh')
        } else {
          this.$message.error('操作失败')
        }
      })
    },
  },
}
</script>

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
.ant-row {
  padding: 5px 5px 5px 10px;
}
.editable-row-operations a {
  margin-right: 8px;
}
.rbutton {
  width: 90px;
  text-align: center;
}
.gutter-box >>> .ant-table-tbody > tr > td {
  max-width: 100px;
  text-align: left;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  border-bottom: none !important;
}
</style>
