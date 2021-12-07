<template>
  <div class="app-container">
    <div class="top">
      <a-tabs
        @tabClick="handleTabClick"
        v-model="activeKey"
        :default-active-key="defaultActiveKey"
      >
        <a-tab-pane key="1">
          <span slot="tab">
            模块分组管理
          </span>
        </a-tab-pane>
        <a-tab-pane key="2">
          <span slot="tab">
            API分组管理
          </span>
        </a-tab-pane>
        <a-tab-pane key="3">
          <span slot="tab">
            角色分组管理
          </span>
        </a-tab-pane>
      </a-tabs>
    </div>
    <div class="bottom">
      <div class="page-left">
        <!-- <div class="tree-catalog">
          <span class="vertical-line">| </span>
          分组目录
        </div> -->
        <gaf-tree-transparent
          ref="myGafTreeTransparent"
          :searchPlaceholder="searchPlaceholder"
          :dataOfTree="dataOfTree"
          :searchType="[0]"
          :expandedNodeKeys.sync="expandedNodeKeys"
          :autoExpandParent="true"
          :selectedKeys.sync="selectedNodeKeys"
          @select="onSelect"
        ></gaf-tree-transparent>
      </div>
      <div class="page-right">
        <div class="main-top">
          <button @click="addSubCatalog" class="btn-fun blue">
            {{ addButtonName }}
          </button>
        </div>
        <add-edit-form
          ref="addEditForm"
          v-show="showAddEditForm"
          :editData="editData"
          :operation="operation"
          :options="dataOfTree"
          :catalogType="activeKey"
          @add-success="afterFormAddSuccess"
          @update-success="afterFormUpdateSuccess"
          @delete-success="afterDeleteSuccess"
          @cancleWhenAdd="cancleWhenAdd"
        >
        </add-edit-form>
      </div>
    </div>
  </div>
</template>

<script>
    import AddEditForm from '../../views/SysCatalog/AddOrEditForm'
    import '../../../common/css/common.css'

    export default {
  components: {
    AddEditForm
  },
  data() {
    return {
      defaultActiveKey: '1',
      activeKey: '1',
      dataOfTree: [],
      expandedNodeKeys: [],
      selectedNodeKeys: [],
      editData: { parentId: '0', parentPath: ['0'] },
      operation: 'edit',
      searchPlaceholder: '请输入名称模糊查询'
    }
  },
  computed: {
    showAddEditForm: function() {
      return this.dataOfTree && this.dataOfTree.length > 0
    },
    addButtonName: function() {
      if (this.activeKey === '1') {
        return '添加模块分组'
      } else if (this.activeKey === '2') {
        return '添加API分组'
      } else if (this.activeKey === '3') {
        return '添加角色分组'
      } else {
        return '添加子目录'
      }
    }
  },
  mounted() {
    this.getNodesAndSetByType(this.defaultActiveKey)
  },
  methods: {
    cancleWhenAdd(parentId) {
      if (parentId && parentId !== '0') {
        this.selectedNodeKeys = [parentId]
        this.getCatalogAndParentPathAndDo(parentId, catalogAndParentPath => {
          this.operation = 'edit'
          this.editData = catalogAndParentPath
          if (
            this.expandedNodeKeys.indexOf(catalogAndParentPath.parentId) < 0
          ) {
            this.expandedNodeKeys.push(catalogAndParentPath.parentId)
          }
        })
      }
    },
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
        this.dataOfTree = this.$refs.myGafTreeTransparent.deleteLeafChildren(
          this.$refs.myGafTreeTransparent.convertToTree(res.data)
        )
        if (this.dataOfTree && this.dataOfTree.length > 0) {
          this.getCatalogAndParentPathAndDo(
            this.dataOfTree[0].key,
            catalogAndParentPath => {
              this.operation = 'edit'
              this.editData = catalogAndParentPath
              this.selectedNodeKeys = [this.dataOfTree[0].key]
            }
          )
        }
      } else {
        this.$message.error('加载目录树失败,原因：' + res.message)
      }
    },
    // deprecated
    async getAllNodes() {
      const url = `/sys-mgt/sys-catalogs/all-nodes`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.dataOfTree = this.$refs.myGafTreeTransparent.convertToTree(
          res.data
        )
      } else {
        this.$message.error('加载目录树失败,原因：' + res.message)
      }
    },
    onSelect(selectedKeys) {
      if (selectedKeys != null && selectedKeys.length > 0) {
        const selectKey = selectedKeys[0]
        this.getCatalogAndParentPathAndDo(selectKey, catalogAndParentPath => {
          this.operation = 'edit'
          this.editData = catalogAndParentPath
        })
      }
    },
    getCatalogAndParentPathAndDo(catalogId, callback) {
      const url = `/sys-mgt/sys-catalogs/${catalogId}`
      this.$axios.get(url).then(p => {
        const result = p.data
        if (result.isSuccessed) {
          const catalog = result.data
          this.$axios
            .get(`/sys-mgt/sys-catalogs/parentPath/${catalogId}`)
            .then(p => {
              const result = p.data
              if (result.isSuccessed) {
                const parentPath = result.data
                callback({ ...catalog, parentPath: parentPath })
              } else {
                this.$message.error(`查询上级目录失败,原因:${result.message}`)
              }
            })
        } else {
          this.$message.error(`查询目录失败,原因:${result.message}`)
        }
      })
    },
    addSubCatalog() {
      if (this.selectedNodeKeys && this.selectedNodeKeys.length === 1) {
        this.getCatalogAndParentPathAndDo(
          this.selectedNodeKeys[0],
          catalogAndParentPath => {
            this.operation = 'add'
            this.$refs.addEditForm.resetFormFields()
            catalogAndParentPath.parentPath.push(catalogAndParentPath.catalogId)
            if (catalogAndParentPath.parentPath[0] === '0') {
              catalogAndParentPath.parentPath.splice(0, 1)
            }
            this.editData = {
              parentId: catalogAndParentPath.catalogId,
              parentPath: catalogAndParentPath.parentPath,
              type: catalogAndParentPath.type,
            }
          }
        )
      } else {
        this.$message.error('请选择一个目录')
      }
    },
    afterFormAddSuccess(catalog) {
      const type = catalog.parentId === '0' ? 11 : 12
      this.$refs.myGafTreeTransparent.inserNode({
        key: catalog.catalogId,
        title: catalog.name,
        sortSn: catalog.sortSn,
        parentId: catalog.parentId,
        type: type,
        scopedSlots: { title: 'title' }
      })
      if (this.expandedNodeKeys.indexOf(catalog.parentId) <= -1) {
        this.expandedNodeKeys.push(catalog.parentId)
      }
      this.selectedNodeKeys = [catalog.catalogId]
      this.getCatalogAndParentPathAndDo(
        catalog.catalogId,
        catalogAndParentPath => {
          this.operation = 'edit'
          this.editData = catalogAndParentPath
        }
      )
    },
    afterFormUpdateSuccess(catalog) {
      this.$refs.myGafTreeTransparent.updateNode({
        key: catalog.catalogId,
        title: catalog.name,
        sortSn: catalog.sortSn,
        parentId: catalog.parentId
      })
      this.editData = catalog
    },
    afterDeleteSuccess(catalog) {
      this.$refs.myGafTreeTransparent.deleteNode({
        key: catalog.catalogId,
        parentId: catalog.parentId
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
  width: 29%;
  float: left;
  overflow: hidden;
  overflow-x: auto;
  overflow-y: auto;
  padding: 1% 1% 0 0;
  margin: 0 0 1% 1%;
  border-top: 2px solid rgb(194, 194, 194);
  border-bottom: 2px solid rgb(194, 194, 194);
}

.main {
  height: 100%;
  width: 70%;
  float: left;
  border-top: 2px solid rgb(194, 194, 194);
  border-bottom: 2px solid rgb(194, 194, 194);
}
.main-top {
  padding-left: 16px;
  padding-bottom: 12px;
  margin-top: 12px;
  border-bottom: 1px solid rgba(194, 194, 194, 0.5);
}
</style>
