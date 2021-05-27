<template>
  <div class="app-container">
    <div class="page-single">
      <gaf-table-layout>
        <template #actions>
          <button class="btn-fun blue" @click="handleAdd">
            <a-icon type="plus" />
            <span>新增</span>
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
          <div style="margin-top: 5px">
            <a-select
              v-model="type"
              size="large"
              style="
                width: 150px;
                box-shadow: 3px 3px 0 rgba(128, 128, 128, 0.1);
              "
            >
              <a-select-option :value="''"> 全部 </a-select-option>
              <a-select-option :value="'1'"> 基础类 </a-select-option>
              <a-select-option :value="'2'"> 业务类 </a-select-option>
            </a-select>
            <a-input-search
              placeholder="请输入工具条名称"
              size="large"
              style="width: 320px"
              @search="onSearch"
            >
              <button slot="enterButton" class="btn-search"> 搜索 </button>
            </a-input-search>
          </div>
        </template>
        <template #default>
          <gaf-table-with-page
            :pagination="pagination"
            :data-source="webgisToolbarList"
            :loading="loading"
            :row-selection="{
              selectedRowKeys: selectedRowKeys,
              onChange: onSelectChange,
            }"
            :row-key="(r) => r.toolbarId"
            :columns="columns"
            style="width: 100%; height: 570px; overflow: auto"
            bordered
            size="small"
            align="center"
            @change="tableChange"
          >
            <template slot="type" slot-scope="text, record">
              <div v-if="record.type === '1'">基础类</div>
              <div v-else-if="record.type === '2'">业务类</div>
            </template>
            <template
              v-if="hasPKField"
              slot="operation"
              slot-scope="text, record"
            >
              <a
                class="btn-edit"
                href="javascript:;"
                @click.stop="() => handleUpdate(record)"
              >
                <a-icon type="edit" /> 编辑
              </a>
              <a-divider type="vertical" />
              <a-popconfirm
                class="btn-del"
                title="删除后无法恢复，确认是否继续?"
                ok-text="确认"
                cancel-text="取消"
                @confirm="() => handleDelete(record)"
              >
                <a href="javascript:;"><a-icon type="delete" /> 删除</a>
              </a-popconfirm>
            </template>
            <template v-if="timeFormat" slot="timeRender" slot-scope="text">
              {{ timeFormat(text) }}
            </template>
          </gaf-table-with-page>
        </template>
      </gaf-table-layout>
      <a-modal
        v-model="open"
        :mask="false"
        :width="800"
        :footer="null"
        :centered="true"
        destroy-on-close
        @cancel="handleBack"
      >
        <add-edit-form
          :title="title"
          :btn-list="btnList"
          :edit-data="editData"
          :mock-data="mockData"
          :operation="operation"
          @submit="handleSubmit"
          @back="handleBack"
        >
        </add-edit-form>
      </a-modal>
    </div>
  </div>
</template>

<script>
// @ts-nocheck
import '~/assets/css/common.css'
import AddEditForm from '../../views/WebgisToolbar/AddOrEditForm.vue'

export default {
  components: {
    AddEditForm,
  },
  data() {
    return {
      type: '',
      // 搜索项
      searchKey: '',
      clearFilters: null,
      // 非多个禁用
      multiple: true,
      // 标题
      title: '',
      // 编辑记录
      editData: {},
      // 地图 全部按钮
      btnList: [],
      // 转换框
      mockData: [],
      // 总条数
      total: 0,
      selectedRowKeys: [],
      // ${functionName}表格数据
      webgisToolbarList: [],
      // 是否显示添加修改弹出层
      open: false,
      // 分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0,
      },
      // 列表是否加载中
      loading: true,
      searchText: '',
      searchInput: null,
      searchedColumn: 'name',
      sorter: {
        order: '',
        field: '',
      },
      // 详情：1，新增：2，编辑：3
      operation: 0,
      // 有无主键
      hasPKField: true,
    }
  },
  computed: {
    columns() {
      const columns = [
        {
          title: '工具条名称',
          dataIndex: 'name',
          key: 'name',
        },
        {
          title: '工具条类型',
          scopedSlots: {
            customRender: 'type',
          },
          dataIndex: 'type',
          key: 'type',
        },
        {
          title: '修改时间',
          dataIndex: 'createdTime',
          key: 'created_time',
          scopedSlots: { customRender: 'timeRender' },
        },
        {
          title: '工具条ID',
          dataIndex: 'toolbarId',
          key: 'toolbar_id',
        },
        {
          title: '操作',
          scopedSlots: { customRender: 'operation' },
        },
      ]
      return this.hasPKField ? columns : columns.slice(0, columns.length - 2)
    },
    timeFormat() {
      if (
        this.columns.filter(
          (item) =>
            item.scopedSlots && item.scopedSlots.customRender === 'timeRender'
        ).length > 0
      ) {
        return function (str) {
          if (!str || str === '') {
            return ''
          }
          const dt = new Date(str)
          const year = dt.getFullYear()
          const month = dt.getMonth() + 1
          const date = dt.getDate()
          let hour = dt.getHours()
          let minute = dt.getMinutes()
          let second = dt.getSeconds()

          hour = hour < 10 ? '0' + hour : hour
          minute = minute < 10 ? '0' + minute : minute
          second = second < 10 ? '0' + second : second

          return `${year}-${month}-${date} ${hour}:${minute}:${second}`
        }
      }
      return null
    },
  },
  created() {
    this.getList()
  },
  methods: {
    handleSearchFieldChange(value) {
      this.searchedColumn = value
    },
    async onSearch(value) {
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
    async handleAdd() {
      this.operation = 2
      this.open = true
      this.title = '添加工具条'
      const url = `/map/webgis-buttons/`
      const res = await this.$axios.get(url)
      this.btnList = res.data.data.pageList
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
      this.title = '修改工具条'
      this.editData = row
    },
    // 删除数据
    async handleDelete(row) {
      const url = `/map/webgis-toolbars/` + row.toolbarId
      const rst = await this.$axios.delete(url)
      if (rst.data.isSuccessed) {
        this.$message.success('删除成功')
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`)
      }
      this.$nextTick(() => {
        if (this.pagination.current !== 1 && this.webgisToolbarList.length === 1){
          this.pagination.current--
        }
        this.getList()
      })
    },
    // 批量删除
    async batchDel() {
      const url = '/map/webgis-toolbars/'
      const selectedRowKeys = this.selectedRowKeys
      if (selectedRowKeys.length !== 0) {
        const rst = await this.$axios.delete(url, { data: selectedRowKeys })
        if (rst.data.isSuccessed) {
          this.$message.success('删除成功')
        } else {
          this.$message.error(`删除失败,原因:${rst.data.message}`)
        }
        this.$nextTick(() => {
          if (this.pagination.current !== 1 && selectedRowKeys.length === this.webgisToolbarList.length){
            this.pagination.current--
          }
          this.getList()
          this.selectedRowKeys = []
        })
      } else {
        this.$message.warn('请选择您要删除的内容')
      }
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
      let url = `/map/webgis-toolbars?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`
      if (this.type !== '') {
        url = url + `&type=` + this.type
      }
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
        this.pagination.current = res.data.pageNum
        this.pagination.pageSize = res.data.pageSize
        this.pagination.total = res.data.totalCount
        this.webgisToolbarList = res.data.pageList
      } else {
        this.$message.error(`查询失败,原因:${res.message}`)
      }
    },
  },
}
</script>

<style scoped>
.ant-table-small {
  height: 100%;
}
</style>
