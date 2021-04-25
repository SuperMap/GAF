<template>
  <div>
    <a-row>
      <a-col :span="12">
        <span style="font-size: large">图层目录</span>
      </a-col>
      <a-col :span="3">
        <a-tooltip placement="topLeft" title="添加目录">
          <a-icon
            type="folder-add"
            style="margin: 5px"
            @click="addCatalogVisible = true"
          />
        </a-tooltip>
      </a-col>
      <a-col :span="3">
        <a-tooltip placement="topLeft" title="添加图层">
          <a-icon type="plus" style="margin: 5px" @click="addLayerClick" />
        </a-tooltip>
      </a-col>
      <a-col :span="3">
        <a-tooltip placement="topLeft" title="删除">
          <a-popconfirm
            title="是否删除？"
            ok-text="是"
            cancel-text="否"
            @confirm="() => deleteCatalogNode()"
          >
            <a href="javascript:;">
              <a-icon type="delete" style="margin: 5px" />
            </a>
          </a-popconfirm>
        </a-tooltip>
      </a-col>
      <a-col :span="3">
        <a-tooltip placement="topLeft" title="设置样式">
          <a-icon
            type="setting"
            style="margin: 5px"
            @click="editCatalogStyle"
          />
        </a-tooltip>
      </a-col>
    </a-row>
    <a-divider style="margin: 10px" />
    <div :style="divStyle">
      <a-tree
        check-strictly
        :selected-keys="selectedKeys"
        size="small"
        :tree-data="treeData"
        @select="onTreeNodeSelect"
      />
    </div>
    <div>
      <gaf-modal
        title="添加目录"
        :visible="addCatalogVisible"
        @cancel="addCatalogCancel"
        @ok="addCatalogOk"
      >
        <add-directory-catalog
          :destroy-on-close="true"
          :parent-catalog-node="selectedNode"
          :map-code="mapCode"
          @newCatalog="getNewCatalog"
        />
      </gaf-modal>
      <gaf-modal
        title="添加地图"
        :visible="addLayerVisible"
        :width="800"
        style="width: 800px; height: 600px"
        @cancel="addLayerCancel"
        @ok="addLayerOk"
      >
        <add-layer-catalog
          ref="addLayerModal"
          :refresh="addrefresh"
          :map-code="mapCode"
          :parent-catalog-node="selectedNode"
        />
      </gaf-modal>
    </div>
  </div>
</template>
<script>
// @ts-nocheck
import {
  getLayerCatalogUrl,
  getMapControlUrl,
} from '~/utils/GAFMapDataUtils'
import addDirectoryCatalog from './addDirectoryCatalog'
import addLayerCatalog from './addLayerCatalog'
const getTreeNodeByKey = (key, tree) => {
  let result
  for (let i = 0; i < tree.length; i++) {
    const node = tree[i]
    if (node.key === key) {
      result = node
      break
    }
    if (getTreeNodeByKey(key, node.children)) {
      result = getTreeNodeByKey(key, node.children)
    }
  }
  return result
}
const getParentKey = (key, tree) => {
  let parentKey
  for (let i = 0; i < tree.length; i++) {
    const node = tree[i]
    if (node.children) {
      if (node.children.some((item) => item.key === key)) {
        parentKey = node.key
      } else if (getParentKey(key, node.children)) {
        parentKey = getParentKey(key, node.children)
      }
    }
  }
  return parentKey
}
export default {
  components: {
    addDirectoryCatalog,
    addLayerCatalog,
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
      addCatalogVisible: false,
      selectedNode: {},
      newCatalogInfo: {},
      addLayerVisible: false,
      selectedKeys: [],
      treeData: [],
      layerCatalogInfo: {},
      addrefresh: false,
    }
  },
  computed: {
    divStyle() {
      return `height:${this.height - 70}px;overflow-y:auto;overflow-x:auto;`
    },
  },
  watch: {
    refresh: {
      handler() {
        this.getMapTree()
      },
    },
    immediate: true,
  },
  created() {
    this.getMapTree()
  },
  methods: {
    async getMapTree() {
      const url = getLayerCatalogUrl(this.mapCode)
      const res = await this.$axios.$get(url)
      if (res && res.data) {
        this.treeData = [...res.data]
        // this.treeData = getMapTreeByCatalogInfo('0', res.data)
      }
    },
    addCatalogCancel() {
      this.selectedNode = {}
      this.addCatalogVisible = false
    },
    async addCatalogOk() {
      this.newCatalogInfo.mapConfigCode = this.mapCode
      this.newCatalogInfo.catalogType = 'directory'
      const url = getLayerCatalogUrl(this.mapCode)
      const result = await this.$axios.$post(url, this.newCatalogInfo)
      if (result.isSuccessed) {
        this.$message.success('操作成功')
        this.getMapTree()
        this.addCatalogVisible = false
      } else {
        this.$message.error('添加失败')
      }
    },
    getNewCatalog(value) {
      this.newCatalogInfo = value
    },
    addLayerCancel() {
      this.selectedNode = {}
      this.addLayerVisible = false
    },
    addLayerOk() {
      const form = this.$refs.addLayerModal.form
      const services = this.$refs.addLayerModal.selectedRows
      form.validateFields(async (err, values) => {
        if (err) {
          return
        }
        const configCode = 'LayerCatalog'
        const url = getMapControlUrl(this.mapCode) + `/${configCode}`
        const res = await this.$axios.$get(url)
        let result = {}
        if (services.length > 0) {
          let index = values.catalogIndex
          for (let i = 0; i < services.length; i++) {
            const LayerCatalogInfo = {
              catalogName: services[i].layerName,
              layerServiceId: services[i].id,
              parentId: values.parentId,
              catalogType: 'layer',
              catalogIndex: index,
              mapConfigCode: this.mapCode,
              mapControlId: res.id,
            }
            const url = getLayerCatalogUrl(this.mapCode)
            result = await this.$axios.$post(url, LayerCatalogInfo)
            index++
          }
        }
        if (result.isSuccessed) {
          this.$message.success('操作成功')
          this.getMapTree()
          this.addLayerVisible = false
          const fields = ['layerServiceId', 'catalogIndex']
          form.resetFields(fields)
          this.$refs.addLayerModal.layerInfo = null
        } else {
          this.$message.error(result.message)
        }
      })
    },
    async deleteCatalogNode() {
      if (this.selectedKeys.length > 0) {
        for (let i = 0; i < this.selectedKeys.length; i++) {
          const node = getTreeNodeByKey(this.selectedKeys[i], this.treeData)
          const pkey = getParentKey(this.selectedKeys[i], this.treeData)
          if (pkey !== undefined) {
            const parentNode = getTreeNodeByKey(pkey, this.treeData)
            this.deleteNode(node.key, parentNode.children)
          } else {
            this.deleteNode(node.key, this.treeData)
          }
          const MapTreeInfo = node
          const url = getLayerCatalogUrl(this.mapCode)
          await this.$axios.$delete(url, {
            data: MapTreeInfo,
          })
        }
      }
    },
    deleteNode(key, tree) {
      for (let i = 0; i < tree.length; i++) {
        const node = tree[i]
        if (node.key === key) {
          tree.splice(i, 1)
          break
        }
      }
      this.treeData = [...this.treeData]
    },
    editCatalogStyle() {
      this.$emit('editCatalogStyle')
    },
    onTreeNodeSelect(selectedKeys, info) {
      this.selectedKeys = selectedKeys
      this.layerCatalogInfo = info.node.$options.propsData.dataRef.slots
      this.editType = 'treeNode'
      if (this.layerCatalogInfo.catalogType === 'directory') {
        this.selectedNode = info.node
      }
      this.$emit('onTreeNodeClick', this.layerCatalogInfo)
    },
    addLayerClick() {
      this.addLayerVisible = true
      this.addrefresh = !this.addrefresh
    },
  },
}
</script>

<style scoped>
.div-range {
  min-height: 80vh;
  max-height: 80vh;
  overflow-y: auto;
  overflow-x: auto;
}
</style>
