<template>
  <div>
    <a-row>
      <a-col :span="12">
        <span style="font-size: large">地图工具</span>
      </a-col>
      <a-col :span="4">
        <a-tooltip placement="topLeft" title="添加工具">
          <a-icon
            type="plus"
            style="margin: 5px"
            @click="() => (addToolVisible = true)"
          />
        </a-tooltip>
      </a-col>
      <a-col :span="4">
        <a-tooltip placement="topLeft" title="删除">
          <a-popconfirm
            title="是否删除？"
            ok-text="是"
            cancel-text="否"
            @confirm="() => deleteSelectedTool()"
          >
            <a href="javascript:;">
              <a-icon type="delete" style="margin: 5px" />
            </a>
          </a-popconfirm>
        </a-tooltip>
      </a-col>
      <a-col :span="4">
        <a-tooltip placement="topLeft" title="设置样式">
          <a-icon
            type="setting"
            style="margin: 5px"
            @click="editToolbarStyle"
          />
        </a-tooltip>
      </a-col>
    </a-row>
    <a-divider style="margin: 10px" />
    <div :style="divStyle">
      <a-menu mode="inline" style="background: #ffffff" @click="handleClick">
        <template v-for="item in toolTreeData">
          <a-menu-item v-if="!item.children.length" :key="item.id">
            <a-icon :type="item.icon" />
            <span>{{ item.name }}</span>
          </a-menu-item>
          <a-sub-menu v-else :key="item.id" @titleClick="handleClick">
            <span slot="title">
              <a-icon :type="item.icon" />
              {{ item.name }}
            </span>
            <template v-for="subItem in item.children">
              <a-menu-item :key="subItem.id">
                <a-icon :type="subItem.icon" />
                <span>{{ subItem.name }}</span>
              </a-menu-item>
            </template>
          </a-sub-menu>
        </template>
      </a-menu>
    </div>
    <add-map-tool
      :visible="addToolVisible"
      :map-code="mapCode"
      @closeMapToolAdd="closeMapToolAdd"
    />
  </div>
</template>

<script>
// @ts-nocheck
import { getMapToolUrl } from '~/utils/GAFMapDataUtils'
import addMapTool from './addMapTool'
export default {
  components: {
    addMapTool,
  },
  model: {
    prop: 'refresh',
    event: 'change',
  },
  props: {
    refresh: Boolean,
    mapCode: {
      type: String,
      required: true,
      validator(value) {
        return value
      },
    },
  },
  data() {
    const { contentWidth, contentHeight } = this.$store.state
    return {
      width: contentWidth,
      height: contentHeight,
      addToolVisible: false,
      toolInfo: {},
      selectedToolKey: '',
      newMapToolInfo: {},
      toolTreeData: [],
    }
  },
  computed: {
    divStyle() {
      return `height:${this.height - 70}px;overflow-y:auto;overflow-x:hidden;`
    },
  },
  watch: {
    refresh: {
      handler() {
        this.setToolsVisible()
      },
    },
    immediate: true,
  },
  created() {
    this.setToolsVisible()
  },
  methods: {
    async setToolsVisible() {
      const url = getMapToolUrl(this.mapCode)
      const res = await this.$axios.$get(url)
      this.toolTreeData = [...res.data]
    },
    closeMapToolAdd() {
      this.setToolsVisible()
      this.addToolVisible = false
    },
    async deleteSelectedTool() {
      const url = getMapToolUrl(this.mapCode) + `/${this.selectedToolKey}`
      await this.$axios.$delete(url)
      this.toolInfo = {}
      this.setToolsVisible()
    },
    getNewMapTool(value) {
      this.newMapToolInfo = value
    },
    async handleClick(e) {
      this.selectedToolKey = e.key
      const url = getMapToolUrl(this.mapCode) + `/${e.key}`
      const res = await this.$axios.$get(url)
      this.toolInfo = res.data
      this.editType = 'toolItem'
      this.$emit('onToolMenuClick', this.selectedToolKey)
    },
    // 地图工具条设置
    editToolbarStyle() {
      this.$emit('editToolbarStyle')
    },
  },
}
</script>

<style scoped>
.div-range {
  max-height: 80vh;
  overflow-y: auto;
}
</style>
