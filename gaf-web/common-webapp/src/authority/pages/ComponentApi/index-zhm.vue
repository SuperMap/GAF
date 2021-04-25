<template>
  <div class="app-container">
    <div class="top">
      <a-tabs @tabClick="handleTabClick" :default-active-key="defaultActiveKey">
        <a-tab-pane key="2">
          <span slot="tab">
            API分组管理
          </span>
        </a-tab-pane>
      </a-tabs>
    </div>
    <div class="bottom">
      <div class="left">
        <gaf-tree-transparent
          ref="myGafTreeTransparent"
          :searchPlaceholder="searchPlaceholder"
          :dataOfTree="dataOfTree"
          :searchType="[0]"
          :expandedNodeKeys.sync="expandedNodeKeys"
          :selectedKeys.sync="selectedNodeKeys"
          @select="onSelect"
        ></gaf-tree-transparent>
      </div>
      <div class="main">
        <div class="main-top"></div>
        <add-edit-form
          v-if="isApiList"
          :component="component"
          :api-group="apiGrpId"
        ></add-edit-form>
      </div>
    </div>
  </div>
</template>

<script>
    import AddEditForm from '../../views/ComponentApi/ApiList'

    export default {
  components: {
    AddEditForm
  },
  data() {
    return {
      defaultActiveKey: '2',
      dataOfTree: [],
      expandedNodeKeys: [],
      selectedNodeKeys: [],
      editData: { parentId: '0', parentPath: ['0'] },
      operation: 'edit',
      searchPlaceholder: '请输入名称模糊查询',
      isApiList: false,
      component: '',
      apiGrpId: ''
    }
  },
  computed: {
    showAddEditForm: function() {
      return this.dataOfTree && this.dataOfTree.length > 0
    }
  },
  mounted() {
    this.getNodesAndSetByType(this.defaultActiveKey)
  },
  methods: {
    handleTabClick(key) {
      this.selectedNodeKeys = []
      this.expandedNodeKeys = []
      this.editData = { parentId: '0', parentPath: ['0'] }
      this.operation = 'edit'
      this.getNodesAndSetByType(key)
      this.$refs.myGafTreeTransparent.setInputSearchValueAndOnSearch('')
    },
    async getNodesAndSetByType(type) {
      const url = `/sys-mgt/sys-catalogs/nodes/type/${type}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.dataOfTree = this.$refs.myGafTreeTransparent.convertToTree(
          res.data
        )
      } else {
        this.$message.error('加载目录树失败,原因：' + res.message)
      }
    },
    onSelect(selectedKeys, e) {
      this.isApiList = e.node.dataRef.children.length === 0
      this.apiGrpId = e.node.dataRef.key
      this.getCatalogById(e.node.dataRef.key)
    },
    getCatalogById(catalogId) {
      const url = `/sys-mgt/sys-catalogs/${catalogId}`
      this.$axios.get(url).then(p => {
        const result = p.data
        if (result.isSuccessed) {
          this.component = result.data.sysComponentId
        } else {
          this.$message.error(`查询失败,原因:${result.message}`)
        }
      })
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
  height: 100%;
  width: 30%;
  border: 1px solid #ccc;
  float: left;
}

.main {
  height: 100%;
  width: 70%;
  border: 1px solid #ccc;
  float: left;
}
.main-top {
  margin-left: 16px;
}
</style>
