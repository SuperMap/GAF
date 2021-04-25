<template>
  <gaf-detail>
    <template>
      <a-breadcrumb separator=">">
        <span class="vertical-line">| </span>
        <a-breadcrumb-item class="text-bolder">路由详情</a-breadcrumb-item>
        <!-- <a-breadcrumb-item>
          <a @click="$emit('back')">路由列表</a>
        </a-breadcrumb-item>
        <a-breadcrumb-item>路由详情</a-breadcrumb-item> -->
      </a-breadcrumb>
    </template>
    <routeForm v-model="routeInfo" />
    <div>
      <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
        <a-row style="margin-top: 10px">
          <a-col :span="24">
            <div style="text-align: center;">
              <a-button
                @click="submitGatewayRoute"
                type="primary"
                html-type="submit"
                class="submit-gray"
              >
                提交
              </a-button>
              <a-button @click="$emit('cancel')" type="primary" class="cancel-modal">
                取消
              </a-button>
            </div>
          </a-col>
        </a-row>
      </a-form-item>
    </div>
  </gaf-detail>
</template>

<script>
import AFormItem from 'ant-design-vue/es/form/FormItem'
import ARow from 'ant-design-vue/es/grid/Row'
import ACol from 'ant-design-vue/es/grid/Col'
import RouteForm from './RouteForm'

const columns = [
  {
    title: '名称',
    width: 150,
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
export default {
  name: 'Index',
  components: { ACol, ARow, AFormItem, RouteForm },
  model: {
    prop: 'routeInfo',
    event: 'close'
  },
  props: {
    routeInfo: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      loading: false,
      visible: false,
      routeType: '',
      columns: columns,
      filtersData: [],
      predicatesData: [],
      selectedRowKeys: []
    }
  },
  watch: {
    predicatesData: {
      handler: function(val, oldVal) {
        console.log(val, oldVal)
      },
      deep: true
    },
    routeInfo: {
      handler: function(val) {
        console.log(val.uriAfter)
      },
      immediate: true
    }
  },
  mounted: function() {
    // this.getRoute()
  },
  methods: {
    async submitGatewayRoute() {
      if (!this.routeInfo.uriAfter || !this.routeInfo.routeId){
        this.$message.error(`提交失败,原因:服务名或路由ID未填`)
      } else {
        this.routeInfo.uri = this.routeInfo.uriBefore + this.routeInfo.uriAfter
        this.routeInfo.createTime = new Date(this.routeInfo.createTime).getTime()
        const updateRst = await this.$axios.put(
          '/srv-governance/routes',
          this.routeInfo
        )
        if (updateRst && updateRst.data) {
          if (updateRst.data.isSuccessed) {
            this.routeInfo = updateRst.data.data
            this.$message.success(updateRst.data.message)
            this.$emit('refreshData')
          } else {
            this.$message.error(updateRst.data.message)
          }
        }
      }
      
    },
    cancle() {
      this.$router.push({
        path: '/gateway'
      })
    },
    async getRoute() {
      this.loading = true
      const _id = this.$route.params.id
      const result = await this.$axios.get('/srv-governance/routes/' + _id)
      if (!result.data.isSuccessed) {
        this.$message.error(result.data.message)
      }
      this.routeInfo = result.data.data
      this.loading = false
      this.formatFiltersData()
    },
    changeType(type) {
      this.routeInfo.type = type
    },
    formatFiltersData() {
      if (!this.routeInfo.filters) {
        return
      }
      this.routeInfo.filters.forEach(f => {
        this.filtersData.push({
          name: f.name,
          args: f.args
        })
      })
      this.routeInfo.predicates.forEach(p => {
        this.predicatesData.push({
          name: p.name,
          args: p.args
        })
      })
    }
  }
}
</script>

<style scoped>
.editable-row-operations a {
  margin-right: 8px;
}
</style>
