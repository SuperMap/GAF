<template>
  <div>
    <gaf-table-layout v-show="!showDetail">
      <template #actions>
        <a-tooltip
          placement="rightTop"
          title="模态弹窗式新增角色，当表单信息相对较少时使用，当内容较多时建议使用列表中详情模式。涉及到如何获取表单内容,"
        >
          <a-button
            @click="handleAction(null, '新建角色')"
            icon="plus"
            type="primary"
          >
            新建角色
          </a-button>
        </a-tooltip>
      </template>
      <template #filter>
        <gaf-search
          @search="keyword => handleFilterChange('keyword', keyword)"
          placeholder="请输入角色名称进行查询"
        />
      </template>
      <template #default>
        <gaf-table-with-page
          :loading="loading"
          :columns="columns"
          :data-source="roleList"
          :pagination="pagination"
          @change="queryTableData"
          bordered
          row-key="id"
          size="small"
        >
          <template slot="action" slot-scope="text, record">
            <a-tooltip
              placement="leftTop"
              title="列表条目详情信息展示、编辑等，当表单信息相对较多时使用。涉及到如何填充表单数据、如何获取表单内容,"
            >
              <a @click.stop="viewDetail(record)" href="javascript:;">
                <a-icon type="edit" /> 详情
              </a>
            </a-tooltip>
            <a-divider type="vertical" />
            <a-tooltip
              placement="leftTop"
              title="模态弹窗式编辑角色，当表单信息相对较少时使用，当内容较多时建议使用列表中详情模式。涉及到如何填充表单数据、如何获取表单内容,"
            >
              <a
                @click.stop="handleAction(record, '编辑角色')"
                href="javascript:;"
              >
                <a-icon type="edit" /> 编辑
              </a>
            </a-tooltip>
            <a-divider type="vertical" />
            <a-popconfirm
              @confirm="deleteItem(record)"
              title="删除后无法恢复，确认是否继续?"
              ok-text="确认"
              cancel-text="取消"
            >
              <a href="#"><a-icon type="delete" /> 删除</a>
            </a-popconfirm>
          </template>
        </gaf-table-with-page>
      </template>
      <gaf-modal v-model="visible" :title="title" :width="400" @ok="saveItem">
        <add-edit-form ref="myModal" :role-item="roleItem" />
      </gaf-modal>
    </gaf-table-layout>
    <role-detail
      v-show="showDetail"
      :role-info="roleItem"
      @back="showDetail = false"
    />
  </div>
</template>

<script>
import AddEditForm from '~/views/demo/roles/AddEditForm'
import RoleDetail from '~/views/demo/roles/RoleDetail'
const columns = [
  { title: '角色名称', dataIndex: 'name', key: 'name' },
  { title: '描述', dataIndex: 'desc', key: 'desc' },
  {
    title: '操作',
    dataIndex: 'action',
    key: 'action',
    width: 300,
    align: 'center',
    scopedSlots: { customRender: 'action' }
  }
]
export default {
  components: {
    AddEditForm,
    RoleDetail
  },
  data: function() {
    return {
      loading: false,
      title: '',
      roleItem: null,
      roleList: [
        {
          id: 0,
          name: '系统管理员',
          desc: '系统管理员'
        },
        {
          id: 1,
          name: '数据管理员',
          desc: '数据管理员'
        }
      ],
      columns: columns,
      visible: false,
      showDetail: false,
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0
      },
      query: {
        keyword: '',
        tenantId: ''
      }
    }
  },
  created() {
    // this.loadRoleList()
  },
  methods: {
    handleAction(roleItem, title) {
      this.visible = true
      this.roleItem = roleItem
      this.title = title
    },
    deleteItem(record) {
      const index = this.roleList.findIndex(r => r.id === record.id)
      this.roleList.splice(index, 1)
    },
    viewDetail(record) {
      this.roleItem = record
      this.showDetail = true
    },
    queryTableData(pageInfo) {
      if (pageInfo) {
        this.pagination.current = pageInfo.current
      }
      this.$nextTick(function() {
        // this.loadRoleList()
      })
    },
    loadRoleList: async function() {
      this.loading = true
      const { keyword } = this.query
      let url = `/security/roles?pageIndex=${this.pagination.current}`
      if (keyword) {
        url = `${url}&key=${keyword}`
      }
      const res = await this.$axios.$get(url)
      this.loading = false
      this.pagination.total = res.total
      if (res) {
        this.roleList = res.content
        this.pagination.total = res.total
      } else {
        this.$message.error('查询失败')
      }
    },
    handleFilterChange(name, value) {
      this.query[name] = value
      this.$nextTick(function() {
        // this.loadRoleList()
      })
    },
    saveItem() {
      const form = this.$refs.myModal.form
      form.validateFields((err, values) => {
        if (err) {
          return
        }
        const roleInfo = {
          name: values.name,
          desc: values.desc
        }
        if (this.roleItem) {
          roleInfo.id = this.roleItem.id
          const index = this.roleList.findIndex(r => r.id === roleInfo.id)
          this.roleList.splice(index, 1, roleInfo)
        } else {
          roleInfo.id = this.roleList.length
          this.roleList.push(roleInfo)
        }
        this.visible = false
        form.resetFields()
        this.roleItem = null
        // const url = `/security/roles`
        // const result = await this.$axios.$post(url, roleInfo)
        // if (result.isSuccessed) {
        //   this.$message.success('操作成功')
        //   this.loadRoleList()
        //   this.visible = false
        // form.resetFields()
        // } else {
        //   this.$message.error(result.message)
        // }
      })
    }
  }
}
</script>

<style scoped></style>
