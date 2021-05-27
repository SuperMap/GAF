<template>
  <div class="gutter-box">
    <a-row :gutter="16">
      <a-col :span="18">
        <a-divider orientation="left" style="margin: 0px">设置</a-divider>
      </a-col>
    </a-row>
    <a-row size="small">
      <a-col :offset="1" :span="6">
        <span>是否显示：</span>
      </a-col>
      <a-col :span="16">
        <a-switch v-model="configInfo.visible" size="small">
          <a-icon slot="checkedChildren" type="check" />
          <a-icon slot="unCheckedChildren" type="close" />
        </a-switch>
      </a-col>
    </a-row>
    <a-row>
      <a-col :offset="1" :span="6">位置：</a-col>
      <a-col :span="16">
        <a-select
          v-model="configInfo.position"
          style="width: 100%"
          size="small"
        >
          <a-select-option value="left">左侧</a-select-option>
          <a-select-option value="top">上方</a-select-option>
          <a-select-option value="right">右侧</a-select-option>
        </a-select>
      </a-col>
    </a-row>
    <a-row>
      <a-col :offset="1" :span="6">
        <span>宽度：</span>
      </a-col>
      <a-col :span="16">
        <a-input v-model="configInfo.width" class="fullWidth" size="small" />
      </a-col>
    </a-row>
    <a-row>
      <a-col :offset="1" :span="6">
        <span>高度：</span>
      </a-col>
      <a-col :span="16">
        <a-input v-model="configInfo.height" class="fullWidth" size="small" />
      </a-col>
    </a-row>
    <a-row>
      <a-col :offset="1" :span="6">
        <span>顺序：</span>
      </a-col>
      <a-col :span="16">
        <a-input-number
          v-model="configInfo.index"
          class="fullWidth"
          size="small"
          :precision="0"
          :min="1"
        />
      </a-col>
    </a-row>
    <a-row v-show="configInfo.type !== 'MapTool'">
      <a-col :offset="1" :span="6">
        <span>路径：</span>
      </a-col>
      <a-col :span="16">
        <a-input v-model="configInfo.routeurl" class="fullWidth" size="small" />
      </a-col>
    </a-row>
    <a-row :gutter="16">
      <a-col :offset="1" :span="6">
        <span>图标：</span>
      </a-col>
      <a-col :span="12">
        <a-input v-model="configInfo.icon" size="small" />
      </a-col>
      <a-col :span="4">
        <a-button icon="setting" size="small" @click="showIcon = true" />
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
    <system-icon
      :visible="showIcon"
      @selectedIcon="selectedIcon"
      @closeIconSetting="showIcon = false"
    />
  </div>
</template>

<script>
// @ts-nocheck
import { getMapControlUrl } from '~/utils/GAFMapDataUtils'
import systemIcon from './systemIcon'
export default {
  components: {
    systemIcon,
  },
  model: {
    prop: 'controlInfo',
    event: 'change',
  },
  props: {
    controlInfo: {
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
      showIcon: false,
      configInfo: {
        id: '',
        type: '',
        visible: true,
        routeurl: '',
        position: '',
        width: 300,
        height: 300,
        index: 0,
        icon: '',
      },
    }
  },
  watch: {
    controlInfo(val) {
      if (!val.id) {
        Object.assign(this.$data, this.$options.data())
      } else {
        this.configInfo = val
      }
      if (val.type === 'FunctionMenu') {
        this.configInfo.name = '功能目录'
      }
      this.configInfo.type = val.type
    },
  },
  methods: {
    async onSubmit() {
      this.configInfo.mapConfigCode = this.mapCode
      const url = getMapControlUrl(this.mapCode)
      const checked = await this.$axios.$get(url + `/${this.configInfo.type}`)
      if (checked && checked.data) {
        const res = await this.$axios.$put(
          url + `/${this.configInfo.id}`,
          this.configInfo
        )
        if (res.isSuccessed) {
          this.$message.success('操作成功')
        }
      } else {
        const res = await this.$axios.$post(url, this.configInfo)
        if (res.isSuccessed) {
          this.$message.success('操作成功')
          this.configInfo.id = res.data
        }
      }
    },
    selectedIcon(icon) {
      this.configInfo.icon = icon
    },
  },
}
</script>

<style scoped>
.gutter-box >>> .ant-row {
  padding: 5px 5px 5px 5px;
}
.fullWidth {
  width: 100%;
}
</style>
