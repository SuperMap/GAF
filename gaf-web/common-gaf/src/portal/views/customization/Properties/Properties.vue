<template>
  <a-collapse
    v-model="activeKey"
    :bordered="false"
    class="gaf-collapse"
    accordion
  >
    <a-collapse-panel key="1" header="面板属性">
      <panel-title :item-layout="itemLayout" :item="item" />
    </a-collapse-panel>
    <a-collapse-panel v-if="data.name === 'grid'" key="2" header="栅格布局属性">
      <panel-column :item-layout="itemLayout" :item="item" />
    </a-collapse-panel>
    <a-collapse-panel v-if="data.name === 'gridTable'" key="8" header="表格布局属性">
      <panel-grid-table :item-layout="itemLayout" :item="item" />
    </a-collapse-panel>
    <a-collapse-panel
      v-if="data.name === 'PlatQuickLink'"
      key="3"
      header="快速导航属性"
    >
      <panel-quick-link :item-layout="itemLayout" :item="item" />
    </a-collapse-panel>
    <a-collapse-panel
      v-if="data.name === 'PlatStep'"
      key="4"
      header="进度控制属性"
    >
      <panel-step :item-layout="itemLayout" :item="item" />
    </a-collapse-panel>
    <a-collapse-panel v-if="data.name === 'PlatList'" key="5" header="列表属性">
      <panel-list :item-layout="itemLayout" :item="item" />
    </a-collapse-panel>
    <a-collapse-panel
      v-if="data.name === 'PlatTable'"
      key="6"
      header="表格属性"
    >
      <panel-table :item-layout="itemLayout" :item="item" />
    </a-collapse-panel>
    <a-collapse-panel
      v-if="data.name === 'PlatChart'"
      key="7"
      header="统计图属性"
    >
      <panel-chart :item-layout="itemLayout" :item="item" />
    </a-collapse-panel>
    <a-collapse-panel
      v-if="data.name === 'PlatResource'"
      key="9"
      header="平台概况属性"
    >
      <panel-resource :item-layout="itemLayout" :item="item" />
    </a-collapse-panel>
  </a-collapse>
</template>

<script>
export default {
  name: 'Properties',
  components: {
    PanelTitle: () => import('./PanelTitle'),
    PanelColumn: () => import('./PanelColumn'),
    PanelQuickLink: () => import('./PanelQuickLink'),
    PanelStep: () => import('./PanelStep'),
    PanelList: () => import('./PanelList'),
    PanelTable: () => import('./PanelTable'),
    PanelChart: () => import('./PanelChart'),
    PanelGridTable: ()=> import('./PanelGridTable'),
    PanelResource: ()=> import('./PanelResource')
  },
  props: {
    item: {
      type: Object,
      required: false,
      default: () => {},
    },
  },
  data() {
    return {
      activeKey: 1,
      itemLayout: {
        row: {
          borderBottom: 'dashed 0px #efefef',
          marginBottom: '5px',
        },
        label: 7,
        textTitle: {
          marginBottom: '6px',
          fontWeight: 'bold',
          lineHeight: '32px',
          height: '32px',
        },
        textArea: {
          marginBottom: '6px',
          fontWeight: 'bold',
          lineHeight: '52px',
          height: '52px',
        },
        chkTitle: {
          fontWeight: 'bold',
          lineHeight: '22px',
          height: '22px',
        },
        chk: {
          lineHeight: '22px',
          height: '22px',
        },
        rdoTitle: {
          fontWeight: 'bold',
          lineHeight: '20px',
          height: '20px',
        },
        rdo: {
          lineHeight: '20px',
          height: '20px',
        },
        content: 17,
      },
    }
  },
  computed: {
    data() {
      return this.item ? this.item : {}
    },
  },
}
</script>

<style scoped></style>
