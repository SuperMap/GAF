<template>
  <div class="app-container">
    <div>
      <div class="page-left">
      <div class="tree-catalog">
        <span class="vertical-line">| </span>
        服务类型
      </div>
        <gaf-tree-transparent
          ref="myGafTreeTransparent"
          :data-of-tree="dataOfTree"
          :search-type="[0]"
          :expanded-node-keys.sync="expandedNodeKeys"
          :show-search="false"
          @select="onSelect"
        >
        </gaf-tree-transparent>
      </div>
      <div class="page-right">
        <gaf-table-layout>
          <template #actions>
            <button
              class="btn-fun blue"
              @click="handleAdd"
            >
             <span><a-icon type="plus" />服务注册</span>
            </button>
            <button class="btn-fun red" @click="batchDel">
              <a-icon type="delete" />
              <span>批量删除</span>
            </button>
          </template>
          <template #filter>
            <div style="margin-top: 5px">
              <a-input-search
                placeholder="请输入服务名按回车查询"
                size="large"
                style="width: 320px"
                @search="onSearch"
              >
                <button slot="enterButton" class="btn-search">
                  搜索
                </button>
              </a-input-search>
            </div>
          </template>
          <template #default>
            <gaf-table-with-page
              :scroll="{ y: 570 }"
              :showXH="false"
              :pagination="pagination"
              :data-source="webgisServiceList"
              :loading="loading"
              :row-selection="{
                selectedRowKeys: selectedRowKeys,
                onChange: onSelectChange,
              }"
              :row-key="(r) => r.gisServiceId"
              :columns="
                columns.filter((item) => item.dataIndex !== 'gisServiceId')
              "
              bordered
              size="small"
              align="center"
              @change="tableChange"
            >
              <template slot="serviceType" slot-scope="text, record">
                {{ map.get(record.typeCode)[0] ? map.get(record.typeCode)[0] : '' }}
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
                <a
                  v-if="JSON.parse(map.get(record.typeCode)[1]) ? JSON.parse(map.get(record.typeCode)[1]).isDataService : false"
                  class="btn-configure"
                  @click.stop="configField(record)"
                >
                  <a-icon type="control" /> 配置
                </a>
                <a
                  v-if="JSON.parse(map.get(record.typeCode)[1]) ? !JSON.parse(map.get(record.typeCode)[1]).isDataService : true"
                  class="btn-code"
                  @click.stop="linkService(record)"
                >
                  <a-icon type="link" /> 关联
                </a>
                <a-divider type="vertical" />
                <a-popconfirm
                  title="删除后无法恢复，确认是否继续?"
                  ok-text="确认"
                  cancel-text="取消"
                  class="btn-del"
                  @confirm="() => handleDelete(record)"
                >
                  <a href="javascript:;"><a-icon type="delete" /> 删除</a>
                </a-popconfirm>
              </template>
              <template v-if="timeFormat" slot="timeRender" slot-scope="text">
                {{ timeFormat(text) }}
              </template>
              <template slot="address" slot-scope="text">
                <div class="url">
                  <a-tooltip placement="topLeft">
                    <template slot="title">
                      {{ text }}
                    </template>
                    <a :href="text" target="_blank">{{ text }}</a>
                  </a-tooltip>
                </div>
              </template>
            </gaf-table-with-page>
          </template>
        </gaf-table-layout>
      </div>
      <a-modal
        v-model="open"
        :width="800"
        :footer="null"
        :centered="true"
        destroy-on-close
        @cancel="handleBack"
      >
        <add-edit-form
          :title="title"
          :edit-data="editData"
          :operation="operation"
          :data-of-tree="dataOfTree"
          @submit="handleSubmit"
          @back="handleBack"
        >
        </add-edit-form>
      </a-modal>
      <a-modal
        v-model="openconfigField"
        title="配置字段"
        width="40%"
        :centered="true"
        destroy-on-close
        :footer="null"
      >
        <config-field-list
          :selected-service-id="selectedServiceId"
          @onOk="onOk"
          @onCancel="onCancel"
        ></config-field-list>
      </a-modal>
      <link-service
        v-model="linkedVisible"
        :webgis-service="webgisService"
      ></link-service>
    </div>
  </div>
</template>

<script>
    // @ts-nocheck
    import '~/assets/css/common.css'
    import AddEditForm from '../../views/WebgisService/AddOrEditForm.vue'
    import ConfigFieldList from './ConfigFieldList.vue'
    import LinkService from './LinkService.vue'

    export default {
  components: {
    AddEditForm,
    LinkService,
    ConfigFieldList,
  },
  data() {
    return {
      open2: false,
      registrationResults: '',
      selectedServiceId: '',
      webgisService: null,
      // 关联模态框是否可见
      linkedVisible: false,
      map: new Map(),
      type: null,
      dataOfTree: [],
      expandedNodeKeys: [],
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
      webgisServiceList: [],
      // 是否显示添加修改弹出层
      open: false,
      openconfigField: false,
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
      // 新增：1，编辑：2
      operation: 0,
      // 有无主键
      hasPKField: true,
    }
  },
  computed: {
    columns() {
      const columns = [
        {
          title: 'GIS服务id',
          dataIndex: 'gisServiceId',
          key: 'gis_service_id',
        },
        {
          title: '服务名称',
          scopedSlots: {
            filterDropdown: 'filterDropdown',
            filterIcon: 'filterIcon',
            customRender: 'customRender',
          },
          width: '135px',
          dataIndex: 'name',
          key: 'name',
        },
        {
          title: '服务类型',
          dataIndex: 'typeCode',
          key: 'type_code',
          width: '135px',
          scopedSlots: { customRender: 'serviceType' },
        },
        {
          title: '服务地址',
          dataIndex: 'address',
          key: 'address',
          scopedSlots: { customRender: 'address' },
          width: '400px',
        },
        {
          title: '时态',
          dataIndex: 'timeAttribute',
          key: 'time_attribute',
          width: '135px',
          scopedSlots: { customRender: 'timeRender' },
        },
        {
          title: '操作',
          width: '400px',
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

          return `${year}-${month}-${date} ${hour}:${minute}:${second}`
        }
      }
      return null
    },
  },
  watch: {},
  mounted() {},
  async created() {
    await this.getNodesAndSetByType()
    this.getList()
  },
  methods: {
    onOk(openconfigField) {
      this.openconfigField = openconfigField
    },
    onCancel(openconfigField) {
      this.openconfigField = openconfigField
    },
    // 点击配置时
    configField(row) {
      // sd
      this.openconfigField = true
      this.selectedServiceId = row.gisServiceId
    },
    // 关联服务
    linkService(record) {
      this.webgisService = record
      this.linkedVisible = true
    },
    // 根据搜索文本拆分单元格文本内容
    splitCellWithSearchText(text) {
      const str = text === null ? '' : text
      return str
        .toString()
        .split(
          new RegExp(`(?<=${this.searchText})|(?=${this.searchText})`, 'i')
        )
    },
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
    tableChange(pageInfo) {
      if (pageInfo) {
        this.pagination.current = pageInfo.current
        this.pagination.pageSize = pageInfo.pageSize
      }
      this.getList()
    },
    // 添加数据
    handleAdd() {
      this.operation = 2
      this.open = true
      this.title = '注册GIS服务'
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
      this.title = '修改GIS服务'
      this.editData = row
    },
    /*   handleDetail(row) {
      this.operation = 1
      this.open = true
      this.title = '详情展示'
      this.editData = row
    }, */
    // 删除数据
    async handleDelete(row) {
      const url = `/map/webgis-services/` + row.gisServiceId
      const rst = await this.$axios.delete(url)
      if (rst.data.isSuccessed) {
        this.$message.success('删除成功')
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`)
      }
      this.$nextTick(() => {
        if (this.pagination.current !== 1 && this.webgisServiceList.length === 1){
          this.pagination.current--
        }
        this.getList()
      })
    },
    // 批量删除
    async batchDel() {
      const url = '/map/webgis-services/'
      const selectedRowKeys = this.selectedRowKeys
      if (selectedRowKeys.length !== 0) {
        const rst = await this.$axios.delete(url, { data: selectedRowKeys })
        if (rst.data.isSuccessed) {
          this.$message.success('删除成功')
        } else {
          this.$message.error(`删除失败,原因:${rst.data.message}`)
        }
        this.$nextTick(() => {
          if (this.pagination.current !== 1 && selectedRowKeys.length === this.webgisServiceList.length){
            this.pagination.current--
          }
          this.getList()
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
    // 获取地图应用 树结构数据
    async getNodesAndSetByType() {
      const url = `/sys-mgt/sys-dicts/ServiceType/tree`
      const res = await this.$axios.$get(url)


      if (res.isSuccessed) {
      res.data.forEach((element) => {
        this.map.set(element.value, [element.label, element.extProperties])
        element.key = element.value
        element.title = element.label
        delete element.value
        delete element.label
      })
      res.data.splice(0, 0, {
        key: '-1',
        title: '所有类型',
        children: null,
        type: 0,
        sortSn: 0,
        parentId: '0',
        scopedSlots: {
          title: 'title',
        },
        style: 'font-size: 18px;font-weight: bold',
      })
        this.dataOfTree = res.data
      } else {
        this.$message.error('加载角色树失败,原因：' + res.message)
      }
    },
    //  树结构数据  搜索
    async getSelected() {
      /* const url = `/authority/auth-role-menu/list-by-role/${this.roleId}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        if (res.data !== null && res.data.length > 0) {
          this.checkedNodeKeys = res.data.map(item => item.resourceMenuId)
        } else {
          this.checkedNodeKeys = []
        }
      } */
    },
    onSelect(selectedKeys, e) {
      if (e.node.dataRef.key === '-1' || !e.selected) {
        this.type = null
      } else {
        this.type = e.node.dataRef.key
      }
      this.getList()
    },
    // 树结构加载
    async getList() {
      this.loading = true

      let url = `/map/webgis-services?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`
      if (this.type) {
        url = url + '&typeCode=' + this.type
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
        this.webgisServiceList = res.data.pageList
      } else {
        this.$message.error(`查询失败,原因:${res.message}`)
      }
    },
  },
}
</script>

<style scoped>
.url {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 400px;
}

.url a {
  color: rgb(34, 34, 34);
  text-decoration: none;
}

.url a:link {
  color: rgb(34, 34, 34);
}

.url a:visited {
  color: rgb(153, 153, 153);
}

.url a:hover {
  color: #559df5;
}

.url a:active {
  color: rgb(153, 153, 153);
}
</style>
