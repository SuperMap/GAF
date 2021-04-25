<template>
  <a-spin :spinning="spinning">
    <div class="gutter-box spin-content">
      <div>
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
          <a-col :span="7">
            <span>专题字段</span>
          </a-col>
          <a-col :span="16">
            <a-select
              v-model="visualizationInfo.themeField"
              show-search
              option-filter-prop="children"
              size="small"
              style="width: 100%"
              @change="fieldChange"
            >
              <a-select-option v-for="item in fieldOptions" :key="item">
                {{ item }}
              </a-select-option>
            </a-select>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="7">
            <span>填充色</span>
          </a-col>
          <a-col :span="16">
            <color-picker2
              v-model="color2Value"
              :count="colorCount"
              @change="onChange"
            />
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="7">
            <span>自定义颜色</span>
          </a-col>
          <a-col :span="16">
            <a-switch v-model="customColor" size="small">
              <a-icon slot="checkedChildren" type="check" />
              <a-icon slot="unCheckedChildren" type="close" />
            </a-switch>
          </a-col>
        </a-row>
        <a-row v-show="customColor" :gutter="16">
          <a-col :span="24">
            <a-table
              :pagination="false"
              :columns="columns"
              :data-source="data"
              :row-key="(record) => record.fieldValue"
              :scroll="{ y: 200 }"
            >
              <template slot="color" slot-scope="text, record">
                <color-picker
                  v-model="record.color"
                  class="colorBtn"
                  @change="colorChange"
                />
              </template>
            </a-table>
          </a-col>
        </a-row>
        <a-row type="flex" justify="center">
          <a-col :span="7">
            <span>透明度</span>
          </a-col>
          <a-col :span="16">
            <a-slider
              v-model="visualizationInfo.fillOpacity"
              :marks="marks"
              :default-value="75"
              @change="change"
            />
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="7">
            <span>点大小</span>
          </a-col>
          <a-col :span="16">
            <a-input-number
              v-model="visualizationInfo.radius"
              size="small"
              style="width: 100%"
              @change="change"
            />
          </a-col>
        </a-row>
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
import colorPicker from '../../manager/color-picker'
import colorPicker2 from '../../manager/colorPicker2'
import LabelLayerSetting from './LabelLayerSetting'
import PropertySetting from './PropertySetting'
const columns = [
  {
    title: '字段单值',
    dataIndex: 'fieldValue',
    key: 'fieldValue',
    scopedSlots: { customRender: 'fieldValue' },
  },
  {
    title: '填充颜色',
    dataIndex: 'color',
    key: 'color',
    scopedSlots: { customRender: 'color' },
  },
]
export default {
  components: {
    LabelLayerSetting,
    PropertySetting,
    colorPicker,
    colorPicker2,
  },
  model: {
    prop: 'functioninfo',
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
      customColor: false,
      showFunction: {},
      visualizationInfo: {
        dataSourceName: '',
        datasetName: '',
        themeField: '',
        customColors: [],
        fillOpacity: 75,
        radius: 15,
        property: {},
        label: {},
      },
      showProperty: false,
      showLabel: false,
      columns,
      data: [],
      dsOptions: [],
      dtOptions: [],
      fieldOptions: [],
      marks: {
        0: '0%',
        50: '50%',
        100: {
          label: '100%',
        },
      },
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
      colorCount: 4,
      color2Value: {},
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
            if (this.visualizationInfo.fillOpacity) {
              this.visualizationInfo.fillOpacity =
                this.visualizationInfo.fillOpacity * 100
            }
            if (this.visualizationInfo.property) {
              this.fieldsInfo.title = this.visualizationInfo.property.title
              this.fieldsInfo.fields = this.visualizationInfo.property.fields
            }
            if (this.visualizationInfo.label) {
              this.labelInfo.field = this.visualizationInfo.label.field
              this.labelInfo.color = this.visualizationInfo.label.color
              this.labelInfo.font = this.visualizationInfo.label.font
            }
            if (this.visualizationInfo.customColors) {
              this.data = this.visualizationInfo.customColors
              this.colorCount = this.data.length
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
  methods: {
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
        this.fieldOptions = [...mapLayerJson.fieldNames]
        this.fieldsInfo.allFields = this.fieldOptions
        this.fieldsInfo = { ...this.fieldsInfo }
        this.labelInfo.allFields = this.fieldOptions
        this.labelInfo = { ...this.labelInfo }
      }
    },
    fieldChange() {
      const url = this.showFunction.actionURL
      this.getFields(url)
      this.emitHeatMapSettingInfo()
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
          this.fieldOptions = allField
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
          this.fieldOptions = []
          for (let i = 0; i < arrAttr.length; i++) {
            const arr = arrAttr[i].split(':')
            const fName = JSON.parse(arr[0])
            this.fieldOptions.push(fName)
          }
          const allField = this.fieldOptions
          this.fieldsInfo.allFields = allField
          this.fieldsInfo = { ...this.fieldsInfo }
          this.labelInfo.allFields = allField
          this.labelInfo = { ...this.labelInfo }
        }
      }
    },
    async getFields(url) {
      if (this.showFunction.actionOptionType === 'RESTDATA') {
        return new Promise(() => {
          const queryParam = new SuperMap.REST.FilterParameter({
            name:
              this.visualizationInfo.datasetName +
              '@' +
              this.visualizationInfo.dataSourceName,
            groupBy: this.visualizationInfo.themeField,
            fields: [this.visualizationInfo.themeField],
          })
          const queryBySQLParams = new SuperMap.REST.GetFeaturesBySQLParameters(
            {
              queryParameter: queryParam,
              toIndex: -1,
              datasetNames: [
                this.visualizationInfo.dataSourceName +
                  ':' +
                  this.visualizationInfo.datasetName,
              ],
            }
          )
          const queryUrl = url + '/data/'
          const queryBySQLService = new SuperMap.REST.GetFeaturesBySQLService(
            queryUrl,
            {
              eventListeners: {
                processCompleted: this.processCompleted,
                processFailed: this.processFailed,
              },
            }
          )
          queryBySQLService.processAsync(queryBySQLParams)
        })
      } else if (this.showFunction.actionOptionType === 'RESTAPI') {
        const res = await this.$axios.$get(url)
        const mapLayerInfo = res.data
        if (mapLayerInfo && mapLayerInfo.length) {
          const fieldName = this.visualizationInfo.themeField
          const fieldValues = []
          for (let i = 0; i < mapLayerInfo.length; i++) {
            const attribute = mapLayerInfo[i].attributes
            const value = attribute[fieldName]
            if (!fieldValues.includes(value)) {
              fieldValues.push(value)
            }
          }
          this.data = []
          for (let i = 0; i < fieldValues.length; i++) {
            const item = {
              fieldValue: fieldValues[i],
              color: '#000000',
            }
            this.data.push(item)
          }
          this.visualizationInfo.customColors = [...this.data]
          this.colorCount = this.data.length
        }
      }
    },
    processCompleted(queryEventArgs) {
      const result = queryEventArgs.result
      if (result && result.features) {
        this.data = []
        for (let i = 0; i < result.features.length; i++) {
          const item = {
            fieldValue:
              result.features[i].data[this.visualizationInfo.themeField],
            color: '#000000',
          }
          this.data.push(item)
        }
        this.visualizationInfo.customColors = this.data
        this.colorCount = this.data.length
      }
    },
    colorChange() {
      this.visualizationInfo.customColors = [...this.data]
      this.emitHeatMapSettingInfo()
    },
    getProperty(value) {
      this.visualizationInfo.property = value
      this.emitHeatMapSettingInfo()
    },
    getLabel(value) {
      this.visualizationInfo.label = value
      this.emitHeatMapSettingInfo()
    },
    onChange(value) {
      // this.visualizationInfo.color = value
      if (this.data) {
        if (value.length === this.data.length) {
          for (let i = 0; i < value.length; i++) {
            this.data[i].color = value[i]
          }
          this.data = [...this.data]
        }
      }
      this.emitHeatMapSettingInfo()
    },
    change() {
      this.emitHeatMapSettingInfo()
    },
    emitHeatMapSettingInfo() {
      this.$emit('uniqueThemeLayer', this.visualizationInfo)
    },
  },
}
</script>

<style scoped>
.colorBtn {
  position: absolute;
  z-index: auto;
}
.spin-content {
  border: 1px solid #ffffff;
  background-color: #ffffff;
}
</style>
