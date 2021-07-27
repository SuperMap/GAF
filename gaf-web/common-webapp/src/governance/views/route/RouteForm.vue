<template>
  <div>
    <a-form layout="horizontal">
      <div style="margin: 10px 20px 10px 30px;">
        <div class="border">
          <a-form-item
            :label-col="{ span: 3 }"
            :wrapper-col="{ span: 19 }"
            label="路由ID"
          >
            <a-input v-model="routeInfo.routeId" placeholder="请输入路由ID" />
          </a-form-item>
          <a-form-item
            :label-col="{ span: 3 }"
            :wrapper-col="{ span: 19 }"
            label="服务名"
          >
            <div>
              <a-input-group compact>
                <a-input
                  v-model="routeInfo.uriAfter"
                  placeholder="请输入服务名"
                >
                  <a-select
                    slot="addonBefore"
                    v-model="routeInfo.uriBefore"
                    :default-value="
                      Object.keys(routeInfo.uriBefore).length == 0
                        ? '请选择'
                        : routeInfo.uriBefore
                    "
                    style="width: 90px"
                  >
                    <a-select-option value="lb://">lb://</a-select-option>
                    <a-select-option value="http://">http://</a-select-option>
                    <a-select-option value="https://">https://</a-select-option>
                  </a-select>
                </a-input>
              </a-input-group>
            </div>
          </a-form-item>
          <a-form-item
            :label-col="{ span: 3 }"
            :wrapper-col="{ span: 19 }"
            label="过滤器信息"
          >
            <a-table
              :columns="columns"
              :data-source="routeInfo.filters"
              :pagination="false"
              :scroll="{ y: routeInfo.filters.length > 3 ? 150 : 0 }"
              bordered
              size="middle"
              row-key="filters"
            >
              <template
                v-for="col in ['name', 'args']"
                :slot="col"
                slot-scope="text, record"
              >
                <div v-if="col === 'name'" :key="col">
                  <a-select
                    :default-value="text"
                    @change="
                      e =>
                        filterOrPredicateHandleChange(
                          routeInfo.filters,
                          e,
                          record,
                          col
                        )
                    "
                  >
                    <template v-for="f in filterTypes">
                      <a-select-option :key="f" :value="f">{{
                        f
                      }}</a-select-option>
                    </template>
                  </a-select>
                </div>
                <div v-else :key="col">
                  <a-input
                    :key="col"
                    :value="text"
                    @change="
                      e =>
                        filterOrPredicateHandleChange(
                          routeInfo.filters,
                          e.target.value,
                          record,
                          col
                        )
                    "
                    placeholder="请输入参数信息"
                    style="margin: -5px 0"
                  />
                </div>
              </template>
              <template slot="operation" slot-scope="text, record">
                <div class="editable-row-operations">
                  <span slot="operation" class="table-operation" width="300">
                    <a
                      @click="removeRow(routeInfo.filters, record)"
                      size="small"
                    >
                      <u>删除</u>
                    </a>
                  </span>
                </div>
              </template>
            </a-table>
            <a-button
              v-show="isadd1"
              @click="addRow(routeInfo.filters), changeisadd1()"
              type="default"
              >添加</a-button
            >
          </a-form-item>
          <a-form-item
            :label-col="{ span: 3 }"
            :wrapper-col="{ span: 19 }"
            label="断言信息"
          >
            <a-table
              :columns="columns"
              :data-source="routeInfo.predicates"
              :pagination="false"
              :scroll="{ y: routeInfo.predicates.length > 3 ? 150 : 0 }"
              bordered
              size="middle"
              row-key="predicates"
            >
              <template
                v-for="col in ['name', 'args']"
                :slot="col"
                slot-scope="text, record"
              >
                <div v-if="col === 'name'" :key="col">
                  <a-select
                    :default-value="text"
                    @change="
                      e =>
                        filterOrPredicateHandleChange(
                          routeInfo.predicates,
                          e,
                          record,
                          col
                        )
                    "
                  >
                    <template v-for="p in predicateTypes">
                      <a-select-option :key="p" :value="p">{{
                        p
                      }}</a-select-option>
                    </template>
                  </a-select>
                </div>
                <div v-else :key="col">
                  <a-input
                    :key="col"
                    :v-model="routeInfo.predicates[col]"
                    :placeholder="col"
                    :value="text"
                    @change="
                      e =>
                        filterOrPredicateHandleChange(
                          routeInfo.predicates,
                          e.target.value,
                          record,
                          col
                        )
                    "
                    style="margin: -5px 0"
                  />
                </div>
              </template>
              <template slot="operation" slot-scope="text, record">
                <div class="editable-row-operations">
                  <span slot="operation" class="table-operation" width="300">
                    <a
                      @click="removeRow(routeInfo.predicates, record)"
                      size="small"
                    >
                      <u>删除</u>
                    </a>
                  </span>
                </div>
              </template>
            </a-table>
            <a-button
              v-show="isadd2"
              @click="addRow(routeInfo.predicates), changeisadd2()"
              type="default"
              >添加</a-button
            >
          </a-form-item>
          <a-form-item
            :label-col="{ span: 3 }"
            :wrapper-col="{ span: 19 }"
            label="是否启用"
          >
            <a-switch v-model="routeInfo.enable" @click="enableChange" />
          </a-form-item>
        </div>
      </div>
    </a-form>
  </div>
</template>

<script>
const columns = [
  {
    title: '名称',
    width: 200,
    dataIndex: 'name',
    key: 'name',
    scopedSlots: { customRender: 'name' }
  },
  {
    title: '参数详情',
    dataIndex: 'args',
    key: 'args',
    scopedSlots: { customRender: 'args' }
  },
  {
    title: '操作',
    width: 200,
    dataIndex: 'operation',
    key: 'operation',
    scopedSlots: { customRender: 'operation' }
  }
]
const filterTypes = ['StripPrefix']
const predicateTypes = ['Path']
export default {
  name: 'RouteForm',
  model: {
    prop: 'routeInfo',
    event: 'change'
  },
  props: {
    routeInfo: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      columns: columns,
      filterTypes: filterTypes,
      predicateTypes: predicateTypes
    }
  },
  watch: {
    routeInfo: {
      handler: function(v) {
        console.log(v)
      },
      deep: true
    }
  },
  mounted() {
    this.addRow(this.routeInfo.predicates)
    this.addRow(this.routeInfo.filters)
  },
  computed: {
    isadd1: function() {
      return this.routeInfo.filters.length === 0
    },
    isadd2: function() {
      return this.routeInfo.predicates.length === 0
    }
  },
  methods: {
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text
          .toLowerCase()
          .indexOf(input.toLowerCase()) >= 0
      )
    },
    enableChange() {
      // this.routeInfo.enable = Boolean(this.routeInfo.enable)
    },
    addRow(obj) {
      if (obj === this.routeInfo.predicates) {
        obj.push({
          id: obj.length + 1,
          name: 'Path',
          args: ''
        })
      }
      if (obj === this.routeInfo.filters) {
        obj.push({
          id: obj.length + 1,
          name: 'StripPrefix',
          args: ''
        })
      }
    },
    changeisadd1() {
      this.isadd1 = !this.isadd1
    },
    changeisadd2() {
      this.isadd2 = !this.isadd2
    },
    removeRow(obj, record) {
      const idx = obj.findIndex(o => o.id === record.id)
      obj.splice(idx, 1)
    },
    chnageUri(text) {
      this.routeInfo.uri = 'lb://' + text
    },
    filterOrPredicateHandleChange(datas, value, record, column) {
      const newData = { id: record.id, name: record.name, args: record.args }
      newData[column] = value
      const idx = datas.findIndex(p => p.id === record.id)
      datas.splice(idx, 1, newData)
    },
    changeType(type) {
      this.routeInfo.type = type
    },
    handleChange(e) {
      console.log('click left button', e)
    }
  }
}
</script>

<style scoped></style>
