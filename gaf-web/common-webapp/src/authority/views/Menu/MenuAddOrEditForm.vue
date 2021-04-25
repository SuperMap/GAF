<template>
  <div class="page-container">
    <template>
      <a-breadcrumb separator=">" class="modal-line">
        <span class="vertical-line">| </span>
        <a-breadcrumb-item class="text-bolder">{{ title }}</a-breadcrumb-item>
      </a-breadcrumb>
    </template>
    <div class="page-container-box">
      <a-form
        :form="addOrEditForm"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 15 }"
        layout="horizontal"
      >
        <a-form-item v-show="false" label="菜单组">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'menuCatalogId',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入菜单组'
                  }
                ]
              }
            ]"
            placeholder="请输入菜单组"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="菜单分组路径">
          <!-- <a-input :disabled="true" v-model="menuGroupPathName" /> -->
          <a-tree-select
            :disabled="operation !== 3"
            :tree-data="dataOfTree"
            placeholder="请选择分组"
            :replaceFields="{children:'children',title:'title',key:'key',value: 'key'}"
            v-decorator="['menuCatalogId']"
          >
          </a-tree-select>
        </a-form-item>
        <a-form-item label="菜单名">
          <a-input
            :disabled="operation === 1"
            v-decorator="[
              'name',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入菜单名'
                  }
                ]
              }
            ]"
            placeholder="请输入显示名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="模块">
          <a-tree-select
            :disabled="operation === 1"
            v-decorator="[
              'resourceModuleId',
              {
                rules: [
                  {
                    required: true,
                    message: '请选择一个模块,用于关联'
                  }
                ]
              }
            ]"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            :tree-data="treeData"
            tree-node-filter-prop="title"
            show-search
            style="width: 100%"
            placeholder="请选择一个模块,用于关联"
            allow-clear
            tree-default-expand-all
          />
        </a-form-item>
        <a-form-item label="参数">
          <a-textarea
            :disabled="operation === 1"
            v-decorator="['params']"
            placeholder="请输入参数名和值,使用&分割,例如param1=value1&param2=value2"
            auto-size
          />
        </a-form-item>

        <a-form-item label="图标">
          <!-- <a-input
            :disabled="operation === 1"
            v-decorator="['icon']"
            placeholder="请输入图标"
            allow-clear
          /> -->
         <gaf-icon-picker v-decorator="['icon']" placeholder="请选择图标"></gaf-icon-picker>
        </a-form-item>
        <a-form-item v-show="false" label="是否可见">
          <a-radio-group
            :disabled="operation === 1"
            v-decorator="['visible', { initialValue: true }]"
            button-style="solid"
          >
            <a-radio-button :value="true">
              是
            </a-radio-button>
            <a-radio-button :value="false">
              否
            </a-radio-button>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="排序序号">
          <a-input-number
            :disabled="operation === 1"
            :min="1"
            :precision="0"
            v-decorator="['sortSn']"
          />
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea
            :disabled="operation === 1"
            v-decorator="['description']"
            placeholder="请输入描述"
            auto-size
          />
        </a-form-item>
        <div v-show="operation === 1">
          <a-form-item label="创建时间">
            <a-date-picker
              v-decorator="['createdTime']"
              show-time
              placeholder="请选择创建时间"
              disabled
            />
          </a-form-item>
          <a-form-item label="创建人">
            <a-input
              v-decorator="['createdBy']"
              placeholder="请输入创建人"
              allow-clear
              disabled
            />
          </a-form-item>
          <a-form-item label="修改时间">
            <a-date-picker
              v-decorator="['updatedTime']"
              show-time
              placeholder="请选择修改时间"
              disabled
            />
          </a-form-item>
          <a-form-item label="修改人">
            <a-input
              v-decorator="['updatedBy']"
              placeholder="请输入修改人"
              allow-clear
              disabled
            />
          </a-form-item>
        </div>
        <div class="btn-div">
          <button v-show="operation !== 1" @click="submitForm" class="submit-gray">
            确定
          </button>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <button @click="backToList" class="cancel-modal">{{this.operation === 1 ? "返回" : "取消"}}</button>
        </div>
      </a-form>
    </div>
  </div>
</template>

<script>
import moment from 'moment'
export default {
  props: {
    title: {
      type: String,
      default: ''
    },
    editData: {
      type: Object,
      default: null
    },
    operation: {
      type: Number,
      default: 0
    },
    menuGrpId: {
      type: String,
      default: ''
    },
    menuGroupPath: {
      type: Array,
      default: () => []
    },
    dataOfTree: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      dataId: '',
      treeData: []
    }
  },
  computed: {
    menuGroupPathName: function() {
      if (this.menuGroupPath && this.menuGroupPath.length > 0) {
        const pathName = this.menuGroupPath
          .map(function(item) {
            return item.title
          })
          .join('/')
        return pathName
      } else {
        return '无'
      }
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    const copyDate = {...this.editData}
    this.dataId = copyDate.resourceMenuId
    delete copyDate.resourceMenuId
    delete copyDate.status
    if (copyDate.createdTime)
      copyDate.createdTime = moment(new Date(copyDate.createdTime))
    if (copyDate.updatedTime)
      copyDate.updatedTime = moment(new Date(copyDate.updatedTime))
    this.addOrEditForm.setFieldsValue({ ...copyDate })
    this.addOrEditForm.setFieldsValue({
      menuCatalogId: this.menuGrpId
    })
    this.getModuleTreeData()
  },
  methods: {
    moment,
    convertToTree(nodeList) {
      if (!nodeList || nodeList.length <= 0) {
        return []
      }
      const rootParent = { key: '0' }
      const children = this.getChildren(rootParent, nodeList)
      return children || []
    },
    getChildren(root, all) {
      const collect = all
        .filter(treeNode => {
          return treeNode.parentId === root.key
        })
        .map(function(treeNode) {
          treeNode.children = this.getChildren(treeNode, all)
          return treeNode
        }, this)
        .sort(function(a, b) {
          return a.sortSn - b.sortSn
        })
      return collect
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
    getModuleTreeData() {
      this.$axios.get('/authority/auth-resource-modules/tree').then(rst => {
        if (rst.data.isSuccessed) {
          const moduleTreeData = this.convertToTree(rst.data.data)
          this.deepFirstTraverseTree(
            { key: '0' },
            moduleTreeData,
            (parentNode, node) => {
              if (node.type !== 7) {
                node.selectable = false
              }
              node.value = node.key
            }
          )
          this.treeData = moduleTreeData
        } else {
          this.$message.error(`加载模块树失败,原因:${rst.data.message}`)
        }
      })
    },
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = `/authority/auth-resource-menus/`
        const data = this.addOrEditForm.getFieldsValue()
        if (this.dataId) {
          url = url + this.dataId
          const rst = await this.$axios.put(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('更新成功')
          } else {
            this.$message.error(`更新失败,原因:${rst.data.message}`)
          }
        } else {
          const rst = await this.$axios.post(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('添加成功')
          } else {
            this.$message.error(`添加失败,原因:${rst.data.message}`)
          }
        }
        this.addOrEditForm.resetFields()
        this.$emit('submit')
      })
    },
    // 从新增修改模态框返回列表
    backToList() {
      this.addOrEditForm.resetFields()
      this.$emit('back')
    }
  }
}
</script>

<style lang="less" scoped>
button {
  width: 80px;
  font-size: 12px;
  cursor: pointer;
}

.page-container {
  width: 100%;
  height: 100%;
}

.page-container-box {
  height: 100%;
}

.btn-div {
  text-align: center;
  margin: 15px 0;
}
</style>
