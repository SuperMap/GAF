<template>
  <a-spin :spinning="spinning">
    <div class="gutter-box">
      <div>
        <a-row :gutter="16">
          <a-col :span="7">
            <span>统计方式</span>
          </a-col>
          <a-col :span="16">
            <a-select
              v-model="visualizationInfo.chartsType"
              allow-clear
              style="width: 100%"
              size="small"
            >
              <a-select-option value="Bar">柱状图</a-select-option>
              <a-select-option value="Line">折线图</a-select-option>
              <a-select-option value="Pie">饼状图</a-select-option>
            </a-select>
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
          <a-col :span="16">
            <span> 统计样式:</span>
          </a-col>
          <a-col :span="4">
            <a-tooltip placement="topLeft" title="添加统计字段">
              <a-icon type="plus" style="margin: 5px" @click="AddThemeField" />
            </a-tooltip>
          </a-col>
          <a-col :span="4">
            <a-tooltip placement="topLeft" title="删除统计字段">
              <a-popconfirm
                title="是否删除？"
                ok-text="是"
                cancel-text="否"
                @confirm="DeleteThemeField"
              >
                <a-icon type="delete" style="margin: 5px" />
              </a-popconfirm>
            </a-tooltip>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="24">
            <a-table
              :pagination="false"
              :columns="columns"
              :data-source="data"
              :row-key="(record) => record.name"
              :scroll="{ y: 300 }"
              :row-selection="{
                selectedRowKeys: selectedRowKeys,
                onChange: onSelectedChange,
              }"
            >
              <template slot="name" slot-scope="text, record">
                <a-select
                  v-model="record.name"
                  show-search
                  option-filter-prop="children"
                  style="width: 100%"
                  size="small"
                  @change="colorChange"
                >
                  <a-select-option v-for="item in fieldOptions" :key="item">
                    {{ item }}
                  </a-select-option>
                </a-select>
              </template>
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
        <a-row :gutter="16">
          <a-col :span="7">
            <span>统计范围</span>
          </a-col>
          <a-col :span="7">
            <a-tooltip placement="topLeft" title="最小值">
              <a-input-number
                v-model="visualizationInfo.minScope"
                size="small"
                :min="0"
                style="width: 100%"
              />
            </a-tooltip>
          </a-col>
          <a-col :span="1">
            <span>-</span>
          </a-col>
          <a-col :span="7">
            <a-tooltip placement="topLeft" title="最大值">
              <a-input-number
                v-model="visualizationInfo.maxScope"
                size="small"
                :min="0"
                style="width: 100%"
              />
            </a-tooltip>
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="7">
            <span>图表宽度</span>
          </a-col>
          <a-col :span="16">
            <a-input-number
              v-model="visualizationInfo.width"
              size="small"
              :min="0"
              style="width: 100%"
              @change="change"
            />
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="7">
            <span>图表高度</span>
          </a-col>
          <a-col :span="16">
            <a-input-number
              v-model="visualizationInfo.height"
              size="small"
              :min="0"
              style="width: 100%"
              @change="change"
            />
          </a-col>
        </a-row>
        <a-row :gutter="16">
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
import LabelLayerSetting from './LabelLayerSetting'
import PropertySetting from './PropertySetting'
const columns = [
  {
    title: '统计字段',
    dataIndex: 'name',
    key: 'name',
    scopedSlots: { customRender: 'name' },
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
      showProperty: false,
      showLabel: false,
      showFunction: {},
      visualizationInfo: {
        chartsType: '',
        dataSourceName: '',
        datasetName: '',
        themeFields: [],
        minScope: 0,
        maxScope: 0,
        width: 0,
        height: 0,
        fillOpacity: 75,
        property: {},
        label: {},
      },
      dsOptions: [],
      dtOptions: [],
      fieldOptions: [],
      selectedRowKeys: [],
      dsName: '',
      columns,
      data: [],
      marks: {
        0: '0%',
        50: '50%',
        100: {
          style: {
            color: '#f50',
          },
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
      color: '',
      disabled: false,
      count: 0,
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
            if (this.visualizationInfo.themeFields) {
              this.data = this.visualizationInfo.themeFields
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
    AddThemeField() {
      const { count, data } = this
      const newData = {
        name: `newField ${count}`,
        color: '#000000',
      }
      this.data = [newData, ...data]
      this.count = count + 1
    },
    DeleteThemeField() {
      if (this.selectedRowKeys.length) {
        for (let i = 0; i < this.selectedRowKeys.length; i++) {
          const key = this.selectedRowKeys[i]
          const dataSource = [...this.data]
          this.data = dataSource.filter((item) => item.name !== key)
          this.visualizationInfo.themeFields = this.data
        }
        this.data = [...this.data]
        this.emitHeatMapSettingInfo()
      }
    },
    onSelectedChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    colorChange() {
      this.visualizationInfo.themeFields = this.data
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
    change() {
      this.emitHeatMapSettingInfo()
    },
    emitHeatMapSettingInfo() {
      this.$emit('graphThemeLayer', this.visualizationInfo)
    },
    headleChangeColor() {},
  },
}
</script>

<style scoped>
.colorBtn {
  position: absolute;
  z-index: auto;
}
</style>
