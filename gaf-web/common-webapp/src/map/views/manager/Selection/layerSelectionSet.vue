<template>
  <div class="settingStyle">
    <a-row>
      <a-col :span="12">
        <span>查询配置</span>
      </a-col>
      <a-col :offset="6" :span="2">
        <a-tooltip placement="topLeft" title="添加">
          <a-icon type="plus" style="margin: 5px" @click="showAddForm" />
        </a-tooltip>
      </a-col>
      <a-col :offset="1" :span="2">
        <a-tooltip placement="topLeft" title="删除">
          <a-popconfirm
            title="是否删除？"
            ok-text="是"
            cancel-text="否"
            @confirm="() => deleteSelection()"
          >
            <a href="javascript:;">
              <a-icon type="delete" style="margin: 5px" />
            </a>
          </a-popconfirm>
        </a-tooltip>
      </a-col>
    </a-row>
    <a-divider style="margin: 10px" />
    <div v-show="activeCard === 'feature'">
      <feature-selection
        type="Feature"
        :refresh="featureRefresh"
        :add-click="featureAdd"
        :map-code="mapCode"
        @onServiceClick="featureSelectionClick"
        @selectionItemClick="itemClick"
      />
    </div>
    <div v-show="activeCard === 'space'">
      <feature-selection
        type="Space"
        :refresh="spaceRefresh"
        :add-click="spaceAdd"
        :map-code="mapCode"
        @onServiceClick="featureSelectionClick"
        @selectionItemClick="itemClick"
      />
    </div>
    <div v-show="activeCard === 'attribute'">
      <feature-selection
        type="Attribute"
        :refresh="attributeRefresh"
        :add-click="attributeAdd"
        :map-code="mapCode"
        @onServiceClick="featureSelectionClick"
        @selectionItemClick="itemClick"
      />
    </div>
  </div>
</template>

<script>
// @ts-nocheck
import { getSelectionUrl } from '~/utils/GAFMapDataUtils'
import featureSelection from './featureSelection'
export default {
  components: {
    featureSelection,
  },
  model: {
    prop: 'refresh',
    event: 'change',
  },
  props: {
    refresh: {
      type: Boolean,
    },
    mapCode: {
      type: String,
      required: true,
      validator(value) {
        return value
      },
    },
    activeCard: {
      type: String,
      requited: true,
      default: 'feature',
    },
  },
  data() {
    return {
      selectedKeys: [],
      featureRefresh: false,
      spaceRefresh: false,
      attributeRefresh: false,
      rowId: '',
      featureAdd: false,
      spaceAdd: false,
      attributeAdd: false,
    }
  },
  watch: {
    refresh: {
      handler() {
        this.refreshSelection()
      },
    },
    activeCard: {
      handler() {
        this.refreshSelection()
      },
    },
    immediate: true,
  },
  methods: {
    featureSelectionClick(row) {
      this.rowId = row.id
      this.$emit('selectedChange', row)
    },
    itemClick(row) {
      this.$emit('selectionItemClick', row)
    },
    onChange(key) {
      if (key && key.target && key.target.name && key.target.value) {
        this[key.target.name] = key.target.value
      }
    },
    showAddForm() {
      if (this.activeCard === 'feature') {
        this.featureAdd = !this.featureAdd
      } else if (this.activeCard === 'space') {
        this.spaceAdd = !this.spaceAdd
      } else if (this.activeCard === 'attribute') {
        this.attributeAdd = !this.attributeAdd
      }
    },
    async deleteSelection() {
      const serviceId = null
      if (this.rowId) {
        const url = getSelectionUrl(this.mapCode, serviceId) + `/${this.rowId}`
        const res = await this.$axios.$delete(url)
        if (res.isSuccessed) {
          this.$message.success('操作成功')
          this.refreshSelection()
        } else {
          this.$message.error('添加失败')
        }
      }
    },
    refreshSelection() {
      if (this.activeCard === 'feature') {
        this.featureRefresh = !this.featureRefresh
      } else if (this.activeCard === 'space') {
        this.spaceRefresh = !this.spaceRefresh
      } else if (this.activeCard === 'attribute') {
        this.attributeRefresh = !this.attributeRefresh
      }
    },
  },
}
</script>
<style scoped>
.tree-font {
  width: 180px;
  text-align: left;
  overflow: hidden;
  text-overflow: ellipsis;
}
.dividerStyle {
  margin: 10px;
}
.settingStyle {
  max-height: 97vh;
  overflow-y: auto;
  overflow-x: hidden;
}
.rbutton {
  width: 60px;
  height: 50px;
  text-align: center;
}
.text-height {
  height: 20px;
}
</style>
