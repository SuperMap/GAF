<template>
  <a-layout class="layout-background">
    <a-layout-sider :width="350" class="sider-style">
      <div class="settingStyle">
        <a-directory-tree
          :tree-data="treeData"
          check-strictly
          size="small"
          class="tree-font"
          @select="onTreeNodeSelect"
        />
      </div>
    </a-layout-sider>
    <a-layout>
      <a-layout-content class="layout-background">
        <a-form layout="horizontal" :form="form">
          <a-form-item
            label="名称"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-input
              v-decorator="[
                'selectionName',
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
            label="子图层"
            :label-col="labelCol"
            :wrapper-col="valueCol"
          >
            <a-select
              v-decorator="[
                'subLayerName',
                {
                  rules: [
                    {
                      required: true,
                      message: '请选择子图层!',
                    },
                  ],
                },
              ]"
              size="small"
              @change="onSelectChange1"
            >
              <a-select-option v-for="item in layerOptions" :key="item.name">
                {{ item.caption }}
              </a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item v-show="false">
            <a-input v-decorator="['layerserviceId']" size="small" />
          </a-form-item>
          <a-form-item v-show="false">
            <a-input v-decorator="['serviceUrl']" size="small" />
          </a-form-item>
        </a-form>
        <a-tabs
          :active-key="activeKey"
          tab-position="top"
          @change="activeChange"
        >
          <a-tab-pane key="1" tab="查询属性">
            <a-table
              :loading="loading"
              :columns="fieldColumns"
              :data-source="fieldDataSource"
              :scroll="{ y: 220 }"
              :row-key="(record) => record.name"
              :row-selection="{
                columnTitle: '查询字段',
                selectedRowKeys: selectedRowKeys,
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
          </a-tab-pane>
          <a-tab-pane key="2" tab="显示内容">
            <a-row>
              <a-form-item
                label="选择样式"
                :label-col="labelCol"
                :wrapper-col="valueCol"
              >
                <a-radio-group
                  v-model="style"
                  size="small"
                  :options="radioOptions"
                />
              </a-form-item>
            </a-row>
            <a-row
              v-show="style === 'style2'"
              style="margin: 5px; text-align: right"
            >
              <a-col span="5">
                <span>选择标题：</span>
              </a-col>
              <a-col span="18">
                <a-select
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
              :scroll="{ y: 170 }"
              :row-key="(record) => record.name"
              :row-selection="{
                columnTitle: '显示字段',
                selectedRowKeys: showTableSelectedRowKeys,
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
          </a-tab-pane>
        </a-tabs>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script>
// @ts-nocheck
import { getLayerCatalogUrl } from '~/utils/GAFMapDataUtils'
const radioOptions = [
  { label: '属性表', value: 'style1' },
  { label: '列表', value: 'style2' },
]
const fieldColumns = [
  {
    title: '字段别名',
    dataIndex: 'caption',
    key: 'caption',
    width: '35%',
    scopedSlots: { customRender: 'caption' },
  },
  {
    title: '字段值',
    dataIndex: 'value',
    key: 'value',
    width: '46%',
    scopedSlots: { customRender: 'value' },
  },
]
export default {
  props: {
    refresh: {
      type: Boolean,
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
      treeData: [],
      labelCol: { span: 5 },
      valueCol: { span: 18 },
      selectedLayerInfo: {},
      serviceInfo: {},
      layerOptions: [],
      fieldDataSource: [],
      selectedRowKeys: [],
      showTableSelectedRowKeys: [],
      selectedRows: [],
      showFieldRows: [],
      loading: false,
      showData: [],
      titleFields: [],
      titleField: {},
      activeKey: '1',
      style: 'style1',
      radioOptions,
      dataUrl: '',
      dsName: '',
    }
  },
  watch: {
    refresh: {
      handler(val) {
        Object.assign(this.$data, this.$options.data())
        this.loadServiceList()
        this.activeKey = '1'
      },
    },
    immediate: true,
  },
  beforeCreate() {
    this.form = this.$form.createForm(this)
  },
  created() {
    this.loadServiceList()
  },
  methods: {
    async loadServiceList() {
      const url = getLayerCatalogUrl(this.mapCode)
      const res = await this.$axios.$get(url)
      if (res && res.data) {
        this.setTreeDisabled(res.data)
        this.treeData = [...res.data]
      }
    },
    setTreeDisabled(list) {
      if (list.length > 0) {
        for (let i = 0; i < list.length; i++) {
          if (list[i].scopedSlots.icon === 'directory') {
            // list[i].disabled = true
            list[i].selectable = false
          }
          if (list[i].scopedSlots.icon === 'layer') {
            list[i].isLeaf = true
          }
          if (list[i].children.length > 0) {
            this.setTreeDisabled(list[i].children)
          }
        }
      }
    },
    onTreeNodeSelect(selectedKeys, info) {
      this.selectedLayerInfo = info.node.$options.propsData.dataRef.slots
      this.serviceInfo = this.selectedLayerInfo.layerInfo
      const url = this.serviceInfo.address
      this.form.setFieldsValue({
        layerserviceId: this.serviceInfo.id,
        serviceUrl: url,
      })
      const serviceType = this.serviceInfo.serviceType
      this.layerOptions = []
      this.fieldDataSource = []
      if (serviceType === 'RESTMAP') {
        this.getLayers(url)
      } else if (serviceType === 'RESTMAP3D') {
        this.get3DLayers(url)
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
              this.layerOptions = []
              for (let i = 0; i < mapLayerJson.datasetNames.length; i++) {
                const item = {
                  name: mapLayerJson.datasetNames[i],
                  caption: mapLayerJson.datasetNames[i],
                }
                this.layerOptions.push(item)
              }
            }
          }
        }
      }
    },
    async getLayers(url) {
      const mapLayerJsonUrl = url + '/layers.jsonp'
      const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
      this.layerOptions = []
      if (mapLayerJson.length) {
        if (mapLayerJson[0].subLayers.layers) {
          for (let i = 0; i < mapLayerJson[0].subLayers.layers.length; i++) {
            if (mapLayerJson[0].subLayers.layers[i].queryable) {
              this.layerOptions.push(mapLayerJson[0].subLayers.layers[i])
            }
          }
        }
      }
    },
    onSelectChange1(value) {
      const serviceType = this.serviceInfo.serviceType
      if (serviceType === 'RESTMAP') {
        this.getFields(this.serviceInfo.address, value)
      } else if (serviceType === 'RESTMAP3D') {
        this.dataUrl =
          this.dataUrl + `/data/datasources/${this.dsName}/datasets/${value}`
        this.get3DFields(this.dataUrl, value)
      }
    },
    async get3DFields(url) {
      const mapLayerJsonUrl = url + `/fields.jsonp`
      const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
      if (mapLayerJson.childUriList && mapLayerJson.childUriList.length) {
        for (let i = 0; i < mapLayerJson.childUriList.length; i++) {
          const fieldUrl = mapLayerJson.childUriList[i] + '.jsonp'
          const field = await this.$axios.jsonp(fieldUrl)
          const fieldInfo = {}
          fieldInfo.name = field.fieldInfo.name
          fieldInfo.type = field.fieldInfo.type
          fieldInfo.caption = field.fieldInfo.caption
          fieldInfo.value = field.fieldInfo.defaultValue
          this.fieldDataSource.push(fieldInfo)
          this.showTableSelectedRowKeys.push(fieldInfo.name)
        }
        this.setTableFieldInfos()
      }
    },
    onSelectChange2(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = [...selectedRows]
      this.setShowType(this.selectedRows, 'query')
    },
    onSelectChange3(selectedRowKeys, selectedRows) {
      this.showTableSelectedRowKeys = selectedRowKeys
      this.showFieldRows = [...selectedRows]
      this.setShowType(this.showFieldRows, 'des')
    },
    setShowType(array, type) {
      if (array && array.length) {
        for (let i = 0; i < array.length; i++) {
          array[i].showType = type
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
            this.showTableSelectedRowKeys.push(fieldinfo.name)
          }
          if (recordset.features && recordset.features.length > 0) {
            for (let i = 0; i < this.fieldDataSource.length; i++) {
              this.fieldDataSource[i].value =
                recordset.features[0].data[this.fieldDataSource[i].name]
            }
          }
        }
      }
      this.setTableFieldInfos()
      this.loading = false
    },
    setTableFieldInfos() {
      this.showData = JSON.parse(JSON.stringify(this.fieldDataSource))
      this.showFieldRows = JSON.parse(JSON.stringify(this.fieldDataSource))
      this.setShowType(this.showFieldRows, 'des')
      this.titleFields = JSON.parse(JSON.stringify(this.fieldDataSource))
      this.titleField = JSON.parse(JSON.stringify(this.fieldDataSource[0]))
      this.titleField.showType = 'title'
    },
    processFailed() {
      this.loading = false
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
    activeChange(key) {
      this.activeKey = key
    },
  },
}
</script>

<style scoped>
.sider-style {
  max-width: 350px;
  min-height: 400px;
  max-height: 400px;
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
</style>
