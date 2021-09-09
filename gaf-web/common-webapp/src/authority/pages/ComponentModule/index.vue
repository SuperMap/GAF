<template>
  <div class="app-container">
    <div class="page-single">
      <module-api v-if="moduleApiopen" :selectModule="moduleId" @back="moduleApiClose">
      </module-api>
      <catalog-manage
        v-show="!moduleApiopen"
        :catalogTypeActiveKey="catalogTypeActiveKey"
        @selected="onSelected"
        @beforeAddSubCatalog="beforeAddSubCatalog"
        @getTreeData="getTreeData"
        :searchPlaceholder="searchPlaceholder"
        :openLeaf="openLeaf"
      >
        <template v-slot:cmanagement="{ iconNodeType }">
          <a-icon
            :type="iconNodeType.type === 12 ? 'appstore' : 'calendar'"
          ></a-icon>
        </template>
        <module-manage
          :component="component"
          :moduleGroup="moduleGrpId"
          :moduleGroupPath="moduleGroupPath"
          @moduleApiVisible="moduleApiVisible"
          :dataOfTree="dataOfTree"
          @changeOpenLeaf="changeOpenLeaf"
        ></module-manage>
      </catalog-manage>
    </div>
  </div>
  <!-- <catalog-manage
    :catalogTypeActiveKey="catalogTypeActiveKey"
    @selected="onSelected"
    @beforeAddSubCatalog="beforeAddSubCatalog"
    :searchPlaceholder="searchPlaceholder"
  >
    <template v-slot:cmanagement="{ iconNodeType }">
      <a-icon
        :type="iconNodeType.type === 12 ? 'appstore' : 'calendar'"
      ></a-icon>
    </template>
    <module-manage
      :component="component"
      :moduleGroup="moduleGrpId"
      :moduleGroupPath="moduleGroupPath"
    ></module-manage>
  </catalog-manage> -->
</template>
<script>
import ModuleManage from '../../views/ComponentModule/ModuleList'
import CatalogManage from '../SysCatalog/CatalogManage'
import ModuleApi from '../ModuleApi'
import '../../../common/css/common.css'

export default {
  components: {
    CatalogManage,
    ModuleManage,
    ModuleApi
  },
  data() {
    return {
      searchPlaceholder: '请输入模块名称查询',
      // '1' 模块分组 '2' API分组 '3' 角色分组
      catalogTypeActiveKey: '1',
      // 模块相关
      component: '',
      moduleGrpId: '',
      moduleGroupPath: [],
      moduleApiopen: false,
      moduleId: '',
      dataOfTree: [],
      openLeaf: false
    }
  },
  watch: {
    openLeaf(newdata){
      console.log(newdata)
    }
  },
  mounted() {},
  methods: {
    moduleApiVisible(r) {
      this.moduleApiopen = true
      this.moduleId = r
    },
    moduleApiClose() {
      this.moduleApiopen = false
    },
    onSelected(selectedKeys, catalogAndParentPath, path) {
      this.moduleGrpId = selectedKeys[0]
      this.component = catalogAndParentPath.sysComponentId
      this.moduleGroupPath = path
      this.openLeaf = true
    },
    beforeAddSubCatalog(node, callback) {
      let isNext = true
      let info = ''
      if (node) {
        if (!node.children || node.children.length <= 0) {
          this.$axios
            .get(
              `/authority/auth-resource-modules/module-catalog-id/${node.key}`
            )
            .then(p => {
              const result = p.data
              if (result.isSuccessed) {
                if (result.data && result.data > 0) {
                  isNext = false
                  info = '该模块组下有模块'
                }
              } else {
                isNext = false
                info = '查询模块组下模块数量失败，请稍后重试'
              }
              callback(isNext, info)
            })
        } else {
          callback(isNext, info)
        }
      } else {
        isNext = false
        info = '未选择模块分组'
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
