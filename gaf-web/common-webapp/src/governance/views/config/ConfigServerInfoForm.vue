<template>
  <a-form :model="configs" laba-position="top">
    <a-row
      v-for="(config, idx) in configs"
      :key="'config-row-' + idx"
      :gutter="16"
    >
      <a-col :span="8">
        <a-form-item
          :prop="configs[idx].propertyKey"
          :label="idx === 0 ? '键(key)' : ''"
        >
          <a-input
            :key="'propertyKey' + idx"
            v-model="configs[idx].propertyKey"
            placeholder="请输入键(key)"
            style="width: 100%;"
          ></a-input>
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item
          :prop="configs[idx].propertyValue"
          :label="idx === 0 ? '值(value)' : ''"
        >
          <a-input
            :key="'propertyValue' + idx"
            v-model="configs[idx].propertyValue"
            placeholder="请输入值(value)"
          ></a-input>
        </a-form-item>
      </a-col>
      <a-col :span="8">
        <a-form-item
          :prop="configs[idx].description"
          :label="idx === 0 ? '描述' : ''"
        >
          <a-row :gutter="16">
          <a-col :span="21">
            <a-input
              :key="'propertyKey' + idx"
              v-model="configs[idx].description"
              placeholder="请输入描述"
              style="width: 100%;"
            ></a-input>
            </a-col>
            <a-col v-if="showPropertyRemoveButton" :span="3">
              <a-button @click="removeRow(idx, configs)">
                <a-icon type="minus-circle" />
              </a-button>
            </a-col>
          </a-row>
        </a-form-item>
      </a-col>
    </a-row>
  </a-form>
</template>

<script>
var vm = null
// 检查空映射
export const checkNullKv = function(propertyInfo) {
  let message = null
  propertyInfo.forEach(c => {
    if (!c.propertyKey && !c.propertyValue) {
      message = '请输入完整的键值对'
    }
  })
  return message
}
// 检查配置映射 key - value
export const checkKv = function(propertyInfo) {
  let message = null
  propertyInfo.forEach(c => {
    if (
      (!c.propertyKey && c.propertyValue) ||
      (c.propertyKey && !c.propertyValue) ||
      (!c.propertyKey && !c.propertyValue)
    ) {
      message = '请输入完整的键值对'
    }
  })
  return message
}
// 动态添加键值对
export const addKvRow = function(propertyInfo) {
  propertyInfo.push({
    id: '',
    propertyKey: '',
    propertyValue: '',
    description: ''
  })
}
// 动态移除键值对(配置映射)
export const removeKvRow = function(idx, propertyInfo) {
  propertyInfo.splice(idx, 1)
}
export const clearKvRow = function() {
  vm.configs = []
}
export default {
  model: {
    prop: 'propertyInfo',
    event: 'change'
  },
  props: {
    propertyInfo: {
      type: Array,
      default: () => [{ id: '', propertyKey: '', propertyValue: '', description: '' }]
    },
    addRow: {
      type: Function,
      default: c => addKvRow(c)
    },
    removeRow: {
      type: Function,
      default: (idx, c) => removeKvRow(idx, c)
    },
    showPropertyRemoveButton: {
      type: Boolean,
      default: false
    }
  },
  data: function() {
    // 属性用作初始值
    return {
      configs: this.propertyInfo
    }
  },
  created() {
    vm = this
  },
  watch: {
    propertyInfo: {
      deep: true,
      handler: function(propertyInfo) {
        vm.configs = propertyInfo
        this.$emit('change', propertyInfo)
      }
    }
  }
}
</script>

<style scoped></style>
