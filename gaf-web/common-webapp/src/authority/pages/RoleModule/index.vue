<template>
  <div class="app-container">
    <div class="top">
      <a-tabs :default-active-key="defaultActiveKey">
        <a-tab-pane key="2">
          <span slot="tab">角色关联模块</span>
        </a-tab-pane>
      </a-tabs>
    </div>
    <div class="bottom">
      <div class="page-left">
        <div class="tree-catalog">
          <span class="vertical-line">| </span>
          菜单分组目录
        </div>
        <gaf-tree-transparent
          ref="myGafTreeTransparent"
          :searchPlaceholder="searchPlaceholder"
          :dataOfTree="dataOfTree"
          :searchType="[0]"
          :expandedNodeKeys.sync="expandedNodeKeys"
          :selectedKeys.sync="selectedNodeKeys"
          @select="onSelect"
        >
          <template v-slot:icon="{ iconNodeType }">
            <a-icon
              :type="iconNodeType.type === 5 ? calendar : appstore"
            ></a-icon>
          </template>
        </gaf-tree-transparent>
      </div>
      <div class="page-right">
        <div class="main-top"></div>
        <div class="main-top-left">
          <div class="tree-catalog">
          <span class="vertical-line">| </span>
          菜单分组目录
        </div>
          <gaf-tree-transparent
            ref="myGafTreeTransparent"
            :searchPlaceholder="searchPlaceholder2"
            :dataOfTree="dataOfTree2"
            :searchType="[0]"
            :expandedNodeKeys.sync="expandedNodeKeys2"
            :selectedKeys.sync="selectedNodeKeys2"
            :checkable="true"
            :checkNodeStrictly="false"
            v-model="checkedNodeKeys"
            @select="onSelect2"
          >
            <template v-slot:icon="{ iconNodeType }">
              <a-icon
                :type="iconNodeType.type === 5 ? calendar : appstore"
              ></a-icon>
            </template>
          </gaf-tree-transparent>
        </div>
        <div class="main-top-right">
          <button
            @click="handleAdd"
            class="btn-fun blue"
            ><span><a-icon type="arrow-left" />添加至当前角色</span></button
          >
          <add-edit-form
            v-if="false"
            :role-id="roleId"
            :apiGrpId="apiGrpId"
            :node-type="nodeType"
          ></add-edit-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import AddEditForm from '../../views/RoleApi/ApiList'
import '~/assets/css/common.css'

export default {
  components: {
    AddEditForm
  },
  data() {
    return {
      calendar: 'calendar',
      appstore: 'appstore',
      checkedNodeKeys: [],
      defaultActiveKey: '3',
      defaultActiveKey2: '2',
      dataOfTree: [],
      dataOfTree2: [],
      expandedNodeKeys: [],
      expandedNodeKeys2: [],
      selectedNodeKeys: [],
      selectedNodeKeys2: [],
      editData: { parentId: '0', parentPath: ['0'] },
      operation: 'edit',
      searchPlaceholder: '请输入角色名查询',
      searchPlaceholder2: '请输入模块名查询',
      roleId: '',
      apiGrpId: '',
      isApiList: false,
      nodeType: 12,
      moduleTree: []
    }
  },
  watch: {
    roleId: function() {
      this.getSelected()
    }
  },
  created() {
    this.getSelected()
  },
  mounted() {
    this.getNodesAndSetByType()
    this.getNodesAndSetByType2(this.defaultActiveKey2)
  },
  methods: {
    async getSelected() {
      const url = `/authority/auth-role-module/list-by-role/${this.roleId}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        if (res.data !== null && res.data.length > 0) {
          this.checkedNodeKeys = res.data.map(item => item.resourceModuleId)
        } else {
          this.checkedNodeKeys = []
        }
      }
    },
    async handleAdd() {
      if (this.nodeType !== 5) {
        this.$message.info('请先选中一个角色')
        return
      }
      const checkedModules = this.moduleTree
        .filter(
          item =>
            this.checkedNodeKeys.indexOf(item.key) !== -1 && item.type === 7
        )
        .map(i => i.key)
      const url = `/authority/auth-role-module/handle`
      const data = {
        roleId: this.roleId,
        moduleList: checkedModules
      }
      const res = await this.$axios.$post(url, data)
      if (res.isSuccessed) {
        this.$message.success('添加成功')
      } else {
        this.$message.error('添加失败')
      }
    },
    async getNodesAndSetByType() {
      const url = `/authority/auth-roles/tree`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.dataOfTree = this.$refs.myGafTreeTransparent.convertToTree(
          res.data
        )
      } else {
        this.$message.error('加载角色树失败,原因：' + res.message)
      }
    },
    async getNodesAndSetByType2() {
      const url = `/authority/auth-resource-modules/tree`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.moduleTree = res.data
        this.dataOfTree2 = this.$refs.myGafTreeTransparent.convertToTree(
          res.data
        )
      } else {
        this.$message.error('加载模块树失败,原因：' + res.message)
      }
    },
    onSelect(selectedKeys, e) {
      this.roleId = e.node.dataRef.key
      this.nodeType = e.node.dataRef.type
    },
    onSelect2(selectedKeys, e) {
      this.isApiList =
        !e.node.dataRef.children || e.node.dataRef.children.length === 0
      this.apiGrpId = e.node.dataRef.key
    }
  }
}
</script>
<style scoped>
.app-container {
  height: 100%;
  width: 100%;
}
.bottom {
  height: 100%;
  width: 100%;
}
.left {
  padding: 1%;
  height: 100%;
  width: 20%;
  float: left;
  overflow: hidden;
  overflow-y: auto;
  overflow-x: auto;
  border-top: 2px solid rgb(194, 194, 194);
}
.main-top-left {
  height: 87%;
  width: 25%;
  float: left;
  padding: 1%;
  overflow: hidden;
  overflow-y: auto;
  overflow-x: auto;
}
.main-top-right {
  padding: 1%;
  height: 87%;
  width: 75%;
  float: left;
  border: 1px solid rgba(204, 204, 204, 0.5);
}
.main {
  height: 100%;
  width: 80%;
  float: left;
  border-top: 2px solid rgb(194, 194, 194);
}
.main-top {
  margin-left: 16px;
}
</style>
