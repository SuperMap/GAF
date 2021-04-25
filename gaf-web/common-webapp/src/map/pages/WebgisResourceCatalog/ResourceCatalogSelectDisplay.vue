<template>
  <div>
    <gaf-table-no-page
      :show-x-h="false"
      :row-key="(r, i) => r.catalogId"
      :data-source="selectedCatalogs"
      :columns="columns"
      bordered
      size="small"
      align="center"
    >
      <template slot="bizType" slot-scope="text">
        {{ bizMap.get(text) ? bizMap.get(text) : text }}
      </template>
    </gaf-table-no-page>
  </div>
</template>
<script>
export default {
  props: {
    // 选中的gis服务
    selectedCatalogs: {
      type: Array,
      default: () => [],
    },
    bizMap: {
      type: Map,
      default: () => new Map(),
    },
  },
  data() {
    return {
      columns: [
        {
          title: '资源目录名称',
          dataIndex: 'name',
          key: 'name',
        },
        {
          title: '目录类型',
          dataIndex: 'bizTypeCode',
          key: 'biz_type_code',
          scopedSlots: { customRender: 'bizType' },
        },
        {
          title: '目录描述',
          dataIndex: 'description',
          key: 'description',
        },
      ],
    }
  },
  computed: {},
  created() {},
  methods: {},
}
</script>
