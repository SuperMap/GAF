<template>
  <div class="app-container">
    <div class="bottom">
      <div class="page-left">
        <!-- <div class="tree-catalog">
          <span class="vertical-line">| </span>
          角色分配菜单
        </div> -->
        <gaf-tree-transparent
          ref="myGafTreeTransparent"
          :searchPlaceholder="searchPlaceholder"
          :dataOfTree="dataOfTree"
          :searchType="[0]"
          :expandedNodeKeys.sync="expandedNodeKeys"
          @select="onSelect"
        >
          <template v-slot:icon="{ iconNodeType }">
            <a-icon :type="iconNodeType.type === 12 ? tags : tag"></a-icon>
          </template>
        </gaf-tree-transparent>
      </div>
      <div class="page-right">
        <div class="main-top">
          <button @click="handleAdd" class="btn-fun blue">
            添加至当前角色
          </button>
        </div>
        <div class="main-top-left">
          <!-- <div class="tree-catalog">
          <span class="vertical-line">| </span>
          菜单分组目录
          </div> -->
          <gaf-tree-transparent
            ref="myGafTreeTransparent2"
            :searchPlaceholder="searchPlaceholder2"
            :dataOfTree="dataOfTree2"
            :searchType="[0]"
            :expandedNodeKeys.sync="expandedNodeKeys2"
            :checkable="true"
            :checkNodeStrictly="false"
            v-model="checkedNodeKeys"
          >
            <template v-slot:icon="{ iconNodeType }">
              <a-icon
                :type="iconNodeType.type === 12 ? profile : menu"
              ></a-icon>
            </template>
          </gaf-tree-transparent>
        </div>
        <div class="main-top-right"></div>
      </div>
    </div>
  </div>
</template>

<script>
import '../../../common/css/common.css'

export default {
  components: {},
  data() {
    return {
      profile: 'profile',
      menu: 'menu',
      tag: 'tag',
      tags: 'tags',
      checkedNodeKeys: [],
      dataOfTree: [],
      dataOfTree2: [],
      expandedNodeKeys: [],
      expandedNodeKeys2: [],
      searchPlaceholder: '请输入角色名称查询',
      searchPlaceholder2: '请输入菜单组名称查询',
      roleId: '',
      nodeType: 12,
      menuTree: []
    }
  },
  watch: {},
  created() {},
  mounted() {
    this.getNodesAndSetByType()
    this.getNodesAndSetByType2()
  },
  methods: {
    async getSelected() {
      const url = `/authority/auth-role-menu/list-by-role/${this.roleId}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        if (res.data !== null && res.data.length > 0) {
          this.checkedNodeKeys = res.data.map(item => item.resourceMenuId)
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
      const checkedMenus = this.menuTree
        .filter(
          item =>
            this.checkedNodeKeys.indexOf(item.key) !== -1 && item.type === 13
        )
        .map(i => i.key)
      const url = `/authority/auth-role-menu/handle`
      const data = {
        roleId: this.roleId,
        menuList: checkedMenus
      }
      const res = await this.$axios.$post(url, data)
      if (res.isSuccessed) {
        this.$message.success('添加成功')
      } else {
        this.$message.error(`添加失败,原因:${res.message}`)
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
    async getNodesAndSetByType2() {
      const url = `/authority/auth-resource-menus/tree`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.menuTree = res.data
        this.dataOfTree2 = this.$refs.myGafTreeTransparent2.convertToTree(
          res.data
        )
      } else {
        this.$message.error('加载菜单树失败,原因：' + res.message)
      }
    },
    onSelect(selectedKeys, e) {
      this.roleId = e.node.dataRef.key
      this.nodeType = e.node.dataRef.type
      if (this.nodeType !== 5) {
        this.checkedNodeKeys = []
      } else {
        this.getSelected()
      }
    }
  }
}
</script>

<style scoped>
.bottom {
  height: 100%;
  width: 100%;
}
.left {
  width: 20%;
  height: 100%;
  float: left;
  margin: 1% 0 1% 1%;
  padding: 0 1% 5% 0;
  overflow: hidden;
  overflow-x: auto;
  overflow-y: auto;
}
.main-top-left {
  height: 87%;
  width: 28%;
  float: left;
  margin: 1% 0 1% 1%;
  padding: 0 1% 5% 0;
  overflow: hidden;
  overflow-x: auto;
  overflow-y: auto;
}
.main-top-right {
  width: 70%;
  height: 87%;
  float: left;
  overflow: hidden;
  overflow-x: auto;
  overflow-y: auto;
  border-left: 1px solid rgba(204, 204, 204, 0.5);
}
.role-btn {
  margin-left: 1%;
  background-color: rgba(84, 92, 100, 0.75);
  color: rgba(255, 255, 255, 0.7);
  font-weight: bold;
  border: none;
  box-shadow: 0 0 2px 2px rgba(84, 92, 100, 0.55);
  transition: linear 0.2s;
}
.role-btn:hover {
  color: white;
  box-shadow: 0 0 2px 2px rgba(84, 92, 100, 0.25);
}
.main {
  height: 100%;
  width: 79%;
  float: left;
  overflow: hidden;
}
.main-top {
  line-height: 6vh;
  width: 100%;
  border-bottom: 1px solid #ccc;
}
</style>
