<template>
  <div class="gutter-box">
    <a-row :gutter="16">
      <a-col :span="18">
        <a-divider orientation="left" style="margin: 0px">设置</a-divider>
      </a-col>
    </a-row>
    <a-row>
      <a-col :offset="1" :span="7">地图服务：</a-col>
      <a-col :span="15">
        <span>{{ serviceInfo.name }}</span>
      </a-col>
    </a-row>
    <a-row>
      <a-col :offset="1" :span="7">服务地址：</a-col>
      <a-col :span="15">
        <a :href="serviceInfo.address" class="span-line" target="_blank">
          <span>{{ serviceInfo.address }}</span>
        </a>
      </a-col>
    </a-row>
    <a-row>
      <a-col :offset="1" :span="7">服务类型：</a-col>
      <a-col :span="15">
        <span v-if="showLocation.serviceType === 'RESTMAP'">地图服务</span>
        <span v-if="showLocation.serviceType === 'RESTDATA'">数据服务</span>
      </a-col>
    </a-row>
    <a-row>
      <a-col :offset="1" :span="7">名称：</a-col>
      <a-col :span="15">
        <a-input v-model="showLocation.name" size="small"></a-input>
      </a-col>
    </a-row>
    <a-row>
      <a-col :offset="1" :span="7">定位标识：</a-col>
      <a-col :span="15">
        <a-input v-model="showLocation.locationCode" size="small"></a-input>
      </a-col>
    </a-row>
    <a-row v-show="showLocation.serviceType === 'RESTDATA'">
      <a-col :offset="1" :span="7">数据源：</a-col>
      <a-col :span="15">
        <a-select
          v-model="showLocation.datasourceName"
          style="width: 100%"
          size="small"
          @change="dsChange"
        >
          <a-select-option v-for="item in dsOptions" :key="item">
            {{ item }}
          </a-select-option>
        </a-select>
      </a-col>
    </a-row>
    <a-row>
      <a-col :offset="1" :span="7">数据集：</a-col>
      <a-col :span="15">
        <a-select
          v-model="showLocation.datasetName"
          style="width: 100%"
          size="small"
          @change="dtChange"
        >
          <a-select-option v-for="item in dtOptions" :key="item">
            {{ item }}
          </a-select-option>
        </a-select>
      </a-col>
    </a-row>
    <a-row>
      <a-col :offset="1" :span="7">定位字段：</a-col>
      <a-col :span="15">
        <a-select
          v-model="showLocation.locationField"
          style="width: 100%"
          size="small"
        >
          <a-select-option v-for="item in fieldOptions" :key="item">
            {{ item }}
          </a-select-option>
        </a-select>
      </a-col>
    </a-row>
    <div
      :style="{
        position: 'fixed',
        bottom: 0,
        padding: '10px 16px',
        textAlign: 'right',
        right: 0,
        background: 'rgba(0, 0, 0, 0.0)',
        borderRadius: '0 0 4px 4px',
      }"
    >
      <a-button type="primary" size="small" @click="onSubmit"> 确定 </a-button>
    </div>
  </div>
</template>

<script>
// @ts-nocheck
import { dataUrl, getLocationUrl } from '~/utils/GAFMapDataUtils'
export default {
  model: {
    prop: 'locationInfo',
    event: 'change',
  },
  props: {
    locationInfo: {
      type: Object,
      required: true,
    },
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
      showLocation: {},
      serviceInfo: {},
      dsOptions: [],
      dtOptions: [],
      fieldOptions: [],
      serviceType: '',
      dsName: '',
    }
  },
  watch: {
    locationInfo: {
      handler(val) {
        this.showLocation = val
        this.serviceType = val.serviceType
        const serviceId = this.showLocation.layerserviceId
        this.getLayerService(serviceId)
      },
    },
    immediate: true,
  },
  methods: {
    async getLayerService(serviceId) {
      const url = dataUrl.ServiceInfoUrl + `/${serviceId}`
      const res = await this.$axios.$get(url)
      if (res && res.data) {
        this.serviceInfo = res.data
        const serviceType = this.serviceInfo.serviceType
        this.dsName = this.showLocation.datasourceName
        if (serviceType === 'RESTDATA') {
          this.getDataSources(this.serviceInfo.address)
          this.dsChange(this.showLocation.datasourceName)
          this.dtChange(this.showLocation.datasetName)
        } else if (serviceType === 'RESTMAP') {
          this.getLayers(this.serviceInfo.address)
          this.getFields(
            this.serviceInfo.address,
            this.showLocation.datasetName
          )
        } else if (serviceType === 'RESTMAP3D') {
          this.get3DLayers(this.serviceInfo.address)
          const dataUrl = this.serviceInfo.dataUrl
          const dtName = this.serviceInfo.datasetName
          this.get3DFields(dataUrl, dtName)
        }
      }
    },
    async getDataSources(url) {
      const mapLayerJsonUrl = url + '/data/datasources.jsonp'
      const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
      if (mapLayerJson.datasourceNames) {
        this.dsOptions = [...mapLayerJson.datasourceNames]
      }
    },
    async dsChange(value) {
      const url = this.serviceInfo.address
      this.dsName = value
      const mapLayerJsonUrl = url + `/data/datasources/${value}/datasets.jsonp`
      const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
      if (mapLayerJson.datasetNames) {
        this.dtOptions = [...mapLayerJson.datasetNames]
      }
    },
    async dtChange(value) {
      const url = this.serviceInfo.address
      const serviceType = this.serviceInfo.serviceType
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
        const dataUrl = this.serviceInfo.dataUrl
        const dtName = this.serviceInfo.datasetName
        this.get3DFields(dataUrl, dtName)
      }
    },
    async getLayers(url) {
      const mapLayerJsonUrl = url + '/layers.jsonp'
      const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
      this.dtOptions = []
      if (mapLayerJson.length) {
        if (mapLayerJson[0].subLayers.layers) {
          for (let i = 0; i < mapLayerJson[0].subLayers.layers.length; i++) {
            if (mapLayerJson[0].subLayers.layers[i].queryable) {
              this.dtOptions.push(mapLayerJson[0].subLayers.layers[i].name)
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
            const mapLayerJsonUrl =
              hostUrl + `/data/datasources/${dsName}/datasets.jsonp`
            const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
            if (mapLayerJson.datasetNames) {
              this.dtOptions = []
              for (let i = 0; i < mapLayerJson.datasetNames.length; i++) {
                this.dtOptions.push(mapLayerJson.datasetNames[i])
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
        });
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
    async get3DFields(url, dtName) {
      const mapLayerJsonUrl =
        url + `/data/datasources/${this.dsName}/datasets/${dtName}/fields.jsonp`
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
    async onSubmit() {
      const serviceId = null
      // const url = `/manager/map/${this.mapCode}/service/${serviceId}/locationinfos/${this.showLocation.id}`
      const url =
        getLocationUrl(this.mapCode, serviceId) + `/${this.showLocation.id}`
      const res = await this.$axios.$put(url, this.showLocation)
      if (res.isSuccessed) {
        this.$message.success('编辑成功')
        this.$emit('refresh')
      } else {
        this.$message.error('编辑失败')
      }
    },
  },
}
</script>

<style scoped>
.gutter-box >>> .ant-row {
  padding: 5px 5px 5px 5px;
}
.gutter-box >>> .ant-table {
  padding: 5px 10px 5px 10px;
  table-layout: fixed;
}
.ant-table {
  table-layout: fixed;
}
.gutter-box >>> .ant-table-tbody > tr > td {
  max-width: 95px;
  border-bottom: 0;
  text-align: center !important;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.gutter-box >>> .ant-table-tbody > tr > td:hover {
  white-space: normal;
  overflow: visible;
}
.span-line {
  word-wrap: break-word;
}
</style>
