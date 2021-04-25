<template>
  <div>
    <a-table
      bordered
      :columns="columns"
      :data-source="data"
      :row-key="(record) => record.code"
      :row-selection="{
        selectedRowKeys: selectedRowKeys,
        selectedRows: selectedRows,
        onChange: onSelectChange,
      }"
      :pagination="false"
      :scroll="{ y: 210 }"
    >
      <template slot="icon" slot-scope="text, record">
        <a-button
          :icon="record.icon"
          size="small"
          style="width: 50px"
          @click="showIconSelection(record)"
        />
      </template>
      <template slot="displayStyle" slot-scope="text, record">
        <a-select
          v-model="record.displayStyle"
          style="width: 180px"
          size="small"
        >
          <a-select-option value="ImageAndText">ImageAndText</a-select-option>
          <a-select-option value="Image">Image</a-select-option>
          <a-select-option value="Text">Text</a-select-option>
        </a-select>
      </template>
    </a-table>
    <system-icon
      :visible="showIcon"
      @selectedIcon="selectedIcon"
      @closeIconSetting="showIcon = false"
    />
  </div>
</template>

<script>
// @ts-nocheck
import systemIcon from '../systemIcon'
const columns = [
  { title: '工具名', dataIndex: 'name', key: 'name' },
  {
    title: '图标',
    dataIndex: 'icon',
    key: 'icon',
    width: '15%',
    scopedSlots: { customRender: 'icon' },
  },
  {
    title: '显示风格',
    dataIndex: 'displayStyle',
    key: 'displayStyle',
    width: '40%',
    scopedSlots: { customRender: 'displayStyle' },
  },
]
const data = [
  {
    name: '全幅',
    code: 'fullmap',
    icon: 'setting',
    displayStyle: 'ImageAndText',
    index: 1,
  },
  {
    name: '漫游',
    code: 'pan',
    icon: 'setting',
    displayStyle: 'ImageAndText',
    index: 2,
  },
  {
    name: '测量',
    code: 'measuretool',
    icon: 'setting',
    displayStyle: 'ImageAndText',
    index: 3,
    children: [
      {
        name: '距离',
        code: 'measure_distance',
        icon: 'setting',
        displayStyle: 'ImageAndText',
        index: 1,
      },
      {
        name: '面积',
        code: 'measure_area',
        icon: 'setting',
        displayStyle: 'ImageAndText',
        index: 2,
      },
      {
        name: '高度',
        code: 'measure_height',
        icon: 'setting',
        displayStyle: 'ImageAndText',
        index: 3,
      },
    ],
  },
  {
    name: '查询',
    code: 'selecttool',
    icon: 'setting',
    displayStyle: 'ImageAndText',
    index: 4,
    children: [
      {
        name: '要素识别',
        code: 'featureidentity',
        icon: 'setting',
        displayStyle: 'ImageAndText',
        index: 1,
      },
      {
        name: '属性查询',
        code: 'querybyattribute',
        icon: 'setting',
        displayStyle: 'ImageAndText',
        index: 2,
      },
      {
        name: '选择对象查询',
        code: 'querybySelect',
        icon: 'setting',
        displayStyle: 'ImageAndText',
        index: 3,
      },
      {
        name: '绘制范围查询',
        code: 'querybyspatial',
        icon: 'setting',
        displayStyle: 'ImageAndText',
        index: 4,
      },
    ],
  },
  {
    name: '分屏',
    code: 'splitscreen',
    icon: 'setting',
    displayStyle: 'ImageAndText',
    index: 5,
    children: [
      {
        name: '水平分屏',
        code: 'splitscreen_horizontal',
        icon: 'setting',
        displayStyle: 'ImageAndText',
        index: 1,
      },
      {
        name: '垂直分屏',
        code: 'splitscreen_vertical',
        icon: 'setting',
        displayStyle: 'ImageAndText',
        index: 2,
      },
      {
        name: '三屏',
        code: 'splitscreen_triple',
        icon: 'setting',
        displayStyle: 'ImageAndText',
        index: 3,
      },
      {
        name: '四屏',
        code: 'splitscreen_quad',
        icon: 'setting',
        displayStyle: 'ImageAndText',
        index: 4,
      },
      {
        name: '关闭',
        code: 'splitscreen_none',
        icon: 'setting',
        displayStyle: 'ImageAndText',
        index: 5,
      },
    ],
  },
  {
    name: '卷帘',
    code: 'rollershutter',
    icon: 'setting',
    displayStyle: 'ImageAndText',
    index: 6,
    children: [
      {
        name: '左右卷帘',
        code: 'rollershutter_horizontal',
        icon: 'setting',
        displayStyle: 'ImageAndText',
        index: 1,
      },
      {
        name: '上下卷帘',
        code: 'rollershutter_vertical',
        icon: 'setting',
        displayStyle: 'ImageAndText',
        index: 2,
      },
      {
        name: '关闭',
        code: 'rollershutter_none',
        icon: 'setting',
        displayStyle: 'ImageAndText',
        index: 3,
      },
    ],
  },
  {
    name: '清空',
    code: 'cleartool',
    icon: 'setting',
    displayStyle: 'ImageAndText',
    index: 7,
  },
]
export default {
  components: {
    systemIcon,
  },
  data() {
    return {
      columns,
      data,
      selectedRowKeys: [],
      selectedRows: [],
      showIcon: false,
      changeIconRow: {},
    }
  },
  methods: {
    onSelectChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
    showIconSelection(row) {
      this.changeIconRow = row
      this.showIcon = true
    },
    selectedIcon(icon) {
      this.changeIcon(this.data, icon)
      this.data = [...this.data]
    },
    changeIcon(list, icon) {
      for (let i = 0; i < list.length; i++) {
        if (list[i].code === this.changeIconRow.code) {
          list[i].icon = icon
          return
        }
        if (list[i].children) {
          this.changeIcon(list[i].children, icon)
        }
      }
    },
  },
}
</script>

<style scoped></style>
