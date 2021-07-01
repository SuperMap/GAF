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
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 19 }"
        layout="horizontal"
      >
        <a-form-item v-show="false" label="目录分类">
          <a-input
            v-decorator="[
              'type',
              {
                rules: [
                  {
                    required: true
                  }
                ],
                initialValue: '0'
              }
            ]"
            allow-clear
            placeholder="请选择类别"
          >
            <a-select-option value="0">无</a-select-option>
          </a-input>
        </a-form-item>
        <a-form-item v-show="!isFirstLevel" label="上级分组">
          <a-tree-select
            v-decorator="[
              'parentId',
              {
                rules: [
                  {
                    required: true,
                    message: '请选择上级目录'
                  }
                ],
                initialValue: ['0']
              }
            ]"
            :tree-data="options"
            :replaceFields="{ title: 'title', value: 'key', key:'key', children: 'children' }"
            @change="onChange"
            :disabled="operation !== 'edit'"
            placeholder="请选择上级目录"
            allow-clear
          >
          </a-tree-select>
        </a-form-item>
        <a-form-item v-show="false" label="上级目录id">
          <a-input
            v-decorator="[
              'parentId',
              {
                rules: [
                  {
                    required: true
                  }
                ],
                initialValue: '0'
              }
            ]"
          />
        </a-form-item>
        <a-form-item label="分组名称">
          <a-input
            :disabled="isFirstLevel && operation === 'edit'"
            v-decorator="[
              'name',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入目录名称'
                  },
                  {
                    max: 255,
                    message: '长度不能超过255个字符'
                  }
                ]
              }
            ]"
            placeholder="请输入目录名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="排序序号">
          <a-input-number
            :disabled="operation === 'detail'"
            :precision="0"
            :min="1"
            v-decorator="['sortSn']"
          />
        </a-form-item>
        <a-form-item label="图标地址">
          <!-- <a-input
            :disabled="operation === 'detail'"
            v-decorator="['iconUrl']"
            placeholder="请输入图标地址"
            allow-clear
          /> -->
          <gaf-icon-picker v-decorator="['iconUrl']" placeholder="请选择图标"></gaf-icon-picker>
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea
            :disabled="operation === 'detail'"
            v-decorator="[
              'description',
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
        <div v-if="operation === 'detail'">
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
      <div class="drawer-footer-div">
          
          
          <!-- <span v-if="!(isFirstLevel && operation === 'edit') ">
          <a-popconfirm
              v-if="operation === 'edit'"
              @confirm="deleteDataOrCancle"
              class="cancel-modal"
              title="删除后无法恢复，确认是否继续?"
              ok-text="确认"
              cancel-text="取消"
            >
              <a-button>{{deleteOrCacel}}</a-button>
          </a-popconfirm>
          <button
            @click="deleteDataOrCancle"
            v-else
            class="cancel-modal"
            >{{ deleteOrCacel }}</button
          >
          </span>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
          <button
            @click="deleteDataOrCancle"
            v-if="operation === 'add'"
            class="cancel-modal"
            >{{ deleteOrCacel }}</button>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           <a-button @click="submitForm" type="primary" :loading="loading" class="submit-gray">
            {{ submitButtonText }}
          </a-button>
          </div>
        </div>
        </div>
  </div>
</template>

<script>
export default {
  props: {
    // 当parentId为'0'或为空时表示一级分类目录
    editData: {
      type: Object,
      default: null
    },
    // add表示新增 edit表示表示编辑 detail表示详情
    operation: {
      type: String,
      default: 'edit'
    },
    options: {
      type: Array,
      default: () => [
        {
          key: '0',
          title: '无'
        }
      ]
    },
    // 控制表单不同名字的显示
    catalogType: {
      type: String,
      default: '0'
    }
  },
  data() {
    return {
      dataId: '',
      casecaderDisabled: false,
      loading: false,
    }
  },
  computed: {
    deleteOrCacel() {
      if (this.operation === 'edit') {
        return '删除'
      } else if (this.operation === 'add') {
        return '取消'
      } else {
        return ''
      }
    },
    submitButtonText() {
      if (this.operation === 'edit') {
        return '保存'
      } else if (this.operation === 'add') {
        return '添加'
      } else {
        return ''
      }
    },
    title() {
      if (this.operation === 'edit') {
        if (!this.isFirstLevel) {
          if (this.catalogType === '1') {
            return '编辑模块分组'
          } else if (this.catalogType === '2') {
            return '编辑API分组'
          } else if (this.catalogType === '3') {
            return '编辑角色分组'
          } else if (this.catalogType === '4') {
            return '编辑菜单分组'
          } else {
            return '编辑子目录'
          }
        } else {
          return '编辑分组根目录'
        }
      } else if (this.operation === 'add') {
        if (!this.isFirstLevel) {
          if (this.catalogType === '1') {
            return '添加模块分组'
          } else if (this.catalogType === '2') {
            return '添加API分组'
          } else if (this.catalogType === '3') {
            return '添加角色分组'
          } else if (this.catalogType === '4') {
            return '添加菜单分组'
          } else {
            return '添加子目录'
          }
        } else {
          return '添加分组根目录'
        }
      } else {
        return ''
      }
    },
    isFirstLevel() {
      return this.editData.parentId === '0'
    },
    urlPath() {
      if (this.operation === 'edit' || this.operation === 'add') {
        if (!this.isFirstLevel) {
          if (this.catalogType === '1') {
            return 'auth-resource-modules/module-catalog-id'
          } else if (this.catalogType === '2') {
            return 'auth-resource-apis/api-catalog-id'
          } else if (this.catalogType === '3') {
            return 'auth-roles/role-catalog-id'
          } else if (this.catalogType === '4') {
            return 'auth-resource-menus/menu-catalog-id'
          } else {
            return '添加子目录'
          }
        } else {
          return '添加分组根目录'
        }
      } else{
        return ''
      }
    }
  },
  watch: {
    editData: function(newEditData) {
      console.log(newEditData,'1')
      this.dataId = newEditData.catalogId
      delete newEditData.catalogId
      delete newEditData.code
      delete newEditData.status
      delete newEditData.createdTime
      delete newEditData.createdBy
      delete newEditData.updatedTime
      delete newEditData.updatedBy
      delete newEditData.tenantId
      delete newEditData.sysComponentId
      if (newEditData.parentPath != null) {
        newEditData.parentId =
          newEditData.parentPath[newEditData.parentPath.length - 1]
      }
      this.addOrEditForm.resetFields()
      this.addOrEditForm.setFieldsValue({ ...newEditData })
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    console.log(this.editData,'2')
    this.dataId = this.editData.catalogId
    delete this.editData.catalogId
    delete this.editData.code
    delete this.editData.status
    delete this.editData.createdTime
    delete this.editData.createdBy
    delete this.editData.updatedTime
    delete this.editData.updatedBy
    this.addOrEditForm.setFieldsValue({ ...this.editData })
  },
  methods: {
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = `/sys-mgt/sys-catalogs`
        const data = this.addOrEditForm.getFieldsValue()
        this.loading = true
        if (this.dataId) {
          url = url + '/' + this.dataId
          const { name, sortSn, iconUrl, description, parentId } = data
          const rst = await this.$axios.put(url, {
            name,
            sortSn,
            iconUrl,
            description,
            parentId
          })
          if (rst.data.isSuccessed) {
            this.$message.success('更新成功')
            this.$emit('update-success', rst.data.data)
          } else {
            this.$message.error(`更新失败,原因:${rst.data.message}`)
          }
        } else {
          const rst = await this.$axios.post(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('添加成功')
            this.$emit('add-success', rst.data.data)
          } else {
            this.$message.error(`添加失败,原因:${rst.data.message}`)
          }
        }
        this.loading = false
      })
    },
    async deleteDataOrCancle() {
      if (this.operation === 'edit') {
        if (this.dataId) {
          const url = `/sys-mgt/sys-catalogs/` + this.dataId
          const rst = await this.$axios.delete(url)
          if (rst.data.isSuccessed) {
            this.$message.success('删除成功')
            this.$emit('delete-success', rst.data.data)
          } else {
            this.$message.error('删除失败，原因: ' + rst.data.message)
          }
        }
      } else if (this.operation === 'add') {
        this.$emit(
          'cancleWhenAdd',
          this.addOrEditForm.getFieldValue('parentId')
        )
      }
    },
    async onChange(value) {
      
      // const parentId = path[path.length - 1]
      // this.addOrEditForm.setFieldsValue({ parentId: parentId })
      const res = await this.$axios.get(`/authority/${this.urlPath}/${value}`)
      if (res.data.isSuccessed) {
        if (res.data.data && res.data.data > 0) {
          this.$message.info(`分组下有数据,无法更改到该分组下`)
          this.addOrEditForm.setFields({
            parentId: {value: '', errors: [Error]}
          })
        }
      }
    },
    resetFormFields() {
      this.addOrEditForm.resetFields()
    },
    setFormFields(fieldsValue) {
      if (fieldsValue) {
        this.addOrEditForm.setFieldsValue(fieldsValue)
      }
    }
  }
}
</script>

<style scoped>
.sysbtn-style {
  text-align: center;
  margin: 10px auto;
  width: 100px;
  font-size: 12px;
  cursor: pointer;
}
</style>
