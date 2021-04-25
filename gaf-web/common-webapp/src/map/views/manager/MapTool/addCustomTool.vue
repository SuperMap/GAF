<template>
  <div>
    <a-form layout="horizontal" :form="form">
      <a-form-item
        label="工具名称"
        :label-col="labelCol"
        :wrapper-col="valueCol"
      >
        <a-input
          v-decorator="[
            'toolName',
            {
              rules: [
                {
                  required: true,
                  message: '请输入工具名称!',
                },
              ],
            },
          ]"
          size="small"
        />
      </a-form-item>
      <a-form-item
        label="上级节点"
        :label-col="labelCol"
        :wrapper-col="valueCol"
      >
        <a-select v-decorator="['parentId']" size="small" allow-clear>
          <a-select-option v-for="item in mapTools" :key="item.id">
            {{ item.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="Code" :label-col="labelCol" :wrapper-col="valueCol">
        <a-input
          v-decorator="[
            'code',
            {
              rules: [
                {
                  required: true,
                  message: '请输入code!',
                },
              ],
            },
          ]"
          size="small"
        />
      </a-form-item>
      <a-form-item label="图标" :label-col="labelCol" :wrapper-col="valueCol">
        <a-row>
          <a-col :span="22">
            <a-input
              v-decorator="[
                'icon',
                {
                  rules: [
                    {
                      required: true,
                      message: '请选择图标!',
                    },
                  ],
                },
              ]"
              size="small"
            />
          </a-col>
          <a-col :span="2">
            <a-button icon="setting" size="small" @click="showIcon = true" />
          </a-col>
        </a-row>
      </a-form-item>
      <a-form-item
        label="显示风格"
        :label-col="labelCol"
        :wrapper-col="valueCol"
      >
        <a-select
          v-decorator="[
            'displayStyle',
            {
              rules: [
                {
                  required: true,
                  message: '请选择上级节点!',
                },
              ],
            },
          ]"
          size="small"
          allow-clear
        >
          <a-select-option value="ImageAndText">ImageAndText</a-select-option>
          <a-select-option value="Image">Image</a-select-option>
          <a-select-option value="Text">Text</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item
        label="执行路径"
        :label-col="labelCol"
        :wrapper-col="valueCol"
      >
        <a-input
          v-decorator="[
            'actionUrl',
            {
              rules: [
                {
                  required: true,
                  message: '请输入执行路径!',
                },
              ],
            },
          ]"
          size="small"
        />
      </a-form-item>
    </a-form>
    <system-icon
      :visible="showIcon"
      @selectedIcon="selectedIcon"
      @closeIconSetting="showIcon = false"
    />
  </div>
</template>

<script>
// @ts-nocheck
import { getMapToolUrl } from '~/utils/GAFMapDataUtils'
import systemIcon from '../systemIcon'
export default {
  components: {
    systemIcon,
  },
  props: {
    mapCode: {
      type: String,
      required: true,
      validator(value) {
        return value
      },
    },
  },
  data() {
    return {
      labelCol: { span: 5 },
      valueCol: { span: 18 },
      mapTools: [],
      showIcon: false,
    }
  },
  beforeCreate() {
    this.form = this.$form.createForm(this)
  },
  created() {
    this.loadMapTools()
  },
  methods: {
    async loadMapTools() {
      const url = getMapToolUrl(this.mapCode)
      const res = await this.$axios.$get(url)
      this.mapTools = res.data
    },
    selectedIcon(icon) {
      this.form.setFieldsValue({
        icon,
      })
    },
  },
}
</script>

<style scoped></style>
