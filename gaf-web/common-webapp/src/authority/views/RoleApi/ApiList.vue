<template>
  <div>
    <gaf-table-layout v-show="!open">
      <template #filter>
        <gaf-search
          v-show="false"
          @search="keyword => handleFilterChange(keyword)"
          placeholder="请输入API名称查询"
          style="width:200px;"
        />
        <button v-show="false">重置</button>
      </template>
      <template #default>
        <gaf-table-with-page
          :pagination="pagination"
          :data-source="authResourceApiList"
          :loading="loading"
          @change="tableChange"
          :row-selection="{
            selectedRowKeys: selectedRowKeys,
            onChange: onSelectChange
          }"
          :row-key="(r, i) => r.resourceApiId"
          :columns="columns.filter(item => item.dataIndex !== 'resourceApiId')"
          style="width: 100%;"
          bordered
          size="small"
          align="center"
        >
          <template slot="method" slot-scope="text, record">
            {{ getMethod(record.method) }}
          </template>
          <template slot="type" slot-scope="text, record">
            {{ record.type === '1' ? '系统资源' : '第三方资源' }}
          </template>
        </gaf-table-with-page>
      </template>
    </gaf-table-layout>
  </div>
</template>

<script>
export default {
  props: {
    apiGrpId: {
      type: String,
      default: ''
    },
    roleId: {
      type: String,
      default: ''
    },
    nodeType: {
      type: Number,
      default: 12
    }
  },
  data() {
    return {
      // 搜索项
      searchKey: '',
      clearFilters: null,
      // 非多个禁用
      multiple: true,
      // 标题
      title: '',
      // 编辑记录
      editData: {},
      // 总条数
      total: 0,
      selectedRowKeys: [],
      // ${functionName}表格数据
      authResourceApiList: [],
      // 是否显示添加修改弹出层
      open: false,
      // 分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0
      },
      // 列表是否加载中
      loading: true,
      searchText: '',
      searchInput: null,
      searchedColumn: '',
      sorter: {
        order: '',
        field: ''
      },
      // 详情：1，新增：2，编辑：3
      operation: 0,
      // 有无主键
      hasPKField: true
      // realRoleId: ''
    }
  },
  computed: {
    realRoleId: function() {
      if (this.nodeType === 5) {
        return this.roleId
      }
      return ''
    },
    columns: function() {
      const columns = [
        {
          title: '名称',
          dataIndex: 'name',
          key: 'name'
        },
        {
          title: '路由路径',
          dataIndex: 'routeUrl',
          key: 'route_url'
        },
        {
          title: '方法',
          dataIndex: 'method',
          key: 'method',
          scopedSlots: { customRender: 'method' }
        },
        {
          title: '类型',
          dataIndex: 'type',
          key: 'type',
          scopedSlots: { customRender: 'type' }
        },
        {
          title: '排序序号',
          dataIndex: 'sortSn',
          key: 'sort_sn'
        }
      ]
      return this.hasPKField ? columns : columns.slice(0, columns.length - 2)
    },
    timeFormat: function() {
      if (
        this.columns.filter(
          item =>
            item.scopedSlots && item.scopedSlots.customRender === 'timeRender'
        ).length > 0
      ) {
        return function(str) {
          if (!str || str === '') {
            return ''
          }
          const dt = new Date(str)
          const year = dt.getFullYear()
          let month = dt.getMonth() + 1
          let date = dt.getDate()
          let hour = dt.getHours()
          let minute = dt.getMinutes()
          let second = dt.getSeconds()

          month = month < 10 ? '0' + month : month
          date = date < 10 ? '0' + date : date
          hour = hour < 10 ? '0' + hour : hour
          minute = minute < 10 ? '0' + minute : minute
          second = second < 10 ? '0' + second : second

          return `${year}/${month}/${date} ${hour}:${minute}:${second}`
        }
      }
      return null
    }
  },
  watch: {
    apiGrpId(val) {
      this.pagination.current = 1
      this.searchText = val
      this.getList(val)
    },
    realRoleId(val) {
      if (val !== '') {
        this.roleId = val
        this.getSelected()
      }
    }
  },
  created() {
    this.searchedColumn = 'api_catalog_id'
    this.searchText = this.apiGrpId
    this.getList()
    if (this.roleId && this.roleId !== '') {
      this.getSelected()
    }
  },
  methods: {
    async getSelected() {
      const url = `/authority/auth-role-api/list-by-role/${this.roleId}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.selectedRowKeys = res.data.map(item => item.resourceApiId)
      }
    },
    getMethod(val) {
      switch (val) {
        case '1':
          return 'GET'
        case '2':
          return 'POST'
        case '3':
          return 'PUT'
        case '4':
          return 'DELETE'
      }
    },
    // handleSearchFieldChange(value) {
    //   this.searchedColumn = value
    // },
    async handleFilterChange(value) {
      this.searchText = value
      this.pagination.current = 1
      await this.getList()
    },
    // 搜索查询
    // handleSearch(selectedKeys, confirm, key, clearFilters) {
    //   if (this.searchedColumn !== key && this.clearFilters) this.clearFilters()
    //   confirm()
    //   this.searchText = selectedKeys[0]
    //   this.searchedColumn = key
    //   this.clearFilters = clearFilters
    // },
    // 重置查询
    handleReset(clearFilters, key) {
      clearFilters()
      if (this.searchedColumn === key) {
        this.searchText = ''
        this.searchedColumn = ''
        this.clearFilters = null
      }
    },
    // 页码，排序项发生改变时，重新获取列表数据
    tableChange(pageInfo, filters, sorter) {
      if (pageInfo) {
        this.pagination.current = pageInfo.current
        this.pagination.pageSize = pageInfo.pageSize
      }
      if (sorter) {
        this.sorter.order = sorter.order === 'descend' ? 'DESC' : 'ASC'
        this.sorter.field = sorter.columnKey
      }
      this.getList()
    },
    // 添加数据
    async handleAdd() {
      const url = `/authority/auth-role-api/handle`
      const data = {
        roleId: this.roleId,
        apiList: this.selectedRowKeys
      }
      const res = await this.$axios.$post(url, data)
      if (res.isSuccessed) {
        this.$message.success('添加成功')
      } else {
        this.$message.error(`添加失败,原因:${res.message}`)
      }
    },
    // 添加修改提交后
    handleSubmit() {
      this.open = false
      this.editData = {}
      this.getList()
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
      if (this.selectedRowKeys.length > 0) {
        this.multiple = false
      } else {
        this.multiple = true
      }
    },
    async getList() {
      this.loading = true
      let url = `/authority/auth-resource-apis?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`
      if (this.searchText.trim() && this.searchedColumn) {
        url =
          url +
          '&searchFieldName=' +
          this.searchedColumn +
          '&searchFieldValue=' +
          this.searchText.trim()
      }
      if (this.sorter.order && this.sorter.field) {
        url =
          url +
          '&orderFieldName=' +
          this.sorter.field +
          '&orderMethod=' +
          this.sorter.order
      }
      const res = await this.$axios.$get(url)
      this.loading = false
      if (res.isSuccessed) {
        this.authResourceApiList = res.data.pageList
        this.pagination.total = res.data.totalCount
      } else {
        this.$message.error(`查询失败,原因:${res.message}`)
      }
    }
  }
}
</script>

<style scoped>
.app-container {
  height: 100%;
}
</style>
