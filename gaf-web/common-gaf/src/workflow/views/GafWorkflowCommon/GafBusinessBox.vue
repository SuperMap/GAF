<template>
  <div class="page-single">
    <Gaf-table-layout v-show="!showDetail && !showAdd">
      <template #filter>
        <!--      <gaf-tenant @change="handleChange" />-->
        <!-- <gaf-search @search="onSearch" /> -->
        <div class="search-position" style="padding-bottom: 10px">
          <a-input-search
            placeholder="请输入名称查询"
            size="large"
            @search="onSearch"
          >
            <a-button slot="enterButton" class="btn-search"> 搜索 </a-button>
          </a-input-search>
        </div>
      </template>
      <!--表格数据-->
      <template #default>
        <gaf-table-with-page
          :loading="loading"
          :columns="columns"
          :data-source="dataSource"
          :pagination="pagination"
          align="center"
          row-key="id"
          bordered
          @change="handleChange()"
        >
          <template slot="ProInstCode" slot-scope="text, record">
            <slot :PIC="{ text, record }" name="ProInstCode"></slot>
          </template>
          <template slot="ProInstName" slot-scope="text, record">
            <slot :PIN="{ text, record }" name="ProInstName"></slot>
          </template>
          <template slot="ActInstName" slot-scope="text, record">
            <slot :AIN="{ text, record }" name="ActInstName"></slot>
          </template>
          <template slot="ProDefTypeName" slot-scope="text, record">
            <slot :PDTN="{ text, record }" name="ProDefTypeName"></slot>
          </template>
          <template slot="ActInstEndDate" slot-scope="text, record">
            <slot :AIED="{ text, record }" name="ActInstEndDate"></slot>
          </template>
          <template slot="operation" slot-scope="text, record">
            <slot :ot="{ text, record }" name="operation"></slot>
          </template>
        </gaf-table-with-page>
      </template>
    </Gaf-table-layout>
  </div>
</template>

<script>
import '../css/workFlow.css'
const columns = [
  {
    title: '流程编号',
    sorter: true,
    dataIndex: 'ProInst_Code',
    key: 'ProInstCode',
    scopedSlots: { customRender: 'ProInstCode' },
  },
  {
    title: '项目名称',
    dataIndex: 'ProInst_Name',
    key: 'ProInstName',
    scopedSlots: { customRender: 'ProInstName' },
  },
  {
    title: '活动名称',
    dataIndex: 'ActInst_Name',
    key: 'ActInstName',
    scopedSlots: { customRender: 'statr' },
  },
  {
    title: '流程名称',
    dataIndex: 'ProDef_TypeName',
    key: 'ProDefTypeName',
    scopedSlots: { customRender: 'ProDefTypeName' },
  },
  {
    title: '剩余时间',
    sorter: true,
    // dataIndex: 'ActInst_EndDate',
    key: 'ActInstEndDate',
    scopedSlots: { customRender: 'ActInstEndDate' },
  },
  {
    title: '操作',
    dataIndex: 'operation',
    key: 'operation',
    scopedSlots: { customRender: 'operation' },
  },
]
export default {
  name: 'Index',
  components: {},
  props: {
    dataSource: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      loading: false,
      columns,
      visible: false,
      selectedRowKeys: [],
      addVisible: false,
      editVisible: false,
      showDetail: false,
      showAdd: false,
      pagination: {
        total: 0,
        pageSize: 10, // 每页中显示10条数据
        showSizeChanger: true,
        // pageSizeOptions: ['5', '10', '20', '50'], // 每页中显示的数据
        showTotal: (total) => `共有 ${total} 条数据`, // 分页中显示总的数据
      },
    }
  },
  methods: {
    onSearch(keyword) {
      this.$emit('searchValue', keyword)
    },
    handleOk() {
      setTimeout(() => {
        this.visible = false
      }, 2000)
    },
    handleCancel() {
      this.visible = false
    },
    editRoute(v) {
      const idx = v.uri.indexOf('://')
      this.$set(v, 'uriBefore', v.uri.slice(0, idx + 3))
      this.$set(v, 'uriAfter', v.uri.slice(5))
      this.showDetail = true
      this.showRouteInfo = v
      for (let i = 0; i < this.showRouteInfo.filters.length; i++) {
        const f = this.showRouteInfo.filters[i]
        this.$set(f, 'id', f.name + i)
      }
      for (let i = 0; i < this.showRouteInfo.predicates.length; i++) {
        const p = this.showRouteInfo.predicates[i]
        this.$set(p, 'id', p.name + i)
      }
    },
    formatterParams() {},
  },
}
</script>
<style scoped>
.layout-badge-blue {
  background: rgb(66, 105, 233);
  color: white;
  padding: 0 3%;
  border-radius: 5px;
}
</style>
