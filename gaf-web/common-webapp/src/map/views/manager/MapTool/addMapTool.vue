<template>
  <div>
    <gaf-modal
      title="添加工具"
      :visible="visible"
      :hight="350"
      @cancel="cancel"
      @ok="ok"
    >
      <a-tabs default-value="system" type="card" @change="change">
        <a-tab-pane key="system" tab="系统">
          <add-system-tool ref="addSystem" />
        </a-tab-pane>
        <a-tab-pane key="custom" tab="自定义">
          <add-custom-tool ref="addCustom" :map-code="mapCode" />
        </a-tab-pane>
      </a-tabs>
    </gaf-modal>
  </div>
</template>

<script>
// @ts-nocheck
import addCustomTool from '../MapTool/addCustomTool'
import addSystemTool from '../MapTool/addSystemTool'
import { getMapToolUrl } from '~/utils/GAFMapDataUtils'
const CheckedAdd = (list, code) => {
  let item
  if (list.length) {
    for (let i = 0; i < list.length; i++) {
      if (list[i].code === code) {
        item = list[i]
      }
    }
  }
  return item
}
const getIdByCode = (list, code) => {
  let id
  if (list.length) {
    for (let i = 0; i < list.length; i++) {
      if (list[i].code === code) {
        id = list[i].id
      }
    }
  }
  return id
}
export default {
  components: {
    addCustomTool,
    addSystemTool,
  },
  model: {
    prop: 'visible',
    event: 'close',
  },
  props: {
    visible: {
      type: Boolean,
    },
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
      activeCard: 'system',
      addResult: false,
    }
  },
  methods: {
    change(activeKey) {
      this.activeCard = activeKey
    },
    ok() {
      if (this.activeCard === 'system') {
        this.addSystemTool()
      } else if (this.activeCard === 'custom') {
        this.addCustomTool()
      }
    },
    cancel() {
      this.$emit('closeMapToolAdd')
    },
    addCustomTool() {
      const form = this.$refs.addCustom.form
      form.validateFields(async (err, values) => {
        if (err) {
          return
        }
        const mapTool = {
          name: values.toolName,
          parentId: values.parentId,
          code: values.code,
          icon: values.icon,
          displayStyle: values.displayStyle,
          actionUrl: values.actionUrl,
          visible: true,
          toolType: 'custom',
          mapConfigCode: this.mapCode,
        }
        const mapTools = []
        mapTools.push(mapTool)
        const url = getMapToolUrl(this.mapCode)
        const result = await this.$axios.$post(url, mapTools)
        if (result.isSuccessed) {
          this.$message.success('操作成功')
          form.resetFields()
          this.$emit('closeMapToolAdd')
        } else {
          this.$message.error('操作失败')
        }
      })
    },
    // list 保存结果，items选择的添加项， haveItems数据库中已有项
    getAddItemList(list, items, haveItems) {
      if (items.length) {
        for (let i = 0; i < items.length; i++) {
          const item = items[i]
          item.visible = true
          item.toolType = 'system'
          item.mapConfigCode = this.mapCode
          if (
            item.code === 'fullmap' ||
            item.code === 'pan' ||
            item.code === 'cleartool'
          ) {
            list.push(item)
          }
          if (
            item.code === 'measuretool' ||
            item.code === 'selecttool' ||
            item.code === 'splitscreen' ||
            item.code === 'rollershutter'
          ) {
            item.children = []
            list.push(item)
          }
        }
        for (let i = 0; i < items.length; i++) {
          const item = items[i]
          if (item.code.includes('query') || item.code.includes('feature')) {
            if (getIdByCode(haveItems, 'selecttool')) {
              item.parentId = getIdByCode(haveItems, 'selecttool')
              list.push(item)
            } else if (CheckedAdd(list, 'selecttool')) {
              const parentItem = CheckedAdd(list, 'selecttool')
              if (parentItem) {
                parentItem.children.push(item)
              }
            }
          } else if (item.code.includes('measure_')) {
            if (getIdByCode(haveItems, 'measuretool')) {
              item.parentId = getIdByCode(haveItems, 'measuretool')
              list.push(item)
            } else if (CheckedAdd(list, 'measuretool')) {
              const parentItem = CheckedAdd(list, 'measuretool')
              if (parentItem) {
                parentItem.children.push(item)
              }
            }
          } else if (item.code.includes('splitscreen_')) {
            if (getIdByCode(haveItems, 'splitscreen')) {
              item.parentId = getIdByCode(haveItems, 'splitscreen')
              list.push(item)
            } else if (CheckedAdd(list, 'splitscreen')) {
              const parentItem = CheckedAdd(list, 'splitscreen')
              if (parentItem) {
                parentItem.children.push(item)
              }
            }
          } else if (item.code.includes('rollershutter_')) {
            if (getIdByCode(haveItems, 'rollershutter')) {
              item.parentId = getIdByCode(haveItems, 'rollershutter')
              list.push(item)
            } else if (CheckedAdd(list, 'rollershutter')) {
              const parentItem = CheckedAdd(list, 'rollershutter')
              if (parentItem) {
                parentItem.children.push(item)
              }
            }
          }
        }
      }
    },
    disRepeatItem(addItems, haveItems) {
      for (let i = 0; i < addItems.length; i++) {
        for (let j = 0; j < haveItems.length; j++) {
          if (addItems[i].code === haveItems[j].code) {
            addItems.splice(i, 1)
            continue
          }
        }
      }
    },
    async addSystemTool() {
      // 页面选择的新建工具
      const selectedRows = this.$refs.addSystem.selectedRows
      if (selectedRows.length) {
        const url = getMapToolUrl(this.mapCode)
        const res = await this.$axios.$get(url)
        // 已经存在的工具
        const list = []
        this.getCodeAndId(list, res.data)
        // 去除重复
        this.disRepeatItem(selectedRows, list)
        const addList = []
        // 设置层级关系
        this.getAddItemList(addList, selectedRows, list)
        const result = await this.$axios.$post(url, addList)
        if (result.isSuccessed) {
          this.$message.success('操作成功')
          this.$emit('closeMapToolAdd')
          Object.assign(
            this.$refs.addSystem.$data,
            this.$refs.addSystem.$options.data()
          )
        } else {
          this.$message.error('操作失败')
        }
      }
    },
    async addMapTools(item, pId) {
      const mapTool = {
        name: item.name,
        code: item.code,
        icon: item.icon,
        displayStyle: item.displayStyle,
        visible: true,
        toolType: 'system',
        mapConfigCode: this.mapCode,
      }
      if (pId) {
        mapTool.parentId = pId
      }
      const url = getMapToolUrl(this.mapCode)
      const result = await this.$axios.$post(url, mapTool)
      if (result.isSuccessed) {
        this.$message.success('操作成功')
        this.$emit('closeMapToolAdd')
        return result.data
      } else {
        this.$message.error('操作失败')
        return null
      }
    },
    getCodeAndId(list, allData) {
      if (allData.length) {
        for (let i = 0; i < allData.length; i++) {
          const item = {
            code: allData[i].code,
            id: allData[i].id,
          }
          list.push(item)
          if (allData[i].children) {
            this.getCodeAndId(list, allData[i].children)
          }
        }
      }
    },
  },
}
</script>

<style scoped></style>
