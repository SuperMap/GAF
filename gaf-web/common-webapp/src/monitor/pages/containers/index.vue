<template>
  <div>
    <div class="gaf-containers-param">
      <a-button type="primary"><a-icon type="bars" />Node</a-button>
      <a-select
        :value="currNode"
        @change="handleChange"
        style="width: 300px"
        placeholder="选择节点"
      >
        <a-select-option v-for="item in varNode" :key="item" :value="item">
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
</template>

<script>
export default {
  data() {
    return {
      baseUri:
        '/grafana/d-solo/_L83rMxGz/gafrong-qi-jian-kong?orgId=1&var-job=container',
      refresh: '30s',
      varNode: [],
      currNode: '',
      collapsePanel: [
        {
          id: '1',
          header: '概览',
          panel: ['7', '5', '6'],
          width: '100%',
          height: '100'
        },
        {
          id: '2',
          header: 'CPU',
          panel: ['2'],
          width: '100%',
          height: '300'
        },
        {
          id: '3',
          header: '内存',
          panel: ['1'],
          width: '100%',
          height: '300'
        },
        {
          id: '4',
          header: '网络',
          panel: ['3', '4'],
          width: '100%',
          height: '300'
        },
        {
          id: '5',
          header: 'IO',
          panel: ['8', '9'],
          width: '100%',
          height: '300'
        }
      ],
      activeKey: ['1', '2', '3', '4', '5'],
      customStyle:
        'background: #f7f7f7;border-radius: 4px;margin-bottom: 24px;border: 0;overflow: hidden'
    }
  },
  created() {
    this.getNode()
  },
  methods: {
    async getNode() {
      const url =
        '/prometheus/api/v1/query?query=count by (instance) (container_cpu_user_seconds_total)'
      const response = await this.$axios.$get(url)
      this.varNode = response.data.result.map(item => item.metric.instance)
      this.currNode = this.varNode[0]
    },
    handleChange(value) {
      this.currNode = value
    },
    getIframeSrc(panelId) {
      return `${this.baseUri}&refresh=${this.refresh}&var-node=${this.currNode}&panelId=${panelId}`
    }
  }
}
</script>

<style scoped>
.flex-iframe {
  display: flex;
}
.gaf-containers-param {
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
