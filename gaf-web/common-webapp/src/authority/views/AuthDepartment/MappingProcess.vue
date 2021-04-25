<template>
  <div class="mapping-box">
    <div id="mappingBox">
      <a-table :columns="columns" :data-source="tenantRst" :pagination="false">
        <template slot="mappingMethod" slot-scope="text, record">
          <div v-if="record.mappingMethod == 1">
            <span>推</span>
          </div>
          <div v-if="record.mappingMethod == 2">
            <span>拉</span>
          </div>
        </template>
        <template slot="mapped" slot-scope="text, record">
          <div v-if="record.mapped === false">
            <span>否</span>
          </div>
          <div v-else-if="record.mapped === true">
            <span>是</span>
          </div>
        </template>
        <template slot="mappingMethodBtn" slot-scope="text, record">
          <div v-if="record.mapped === false">
            <div v-if="record.mappingMethod == 2">
              <a-button
                @click.stop="() => toMapped(record)"
                v-model="record.data"
                class="mapping-btn"
              >
                <span>去映射</span>
              </a-button>
            </div>
            <div v-if="record.mappingMethod == 1">
              <a-button
                @click.stop="() => mapping(record)"
                v-model="record.data"
                class="mapping-btn"
              >
                <span>映射</span>
              </a-button>
            </div>
          </div>
          <div v-else-if="record.mapped === true"></div>
        </template>
      </a-table>
    </div>
    <div id="searchBox" :isdisabled="isdisabled">
      <div v-if="isdisabled === true">
        <a-table
          :columns="searchColumns"
          :data-source="toMapData"
          :pagination="false"
        >
          <template slot="mappingMethod" slot-scope="text, record">
            <a-button
              @click.stop="() => doneMapping(record)"
              v-model="record.data"
              class="mapping-btn"
            >
              <span>映射</span>
            </a-button>
          </template>
        </a-table>
      </div>
      <div v-else-if="isdisabled === true"></div>
    </div>
  </div>
</template>
<script>
import '~/static/css/style.css'
const searchColumns = [
  {
    title: '序号',
    dataIndex: 'id',
    key: 'id',
    ellipsis: true
  },
  {
    title: '名称',
    dataIndex: 'name',
    key: 'name',
    ellipsis: true
  },
  {
    title: '操作',
    dataIndex: 'mappingMethod',
    key: 'mappingMethod',
    ellipsis: true,
    scopedSlots: { customRender: 'mappingMethod' }
  }
]
const columns = [
  {
    title: '规则名称',
    dataIndex: 'mappingRuleName',
    key: 'mappingRuleName'
  },
  {
    title: '选择要同步的组件',
    dataIndex: 'p3ComponentName',
    key: 'p3ComponentName',
    scopedSlots: { customRender: 'p3ComponentId' }
  },
  {
    title: '同步方式',
    dataIndex: 'mappingMethod',
    key: 'mappingMethod',
    ellipsis: true,
    scopedSlots: { customRender: 'mappingMethod' }
  },
  {
    title: '是否已映射',
    dataIndex: 'mapped',
    key: 'mapped',
    ellipsis: true,
    scopedSlots: { customRender: 'mapped' }
  },
  {
    title: '操作',
    dataIndex: 'mappingMethod',
    key: 'mappingMethod',
    ellipsis: true,
    scopedSlots: { customRender: 'mappingMethodBtn' }
  }
]
const dataSearch = [
  {
    id: '1',
    choose: '',
    index: 'index',
    hide: 'hide',
    TenantName: 'TenantName'
  }
]
const data = [
  {
    key: '1',
    components: 'SinfCloud BPM',
    mode: '拉',
    whether: '否',
    mapping: '去映射'
  }
]
export default {
  props: {
    tenantRst: {
      type: Array,
      default: () => []
    },
    MapDataId: {
      type: String,
      default: () => ''
    }
  },
  data() {
    return {
      isdisabled: false,
      columns,
      searchColumns,
      data,
      dataSearch,
      tomaping: {}
    }
  },
  methods: {
    // 映射   first
    async mapping(e) {
      this.isdisabled = false
      const ruleId = e.mappingRuleId
      const url = `/authority/auth-p3-sync-mapping`
      const res = await this.$axios.post(url, {
        ruleId: ruleId,
        gafResourceId: this.MapDataId
      })
      const mappingData = res.data
      if (mappingData.isSuccessed) {
        this.$message.success('同步成功')
      } else {
        this.$message.warn(mappingData.message)
      }
    },
    // 映射  second
    async doneMapping() {
      const p3TenantId = this.toMapData[0].id
      const p3ComponentId = this.tomaping.p3ComponentId
      const url = `/authority/auth-p3-tenant-mapping`
      const res = await this.$axios.post(url, {
        tenantId: '',
        p3TenantId: p3TenantId,
        p3ComponentId: p3ComponentId
      })
      // eslint-disable-next-line no-console
      console.log(res.data)
      if (res.data.isdisabled) {
        this.$message.success('映射成功')
        this.isdisabled = true
      } else {
        this.$message.warn(res.data.message)
      }
    },
    // 去映射
    async toMapped(m) {
      this.tomaping = m
      const ruleId = m.mappingRuleId
      const url = `/authority/auth-p3-sync-mapping/`
      const res = await this.$axios.post(url, { ruleId: ruleId })
      const toMapRes = res.data
      if (toMapRes.isSuccessed) {
        this.isdisabled = true
      } else {
        this.$message.warn(toMapRes.message)
      }
      this.toMapData = toMapRes.data
    },
    onSearch() {
      // console.log(value)
    }
  }
}
</script>
