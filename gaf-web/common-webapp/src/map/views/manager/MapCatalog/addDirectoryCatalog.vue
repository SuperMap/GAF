<template>
  <div>
    <a-form :form="catalogInfo">
      <a-form-item
        label="目录名称"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 17 }"
      >
        <a-input
          v-model="catalogInfo.catalogName"
          size="small"
          @change="onNameChange"
        />
      </a-form-item>
      <a-form-item
        label="上级目录"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 17 }"
      >
        <a-select
          v-model="parentNode.id"
          size="small"
          allow-clear
          @change="onParentChange"
        >
          <a-select-option v-for="item in directoryCatalog" :key="item.id">
            {{ item.catalogName }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item
        label="顺序"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 17 }"
      >
        <a-input-number
          v-model="catalogInfo.catalogIndex"
          size="small"
          style="width: 100%"
          @change="onIndexChange"
        />
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
import { getLayerCatalogUrl } from '~/utils/GAFMapDataUtils'
export default {
  model: {
    prop: 'parentCatalogNode',
    event: 'change',
  },
  props: {
    parentCatalogNode: {
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
      directoryCatalog: [],
      catalogInfo: {
        id: '',
        parentId: '0',
        catalogType: 'directory',
        catalogName: '',
        catalogIndex: 0,
      },
      parentNode: {},
      parentId: '',
    }
  },
  watch: {
    parentCatalogNode(val) {
      if (this.parentNode.$options) {
        this.parentNode = val
        this.parentId = this.parentNode.$options.propsData.dataRef.slots.id
      }
    },
  },
  created() {
    this.catalogInfo.catalogName = ''
    this.loadDirectoryCatalog()
  },
  methods: {
    // 获取所有目录 1
    async loadDirectoryCatalog() {
      const url = getLayerCatalogUrl(this.mapCode)
      const res = await this.$axios.$get(url)
      this.directoryCatalog = []
      this.setCatalogToDirectory(this.directoryCatalog, res.data)
    },
    // 获取所有目录 2
    setCatalogToDirectory(list, tree) {
      if (tree !== undefined && tree.length > 0) {
        for (let i = 0; i < tree.length; i++) {
          const item = tree[i]
          if (item.slots.catalogType === 'directory') {
            list.push(item.slots)
            this.setCatalogToDirectory(list, item.children)
          }
        }
      }
    },
    // 修改目录名字
    onNameChange() {
      // this.catalogInfo.catalogName = value
      this.getNewCatalog()
    },
    // 修改上级目录
    onParentChange(value) {
      this.catalogInfo.parentId = value
      this.getNewCatalog()
    },
    // 修改目录顺序
    onIndexChange(value) {
      this.catalogInfo.catalogIndex = value
      this.getNewCatalog()
    },
    getNewCatalog() {
      this.$emit('newCatalog', this.catalogInfo)
    },
  },
}
</script>
