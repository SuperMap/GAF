<template>
  <gaf-detail>
    <template>
      <a-breadcrumb separator=">">
        <span class="vertical-line">| </span>
        <a-breadcrumb-item class="text-bolder">添加路由</a-breadcrumb-item>
        <!-- <a-breadcrumb-item>
          <a @click="$emit('back')">路由列表</a>
        </a-breadcrumb-item>
        <a-breadcrumb-item>添加路由</a-breadcrumb-item> -->
      </a-breadcrumb>
    </template>
    <routeForm v-model="newRouteInfo"></routeForm>
    <div>
      <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
        <a-row style="margin-top: 10px">
          <a-col :span="24">
            <div style="text-align: center;">
              <a-button
                @click="addGatewayRoute"
                type="primary"
                html-type="submit"
                class="submit-gray"
              >
                确定
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
import RouteForm from './RouteForm'

export default {
  name: 'AddRoute',
  components: { routeForm: RouteForm },
  model: {
    prop: 'visible',
    event: 'close'
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data: function() {
    return {
      newRouteInfo: {
        id: '',
        enable: true,
        createTime: '',
        updateTime: '',
        routeId: '',
        uri: '',
        order: 0,
        predicates: [],
        filters: [],
        uriAfter: '',
        uriBefore: 'lb://'
      }
    }
  },
  methods: {
    async addGatewayRoute() {
      if (!this.newRouteInfo.uriAfter || !this.newRouteInfo.routeId){
        this.$message.error(`新增失败,原因:服务名或路由ID未填`)
      } else {
        this.newRouteInfo.uri =
        this.newRouteInfo.uriBefore + this.newRouteInfo.uriAfter
      const addRst = await this.$axios.post(
        '/srv-governance/routes',
        this.newRouteInfo
      )
      if (addRst) {
        if (addRst.data.isSuccessed) {
          this.$message.success(addRst.data.message)
          this.$emit('refreshData')
          this.$emit('cancel')
        } else {
          this.$message.error(addRst.data.message)
        }
      }
      }
    }
  }
}
</script>

<style scoped></style>
