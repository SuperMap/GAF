<template>
  <div class="app-container">
    <div v-show="!showAddform">
    <div class="page-left">
      <!-- <div class="action-div">
        <button
          @click="handleAddUser"
          class="add-user-btn"
          ><span><a-icon type="plus" />新增用户</span></button
        >
      </div> -->
      <div class="tree-div">
        <!-- <div class="tree-catalog">
          <span class="vertical-line">| </span>
          部门用户
        </div> -->
        <gaf-tree-transparent
          ref="myGafTree"
          :searchPlaceholder="searchPlaceholder"
          :dataOfTree="dataOfTree"
          :searchType="[0]"
          :expandedNodeKeys.sync="expandedNodeKeys"
          :selectedKeys.sync="selectedNodeKeys"
          @select="onSelect"
          :show-line="true"
        >
          <template v-slot:icon="{ iconNodeType }">
            <a-icon :type="iconNodeType.type === 2 ? 'team' : 'user'"></a-icon>
          </template>
        </gaf-tree-transparent>
      </div>
    </div>
    <div class="page-right">
      <a-tabs
        ref="tabs"
        v-model="activeKey"
        @change="tabChange"
        default-active-key
      >
        <a-tab-pane key="1" v-if="!departmentShow" tab="用户信息">
          <add-edit-form
            v-if="showPaneContent"
            :editData="userEditData"
            :operation="userOperation"
            @updateUserSuccess="afterUpdateUserSuccess"
            @deleteUserSuccess="afterDeleteUserSuccess"
            @addUserSuccess="afterAddUserSuccess"
            @cancleWhenAddUser="cancleWhenAddUser"
          ></add-edit-form>
        </a-tab-pane>
        <a-tab-pane key="2" v-if="!departmentShow" tab="绑定角色">
          <div v-show="showPaneContent" class="main-top">
            <div class="role-tree">
              <gaf-tree-transparent
                :dataOfTree="roleOfTree"
                :searchType="[0]"
                :expandedNodeKeys.sync="expandedNodeKeys2"
                :checkable="true"
                :checkNodeStrictly="false"
                :searchStyle="{ margin: '10px', width: '100%' }"
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
            <div class="add-botton">
              <button
                @click="handleAdd"
                class="main-top-button"
                ><span><a-icon type="arrow-left" />添加至当前用户</span></button
              >
            </div>
          </div>
        </a-tab-pane>
        <a-tab-pane key="3" v-if="!departmentShow" tab="挂职">
          <div v-show="showPaneContent">
            <auth-user-parttime :userId="parttimeUserId"></auth-user-parttime>
          </div>
        </a-tab-pane>
        <a-tab-pane key="4" v-if="departmentShow" tab="部门用户">
          <div v-show="showPaneContent">
            <user-table-no-page
              ref="departmentUserTable"
              :id="departmentId"
              :isDepartmentId="true"
              :isUser="true"
              @deleteUserSuccess="afterDeleteUserSuccessInDepartmentShow"
              @activeUserSuccess="afterActiveUserSuccessInDepartmentShow"
              @handleAddUser="handleAddUser"
            ></user-table-no-page>
          </div>
        </a-tab-pane>
      </a-tabs>
    </div>
    </div>
    <add-edit-form
      ref="userAddEditForm"
      v-if="showAddform"
      :editData="userEditData"
      :operation="userOperation"
      @updateUserSuccess="afterUpdateUserSuccess"
      @deleteUserSuccess="afterDeleteUserSuccess"
      @addUserSuccess="afterAddUserSuccess"
      @cancleWhenAddUser="cancleWhenAddUser"
    ></add-edit-form>
  </div>
</template>

<script>
import AddEditForm from '../../views/AuthUser/AddOrEditForm'
import UserTableNoPage from '../DepartmentPostManage/UserTableNoPage'
import AuthUserParttime from '../AuthUserParttime/index'
import '~/assets/css/common.css'

export default {
  components: {
    AddEditForm,
    UserTableNoPage,
    AuthUserParttime
  },
  data() {
    return {
      searchPlaceholder: '请输入用户名称查询',
      roleOfTree: [],
      roleTreeNodes: [],
      expandedNodeKeys2: [],
      checkedNodeKeys: [],
      dataOfTree: [],
      expandedNodeKeys: [],
      selectedNodeKeys: [],
      departmentShow: false,
      userEditData: {},
      userOperation: 2,
      activeKey: '1',
      selectedNodeKeyVesion: 1,
      tab1SelectedKeyVesion: 0,
      tab2SelectedKeyVesion: 0,
      tab3SelectedKeyVesion: 0,
      tab4SelectedKeyVesion: 0,
      userId: '',
      departmentId: '',
      parttimeUserId: '',
      showAddform: false
    }
  },
  computed: {
    showPaneContent: function() {
      return this.selectedNodeKeys && this.selectedNodeKeys.length > 0
    }
  },
  watch: {
    userId: function() {
      this.getSelected()
    },
    departmentShow: function(newDepartmentShow) {
      if (newDepartmentShow) {
        this.activeKey = '4'
      } else {
        this.activeKey = '1'
      }
    },
    selectedNodeKeys: function() {
      this.selectedNodeKeyVesion += 1
    }
  },
  created() {
    this.getTreeData()
    this.getRoleTree()
  },
  methods: {
    async handleAdd() {
      const checkedRoles = this.roleTreeNodes
        .filter(
          item =>
            this.checkedNodeKeys.indexOf(item.key) !== -1 && item.type === 5
        )
        .map(i => i.key)
      // const url = `/authority/auth-user-role/handle`
      const url = `/authority/auth-user-role/batch/` + this.userId
      // const data = {
      //   userId: this.userId,
      //   roleList: checkedRoles
      // }
      const addList = checkedRoles.map(function(item) {
        return { userId: this.userId, roleId: item, status: true }
      }, this)

      const res = await this.$axios.$post(url, addList)
      if (res.isSuccessed) {
        this.$message.success('添加成功')
      } else {
        this.$message.error(`添加失败,原因:${res.message}`)
        this.getSelected()
      }
    },
    async getSelected() {
      const url = `/authority/auth-user-role/list-by-user/${this.userId}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        if (res.data !== null && res.data.length > 0) {
          this.checkedNodeKeys = res.data.map(item => item.roleId)
        } else {
          this.checkedNodeKeys = []
        }
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
    updateUserParttimeList(selectedKeys) {
      this.parttimeUserId = selectedKeys[0]
    },
    updateUserEditForm(selectedKeys) {
      const parent = this.$refs.myGafTree.getParent(
        selectedKeys[0],
        this.dataOfTree
      )
      this.userOperation = 3
      this.getUserAndSet(parent ? parent.title : '无', selectedKeys[0]).then(
        isSuccessed => {
          if (isSuccessed) {
            this.userOperation = 3
          }
        }
      )
    },
    updateDepartmentUser(selectedNodeKeys) {
      this.departmentId = selectedNodeKeys[0]
      // todo： 这边获取数据再设置
      this.$nextTick(function() {
        this.$refs.departmentUserTable.getList()
      })
    },
    afterActiveUserSuccessInDepartmentShow(userData) {
      this.$refs.myGafTree.inserNode({
        key: userData.userId,
        title: userData.realName,
        sortSn: userData.sortSn,
        parentId: userData.departmentId,
        type: 4,
        scopedSlots: { title: 'title' }
      })
    },
    afterDeleteUserSuccessInDepartmentShow(userData) {
      const parentId =
        userData.departmentId && userData.departmentId.trim() !== ''
          ? userData.departmentId
          : '0'
      this.$refs.myGafTree.deleteNode({
        key: userData.userId,
        parentId: parentId
      })
    },
    afterUpdateUserSuccess(userData) {
      const node = this.$refs.myGafTree.getNode(userData.userId)
      if (node && node.parentId === userData.departmentId) {
        this.$refs.myGafTree.updateNode({
          key: userData.userId,
          title: userData.realName,
          sortSn: userData.sortSn,
          parentId: userData.departmentId
        })
      } else if (node) {
        this.$refs.myGafTree.deleteNode({
          key: node.key,
          parentId: node.parentId
        })
        this.$refs.myGafTree.inserNode({
          key: userData.userId,
          title: userData.realName,
          sortSn: userData.sortSn,
          parentId: userData.departmentId,
          type: 4,
          scopedSlots: { title: 'title' }
        })
      } else {
        this.$refs.myGafTree.inserNode({
          key: userData.userId,
          title: userData.realName,
          sortSn: userData.sortSn,
          parentId: userData.departmentId,
          type: 4,
          scopedSlots: { title: 'title' }
        })
      }
      this.userEditData = userData
      // this.selectedNodeKeys = [userData.userId]
    },
    afterDeleteUserSuccess(userData) {
      const parentId =
        userData.departmentId && userData.departmentId.trim() !== ''
          ? userData.departmentId
          : '0'
      this.$refs.myGafTree.deleteNode({
        key: userData.userId,
        parentId: parentId
      })
      this.selectedNodeKeys = []
    },
    afterAddUserSuccess(userData) {
      this.showAddform = false
      this.$refs.myGafTree.inserNode({
        key: userData.userId,
        title: userData.realName,
        sortSn: userData.sortSn,
        parentId: userData.departmentId,
        type: 4,
        scopedSlots: { title: 'title' }
      })
      if (this.expandedNodeKeys.indexOf(userData.departmentId) <= -1) {
        this.expandedNodeKeys.push(userData.departmentId)
      }
      // this.selectedNodeKeys = [userData.userId]
      let parentName = '无'
      if (userData.departmentId !== '0') {
        const parentNode = this.$refs.myGafTree.getNode(userData.departmentId)
        if (parentNode) {
          parentName = parentNode.title
        }
      }
      userData.departmentName = parentName
      this.userOperation = 3
      this.userEditData = userData
      this.$nextTick(function() {
        this.$refs.departmentUserTable.getList()
      })
    },
    cancleWhenAddUser(formData) {
      this.showAddform = false
      this.departmentShow = true
      this.selectedNodeKeys = [formData.departmentId]
      this.$nextTick(function() {
        this.activeKey = '4'
        this.updateDepartmentUser(this.selectedNodeKeys)
        this.tab4SelectedKeyVesion = this.selectedNodeKeyVesion
      })
    },
    async getTreeData() {
      const url = '/authority/auth-users/tree'
      const res = await this.$axios.get(url)
      if (res.data.isSuccessed) {
        this.dataOfTree = res.data.data
      } else {
        this.$message.error(`获取部门用户树失败，原因：${res.data.message}`)
      }
    },
    onSelect(selectedKeys, e) {
      if (e.selected) {
        if (e.node.dataRef.type === 2) {
          this.departmentShow = true
          this.$nextTick(function() {
            if (this.activeKey === '4') {
              this.updateDepartmentUser(selectedKeys)
              this.tab4SelectedKeyVesion = this.selectedNodeKeyVesion
            }
          })
        } else if (e.node.dataRef.type === 4) {
          this.userId = e.node.dataRef.key
          this.departmentShow = false
          this.$nextTick(function() {
            if (this.activeKey === '1') {
              this.updateUserEditForm(selectedKeys)
              this.tab1SelectedKeyVesion = this.selectedNodeKeyVesion
            } else if (this.activeKey === '2') {
              this.tab2SelectedKeyVesion = this.selectedNodeKeyVesion
            } else if (this.activeKey === '3') {
              // 挂职信息更新
              this.updateUserParttimeList(selectedKeys)
              this.tab3SelectedKeyVesion = this.selectedNodeKeyVesion
            }
          })
        }
      }
    },
    tabChange(activeKey) {
      if (this.selectedNodeKeys && this.selectedNodeKeys.length === 1) {
        this.$nextTick(function() {
          if (
            activeKey === '1' &&
            this.tab1SelectedKeyVesion < this.selectedNodeKeyVesion
          ) {
            if (this.userOperation === 3) {
              this.updateUserEditForm(this.selectedNodeKeys)
              this.tab1SelectedKeyVesion = this.selectedNodeKeyVesion
            }
          } else if (
            activeKey === '2' &&
            this.tab2SelectedKeyVesion < this.selectedNodeKeyVesion
          ) {
            // todo isSuceess
            this.tab2SelectedKeyVesion = this.selectedNodeKeyVesion
          } else if (
            activeKey === '3' &&
            this.tab3SelectedKeyVesion < this.selectedNodeKeyVesion
          ) {
            this.updateUserParttimeList(this.selectedNodeKeys)
            this.tab3SelectedKeyVesion = this.selectedNodeKeyVesion
          } else if (
            activeKey === '4' &&
            this.tab4SelectedKeyVesion < this.selectedNodeKeyVesion
          ) {
            this.updateDepartmentUser(this.selectedNodeKeys)
            // todo : isSuccessed
            this.tab4SelectedKeyVesion = this.selectedNodeKeyVesion
          }
        })
      }
    },
    handleAddUser() {
      this.showAddform = true
      if (this.selectedNodeKeys && this.selectedNodeKeys.length === 1) {
        const node = this.$refs.myGafTree.getNode(this.selectedNodeKeys[0])
        if (node.type === 2) {
          // this.departmentShow = false
          // 此时 DOM 还没有更新，设置回调函数
          this.$nextTick(function() {
            // 此时已经渲染完成
            this.$refs.userAddEditForm.resetFields()
            this.userEditData = {
              departmentId: node.key,
              departmentName: node.title
            }
            this.userOperation = 2
            // this.activeKey = '1'
          })
        } else {
          this.$message.info('请选择一个部门')
        }
      } else {
        this.$message.info('请选择一个部门')
      }
    },
    async getUserAndSet(parentName, key) {
      const data = await this.getUser(key)
      if (data) {
        data.departmentName = parentName
        this.userEditData = data
        return true
      } else {
        return false
      }
    },
    async getUser(key) {
      const url = `/authority/auth-users/${key}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        return res.data
      } else {
        this.$message.error(`取部门信息失败，原因:${res.message}`)
        return null
      }
    },
    async getAuthTenantList() {
      const url = `/authority/auth-tenants`
      const res = await this.$axios.get(url)
      if (res.isSuccessed) {
        const data = res.data
        data.forEach(item => {
          const index = this.authUserList.findIndex(
            val => val.teanantId === item.tenantId
          )
          this.$set(this.authUserList[index], 'tenantName', item.tenantName)
        })
      }
    }
  }
}
</script>

<style scoped>
.main-top {
  height: 100%;
  width: 100%;
  float: left;
}
.main-top-button {
  margin-top: 10px;
  margin-left: 10px;
  border: 1px solid rgb(24, 144, 255);
    /* border-radius: 3px; */
    background-color: white;
    margin-top: 8px;
    height: 36px;
    width: 136px;
    cursor: pointer;
    font-size: 16px;
    background-color: rgb(24, 144, 255);
    color: white;
}

.add-user-btn:hover {
  color: white;
  box-shadow: 0 0 2px 2px rgba(84, 92, 100, 0.25);
}
.list-table {
  width: 100%;
  padding-right: 1px;
}

.action-div {
  text-align: center;
  overflow: hidden;
  margin-bottom: 5px;
}
.tree-div {
  height: 93%;
  padding: 3% 4% 1% 4%;
  overflow: hidden;
  overflow-y: auto;
  overflow-x: auto;
}
.role-tree {
  float: left;
  width: 25%;
}

.add-botton {
  float: left;
  width: 75%;
  padding-left: 10px;
}
.main-top {
  margin-left: 16px;
} 
.add-user-btn {
  cursor: pointer;
  width: 180px;
  height: 32px;
  margin: 3px auto;
  font-size: 14px;
  background-color: rgb(47, 117, 249);
  border: none;
  border-radius: 4px;
  color: white;
  padding: 6px 6px;
}

</style>
