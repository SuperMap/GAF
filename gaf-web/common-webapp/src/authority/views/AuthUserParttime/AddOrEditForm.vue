<template>
  <div class="page-container">
    <div class="grid-container">
    <div class="drawer-header">
      <template>
        <a-breadcrumb separator=">" class="modal-line">
          <span class="vertical-line">| </span>
          <a-breadcrumb-item class="text-bolder">{{ title }}</a-breadcrumb-item>
        </a-breadcrumb>
      </template>
    </div>
    <div class="drawer-content">
      <a-form
        :form="addOrEditForm"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 13 }"
        layout="horizontal"
      >
        <a-form-item v-show="false" label="用户id">
          <a-input
            v-decorator="[
              'userId',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入用户id'
                  }
                ]
              }
            ]"
            :disabled="operation === 1"
            placeholder="请输入用户id"
            allow-clear
          />
        </a-form-item>
        <a-form-item v-show="false" label="挂职部门id">
          <a-input
            v-decorator="[
              'departmentId',
              {
                rules: [
                  {
                    required: true,
                    message: '请选择一个部门'
                  }
                ]
              }
            ]"
            :disabled="operation === 1"
            placeholder="请输入部门"
          />
        </a-form-item>
        <a-form-item label="挂职部门">
          <a-cascader
            v-decorator="[
              'departmentPath',
              {
                rules: [
                  {
                    required: true,
                    message: '请选择一个部门'
                  }
                ]
              }
            ]"
            :disabled="operation === 1"
            :options="departmentOptions"
            @change="onChange"
            :fieldNames="{
              label: 'title',
              value: 'key',
              children: 'children'
            }"
            change-on-select
            placeholder="请选择部门"
          />
        </a-form-item>
        <a-form-item label="岗位">
          <a-select
            v-decorator="[
              'postId',
              {
                rules: [
                  {
                    required: true,
                    message: '请选择岗位'
                  }
                ]
              }
            ]"
            :options="postOptions"
            :disabled="operation === 1"
            allow-clear
            placeholder="请选择岗位"
          >
            <a-select-option value="">-请选择岗位id-</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item v-show="false" label="岗位类别">
          <a-select
            :disabled="operation === 1"
            v-decorator="['postType']"
            allow-clear
          >
            <a-select-option value="">-请选择岗位类别-</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item v-show="false"  label="过期时间">
          <a-date-picker
            :disabled="operation === 1"
            v-decorator="['expirationTime']"
            show-time
            placeholder="请选择过期时间"
          />
        </a-form-item>
        <div v-if="operation === 1">
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
      </a-form>
    </div>
    <div class="drawer-footer">
      <a-button
        v-show="operation !== 1"
        @click="submitForm"
        type="primary"
        :loading="loading"
        class="submit-gray"
      >
        确定
      </a-button>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <button @click="backToList" class="cancel-modal">取消</button>
    </div>
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
    }
  },
  data() {
    return {
      dataId: '',
      departmentOptions: [],
      postOptions: [],
      loading: false
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    const copyData = { ...this.editData }
    this.dataId = copyData.userParttimeId
    delete copyData.userParttimeId
    delete copyData.status
    delete copyData.sortSn
    delete copyData.departmentName
    delete copyData.postName
    delete copyData.postType
    if (this.operation !== 1) {
      delete copyData.createdTime
      delete copyData.updatedTime
      delete copyData.createdBy
      delete copyData.updatedBy
    }
    if (copyData.expirationTime)
      copyData.expirationTime = moment(new Date(copyData.expirationTime))
    if (copyData.createdTime)
      copyData.createdTime = moment(new Date(copyData.createdTime))
    if (copyData.updatedTime)
      copyData.updatedTime = moment(new Date(copyData.updatedTime))
    this.getDepartmentOptionsAndSet()
    if (copyData.departmentId) {
      this.getDepartmentPathAndSet(copyData.departmentId)
      this.getPostsByDepartmentIdAndSet(copyData.departmentId)
    }
    this.addOrEditForm.setFieldsValue({ ...copyData })
  },
  methods: {
    // 树相关方法
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
    // 删除叶子节点的children属性
    deleteLeafChildren(treeData) {
      this.deepFirstTraverseTree({ key: '0' }, treeData, (parentNode, node) => {
        if (!node.children || node.children.length === 0) {
          delete node.children
        }
      })
      return treeData
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
    moment,
    async getDepartmentPathAndSet(departmentId) {
      const url = `/authority/auth-departments/department-path/${departmentId}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.addOrEditForm.setFieldsValue({ departmentPath: res.data })
      } else {
        this.$message.error(`取上级部门信息失败，原因:${res.message}`)
      }
    },
    async getDepartmentOptionsAndSet() {
      const url = `/authority/auth-departments/list-tree-node`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        const treeData = this.convertToTree(res.data)
        this.departmentOptions = this.deleteLeafChildren(treeData)
      } else {
        this.$message.error(`取部门信息失败，原因:${res.message}`)
      }
    },
    async getPostsByDepartmentIdAndSet(departmentId) {
      const url = `/authority/auth-posts/departments/${departmentId}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.postOptions = res.data
      } else {
        this.$message.error(`取部门下的岗位失败，原因:${res.message}`)
      }
    },
    onChange(value) {
      if (value && value.length > 0) {
        this.addOrEditForm.setFieldsValue({
          departmentId: value[value.length - 1]
        })
        this.addOrEditForm.setFieldsValue({
          postId: undefined
        })
        this.getPostsByDepartmentIdAndSet(value[value.length - 1])
      } else {
        this.addOrEditForm.setFieldsValue({
          departmentId: undefined
        })
        this.addOrEditForm.setFieldsValue({
          postId: undefined
        })
        this.postOptions = undefined
      }
    },
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = `/authority/auth-user-parttime/`
        const data = this.addOrEditForm.getFieldsValue()
        this.loading = true
        if (this.dataId) {
          url = url + this.dataId
          const rst = await this.$axios.put(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('更新成功')
          } else {
            this.$message.error(`更新失败,原因：${rst.data.message}`)
          }
        } else {
          const rst = await this.$axios.post(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('添加成功')
          } else {
            this.$message.error(`添加失败,原因：${rst.data.message}`)
          }
        }
        this.loading = false
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
