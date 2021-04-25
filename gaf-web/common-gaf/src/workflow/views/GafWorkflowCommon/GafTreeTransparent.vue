<template>
  <div>
    <a-input-search
      v-show="showSearch"
      v-model="inputSearchValue"
      :placeholder="searchPlaceholder"
      :style="searchStyle"
      :class="searchClass"
      @search="onSearch"
    />
    <a-tree
      v-model="checkedKeys"
      v-bind="$attrs"
      :tree-data="dataOfTree"
      :expanded-keys="expandedNodeKeys"
      :auto-expand-parent="autoExpandParent"
      :check-strictly="checkNodeStrictly"
      @update:expandedKeys="$emit('update:expandedNodeKeys', $event)"
      @expand="onExpand"
      v-on="treeListeners"
    >
      <template slot="title" slot-scope="{ title, type }">
        <span
          v-if="
            title &&
            title.indexOf(searchValue) > -1 &&
            (searchType.indexOf(0) > -1 || searchType.indexOf(type) > -1)
          "
        >
          {{ title.substr(0, title.indexOf(searchValue)) }}
          <span style="color: #f50">{{ searchValue }}</span>
          {{ title.substr(title.indexOf(searchValue) + searchValue.length) }}
        </span>
        <span v-else>{{ title }}</span>
      </template>
    </a-tree>
  </div>
</template>
<script>
const getParent = (key, tree) => {
  let parent
  for (let i = 0; i < tree.length; i++) {
    const node = tree[i]
    if (node.children) {
      if (node.children.some((item) => item.key === key)) {
        parent = { key: node.key, title: node.title }
      } else if (getParent(key, node.children)) {
        parent = getParent(key, node.children)
      }
    }
  }
  return parent
}
export default {
  name: 'GafTreeTransparent',
  inheritAttrs: false,
  model: {
    prop: 'checkedNodeKeys',
    event: 'checkedNodeKeysChange',
  },
  props: {
    dataOfTree: {
      type: Array,
      default: () => [],
    },
    expandedNodeKeys: {
      type: Array,
      default: () => [],
      required: true,
    },
    checkedNodeKeys: {
      type: [Object, Array],
      default: () => [],
    },
    checkNodeStrictly: {
      type: Boolean,
      default: false,
    },
    searchPlaceholder: {
      type: String,
      default: '',
    },
    showSearch: {
      type: Boolean,
      default: true,
    },
    searchStyle: {
      type: Object,
      default: null,
    },
    searchClass: {
      type: String,
      default: '',
    },
    searchType: {
      type: Array,
      default: () => [0],
    },
  },
  data() {
    return {
      inputSearchValue: '',
      searchValue: '',
      checkedKeys: this.checkNodeStrictly
        ? { checked: [], halfChecked: [] }
        : [],
      autoExpandParent: true,
    }
  },
  computed: {
    treeListeners() {
      return Object.assign(
        {},
        // 从父级添加所有的监听器
        this.$listeners
      )
    },
  },
  watch: {
    checkedKeys(newCheckedKeys) {
      this.$emit('checkedNodeKeysChange', newCheckedKeys)
    },
    checkedNodeKeys(newCheckedNodeKeys) {
      this.checkedKeys = newCheckedNodeKeys
    },
  },
  mounted() {
    this.checkedKeys = this.checkedNodeKeys
  },
  methods: {
    onExpand(expandedKeys) {
      this.expandedKeys = expandedKeys
      this.autoExpandParent = false
      this.$emit('nodeExpand', expandedKeys)
    },
    setInputSearchValueAndOnSearch(value) {
      this.inputSearchValue = value
      this.onSearch(value)
    },
    getParent,
    convertToTree(nodeList) {
      if (!nodeList || nodeList.length <= 0) {
        return []
      }
      const rootParent = { key: '0' }
      return this.getChildren(rootParent, nodeList)
    },
    getChildren(root, all) {
      const collect = all
        .filter((treeNode) => {
          return treeNode.parentId === root.key
        })
        .map(function (treeNode) {
          treeNode.children = this.getChildren(treeNode, all)
          return treeNode
        }, this)
        .sort(function (a, b) {
          return a.sortSn - b.sortSn
        })
      if (!collect || collect.length <= 0) {
        delete root.children
      }
      return collect
    },
    onSearch(value) {
      if (value && value.trim() !== '') {
        const needExpandedKeys = new Set()
        this.deepFirstTraverseTree(
          {
            key: '0',
            title: '默认根',
            type: 0,
          },
          this.dataOfTree,
          (parentNode, node) => {
            const title = node.title
            const type = node.type
            if (
              title &&
              title.includes(value) &&
              (this.searchType.includes(0) || this.searchType.includes(type))
            ) {
              needExpandedKeys.add(parentNode.key)
            }
          }
        )
        this.$emit('update:expandedNodeKeys', Array.from(needExpandedKeys))
        Object.assign(this, {
          searchValue: value,
          autoExpandParent: true,
        })
      } else {
        Object.assign(this, {
          searchValue: '',
        })
      }
    },
    inserNode(insertNodeData) {
      const needInsertNodeData = { ...insertNodeData }
      if (!insertNodeData.parentId || insertNodeData.parentId === '0') {
        this.dataOfTree.splice(insertNodeData.sortSn - 1, 0, needInsertNodeData)
      } else {
        const node = this.getNodeByKey(this.dataOfTree, insertNodeData.parentId)
        if (node != null) {
          if (node.children) {
            node.children.splice(
              insertNodeData.sortSn - 1,
              0,
              needInsertNodeData
            )
          } else {
            this.$set(node, 'children', [needInsertNodeData])
          }
        }
      }
    },
    updateNode(updateNodeData) {
      let children = null
      if (updateNodeData.parentId === '0') {
        children = this.dataOfTree
      } else {
        const parentNode = this.getNodeByKey(
          this.dataOfTree,
          updateNodeData.parentId
        )
        if (parentNode != null && parentNode.children) {
          children = parentNode.children
        }
      }
      if (children) {
        let i = 0
        for (; i < children.length; i++) {
          if (children[i].key === updateNodeData.key) {
            break
          }
        }
        if (i < children.length) {
          const needUpdateNodeData = { ...updateNodeData }
          delete needUpdateNodeData.key
          if (i === updateNodeData.sortSn - 1) {
            Object.assign(children[i], needUpdateNodeData)
          } else {
            const old = children[i]
            children.splice(i, 1)
            Object.assign(old, needUpdateNodeData)
            if (updateNodeData.sortSn > children.length) {
              children.push(old)
            } else if (updateNodeData.sortSn < 1) {
              children.splice(0, 0, old)
            } else {
              children.splice(updateNodeData.sortSn - 1, 0, old)
            }
          }
        }
      }
    },
    deleteNode(deleteNodeData) {
      if (!deleteNodeData.parentId || deleteNodeData.parentId === '0') {
        for (let i = 0; i < this.dataOfTree.length; i++) {
          if (this.dataOfTree[i].key === deleteNodeData.key) {
            this.dataOfTree.splice(i, 1)
            break
          }
        }
      } else {
        const node = this.getNodeByKey(this.dataOfTree, deleteNodeData.parentId)
        if (node != null) {
          if (node.children) {
            const children = node.children
            for (let i = 0; i < children.length; i++) {
              if (children[i].key === deleteNodeData.key) {
                children.splice(i, 1)
                break
              }
            }
          }
        }
      }
    },
    getNodeByKey(data, key) {
      if (data) {
        for (let i = 0; i < data.length; i++) {
          const node = data[i]
          if (node.key === key) {
            return node
          }
          if (node.children) {
            const hit = this.getNodeByKey(node.children, key)
            if (hit != null) {
              return hit
            } else {
              continue
            }
          }
        }
      }
      return null
    },
    getNode(key) {
      return this.getNodeByKey(this.dataOfTree, key)
    },
    deepFirstTraverseTree(parentNode, data, callback) {
      if (data) {
        for (let i = 0; i < data.length; i++) {
          const node = data[i]
          callback(parentNode, node)
          if (node.children) {
            this.deepFirstTraverseTree(node, node.children, callback)
          }
        }
      }
    },
  },
}
</script>
