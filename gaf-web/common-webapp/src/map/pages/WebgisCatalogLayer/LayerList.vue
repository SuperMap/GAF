<template>
  <div>
    <gaf-table-layout v-show="!open">
      <template #actions>
        <button
          class="btn-fun blue btn-16"
          visible="true"
          @click="handleLink"
        >
          <a-icon type="link" />批量关联服务
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
        <a-select
          v-show="false"
          v-model="searchedColumn"
          style="width: 150px"
          @change="handleSearchFieldChange"
        >
          <a-select-option value="name"> 名称 </a-select-option>
        </a-select>
        <gaf-search
          placeholder="请输入图层名按回车查询"
          style="width: 200px"
          @search="(keyword) => handleFilterChange(keyword)"
        />
      </template>
      <template #default>
        <gaf-table-with-page
          :pagination="pagination"
          :row-selection="{
            selectedRowKeys: selectedRowKeys,
            onChange: onSelectChange,
          }"
          :data-source="webgisCatalogLayerList"
          :loading="loading"
          :row-key="(r) => r.catalogLayerId"
          :columns="
            columns.filter((item) => item.dataIndex !== 'catalogLayerId')
          "
          class="table-style"
          size="middle"
          @change="tableChange"
        >
          <template slot="initLoad" slot-scope="text, record">
            <span>{{ record.initLoad ? '是' : '否' }}</span>
          </template>
          <template slot="operation" slot-scope="text, record">
            <a
              @click.stop="() => handleUpdate(record)"
              href="javascript:;"
              class="btn-margin"
              >编辑
            </a>
            <a-popconfirm
              @confirm="() => handleDelete(record)"
              title="删除后无法恢复，确认是否继续?"
              ok-text="确认"
              cancel-text="取消"
            >
              <a href="javascript:;">删除</a>
            </a-popconfirm>
          </template>
        </gaf-table-with-page>
      </template>
    </gaf-table-layout>
    <a-modal
      v-model="showMutiSelectList"
      :width="1200"
      :centered="true"
      title="批量关联服务"
      destroy-on-close
      ok-text="确认"
      cancel-text="取消"
      @ok="onOk"
      @cancel="onCancel"
    >
      <webgis-muti-select-list
        :options="options"
        :webgis-service-types="webgisServiceTypes"
        :layer-catalog-id="layerCatalogId"
        @onSelectedChange="onSelectedChange"
      ></webgis-muti-select-list>
    </a-modal>
    <a-modal
      v-model="open"
      :width="1000"
      :centered="true"
      :title="operation === 2 ? '新增图层' : '编辑图层'"
      destroy-on-close
      @ok="onAddOrEditOk"
    >
      <add-edit-form
        ref="addOrEditForm"
        :edit-data="editData"
        :layer-catalog-id="layerCatalogId"
        :operation="operation"
      >
      </add-edit-form>
    </a-modal>
  </div>
</template>

<script>
    import '~/assets/css/common.css'
    import AddEditForm from '../../views/WebgisCatalogLayer/AddOrEditForm.vue'
    import WebgisMutiSelectList from '../WebgisService/WebgisMutiSelectList.vue'

    export default {
  components: {
    AddEditForm,
    WebgisMutiSelectList,
  },
  props: {
    layerCatalogId: {
      type: String,
      default: '',
    },
    // ,
    // layerCatalogPath: {
    //   type: Array,
    //   default: () => []
    // }
  },
  data() {
    return {
      // -------------------------
      // ---选择服务相关数据
      tempSelectedRowKeys: [],
      selectedRowKeys: [],
      tempSelectedRows: [],
      // 是否显示服务选择列表
      showMutiSelectList: false,
      // 服务类型可选项
      options: [],
      // 服务类Map
      webgisServiceTypes: new Map(),
      // -------------------------
      // ---- 图层相关（即已选的服务对应的列表）
      clearFilters: null,
      // 编辑记录
      editData: {},
      // ${functionName}表格数据
      webgisCatalogLayerList: [],
      // 是否显示添加修改弹出层
      open: false,
      // 分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0,
      },
      // 列表是否加载中
      loading: false,
      searchText: '',
      searchedColumn: 'name',
      sorter: {
        order: 'ASC',
        field: 'sort_sn',
      },
      columns: [
        {
          title: '名称',
          dataIndex: 'name',
          key: 'name',
        },
        {
          title: '关联的GIS服务名',
          dataIndex: 'serviceName',
          key: 'service_name',
        },
        {
          title: '排序序号',
          dataIndex: 'sortSn',
          key: 'sort_sn',
          sorter: true,
          defaultSortOrder: 'ascend',
          sortDirections: ['descend', 'ascend'],
        },
        {
          title: '是否初始加载',
          dataIndex: 'initLoad',
          key: 'init_load',
          scopedSlots: { customRender: 'initLoad' },
        },
        {
          title: '描述',
          dataIndex: 'description',
          key: 'description',
        },
        {
          title: '有效性',
          dataIndex: '',
          key: '',
          scopedSlots: { customRender: '' },
        },
        {
          title: '操作',
          scopedSlots: { customRender: 'operation' },
        },
      ],
      // 详情：1，新增：2，编辑：3
      operation: 0,
    }
  },
  computed: {},
  watch: {
    layerCatalogId() {
      this.pagination.current = 1
      this.getList()
    },
  },
  created() {
    if (this.layerCatalogId && this.layerCatalogId !== '') {
      this.getList()
    }
    this.getWebgisServiceTypes()
  },
  methods: {
    // ---- 服务选择相关
    onSelectedChange(selectedRowKeys, selectedRows) {
      this.tempSelectedRowKeys = selectedRowKeys
      this.tempSelectedRows = selectedRows
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
      this.selectRowLength = selectedRowKeys.length;
      if (this.selectedRowKeys.length > 0) {
        this.multiple = false;
      } else {
        this.multiple = true;
      }
    },
    onOk() {
      // 校验
      if (this.tempSelectedRows && this.tempSelectedRows.length > 0) {
        const data = this.tempSelectedRows.map((item) => {
          const copy = { ...item }
          copy.layerCatalogId = this.layerCatalogId
          return copy
        }, this)
        this.addLayersAndThen(data)
      } else {
        this.$message.info('请选择至少一个服务')
      }
      this.tempSelectedRows = []
      this.tempSelectedRowKeys = []
    },
    onCancel() {
      // 清空临时变量
      this.tempSelectedRowKeys = []
      this.tempSelectedRows = []
    },
    // -------------------编辑图层表单
    onAddOrEditOk() {
      this.$refs.addOrEditForm.submitForm((isSuccessed) => {
        if (isSuccessed) {
          this.open = false
          this.getList()
        }
      })
    },
    // ----图层相关（已选择的服务对应的服务列表）
    handleSearchFieldChange(value) {
      this.searchedColumn = value
    },
    async handleFilterChange(value) {
      this.searchText = value
      this.pagination.current = 1
      await this.getList()
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
      } else {
        this.sorter.order = null
        this.sorter.field = null
      }
      this.getList()
    },
    // 处理点击关联服务按钮 打开选择列表
    handleLink() {
      this.showMutiSelectList = true
    },
    // 修改数据
    handleUpdate(row) {
      this.operation = 3
      this.open = true
      this.editData = row
    },
    //批量删除
    async batchDel() {
      const url = "/map/webgis-catalog-layers/";
      const selectedRowKeys = this.selectedRowKeys;
      if (selectedRowKeys.length !== 0) {
        const rst = await this.$axios.delete(url, { data: selectedRowKeys });
        if (rst.data.isSuccessed) {
          this.$message.success("删除成功");
        } else {
          this.$message.error(`删除失败,原因:${rst.data.message}`);
        }
        this.$nextTick(() => {
          if (
            this.pagination.current !== 1 &&
            selectedRowKeys.length === this.webgisButtonList.length
          ) {
            this.pagination.current--;
          }
          this.getList();
          this.selectedRowKeys = [];
        });
      } else {
        this.$message.warn("请选择您要删除的内容");
      }
    },
    // 删除数据
    async handleDelete(row) {
      const url = `/map/webgis-catalog-layers/` + row.catalogLayerId
      const rst = await this.$axios.delete(url)
      if (rst.data.isSuccessed) {
        this.$message.success('删除成功')
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`)
      }
      this.$nextTick(() => {
        this.getList()
      })
    },
    async getList() {
      this.loading = true
      let url = `/map/webgis-catalog-layers?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`
      if (this.layerCatalogId) {
        url = url + '&layerCatalogId=' + this.layerCatalogId
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
        this.webgisCatalogLayerList = res.data.pageList
        if (this.webgisCatalogLayerList.length > 0){
          this.$emit('changeOpen', false)
        } else{
          this.$emit('changeOpen', true)
        }
      } else {
        this.$message.error(`查询失败,原因:${res.message}`)
      }
    },
    async getWebgisServiceTypes() {
      const url = `/sys-mgt/sys-dicts/ServiceType/dict-data`
      const res = await this.$axios.$get(url)
      const allOptions = [{ value: '-1', label: '所有服务类型' }]
      if (res.isSuccessed) {
        const typeList = res.data
        if (typeList && typeList.length > 0) {
          const map = new Map()
          typeList.forEach((element) => {
            map.set(element.value, element.label)
          })
          this.webgisServiceTypes = map
          allOptions.push(
            ...typeList.map((item) => {
              return { value: item.value, label: item.label }
            })
          )
        }
      } else {
        this.$message.error('加载服务类型失败,原因：' + res.message)
      }
      this.options = allOptions
    },
    async addLayersAndThen(data) {
      const url = '/map/webgis-catalog-layers/batch/webgis-service'
      const res = await this.$axios.$post(url, data)
      if (res.isSuccessed) {
        this.showMutiSelectList = false
        this.getList()
      } else {
        this.$message.error(`关联失败，原因:${res.message}`)
      }
    },
  },
}
</script>

<style scoped>
.app-container {
  height: 100%;
}
</style>
