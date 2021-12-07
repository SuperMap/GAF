<template>
  <catalog-manage
    :catalogTypeActiveKey="catalogTypeActiveKey"
    @selected="onSelected"
    @beforeAddSubCatalog="beforeAddSubCatalog"
    :searchPlaceholder="searchPlaceholder"
    @getTreeData="getTreeData"
    :openLeaf="openLeaf"
  >
    <template v-slot:cmanagement="{ iconNodeType }">
      <a-icon :type="iconNodeType.type === 12 ? 'tags' : 'tag'"></a-icon>
    </template>
    <api-manage
      :api-group="apiGrpId"
      :api-group-path="apiGroupPath"
      :dataOfTree="dataOfTree"
      @changeOpenLeaf="changeOpenLeaf"
    ></api-manage>
  </catalog-manage>
</template>
<script>
import ApiManage from '../../views/ComponentApi/ApiList'
import CatalogManage from '../SysCatalog/CatalogManage'
import '../../../common/css/common.css'
export default {
  components: {
    CatalogManage,
    ApiManage
  },
  data() {
    return {
      searchPlaceholder: '请输入API分组名称查询',
      // '1' 模块分组 '2' API分组 '3' 角色分组
      catalogTypeActiveKey: '2',
      // api相关
      apiGrpId: '',
      apiGroupPath: [],
      dataOfTree: [],
      openLeaf: false
    }
  },
  mounted() {},
  methods: {
    onSelected(selectedKeys, catalogAndParentPath, path) {
      this.apiGrpId = selectedKeys[0]
      this.apiGroupPath = path
      this.openLeaf = true
    },
    beforeAddSubCatalog(node, callback) {
      let isNext = true
      let info = ''
      if (node) {
        if (!node.children || node.children.length <= 0) {
          this.$axios
            .get(`/authority/auth-resource-apis/api-catalog-id/${node.key}`)
            .then(p => {
              const result = p.data
              if (result.isSuccessed) {
                if (result.data && result.data > 0) {
                  isNext = false
                  info = '该API组下有API'
                }
              } else {
                isNext = false
                info = '查询API组下API数量失败，请稍后重试'
              }
              callback(isNext, info)
            })
        } else {
          callback(isNext, info)
        }
      } else {
        isNext = false
        info = '未选择API组'
        callback(isNext, info)
      }
    },
    getTreeData(dataOfTree) {
      this.dataOfTree = dataOfTree
    },
    changeOpenLeaf(data) {
      this.openLeaf = data
    }
  }
}
</script>
