<template>
  <div class="gutter-box">
    <div>
      <a-row>
        <a-col :span="7">
          <span>是否显示</span>
        </a-col>
        <a-col>
          <a-switch v-model="labelInfo.visible" size="small" @change="change">
            <a-icon slot="checkedChildren" type="check" />
            <a-icon slot="unCheckedChildren" type="close" />
          </a-switch>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="7">
          <span>标签字段</span>
        </a-col>
        <a-col :span="16">
          <a-select
            v-model="labelInfo.field"
            show-search
            option-filter-prop="children"
            allow-clear
            style="width: 100%"
            size="small"
            @change="change"
          >
            <a-select-option v-for="item in fieldOptions" :key="item">
              {{ item }}
            </a-select-option>
          </a-select>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="7">
          <span>标签颜色</span>
        </a-col>
        <a-col :span="16">
          <color-picker
            v-model="labelInfo.color"
            class="colorBtn"
            @change="change"
          />
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="7">
          <span>标签字体</span>
        </a-col>
        <a-col :span="16">
          <a-select
            v-model="labelInfo.font"
            allow-clear
            style="width: 100%"
            size="small"
            @change="change"
          >
            <a-select-option value="黑体">黑体</a-select-option>
            <a-select-option value="宋体">宋体</a-select-option>
          </a-select>
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script>
// @ts-ignore
import colorPicker from '../../manager/color-picker'
export default {
  components: {
    colorPicker,
  },
  model: {
    prop: 'labelSettingInfo',
    event: 'change',
  },
  props: {
    labelSettingInfo: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      fieldOptions: [],
      labelInfo: {
        visible: true,
        field: '',
        color: '#000000',
        font: '',
      },
    }
  },
  watch: {
    labelSettingInfo(val) {
      this.fieldOptions = []
      if (val.allFields.length) {
        const fieldNames = val.allFields
        for (let i = 0; i < fieldNames.length; i++) {
          this.fieldOptions.push(fieldNames[i])
        }
        this.labelInfo.field = val.field
        this.labelInfo.color = val.color
        this.labelInfo.font = val.font
      }
    },
  },
  methods: {
    change() {
      this.$emit('labelInfo', this.labelInfo)
    },
  },
}
</script>

<style scoped>
.colorBtn {
  position: absolute;
  z-index: auto;
}
</style>
