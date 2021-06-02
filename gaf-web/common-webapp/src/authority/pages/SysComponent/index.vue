<template>
  <div class="app-container">
    <div class="page-single">
    <gaf-table-layout v-show="!open">
      <template #actions>
        <button @click="handleAdd" class="btn-fun blue">
          <span><a-icon type="plus" />
          新增</span>
        </button>
        <a-popconfirm
          class="btn-fun red"
          title="删除后无法恢复，确认是否继续?"
          ok-text="确认"
          cancel-text="取消"
          @confirm="() => batchDel()"
        >
          <button class="btn-fun red">
            <a-icon type="delete" />
            <span>批量删除</span>
          </button>
        </a-popconfirm>
      </template>
      <template #filter>
        <div class="search-position">
          <a-input-search
            @search="onSearch"
            placeholder="请输入名称查询"
            size="large"
          >
            <button slot="enterButton" class="btn-search">
              搜索
            </button>
          </a-input-search>
        </div>
      </template>
      <template #default>
        <gaf-table-with-page
          :pagination="pagination"
          :row-selection="{
            selectedRowKeys: selectedRowKeys,
            onChange: onSelectChange,
            onSelect: rowSelect,
            onSelectAll: rowSelectAll
          }"
          :data-source="sysComponentList"
          :loading="loading"
          @change="tableChange"
          :row-key="(r, i) => r.sysComponentId"
          :columns="columns.filter(item => item.dataIndex !== 'sysComponentId')"
          style="width: 100%;"
          bordered
          size="small"
          align="center"
        >
          <template slot="type" slot-scope="text, record">{{
            getType(record.type)
          }}</template>
          <template slot="status" slot-scope="text, record">{{
            record.status === true ? '可见' : '不可见'
          }}</template>
          <template
            slot="operation"
            slot-scope="text, record"
            v-if="hasPKField"
          >
            <a @click.stop="() => handleDetail(record)" href="javascript:;" class="btn-view">
              <a-icon type="profile" /> 详情
            </a>
            <a-divider type="vertical" />
            <a @click.stop="() => handleUpdate(record)" href="javascript:;" class="btn-edit">
              <a-icon type="edit" /> 编辑
            </a>
            <a-divider type="vertical" />
            <a-popconfirm
              @confirm="() => handleDelete(record)"
              title="删除后无法恢复，确认是否继续?"
              ok-text="确认"
              cancel-text="取消"
            >
              <a href="javascript:;" class="btn-del"><a-icon type="delete" /> 删除</a>
            </a-popconfirm>
          </template>

          <template slot="timeRender" v-if="timeFormat" slot-scope="text">
            {{ timeFormat(text) }}
          </template>
        </gaf-table-with-page>
      </template>
    </gaf-table-layout>
    <add-edit-form
      :title="title"
      :editData="editData"
      @submit="handleSubmit"
      @back="handleBack"
      v-if="open"
      :operation="operation"
    >
    </add-edit-form>
  </div>
  </div>
</template>

<script>
import AddEditForm from '../../views/SysComponent/AddOrEditForm'
import '~/assets/css/common.css'

export default {
  components: {
    AddEditForm
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
      sysComponentList: [],
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
      searchedColumn: 'name',
      sorter: {
        order: '',
        field: ''
      },
      // 详情：1，新增：2，编辑：3
      operation: 0,
      // 有无主键
      hasPKField: true
    }
  },
  computed: {
    columns: function() {
      const columns = [
        {
          title: '系统组件id',
          dataIndex: 'sysComponentId',
          key: 'sys_component_id'
        },
        {
          title: '名称',
          dataIndex: 'name',
          key: 'name'
        },
        {
          title: '别名',
          dataIndex: 'nameCn',
          key: 'name_cn'
        },
        // {
        //   title: '编码',
        //   dataIndex: 'code',
        //   key: 'code'
        // },
        {
          title: '类型',
          dataIndex: 'type',
          key: 'type',
          scopedSlots: { customRender: 'type' }
        },
        // {
        //   title: '可见性',
        //   dataIndex: 'status',
        //   key: 'status',
        //   scopedSlots: { customRender: 'status' }
        // },
        {
          title: '描述',
          dataIndex: 'description',
          key: 'description'
        },
        {
          title: '操作',
          scopedSlots: { customRender: 'operation' }
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
  created() {
    this.getList()
  },
  methods: {
    async onSearch(val) {
      console.log(val)
      this.searchText = val
      this.pagination.current = 1
      await this.getList()
    },
    async batchDel() {
      const url = `/authority/${'sys_component'.replace('_', '-')}s/`
      const selectedRowKeys = this.selectedRowKeys
      console.log(selectedRowKeys)
      if (selectedRowKeys.length !== 0) {
        const rst = await this.$axios.delete(url, { data: selectedRowKeys })
        if (rst.data.isSuccessed) {
          this.$message.success('删除成功')
         
          console.log(this.selectedRowKeys)
        } else {
          this.$message.error(`删除失败,原因:${rst.data.message}`)
        }
        this.$nextTick(() => {
          if (this.pagination.current !== 1 && selectedRowKeys.length === this.sysComponentList.length){
            this.pagination.current--
          }
          this.getList()
        })
      } else {
        this.$message.warn('请选择您要删除的内容')
      }
    },
    rowSelect(record, selected, selectedRows) {
      console.log(record, selected, selectedRows)
    },
    rowSelectAll(selected, selectedRows, changeRows) {
      console.log(selected, selectedRows, changeRows)
    },
    getType(type) {
      switch (type) {
        case '1':
          return 'web前端'
        case '2':
          return '后端'
        case '3':
          return '移动端'
        case '4':
          return 'web前后端'
      }
    },
    handleSearchFieldChange(value) {
      this.searchedColumn = value
    },
    async handleFilterChange(value) {
      this.searchText = value
      this.pagination.current = 1
      await this.getList()
    },
    // 搜索查询
    handleSearch(selectedKeys, confirm, key, clearFilters) {
      if (this.searchedColumn !== key && this.clearFilters) this.clearFilters()
      confirm()
      this.searchText = selectedKeys[0]
      this.searchedColumn = key
      this.clearFilters = clearFilters
    },
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
    handleAdd() {
      this.operation = 2
      this.open = true
      this.title = '添加组件'
    },
    // 添加修改提交后
    handleSubmit() {
      this.open = false
      this.editData = {}
      this.getList()
    },
    // 添加修改返回后
    handleBack() {
      this.editData = {}
      this.open = false
    },
    // 修改数据
    handleUpdate(row) {
      this.operation = 3
      this.open = true
      this.title = '修改组件'
      this.editData = row
    },
    handleDetail(row) {
      this.operation = 1
      this.open = true
      this.title = '详情展示'
      this.editData = row
    },
    // 删除数据
    async handleDelete(row) {
      const url =
        `/authority/${'sys_component'.replace('_', '-')}s/` + row.sysComponentId
      const rst = await this.$axios.delete(url)
      if (rst.data.isSuccessed) {
        this.$message.success('删除成功')
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`)
      }
      this.$nextTick(() => {
        if(this.pagination.current !== 1 && this.sysComponentList.length === 1){
          this.pagination.current--
        }
        this.getList()
      })
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
      this.selectedRowKeys=[]
      this.loading = true
      let url = `/authority/sys-components?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`
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
        this.sysComponentList = res.data.pageList
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
