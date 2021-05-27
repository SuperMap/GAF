<template>
  <div class="app-container">
    <div class="page-single">
    <gaf-table-layout>
      <template #actions>
        <button @click="handleAdd" class="btn-fun blue" icon="plus" visible="true">
          <a-icon type="plus" />新增
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
        <a-cascader
          placeholder="请选择类型"
          @change="handleSearchFieldChange"
          size="large"
          :options="option"
          :display-render="displayRender"
          style="width:150px;  box-shadow: 3px 3px 0 rgba(128, 128, 128, 0.1); text-align: left"
        >
        </a-cascader>
        <a-input-search
          @search="onSearch"
          placeholder="请输入工作空间名称查询"
          style="width: 320px"
          size="large"
        >
          <a-button slot="enterButton" class="btn-search">
            搜索
          </a-button>
        </a-input-search>
      </template>
      <template #default>
        <gaf-table-with-page
          :pagination="pagination"
          :data-source="dataWorkspaceList"
          :loading="loading"
          @change="tableChange"
          :row-selection="{
            selectedRowKeys: selectedRowKeys,
            onChange: rowChange,
            onSelect: rowSelect,
            onSelectAll: rowSelectAll
          }"
          :row-key="r => r.workspaceId"
          :columns="columns.filter(item => item.dataIndex !== 'workspaceId')"
          style="width: 100%;"
          bordered
          size="small"
          align="center"
        >
          <template slot="typeCode" slot-scope="text, record">
            {{ map.get(record.typeCode) ? map.get(record.typeCode) : '' }}
          </template>
          <!-- <template slot="published" slot-scope="text, record">
            {{ record.published ? '已发布' : '未发布' }}
          </template> -->
          <template
            slot="operation"
            slot-scope="text, record"
            v-if="hasPKField"
          >
            <a @click.stop="() => handleUpdate(record)" class="btn-edit" href="javascript:;">
              <a-icon type="edit" /> 编辑
            </a>
            <a-divider type="vertical" />
            <a-popconfirm
              @confirm="() => handleDelete(record)"
              class="btn-del"
              title="删除后无法恢复，确认是否继续?"
              ok-text="确认"
              cancel-text="取消"
            >
              <a href="javascript:;"><a-icon type="delete" /> 删除</a>
            </a-popconfirm>
            <a-divider type="vertical" />
            <a @click.stop="() => handleDownload(record)"  class="btn-view" href="javascript:;">
              <a-icon type="cloud-download" /> 下载
            </a>
            <!-- <a-divider type="vertical" />
            <a @click.stop="() => handlePublish(record)" class="btn-release" href="javascript:;" style="color: rgb(45, 140, 240);">
              <a-icon type="cloud-upload" /> 发布
            </a> -->
          </template>

          <template v-if="timeFormat" slot="timeRender" slot-scope="text">
            {{ timeFormat(text) }}
          </template>
        </gaf-table-with-page>
      </template>
    </gaf-table-layout>
	<a-modal
      v-model="open"
      :width="1000"
      :footer="null"
      :centered="true"
      @cancel="handleBack"
      destroy-on-close
    >
     <add-edit-form
       :title="title"
       :editData="editData"
       @submit="handleSubmit"
       @back="handleBack"
       :operation="operation"
       :option="option"
     >
     </add-edit-form>
    </a-modal>
    <!-- <a-modal
      title="发布服务"
      v-model="openPublish"
      :width="600"
      :centered="true"
      @cancel="handlePublishBack"
      @ok="handleOk"
      destroy-on-close
    >
     <a-select mode="tags" style="width: 100%" placeholder="请选择一个或多个类型" v-model="serviceTypes" :options="dataOption" @change="handleChange">
     </a-select>
    </a-modal> -->
  </div>
  </div>
</template>

<script>
    import AddEditForm from '../../views/DataWorkspace/AddOrEditForm'
    import {gafDownloadUtil} from 'gaf-ui'

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
      dataWorkspaceList: [],
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
      searchedColumn: 'ws_name',
      searchText2: '',
      searchedColumn2: '',
      sorter: {
        order: '',
        field: ''
      },
      // 详情：1，新增：2，编辑：3
      operation: 0,
      // 有无主键
      hasPKField: true,
      //级联数据
      option: [],
      map: new Map(),
      openPublish: false,
      workspaceId: '',
      serviceTypes: [],
      dataOption: [{value:'RESTMAP',label:'地图服务'},{value:'RESTDATA',label:'数据服务'},{value:'RESTREALSPACE',label:'三维服务'},{value:'RESTSPATIALANALYST',label:'空间分析服务'}]
    }
  },
  computed: {
    //表格
    columns: function() {
      const columns = [
        {
          title: '工作空间id',
          dataIndex: 'workspaceId',
          key: 'workspace_id'
        },
        {
          title: '工作空间名称',
          dataIndex: 'wsName',
          key: 'ws_name'
        },
        {
          title: '工作空间类型',
          dataIndex: 'typeCode',
          key: 'type_code',
          scopedSlots: { customRender: 'typeCode' }
        },
        {
          title: '地址',
          dataIndex: 'server',
          key: 'server'
        },
        // {
        //   title: '发布状态',
        //   dataIndex: 'published',
        //   key: 'published',
        //   scopedSlots: { customRender: 'published' }
        // },
        // {
        //   title: '状态',
        //   dataIndex: 'status',
        //   key: 'status'
        // },
        // {
        //   title: '创建时间',
        //   scopedSlots: {
        //     customRender: 'timeRender'
        //   },
        //   dataIndex: 'createdTime',
        //   key: 'created_time'
        // },
        // {
        //   title: '创建人',
        //   dataIndex: 'createdBy',
        //   key: 'created_by'
        // },
        // {
        //   title: '修改时间',
        //   scopedSlots: {
        //     customRender: 'timeRender'
        //   },
        //   dataIndex: 'updatedTime',
        //   key: 'updated_time'
        // },
        // {
        //   title: '修改人',
        //   dataIndex: 'updatedBy',
        //   key: 'updated_by'
        // },
        {
          title: '操作',
          scopedSlots: { customRender: 'operation' }
        }
      ]
      return this.hasPKField ? columns : columns.slice(0, columns.length - 2)
    },
    //时间格式
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
    this.getOptions()
  },
  methods: {
    //searchText2随级联选择改变
    handleSearchFieldChange(value) {
      this.searchedColumn2 = "typeCode"
      this.searchText2 = value.length > 0 ? value[value.length - 1] : ''
    },
    /* async handleFilterChange(value) {
      this.searchText = value
      this.pagination.current = 1
      await this.getList()
    }, */
    // 搜索查询
    async onSearch(val) {
      this.searchText = val
      this.pagination.current = 1
      await this.getList()
    },
    /* handleSearch(selectedKeys, confirm, key, clearFilters) {
      if (this.searchedColumn !== key && this.clearFilters) this.clearFilters()
      confirm()
      this.searchText = selectedKeys[0]
      this.searchedColumn = key
      this.clearFilters = clearFilters
    }, */
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
      this.title = '添加工作空间'
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
      this.title = '修改工作空间'
      this.editData = row
    },
    //详情展示
    handleDetail(row) {
      this.operation = 1
      this.open = true
      this.title = '详情展示'
      this.editData = row
    },
    handleChange(value){
      console.log(value,this.serviceTypes,'value')

    },
    async handleOk() {
      const url = `/data-mgt/data-workspaces/publish-service`
      const data = {}
      data.workspaceId = this.workspaceId
      data.serviceTypes = this.serviceTypes
      const rst = await this.$axios.post(url,[data])
      if (rst.data.isSuccessed) {
        if (rst.data.data[0].isSuccessed) {
          this.$message.success('发布成功')
          this.getList()
        } else {
          this.$message.error(`发布失败,原因:${rst.data.data[0].message}`)
        }
        this.openPublish = false

      } else {
        this.$message.error(`发布失败,原因:${rst.data.message}`)
        this.openPublish = false
      }
      this.serviceTypes = []
    },
    handlePublishBack() {
      this.serviceTypes = []
      this.openPublish = false
    },
    handlePublish(row) {
      this.workspaceId = row.workspaceId
      this.openPublish = true
      // const data.workspaceType = row.
      // const url = `/data-mgt/services/publish-service`
      // const rst = await this.$axios.post(url,data)
      // console.log(rst)
      // if (rst.data.isSuccessed) {
      //   this.$message.success('删除成功')
      // } else {
      //   this.$message.error(`删除失败,原因:${rst.data.message}`)
      // }
      // this.$nextTick(() => {
      //   if (this.pagination.current !== 1 && this.dataWorkspaceList.length === 1){
      //     this.pagination.current--
      //   }
      //   this.getList()
      // })
    },
    //下载
    handleDownload(row) {
      let isFileType = false
      this.option.forEach(item => {
        if (item.value === "file") {
          item.children.forEach(itemChildren => {
            if(itemChildren.value === row.typeCode){
              gafDownloadUtil(this.$axios,'/storage/file-storage/',row.server)
              isFileType = true
              return
            }
          })

        }
      })
      if (!isFileType) {
        this.$message.info('不是文件类型，不能下载')
      }
    },
    // 删除数据
    async handleDelete(row) {
      const url = `/data-mgt/data-workspaces/` + row.workspaceId
      const rst = await this.$axios.delete(url)
      if (rst.data.isSuccessed) {
        this.$message.success('删除成功')
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`)
      }
      this.$nextTick(() => {
        if (this.pagination.current !== 1 && this.dataWorkspaceList.length === 1){
          this.pagination.current--
        }
        this.getList()
      })
    },
    // 批量删除
     async batchDel() {
      const url = `/data-mgt/data-workspaces/`
      const selectedRowKeys = this.selectedRowKeys
      if (selectedRowKeys.length !== 0) {
        const rst = await this.$axios.delete(url, { data: selectedRowKeys })
        if (rst.data.isSuccessed) {
          this.$message.success('删除成功')
        } else {
          this.$message.error(`删除失败,原因:${rst.data.message}`)
        }
        this.$nextTick(() => {
          if (this.pagination.current !== 1 && selectedRowKeys.length === this.dataWorkspaceList.length){
            this.pagination.current--
          }
          this.getList()
          this.selectedRowKeys = []
        })
      } else {
        this.$message.warn('请选择您要删除的内容')
      }
    },
    // 复选框
    rowChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
      if (this.selectedRowKeys.length > 0) {
        this.multiple = false
      } else {
        this.multiple = true
      }
    },
    rowSelect(record, selected, selectedRows) {
      console.log(record, selected, selectedRows)
    },
    rowSelectAll(selected, selectedRows, changeRows) {
      console.log(selected, selectedRows, changeRows)
    },
    //获取表格数据
    async getList() {
      this.loading = true
      let url = `/data-mgt/data-workspaces?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`
      if (this.searchText.trim() && this.searchedColumn) {
        url =
          url +
          '&searchFieldName=' +
          this.searchedColumn +
          '&searchFieldValue=' +
          this.searchText.trim()
      }
      if (this.searchText2.trim() && this.searchedColumn2) {
        url =
          url +
          '&' +
          this.searchedColumn2 + '=' +
          this.searchText2.trim()
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
        this.dataWorkspaceList = res.data.pageList
        this.dataWorkspaceList.forEach(item => {
          if (!item.wsName) {
            item.wsName = item.server.split('/')[item.server.split('/').length - 1]
          }
        })
      } else {
        this.$message.error(`查询失败,原因:${res.message}`)
      }
    },
    //获取工作空间类型的树形数据
    async getOptions() {
      const url = `/sys-mgt/sys-dicts/WorkSpaceType/tree`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.option = res.data
        this.getMap(res.data)
      } else {
        this.$message.error('加载数据失败,原因：' + res.message)
      }
    },
    //级联选择器值渲染最后一级数据
    displayRender({ labels }) {
      return labels[labels.length - 1];
    },
    getMap(data) {
      data.forEach((element) => {
        this.map.set(element.value, element.label)
        if (element.children){
          this.getMap(element.children)
        }
      })
    },
  }
}
</script>

<style scoped>
.app-container {
  height: 100%;
}
</style>
