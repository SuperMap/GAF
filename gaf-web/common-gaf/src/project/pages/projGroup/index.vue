<template>
  <div class="app-container">
    <div class="page-single">
      <gaf-table-layout v-show="!showGroupUsers && !showGroupProjects">
        <template #actions>
          <button class="btn-fun blue" @click="handleAdd">
            <span><a-icon type="plus" />新增</span>
          </button>
        </template>
        <gaf-table-with-page
          :pagination="pagination"
          :data-source="projGroupList"
          :loading="loading"
          :columns="columns.filter((item) => item.dataIndex !== 'projGroupId')"
          style="width: 100%; height: 570px"
          size="small"
          @change="tableChange"
        >
          <template slot="description" slot-scope="text">
            <div class="description">
              <a-tooltip class="shortcutTip">
                <template slot="title">
                  {{ text }}
                </template>
                <span>
                  {{ text }}
                </span>
              </a-tooltip>
            </div>
          </template>
          <template slot="visibility" slot-scope="text">
            <span>{{ text === 'private' ? '私有' : '公开' }}</span>
          </template>
          <template slot="operation" slot-scope="text, record">
            <a
              class="btn-human"
              href="javascript:;"
              @click.stop="() => groupUsers(record)"
            >
              <a-icon type="team" /> 成员
            </a>
            <span class="divider">|</span>
            <a
              class="btn-project"
              href="javascript:;"
              @click.stop="() => groupProjects(record)"
            >
              <a-icon type="project" /> 项目
            </a>
          </template>
        </gaf-table-with-page>
      </gaf-table-layout>

      <gaf-modal
        v-model="generalVisible"
        :width="800"
        :footer="null"
        title="新增工程组"
      >
        <add-edit-form
          :edit-data="editData"
          @submit="handleSubmit"
          @back="handleCancel()"
        ></add-edit-form>
      </gaf-modal>

      <div v-show="showGroupUsers && !showGroupProjects">
        <group-users
          :record-item="recordItem"
          :is-requesting="showGroupUsers"
          @submit="handleSubmit"
          @back="handleBack"
        >
        </group-users>
      </div>

      <div v-show="showGroupProjects && !showGroupUsers">
        <group-projects
          :record-item="projectItem"
          :is-requesting="showGroupProjects"
          @back="handleBack"
        ></group-projects>
      </div>
    </div>
  </div>
</template>

<script>
//test
import AddEditForm from '../../views/projGroup/AddOrEditForm'
import GroupUsers from '../../views/projGroup/GroupUsers'
import GroupProjects from '../../views/projGroup/GroupProjects'

export default {
  components: {
    AddEditForm,
    GroupUsers,
    GroupProjects,
  },
  data() {
    return {
      // 编辑记录
      editData: {},
      // 总条数
      total: 0,
      // ${functionName}表格数据
      projGroupList: [],
      // 分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0,
      },
      // 列表是否加载中
      loading: true,
      generalVisible: false,
      showGroupUsers: false,
      showGroupProjects: false,
      recordItem: {},
      projectItem: {},
      columns: [
        {
          title: '工程组id',
          dataIndex: 'projGroupId',
          key: 'proj_group_id',
        },
        {
          title: '工程组名称',
          dataIndex: 'projGroupName',
          key: 'proj_group_name',
        },
        {
          title: '工程组路径',
          dataIndex: 'webUrl',
          key: 'web_url',
          width: '10%',
        },
        {
          title: '工程组描述',
          dataIndex: 'description',
          key: 'description',
          scopedSlots: { customRender: 'description' },
        },
        {
          title: '可见性',
          dataIndex: 'visibility',
          key: 'visibility',
          scopedSlots: {
            customRender: 'visibility',
          },
        },
        {
          title: '项目数',
          dataIndex: 'totalProject',
          key: 'totalProject',
        },
        {
          title: '成员数',
          dataIndex: 'totalMember',
          key: 'totalMember',
        },
        {
          title: '操作',
          scopedSlots: { customRender: 'operation' },
        },
      ],
    }
  },
  created() {
    this.getList()
  },
  methods: {
    handleCancel() {
      this.generalVisible = false
    },
    // 页码，排序项发生改变时，重新获取列表数据
    tableChange(pageInfo) {
      if (pageInfo) {
        this.pagination.current = pageInfo.current
        this.pagination.pageSize = pageInfo.pageSize
      }
      this.getList()
    },
    // 添加数据
    handleAdd() {
      this.generalVisible = true
    },
    // 添加修改提交后
    handleSubmit() {
      this.editData = {}
      this.getList()
      this.generalVisible = false
    },
    // 添加修改返回后
    handleBack() {
      this.editData = {}
      this.showGroupUsers = false
      this.showGroupProjects = false
      this.getList()
    },
    // 删除数据
    async handleDelete(row) {
      const url =
        '/my-jersey-servicemytes02/projGroups/projGroup/' + row.projGroupId
      const rst = await this.$axios.delete(url)
      if (rst.data.isSuccessed) {
        this.$message.success('删除成功')
      } else {
        this.$message.error('删除失败, 原因:' + rst.data.message)
      }
      this.$nextTick(() => {
        this.getList()
      })
    },
    async getList() {
      this.loading = true
      const url = '/proj/groups'
      const res = await this.$axios.$get(url)
      this.loading = false
      if (res.isSuccessed) {
        this.projGroupList = res.data
      } else {
        this.$message.error(`查询失败,原因:${res.message}`)
      }
    },
    groupUsers(record) {
      this.recordItem = record
      this.showGroupProjects = false
      this.showGroupUsers = true
    },
    groupProjects(record) {
      this.projectItem = record
      this.showGroupUsers = false
      this.showGroupProjects = true
    },
  },
}
</script>

<style scoped>
.divider {
  background: #e8e8e8;
  color: transparent;
  font-size: 1px;
  margin: 0 3px;
}

.shortcutTip {
  width: 370px;
}

.description {
  overflow: hidden;
  text-overflow: ellipsis;
  width: 180px;
}
</style>
