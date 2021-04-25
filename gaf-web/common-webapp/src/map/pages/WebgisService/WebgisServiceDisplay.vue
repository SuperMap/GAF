<template>
  <div>
    <gaf-table-no-page
      :show-x-h="false"
      :row-key="(r, i) => i.toString()"
      :data-source="webgisServiceList"
      :columns="columns"
      bordered
      size="small"
      :locale="{
        filterConfirm: '确定',
        filterReset: '重置',
        emptyText: '请选择一个服务',
      }"
    >
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
      <template slot="timeRender" slot-scope="text">
        {{ timeFormat(text) }}
      </template>
      <template slot="type" slot-scope="text">
        {{ webgisServiceTypes.get(text) ? webgisServiceTypes.get(text) : text }}
      </template>
    </gaf-table-no-page>
  </div>
</template>
<script>
// @ts-nocheck
export default {
  props: {
    // 选中的gis服务id
    webgisServiceList: {
      type: Array,
      default: () => [],
    },
    webgisServiceTypes: {
      type: Map,
      default: () => new Map(),
    },
  },
  data() {
    return {
      columns: [
        {
          title: '服务名称',
          dataIndex: 'name',
          key: 'name',
        },
        {
          title: '服务地址',
          dataIndex: 'address',
          key: 'address',
          scopedSlots: { customRender: 'address' },
          width: '550px',
        },
        {
          title: '服务类型',
          dataIndex: 'typeCode',
          key: 'type_code',
          scopedSlots: { customRender: 'type' },
        },
        {
          title: '时态',
          dataIndex: 'timeAttribute',
          key: 'time_attribute',
          scopedSlots: { customRender: 'timeRender' },
        },
        {
          title: '注册时间',
          dataIndex: 'createdTime',
          key: 'created_time',
          scopedSlots: { customRender: 'timeRender' },
        },
      ],
    }
  },
  computed: {},
  created() {},
  methods: {
    timeFormat(str) {
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
    },
  },
}
</script>

<style scoped>
.url {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 550px;
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
