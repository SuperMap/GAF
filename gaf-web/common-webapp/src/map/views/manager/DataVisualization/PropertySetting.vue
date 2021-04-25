<template>
  <div class="gutter-box">
    <div>
      <a-row>
        <a-col :span="7">
          <span>是否显示</span>
        </a-col>
        <a-col>
          <a-switch
            v-model="propertyInfo.visible"
            size="small"
            @change="change"
          >
            <a-icon slot="checkedChildren" type="check" />
            <a-icon slot="unCheckedChildren" type="close" />
          </a-switch>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="7">
          <span>标题</span>
        </a-col>
        <a-col :span="16">
          <a-input
            v-model="propertyInfo.title"
            size="small"
            @change="titleChange"
          />
        </a-col>
      </a-row>
      <a-row :gutter="16"></a-row>
      <a-row :gutter="16">
        <a-col :span="24">
          <a-table
            :pagination="false"
            :columns="columns"
            :data-source="data"
            :row-key="(record) => record.name"
            :scroll="{ y: 200 }"
            :row-selection="{
              columnTitle: '显示',
              selectedRowKeys: selectedRowKeys,
              onChange: onSelectedChange,
            }"
          />
        </a-col>
      </a-row>
    </div>
  </div>
</template>

<script>
const columns = [
  {
    title: '字段别名',
    dataIndex: 'name',
    key: 'name',
    scopedSlots: { customRender: 'caption' },
  },
]
export default {
  components: {},
  model: {
    prop: 'fieldsInfo',
    event: 'change',
  },
  props: {
    fieldsInfo: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      propertyInfo: {
        visible: true,
        title: '',
        fields: [],
      },
      columns,
      data: [],
      selectedRowKeys: [],
      selectedRows: [],
    }
  },
  watch: {
    fieldsInfo(val) {
      this.propertyInfo.title = val.title
      this.data = []
      if (val.allFields) {
        const fieldNames = val.allFields
        if (fieldNames.length) {
          for (let i = 0; i < fieldNames.length; i++) {
            const item = {
              name: fieldNames[i],
            }
            this.data.push(item)
          }
        }
      }
      const selectedFields = val.fields
      if (selectedFields && selectedFields.length) {
        this.selectedRowKeys = []
        for (let i = 0; i < selectedFields.length; i++) {
          this.selectedRowKeys.push(selectedFields[i].name)
        }
      }
    },
  },
  methods: {
    change() {
      this.$emit('propertyInfo', this.propertyInfo)
    },
    titleChange() {
      this.$emit('propertyInfo', this.propertyInfo)
    },
    onSelectedChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.propertyInfo.fields = selectedRows
      this.$emit('propertyInfo', this.propertyInfo)
    },
  },
}
</script>

<style scoped></style>
