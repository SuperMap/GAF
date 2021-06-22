<template>
  <div class="app-container">
    <div class="page-left">
      <!-- <div class="action-div">
        <button
          class="add-user-btn"
          @click="onAddCatalog"
        >
          新增分组
        </button>
        <button
          class="add-user-btn"
          @click="onAddDicType"
        >
          新增类别
        </button>
      </div> -->
      <div class="let-btm">
        <!-- <div class="tree-catalog">
          <span class="vertical-line">| </span>
          字典目录
        </div> -->
        <gaf-tree-transparent
          ref="myGafTree"
          :dataOfTree="dataOfTree"
          :searchType="[0]"
          :expandedNodeKeys.sync="expandedNodeKeys"
          :autoExpandParent="true"
          :selectedKeys.sync="selectedNodeKeys"
          :searchPlaceholder="searchPlaceholder"
          @select="onSelect"
          :show-line="true"
        >
          <template v-slot:icon="{ iconNodeType }">
            <a-icon
              :type="iconNodeType.type === 12 ? 'folder' : 'book'"
            ></a-icon>
          </template>
        </gaf-tree-transparent>
      </div>
    </div>
    <div class="page-right">
      <a-tabs
        ref="tabs"
        v-model="activeKey"
        @change="tabChange"
        default-active-key="1"
      >
        <a-tab-pane key="1" v-if="groupShow && openCatalogForm" tab="字典分组">
          <div class="special-container">
            <dic-catalog-form
              ref="dicCatalogForm"
              :editData="catalogEditData"
              :operation="catalogOperation"
              @update-success="afterUpdateCatalogSuccess"
              @delete-success="afterDeleteCatalogSuccess"
            ></dic-catalog-form>
          </div>
        </a-tab-pane>
        <a-tab-pane key="4" v-if="groupShow && openAddType" tab="类别列表">
          <div class="margin-15">
            <add-type
              ref="dicTypeForm"
              :typeList = "typeList"
              :editData="dicTypeEditData"
              :operation="dicTypeOperation"
              @add-success="afterAddDicTypeSuccess"
              @update-success="afterUpdateDicTypeSuccess"
              @delete-success="afterDeleteDicTypeSuccess"
              addButtonName="新增类别"
              @onAddDicType="onAddDicType"
            ></add-type>
          </div>
        </a-tab-pane>
        <a-tab-pane key="5" v-if="groupShow && openAddCatalog" tab="子组列表">
          <div class="margin-15">
            <add-catalog
              ref="dicCatalogForm"
              :catalogList="catalogList"
              :editData="catalogEditData"
              :operation="catalogOperation"
              @add-success="afterAddCatalogSuccess"
              @update-success="afterUpdateCatalogSuccess"
              @delete-success="afterDeleteCatalogSuccess"
              addButtonName="新增分组"
              @onAddCatalog="onAddCatalog"
              :rootNode="rootNode"
            ></add-catalog>
          </div>
        </a-tab-pane>
        <a-tab-pane key="2" v-if="!groupShow" tab="字典类别">
          <div class="special-container">
            <dic-type-form
              ref="dicTypeForm"
              :editData="dicTypeEditData"
              :operation="dicTypeOperation"
              @add-success="afterAddDicTypeSuccess"
              @update-success="afterUpdateDicTypeSuccess"
              @delete-success="afterDeleteDicTypeSuccess"
            ></dic-type-form>
          </div>
        </a-tab-pane>
        <a-tab-pane key="3" v-if="!groupShow && dicTypeOperation != 2" tab="字典数据">
          <div >
            <dic-list :dicTypeId="dicTypeId" :dictCode="dictCode"></dic-list>
          </div>
        </a-tab-pane>
      </a-tabs>
    </div>
  </div>
</template>

<script>
    // @ts-ignore
    import DicCatalogForm from '../../views/dics/DictCatalogForm'
    import DicTypeForm from '../../views/dics/DicTypeForm'
    import '~/assets/css/common.css'
    import DicList from '../../views/dics/DicList.vue'
    import treeUtil from '../../utils/tree/TreeUtil.js'
    import addType from './addType'
    import addCatalog from './addCatalog'

    export default {
  components: {
    DicCatalogForm,
    DicTypeForm,
    DicList,
    addType,
    addCatalog
  },
  data() {
    return {
      searchPlaceholder: '请输入分组名称或字典类别模糊查询',
      // 左侧的字典目录树
      checkedNodeKeys: [],
      dataOfTree: [],
      expandedNodeKeys: [],
      selectedNodeKeys: ['0'],
      // 字典目录分组
      groupShow: true,
      catalogOperation: 'add',
      catalogEditData: {parentId: '0'},
      // 字典类别
      dicTypeOperation: 2,
      dicTypeEditData: {catalogId: '0'},
      // 字典数据
      dicTypeId: null,
      dictCode: null,
      // tabs
      activeKey: '5',
      // 版本
      selectedNodeKeyVesion: 1,
      tab1SelectedKeyVesion: 0,
      tab2SelectedKeyVesion: 0,
      tab3SelectedKeyVesion: 0,
      catalogList: [],
      typeList: [],
      openAddCatalog: true,
      openAddType: false,
      openCatalogForm: false,
      rootNode: ''
    }
  },
  computed: {
  },
  watch: {
    groupShow: function(newValue) {
      if (newValue) {
        this.activeKey = '1'
      } else {
        this.activeKey = '2'
      }
    },
    selectedNodeKeys: function() {
      this.selectedNodeKeyVesion += 1
    }
  },
  async mounted() {
    await this.getTree()
    this.rootNode = this.dataOfTree[0].key
    this.catalogList = this.dataOfTree[0].children.filter(item => item.type === 12)
  },
  methods: {
    // 当字典目录添加成功后
    afterAddCatalogSuccess(catalog) {
      console.log(catalog,this.dataOfTree,'tree')
      this.openAddType = false
      this.catalogList.push({
        key: catalog.catalogId,
        title: catalog.name,
        sortSn: catalog.sortSn,
        parentId: catalog.parentId,
        type: 12,
        scopedSlots: { title: 'title' }
      })
      
      // this.catalogOperation = 'edit'
      // this.catalogEditData = catalog
      // 左侧树做改变
      
      this.$refs.myGafTree.inserNode({
        key: catalog.catalogId,
        title: catalog.name,
        sortSn: catalog.sortSn,
        parentId: catalog.parentId,
        type: 12,
        scopedSlots: { title: 'title' }
      })
      if (this.expandedNodeKeys.indexOf(catalog.parentId) <= -1) {
        this.expandedNodeKeys.push(catalog.parentId)
      }
      this.selectedNodeKeys = [catalog.catalogId]
    },
    afterUpdateCatalogSuccess(catalog) {
      this.$refs.myGafTree.updateNode({
        key: catalog.catalogId,
        title: catalog.name,
        sortSn: catalog.sortSn,
        parentId: catalog.parentId
      })
      if (this.expandedNodeKeys.indexOf(catalog.parentId) <= -1) {
        this.expandedNodeKeys.push(catalog.parentId)
      }
      this.catalogEditData = catalog
    },
    // 删除字典目录成功
    afterDeleteCatalogSuccess(catalog,boolean) {
      if(boolean){
        this.openAddType = true
      }
      if (catalog.key && catalog.key.length > 0){
        catalog.key.forEach(item => {
          this.$refs.myGafTree.deleteNode({
            key: item,
            parentId: catalog.parentId
          })
        })
      } else {
        this.$refs.myGafTree.deleteNode({
          key: catalog.catalogId,
          parentId: catalog.parentId
        })
      }
      this.selectedNodeKeys = []
      this.catalogOperation = 'add'
      // this.$refs.dicCatalogForm.clear()
    },
    // 当字典类别添加成功后
    afterAddDicTypeSuccess(dicType) {
      console.log('sds')
      this.openAddCatalog=false
      this.typeList.push({
        key: dicType.dataDictId,
        title: dicType.dictName,
        sortSn: dicType.seq + sameLevelCatalogCount,
        parentId: dicType.catalogId,
        type: 14,
        scopedSlots: { title: 'title' }
      })
      this.dicTypeOperation = 3
      this.dicTypeEditData = dicType

      const sameLevelCatalogCount = this.getSameLevelCatalogCount(dicType)
      // 左侧树做改变
      this.$refs.myGafTree.inserNode({
        key: dicType.dataDictId,
        title: dicType.dictName,
        sortSn: dicType.seq + sameLevelCatalogCount,
        parentId: dicType.catalogId,
        type: 14,
        scopedSlots: { title: 'title' }
      })
      if (this.expandedNodeKeys.indexOf(dicType.catalogId) <= -1) {
        this.expandedNodeKeys.push(dicType.catalogId)
      }
      this.selectedNodeKeys = [dicType.dataDictId]
    },
    getSameLevelCatalogCount(dicType) {
       let sameLevelCatalogCount = 0
      if(dicType.catalogId === '0') {
        sameLevelCatalogCount = this.dataOfTree.filter(item=> item.type === 12)
      } else {
        const parentNode = this.$refs.myGafTree.getNode(dicType.catalogId)
        if(parentNode && parentNode.children) {
          const catalogs = parentNode.children.filter(item=> item.type === 12)
          sameLevelCatalogCount = catalogs.length
        }
      }
      return sameLevelCatalogCount
    },
    afterUpdateDicTypeSuccess(dicType) {
      const sameLevelCatalogCount = this.getSameLevelCatalogCount(dicType)
      this.$refs.myGafTree.updateNode({
        key: dicType.dataDictId,
        title: dicType.dictName,
        sortSn: dicType.seq + sameLevelCatalogCount,
        parentId: dicType.catalogId
      })
      if (this.expandedNodeKeys.indexOf(dicType.catalogId) <= -1) {
        this.expandedNodeKeys.push(dicType.catalogId)
      }
      this.dicTypeEditData = dicType
    },
    afterDeleteDicTypeSuccess(dicType,boolean) {
      if(boolean){
        this.openAddCatalog = true
      }
      if (dicType.key && dicType.key.length > 0){
        dicType.key.forEach(item => {
          this.$refs.myGafTree.deleteNode({
            key: item,
            parentId: dicType.catalogId
          })
        })
      } else {
        this.$refs.myGafTree.deleteNode({
          key: dicType.dataDictId,
          parentId: dicType.catalogId
        })
      }
      this.selectedNodeKeys = []
      this.dicTypeOperation = 2
      // this.$refs.dicTypeForm.clear()
    },
    onAddCatalog() {
      // this.groupShow = true
      // this.activeKey = '1'
      this.selectedNodeKeys = []
      this.catalogOperation = 'add'
      this.$nextTick(()=> {
        // this.$refs.dicCatalogForm.clear()
      })
    },
    onAddDicType() {
      // this.groupShow = false
      // this.activeKey = '2'
      this.selectedNodeKeys = []
      this.dicTypeOperation = 2
      // this.$nextTick(()=> {
      //   this.$refs.dicTypeForm.clear()
      // })
    },
    async onSelect(selectedKeys, e) {
      this.typeList = []
      this.catalogList = []
      if (e.node.dataRef.children){
        this.typeList = e.node.dataRef.children.filter(item => item.type === 14)
        this.catalogList = e.node.dataRef.children.filter(item => item.type === 12)
      }
      if (this.typeList.length > 0){
        this.openAddCatalog = false
      } else {
        this.openAddCatalog = true
      }
      if (this.catalogList.length > 0){
        this.openAddType = false
      } else {
        this.openAddType = true
      }
      this.nodeType = e.node.dataRef.type
      if (e.selected) {
        const selectKey = selectedKeys[0]
        if (e.node.dataRef.type === 12) {
          this.groupShow = true
          this.activeKey = '1'
          const catalog = await this.getCatalog(selectKey)
          if(catalog) {
            this.catalogOperation = 'edit'
            this.catalogEditData = {...catalog}
            this.dicTypeEditData = {catalogId: catalog.catalogId}
            this.openCatalogForm = true
            if (catalog.catalogId === this.rootNode){
              this.openCatalogForm = false
              this.activeKey = '5'
            }
          }
          this.tab1SelectedKeyVesion = this.selectedNodeKeyVesion
        } else if (e.node.dataRef.type === 14) {
          this.groupShow = false
          const dicType = await this.getDicType(selectKey)
          if(dicType) {
              this.dicTypeOperation = 3
              this.dicTypeEditData = dicType
            }
          if(this.activeKey === '1' || this.activeKey === '2') {
            this.activeKey = '2'
            if(dicType) {
              this.dicTypeOperation = 3
              this.dicTypeEditData = dicType
            }
            this.tab2SelectedKeyVesion = this.selectedNodeKeyVesion
          } else if(this.activeKey === '3') {
            this.dicTypeId = selectKey
            if(dicType) {
              this.dictCode = dicType.dictCode
            }
            this.tab3SelectedKeyVesion = this.selectedNodeKeyVesion
          }
        }
      }
    },
    async tabChange(activeKey) {
      if (this.selectedNodeKeys && this.selectedNodeKeys.length === 1) {
        const selectKey = this.selectedNodeKeys[0]
          // eslint-disable-next-line no-empty
        if (activeKey === '1' && this.tab1SelectedKeyVesion < this.selectedNodeKeyVesion) {
          const catalog = await this.getCatalog(selectKey)
          if(catalog) {
            this.catalogOperation = 'edit'
            this.catalogEditData = catalog
          }
          this.tab1SelectedKeyVesion = this.selectedNodeKeyVesion
        } else if(activeKey === '2' && this.tab2SelectedKeyVesion < this.selectedNodeKeyVesion) {
          const dicType = await this.getDicType(selectKey)
          if(dicType) {
            this.dicTypeOperation = 3
            this.dicTypeEditData = dicType
          }
          this.tab2SelectedKeyVesion = this.selectedNodeKeyVesion
        } else if(activeKey === '3' && this.tab3SelectedKeyVesion < this.selectedNodeKeyVesion) {
          const dicType = await this.getDicType(selectKey)
          this.dicTypeId = selectKey
          if(dicType) {
            this.dictCode = dicType.dictCode
          }
          this.tab3SelectedKeyVesion = this.selectedNodeKeyVesion
        } else if (activeKey === '5') {
          const catalog = await this.getCatalog(selectKey)
          if(catalog) {
            this.catalogOperation = 'add'
            this.catalogEditData = catalog
          }
        }
      }
    },
    async getDicType(dicTypeId) {
      const url = `/sys-mgt/sys-dicts/${dicTypeId}`
      const res = await this.$axios.$get(url)
      if(res.isSuccessed) {
        return res.data
      } else {
        this.$message.error(`查询字典类别失败，原因:${res.message}`)
        return null
      }
    },
    async getCatalog(catalogId) {
      const url = `/sys-mgt/sys-catalogs/${catalogId}`
      const res = await this.$axios.$get(url)
      if(res.isSuccessed) {
        return res.data
      } else {
        this.$message.error(`查询字典分组失败，原因:${res.message}`)
        return null
      }
    },
    async getTree() {
      const url = '/sys-mgt/sys-dicts/allnodes'
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.dataOfTree = treeUtil.convertToCustomSortTree({key: '0'}, res.data, (a , b) => {
          if(a.type == b.type) {
            return a.sortSn - b.sortSn
          } else {
            return a.type - b.type
          }
        })
      } else {
        this.$message.error(`查询失败,原因:${res.message}`)
      }
    }
  }
}
</script>

<style scoped>
.dic-catalog-form-div {
  margin-top: 50px;
}

.ant-select {
  width: 200px;
}
.dic-type-form-div {
  margin-top: 50px;
}
.let-btm {
  overflow-y: auto;
  overflow-x: auto;
  height: 93%;
  padding: 2%;
}
.add-user-btn {
  cursor: pointer;
   width: 140px;
  height: 32px;
  margin: 3px auto;
  font-size: 14px;
  background-color: rgb(47, 117, 249);
  border: none;
  border-radius: 4px;
  color: white;
  padding: 3px 6px;
}
.action-div {
  text-align: center;
  overflow: hidden;
}
.ant-tabs {
  width: 100%;
  height: 100%;
}
</style>
