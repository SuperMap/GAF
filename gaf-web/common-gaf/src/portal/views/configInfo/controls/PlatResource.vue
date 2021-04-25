<template>
  <iframe :src="grafanaUrl" width="100%" height="200" :style="style" frameborder="0" />
</template>

<script>
export default {
  name: 'PlatResource',
  data() {
    return {
      grafanaUrl:'/grafana/d/oWe9aYxmx/gafshou-ye-ding-zhi-jian-kong?refresh=30s&orgId=1&kiosk',
      style: null
    }
  },
  props: {
    data: {
      type: Object,
      required: true,
    },
  },
   mounted() {
    this.initStyle()
    this.initSrc()
  },
  methods:{
    initStyle() {
      const item = this.data
      let style = null
      let width = null
      let height = null
      if(item.width && item.width !== '') {
         width = item.width
         style += `;width:${width}px`
      }
      if(item.height && item.height !== '') {
         height = item.height
         style += `;height:${height}px;`
      }
      if(style) {
        this.style = style
      }
    },
    async initSrc() {
        try {
        const { data, successed } = await this.$axios.$get(
          '/portal/env/GRAFANA_URL'
        )
        if (successed && data) {
          if (data.endsWith('/')) {
            this.grafanaUrl = data.substr(0, data.length - 1) + this.grafanaUrl
          } else {
            this.grafanaUrl = data + this.grafanaUrl
          }
        }
      } catch {
        //
      }
    }
  }
}
</script>

<style scoped></style>
