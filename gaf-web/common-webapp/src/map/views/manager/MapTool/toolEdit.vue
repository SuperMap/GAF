<template>
  <div>
    <div v-show="!showSelectionSetting" class="gutter-box">
      <a-divider orientation="left" style="margin: 0px">设置</a-divider>
      <a-row :gutter="16">
        <a-col :span="7">
          <span>类型：</span>
        </a-col>
        <a-col :span="16">
          <a-select
            v-model="toolInfo.toolType"
            size="small"
            disabled
            style="width: 100%"
          >
            <a-select-option value="system">系统</a-select-option>
            <a-select-option value="custom">自定义</a-select-option>
          </a-select>
        </a-col>
      </a-row>
      <a-row v-show="toolInfo.toolType === 'custom'" :gutter="16">
        <a-col :span="7">
          <span>执行路径：</span>
        </a-col>
        <a-col :span="16">
          <a-input v-model="toolInfo.actionUrl" disabled size="small" />
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="7">
          <span>名称：</span>
        </a-col>
        <a-col :span="16">
          <a-input v-model="toolInfo.name" size="small" @change="nameChanged" />
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="7">
          <span>上级节点：</span>
        </a-col>
        <a-col :span="16">
          <a-input v-model="parentName" disabled size="small" />
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="7">
          <span>code：</span>
        </a-col>
        <a-col :span="16">
          <a-input v-model="toolInfo.code" disabled size="small" />
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="7">
          <span>图标：</span>
        </a-col>
        <a-col :span="12">
          <a-input v-model="toolInfo.icon" size="small" @change="iconChange" />
        </a-col>
        <a-col :span="4">
          <a-button icon="setting" size="small" @click="showIcon = true" />
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="7">
          <span>顺序：</span>
        </a-col>
        <a-col :span="16">
          <a-input-number v-model="toolInfo.index" width="100%" size="small" />
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="7">
          <span>显示风格：</span>
        </a-col>
        <a-col :span="16">
          <a-select
            v-model="toolInfo.displayStyle"
            size="small"
            style="width: 100%"
            @change="displayStyleChanged"
          >
            <a-select-option value="ImageAndText">ImageAndText</a-select-option>
            <a-select-option value="Image">Image</a-select-option>
            <a-select-option value="Text">Text</a-select-option>
          </a-select>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="7">
          <span>是否显示：</span>
        </a-col>
        <a-col :span="16">
          <a-switch v-model="toolInfo.visible" size="small">
            <a-icon slot="checkedChildren" type="check" />
            <a-icon slot="unCheckedChildren" type="close" />
          </a-switch>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="7">
          <span>预览：</span>
        </a-col>
        <a-col :span="16">
          <a-button :icon="icon" size="small">
            <span>{{ text }}</span>
          </a-button>
        </a-col>
      </a-row>
      <div
        v-if="
          toolInfo.code === 'featureidentity' ||
          toolInfo.code === 'querybyattribute' ||
          toolInfo.code === 'querybySelect' ||
          toolInfo.code === 'querybyspatial'
        "
        style="width: 95%"
      >
        <layer-selection-set
          :active-card="activeCode"
          :refresh="refresh"
          :map-code="mapCode"
          @selectionItemClick="selectionItemClick"
        />
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
        <a-button type="primary" size="small" @click="onSubmit">
          确定
        </a-button>
      </div>
    </div>
    <div v-show="showSelectionSetting">
      <layer-select-detailed-setting
        :layer-service-info="selectionInfo"
        :map-code="mapCode"
        @refresh="selectionRefresh"
        @back="back"
      />
    </div>
  </div>
</template>

<script>
// @ts-nocheck
import { getMapToolUrl } from '~/utils/GAFMapDataUtils'
import systemIcon from '../systemIcon'
import layerSelectionSet from '../Selection/layerSelectionSet'
import layerSelectDetailedSetting from '../Selection/layerSelDetailedSetting'
export default {
  components: {
    systemIcon,
    layerSelectionSet,
    layerSelectDetailedSetting,
  },
  model: {
    prop: 'mapToolInfo',
    event: 'change',
  },
  props: {
    mapToolInfo: {
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
      toolInfo: {
        id: '',
        name: '',
        icon: '',
        index: 0,
        displayStyle: '',
        actionUrl: '',
        visible: '',
      },
      selectionInfo: {},
      icon: '',
      text: '',
      parentName: '',
      showIcon: false,
      activeCode: '',
      refresh: false,
      showSelectionSetting: false,
    }
  },
  watch: {
    mapToolInfo(val) {
      if (val.id) {
        this.toolInfo = val
        const parentId = this.toolInfo.parentId
        this.loadParentName(parentId)
        this.displayStyleChanged(this.toolInfo.displayStyle)
        this.setActiveCode(val.code)
        this.showSelectionSetting = false
      } else {
        this.toolInfo = {
          id: '',
          name: '',
          icon: '',
          displayStyle: '',
          actionUrl: '',
          visible: '',
        }
      }
    },
  },
  methods: {
    async onSubmit() {
      const url = getMapToolUrl(this.mapCode)
      const res = await this.$axios.$put(
        url + `/${this.toolInfo.id}`,
        this.toolInfo
      )
      if (res.isSuccessed) {
        this.$message.success('编辑成功！')
        this.$emit('refresh')
      } else {
        this.$message.error('编辑失败')
      }
    },
    setActiveCode(code) {
      if (code === 'featureidentity') {
        this.activeCode = 'feature'
      } else if (code === 'querybyattribute') {
        this.activeCode = 'attribute'
      } else if (code === 'querybySelect' || code === 'querybyspatial') {
        this.activeCode = 'space'
      }
    },
    async loadParentName(parentId) {
      if (parentId !== null && parentId !== undefined && parentId !== '') {
        const url = getMapToolUrl(this.mapCode)
        const res = await this.$axios.$get(url + `/${parentId}`)
        if (res && res.data) {
          this.parentName = res.data.name
        }
      } else {
        this.parentName = ''
      }
    },
    nameChanged() {
      this.text = this.toolInfo.name
    },
    iconChange(value) {
      this.icon = value
    },
    displayStyleChanged(value) {
      if (value.includes('Text')) {
        this.text = this.toolInfo.name
      } else {
        this.text = ''
      }
      if (value.includes('Image')) {
        this.icon = this.toolInfo.icon
      } else {
        this.icon = ''
      }
    },
    selectedIcon(icon) {
      this.toolInfo.icon = icon
      this.icon = icon
    },
    selectionItemClick(row) {
      this.showSelectionSetting = true
      this.selectionInfo = { ...row }
    },
    selectionRefresh() {
      this.refresh = !this.refresh
    },
    back() {
      this.showSelectionSetting = false
    },
  },
}
</script>

<style scoped>
.gutter-box >>> .ant-row {
  padding: 5px 0px 5px 15px;
}
</style>
