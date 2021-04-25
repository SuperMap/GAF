<template>
  <div>
    <a-form layout="horizontal" :form="form">
      <a-form-item
        label="目录名称"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 17 }"
      >
        <a-input
          v-decorator="[
            'catalogName',
            {
              rules: [
                {
                  required: true,
                  message: '请输入目录名称!',
                },
              ],
            },
          ]"
          size="small"
        />
      </a-form-item>
      <a-form-item
        label="上级目录"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 17 }"
      >
        <a-select v-decorator="['parentId']" size="small" allow-clear>
          <a-select-option v-for="item in directoryCatalog" :key="item.id">
            {{ item.name }}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item
        label="图标"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 17 }"
      >
        <a-row>
          <a-col :span="20">
            <a-input v-decorator="['icon']" size="small" />
          </a-col>
          <a-col :span="4">
            <a-button icon="setting" size="small" @click="showIcon = true" />
          </a-col>
        </a-row>
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
import { getFunctionUrl } from '~/utils/GAFMapDataUtils'
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
      directoryCatalog: [],
      showIcon: false,
    }
  },
  beforeCreate() {
    this.form = this.$form.createForm(this)
  },
  created() {
    this.loadDirectoryCatalog()
  },
  methods: {
    // 获取所有目录 1
    async loadDirectoryCatalog() {
      const url = getFunctionUrl(this.mapCode)
      const res = await this.$axios.$get(url)
      this.directoryCatalog = []
      if (res && res.data) {
        this.setCatalogToDirectory(this.directoryCatalog, res.data)
      }
    },
    // 获取所有目录 2
    setCatalogToDirectory(list, tree) {
      if (tree !== undefined && tree.length > 0) {
        for (let i = 0; i < tree.length; i++) {
          const item = tree[i]
          if (item.type === 'catalog') {
            list.push(item)
            this.setCatalogToDirectory(list, item.children)
          }
        }
      }
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
