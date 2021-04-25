<template>
  <div class="container">
    <a-row
      v-for="(config, index) in configs"
      :key="index"
      :gutter="16"
      class="row"
      type="flex"
      justify="space-around"
      align="top"
    >
      <template v-if="config.name === 'grid'">
        <a-col
          v-for="(col, cIndex) in config.columns"
          :key="cIndex"
          :span="col.span"
        >
          <a-card
            :bordered="false"
            :title="getTitle(col.list[0])"
            :style="getStyle(col.list[0].name)"
            class="box-card"
          >
            <component :is="col.list[0].name" :data="col.list[0]" />
          </a-card>
        </a-col>
      </template>
      <template v-else>
        <a-col :span="24">
          <a-card :bordered="false" :title="config.title" class="box-card">
            <component :is="config.name" :data="config" />
          </a-card>
        </a-col>
      </template>
    </a-row>
  </div>
</template>

<script>
import '../configInfo/componentAll.js'
export default {
  name: 'WithLogin',
  components: {},
  props: {},
  data() {
    return {}
  },
  computed: {
    configs() {
      return JSON.parse(this.$store.state.config.configInfo)
    },
  },
  watch: {},
  created() {},
  mounted() {},
  methods: {
    getTitle(item) {
      return item.name === 'PlatQuickLink' ? '' : item.title
    },
    getStyle(name) {
      return `text-align:${name === 'PlatQuickLink' ? 'center' : 'left'}`
    },
  },
}
</script>

<style scoped lang="less">
.container {
  margin: 15px 15px 0;
}
.row {
  margin-bottom: 45px;
}
</style>
