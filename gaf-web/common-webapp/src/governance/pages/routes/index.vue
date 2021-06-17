<template>
  <div class="app-container">
  <div class="page-single">
    <Gaf-table-layout v-show="!showDetail && !showAdd">
      <template #actions>
        <a-button
          @click="addGatewayRoute"
          type="primary"
          icon="plus"
          visible="true"
          class="btn-fun blue"
        >
          新增
        </a-button>
        <!-- <a-button
          @click="deleteGatewayRoute"
          disabled
          type="danger"
          icon="delete"
        >
          删除
        </a-button> -->
      </template>
      <template #filter>
        <!-- <gaf-search @search="onSearch" /> -->
        <div class="search-position" style="padding-bottom: 10px">
          <a-input-search
            placeholder="请输入路由ID查询"
            size="large"
            @search="onSearch"
          >
            <a-button slot="enterButton" class="btn-search"> 搜索 </a-button>
          </a-input-search>
        </div>
      </template>
      <!--表格数据-->
      <a-table
        :loading="loading"
        :columns="columns"
        :data-source="dataSource"
        :row-selection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange
        }"
        align="center"
        row-key="id"
      >
        <template slot="enable" slot-scope="v">
          <a-tag v-if="v" color="green">启用</a-tag>
          <template v-else><a-tag color="red">未启用</a-tag></template>
        </template>
        <template v-if="timeFormat" slot="createTime" slot-scope="text">
          {{ timeFormat(text) }}
        </template>
        <span slot="operation" slot-scope="text, record">
          <a @click.stop="() => editRoute(record)" href="javascript:;" class="btn-edit">
            <a-icon type="edit" /> 编辑
          </a>
          <a-divider type="vertical" />
          <a-popconfirm
            @confirm="() => deleteRoute(record.id)"
            title="删除后无法恢复，确认是否继续?"
            ok-text="确认"
            cancel-text="取消"
          >
            <a href="javascript:;" class="btn-del"><a-icon type="delete" /> 删除</a>
          </a-popconfirm>
        </span>
      </a-table>
    </Gaf-table-layout>
    <div v-show="showDetail">
      <route-datial-form
        v-model="showRouteInfo"
        @refreshData="loadRouteList"
        @cancel="showDetail = false"
        @back="showDetail = false"
      ></route-datial-form>
    </div>
    <div v-show="showAdd">
      <add-route-form
        @refreshData="loadRouteList"
        @cancel="showAdd = false"
        @back="showAdd = false"
      />
    </div>
  </div>
  </div>
</template>

<script>
// import { formatDateTime } from 'gaf-web-common'
import AddRouteForm from '../../views/route/AddRoute'
import RouteDatialForm from '../../views/route/RouteDetail'

const columns = [
  { title: '路由ID', width: 100, dataIndex: 'routeId', key: 'routeId' },
  { title: '地址', dataIndex: 'uri', key: 'uri', width: 150 },
  {
    title: '顺序',
    width: 100,
    dataIndex: 'order',
    key: 'order'
  },
  {
    title: '是否启用',
    width: 100,
    dataIndex: 'enable',
    key: 'enable',
    scopedSlots: { customRender: 'enable' }
  },
  {
    title: '创建时间',
    width: 100,
    dataIndex: 'createTime',
    key: 'createTime',
    scopedSlots: { customRender: 'createTime' }
  },
  {
    title: '操作',
    width: 100,
    dataIndex: 'operation',
    key: 'operation',
    scopedSlots: { customRender: 'operation' }
  }
]
export default {
  name: 'Index',
  components: {
    AddRouteForm,
    RouteDatialForm
  },
  computed: {
    timeFormat() {
      if (
        this.columns.filter(
          (item) =>
            item.scopedSlots && item.scopedSlots.customRender === 'createTime'
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
          // let hour = dt.getHours()
          // let minute = dt.getMinutes()
          // let second = dt.getSeconds()
          month = month < 10 ? '0' + month : month
          date = date < 10 ? '0' + date : date
          // hour = hour < 10 ? '0' + hour : hour
          // minute = minute < 10 ? '0' + minute : minute
          // second = second < 10 ? '0' + second : second

          return `${year}-${month}-${date}`
        }
      }
      return null
    },
  },
  data() {
    return {
      loading: false,
      columns: columns,
      dataSource: [],
      visible: false,
      selectedRowKeys: [],
      addVisible: false,
      editVisible: false,
      showDetail: false,
      showAdd: false,
      showRouteInfo: {
        id: '',
        enable: '',
        type: '',
        createTime: '',
        updateTime: '',
        routeId: '',
        uri: '',
        order: '',
        predicates: [],
        filters: [],
        uriAfter: '',
        uriBefore: ''
      }
    }
  },
  mounted: function() {
    this.loadRouteList()
  },
  methods: {
    async onSearch(value) {
      if (value === '') {
        this.loadRouteList()
      } else {
        this.loading = true
        const params = this.formatterParams()
        const res = await this.$axios.get('/srv-governance/routes', params)
        this.dataSource = res.data.data.filter(item =>
          item.routeId.includes(value)
        )
        this.loading = false
      }
    },
    onChange() {},
    handleOk() {
      setTimeout(() => {
        this.visible = false
      }, 2000)
    },
    handleCancel() {
      this.visible = false
    },
    addGatewayRoute: function() {
      this.showAdd = true
      this.addVisible = true
    },
    deleteGatewayRoute() {
      this.$confirm(`确认删除所选服务吗？`, '提示', {
        confirmButtonText: '删除',
        confirmButtonType: 'danger',
        cancelButtonText: '取消',
        type: 'warning'
      })
    },
    async loadRouteList() {
      this.loading = true
      const params = this.formatterParams()
      const res = await this.$axios.get('/srv-governance/routes', params)
      const { isSuccessed, message, data } = res.data
      if (!isSuccessed) {
        this.$message.error(message)
      }
      // data.forEach(d => (d.createTime = formatDateTime(d.createTime)))
      this.dataSource = data
      this.loading = false
      this.showDetail = false
    },
    editRoute(v) {
      const idx = v.uri.indexOf('://')
      this.$set(v, 'uriBefore', v.uri.slice(0, idx + 3))
      this.$set(v, 'uriAfter', v.uri.slice(idx + 3))
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
    async deleteRoute(v) {
      const rst = await this.$axios.delete('/srv-governance/routes/' + v)
      const { isSuccessed, message } = rst.data
      if (isSuccessed) {
        this.$message.success(message)
        const index = this.dataSource.findIndex(item => item.id === v)
        this.dataSource.splice(index, 1)
      } else {
        this.$message.error(message)
      }
    },
    formatterParams() {},
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    }
  }
}
</script>

<style scoped></style>
