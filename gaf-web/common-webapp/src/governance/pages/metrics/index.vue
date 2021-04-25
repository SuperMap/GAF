<template>
<div class="app-container">
  <div class="page-single">
    <div class="gaf-metrics-param">
      <a-button type="primary"><a-icon type="bars" />Application</a-button>
      <a-select
        :value="currApplication"
        @change="handleChange"
        style="width: 300px"
        placeholder="选择应用"
      >
        <a-select-option
          v-for="item in varApplications"
          :key="item"
          :value="item"
        >
          {{ item }}
        </a-select-option>
      </a-select>
    </div>

    <a-collapse v-model="activeKey" :bordered="false">
      <template #expandIcon="props">
        <a-icon :rotate="props.isActive ? 90 : 0" type="caret-right" />
      </template>
      <a-collapse-panel
        v-for="collapse in collapsePanel"
        :key="collapse.id"
        :style="customStyle"
        :header="collapse.header"
      >
        <div class="flex-iframe">
          <iframe
            v-for="panelId in collapse.panel"
            :key="panelId"
            :src="getIframeSrc(panelId)"
            :height="collapse.height"
            :width="collapse.width"
            frameborder="0"
          ></iframe>
        </div>
      </a-collapse-panel>
    </a-collapse>
  </div>
</div>
</template>

<script>
export default {
  data() {
    return {
      baseUri:
        '/grafana/d-solo/gaf-microservice-jvm/gafwei-fu-wu-jian-kong-da-pan?orgId=1&var-jvm_memory_pool_heap=All&var-jvm_memory_pool_nonheap=All',
      refresh: '30s',
      varApplications: [],
      currApplication: '',
      collapsePanel: [
        {
          id: '1',
          header: '概览',
          panel: ['63', '92', '65', '75'],
          width: '100%',
          height: '100'
        },
        {
          id: '2',
          header: 'HTTP请求指标',
          panel: ['112', '111', '113'],
          width: '100%',
          height: '300'
        },
        {
          id: '3',
          header: '日志输出指标',
          panel: ['91'],
          width: '100%',
          height: '300'
        },
        {
          id: '4',
          header: 'JVM指标',
          panel: ['32', '124'],
          width: '100%',
          height: '300'
        },
        {
          id: '5',
          header: 'JVM 垃圾回收GC',
          panel: ['98', '101', '99'],
          width: '100%',
          height: '300'
        }
      ],
      activeKey: ['1', '2'],
      customStyle:
        'background: #f7f7f7;border-radius: 4px;margin-bottom: 24px;border: 0;overflow: hidden'
    }
  },
  created() {
    this.getApplication()
  },
  methods: {
    async getApplication() {
      const url = '/prometheus/api/v1/label/application/values'
      const response = await this.$axios.$get(url)
      this.varApplications = response.data
      this.currApplication = this.varApplications[0]
    },
    handleChange(value) {
      this.currApplication = value
    },
    getIframeSrc(panelId) {
      return `${this.baseUri}&refresh=${this.refresh}&var-application=${this.currApplication}&panelId=${panelId}`
    }
  }
}
</script>

<style scoped>
.flex-iframe {
  display: flex;
}
.gaf-metrics-param {
  margin-bottom: 10px;
  margin-left: 20px;
  margin-top: 10px;
}
iframe {
  margin-right: 10px;
  margin-bottom: 10px;
  padding: 1px;
  border: thin solid #c0c0c0;
}
</style>
