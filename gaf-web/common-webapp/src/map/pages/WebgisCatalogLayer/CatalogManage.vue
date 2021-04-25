<template>
  <div>
    <div class="resource-left">
      <div class="tree-catalog">
        <span class="vertical-line">| </span>
        资源目录
      </div>
      <div>
        <button
          style="height: 40px; border-top-right-radius: 20px"
          class="btn-fun gray"
          @click="backToList"
          ><a-icon type="rollback" />返回</button
        >
      </div>
      <gaf-tree-transparent
        ref="myGafTreeTransparent"
        style="padding-top: 7px"
        :search-placeholder="searchPlaceholder"
        :data-of-tree="dataOfTree"
        :search-type="[0]"
        :expanded-node-keys.sync="expandedNodeKeys"
        :auto-expand-parent="true"
        :selected-keys.sync="selectedNodeKeys"
        @select="onSelect"
      >
        <template v-slot:icon="{ iconNodeType }">
          <slot
            :iconNodeType="{ type: iconNodeType.type }"
            name="cmanagement"
          ></slot>
        </template>
      </gaf-tree-transparent>
    </div>
    <div class="right">
      <a-tabs v-model="activeKey" default-active-key>
        <a-tab-pane key="4" :tab="tab4Name">
          <!-- <div>
            <button
              class="btn-fun blue"
              style="margin: 10px 0"
              @click="addSubCatalog"
              v-show="dataOfTree && dataOfTree.length > 0"
            >
              {{ addButtonName }}
            </button>
          </div> -->
          <div class="margin-15">
          <add-edit-form
            ref="addEditForm"
            :edit-data="editData"
            :operation="operation"
            :options="dataOfTree"
            :catalog-type="catalogType"
            :biz-types="bizTypes"
            @add-success="afterFormAddSuccess"
            @update-success="afterFormUpdateSuccess"
            @delete-success="afterDeleteSuccess"
            @cancleWhenAdd="cancleWhenAdd"
          ></add-edit-form>
          </div>
        </a-tab-pane>
        <a-tab-pane v-if="isShowTab5" key="5" :tab="tab5Name">
          <slot></slot>
        </a-tab-pane>
        <a-tab-pane v-if="openLeaf" key="6" :tab="tab6Name">
          <leaf-manage
              :leaf-list="LeafList"
              :data-of-tree="dataOfTree"
              :editData="editData"
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
</template>

<script>
    import '~/assets/css/common.css'
    import AddEditForm from './AddOrEditForm.vue'
    import LeafManage from './LeafManage'

    export default {
  components: {
    AddEditForm,
    LeafManage
  },
  props: {
    rootCatalogId: {
      type: String,
      default: null,
    },
    searchPlaceholder: {
      type: String,
      default: '请输入资源目录名称搜索',
    },
    bizTypes: {
      type: Array,
      default: () => [],
    },
    openLeaf: {
      type: Boolean,
      dafault: true
    }
  },
  data() {
    return {
      catalogType: '6',
      dataOfTree: [],
      expandedNodeKeys: [],
      selectedNodeKeys: [],
      editData: {
        parentId: '0',
        parentPath: ['0'],
        type: 6,
        sysComponentId: '',
      },
      operation: 'add',
      // ew
      activeKey: '4',
      isShowTab5: false,
      LeafList: [],
      openadd: false
    }
  },
  computed: {
    addButtonName() {
      if (this.dataOfTree && this.dataOfTree.length > 0) {
        return '添加子目录'
      } else {
        return '添加根目录'
      }
    },
    tab4Name() {
      return '资源目录'
    },
    tab5Name() {
      return '图层'
    },
    tab6Name() {
      return '添加子目录'
    },
  },
  async created() {
    if (this.rootCatalogId && this.rootCatalogId !== '') {
      await this.getTreeByRoot(this.rootCatalogId)
      this.LeafList = this.dataOfTree[0].children
    }
  },
  mounted() {
    // this.getNodesAndSetByRoot(this.rootCatalogId)
    // this.getNodesAndSetByType(this.catalogType)
  },
  methods: {
    // 返回
    backToList() {
      this.$emit('backToList')
    },
    cancleWhenAdd(parentId) {
      if (parentId && parentId !== '0') {
        this.selectedNodeKeys = [parentId]
        this.getCatalogAndParentPathAndDo(parentId, (catalogAndParentPath) => {
          this.operation = 'edit'
          this.editData = catalogAndParentPath
          if (!this.expandedNodeKeys.includes(catalogAndParentPath.parentId)) {
            this.expandedNodeKeys.push(catalogAndParentPath.parentId)
          }
        })
      }
    },
    // 根据根节点查询树
    async getTreeByRoot(rootCatalogId) {
      const res = await this.$axios.$get(
        `/sys-mgt/sys-catalogs/root/${rootCatalogId}`
      )
      if (res.isSuccessed) {
        this.dataOfTree = this.$refs.myGafTreeTransparent.convertToTree(
          res.data
        )
        if (this.dataOfTree && this.dataOfTree.length > 0) {
          if (
            !this.dataOfTree[0].children ||
            this.dataOfTree[0].children.length === 0
          ) {
            this.isShowTab5 = true
            this.activeKey = '5'
          } else {
            this.isShowTab5 = false
            this.activeKey = '4'
          }

          this.getCatalogAndParentPathAndDo(
            this.dataOfTree[0].key,
            (catalogAndParentPath) => {
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
      } else {
        this.$message.error(`查询目录树失败，原因:${res.message}`)
      }
    },
    // async getNodesAndSetByType(type) {
    //   const url = `/sys-mgt/sys-catalogs/nodes/type/${type}`
    //   const res = await this.$axios.$get(url)
    //   if (res.isSuccessed) {
    //     this.dataOfTree = this.$refs.myGafTreeTransparent.convertToTree(
    //       res.data
    //     )
    //     if (this.dataOfTree && this.dataOfTree.length > 0) {
    //       if (
    //         !this.dataOfTree[0].children ||
    //         this.dataOfTree[0].children.length === 0
    //       ) {
    //         this.isShowTab5 = true
    //         this.activeKey = '5'
    //       } else {
    //         this.isShowTab5 = false
    //         this.activeKey = '4'
    //       }

    //       this.getCatalogAndParentPathAndDo(
    //         this.dataOfTree[0].key,
    //         catalogAndParentPath => {
    //           this.operation = 'edit'
    //           this.editData = catalogAndParentPath
    //           this.selectedNodeKeys = [this.dataOfTree[0].key]
    //           const copyCatalogAndParentPath = { ...catalogAndParentPath }
    //           this.$emit(
    //             'selected',
    //             this.selectedNodeKeys,
    //             copyCatalogAndParentPath,
    //             [this.dataOfTree[0]]
    //           )
    //         }
    //       )
    //     }
    //   } else {
    //     this.$message.error('加载目录树失败,原因：' + res.message)
    //   }
    // },
    onSelect(selectedKeys, e) {
      this.LeafList = e.node.dataRef.children
      if (selectedKeys && selectedKeys.length > 0) {
        if (!e.node.dataRef.children || e.node.dataRef.children.length === 0) {
          this.isShowTab5 = true
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
        this.getCatalogAndParentPathAndDo(selectKey, (catalogAndParentPath) => {
          this.operation = 'edit'
          this.editData = catalogAndParentPath
          const copyCatalogAndParentPath = { ...catalogAndParentPath }
          this.$emit('selected', selectedKeys, copyCatalogAndParentPath, path)
        })
        const node = this.$refs.myGafTreeTransparent.getNode(
          this.selectedNodeKeys[0]
        )
        this.$emit('LayersSelected', node)
      }
    },
    getCatalogAndParentPathAndDo(catalogId, callback) {
      const url = `/sys-mgt/sys-catalogs/${catalogId}`
      this.$axios.get(url).then((p) => {
        const result = p.data
        if (result.isSuccessed) {
          const catalog = result.data
          this.$axios
            .get(`/sys-mgt/sys-catalogs/parentPath/${catalogId}`)
            .then((p) => {
              const result = p.data
              if (result.isSuccessed) {
                const parentPath = result.data
                const temp = { ...catalog, parentPath }
                callback(temp)
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
      if (this.dataOfTree && this.dataOfTree.length > 0) {
        this.openadd = true
        const node = this.$refs.myGafTreeTransparent.getNode(
          this.selectedNodeKeys[0]
        )
        this.$emit('beforeAddSubCatalog', node, (isNext, info) => {
          if (isNext) {
            this.getCatalogAndParentPathAndDo(
              this.selectedNodeKeys[0],
              (catalogAndParentPath) => {
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
                  sysComponentId: catalogAndParentPath.sysComponentId,
                }
              }
            )
          } else {
            this.openadd = false
            this.$message.info(`不能添加子目录,原因:${info}`)
          }
        })
      } else {
        this.operation = 'add'
        this.$refs.addEditForm.resetFormFields()
        this.editData = {
          parentId: '0',
          parentPath: ['0'],
          type: this.catalogType,
          sysComponentId: '',
        }
      }
    },
    afterFormAddSuccess(catalog) {
      const type = 12
      this.$refs.myGafTreeTransparent.inserNode({
        key: catalog.catalogId,
        title: catalog.name,
        sortSn: catalog.sortSn,
        parentId: catalog.parentId,
        type,
        scopedSlots: { title: 'title' },
      })
      if (this.expandedNodeKeys.indexOf(catalog.parentId) <= -1) {
        this.expandedNodeKeys.push(catalog.parentId)
      }
      // this.selectedNodeKeys = [catalog.catalogId]
      this.isShowTab5 = false
      this.getCatalogAndParentPathAndDo(
        catalog.catalogId,
        (catalogAndParentPath) => {
          this.operation = 'edit'
          this.editData = catalogAndParentPath
          this.$emit(
            'selected',
            this.selectedNodeKeys,
            catalogAndParentPath,
            null
          )
        }
      )
    },
    afterFormUpdateSuccess(catalog) {
      this.$refs.myGafTreeTransparent.updateNode({
        key: catalog.catalogId,
        title: catalog.name,
        sortSn: catalog.sortSn,
        parentId: catalog.parentId,
      })
      this.editData = catalog
    },
    afterDeleteSuccess(catalog,boolean) {
      if (boolean){
        this.isShowTab5 = boolean
      }
      // this.$refs.myGafTreeTransparent.deleteNode({
      //   key: catalog.catalogId,
      //   parentId: catalog.parentId,
      // })
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
      // this.selectedNodeKeys = []
      // this.isShowTab5 = false
    },
  },
}
</script>
<style scoped>
.right {
  padding: 15px;
  height: 97vh;
  width: 83%;
  float: left;
  border-top-left-radius: 10px;
  border-bottom-left-radius: 10px;
  background-color: #fff;
}
</style>
