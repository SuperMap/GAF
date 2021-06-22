<template>
  <div class="app-container">
    <div class="page-left">
      <!-- <div class="action-div">
        <a-button
          @click="handleAddDepartment"
          class="add-user-btn"
        >
          <span>新增子部门</span>
        </a-button>

        <a-button
          @click="handleAddPost"
          class="add-user-btn"
        >
          新增岗位
        </a-button>
      </div> -->
      <div class="let-btm">
        <!-- <div class="tree-catalog">
          <span class="vertical-line">| </span>
          部门岗位
        </div> -->
        <gaf-tree-transparent
          ref="myGafTree"
          :dataOfTree="dataOfTree"
          :searchType="[2, 3]"
          :expandedNodeKeys.sync="expandedNodeKeys"
          :selectedKeys.sync="selectedNodeKeys"
          :searchPlaceholder="searchPlaceholder"
          @select="onSelect"
          :show-line="true"
        >
          <template v-slot:icon="{ iconNodeType }">
            <a-icon
              :type="iconNodeType.type === 2 ? 'team' : 'solution'"
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
        default-active-key="6"
      >
        <a-tab-pane key="1" v-if="departmentShow" tab="部门信息">
          <div v-show="showPaneContent" class="special-container">
            <add-edit-department
              ref="departmentAddEditForm"
              :editData="departmentEditData"
              :operation="departmentOperation"
              @updateDepartmentSuccess="afterUpdateDepartmentSuccess"
              @deleteDepartmentSuccess="afterDeleteDepartmentSuccess"
              @addDepartmentSuccess="afterAddDepartmentSuccess"
              @cancleWhenAddDepartment="cancleWhenAddDepartment"
            >
            </add-edit-department>
          </div>
        </a-tab-pane>
        <a-tab-pane key="2" v-if="departmentShow" tab="部门用户">
          <div v-show="showPaneContent" class="list-table">
            <user-table-no-page
              ref="departmentUserTable"
              :id="departmentId"
              :isDepartmentId="true"
            ></user-table-no-page>
          </div>
        </a-tab-pane>
        
        <a-tab-pane key="3" v-if="!departmentShow" tab="岗位信息">
          <div class="special-container">
          <add-edit-post
            ref="postAddEditForm"
            v-show="showPaneContent"
            :editData="postEditData"
            :operation="postOperation"
            @updatePostSuccess="afterUpdatePostSuccess"
            @deletePostSuccess="afterDeletePostSuccess"
            @addPostSuccess="afterAddPostSuccess"
            @cancleWhenAddPost="cancleWhenAddPost"
          >
          </add-edit-post>
          </div>
        </a-tab-pane>
        <a-tab-pane key="4" v-if="!departmentShow" tab="绑定角色">
          <div class="main-top">
            <a-button
              @click="handleAdd"
              class="btn-fun blue main-top-button"
            >
              <span><a-icon type="arrow-left" />添加至当前岗位</span>
            </a-button>
            <gaf-tree-transparent
              :dataOfTree="roleOfTree"
              :searchType="[0]"
              :expandedNodeKeys.sync="expandedNodeKeys2"
              :checkable="true"
              :checkNodeStrictly="false"
              v-model="checkedNodeKeys"
              search-placeholder="请输入角色名查询"
            >
              <template v-slot:icon="{ iconNodeType }">
                <a-icon
                  :type="iconNodeType.type === 5 ? 'switcher' : 'branches'"
                ></a-icon>
              </template>
            </gaf-tree-transparent>
          </div>
        </a-tab-pane>
        <a-tab-pane key="5" v-if="!departmentShow" tab="岗位人员">
          <div v-show="showPaneContent" class="list-table">
            <user-table-no-page
              ref="postUserTable"
              :id="postId"
              :isDepartmentId="false"
            ></user-table-no-page>
          </div>
        </a-tab-pane>
        <a-tab-pane key="6" v-if="departmentShow" tab="部门子部门">
          <div v-show="showPaneContent" class="list-table">
            <add-department
              ref="departmentAddEditForm"
              :editData="departmentEditData"
              :operation="departmentOperation"
              @afterFormUpdateSuccess="afterUpdateDepartmentSuccess"
              @afterDeleteSuccess="afterDeleteDepartmentSuccess"
              @afterFormAddSuccess="afterAddDepartmentSuccess"
              @cancleWhenAdd="cancleWhenAddDepartment"
              @handleAddDepartment="handleAddDepartment"
              :departmentList="departmentList"
              addButtonName="新增子部门"
            ></add-department>
          </div>
        </a-tab-pane>
        <a-tab-pane key="7" v-if="departmentShow" tab="部门岗位">
          <div v-show="showPaneContent" class="list-table">
            <add-post
              ref="postAddEditForm"
              v-show="showPaneContent"
              :editData="postEditData"
              :operation="postOperation"
              @afterFormUpdateSuccess="afterUpdatePostSuccess"
              @afterDeleteSuccess="afterDeletePostSuccess"
              @afterFormAddSuccess="afterAddPostSuccess"
              @cancleWhenAdd="cancleWhenAddPost"
              @handleAddPost="handleAddPost"
              :postList="postList"
              addButtonName="新增岗位"
            ></add-post>
          </div>
        </a-tab-pane>
      </a-tabs>
    </div>
  </div>
</template>

<script>
import AddEditDepartment from '../../views/AuthDepartment/AddOrEditForm'
import AddEditPost from '../../views/AuthPost/AddOrEditForm'
import UserTableNoPage from './UserTableNoPage'
import addPost from './addPost'
import addDepartment from './addDepartment'
import '~/assets/css/common.css'

export default {
  components: {
    AddEditDepartment,
    AddEditPost,
    UserTableNoPage,
    addPost,
    addDepartment
  },
  data() {
    return {
      searchPlaceholder: '请输入岗位名称查询',
      // tree
      nodeType: [],
      roleTreeNodes: [],
      checkedNodeKeys: [],
      dataOfTree: [],
      roleOfTree: [],
      expandedNodeKeys: [],
      selectedNodeKeys: [],
      expandedNodeKeys2: [],
      // department
      departmentShow: true,
      departmentOperation: 3, // 详情：1，新增：2，编辑：3
      departmentEditData: {},
      // post 详情：1，新增：2，编辑：3
      postOperation: 3,
      postEditData: {},
      // tabs
      activeKey: '6',
      // selectedNodeKeyVesion
      selectedNodeKeyVesion: 1,
      tab1SelectedKeyVesion: 0,
      tab2SelectedKeyVesion: 0,
      tab3SelectedKeyVesion: 0,
      tab4SelectedKeyVesion: 0,
      tab5SelectedKeyVesion: 0,
      // 岗位id
      postId: '',
      // 部门id
      departmentId: '',
      departmentList: [],
      postList: []
    }
  },
  computed: {
    showPaneContent: function() {
      return this.selectedNodeKeys && this.selectedNodeKeys.length > 0
    }
  },
  watch: {
    postId: function() {
      this.getSelected()
    },
    departmentShow: function(newDepartmentShow) {
      if (newDepartmentShow) {
        this.activeKey = '1'
      } else {
        this.activeKey = '3'
      }
    },
    selectedNodeKeys: function() {
      this.selectedNodeKeyVesion += 1
    }
  },
  async mounted() {
    await this.getTree()
    await this.getRoleTree()
    await this.getSelected()
    
  },
  methods: {
    async getSelected() {
      const url = `/authority/auth-post-role/list-by-post/${this.postId}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        if (res.data !== null && res.data.length > 0) {
          this.checkedNodeKeys = res.data.map(item => item.roleId)
        } else {
          this.checkedNodeKeys = []
        }
      }
    },
    async handleAdd() {
      const checkedRoles = this.roleTreeNodes
        .filter(
          item =>
            this.checkedNodeKeys.indexOf(item.key) !== -1 && item.type === 5
        )
        .map(i => i.key)
      const url = `/authority/auth-post-role/handle`
      const data = {
        postId: this.postId,
        roleList: checkedRoles
      }
      const res = await this.$axios.$post(url, data)
      if (res.isSuccessed) {
        this.$message.success('添加成功')
      } else {
        this.$message.error(`添加失败,原因:${res.message}`)
        this.getSelected()
      }
    },
    async getRoleTree() {
      const url = `/authority/auth-roles/tree`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.roleTreeNodes = res.data
        this.roleOfTree = this.$refs.myGafTree.convertToTree(res.data)
      } else {
        this.$message.error('加载角色树失败,原因：' + res.message)
      }
    },
    updatPostUser(selectedNodeKeys) {
      this.postId = selectedNodeKeys[0]
      this.$nextTick(function() {
        this.$refs.postUserTable.getList()
      })
    },
    updateDepartmentUser(selectedNodeKeys) {
      this.departmentId = selectedNodeKeys[0]
      this.$nextTick(function() {
        this.$refs.departmentUserTable.getList()
      })
    },
    updateDepartmentEditForm(selectedKeys) {
      const parent = this.$refs.myGafTree.getParent(
        selectedKeys[0],
        this.dataOfTree
      )
      this.getDepartmentAndSet(
        parent ? parent.title : '无',
        selectedKeys[0]
      ).then(isSuccessed => {
        if (isSuccessed) {
          this.departmentOperation = 3
        }
      })
    },
    updatePostEditForm(selectedKeys) {
      const parent = this.$refs.myGafTree.getParent(
        selectedKeys[0],
        this.dataOfTree
      )
      this.getPostAndSet(parent ? parent.title : '无', selectedKeys[0]).then(
        isSuccessed => {
          if (isSuccessed) {
            this.postOperation = 3
          }
        }
      )
    },
    async getTree() {
      const url = '/authority/auth-posts/tree'
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.activeKey = '6'
        this.dataOfTree = res.data
        this.departmentList = this.dataOfTree[0].children.filter(item => item.type === 2)
        this.postList = this.dataOfTree[0].children.filter(item => item.type === 3)
        this.selectedNodeKeys = [this.dataOfTree[0].key]
      } else {
        this.$message.error(`查询失败,原因:${res.message}`)
      }
    },
    onSelect(selectedKeys, e) {
      this.departmentList = []
      this.postList = []
      if (e.node.dataRef.type === 2 && e.node.dataRef.children){
        this.departmentList = e.node.dataRef.children.filter(item => item.type === 2)
        this.postList = e.node.dataRef.children.filter(item => item.type === 3)
      }
      this.nodeType = e.node.dataRef.type
      if (e.selected) {
        if (e.node.dataRef.type === 2) {
          this.departmentShow = true
          this.$nextTick(function() {
            if (this.activeKey === '1') {
              this.updateDepartmentEditForm(selectedKeys)
              this.tab1SelectedKeyVesion = this.selectedNodeKeyVesion
            } else if (this.activeKey === '2') {
              this.updateDepartmentUser(selectedKeys)
              this.tab2SelectedKeyVesion = this.selectedNodeKeyVesion
            }
          })
        } else if (e.node.dataRef.type === 3) {
          this.postId = e.node.dataRef.key
          this.departmentShow = false
          this.$nextTick(function() {
            if (this.activeKey === '3') {
              this.updatePostEditForm(selectedKeys)
              this.tab3SelectedKeyVesion = this.selectedNodeKeyVesion
            } else if (this.activeKey === '4') {
              this.tab4SelectedKeyVesion = this.selectedNodeKeyVesion
            } else if (this.activeKey === '5') {
              this.updatPostUser(selectedKeys)
              this.tab5SelectedKeyVesion = this.selectedNodeKeyVesion
            }
          })
        }
      }
    },
    afterUpdateDepartmentSuccess(departmentData) {
      this.$refs.myGafTree.updateNode({
        key: departmentData.departmentId,
        title: departmentData.departmentName,
        sortSn: departmentData.sortSn,
        parentId: departmentData.parentId
      })
      this.departmentEditData = departmentData
      this.selectedNodeKeys = [departmentData.departmentId]
    },
    afterDeleteDepartmentSuccess(departmentData) {
      const parentId =
        departmentData.parentId && departmentData.parentId.trim() !== ''
          ? departmentData.parentId
          : '0'
      // this.$refs.myGafTree.deleteNode({
      //   key: departmentData.departmentId,
      //   parentId: parentId
      // })
      if (departmentData.key && departmentData.key.length > 0){
        console.log('sdasdafaf',departmentData)
        departmentData.key.forEach(item => {
          this.$refs.myGafTree.deleteNode({
            key: item,
            parentId: parentId
          })
        })
      } else {
        console.log(departmentData)
        this.$refs.myGafTree.deleteNode({
          key: departmentData.departmentId,
          parentId: parentId
        })
      }
      // this.selectedNodeKeys = []
    },
    afterAddDepartmentSuccess(departmentData) {
      this.departmentList.push({
        key: departmentData.departmentId,
        title: departmentData.departmentName,
        sortSn: departmentData.sortSn,
        parentId: departmentData.parentId,
        type: 2,
        scopedSlots: { title: 'title' }
      })
      this.$refs.myGafTree.inserNode({
        key: departmentData.departmentId,
        title: departmentData.departmentName,
        sortSn: departmentData.sortSn,
        parentId: departmentData.parentId,
        type: 2,
        scopedSlots: { title: 'title' }
      })
      if (this.expandedNodeKeys.indexOf(departmentData.parentId) <= -1) {
        this.expandedNodeKeys.push(departmentData.parentId)
      }
      // this.selectedNodeKeys = [departmentData.departmentId]
      let parentName = '无'
      if (departmentData.parentId !== '0') {
        const parentNode = this.$refs.myGafTree.getNode(departmentData.parentId)
        if (parentNode) {
          parentName = parentNode.title
        }
      }
      departmentData.parentName = parentName
      this.departmentOperation = 3
      this.departmentEditData = departmentData
    },
    cancleWhenAddDepartment(id) {
      if (id) {
        this.selectedNodeKeys = [id]
        this.updateDepartmentEditForm(this.selectedNodeKeys)
        this.$nextTick(function() {
          this.tab1SelectedKeyVesion = this.selectedNodeKeyVesion
        })
      }
    },
    afterUpdatePostSuccess(postData) {
      this.$refs.myGafTree.updateNode({
        key: postData.postId,
        title: postData.postName,
        sortSn: postData.sortSn,
        parentId: postData.departmentId
      })
      this.postEditData = postData
      this.selectedNodeKeys = [postData.postId]
    },
    afterDeletePostSuccess(postData) {
      console.log(postData)
      const parentId =
        postData.departmentId && postData.departmentId.trim() !== ''
          ? postData.departmentId
          : '0'
      if (postData.key && postData.key.length > 0){
        postData.key.forEach(item => {
          this.$refs.myGafTree.deleteNode({
            key: item,
            parentId: parentId
          })
        })
      } else {
        this.$refs.myGafTree.deleteNode({
          key: postData.postId,
          parentId: parentId
        })
      }
      
      // this.selectedNodeKeys = []
    },
    afterAddPostSuccess(postData) {
      this.postList.push({
        key: postData.postId,
        parentId: postData.departmentId,
        sortSn: postData.sortSn,
        title: postData.postName,
        type: 3,
        scopedSlots: { title: 'title' }
      })
      this.$refs.myGafTree.inserNode({
        key: postData.postId,
        title: postData.postName,
        sortSn: postData.sortSn,
        parentId: postData.departmentId,
        type: 3,
        scopedSlots: { title: 'title' }
      })
      if (this.expandedNodeKeys.indexOf(postData.departmentId) <= -1) {
        this.expandedNodeKeys.push(postData.departmentId)
      }
      // this.selectedNodeKeys = [postData.postId]
      let parentName = '无'
      if (postData.departmentId !== '0') {
        const parentNode = this.$refs.myGafTree.getNode(postData.departmentId)
        if (parentNode) {
          parentName = parentNode.title
        }
      }
      postData.departmentName = parentName
      this.postOperation = 3
      this.postEditData = postData
    },
    cancleWhenAddPost(departmentId) {
      if (departmentId) {
        this.departmentShow = true
        this.activeKey = '1'
        this.selectedNodeKeys = [departmentId]
        this.$nextTick(function() {
          this.updateDepartmentEditForm(this.selectedNodeKeys)
          this.tab1SelectedKeyVesion = this.selectedNodeKeyVesion
        })
      }
    },
    tabChange(activeKey) {
      if (this.selectedNodeKeys && this.selectedNodeKeys.length === 1) {
        this.$nextTick(function() {
          if (
            activeKey === '1' &&
            this.tab1SelectedKeyVesion < this.selectedNodeKeyVesion
          ) {
            if (this.departmentOperation === 3) {
              this.updateDepartmentEditForm(this.selectedNodeKeys)
              this.tab1SelectedKeyVesion = this.selectedNodeKeyVesion
            }
          } else if (
            activeKey === '2' &&
            this.tab2SelectedKeyVesion < this.selectedNodeKeyVesion
          ) {
            this.updateDepartmentUser(this.selectedNodeKeys)
            // todo isSuceess
            this.tab2SelectedKeyVesion = this.selectedNodeKeyVesion
          } else if (
            activeKey === '3' &&
            this.tab3SelectedKeyVesion < this.selectedNodeKeyVesion
          ) {
            if (this.postOperation === 3) {
              this.updatePostEditForm(this.selectedNodeKeys)
              this.tab1SelectedKeyVesion = this.selectedNodeKeyVesion
            }
          } else if (
            activeKey === '4' &&
            this.tab4SelectedKeyVesion < this.selectedNodeKeyVesion
          ) {
            //
          } else if (
            activeKey === '5' &&
            this.tab5SelectedKeyVesion < this.selectedNodeKeyVesion
          ) {
            this.updatPostUser(this.selectedNodeKeys)
            // todo isSuccess
            this.tab5SelectedKeyVesion = this.selectedNodeKeyVesion
          }
        })
      }
    },
    // 添加数据
    handleAddDepartment() {
      console.log('here')
      this.departmentShow = true
      this.$nextTick(function() {
        // 此时已经渲染完成
        // this.$refs.departmentAddEditForm.resetFields()
        if (this.selectedNodeKeys && this.selectedNodeKeys.length === 1) {
          this.getDepartment(this.selectedNodeKeys[0]).then(data => {
            if (data) {
              this.departmentEditData = {
                parentId: data.departmentId,
                parentName: data.departmentName,
                isThirdParty: data.isThirdParty
              }
              this.departmentOperation = 2
              // this.activeKey = '1'
            }
          })
        } else {
          this.$message.info('请选择一个部门')
          // this.selectedNodeKeys = ['0']
          // this.departmentEditData = { parentId: '0', parentName: '无' }
          // this.departmentOperation = 2
          // this.activeKey = '1'
        }
      })
    },
    handleAddPost() {
      console.log('zhel')
      if (this.selectedNodeKeys && this.selectedNodeKeys.length === 1) {
        const node = this.$refs.myGafTree.getNode(this.selectedNodeKeys[0])
        if (node.type === 2) {
          // this.departmentShow = false
          // 此时 DOM 还没有更新，设置回调函数
          this.$nextTick(function() {
            // 此时已经渲染完成
            // this.$refs.postAddEditForm.resetFields()
            this.postEditData = {
              departmentId: node.key,
              departmentName: node.title
            }
            this.postOperation = 2
            // this.activeKey = '3'
          })
        } else {
          this.$message.info('请选择一个部门')
        }
      } else {
        this.$message.info('请选择一个部门')
      }
    },
    async getDepartmentAndSet(parentName, key) {
      const url = `/authority/auth-departments/${key}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        res.data.parentName = parentName
        this.departmentEditData = res.data
        return true
      } else {
        this.$message.error(`取部门信息失败，原因:${res.message}`)
        return false
      }
    },
    async getDepartment(key) {
      const url = `/authority/auth-departments/${key}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        return res.data
      } else {
        this.$message.error(`取部门信息失败，原因:${res.message}`)
        return null
      }
    },
    async getPostAndSet(parentName, key) {
      const data = await this.getPost(key)
      if (data) {
        data.departmentName = parentName
        this.postEditData = data
        return true
      } else {
        return false
      }
    },
    async getPost(key) {
      const url = `/authority/auth-posts/${key}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        return res.data
      } else {
        this.$message.error(`取部门信息失败，原因:${res.message}`)
        return null
      }
    }
  }
}
</script>

<style scoped>
/* .app-container {
  height: 100%;
}

.left {
  float: left;
  height: 100%;
  width: 25%;
  box-sizing: border-box;
  margin-right: -1px;
} */
.let-btm {
  overflow-y: auto;
  overflow-x: auto;
  height: 93%;
  padding: 2%;
}

.add-user-btn {
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

.list-table {
  width: 100%;
  padding: 0 0 0 1px;
  margin-top: 1%;
}
.main-top {
  height: 100%;
  width: 100%;
  float: left;
  width: 30%;
  padding: 10px 0 0 10px;
}
.main-top-button {
  margin: 10px 0;
}
.action-div {
  text-align: center;
  overflow: hidden;
  margin-bottom: 10px;
}
.ant-tabs {
  width: 100%;
  height: 100%;
}
</style>
