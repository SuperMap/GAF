<template>
  <div class="app-container">
    <!-- <div v-show="false" class="top">
      <a-tabs :default-active-key="defaultActiveKey">
        <a-tab-pane key="2">
          <span slot="tab">角色关联API</span>
        </a-tab-pane>
      </a-tabs>
    </div> -->
    <!-- <div class="page-left">
        <div class="tree-catalog">
          <span class="vertical-line">| </span>
          角色分配API
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
            <a-icon :type="iconNodeType.type === 5 ? tag : tags"></a-icon>
          </template>
        </gaf-tree-transparent>
    </div> -->
  <div class="page-left">
        <!-- <div class="main-top">
          <button
            @click="handleAdd"
            class="btn-fun blue"
          >
            <span><a-icon type="arrow-left" />添加至当前角色</span>
          </button>
        </div> -->
        <div class="selectroleApi">分配API到: {{roleId.roleName}}</div>
        <!-- <div class="tree-catalog">
          <span class="vertical-line">| </span>
          API分组目录
        </div> -->
          <gaf-tree-transparent
            ref="myGafTreeTransparent"
            :searchPlaceholder="searchPlaceholder2"
            :dataOfTree="dataOfTree2"
            :searchType="[0]"
            :expandedNodeKeys.sync="expandedNodeKeys2"
            :selectedKeys.sync="selectedNodeKeys2"
            @select="onSelect2"
          >
            <template v-slot:icon="{ iconNodeType }">
              <a-icon :type="iconNodeType.type === 5 ? tag : tags"></a-icon>
            </template>
          </gaf-tree-transparent>
    </div>

    <div class="page-right">
        <div class="main-top">
          <button
            @click="handleAdd"
            class="btn-fun blue btn-16"
          >
            保存
          </button>
          <button
            @click="handleBack"
            class="btn-fun blue"
          >
            返回
          </button>
        </div>
        <div>
          <add-edit-form
            ref="apiList"
            v-if="isApiList"
            :role-id="roleId.roleId"
            :apiGrpId="apiGrpId"
            :node-type="nodeType"
          ></add-edit-form>
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
  props: {
    roleId: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      tag: 'tag',
      tags: 'tags',
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
      searchPlaceholder2: '请输入API分组名查询',
      apiGrpId: '',
      isApiList: false,
      nodeType: 12
    }
  },
  mounted() {
    this.getNodesAndSetByType()
    this.getNodesAndSetByType2(this.defaultActiveKey2)
  },
  methods: {
    handleAdd() {
      if (this.isApiList) {
        this.$nextTick(function() {
          this.$refs.apiList.handleAdd()
        })
      } else {
        this.$message.warning('请选择API分组')
      }
    },
    async getNodesAndSetByType() {
      const url = `/authority/auth-roles/selftree`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.dataOfTree = this.$refs.myGafTreeTransparent.convertToTree(
          res.data
        )
      } else {
        this.$message.error('加载角色树失败,原因：' + res.message)
      }
    },
    async getNodesAndSetByType2(type) {
      const url = `/sys-mgt/sys-catalogs/nodes/type/${type}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.dataOfTree2 = this.$refs.myGafTreeTransparent.convertToTree(
          res.data
        )
      } else {
        this.$message.error('加载API分组树失败,原因：' + res.message)
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
    },
    handleBack() {
      this.$emit('back')
    },
  }
}
</script>

<style scoped>
.left {
  width: 20%;
  height: 100%;
  float: left;
  margin: 1% 0 1% 1%;
  padding: 0 1% 1% 0;
  overflow: hidden;
  overflow-x: auto;
  overflow-y: auto;
  padding-bottom: 1%;
}
.main-top {
  width: 100%;
  border-bottom: 1px solid rgba(204, 204, 204, 0.5);
}
.main-top-left {
  height: 87%;
  width: 25%;
  float: left;
  margin: 1% 0 1% 1%;
  padding: 0 1% 1% 0;
  overflow: hidden;
  overflow-x: auto;
  overflow-y: auto;
}
.main-top-right {
  height: 87%;
  width: 74%;
  float: left;
  border-left: 1px solid rgba(204, 204, 204, 0.5);
  overflow-x: auto;
  overflow-y: auto;
}
.main {
  height: 100%;
  width: 79%;
  float: left;
}
.role-btn {
  margin-left: 1%;
  height: 35px;
  background-color: rgba(84, 92, 100, 0.75);
  color: rgba(255, 255, 255, 0.7);
  font-weight: bold;
  border: none;
  box-shadow: 0 0 2px 2px rgba(84, 92, 100, 0.55);
  transition: linear 0.15s;
}
.role-btn:hover {
  color: white;
  box-shadow: 0 0 2px 2px rgba(84, 92, 100, 0.25);
}
.selectroleApi {
  font-size: 18px;
  padding: 5px 10px;
  border-radius: 5px;
  font-weight: bold;
  margin-bottom: 10px
}
</style>
