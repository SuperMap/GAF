<template>
  <div class="page-container">
    <div class="grid-container">
    <div class="drawer-header">
    <template>
      <a-breadcrumb separator=">" class="modal-line">
        <a-breadcrumb-item class="text-bolder">{{ title }}</a-breadcrumb-item>
      </a-breadcrumb>
    </template>
    </div>
    <div class="drawer-content">
      <a-form
        :form="addOrEditForm"
        layout="horizontal"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 19 }"
      >
        <a-form-item label="所属分组">
          <a-tree-select
              :disabled="true"
              v-decorator="[
                'catalogId',
                {
                  rules: [
                    {
                      required: true,
                      message: '所属分组不能为空'
                    }
                  ]
                }
              ]"
              :treeData="treeData"
              placeholder="请选择所属分组"
              tree-node-filter-prop="title"
              show-search
              tree-default-expand-all
              allow-clear
            >
          </a-tree-select>
        </a-form-item>
        <a-form-item label="字典编码">
          <a-input
            v-decorator="[
              'dictCode',
              {
                rules: [
                  {
                    required: true,
                    message: '字典编码不能为空'
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ]
              }
            ]"
            placeholder="一般为英文"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="中文名称">
          <a-input
            v-decorator="[
              'dictName',
              {
                rules: [
                  {
                    required: true,
                    message: '中文名称不能为空'
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入中文名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="排序序号">
          <a-input-number
            v-decorator="['seq']"
            :precision="0"
            :min="1"
          />
        </a-form-item>
        <a-form-item v-show="false" label="父级id">
          <a-input
            v-decorator="['pid', {initialValue: '0'}]"
            placeholder="请输入父级id"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea
            v-decorator="[
              'dictDesc',
              {
                rules: [
                  {
                    max: 500,
                    message: '长度不能超过500个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入描述"
            auto-size
          />
        </a-form-item>
      </a-form>
    </div>
    <div class="drawer-footer">
      <div class="drawer-footer-div">
      <button v-if="operation === 2" @click="handleBack" class="cancel-modal">
        取消
      </button>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <a-button @click="submitForm" type="primary" :loading="loading" class="submit-gray">
        {{submitButtonText}}
      </a-button>
      </div>
    </div>
    </div>
  </div>
</template>

<script>
    import moment from 'moment'
    import treeUtil from '../../utils/tree/TreeUtil.js'

    export default {
  props: {
    // title: {
    //   type: String,
    //   default: ''
    // },
    editData: {
      type: Object,
      default: null
    },
    operation: {
      type: Number,
      default: 0
    }
  },
  computed: {
    submitButtonText() {
      if (this.operation === 3) {
        return '保存'
      } else if (this.operation === 2) {
        return '添加'
      } else {
        return ''
      }
    },
    title() {
      if (this.operation === 3) {
        return '编辑类别'
      } else if (this.operation === 2) {
        return '新增类别'
      } else {
        return ''
      }
    }
  },
  data() {
    return {
      dataId: '',
      treeData: [],
      loading: false
    }
  },
  watch: {
    editData: function(newEditData) {
      console.log(newEditData)
      this.dataId = newEditData.dataDictId
      delete newEditData.dataDictId
      delete newEditData.dictClass
      delete newEditData.status
      delete newEditData.tenantId
      delete newEditData.createdTime
      delete newEditData.updatedTime
      delete newEditData.createdBy
      delete newEditData.updatedBy
      this.addOrEditForm.setFieldsValue({ ...newEditData })

    }
  },
  created() {
    this.getTreeDataAndSet()
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    console.log(this.editData.catalogId)
    const copyData = { ...this.editData }
    this.dataId = copyData.dataDictId
    delete copyData.dataDictId
    delete copyData.dictClass
    delete copyData.status
    delete copyData.tenantId
    delete copyData.createdTime
    delete copyData.updatedTime
    delete copyData.createdBy
    delete copyData.updatedBy
    // this.addOrEditForm.setFieldsValue({ ...copyData })
    if (this.operation === 3){
      this.addOrEditForm.setFieldsValue({ ...this.editData })
    } else if(this.operation === 2) {
      this.addOrEditForm.setFieldsValue({catalogId: this.editData.catalogId})
    }
  },
  methods: {
    moment,
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = `/sys-mgt/sys-dicts/`
        const data = this.addOrEditForm.getFieldsValue()
        this.loading = true
        if (this.dataId) {
          url = url  + this.dataId
          const rst = await this.$axios.put(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('更新成功')
            this.$emit('update-success', {...rst.data.data})
          } else {
            this.$message.error(`更新失败,原因:${rst.data.message}`)
          }
        } else {
          const rst = await this.$axios.post(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('添加成功')
            this.$emit('add-success', {...rst.data.data})
          } else {
            this.$message.error(`添加失败,原因:${rst.data.message}`)
          }
        }
        this.loading = false
        this.addOrEditForm.resetFields()
        this.$emit('submit')
      })
    },
    async deleteData() {
      if (this.dataId) {
          const url = `/sys-mgt/sys-dicts/` + this.dataId
          const rst = await this.$axios.delete(url)
          if (rst.data.isSuccessed) {
            this.$message.success('删除成功')
            this.clear()
            this.$emit('delete-success', {...rst.data.data})
          } else {
            this.$message.error('删除失败，原因: ' + rst.data.message)
          }
      }
    },
    resetFormFields() {
      this.addOrEditForm.resetFields()
    },
    clear() {
      this.dataId = null
      this.resetFormFields()
    },
    async getTreeDataAndSet() {
      const catalogNodes = await this.getDicCatalogs()
      if(catalogNodes) {
        catalogNodes.forEach(element => {
          element.value = element.key
          element.disabled = false
        });
        this.treeData = treeUtil.convertToTree({key: '0'}, catalogNodes)
      }
    },
    async getDicCatalogs() {
      const url = `/sys-mgt/sys-catalogs/nodes/type/7`
      const res = await this.$axios.$get(url)
      if(res.isSuccessed) {
        return res.data
      } else {
        this.$message.error(`查询失败,原因: ${res.message}`)
        return null
      }
    },
    handleBack() {
      this.addOrEditForm.resetFields()
      this.$emit('back')
    }
  }
}
</script>

<style lang="less" scoped>
.page-container {
  width: 100%;
  height: 100%;
}

.page-container-box {
  height: 100%;
}

// .btn-div {
//   text-align: center;
//   margin: 15px 0;
// }

.form-foot {
  position: relative;
  left: 32%;
}
</style>
