<template>
  <div class="app-container">
    <div v-show="false" class="top">
      <a-tabs
        @tabClick="handleTabClick"
        v-model="catalogTypeActiveKey"
        activedefault-active-key
      >
        <a-tab-pane key="1">
          <span slot="tab">模块分组管理</span>
        </a-tab-pane>
        <a-tab-pane key="2">
          <span slot="tab">API分组管理</span>
        </a-tab-pane>
        <a-tab-pane key="3">
          <span slot="tab">角色分组管理</span>
        </a-tab-pane>
        <a-tab-pane key="4">
          <span slot="tab">菜单分组管理</span>
        </a-tab-pane>
      </a-tabs>
    </div>
    <div>
      <div class="page-left">
        <div class="tree-catalog">
          <span class="vertical-line">| </span>
          <span v-if="catalogTypeActiveKey === '1'">模块分组</span>
          <span v-if="catalogTypeActiveKey === '2'">API分组</span>
          <span v-if="catalogTypeActiveKey === '3'">角色分组</span>
          <span v-if="catalogTypeActiveKey === '4'">菜单分组</span>
        </div>
        <gaf-tree-transparent
          ref="myGafTreeTransparent"
          :searchPlaceholder="searchPlaceholder"
          :dataOfTree="dataOfTree"
          :searchType="[0]"
          :expandedNodeKeys.sync="expandedNodeKeys"
          :autoExpandParent="true"
          :selectedKeys.sync="selectedNodeKeys"
          @select="onSelect"
        >
          <template v-slot:icon="{ iconNodeType }">
            <slot
              v-bind:iconNodeType="{ type: iconNodeType.type }"
              name="cmanagement"
            ></slot>
          </template>
        </gaf-tree-transparent>
      </div>
      <div class="page-right">
        <a-tabs v-model="activeKey" default-active-key>
          <a-tab-pane key="4" :tab="tab4Name">
            <!-- <div class="margin-15">
              <button @click="addSubCatalog" class="btn-fun blue">
                {{ addButtonName }}
              </button>
            </div> -->
            <div class="margin-15">
            <add-edit-form
              ref="addEditForm"
              v-show="showAddEditForm"
              :editData="editData"
              :operation="operation"
              :options="dataOfTree"
              :catalogType="catalogTypeActiveKey"
              @add-success="afterFormAddSuccess"
              @update-success="afterFormUpdateSuccess"
              @delete-success="afterDeleteSuccess"
              @cancleWhenAdd="cancleWhenAdd"
            ></add-edit-form>
            </div>
          </a-tab-pane>
          <a-tab-pane key="5" v-if="catalogTypeActiveKey === '4' ? true : isShowTab5" :tab="tab5Name">
            <slot></slot>
          </a-tab-pane>
          <a-tab-pane key="6" v-if="catalogTypeActiveKey === '4' ? true : openLeaf" :tab="tab6Name">
            <leaf-manage
              :leaf-list="LeafList"
              :data-of-tree="dataOfTree"
              :editData="editData"
              :catalogType="catalogTypeActiveKey"
              :operation1="operation"
              :addButtonName="addButtonName"
              @afterFormAddSuccess="afterFormAddSuccess"
              @afterFormUpdateSuccess="afterFormUpdateSuccess"
              @afterDeleteSuccess="afterDeleteSuccess"
              @cancleWhenAdd="cancleWhenAdd"
              @addSubCatalog="addSubCatalog"
              :openadd="openadd"
            ></leaf-manage>
          </a-tab-pane>
        </a-tabs>
      </div>
    </div>
  </div>
</template>

<script>
    import AddEditForm from '../../views/SysCatalog/AddOrEditForm'
    import LeafManage from './LeafManage'

    export default {
  components: {
    AddEditForm,
    LeafManage
  },
  props: {
    catalogTypeActiveKey: {
      type: String,
      default: '1'
    },
    searchPlaceholder: {
      type: String,
      default: ''
    },
    openLeaf: {
      type: Boolean,
      dafault: true
    }
  },
  data() {
    return {
      LeafList: [],
      dataOfTree: [],
      expandedNodeKeys: [],
      selectedNodeKeys: [],
      editData: { parentId: '0', parentPath: ['0'] },
      operation: 'edit',
      // ew
      activeKey: '4',
      isShowTab5: true,
      isShowTab6: false,
      openadd: false
    }
  },
  watch: {
    activeKey(newdata){
      if (newdata === '4'){
        this.operation = 'edit'
      }
    }
  },
  computed: {
    // isShowTab5: function() {
    //   return !(this.dataOfTree[0].children && this.dataOfTree[0].children.length > 0)
    // },
    showAddEditForm: function() {
      return this.dataOfTree && this.dataOfTree.length > 0
    },
    addButtonName: function() {
      if (this.catalogTypeActiveKey === '1') {
        return '添加模块分组'
      } else if (this.catalogTypeActiveKey === '2') {
        return '添加API分组'
      } else if (this.catalogTypeActiveKey === '3') {
        return '添加角色分组'
      } else if (this.catalogTypeActiveKey === '4') {
        return '添加菜单分组'
      } else {
        return '添加子目录'
      }
    },
    tab4Name: function() {
      if (this.catalogTypeActiveKey === '1') {
        return '模块组'
      } else if (this.catalogTypeActiveKey === '2') {
        return 'API组'
      } else if (this.catalogTypeActiveKey === '3') {
        return '角色组'
      } else if (this.catalogTypeActiveKey === '4') {
        return '菜单组'
      } else {
        return '分组'
      }
    },
    tab5Name: function() {
      if (this.catalogTypeActiveKey === '1') {
        return '模块项'
      } else if (this.catalogTypeActiveKey === '2') {
        return 'API项'
      } else if (this.catalogTypeActiveKey === '3') {
        return '角色项'
      } else if (this.catalogTypeActiveKey === '4') {
        return '菜单项'
      } else {
        return ''
      }
    },
    tab6Name: function() {
      if (this.catalogTypeActiveKey === '1') {
        return '模块子组'
      } else if (this.catalogTypeActiveKey === '2') {
        return 'API子组'
      } else if (this.catalogTypeActiveKey === '3') {
        return '角色子组'
      } else if (this.catalogTypeActiveKey === '4') {
        return '菜单子组'
      } else {
        return ''
      }
    }
  },
  async mounted() {
    await this.getNodesAndSetByType(this.catalogTypeActiveKey)
    this.LeafList = this.dataOfTree[0].children
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
        this.dataOfTree = this.$refs.myGafTreeTransparent.convertToTree(
          res.data
        )
        if (this.dataOfTree && this.dataOfTree.length > 0) {
          if (
            !this.dataOfTree[0].children ||
            this.dataOfTree[0].children.length === 0
          ) {
            // this.isShowTab5 = true
            this.activeKey = '5'
            // this.activeKey = '4'
          } else {
            // this.isShowTab5 = false
            this.activeKey = '4'
          }

          this.getCatalogAndParentPathAndDo(
            this.dataOfTree[0].key,
            catalogAndParentPath => {
              this.operation = 'edit'
              this.editData = catalogAndParentPath
              this.selectedNodeKeys = [this.dataOfTree[0].key]
              const copyCatalogAndParentPath = { ...catalogAndParentPath }
              this.$emit(
                'selected',
                this.selectedNodeKeys,
                copyCatalogAndParentPath,
                [this.dataOfTree[0]]
              )
            }
          )
        }
        this.isShowTab5 = !(this.dataOfTree[0].children && this.dataOfTree[0].children.length > 0)
        let datatree = JSON.parse(JSON.stringify(this.dataOfTree))
        this.$emit('getTreeData', datatree)
      } else {
        this.$message.error('加载目录树失败,原因：' + res.message)
      }
    },
    getChildren(key) {
      this.dataOfTree.forEach(item => {
        if(item.key === key) {
          this.LeafList = item.children
        }
      })
    },
    onSelect(selectedKeys, e) {
      // console.log(selectedKeys,e,'xz')
      // this.getChildren(selectedKeys)

      this.LeafList = e.node.dataRef.children
      if (selectedKeys && selectedKeys.length > 0) {
        if (!e.node.dataRef.children || e.node.dataRef.children.length === 0) {
          this.isShowTab5 = true
          // this.activeKey = '4'
          this.activeKey = '5'
        } else {
          this.isShowTab5 = false
          this.activeKey = '4'
        }
        const selectKey = selectedKeys[0]
        const path = []
        this.$refs.myGafTreeTransparent.getPath(
          this.dataOfTree,
          selectKey,
          path
        )
        this.getCatalogAndParentPathAndDo(selectKey, catalogAndParentPath => {
          this.operation = 'edit'
          this.editData = catalogAndParentPath
          const copyCatalogAndParentPath = { ...catalogAndParentPath }
          this.$emit('selected', selectedKeys, copyCatalogAndParentPath, path)
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
                this.$message.error(`查询上级分组失败,原因:${result.message}`)
              }
            })
        } else {
          this.$message.error(`查询分组信息失败,原因:${result.message}`)
        }
      })
    },
    addSubCatalog() {
      this.operation = 'add'
      if (this.selectedNodeKeys && this.selectedNodeKeys.length === 1) {
        this.openadd = true
        const node = this.$refs.myGafTreeTransparent.getNode(
          this.selectedNodeKeys[0]
        )
        this.$emit('beforeAddSubCatalog', node, () => {
          // if (isNext) {
            this.getCatalogAndParentPathAndDo(
              this.selectedNodeKeys[0],
              catalogAndParentPath => {
                this.operation = 'add'
                this.$refs.addEditForm.resetFormFields()
                catalogAndParentPath.parentPath.push(
                  catalogAndParentPath.catalogId
                )
                if (catalogAndParentPath.parentPath[0] === '0') {
                  catalogAndParentPath.parentPath.splice(0, 1)
                }
                this.editData = {
                  parentId: catalogAndParentPath.catalogId,
                  parentPath: catalogAndParentPath.parentPath,
                  type: catalogAndParentPath.type,
                  sysComponentId: catalogAndParentPath.sysComponentId
                }
              }
            )
          // }
          // else {
          //   this.$message.info(`不能添加子分组，原因：${info}`)
          // }
        })
      } else {
        this.openadd = false
        this.$message.info('请选择一个分组')
      }
    },
    afterFormAddSuccess(catalog) {
      if (this.catalogTypeActiveKey !== '4'){
        this.isShowTab5 = false
      }
      const type = 12
      this.$refs.myGafTreeTransparent.inserNode({
        key: catalog.catalogId,
        title: catalog.name,
        sortSn: catalog.sortSn,
        parentId: catalog.parentId,
        type: type,
        scopedSlots: { title: 'title' },
        children: []
      })
      if (this.expandedNodeKeys.indexOf(catalog.parentId) <= -1) {
        this.expandedNodeKeys.push(catalog.parentId)
      }
      // this.selectedNodeKeys = [catalog.catalogId]
      this.getCatalogAndParentPathAndDo(
        catalog.catalogId,
        catalogAndParentPath => {
          this.operation = 'edit'
          this.editData = catalogAndParentPath
        }
      )
      let datatree = JSON.parse(JSON.stringify(this.dataOfTree))
      this.$emit('getTreeData', datatree)
      this.$emit('selected', this.selectedNodeKeys, 'copyCatalogAndParentPath', [])
    },
    afterFormUpdateSuccess(catalog) {
      this.$refs.myGafTreeTransparent.updateNode({
        key: catalog.catalogId,
        title: catalog.name,
        sortSn: catalog.sortSn,
        parentId: catalog.parentId
      })
      if (this.expandedNodeKeys.indexOf(catalog.parentId) <= -1) {
        this.expandedNodeKeys.push(catalog.parentId)
      }
      let datatree = JSON.parse(JSON.stringify(this.dataOfTree))
      this.$emit('getTreeData', datatree)
      this.editData = catalog
    },
    afterDeleteSuccess(catalog,boolean) {
      if (this.catalogTypeActiveKey !== '4' && boolean){
        this.isShowTab5 = boolean
      }
      // this.$refs.myGafTreeTransparent.deleteNode({
      //   key: catalog.catalogId,
      //   parentId: catalog.parentId
      // })
      console.log(catalog)
      if (catalog.key && catalog.key.length > 0){
          catalog.key.forEach(item => {
            this.$refs.myGafTreeTransparent.deleteNode({
              key: item,
              parentId: catalog.parentId
            })
          })
      } else {
        this.$refs.myGafTreeTransparent.deleteNode({
          key: catalog.catalogId,
          parentId: catalog.parentId
        })
      }
    },
    addDisabled(data) {
      data.forEach(item => {
        if (item.children.length > 0){
          item.disabled = true
          this.addDisabled(item.children)
        }
      })
      return data
    }
  }
}
</script>
