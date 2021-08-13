<template>
  <div class="app-container">
    <div v-show="!showCanves">
      <div class="page-left">
        <div class="let-btm">
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
                :type="iconNodeType.type === 'model' ? 'apartment' : 'database'"
              ></a-icon>
              <!-- <a-button type="primary" @click="hh(iconNodeType)"></a-button> -->
            </template>
          </gaf-tree-transparent>
        </div>
      </div>
      <div class="page-right">
        <a-tabs
          ref="tabs"
          v-model="activeKey"
          @change="tabChange"
          default-active-key="4"
        >
          <a-tab-pane key="1" v-if="isModel" tab="模型">
            <div class="special-container">
              <model-form
                ref="modelForm"
                :editData="modelEditData"
                :operation="modelOperation"
                @changeOperation="changeOperation"
                @update-success="afterUpdateCatalogSuccess"
                @add-success="afterAddCatalogSuccess"
                @delete-success="afterDeleteCatalogSuccess"
              ></model-form>
            </div>
          </a-tab-pane>
          <a-tab-pane key="4" v-if="isModel" tab="逻辑表">
            <div class="margin-15">
              <logic-list
                ref="dicTypeForm"
                :typeList = "typeList"
                :editData="tableEditData"
                :operation="tableOperation"
                :modelId="modelId"
                @add-success="afterAddLogicTableSuccess"
                @update-success="afterUpdateDicTypeSuccess"
                @delete-success="afterDeleteDicTypeSuccess"
                addButtonName="新增逻辑表"
                @onAddDicType="onAddDicType"
                @changeShowModelCanvas="changeShowModelCanvas"
              ></logic-list>
            </div>
          </a-tab-pane>
          <!-- <a-tab-pane key="5" v-if="groupShow && openAddCatalog" tab="子组列表">
            <div class="margin-15">
              <add-catalog
                ref="modelForm"
                :catalogList="catalogList"
                :editData="modelEditData"
                :operation="modelOperation"
                @add-success="afterAddCatalogSuccess"
                @update-success="afterUpdateCatalogSuccess"
                @delete-success="afterDeleteCatalogSuccess"
                addButtonName="新增分组"
                @onAddCatalog="onAddCatalog"
                :rootNode="rootNode"
              ></add-catalog>
            </div>
          </a-tab-pane> -->
          <a-tab-pane key="2" v-if="!isModel" tab="表">
            <div class="special-container">
              <table-form
                ref="dicTypeForm"
                :editData="tableEditData"
                :operation="tableOperation"
                :modelId="modelId"
                @add-success="afterAddLogicTableSuccess"
                @update-success="afterUpdateDicTypeSuccess"
                @delete-success="afterDeleteDicTypeSuccess"
              ></table-form>
            </div>
          </a-tab-pane>
          <a-tab-pane key="3" v-if="!isModel" tab="字段">
            <div >
              <field-list :tableId="tableId" :modelId="modelId" :dictCode="dictCode"></field-list>
            </div>
          </a-tab-pane>
          <a-tab-pane key="4" v-if="!isModel" tab="物理表">
            <div >
              <physics-list :tableId="tableId" :modelId="modelId" :dictCode="dictCode"></physics-list>
            </div>
          </a-tab-pane>
        </a-tabs>
      </div>
    </div>
    <div v-if="showCanves">
      <mm-canvas :modelId="modelId" @backModel="backModel"></mm-canvas>
    </div>
  </div>
</template>

<script>
    // @ts-ignore
    import ModelForm from '../../views/model/ModelForm'
    import TableForm from '../../views/model/TableForm'
    import '~/assets/css/common.css'
    import FieldList from '../../views/model/FieldList.vue'
    import PhysicsList from '../../views/model/PhysicsList.vue'
    // import treeUtil from '../../../common/utils/TreeUtil'
    import LogicList from '../../views/model/LogicList'
    import MmCanvas from '../MmCanvas'
    // import addCatalog from './addCatalog'

    export default {
  components: {
    ModelForm,
    TableForm,
    FieldList,
    PhysicsList,
    LogicList,
    MmCanvas
    // addCatalog
  },
  provide() {     // provide是一个匿名函数，返回一个对象
    return {
      mainCompent: this,
    }
  },
  data() {
    return {
      searchPlaceholder: '请输入分组名称或字典类别模糊查询',
      showCanves: false,
      // 左侧的字典目录树
      checkedNodeKeys: [],
      dataOfTree: [],
      expandedNodeKeys: [],
      selectedNodeKeys: ['0'],
      // 字典目录分组
      groupShow: true,
      modelOperation: 'add',
      modelEditData: {parentId: '0'},
      // 字典类别
      tableOperation: 3,
      tableEditData: {catalogId: '0'},
      // 字典数据
      dicTypeId: null,
      dictCode: null,
      // tabs
      activeKey: '4',
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
      rootNode: '',
      isModel: true,
      modelId: '',
      tableId: '',
      modelData: null,
    }
  },
  computed: {
  },
  watch: {
    isModel: function(newValue) {
      if (newValue) {
        this.activeKey = '4'
      } else {
        this.activeKey = '3'
      }
    },
    selectedNodeKeys: function() {
      this.selectedNodeKeyVesion += 1
    },
    
  },
  async created() {
    await this.getTree()  
    if (this.dataOfTree.length > 0) {
      this.modelId = this.dataOfTree[0].key
      this.selectedNodeKeys = [this.dataOfTree[0].key]
      this.expandedNodeKeys = [this.dataOfTree[0].key]
      const model = await this.getModel(this.dataOfTree[0].key)
      this.modelData = model
    } else {
      this.activeKey = '1'
    }
    
  },
  async mounted() {
    
    // this.rootNode = this.dataOfTree[0].key
    // this.catalogList = this.dataOfTree[0].children.filter(item => item.type === 12)
  },
  methods: {
    // 当字典目录添加成功后
    afterAddCatalogSuccess(model) {
      console.log(model,this.dataOfTree,'tree')
      this.$refs.myGafTree.inserNode({
        key: model.modelId,
        title: model.modelName,
        sortSn: model.sortSn,
        parentId: '0',
        userObject: { type: 'model' },
        type: 'model',
        scopedSlots: { title: 'title' }
      })
      if (this.expandedNodeKeys.indexOf(model.parentId) <= -1) {
        this.expandedNodeKeys.push(model.parentId)
      }
      this.selectedNodeKeys = [model.modelId]
      this.modelId = model.modelId
      this.activeKey = '4'
      this.modelData = model
    },
    afterUpdateCatalogSuccess(model) {
      console.log(model)
      this.$refs.myGafTree.updateNode({
        key: model.modelId,
        title: model.modelName,
        sortSn: model.sortSn,
        parentId: '0'
      })
      if (this.expandedNodeKeys.indexOf(model.parentId) <= -1) {
        this.expandedNodeKeys.push(model.parentId)
      }
      this.modelEditData = model
    },
    // 删除字典目录成功
    afterDeleteCatalogSuccess(model,boolean) {
      if(boolean){
        this.openAddType = true
      }
      if (model.key && model.key.length > 0){
        model.key.forEach(item => {
          this.$refs.myGafTree.deleteNode({
            key: item,
            parentId: model.parentId
          })
        })
      } else {
        this.$refs.myGafTree.deleteNode({
          key: model.modelId,
          parentId: model.parentId
        })
      }
      this.selectedNodeKeys = []
      this.modelOperation = 'add'
      // this.$refs.modelForm.clear()
    },
    // 当字典类别添加成功后
    afterAddLogicTableSuccess(table) {
      console.log('sds')
      this.openAddCatalog=false
      this.tableOperation = 3
      this.tableEditData = table

      // 左侧树做改变
      this.$refs.myGafTree.inserNode({
        key: table.tableId,
        title: table.tableName,
        sortSn: table.sortSn,
        parentId: table.modelId,
        userObject: { type: 'table'},
        type: 'table',
        scopedSlots: { title: 'title' }
      })
      if (this.expandedNodeKeys.indexOf(table.modelId) <= -1) {
        this.expandedNodeKeys.push(table.modelId)
      }
      this.selectedNodeKeys = [table.modelId]
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
    afterUpdateDicTypeSuccess(table) {
      this.$refs.myGafTree.updateNode({
        key: table.tableId,
        title: table.tableName,
        sortSn: table.sortSn,
        parentId: table.modelId
      })
      if (this.expandedNodeKeys.indexOf(table.modelId) <= -1) {
        this.expandedNodeKeys.push(table.modelId)
      }
      this.tableEditData = table
    },
    afterDeleteDicTypeSuccess(table) {
      if (table.key && table.key.length > 0){
        table.key.forEach(item => {
          this.$refs.myGafTree.deleteNode({
            key: item,
            parentId: table.modelId
          })
        })
      } else {
        this.$refs.myGafTree.deleteNode({
          key: table.tableId,
          parentId: table.modelId
        })
      }
      this.selectedNodeKeys = []
      this.tableOperation = 2
      // this.$refs.dicTypeForm.clear()
    },
    onAddCatalog() {
      // this.groupShow = true
      // this.activeKey = '1'
      this.selectedNodeKeys = []
      this.modelOperation = 'add'
      this.$nextTick(()=> {
        // this.$refs.modelForm.clear()
      })
    },
    onAddDicType() {
      // this.groupShow = false
      // this.activeKey = '2'
      this.tableEditData = {}
      this.selectedNodeKeys = []
      this.tableOperation = 2
      // this.$nextTick(()=> {
      //   this.$refs.dicTypeForm.clear()
      // })
    },
    async onSelect(selectedKeys, e) {
      console.log(selectedKeys, e)
      if (e.selected) {
        if (e.node.dataRef.userObject.type === 'model') {
          const model = await this.getModel(e.node.dataRef.key)
          this.modelEditData = model
          this.modelOperation = 'edit'
          this.isModel = true
          this.activeKey = '4'
          this.modelId = e.node.dataRef.key
          console.log(this.modelId, '2')
          this.modelData = model
        } else if (e.node.dataRef.userObject.type === 'table') {
          const table = await this.getTable(e.node.dataRef.key)
          const model = await this.getModel(e.node.dataRef.parentId)
          this.tableEditData = table
          this.activeKey = '3'
          this.isModel = false
          this.tableId = e.node.dataRef.key
          this.modelId = e.node.dataRef.parentId
          this.modelData = model
        }
      }
      
      // this.typeList = []
      // this.catalogList = []
      // if (e.node.dataRef.children){
      //   this.typeList = e.node.dataRef.children.filter(item => item.type === 14)
      //   this.catalogList = e.node.dataRef.children.filter(item => item.type === 12)
      // }
      // if (this.typeList.length > 0){
      //   this.openAddCatalog = false
      // } else {
      //   this.openAddCatalog = true
      // }
      // if (this.catalogList.length > 0){
      //   this.openAddType = false
      // } else {
      //   this.openAddType = true
      // }
      // this.nodeType = e.node.dataRef.type
      // if (e.selected) {
      //   const selectKey = selectedKeys[0]
      //   if (e.node.dataRef.type === 12) {
      //     this.isModel = true
      //     this.groupShow = true
      //     this.activeKey = '1'
      //     const catalog = await this.getCatalog(selectKey)
      //     if(catalog) {
      //       this.modelOperation = 'edit'
      //       this.modelEditData = {...catalog}
      //       this.tableEditData = {catalogId: catalog.catalogId}
      //       this.openCatalogForm = true
      //       if (catalog.catalogId === this.rootNode){
      //         this.openCatalogForm = false
      //         this.activeKey = '5'
      //       }
      //     }
      //     this.tab1SelectedKeyVesion = this.selectedNodeKeyVesion
      //   } else if (e.node.dataRef.type === 14) {
      //     this.isModel = true
      //     this.groupShow = false
      //     const dicType = await this.getTable(selectKey)
      //     if(dicType) {
      //         this.tableOperation = 3
      //         this.tableEditData = dicType
      //       }
      //     if(this.activeKey === '1' || this.activeKey === '2') {
      //       this.activeKey = '2'
      //       if(dicType) {
      //         this.tableOperation = 3
      //         this.tableEditData = dicType
      //       }
      //       this.tab2SelectedKeyVesion = this.selectedNodeKeyVesion
      //     } else if(this.activeKey === '3') {
      //       this.dicTypeId = selectKey
      //       if(dicType) {
      //         this.dictCode = dicType.dictCode
      //       }
      //       this.tab3SelectedKeyVesion = this.selectedNodeKeyVesion
      //     }
      //   }
      // }
    },
    changeOperation() {
      this.modelOperation = 'add'
    },
    async tabChange(activeKey) {
      if (this.selectedNodeKeys && this.selectedNodeKeys.length === 1) {
        const selectKey = this.selectedNodeKeys[0]
          // eslint-disable-next-line no-empty
        if (activeKey === '1' && this.tab1SelectedKeyVesion < this.selectedNodeKeyVesion) {
          const catalog = await this.getModel(selectKey)
          if(catalog) {
            this.modelOperation = 'edit'
            this.modelEditData = catalog
          }
          this.tab1SelectedKeyVesion = this.selectedNodeKeyVesion
        } else if(activeKey === '2' && this.tab2SelectedKeyVesion < this.selectedNodeKeyVesion) {
          const dicType = await this.getTable(selectKey)
          if(dicType) {
            this.tableOperation = 3
            this.tableEditData = dicType
          }
          this.tab2SelectedKeyVesion = this.selectedNodeKeyVesion
        } else if(activeKey === '3' && this.tab3SelectedKeyVesion < this.selectedNodeKeyVesion) {
          const dicType = await this.getTable(selectKey)
          this.dicTypeId = selectKey
          if(dicType) {
            this.dictCode = dicType.dictCode
          }
          this.tab3SelectedKeyVesion = this.selectedNodeKeyVesion
        } else if (activeKey === '5') {
          const catalog = await this.getCatalog(selectKey)
          if(catalog) {
            this.modelOperation = 'add'
            this.modelEditData = catalog
          }
        }
      }
    },
    async getTable(tableId) {
      const url = `/data-mgt/model-manage/logic-tables/${tableId}`
      const res = await this.$axios.$get(url)
      if(res.isSuccessed) {
        return res.data
      } else {
        this.$message.error(`查询字典类别失败，原因:${res.message}`)
        return null
      }
    },
    async getModel(modelId) {
      // const url = `/data-mgt/model-manage/${modelId}`
      const url = `/data-mgt/model-manage/models/${modelId}`
      const res = await this.$axios.$get(url)
      if(res.isSuccessed) {
        return res.data
      } else {
        this.$message.error(`查询模型失败，原因:${res.message}`)
        return null
      }
    },
    async getTree() {
      // const url = '/sys-mgt/sys-dicts/allnodes'
      const url = '/data-mgt/model-manage/models/model-tables-tree'
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        console.log(res, 'res')
        this.dataOfTree = res.data
        this.setScopedSlots(this.dataOfTree)
        // this.dataOfTree = treeUtil.convertToCustomSortTree({key: '0'}, res.data, (a , b) => {
        //   if(a.type == b.type) {
        //     return a.sortSn - b.sortSn
        //   } else {
        //     return a.type - b.type
        //   }
        // })
        // this.dataOfTree.map(item => {
        //   if (item.logicTables.length > 0) {
        //     item.logicTables.map(childrenItem => {
        //       childrenItem['key'] = childrenItem.tableId
        //       childrenItem['title'] = childrenItem.tableName
        //       childrenItem['scopedSlots'] =  { title: 'title' }
        //     })
        //   }
        //   item['key'] = item.modelId
        //   item['title'] = item.modelName
        //   item['children'] = item.logicTables
        //   item['scopedSlots'] =  { title: 'title' }
        // })
        // if (this.dataOfTree.length === 0) {
        //   this.activeKey = '1'
        //   this.modelOperation = 'add'
        // }
        console.log(this.dataOfTree, 'data')
      } else {
        this.$message.error(`查询失败,原因:${res.message}`)
      }
    },
    hh(ss) {
      console.log(ss, 'ss')
    },
    setScopedSlots(data) {
      data.forEach(item => {
        item['scopedSlots'] = { title: 'title' }
        item['type'] = item.userObject.type
        if (item.children && item.children.length > 0) {
          this.setScopedSlots(item.children)
        }
      })
    },
    changeShowModelCanvas() {
      this.showCanves = true
    },
    backModel() {
      this.showCanves = false
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
