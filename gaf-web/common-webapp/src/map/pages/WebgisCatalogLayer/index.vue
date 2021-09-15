<template>
  <div>
    <catalog-manage
      :root-catalog-id="rootCatalogId"
      :biz-types="bizTypes"
      :search-placeholder="searchPlaceholder"
      @backToList="backToList"
      @selected="onSelected"
      @beforeAddSubCatalog="beforeAddSubCatalog"
      :openLeaf="openLeaf"
      @LayersSelected="LayersSelected"
      @updataRootId="updataRootId"
    >
      <template v-slot:cmanagement="{ iconNodeType }">
        <a-icon :type="iconNodeType.type === 12 ? 'profile' : 'menu'"></a-icon>
      </template>
      <layer-list @changeOpen="changeOpen" :layer-catalog-id="layerCatalogId"></layer-list>
    </catalog-manage>
  </div>
</template>
<script>
import '../../../common/css/common.css'
import LayerList from './LayerList'
import CatalogManage from './CatalogManage'

export default {
  components: {
    CatalogManage,
    LayerList,
  },
  props: {
    rootCatalogId: {
      type: String,
      default: null,
    },
    bizTypes: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      searchPlaceholder: '请输入目录名称查询',
      // '1' 模块分组 '2' API分组 '3' 角色分组 '4' 菜单分组 '6' 图层目录
      catalogTypeActiveKey: '6',
      // 模块相关
      layerCatalogId: null,
      openLeaf: true
      // layerCatalogPath: []
    }
  },
  created() {},
  mounted() {},
  methods: {
    // 返回
    backToList() {
      this.$emit('backToList')
    },
    updataRootId(rootId) {
      this.$emit('updataRootId', rootId)
    },
    onSelected(selectedKeys) {
      this.layerCatalogId = selectedKeys[0]
      // this.layerCatalogPath = path
    },
    async LayersSelected (node){
      this.$axios
      .get(`/map/webgis-catalog-layers/count/${node.key}`)
      .then((p) => {
        const result = p.data
        if (result.isSuccessed) {
          if (result.data && result.data > 0) {
            this.openLeaf = false
          } else {
            this.openLeaf = true
          }
        }
      }
      )
    },
    beforeAddSubCatalog(node, callback) {
      let isNext = true
      let info = ''
      if (node) {
        if (!node.children || node.children.length <= 0) {
          this.$axios
            .get(`/map/webgis-catalog-layers/count/${node.key}`)
            .then((p) => {
              const result = p.data
              if (result.isSuccessed) {
                if (result.data && result.data > 0) {
                  this.openLeaf = false
                  isNext = false
                  info = '该资源目录下有图层'
                } else {
                  this.openLeaf = true
                }
              } else {
                isNext = false
                info = '查询资源目录下图层数量失败，请稍后重试'
              }
              callback(isNext, info)
            })
        } else {
          callback(isNext, info)
        }
      } else {
        isNext = false
        info = '未选择资源目录'
        callback(isNext, info)
      }
    },
    changeOpen(boolean) {
      this.openLeaf = boolean
    }
  },
}
</script>
<style scoped>
.left {
  height: 95vh !important;
}
</style>
