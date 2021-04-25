<template>
  <div class="gutter-box">
    <a-row :gutter="16">
      <a-breadcrumb separator=">">
        <a-breadcrumb-item>
          <a @click="$emit('back')">设置</a>
        </a-breadcrumb-item>
        <a-breadcrumb-item>查询设置</a-breadcrumb-item>
      </a-breadcrumb>
    </a-row>
    <a-row>
      <a-col :offset="1" :span="7">地图服务：</a-col>
      <a-col :span="15">
        <span>{{ serviceName }}</span>
      </a-col>
    </a-row>
    <a-row>
      <a-col :offset="1" :span="7">服务地址：</a-col>
      <a-col :span="15">
        <a :href="selectionInfo.serviceUrl" class="span-line" target="_blank">
          <span>{{ selectionInfo.serviceUrl }}</span>
        </a>
      </a-col>
    </a-row>
    <a-row>
      <a-col :offset="1" :span="7">名称：</a-col>
      <a-col :span="15">
        <a-input v-model="selectionInfo.name" size="small"></a-input>
      </a-col>
    </a-row>
    <a-row>
      <a-col :offset="1" :span="7">选择子图层：</a-col>
      <a-col :span="15">
        <a-select
          v-model="selectionInfo.subLayerName"
          style="width: 100%"
          size="small"
          @change="onSelectChange1"
        >
          <a-select-option v-for="item in layerOptions" :key="item.name">
            {{ item.caption }}
          </a-select-option>
        </a-select>
      </a-col>
    </a-row>
    <a-row v-if="selectionInfo.type === 'Space'">
      <a-col :offset="1" :span="7">选择样式：</a-col>
      <a-col :span="15">
        <a-radio-group
          v-model="selectionInfo.style"
          size="small"
          :options="radioOptions"
        />
      </a-col>
    </a-row>
    <a-row
      v-if="selectionInfo.type === 'Space' && selectionInfo.style === 'style2'"
    >
      <a-col :offset="1" :span="7">标题字段：</a-col>
      <a-col :span="15">
        <a-select
          v-model="title"
          size="small"
          style="width: 100%"
          @change="titleChange"
        >
          <a-select-option v-for="item in titleFields" :key="item.name">
            {{ item.caption }}
          </a-select-option>
        </a-select>
      </a-col>
    </a-row>
    <a-table
      :loading="loading"
      :columns="fieldColumns"
      :data-source="fieldDataSource"
      :scroll="{ y: 190 }"
      :row-key="(record) => record.name"
      :row-selection="{
        columnTitle: columnTitle,
        selectedRowKeys: selectedRowKeys,
        selectedRows: selectedRows,
        onChange: onSelectChange2,
      }"
      :pagination="false"
      style="max-width: 100%"
      bordered
      size="small"
    >
      <template slot="caption" slot-scope="text">
        <a-tooltip placement="bottom">
          <template slot="title">
            <span>{{ text }}</span>
          </template>
          <span>{{ text }}</span>
        </a-tooltip>
      </template>
      <template slot="value" slot-scope="text">
        <a-tooltip placement="bottom">
          <template slot="title">
            <span>{{ text }}</span>
          </template>
          <span>{{ text }}</span>
        </a-tooltip>
      </template>
    </a-table>
    <a-row v-if="selectionInfo.type === 'Attribute'">
      <a-col :offset="1" :span="7">选择样式：</a-col>
      <a-col :span="15">
        <a-radio-group
          v-model="selectionInfo.style"
          size="small"
          :options="radioOptions"
        />
      </a-col>
    </a-row>
    <div v-if="selectionInfo.type === 'Attribute'">
      <a-row
        v-if="selectionInfo.style === 'style2'"
        style="margin: 5px; text-align: right"
      >
        <a-col span="7">
          <span>选择标题：</span>
        </a-col>
        <a-col span="16">
          <a-select
            v-model="title"
            size="small"
            style="width: 100%"
            @change="titleChange"
          >
            <a-select-option v-for="item in titleFields" :key="item.name">
              {{ item.caption }}
            </a-select-option>
          </a-select>
        </a-col>
      </a-row>
      <a-table
        :loading="loading"
        :columns="fieldColumns"
        :data-source="showData"
        :scroll="{ y: 190 }"
        :row-key="(record) => record.name"
        :row-selection="{
          columnTitle: '显示字段',
          selectedRowKeys: showTableSelectedRowKeys,
          selectedRows: showFieldRows,
          onChange: onSelectChange3,
        }"
        :pagination="false"
        style="max-width: 100%"
        bordered
        size="small"
      >
        <template slot="caption" slot-scope="text">
          <a-tooltip placement="bottom">
            <template slot="title">
              <span>{{ text }}</span>
            </template>
            <span>{{ text }}</span>
          </a-tooltip>
        </template>
        <template slot="value" slot-scope="text">
          <a-tooltip placement="bottom">
            <template slot="title">
              <span>{{ text }}</span>
            </template>
            <span>{{ text }}</span>
          </a-tooltip>
        </template>
      </a-table>
    </div>
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
import { dataUrl, getSelectionUrl } from '~/utils/GAFMapDataUtils'
const radioOptions = [
  { label: '属性表', value: 'style1' },
  { label: '列表', value: 'style2' },
]
const fieldColumns = [
  {
    title: '字段别名',
    dataIndex: 'caption',
    key: 'caption',
    width: '36%',
    scopedSlots: { customRender: 'caption' },
  },
  {
    title: '字段值',
    dataIndex: 'value',
    key: 'value',
    width: '36%',
    scopedSlots: { customRender: 'value' },
  },
]
export default {
  model: {
    prop: 'layerServiceInfo',
    event: 'change',
  },
  props: {
    layerServiceInfo: {
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
      fieldColumns,
      selectionInfo: {
        style: 'style1',
      },
      radioOptions,
      radioValue: '',
      layerOptions: [],
      fieldDataSource: [],
      selectedRowKeys: [],
      selectedRows: [],
      showFieldRows: [],
      selectionRows: [],
      loading: false,
      title: '',
      titleFields: [],
      titleField: {},
      serviceName: '',
      showData: [],
      showTableSelectedRowKeys: [],
      columnTitle: '显示字段',
    }
  },
  watch: {
    layerServiceInfo(val) {
      Object.assign(this.$data, this.$options.data())
      this.selectionInfo = { ...val }
      const url = this.selectionInfo.serviceUrl
      const serviceId = this.selectionInfo.layerserviceId
      const dataUrl = this.selectionInfo.dataUrl
      this.getLayerService(serviceId)
      if (dataUrl) {
        const dsName = url.substr(url.lastIndexOf('/') + 1)
        this.get3DLayers(dataUrl, dsName)
        this.get3DFields(dataUrl, dsName, val.subLayerName)
      } else {
        this.getLayers(url)
        this.getFields(url, val.subLayerName)
      }
      if (val.fields) {
        this.selectedRowKeys = []
        if (val.type === 'Attribute') {
          this.columnTitle = '查询字段'
        } else {
          this.columnTitle = '显示字段'
        }
        for (let i = 0; i < val.fields.length; i++) {
          const field = val.fields[i]
          if (val.type !== 'Attribute' && field.showType !== 'title') {
            this.selectedRowKeys.push(field.name)
          }
          if (val.type === 'Attribute' && field.showType === 'query') {
            this.selectedRowKeys.push(field.name)
            this.selectedRows.push(field)
          }
          if (field.showType === 'des') {
            this.showTableSelectedRowKeys.push(field.name)
            this.showFieldRows.push(field)
          }
          if (field.showType !== 'title') {
            this.title = field.name
            this.titleField = field
          }
        }
      }
    },
  },
  methods: {
    // 表格选择修改
    handleTableChange(pagination) {
      const pager = { ...this.pagination }
      pager.current = pagination.current
      this.pagination = pager
    },
    async getLayerService(serviceId) {
      const res = await this.$axios.$get(
        dataUrl.ServiceInfoUrl + `/${serviceId}`
      )
      if (res && res.data) {
        this.serviceName = res.data.name
      }
    },
    async getLayers(url) {
      const mapLayerJsonUrl = url + '/layers.jsonp'
      const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
      this.layerOptions = []
      if (mapLayerJson[0].subLayers.layers) {
        for (let i = 0; i < mapLayerJson[0].subLayers.layers.length; i++) {
          if (mapLayerJson[0].subLayers.layers[i].queryable) {
            this.layerOptions.push(mapLayerJson[0].subLayers.layers[i])
          }
        }
      }
    },
    async get3DLayers(url) {
      const mapLayerJsonUrl = url.substr(0, url.lastIndexOf('/')) + '.jsonp'
      const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
      if (mapLayerJson.datasetNames) {
        this.layerOptions = []
        for (let i = 0; i < mapLayerJson.datasetNames.length; i++) {
          const item = {
            name: mapLayerJson.datasetNames[i],
            caption: mapLayerJson.datasetNames[i],
          }
          this.layerOptions.push(item)
        }
      }
    },
    getFields(url, name) {
      this.loading = true
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
        const queryBySQLService = new SuperMap.REST.QueryBySQLService(
          decodeURIComponent(url),
          {
            eventListeners: {
              processCompleted: this.processCompleted,
              processFailed: this.processFailed,
            },
          }
        )
        queryBySQLService.processAsync(queryBySQLParams)
      })
    },
    processCompleted(queryEventArgs) {
      const result = queryEventArgs.result
      if (result && result.recordsets) {
        const recordset = result.recordsets[0]
        if (recordset && recordset.fields) {
          this.fieldDataSource = []
          for (let i = 0; i < recordset.fields.length; i++) {
            const fieldinfo = {}
            fieldinfo.name = recordset.fields[i]
            fieldinfo.type = recordset.fieldTypes[i]
            fieldinfo.caption = recordset.fieldCaptions[i]
            this.fieldDataSource.push(fieldinfo)
          }
          if (recordset.features && recordset.features.length > 0) {
            for (let i = 0; i < this.fieldDataSource.length; i++) {
              this.fieldDataSource[i].value =
                recordset.features[0].data[this.fieldDataSource[i].name]
            }
          }
          this.titleFields = JSON.parse(JSON.stringify(this.fieldDataSource))
          this.showData = JSON.parse(JSON.stringify(this.fieldDataSource))
        }
      }
      this.loading = false
    },
    async get3DFields(url) {
      const mapLayerJsonUrl = url + `/fields.jsonp`
      const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
      if (mapLayerJson.childUriList && mapLayerJson.childUriList.length) {
        this.fieldDataSource = []
        for (let i = 0; i < mapLayerJson.childUriList.length; i++) {
          const fieldUrl = mapLayerJson.childUriList[i] + '.jsonp'
          const field = await this.$axios.jsonp(fieldUrl)
          const fieldInfo = {}
          fieldInfo.name = field.fieldInfo.name
          fieldInfo.type = field.fieldInfo.type
          fieldInfo.caption = field.fieldInfo.caption
          fieldInfo.value = field.fieldInfo.defaultValue
          this.fieldDataSource.push(fieldInfo)
        }
        this.titleFields = JSON.parse(JSON.stringify(this.fieldDataSource))
        this.showData = JSON.parse(JSON.stringify(this.fieldDataSource))
      }
    },
    processFailed() {
      this.loading = false
    },
    // 选择子图层改变
    onSelectChange1(value) {
      this.getFields(this.selectionInfo.serviceUrl, value)
    },
    // 选择字段
    onSelectChange2(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.setShowType(this.selectedRows, 'query')
    },
    onSelectChange3(selectedRowKeys, selectedRows) {
      this.showTableSelectedRowKeys = selectedRowKeys
      this.showFieldRows = selectedRows
      this.setShowType(this.showFieldRows, 'des')
    },
    setShowType(array, type) {
      if (array && array.length) {
        for (let i = 0; i < array.length; i++) {
          array[i].showType = type
        }
      }
    },
    async onSubmit() {
      this.selectionInfo.fields = []
      if (this.selectionInfo.type === 'Feature') {
        this.selectionInfo.fields.push(...this.selectedRows)
      }
      if (this.selectionInfo.type === 'Space') {
        this.selectionInfo.fields.push(...this.selectedRows)
        if (this.selectionInfo.fields && this.selectionInfo.fields.length) {
          for (let i = 0; i < this.selectionInfo.fields.length; i++) {
            this.selectionInfo.fields[i].showType = 'des'
          }
        }
        if (
          this.selectionInfo.style === 'style2' &&
          this.titleField &&
          this.titleField.name
        ) {
          this.selectionInfo.fields.push(this.titleField)
        }
      }
      if (this.selectionInfo.type === 'Attribute') {
        this.selectionInfo.fields.push(...this.selectedRows)
        this.selectionInfo.fields.push(...this.showFieldRows)
        this.selectionInfo.fields.push(this.titleField)
      }
      const serviceId = null
      const url =
        getSelectionUrl(this.mapCode, serviceId) + `/${this.selectionInfo.id}`
      const res = await this.$axios.$put(url, this.selectionInfo)
      if (res.isSuccessed) {
        this.$message.success('编辑成功')
        this.$emit('refresh')
      } else {
        this.$message.error('编辑失败')
      }
    },
    titleChange(value) {
      if (this.titleFields.length) {
        for (let i = 0; i < this.titleFields.length; i++) {
          if (this.titleFields[i].name === value) {
            this.titleField.name = this.titleFields[i].name
            this.titleField.type = this.titleFields[i].type
            this.titleField.caption = this.titleFields[i].caption
            break
          }
        }
        this.titleField.showType = 'title'
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
.span-line {
  word-wrap: break-word;
}
</style>
