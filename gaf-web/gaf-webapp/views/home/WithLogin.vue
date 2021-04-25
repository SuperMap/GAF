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
          <template v-for="(c, idx) in col.list">
            <a-card
              :key="c.id"
              :bordered="false"
              :title="getTitle(c)"
              :style="getStyle(c.name)"
              class="box-card"
            >
              <a v-if="c.isShowMore" slot="extra" href="#" @click="openHref(c)"
                >更多</a
              >
              <component :is="c.name" :key="idx" :data="c" />
            </a-card>
          </template>
        </a-col>
      </template>
      <template v-else-if="config.name === 'gridTable'">
        <table
          class="table-layout div-table"
          :class="{
            bright: config.options.bright,
            small: config.options.small,
            bordered: config.options.bordered,
          }"
          :style="config.options.customStyle"
        >
          <tr v-for="(trItem, trIndex) in config.trs" :key="trIndex">
            <td
              v-for="(tdItem, tdIndex) in trItem.tds"
              :key="tdIndex"
              class="table-td"
              :colspan="tdItem.colspan"
              :rowspan="tdItem.rowspan"
            >
              <transition-group
                :key="tdIndex"
                tag="div"
                type="transition"
                class="list-main"
              >
                <template v-for="(l, idx) in tdItem.list">
                  <a-card
                    :key="l.id"
                    :bordered="false"
                    :title="getTitle(l)"
                    :style="getStyle(l.name)"
                    class="box-card"
                  >
                    <a
                      v-if="l.isShowMore"
                      slot="extra"
                      href="#"
                      @click="openHref(l)"
                      >更多</a
                    >
                    <component
                      :is="l.name"
                      :key="idx"
                      :data="l"
                      :style="setWidthHeight(l)"
                    />
                  </a-card>
                </template>
              </transition-group>
              <!-- <component
                :is="tdItem.list[tdIndex].name"
                :data="tdItem.list[tdIndex]"
              /> -->
              <!-- <sortable-control-item
            :key="idx"
            :data="l"
            @delete="tdItem.list.splice(idx, 1)"
          /> -->
            </td>
          </tr>
        </table>
      </template>
      <template v-else>
        <a-col :span="24">
          <a-card :bordered="false" :title="config.title" class="box-card">
            <a
              v-if="config.isShowMore"
              slot="extra"
              href="#"
              @click="openHref(config)"
              >更多</a
            >
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
    openHref(item) {
      // const toUrl = this.$router.resolve({ path: this.href, query: { id: "1" } })
      if (item && item.href) {
        const toUrl = this.$router.resolve({ path: item.href })
        window.open(toUrl.href, '_blank')
      }
    },
    setWidthHeight(item) {
      let style = null
      let width = null
      let height = null
      if (item.width && item.width !== '') {
        width = item.width
        style += `;width:${width}px`
      }
      if (item.height && item.height !== '') {
        height = item.height
        style += `;height:${height}px;`
      }
      return style
    },
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
.div-table {
  width: 100%;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
}
.div-table,
.div-table tr,
.div-table tr td {
  -webkit-transition: all 0.3s;
  transition: all 0.3s;
  border-collapse: collapse;
}
.div-table tr td {
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  padding: 12px 12px;
  vertical-align: top;
}
.div-table.bordered tr td {
  border: 1px solid #e8e8e8 !important;
}
.div-table.bright tr:hover > td {
  background: #e6f7ff;
}
.div-table.small tr td {
  padding: 8px 8px;
}
.div-table .ant-row.ant-form-item {
  margin: 0 !important;
}
.list-main {
  min-height: 60px;
  position: relative;
}
</style>
