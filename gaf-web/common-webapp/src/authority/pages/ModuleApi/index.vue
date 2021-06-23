<template>
  <div class="app-container">
    <div class="bottom">
      <div class="page-left">
        <!-- <div class="tree-catalog">
          <span class="vertical-line">| </span>
          模块关联API
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
              :type="iconNodeType.type === 12 ? appstore : calendar"
            ></a-icon>
          </template>
        </gaf-tree-transparent> -->
        <div class="selectModule">API关联所属模块: {{selectModule.name}}</div>
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
          :show-line="true"
        >
          <template v-slot:icon="{ iconNodeType }">
            <a-icon :type="iconNodeType.type === 2 ? tag : tags"></a-icon>
          </template>
        </gaf-tree-transparent>
      </div>
      <div class="page-right">
        <div class="main-top">
          <button
            @click="handleAdd"
            class="btn-fun blue"
          >
            保存
          </button>
          <button
            @click="handleBack"
            class="btn-fun red"
          >
            返回
          </button>
        </div>
        <!-- <div class="main-top-left">
            <div class="tree-catalog">
            <span class="vertical-line">| </span>
            API分组目录
          </div>
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
              <a-icon :type="iconNodeType.type === 2 ? tag : tags"></a-icon>
            </template>
          </gaf-tree-transparent>
        </div>  -->
        <div>
          <add-edit-form
            ref="moduleApi"
            v-if="isApiList"
            :module-id="selectModule.resourceModuleId"
            :apiGrpId="apiGrpId"
            :node-type="nodeType"
          ></add-edit-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
    import AddEditForm from '../../views/ModuleApi/ApiList'
    import '~/assets/css/common.css'

    export default {
  components: {
    AddEditForm
  },
  props: {
    selectModule: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      tag: 'tag',
      tags: 'tags',
      appstore: 'appstore',
      calendar: 'calendar',
      defaultActiveKey: '1',
      defaultActiveKey2: '2',
      dataOfTree: [],
      dataOfTree2: [],
      expandedNodeKeys: [],
      expandedNodeKeys2: [],
      selectedNodeKeys: [],
      selectedNodeKeys2: [],
      editData: { parentId: '0', parentPath: ['0'] },
      operation: 'edit',
      searchPlaceholder: '请输入模块名查询',
      searchPlaceholder2: '请输入API分组名查询',
      moduleGrpId: '',
      moduleId: '',
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
          this.$refs.moduleApi.handleAdd()
        })
      } else {
        this.$message.warning('请选择模块分组')
      }
    },
    handleBack() {
      this.$emit('back')
    },
    async getNodesAndSetByType() {
      const url = `/authority/auth-resource-modules/tree`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.dataOfTree = this.$refs.myGafTreeTransparent.convertToTree(
          res.data
        )
      } else {
        this.$message.error('加载模块树失败,原因：' + res.message)
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
      this.moduleGrpId = e.node.dataRef.key
      this.moduleId = e.node.dataRef.key
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
/* .app-container {
  height: 100%;
  width: 100%;
  overflow: hidden;
}
.bottom {
  height: 100%;
  width: 100%;
}
.left {
  height: 100%;
  width: 20%;
  float: left;
  margin: 1% 0 1% 1%;
  padding: 0 1% 1% 0;
  overflow: hidden;
  overflow-y: auto;
  overflow-x: auto;
}*/
.main-top-left {
  border-top: 2px solid transparent;
  height: 87%;
  width: 25%;
  float: left;
  margin: 1% 0 1% 1%;
  padding: 0 1% 1% 0;
  overflow: hidden;
  overflow-y: auto;
  overflow-x: auto;
}
.main-top {
  border-bottom: 1px solid rgba(204, 204, 204, 0.5);
}
.main-top-right {
  height: 87%;
  width: 74%;
  float: left;
  border-left: 1px solid rgba(204, 204, 204, 0.5);
  padding: 1%;
  overflow-y: auto;
  overflow-x: auto;
  /* background-color: #fff; */
}
.main {
  height: 100%;
  width: 79%;
  float: left;
}
.module-btn {
  margin-left: 1%;
  background-color: rgba(84, 92, 100, 0.75);
  color: rgba(255, 255, 255, 0.7);
  font-weight: bold;
  border: none;
  box-shadow: 0 0 2px 2px rgba(84, 92, 100, 0.55);
  transition: linear 0.2s;
}
.module-btn:hover {
  color: white;
  box-shadow: 0 0 2px 2px rgba(84, 92, 100, 0.25);
}
.over {
  overflow: auto
}
.selectModule {
  font-size: 18px;
  background: skyblue;
  padding: 5px 10px;
  border-radius: 5px;
  font-weight: bold;
}
</style>
