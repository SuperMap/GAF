<template>
<div class="app-container">
  <div class="page-single">
    <div class="gaf-date-param">
      <a-row type="flex" justify="start" align="middle">
        <a-col style="margin-right: 10px">
          <label>时间选择</label>
        </a-col>
        <a-col>
          <a-range-picker
            :show-time="{ format: 'HH:mm:ss' }"
            :value="timeRange"
            @change="onPickerChange"
            @ok="onPickerOk"
            style="width:400px"
            format="YYYY-MM-DD HH:mm:ss"
          />
        </a-col>
      </a-row>
    </div>
    <gaf-table-with-page
      :row-key="record => record.id"
      :columns="table.columns"
      :data-source="table.data"
      :pagination="table.pagination"
      :loading="table.loading"
      @change="onTableChange"
      :indent-size="50"
      size="small"
    >
      <div slot="customHttpInfoRender" slot-scope="text, record">
        {{ record.httpMethod }} {{ text }}
      </div>
      <div
        slot="customTimeAnalyRender"
        slot-scope="record"
        style="position:relative;bottom: 10px"
      >
        <a-tooltip>
          <template slot="title">
            <div><span style="color: green">SpanID:</span>{{ record.id }}</div>
            <div v-if="record.error">
              <span style="color: red">错误信息:</span>{{ record.error }}
            </div>
          </template>
          <div :style="getTimeAnalyStyle(record)">
            {{ record.durationFormat }}
          </div>
        </a-tooltip>
      </div>

      <div
        slot="filterDropdown"
        slot-scope="{
          setSelectedKeys,
          selectedKeys,
          confirm,
          clearFilters,
          column
        }"
        style="padding: 8px"
      >
        <a-input
          :placeholder="`输入搜索内容`"
          :value="selectedKeys[0]"
          @pressEnter="
            () => handleFilterSearch(selectedKeys, confirm, column.dataIndex)
          "
          @change="e => setSelectedKeys(e.target.value ? [e.target.value] : [])"
          style="width: 188px; margin-bottom: 8px; display: block;"
        />
        <a-button
          @click="
            () => handleFilterSearch(selectedKeys, confirm, column.dataIndex)
          "
          type="primary"
          icon="search"
          size="small"
          style="width: 90px; margin-right: 8px"
        >
          确认
        </a-button>
        <a-button
          @click="() => handleFilterReset(clearFilters, column.dataIndex)"
          size="small"
          style="width: 90px"
        >
          重置
        </a-button>
      </div>
      <a-icon
        slot="filterIcon"
        slot-scope="filtered"
        :style="{ color: filtered ? '#108ee9' : undefined }"
        type="search"
      />
    </gaf-table-with-page>
  </div>
</div>
</template>
<script>
import qs from 'qs'
import moment from 'moment'

const columns = [
  {
    title: '服务',
    dataIndex: 'serviceName',
    width: 150,
    scopedSlots: {
      filterDropdown: 'filterDropdown',
      filterIcon: 'filterIcon'
    }
  },
  {
    title: 'HTTP请求信息',
    dataIndex: 'httpPath',
    width: 150,
    scopedSlots: { customRender: 'customHttpInfoRender' }
  },
  {
    title: '开始时间',
    dataIndex: 'timestampFormat',
    width: 150,
    sorter: true
  },
  {
    title: '耗时分析',
    dataIndex: '',
    width: 350,
    scopedSlots: { customRender: 'customTimeAnalyRender' }
  },
  {
    title: '用户',
    dataIndex: 'username',
    width: 100,
    scopedSlots: {
      filterDropdown: 'filterDropdown',
      filterIcon: 'filterIcon'
    }
  }
]

// const dataResMock = {
//   totalHits: 100,
//   data: [
//     {
//       traceId: '3cc18da987d55191',
//       id: '3cc18da987d55191',
//       kind: 'SERVER',
//       timestamp: 1606963912007524,
//       timestampFormat: '2020-12-03 10:51:52',
//       duration: 1405997,
//       durationFormat: '1.406s',
//       serviceName: 'gaf-microservice-gateway',
//       ipv4: '10.10.104.96',
//       httpMethod: 'GET',
//       httpPath: '/srv-governance/log',
//       error: null,
//       username: 'sys_admin',
//       rectWidth: 100,
//       rectLeftOffset: 0,
//       children: [
//         {
//           traceId: '3cc18da987d55191',
//           parentId: '3cc18da987d55191',
//           id: 'c2a2bc79ffdd23d3',
//           kind: 'SERVER',
//           timestamp: 1606963912346755,
//           timestampFormat: '2020-12-03 10:51:52',
//           duration: 179764,
//           durationFormat: '180ms',
//           serviceName: 'gaf-authentication',
//           ipv4: '10.10.104.96',
//           httpMethod: 'POST',
//           httpPath: '/authentication/validate',
//           error: null,
//           rectWidth: 20,
//           rectLeftOffset: 20
//         },
//         {
//           traceId: '3cc18da987d55191',
//           parentId: '3cc18da987d55191',
//           id: '4e7e934879f0d44d',
//           kind: 'SERVER',
//           timestamp: 1606963912929398,
//           timestampFormat: '2020-12-03 10:51:52',
//           duration: 472932,
//           durationFormat: '472ms',
//           serviceName: 'gaf-microservice-governance',
//           ipv4: '10.10.104.96',
//           httpMethod: 'GET',
//           httpPath: '/srv-governance/log',
//           error: '测试错误',
//           rectWidth: 40,
//           rectLeftOffset: 60
//         }
//       ]
//     }
//   ]
// }

export default {
  data() {
    return {
      timeRange: [],
      searchParam: {
        serviceName: null,
        username: null,
        startTime: null,
        endTime: null,
        timeSortOrder: 'desc'
      },
      table: {
        columns: columns,
        data: [],
        loading: true,
        pagination: {
          current: 1,
          pageSize: 15,
          showSizeChanger: true,
          total: 0,
          showTotal: total => `总共 ${total} 条数据`
        }
      }
    }
  },
  mounted() {
    this.defaultPicker()
    this.getTrace()
  },
  methods: {
    moment,
    getTimeAnalyStyle(record) {
      if (record.error === null || record === '') {
        return `text-align: center;background-color: #90B1C1;position: absolute;width: ${record.rectWidth}%;left: ${record.rectLeftOffset}%;-webkit-border-radius: 5px;-moz-border-radius: 5px;box-shadow: 0 0 5px #d7d8db;border: 1px solid #dcdfe6;`
      } else {
        return `text-align: center;background-color: #CF7E75;position: absolute;width: ${record.rectWidth}%;left: ${record.rectLeftOffset}%;-webkit-border-radius: 5px;-moz-border-radius: 5px;box-shadow: 0 0 5px #d7d8db;border: 1px solid #dcdfe6;`
      }
    },
    async getTrace() {
      const searchParam = this.searchParam
      searchParam.pageIndex = this.table.pagination.current - 1
      searchParam.pageSize = this.table.pagination.pageSize
      searchParam.startTime = this.timeRange[0].valueOf()
      searchParam.endTime = this.timeRange[1].valueOf()
      const param = qs.stringify(searchParam, { skipNulls: true })
      const url = `/srv-governance/trace?${param}`
      console.log(url)
      const response = await this.$axios.$get(url)
      // this.table.data = dataResMock.data
      // this.table.pagination.total = dataResMock.totalHits

      this.table.data = response.data.data
      this.table.pagination.total = response.data.totalHits

      this.table.loading = false
    },
    onTableChange(pagination, filters, sorter) {
      // console.log('params', pagination, filters, sorter)
      if (typeof sorter.field === 'undefined' || sorter.field === null) {
        this.searchParam.timeSortOrder = 'desc'
      } else if (sorter.order.toLowerCase() === 'ascend') {
        this.searchParam.timeSortOrder = 'asc'
      } else {
        this.searchParam.timeSortOrder = 'desc'
      }
      this.table.pagination = pagination
      this.getTrace()
    },
    handleFilterSearch(selectedKeys, confirm, dataIndex) {
      confirm()
      if (dataIndex === 'serviceName') {
        this.searchParam.serviceName = selectedKeys[0]
      } else if (dataIndex === 'username') {
        this.searchParam.username = selectedKeys[0]
      }
    },
    handleFilterReset(clearFilters, dataIndex) {
      clearFilters()
      if (dataIndex === 'serviceName') {
        this.searchParam.serviceName = null
      } else if (dataIndex === 'username') {
        this.searchParam.username = null
      }
    },
    defaultPicker() {
      const from = moment().subtract(1, 'days')
      const to = moment()
      this.timeRange.length = 0
      this.timeRange.push(from)
      this.timeRange.push(to)
    },
    onPickerChange(date) {
      this.timeRange.length = 0
      this.timeRange.push(...date)
    },
    onPickerOk() {
      this.getTrace()
    }
  }
}
</script>

<style scoped>
.gaf-date-param {
  margin-top: 10px;
  margin-bottom: 10px;
  margin-left: 20px;
}
</style>
