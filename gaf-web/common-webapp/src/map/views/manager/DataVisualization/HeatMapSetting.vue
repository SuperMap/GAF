<template>
  <a-spin :spinning="spinning">
    <div class="gutter-box">
      <div>
        <a-row :gutter="16">
          <a-col :span="7">
            <span>热点颜色</span>
          </a-col>
          <a-col :span="16">
            <color-picker2
              v-model="colorValue"
              :count="5"
              @change="colorChange"
            />
          </a-col>
        </a-row>
        <a-row :gutter="16">
          <a-col :span="7">
            <span>热点半径</span>
          </a-col>
          <a-col :span="16">
            <a-input-number
              v-model="visualizationInfo.radius"
              size="small"
              style="width: 80%"
              @change="change"
            />
            <span>px</span>
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
          <a-col :span="7">
            <span>权重字段</span>
          </a-col>
          <a-col :span="16">
            <a-select
              v-model="visualizationInfo.featureWeight"
              show-search
              option-filter-prop="children"
              size="small"
              style="width: 100%"
              @change="change"
            >
              <a-select-option v-for="item in fieldOptions" :key="item">
                {{ item }}
              </a-select-option>
            </a-select>
          </a-col>
        </a-row>
      </div>
    </div>
  </a-spin>
</template>

<script>
// @ts-nocheck
import colorPicker2 from '../../manager/colorPicker2'
export default {
  components: {
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
      showFunction: {},
      visualizationInfo: {
        color: [],
        radius: 15,
        dataSourceName: '',
        datasetName: '',
        featureWeight: '',
        property: {},
        label: {},
      },
      dsName: '',
      dsOptions: [],
      dtOptions: [],
      fieldOptions: [],
      labelInfo: {
        allFields: [],
        field: '',
        color: '',
        font: '',
      },
      colorValue: {},
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
      this.fieldOptions = []
      if (mapLayerJson.fieldNames) {
        if (mapLayerJson.childUriList && mapLayerJson.childUriList.length) {
          const list = mapLayerJson.childUriList
          this.setFieldsByFieldType(list)
        }
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
          this.fieldOptions = []
          if (mapLayerJson.childUriList && mapLayerJson.childUriList.length) {
            const list = mapLayerJson.childUriList
            this.setFieldsByFieldType(list)
          }
        }
      }
    },
    async setFieldsByFieldType(list) {
      for (let i = 0; i < list.length; i++) {
        const fieldUrl = list[i] + '.jsonp'
        const field = await this.$axios.jsonp(fieldUrl)
        if (
          field.fieldInfo.type === 'INT32' ||
          field.fieldInfo.type === 'INT64' ||
          field.fieldInfo.type === 'SINGLE' ||
          field.fieldInfo.type === 'DOUBLE'
        ) {
          this.fieldOptions.push(field.fieldInfo.name)
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
    colorChange(value) {
      this.visualizationInfo.color = value
      this.emitHeatMapSettingInfo()
    },
    change() {
      this.emitHeatMapSettingInfo()
    },
    emitHeatMapSettingInfo() {
      this.$emit('heatMapInfo', this.visualizationInfo)
    },
  },
}
</script>

<style scoped></style>
