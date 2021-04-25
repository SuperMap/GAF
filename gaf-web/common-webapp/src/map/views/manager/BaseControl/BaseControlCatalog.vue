<template>
  <div>
    <a-row>
      <a-col :span="12">
        <span style="font-size: large">基础控件</span>
      </a-col>
    </a-row>
    <a-divider style="margin: 10px" />
    <div>
      <a-menu mode="inline" style="background: #ffffff" @click="handleClick">
        <template v-for="item in data">
          <a-menu-item :key="item.type">
            <a-icon :type="item.icon" />
            <span>{{ item.name }}</span>
          </a-menu-item>
        </template>
      </a-menu>
    </div>
  </div>
</template>

<script>
import { getMapControlUrl } from '~/utils/GAFMapDataUtils'
const data = [
  {
    name: '缩放',
    type: 'Zoom',
    icon: 'setting',
  },
  {
    name: '比例尺',
    type: 'ScaleLine',
    icon: 'setting',
  },
  {
    name: '鼠标位置坐标',
    type: 'MousePosition',
    icon: 'setting',
  },
  {
    name: '鹰眼图',
    type: 'OverviewMap',
    icon: 'setting',
  },
  {
    name: '图层控制',
    type: 'LayerControl',
    icon: 'setting',
  },
]
export default {
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
    return {
      data,
      addVisible: false,
      selectedItem: {},
    }
  },
  watch: {
    refresh: {
      handler() {
        this.loadBaseControls()
      },
    },
    immediate: true,
  },
  created() {
    for (let i = 0; i < this.data.length; i++) {
      this.data[i].mapConfigCode = this.mapCode
    }
    this.loadBaseControls()
  },
  methods: {
    async loadBaseControls() {
      const url = getMapControlUrl(this.mapCode)
      const res = await this.$axios.$get(url)
      const result = res.data
      if (result && result.length) {
        result.forEach((item) => {
          for (let i = 0; i < this.data.length; i++) {
            const dItem = this.data[i]
            if (dItem.type === item.type) {
              this.data[i] = item
            }
          }
        })
        this.data = [...this.data]
      }
    },
    async handleClick(e) {
      const url = getMapControlUrl(this.mapCode)
      const res = await this.$axios.$get(url + `/${e.key}`)
      if (res && res.data) {
        this.selectedItem = { ...res.data }
      } else {
        this.data.forEach((item) => {
          if (item.type === e.key) {
            this.selectedItem = item
          }
        })
      }
      this.$emit('onBaseControlClick', this.selectedItem)
    },
  },
}
</script>

<style scoped></style>
