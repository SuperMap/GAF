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
        <a-switch v-model="settingControl.visible" size="small">
          <a-icon slot="checkedChildren" type="check" />
          <a-icon slot="unCheckedChildren" type="close" />
        </a-switch>
      </a-col>
    </a-row>
    <a-row>
      <a-col :offset="1" :span="6">位置：</a-col>
      <a-col :span="16">
        <a-select
          v-model="settingControl.position"
          style="width: 100%"
          size="small"
        >
          <a-select-option value="left">左侧</a-select-option>
          <a-select-option value="top">上方</a-select-option>
          <a-select-option value="right">右侧</a-select-option>
        </a-select>
      </a-col>
    </a-row>
    <div v-if="settingControl.type === 'LayerControl'">
      <a-row>
        <a-col :offset="1" :span="6">
          <span>图标：</span>
        </a-col>
        <a-col :span="12">
          <a-input v-model="settingControl.icon" size="small" />
        </a-col>
        <a-col :span="4">
          <button @click="showIcon = true"><a-icon type="setting" /></button>
        </a-col>
      </a-row>
      <a-row>
        <a-col :offset="1" :span="6">选择样式：</a-col>
        <a-col :span="16">
          <a-radio-group v-model="settingControl.style" size="small">
            <a-radio value="Union">合并显示</a-radio>
            <a-radio value="Independent">独立显示</a-radio>
          </a-radio-group>
        </a-col>
      </a-row>
      <a-row>
        <a-card
          v-if="settingControl.style === 'Union'"
          hoverable
          style="width: 280px; height: 360px"
        >
          <img slot="cover" alt="" :src="unionUrl" />
        </a-card>
        <a-card
          v-if="settingControl.style === 'Independent'"
          hoverable
          style="width: 280px; height: 360px"
        >
          <img slot="cover" alt="" :src="independentUrl" />
        </a-card>
      </a-row>
    </div>
    <system-icon
      :visible="showIcon"
      @selectedIcon="selectedIcon"
      @closeIconSetting="showIcon = false"
    />
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
      <button @click="onSubmit"> 确定 </button>
    </div>
  </div>
</template>

<script>
// @ts-nocheck
import { getMapControlUrl } from '~/utils/GAFMapDataUtils'
import systemIcon from '../systemIcon'
import unionImgUrl from '~/assets/Union.png'
import independentImgUrl from '~/assets/Independent.png'
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
      settingControl: {},
      independentUrl: independentImgUrl,
      unionUrl: unionImgUrl,
      showIcon: false,
    }
  },
  watch: {
    controlInfo(val) {
      Object.assign(this.$data, this.$options.data())
      this.settingControl = val
    },
  },
  methods: {
    async onSubmit() {
      const url = getMapControlUrl(this.mapCode)
      const checked = await this.$axios.$get(
        url + `/${this.settingControl.type}`
      )
      if (checked && checked.data) {
        const res = await this.$axios.$put(
          url + `/${this.settingControl.id}`,
          this.settingControl
        )
        if (res.isSuccessed) {
          this.$message.success('操作成功')
          this.$emit('refresh')
        } else {
          this.$message.error('操作失败')
        }
      } else {
        const res = await this.$axios.$post(url, this.settingControl)
        if (res.isSuccessed) {
          this.$message.success('操作成功')
          this.settingControl.id = res.data
        } else {
          this.$message.error('操作失败')
        }
      }
    },
    selectedIcon(icon) {
      this.settingControl.icon = icon
      this.icon = icon
    },
  },
}
</script>

<style scoped>
.gutter-box >>> .ant-row {
  padding: 5px 5px 5px 5px;
}
</style>
