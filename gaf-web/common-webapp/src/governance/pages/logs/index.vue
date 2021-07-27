<template>
<div class="app-container">
  <div class="page-single">
    <div class="gaf-date-param">
      <a-row
        type="flex"
        justify="start"
        align="middle"
        style="margin-bottom: inherit"
      >
        <a-col>
          <label>自动刷新</label>
          <a-switch
            @change="onSwitchChange"
            checked-children="开启"
            un-checked-children="关闭"
          />
        </a-col>
        <a-col v-show="autoRefresh" offset="1">
          <a-select @change="onFrequencyChange" default-value="60">
            <a-select-option v-for="o in frequencySelect" :key="o" :value="o">
              {{ o.toUpperCase() }}
            </a-select-option>
          </a-select>
          <label>秒</label>
        </a-col>
      </a-row>
      <a-row type="flex" justify="start" align="middle">
        <a-col style="margin-right: 5px;">
          <label>时间选择</label>
          <a-select @change="onPastTimeChange" default-value="1天">
            <a-select-option v-for="o in pastTimeSelect" :key="o" :value="o">
              {{ o.toUpperCase() }}
            </a-select-option>
          </a-select>
        </a-col>
        <a-col>
          <a-range-picker
            :show-time="{ format: 'HH:mm:ss' }"
            :value="timeRange"
            @change="onChange"
            @ok="onOk"
            style="width:400px"
            format="YYYY-MM-DD HH:mm:ss"
          />
        </a-col>
      </a-row>
    </div>
    <div class="log-content">
      <div>
        <a-table
          :row-key="record => record.id"
          @change="onTableChange"
          :columns="table.columns"
          :data-source="table.data"
          :pagination="table.pagination"
          :loading="table.loading"
          size="small"
        >
          <div
            slot="filterDropdown"
            slot-scope="{
              setSelectedKeys,
              selectedKeys,
              confirm,
              clearFilters,
              column
            }"
            class="custom-filter-dropdown"
          >
            <a-input
              v-ant-ref="c => (searchInput = c)"
              :placeholder="`查询${column.title}`"
              :value="selectedKeys[0]"
              @change="
                e => setSelectedKeys(e.target.value ? [e.target.value] : [])
              "
              @pressEnter="
                () => handleSearch(selectedKeys, column.dataIndex, confirm)
              "
              style="width: 188px; margin-bottom: 8px; display: block;"
            />
            <a-button
              @click="
                () => handleSearch(selectedKeys, column.dataIndex, confirm)
              "
              type="primary"
              icon="search"
              size="small"
              style="width: 90px; margin-right: 8px"
              >查询</a-button
            >
            <a-button
              @click="() => handleReset(column.dataIndex, clearFilters)"
              size="small"
              style="width: 90px"
              >重置</a-button
            >
          </div>
          <a-icon
            slot="filterIcon"
            slot-scope="filtered"
            :style="{ color: filtered ? '#108ee9' : undefined }"
            type="search"
          />
          <span slot="levelRender" slot-scope="level">
            <a-tag :color="level === 'INFO' ? 'green' : 'volcano'">{{
              level
            }}</a-tag>
          </span>
          <span slot="messageRender" slot-scope="message, record">
            <a-popover placement="bottomLeft" title="日志详情">
              <template slot="content">
                <a-tag style="display:block">{{ record.logger }}</a-tag>
                <span style="font-size:5px">{{ message }}</span>
              </template>
              {{ message }}
            </a-popover>
          </span>
        </a-table>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import moment from 'moment'
import qs from 'qs'

export default {
  data() {
    return {
      timeRange: [],
      autoRefresh: false,
      frequency: 60,
      searchText: '',
      searchInput: null,
      search: {
        application: null,
        level: null,
        timeSortOrder: 'desc',
        startTime: null,
        endTime: null
      },
      intervalSelect: ['1m', '30m', '1h', '2h', '5h', '1d', '1w'],
      frequencySelect: ['1', '5', '10', '30', '60', '100', '200'],
      pastTimeSelect: ['1小时', '2小时', '5小时', '10小时', '1天', '1周'],
      table: {
        columns: [
          {
            title: '时间',
            dataIndex: 'timestamp',
            width: 150,
            align: 'center',
            sorter: true
          },
          {
            title: '日志级别',
            dataIndex: 'level',
            filters: [
              { text: 'INFO', value: 'INFO' },
              { text: 'DEBUG', value: 'DEBUG' },
              { text: 'WARN', value: 'WARN' },
              { text: 'ERROR', value: 'ERROR' }
            ],
            filterMultiple: true,
            width: 150,
            align: 'center',
            scopedSlots: {
              customRender: 'levelRender'
            }
          },
          {
            title: '服务名称',
            dataIndex: 'application',
            scopedSlots: {
              filterDropdown: 'filterDropdown',
              filterIcon: 'filterIcon'
            },
            onFilterDropdownVisibleChange: visible => {
              if (visible) {
                setTimeout(() => {
                  this.searchInput.focus()
                }, 0)
              }
            },
            width: 200
          },
          {
            title: '日志信息',
            dataIndex: 'message',
            scopedSlots: {
              customRender: 'messageRender'
            },
            ellipsis: true
          }
        ],
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
    this.searchServiceLog()
    this.start(this.frequency * 1000)
  },
  methods: {
    moment,
    onChange(d) {
      console.log(d)
      this.timeRange.length = 0
      this.timeRange.push(...d)
    },
    onOk(d) {
      console.log(d)
      this.searchServiceLog()
    },
    onSwitchChange(d) {
      console.log(d)
      if (d) {
        this.autoRefresh = true
        const now = this.moment()
        this.timeRange[1] = now
        this.searchServiceLog()
        this.start(parseInt(this.frequency) * 1000)
      } else {
        this.autoRefresh = false
        this.pause()
      }
    },
    onFrequencyChange(o) {
      // console.log(o)
      this.frequency = o
      if (this.autoRefresh) {
        this.pause()
        const now = this.moment()
        this.timeRange[1] = now
        this.searchServiceLog()
        this.start(parseInt(this.frequency) * 1000)
      }
    },
    onPastTimeChange(o) {
      // console.log(o)
      let value
      let unit
      switch (o) {
        case '1小时':
          value = 1
          unit = 'hours'
          break
        case '2小时':
          value = 2
          unit = 'hours'
          break
        case '5小时':
          value = 5
          unit = 'hours'
          break
        case '10小时':
          value = 10
          unit = 'hours'
          break
        case '1天':
          value = 1
          unit = 'days'
          break
        case '1周':
          value = 1
          unit = 'weeks'
          break
        default:
          value = 1
          unit = 'days'
      }

      const from = moment().subtract(value, unit)
      const now = this.moment()
      this.timeRange.length = 0
      this.timeRange.push(from)
      this.timeRange.push(now)
      this.searchServiceLog()
    },
    async searchServiceLog() {
      let searchRes
      try {
        const param = this.search
        param.pageIndex = this.table.pagination.current - 1
        param.pageSize = this.table.pagination.pageSize
        param.startTime = this.timeRange[0].valueOf()
        param.endTime = this.timeRange[1].valueOf()
        const searchParam = qs.stringify(param, { skipNulls: true })
        const url = `/srv-governance/log?${searchParam}`
        searchRes = await this.$axios.$get(url)
      } catch (e) {
        // console.log(e)
      }
      if (typeof searchRes !== 'undefined') {
        this.table.data = searchRes.data.fluentLogs
        this.table.pagination.total = searchRes.data.hitsTotal
      }
      this.table.loading = false
    },
    defaultPicker() {
      const from = moment().subtract(1, 'days')
      const to = moment()
      this.timeRange.length = 0
      this.timeRange.push(from)
      this.timeRange.push(to)
      // return { gte: from.valueOf(), lte: to.valueOf() }
    },
    onTableChange(pagination, filters, sorter) {
      // console.log('params', pagination, filters, sorter)
      if (
        typeof filters.level === 'undefined' ||
        filters.level === null ||
        filters.level.length === 0
      ) {
        this.search.level = null
      } else {
        this.search.level = filters.level.join(' ')
      }
      if (
        typeof filters.application === 'undefined' ||
        filters.application === null ||
        filters.application.length === 0
      ) {
        this.search.application = null
      } else {
        this.search.application = filters.application[0]
      }
      if (typeof sorter.field === 'undefined' || sorter.field === null) {
        this.search.timeSortOrder = 'desc'
      } else if (sorter.order.toLowerCase() === 'ascend') {
        this.search.timeSortOrder = 'asc'
      } else {
        this.search.timeSortOrder = 'desc'
      }
      this.table.pagination = pagination
      this.searchServiceLog()
    },
    handleSearch(selectedKeys, dataIndex, confirm) {
      confirm()
      this.searchText = selectedKeys[0]
      if (dataIndex === 'application') {
        this.search.application = selectedKeys[0]
      }
    },
    handleReset(dataIndex, clearFilters) {
      clearFilters()
    },
    pause() {
      clearInterval(this._timer)
      this._timer = null
    },
    start(interval) {
      this._timer = setInterval(() => {
        const now = this.moment()
        const from = this.timeRange[0]
        const to = this.timeRange[1]
        if (now.valueOf() - to.valueOf() < interval + 1000) {
          // 结束时间距离当前时间 1m 之内，刷新到当前时间
          this.timeRange.length = 0
          this.timeRange.push(from)
          this.timeRange.push(now)
          this.searchServiceLog()
        }
      }, interval)
    }
  }
}
</script>

<style scoped>
.log-content {
  padding: 0 15px;
}

.display-row label {
  width: 90px;
  overflow: hidden;
}
.custom-filter-dropdown {
  padding: 8px;
  border-radius: 4px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.highlight {
  background-color: rgb(255, 192, 105);
  padding: 0px;
}

.col-sql {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: inline-block;
  width: 750px;
}
.gaf-date-param {
  margin-top: 10px;
  margin-bottom: 10px;
  margin-left: 20px;
}
</style>
