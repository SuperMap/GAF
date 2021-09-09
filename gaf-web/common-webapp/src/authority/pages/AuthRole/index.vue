<template>
  <div class="app-container">
    <div class="page-single">
      <catalog-manage
        :catalogTypeActiveKey="catalogTypeActiveKey"
        @selected="onSelected"
        @beforeAddSubCatalog="beforeAddSubCatalog"
        :searchPlaceholder="searchPlaceholder"
        @getTreeData="getTreeData"
        v-show="!open"
        :openLeaf="openLeaf"
      >
        <template v-slot:cmanagement="{ iconNodeType }">
          <a-icon
            :type="iconNodeType.type === 12 ? 'switcher' : 'trademark'"
          ></a-icon>
        </template>
        <role-manage
          :tenant="tenant"
          :role-group="roleGrpId"
          :role-group-path="roleGroupPath"
          :dataOfTree="dataOfTree"
          @openRoleApi="openRoleApi"
          @changeOpenLeaf="changeOpenLeaf"
        ></role-manage>
      </catalog-manage>
      <role-api
        v-if="open"
        :roleId="roleId"
        @back="back"
      ></role-api>
    </div>
  </div>
</template>
<script>
import RoleManage from '../../views/AuthRole/RoleList'
import CatalogManage from '../SysCatalog/CatalogManage'
import '../../../common/css/common.css'
import RoleApi from './RoleApi'
export default {
  components: {
    CatalogManage,
    RoleManage,
    RoleApi
  },
  data() {
    return {
      open: false,
      searchPlaceholder: '请输入角色组名称查询',
      // '1' 模块分组 '2' API分组 '3' 角色分组
      catalogTypeActiveKey: '3',
      // 角色相关
      tenant: '',
      roleGrpId: '',
      roleGroupPath: '无',
      dataOfTree: [],
      roleId: '',
      openLeaf: false
    }
  },
  mounted() {},
  methods: {
    onSelected(selectedKeys, catalogAndParentPath, path) {
      this.roleGrpId = selectedKeys[0]
      this.tenant = catalogAndParentPath.tenantId
      this.roleGroupPath = path
      this.openLeaf = true
    },
    beforeAddSubCatalog(node, callback) {
      let isNext = true
      let info = ''
      if (node) {
        if (!node.children || node.children.length <= 0) {
          this.$axios
            .get(`/authority/auth-roles/role-catalog-id/${node.key}`)
            .then(p => {
              const result = p.data
              if (result.isSuccessed) {
                if (result.data && result.data > 0) {
                  isNext = false
                  info = '该角色组下有角色'
                }
              } else {
                isNext = false
                info = '查询角色组下角色数量失败，请稍后重试'
              }
              callback(isNext, info)
            })
        } else {
          callback(isNext, info)
        }
      } else {
        isNext = false
        info = '未选择角色组'
        callback(isNext, info)
      }
    },
    getTreeData(dataOfTree) {
      this.dataOfTree = dataOfTree
    },
    back() {
      this.open = false
    },
    openRoleApi(data) {
      this.open = true
      this.roleId = data
    },
    changeOpenLeaf(data) {
      this.openLeaf = data
    }
  }
}
</script>
