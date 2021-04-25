<template>
  <a-layout class="layout-background gutter-box">
    <a-layout-sider class="sider-style" :width="350">
      <a-row>
        <a-input-search class="search-style" @search="onSearch" />
      </a-row>
      <a-table
        :columns="columns"
        :row-key="(record) => record.id"
        :data-source="serviceList"
        :pagination="pagination"
        size="small"
        style="min-height: 32vh"
        bordered
        :custom-row="click"
        :row-class-name="setRowClassName"
        @change="handlePaginationChange"
      >
        <template slot="name" slot-scope="text">
          <a-tooltip placement="bottom">
            <template slot="title">
              <span>{{ text }}</span>
            </template>
            <span>{{ text }}</span>
          </a-tooltip>
        </template>
        <template slot="serviceType" slot-scope="text">
          <span v-if="text === 'RESTMAP'">地图服务</span>
          <span v-if="text === 'RESTDATA'">数据服务</span>
          <span v-if="text === 'RESTMAP3D'">三维服务</span>
        </template>
      </a-table>
    </a-layout-sider>
    <a-divider type="vertical" />
    <a-layout>
      <a-layout-content class="layout-background">
        <a-form layout="horizontal" :form="form">
          <a-form-item
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
              @change="change"
            >
              <a-select-option value="RESTDATA">数据服务</a-select-option>
              <a-select-option value="RESTMAP">地图服务</a-select-option>
              <a-select-option value="RESTMAP3D">三维服务</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item
            label="名称"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-input
              v-decorator="[
                'locationName',
                {
                  rules: [
                    {
                      required: true,
                      message: '请输入名称!',
                    },
                  ],
                },
              ]"
              size="small"
            />
          </a-form-item>
          <a-form-item
            v-show="serviceType === 'RESTDATA'"
            label="数据源"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-select
              v-decorator="['datasourceName']"
              size="small"
              allow-clear
              @change="dsChange"
            >
              <a-select-option v-for="item in datasourceOptions" :key="item">
                {{ item }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item
            label="数据集"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-select
              v-decorator="['datasetName']"
              size="small"
              allow-clear
              @change="dtChange"
            >
              <a-select-option v-for="item in datasetOptions" :key="item">
                {{ item }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item
            label="定位字段"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-select
              v-decorator="[
                'locationField',
                {
                  rules: [
                    {
                      required: true,
                      message: '请选择定位字段!',
                    },
                  ],
                },
              ]"
              size="small"
              allow-clear
            >
              <a-select-option v-for="item in fieldOptions" :key="item">
                {{ item }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item
            label="定位标识"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-input
              v-decorator="[
                'locationCode',
                {
                  rules: [
                    {
                      required: true,
                      message: '请输入定位标识!',
                    },
                  ],
                },
              ]"
              size="small"
              @blur="checkedCode"
            >
            </a-input>
          </a-form-item>
          <a-form-item
            v-show="false"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-input v-decorator="['layerserviceId']" size="small" />
          </a-form-item>
          <a-form-item>
            <div v-show="layerInfo !== null">
              <a-row>
                <a-col :offset="1" :span="22">
                  <span>
                    图层名：{{ layerInfo === null ? '' : layerInfo.layerName }}
                  </span>
                </a-col>
              </a-row>
              <a-row>
                <a-col :offset="1" :span="22">
                  <span class="span-line">地址：</span>
                  <a
                    :href="layerInfo === null ? '' : layerInfo.address"
                    target="_blank"
                  >
                    <span class="span-line">
                      {{ layerInfo === null ? '' : layerInfo.address }}
                    </span>
                  </a>
                </a-col>
              </a-row>
              <a-row>
                <a-col :offset="1" :span="22">
                  <span>
                    类型：{{ layerInfo === null ? '' : layerInfo.serviceType }}
                  </span>
                </a-col>
              </a-row>
            </div>
          </a-form-item>
        </a-form>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script>
// @ts-nocheck
import { dataUrl, getLocationUrl } from '~/utils/GAFMapDataUtils'
const columns = [
  {
    title: '名称',
    dataIndex: 'name',
    key: 'name',
    scopedSlots: { customRender: 'name' },
  },
  {
    title: '类型',
    dataIndex: 'serviceType',
    key: 'serviceType',
    scopedSlots: { customRender: 'serviceType' },
  },
]
export default {
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
      labelCol: { span: 5 },
      valueCol: { span: 18 },
      serviceList: [],
      columns,
      pagination: {
        pageSize: 8,
        current: 1,
        total: 8,
        hideOnSinglePage: true,
      },
      layerInfo: null,
      rowId: '',
      serviceType: 'RESTMAP',
      datasourceOptions: [],
      datasetOptions: [],
      fieldOptions: [],
      dsName: '',
      search: '',
      dataUrl: '',
    }
  },
  beforeCreate() {
    this.form = this.$form.createForm(this)
  },
  created() {
    this.$nextTick(() => {
      this.form.setFieldsValue({
        serviceType: this.serviceType,
      })
    })
    this.loadMapService('RESTMAP')
  },
  methods: {
    async loadMapService() {
      const res = await this.$axios.$get(dataUrl.ServiceInfoUrl, {
        params: {
          ServeSearchParameter: {
            pageSize: 8,
            pageIndex: 1,
          },
        },
      })
      for (let i = 0; i < res.content.length; i++) {
        if (res.content[i].serviceType === 'RESTDATA') {
          res.content.splice(i, 1)
          i--
        }
      }
      this.serviceList = [...res.content]
      this.pagination.total = res.total
    },
    async onSearch(value) {
      this.search = value
      const res = await this.$axios.$get(dataUrl.ServiceInfoUrl, {
        params: {
          ServeSearchParameter: {
            key: value === this.serviceType ? null : value,
            pageSize: 8,
            pageIndex: 1,
          },
        },
      })
      if (this.serviceType === 'RESTMAP') {
        for (let i = 0; i < res.content.length; i++) {
          if (res.content[i].serviceType === 'RESTMAP3D') {
            res.content.splice(i, 1)
            i--
          }
        }
      }
      if (res == null) {
        this.$message({
          message: '查询失败',
          type: 'error',
        })
      } else {
        this.serviceList = res.content
        this.pagination.total = this.serviceList.length
      }
    },
    click(record) {
      return {
        on: {
          click: () => {
            this.layerInfo = record
            this.rowId = record.id
            const serviceType = record.serviceType
            this.form.setFieldsValue({
              layerserviceId: record.id,
              serviceType,
            })
            if (serviceType === 'RESTDATA') {
              this.getDataSources(record.address)
            } else if (serviceType === 'RESTMAP') {
              this.getLayers(record.address)
            } else if (serviceType === 'RESTMAP3D') {
              this.get3DLayers(record.address)
            }
          },
        },
      }
    },
    setRowClassName(record) {
      return record.id === this.rowId ? 'clickRowStyl' : ''
    },
    async getDataSources(url) {
      const mapLayerJsonUrl = url + '/data/datasources.jsonp'
      const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
      if (mapLayerJson.datasourceNames) {
        this.datasourceOptions = [...mapLayerJson.datasourceNames]
      }
    },
    async dsChange(value) {
      const url = this.layerInfo.address
      this.dsName = value
      const mapLayerJsonUrl = url + `/data/datasources/${value}/datasets.jsonp`
      const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
      if (mapLayerJson.datasetNames) {
        this.datasetOptions = [...mapLayerJson.datasetNames]
      }
    },
    async dtChange(value) {
      const url = this.layerInfo.address
      const serviceType = this.layerInfo.serviceType
      if (serviceType === 'RESTDATA') {
        const mapLayerJsonUrl =
          url +
          `/data/datasources/${this.dsName}/datasets/${value}/fields.jsonp`
        const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
        if (mapLayerJson.fieldNames) {
          this.fieldOptions = [...mapLayerJson.fieldNames]
        }
      } else if (serviceType === 'RESTMAP') {
        this.getFields(url, value)
      } else if (serviceType === 'RESTMAP3D') {
        this.dataUrl =
          this.dataUrl + `/data/datasources/${this.dsName}/datasets/${value}`
        this.get3DFields(this.dataUrl)
      }
    },
    async getLayers(url) {
      const mapLayerJsonUrl = url + '/layers.jsonp'
      const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
      this.datasetOptions = []
      if (mapLayerJson.length) {
        if (mapLayerJson[0].subLayers.layers) {
          for (let i = 0; i < mapLayerJson[0].subLayers.layers.length; i++) {
            if (mapLayerJson[0].subLayers.layers[i].queryable) {
              this.datasetOptions.push(mapLayerJson[0].subLayers.layers[i].name)
            }
          }
        }
      }
    },
    async get3DLayers(url) {
      if (url.includes('rest')) {
        const urls = url.split('rest')
        if (urls.length > 0) {
          let hostUrl = urls[0]
          if (hostUrl.includes('3D')) {
            const reg = new RegExp('3D', 'g') // g代表全部
            hostUrl = hostUrl.replace(reg, 'data') + 'rest'
            this.dataUrl = hostUrl
            let dsName = ''
            if (url.includes('datas')) {
              dsName = url.substr(url.lastIndexOf('/') + 1)
            }
            this.dsName = dsName
            this.form.setFieldsValue({
              datasourceName: dsName,
            })
            const mapLayerJsonUrl =
              hostUrl + `/data/datasources/${dsName}/datasets.jsonp`
            const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
            if (mapLayerJson.datasetNames) {
              this.datasetOptions = []
              for (let i = 0; i < mapLayerJson.datasetNames.length; i++) {
                this.datasetOptions.push(mapLayerJson.datasetNames[i])
              }
            }
          }
        }
      }
    },
    getFields(url, name) {
      return new Promise(() => {
        const queryParam = new SuperMap.REST.FilterParameter({
          name,
          attributeFilter: 'SMID>1',
        })
        const queryBySQLParams = new SuperMap.REST.QueryBySQLParameters({
          queryParams: [queryParam],
          queryOption: SuperMap.REST.QueryOption.ATTRIBUTE,
          expectCount: 1,
        })
        const queryBySQLService = new SuperMap.REST.QueryBySQLService(url, {
          eventListeners: {
            processCompleted: this.processCompleted,
            processFailed: this.processFailed,
          },
        })
        queryBySQLService.processAsync(queryBySQLParams)
      })
    },
    processCompleted(queryEventArgs) {
      const result = queryEventArgs.result
      if (result && result.recordsets) {
        const recordset = result.recordsets[0]
        if (recordset && recordset.fields) {
          this.fieldOptions = recordset.fields
        }
      }
    },
    processFailed() {},
    async get3DFields(url) {
      const mapLayerJsonUrl = url + `/fields.jsonp`
      const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
      if (mapLayerJson.childUriList && mapLayerJson.childUriList.length) {
        this.fieldOptions = []
        for (let i = 0; i < mapLayerJson.childUriList.length; i++) {
          const fieldUrl = mapLayerJson.childUriList[i] + '.jsonp'
          const field = await this.$axios.jsonp(fieldUrl)
          this.fieldOptions.push(field.fieldInfo.name)
        }
      }
    },
    async handlePaginationChange(pagination) {
      const res = await this.$axios.$get(dataUrl.ServiceInfoUrl, {
        params: {
          ServeSearchParameter: {
            key: this.search === '' ? this.serviceType : this.search,
            pageIndex: pagination.current,
            pageSize: pagination.pageSize,
          },
        },
      })
      this.serviceList = [...res.content]
      this.pagination.current = pagination.current
      this.pagination.total = res.total
    },
    change(value) {
      this.serviceType = value
      this.datasourceOptions = []
      this.datasetOptions = []
      this.fieldOptions = []
      this.loadMapService(value)
    },
    async checkedCode(e) {
      const code = e.target.value
      const serviceId = null
      const url =
        getLocationUrl(this.mapCode, serviceId) + `/ishavecode/${code}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        const num = parseInt(res.data)
        if (num > 0) {
          const arr = [
            {
              message: '定位标识重复',
              field: 'locationCode',
            },
          ]
          this.form.setFields({
            locationCode: { value: code, errors: arr },
          })
        }
      }
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
  max-width: 350px;
  min-height: 425px;
  max-height: 425px;
  background: #ffffff;
}
.layout-background {
  background: #ffffff;
}
.settingStyle {
  max-height: 400px;
  overflow-y: auto;
  overflow-x: hidden;
}
.search-style {
  width: 100%;
  padding: 0 0 10px;
}
.gutter-box >>> .ant-table-tbody > tr > td {
  max-width: 95px;
  border-bottom: 0;
  text-align: center !important;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
