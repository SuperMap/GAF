<template>
  <div class="app-container">
    <div class="page-single">
      <div v-show="showList">
        <gaf-table-layout>
          <template #actions>
            <button
              class="btn-fun blue"
              @click="onAdd"
            >
              <span><a-icon type="plus" />新增</span>
            </button>
            <button class="btn-fun red" @click="batchDel">
              <span><a-icon type="delete" />
              批量删除</span>
            </button>
          </template>
          <template #filter>
            <div style="margin-top: 5px">
              <a-input-search
                placeholder="请输入目录名按回车查询"
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
              :row-selection="{
                selectedRowKeys: selectedRowKeys,
                onChange: onSelectChange,
              }"
              :pagination="pagination"
              :data-source="resourceRootCatalogs"
              :loading="loading"
              :row-key="(r) => r.catalogId"
              style="width: 100%; height: 570px; overflow: auto"
              :columns="columns"
              bordered
              size="small"
              align="center"
              @change="tableChange"
            >
              <template slot="bizType" slot-scope="text">
                {{ bizTypeMap.get(text) ? bizTypeMap.get(text) : text }}
              </template>
              <template slot="operation" slot-scope="text, record">
                <a
                  class="btn-edit"
                  href="javascript:;"
                  @click.stop="() => handleUpdate(record)"
                >
                  <span><a-icon type="edit" />编辑</span>
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
            </gaf-table-with-page>
          </template>
        </gaf-table-layout>
      </div>
      <div v-if="!showList" class="app-container">
        <webgis-catalog-layer
          :root-catalog-id="rootCatalogId"
          :biz-types="bizTypes"
          @backToList="backToList"
        ></webgis-catalog-layer>
      </div>
    </div>
  </div>
</template>

<script>
    import '~/assets/css/common.css'
    import WebgisCatalogLayer from '../WebgisCatalogLayer/index.vue'

    export default {
  components: {
    WebgisCatalogLayer,
  },
  props: {
    // webgisServiceTypes: {
    //   type: Map,
    //   default: () => new Map()
    // }
  },
  data() {
    return {
      selectedRowKeys: [],
      bizTypes: [],
      bizTypeMap: new Map(),
      rootCatalogId: null,
      showList: true,
      resourceRootCatalogs: [],
      // 是点击了新增还是编辑
      isAdd: false,
      // 列表相关数据
      searchValue: '',
      searchedColumn: 'name',
      // 分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0,
      },
      sorter: {
        order: null,
        field: null,
      },
      // 列表是否加载中
      loading: true,
      columns: [
        {
          title: '资源目录名称',
          dataIndex: 'name',
          key: 'name',
        },
        {
          title: '目录类型',
          dataIndex: 'bizTypeCode',
          key: 'biz_type_code',
          scopedSlots: { customRender: 'bizType' },
        },
        {
          title: '目录描述',
          dataIndex: 'description',
          key: 'description',
        },
        {
          title: '资源目录ID',
          dataIndex: 'catalogId',
          key: 'catalogId',
        },
        {
          key: 'operation',
          title: '操作',
          scopedSlots: { customRender: 'operation' },
        },
      ],
    }
  },
  computed: {
    // searchPlaceholder: function() {
    //   if (this.searchedColumn === 'name') {
    //     return '请输入服务名称进行搜索'
    //   } else if (this.searchedColumn === 'address') {
    //     return '请输入服务地址进行搜索'
    //   } else {
    //     return '请输入值进行搜索'
    //   }
    // }
  },
  created() {
    this.getList()
    this.getBizTypes()
  },
  methods: {
    // 新增
    onAdd() {
      this.rootCatalogId = null
      this.isAdd = true
      this.showList = false
    },
    // 编辑
    handleUpdate(record) {
      this.rootCatalogId = record.catalogId
      this.showList = false
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
      if (this.selectedRowKeys.length > 0) {
        this.multiple = false
      } else {
        this.multiple = true
      }
    },
    async handleDelete(record) {
      const url = `/sys-mgt/sys-catalogs/${record.catalogId}`
      const res = await this.$axios.$delete(url)
      if (res.isSuccessed) {
        this.$message.success('删除成功')
      } else {
        this.$message.error(`删除失败，原因:${res.message}`)
      }
      this.$nextTick(() => {
        if (this.pagination.current !== 1 && this.resourceRootCatalogs.length === 1){
          this.pagination.current--
        }
        this.getList()
      })
    },
    // 批量删除
    async batchDel() {
      const url = '/sys-mgt/sys-catalogs/'
      const selectedRowKeys = this.selectedRowKeys
      if (selectedRowKeys.length !== 0) {
        const rst = await this.$axios.delete(url, { data: selectedRowKeys })
        if (rst.data.isSuccessed) {
          this.$message.success('删除成功')
        } else {
          this.$message.error(`删除失败,原因:${rst.data.message}`)
        }
        this.$nextTick(() => {
          if (this.pagination.current !== 1 && selectedRowKeys.length === this.resourceRootCatalogs.length){
            this.pagination.current--
          }
          this.getList()
        })
      } else {
        this.$message.warn('请选择您要删除的内容')
      }
    },
    // 当搜索时
    onSearch(searchValue) {
      this.searchValue = searchValue
      this.getList()
    },
    // backToList
    backToList() {
      this.showList = true
      this.getList()
      this.isAdd = false
    },
    async getList() {
      this.loading = true
      let url = `/sys-mgt/sys-catalogs/resource-root-catalogs?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`
      if (this.searchValue.trim() && this.searchedColumn) {
        url =
          url +
          '&searchFieldName=' +
          this.searchedColumn +
          '&searchFieldValue=' +
          this.searchValue.trim()
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
        this.pagination.current = res.data.pageIndex
        this.pagination.pageSize = res.data.pageSize
        this.pagination.total = res.data.total
        this.resourceRootCatalogs = res.data.content
      } else {
        this.$message.error(`查询失败,原因:${res.message}`)
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
    async getBizTypes() {
      const res = await this.$axios.$get('/sys-mgt/sys-catalogs/biz-types')
      if (res.isSuccessed) {
        this.bizTypes = res.data
        const typeMap = new Map()
        res.data.forEach((element) => {
          typeMap.set(element.value, element.label)
        })
        this.bizTypeMap = typeMap
      } else {
        this.$message.error(`未查询到业务类别,原因:${res.message}`)
      }
    },
  },
}
</script>
