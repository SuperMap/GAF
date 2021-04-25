<template>
  <a-spin :spinning="spinning">
    <div class="gutter-box">
      <div>
        <div>
          <a-row :gutter="16">
            <a-col :span="7">
              <span>标题</span>
            </a-col>
            <a-col :span="16">
              <a-input
                v-model="visualizationInfo.title"
                size="small"
                @change="change"
              />
            </a-col>
          </a-row>
          <a-row
            v-show="showFunction.actionOptionType === 'RESTDATA'"
            :gutter="16"
          >
            <a-col :span="7">
              <span>数据源</span>
            </a-col>
            <a-col :span="16">
              <a-select
                v-model="visualizationInfo.dataSourceName"
                show-search
                option-filter-prop="children"
                size="small"
                style="width: 100%"
                @change="dsChange"
              >
                <a-select-option v-for="item in dsOptions" :key="item">
                  {{ item }}
                </a-select-option>
              </a-select>
            </a-col>
          </a-row>
          <a-row
            v-show="showFunction.actionOptionType === 'RESTDATA'"
            :gutter="16"
          >
            <a-col :span="7">
              <span>数据集</span>
            </a-col>
            <a-col :span="16">
              <a-select
                v-model="visualizationInfo.datasetName"
                show-search
                option-filter-prop="children"
                size="small"
                style="width: 100%"
                @change="dtChange"
              >
                <a-select-option v-for="item in dtOptions" :key="item">
                  {{ item }}
                </a-select-option>
              </a-select>
            </a-col>
          </a-row>
          <a-row :gutter="16">
            <a-col :span="24">
              <a-table
                :pagination="false"
                :columns="columns"
                :data-source="data"
                :row-key="(record) => record.name"
                :scroll="{ y: 200 }"
                :row-selection="{
                  columnTitle: '显示',
                  selectedRowKeys: selectedRowKeys,
                  onChange: onSelectedChange,
                }"
              />
            </a-col>
          </a-row>
        </div>
        <a-row :gutter="16">
          <a-col :span="7">
            <span>要素识别</span>
          </a-col>
          <a-col :span="16">
            <a-switch v-model="showProperty" size="small">
              <a-icon slot="checkedChildren" type="check" />
              <a-icon slot="unCheckedChildren" type="close" />
            </a-switch>
          </a-col>
        </a-row>
        <a-row v-show="showProperty" :gutter="16">
          <a-col :span="24">
            <PropertySetting
              :fields-info="fieldsInfo"
              @propertyInfo="getProperty"
            />
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="7">
            <span>标签</span>
          </a-col>
          <a-col :span="16">
            <a-switch v-model="showLabel" size="small">
              <a-icon slot="checkedChildren" type="check" />
              <a-icon slot="unCheckedChildren" type="close" />
            </a-switch>
          </a-col>
        </a-row>
        <a-row v-show="showLabel" :gutter="16">
          <a-col :span="24">
            <LabelLayerSetting
              :label-setting-info="labelInfo"
              @labelInfo="getLabel"
            />
          </a-col>
        </a-row>
      </div>
    </div>
  </a-spin>
</template>

<script>
// @ts-nocheck
import LabelLayerSetting from './LabelLayerSetting'
import PropertySetting from './PropertySetting'
const columns = [
  {
    title: '字段别名',
    dataIndex: 'name',
    key: 'name',
    scopedSlots: { customRender: 'name' },
  },
]
export default {
  components: {
    LabelLayerSetting,
    PropertySetting,
  },
  model: {
    prop: 'functionSetting',
    event: 'change',
  },
  props: {
    functioninfo: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      showProperty: false,
      showLabel: false,
      showFunction: {},
      visualizationInfo: {
        title: '',
        dataSourceName: '',
        datasetName: '',
        fields: [],
        property: {},
        label: {},
      },
      selectedRowKeys: [],
      selectedRows: [],
      dsOptions: [],
      dtOptions: [],
      columns,
      data: [],
      fieldsInfo: {
        allFields: [],
        title: '',
        fields: [],
      },
      labelInfo: {
        allFields: [],
        field: '',
        color: '',
        font: '',
      },
      spinning: true,
    }
  },
  watch: {
    functioninfo: {
      async handler(val) {
        Object.assign(this.$data, this.$options.data())
        this.showFunction = val
        if (val.visualizationInfo) {
          const jsonString = val.visualizationInfo.dataVisualizationSetting
          if (jsonString) {
            this.visualizationInfo = JSON.parse(jsonString)
            if (this.visualizationInfo.property) {
              this.fieldsInfo.title = this.visualizationInfo.property.title
              this.fieldsInfo.fields = this.visualizationInfo.property.fields
            }
            if (this.visualizationInfo.label) {
              this.labelInfo.field = this.visualizationInfo.label.field
              this.labelInfo.color = this.visualizationInfo.label.color
              this.labelInfo.font = this.visualizationInfo.label.font
            }
          }
        }
        const url = val.actionURL
        if (this.showFunction.actionOptionType === 'RESTDATA') {
          const mapLayerJsonUrl = url + '/data/datasources.jsonp'
          const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
          if (mapLayerJson.datasourceNames) {
            this.dsOptions = [...mapLayerJson.datasourceNames]
          }
          if (this.visualizationInfo.dataSourceName) {
            const dsName = this.visualizationInfo.dataSourceName
            this.dsName = dsName
            const mapLayerJsonUrl =
              url + `/data/datasources/${dsName}/datasets.jsonp`
            const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
            if (mapLayerJson.datasetNames) {
              this.dtOptions = [...mapLayerJson.datasetNames]
            }
          }
          this.showPropertyAndLabel()
          this.spinning = false
        } else if (this.showFunction.actionOptionType === 'RESTAPI') {
          const res = await this.$axios.$get(url)
          if (res.isSuccessed) {
            const mapLayerInfo = res.data
            this.showPropertyAndLabel2(mapLayerInfo)
          }
          this.spinning = false
        }
      },
      immediate: true,
    },
  },
  created() {},
  methods: {
    change() {
      this.emitHeatMapSettingInfo()
    },
    onSelectedChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.visualizationInfo.fields = selectedRows
      this.emitHeatMapSettingInfo()
    },
    async dsChange(value) {
      const url = this.showFunction.actionURL
      this.dsName = value
      const mapLayerJsonUrl = url + `/data/datasources/${value}/datasets.jsonp`
      const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
      this.dtOptions = [...mapLayerJson.datasetNames]
    },
    async dtChange(value) {
      const url = this.showFunction.actionURL
      const mapLayerJsonUrl =
        url + `/data/datasources/${this.dsName}/datasets/${value}/fields.jsonp`
      const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
      if (mapLayerJson.fieldNames) {
        this.data = []
        const fieldNames = mapLayerJson.fieldNames
        if (fieldNames.length) {
          for (let i = 0; i < fieldNames.length; i++) {
            const item = {
              name: fieldNames[i],
            }
            this.data.push(item)
          }
        }
        this.data = [...this.data]
        this.fieldsInfo.allFields = fieldNames
        this.fieldsInfo = { ...this.fieldsInfo }
        this.labelInfo.allFields = fieldNames
        this.labelInfo = { ...this.labelInfo }
      }
    },
    async showPropertyAndLabel() {
      if (
        this.visualizationInfo.dataSourceName !== '' &&
        this.visualizationInfo.datasetName !== ''
      ) {
        const url = this.showFunction.actionURL
        const dsName = this.visualizationInfo.dataSourceName
        const dtName = this.visualizationInfo.datasetName
        const mapLayerJsonUrl =
          url + `/data/datasources/${dsName}/datasets/${dtName}/fields.jsonp`
        const mapLayerJson = await this.$axios.jsonp(mapLayerJsonUrl)
        if (mapLayerJson.fieldNames) {
          const allField = [...mapLayerJson.fieldNames]
          this.data = []
          for (let i = 0; i < allField.length; i++) {
            const item = {
              name: allField[i],
            }
            this.data.push(item)
          }
          this.selectedRows = this.visualizationInfo.fields
          this.fieldsInfo.allFields = allField
          this.fieldsInfo = { ...this.fieldsInfo }
          this.labelInfo.allFields = allField
          this.labelInfo = { ...this.labelInfo }
        }
      }
    },
    showPropertyAndLabel2(mapLayerInfo) {
      if (mapLayerInfo && mapLayerInfo.length) {
        const attributes = mapLayerInfo[0].attributes
        const strAttr = JSON.stringify(attributes)
        const arrAttr = strAttr.slice(1, strAttr.length - 1).split(',')
        if (arrAttr.length > 0) {
          const allField = []
          for (let i = 0; i < arrAttr.length; i++) {
            const arr = arrAttr[i].split(':')
            const fName = JSON.parse(arr[0])
            allField.push(fName)
          }
          this.data = []
          for (let i = 0; i < allField.length; i++) {
            const item = {
              name: allField[i],
            }
            this.data.push(item)
          }
          this.selectedRows = this.visualizationInfo.fields
          this.fieldsInfo.allFields = allField
          this.fieldsInfo = { ...this.fieldsInfo }
          this.labelInfo.allFields = allField
          this.labelInfo = { ...this.labelInfo }
        }
      }
    },
    getProperty(value) {
      this.visualizationInfo.property = value
      this.emitHeatMapSettingInfo()
    },
    getLabel(value) {
      this.visualizationInfo.label = value
      this.emitHeatMapSettingInfo()
    },
    emitHeatMapSettingInfo() {
      this.$emit('propertyTableInfo', this.visualizationInfo)
    },
  },
}
</script>

<style scoped></style>
