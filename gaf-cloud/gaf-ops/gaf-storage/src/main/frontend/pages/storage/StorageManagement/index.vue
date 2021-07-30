<template>
  <div class="app-container">
    <div class="page-single">
      <div class="breadcrumb">
        <a-breadcrumb :routes="routes">
          <template
            slot="itemRender"
            slot-scope="{ route, params, routes, paths }"
          >
            <span v-if="routes.indexOf(route) === routes.length - 1">
              {{ route.breadcrumbName }}
            </span>
            <a
              v-else
              href="javascript:;"
              @click.stop="() => spanClick(route, params, routes, paths)"
            >
              {{ route.breadcrumbName }}
            </a>
          </template>
        </a-breadcrumb>
        <span class="logout"
          ><a-dropdown :trigger="['click']">
            <form id="logoutForm" method="post" action="/logout">
              <a class="ant-dropdown-link" @click.stop="() => logout()">
                退出 <a-icon type="logout" />
              </a>
            </form>
            <!-- <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:;">个人中心</a>
              </a-menu-item>
              <a-menu-item>
                <a href="javascript:;" @click.stop="() => logout()">退出</a>
              </a-menu-item>
            </a-menu> -->
          </a-dropdown></span
        >
      </div>
      <div v-show="isShow">
        <gaf-table-layout>
          <template #actions>
            <button class="btn-fun blue btn-16" @click="handleAdd">
              <a-icon type="plus-circle" /><span>新增配置</span>
            </button>
            <a-popconfirm
              class="btn-fun blue"
              title="删除后无法恢复，确认是否继续?"
              ok-text="确认"
              cancel-text="取消"
              @confirm="() => batchDel()"
            >
              <button class="btn-fun blue">
                <span>批量删除</span>
              </button>
            </a-popconfirm>
          </template>
          <template #filter>
            <div class="box">
              <div class="search-position">
                <a-input-search
                  v-if="searchedColumn !== 'type'"
                  placeholder="请输入名称查询"
                  size="large"
                  @search="onSearch"
                >
                </a-input-search>
              </div>
            </div>
          </template>
          <template #default>
            <div class="choose-box">
              <a-icon type="exclamation-circle" class="exclamation" /><span
                >已选择</span
              >
              <b>{{ selectRowLength }}</b>
              <span>项</span>
              <a-popconfirm
                title="清空后无法恢复，确认是否继续?"
                ok-text="确认"
                cancel-text="取消"
                @confirm="() => clearOptions(record)"
              >
                <a href="javascript:;"><u>清空</u></a>
              </a-popconfirm>
            </div>
            <gaf-table-with-page
              :scroll="{ y: 508, x: 1440 }"
              :pagination="pagination"
              :data-source="configurationList"
              :loading="loading"
              :row-selection="{
                selectedRowKeys: selectedRowKeys,
                onChange: onSelectChange,
              }"
              :row-key="(r) => r.id"
              :columns="columns.filter((item) => item.dataIndex !== 'id')"
              class="table-style"
              size="middle"
              @change="tableChange"
            >
              <template slot="customRender" slot-scope="text, record">
                <a
                  href="javascript:;"
                  class="btn-margin"
                  @click.stop="() => handleConfig(record)"
                  ><u>{{ text }}</u>
                </a>
              </template>
              <template
                v-if="hasPKField"
                slot="operation"
                slot-scope="text, record"
              >
                <a
                  href="javascript:;"
                  class="btn-margin"
                  @click.stop="() => handleUpdate(record)"
                  ><u>编辑</u>
                </a>

                <a-popconfirm
                  title="删除后无法恢复，确认是否继续?"
                  ok-text="确认"
                  cancel-text="取消"
                  @confirm="() => handleDelete(record)"
                >
                  <a href="javascript:;"><u>删除</u></a>
                </a-popconfirm>
              </template>
              <template v-if="timeFormat" slot="timeRender" slot-scope="text">
                {{ timeFormat(text) }}
              </template>
            </gaf-table-with-page>
          </template>
        </gaf-table-layout>
        <gaf-drawer
          :visible="open"
          :width="500"
          :footer="null"
          :centered="true"
          destroy-on-close
          placement="right"
          :closable="false"
          @close="handleBack"
        >
          <add-edit-form
            :title="title"
            :edit-data="editData"
            :operation="operation"
            @submit="handleSubmit"
            @back="handleBack"
          >
          </add-edit-form>
        </gaf-drawer>
      </div>
      <div v-if="!isShow">
        <attachment-management
          ref="AttachmentManagement"
          :config-name="configName"
          @addbreadcrumb="addbreadcrumb"
          @popRoutes="popRoutes"
        >
        </attachment-management>
      </div>
    </div>
  </div>
</template>

<script>
import '~/assets/css/common.css'
import AddEditForm from '../../../views/storage/StorageManagement/AddOrEditForm.vue'
import AttachmentManagement from '../AttachmentManagement/index.vue'
export default {
  components: {
    AddEditForm,
    AttachmentManagement,
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
      configurationList: [],
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
      buttonType: '1',
      selectRowLength: 0,
      // basePath: '/components/breadcrumb',
      routes: [
        {
          path: 'StorageManagement',
          breadcrumbName: '配置管理',
        },
      ],
      isShow: true,
      configName: null,
    }
  },
  computed: {
    columns() {
      const columns = [
        {
          title: '名称',
          width: '18%',
          scopedSlots: {
            customRender: 'customRender',
          },
          dataIndex: 'name',
          key: 'name',
        },
        {
          title: '地址',
          width: '18%',
          dataIndex: 'serviceEndpoint',
          key: 'serviceEndpoint',
        },
        {
          title: 'bucket名称',
          width: '22%',
          dataIndex: 'bucketName',
          key: 'bucketName',
        },
        {
          title: '修改时间',
          width: '18%',
          dataIndex: 'updatedTime',
          key: 'updatedTime',
          scopedSlots: {
            customRender: 'timeRender',
          },
        },
        {
          title: '操作',
          // fixed: 'right',
          scopedSlots: { customRender: 'operation' },
        },
      ]
      return this.hasPKField ? columns : columns.slice(0, columns.length - 2)
    },
    timeFormat() {
      if (
        // @ts-ignore
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
    },
  },
  created() {
    this.getList()
  },
  methods: {
    spanClick(route, params, routes) {
      routes.forEach((element, index) => {
        if (
          element.path === route.path &&
          element.breadcrumbName === route.breadcrumbName
        ) {
          // index = 0  true / index > 0 false / index > 1 getlist()
          if (index === 0) {
            this.isShow = true
          } else if (index > 0) {
            this.isShow = false
            if (index === 1) {
              this.$refs.AttachmentManagement.getList()
              this.$refs.AttachmentManagement.setDir()
            } else {
              this.$refs.AttachmentManagement.getList(route.path)
              this.$refs.AttachmentManagement.setDir(route.path)
            }
          }
          this.routes.splice(index + 1)
        }
      })
    },
    handleSearchFieldChange(value) {
      if (value === 'type') {
        this.searchText = this.buttonType
      } else {
        this.searchedColumn = value
        this.searchText = ''
      }
      this.pagination.current = 1
      this.getList()
    },
    handleButtonTypeChange(value) {
      this.searchText = value
      this.pagination.current = 1
      this.getList()
    },
    async onSearch(value) {
      this.searchText = value
      this.pagination.current = 1
      await this.getList()
    },
    // 搜索查询
    // async onSearch(value) {
    //   this.searchText = value
    //   this.pagination.current = 1
    //   await this.getList()
    // },
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
      // this.routes.push({path: Math.random()+'', breadcrumbName: Math.random()+''})
      this.operation = 2
      this.open = true
      this.title = '新增配置'
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
      this.title = '编辑配置'
      this.editData = row
    },
    handleDetail(row) {
      this.operation = 1
      this.open = true
      this.title = '详情展示'
      this.editData = row
    },
    handleConfig(row) {
      this.isShow = false
      this.routes.push({ path: row.name, breadcrumbName: row.name })
      this.configName = row.name
    },
    // 删除数据
    async handleDelete(row) {
      const url = `/storage/tenant-server-configs/` + row.id
      const rst = await this.$axios.delete(url)
      if (rst.data.isSuccessed) {
        this.$message.success('删除成功')
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`)
      }
      this.$nextTick(() => {
        if (
          this.pagination.current !== 1 &&
          this.configurationList.length === 1
        ) {
          this.pagination.current--
        }
        this.getList()
      })
    },
    // 批量删除
    async batchDel() {
      const url = '/storage/tenant-server-configs/'
      const selectedRowKeys = this.selectedRowKeys
      if (selectedRowKeys.length !== 0) {
        const rst = await this.$axios.delete(url, { data: selectedRowKeys })
        if (rst.data.isSuccessed) {
          this.$message.success('删除成功')
        } else {
          this.$message.error(`删除失败,原因:${rst.data.message}`)
        }
        this.$nextTick(() => {
          if (
            this.pagination.current !== 1 &&
            selectedRowKeys.length === this.configurationList.length
          ) {
            this.pagination.current--
          }
          this.getList()
          this.selectedRowKeys = []
        })
      } else {
        this.$message.warn('请选择您要删除的内容')
      }
    },
    // 清空
    clearOptions() {
      this.selectedRowKeys = []
      this.selectRowLength = 0
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
      this.selectRowLength = selectedRowKeys.length
      if (this.selectedRowKeys.length > 0) {
        this.multiple = false
      } else {
        this.multiple = true
      }
    },
    async getList() {
      this.loading = true
      let url = `/storage/global-server-configs?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`
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
        this.configurationList = res.data.pageList
        // for (const i in this.configurationList) {
        //   if (this.configurationList[i].type === '1') {
        //     this.configurationList[i].type = '基础类'
        //   }
        //   if (this.configurationList[i].type === '2') {
        //     this.configurationList[i].type = '业务类'
        //   }
        // }
      } else {
        this.$message.error(`查询失败,原因:${res.message}`)
      }
    },
    addbreadcrumb(row) {
      const name = row.name.split('/')
      this.routes.push({
        path: row.name,
        breadcrumbName: name[name.length - 2],
      })
    },
    popRoutes() {
      if (this.routes.length > 2) {
        this.routes.pop()
      }
    },
    logout() {
      document.getElementById('logoutForm').submit()
      // const url = `/logout`
      // this.$axios.post(url)
      // this.$message.success('退出成功')
    },
  },
}
</script>

<style scoped>
.app-container {
  height: 100%;
}
.select-box {
  margin: 15px 0 0 65%;
  position: absolute;
}
.breadcrumb {
  position: relative;
  top: 10px;
  padding: 0 16px;
  z-index: 10;
  line-height: 21px;
}
.breadcrumb > div {
  display: inline-block;
}
.app-container::after {
  content: '';
  display: block;
  clear: both;
}
.page-single {
  min-height: 100vh;
  height: auto;
}
.logout {
  float: right;
  font-size: 1.2em;
  color: #fff !important;
}
</style>
