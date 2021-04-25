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
      <a-icon :type="iconNodeType.type === 12 ? 'profile' : 'menu'"></a-icon>
    </template>
    <menu-manage
      :menuGroup="menuGrpId"
      :menuGroupPath="menuGroupPath"
      :dataOfTree="dataOfTree"
      @changeOpenLeaf="changeOpenLeaf"
    ></menu-manage>
  </catalog-manage>
</template>
<script>
import menuManage from '../../views/Menu/MenuList'
import CatalogManage from '../SysCatalog/CatalogManage'
import '~/assets/css/common.css'
export default {
  components: {
    CatalogManage,
    menuManage
  },
  data() {
    return {
      searchPlaceholder: '请输入菜单名称查询',
      // '1' 模块分组 '2' API分组 '3' 角色分组 '4' 菜单分组
      catalogTypeActiveKey: '4',
      // 模块相关
      menuGrpId: '',
      menuGroupPath: [],
      dataOfTree: [],
      openLeaf: false
    }
  },
  mounted() {},
  methods: {
    onSelected(selectedKeys, catalogAndParentPath, path) {
      this.menuGrpId = selectedKeys[0]
      this.menuGroupPath = path
      // this.openLeaf = true
    },
    beforeAddSubCatalog(node, callback) {
      let isNext = true
      let info = ''
      if (node) {
        if (!node.children || node.children.length <= 0) {
          this.$axios
            .get(`/authority/auth-resource-menus/menu-catalog-id/${node.key}`)
            .then(p => {
              const result = p.data
              if (result.isSuccessed) {
                if (result.data && result.data > 0) {
                  isNext = false
                  info = '该菜单组下有菜单'
                }
              } else {
                isNext = false
                info = '查询菜单组下菜单数量失败，请稍后重试'
              }
              callback(isNext, info)
            })
        } else {
          callback(isNext, info)
        }
      } else {
        isNext = false
        info = '未选择菜单分组'
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
