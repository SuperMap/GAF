<template>
  <div class="page-container">
    <div class="grid-container">
    <div class="drawer-header">
      <template>
        <a-breadcrumb separator=">" class="modal-line">
        <a-breadcrumb-item class="text-bolder"><span class="vertical-line">| </span>{{ title }}</a-breadcrumb-item>
      </a-breadcrumb>
      </template>
    </div>
    <div class="drawer-content">
    <a-form
      :form="addOrEditForm"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 15 }"
      layout="horizontal"
    >
      <a-form-item v-show="false" label="上级部门id">
        <a-input
          :disabled="true"
          v-decorator="[
            'parentId',
            {
              rules: [
                {
                  message: '上级部门id'
                }
              ],
              initialValue: '无'
            }
          ]"
        >
        </a-input>
      </a-form-item>
      <a-form-item label="上级部门">
        <a-input
          :disabled="true"
          v-decorator="[
            'parentName',
            {
              rules: [
                {
                  message: '上级部门'
                }
              ],
              initialValue: '无'
            }
          ]"
        >
        </a-input>
      </a-form-item>
      <a-form-item label="名称">
        <a-input
          :disabled="operation === 1"
          v-decorator="[
            'departmentName',
            {
              rules: [
                {
                  required: true,
                  message: '名称不能为空！'
                },
                {
                  max: 255,
                  message: '长度不能超过255个字符'
                }
              ]
            }
          ]"
          placeholder="请输入名称"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="排序序号">
        <a-input-number
          :disabled="operation === 1"
          v-decorator="['sortSn']"
          :precision="0"
          :min="1"
        />
      </a-form-item>
      <a-form-item label="部门类型">
        <a-select
          :disabled="operation === 1"
          v-decorator="[
            'departmentType',
            {
              rules: [
                {
                  message: '请选择部门类型'
                }
              ],
              initialValue: '0'
            }
          ]"
          placeholder="请输入部门类型"
          auto-size
        >
          <a-select-option value="0">无</a-select-option>
          <a-select-option value="1">管理</a-select-option>
          <a-select-option value="2">研发</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="是否第三方">
        <a-radio-group
          :disabled="operation === 1"
          v-decorator="[
            'isThirdParty',
            {
              initialValue: false
            }
          ]"
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
      <a-form-item label="英文名称">
        <a-input
          :disabled="operation === 1"
          v-decorator="[
            'nameEn',
            {
              rules: [
                {
                  max: 255,
                  message: '长度不能超过255个字符'
                }
              ]
            }
          ]"
          placeholder="请输入英文名称"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="简称">
        <a-input
          :disabled="operation === 1"
          v-decorator="[
            'briefName',
            {
              rules: [
                {
                  max: 255,
                  message: '长度不能超过255个字符'
                }
              ]
            }
          ]"
          placeholder="请输入简称"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="描述">
        <a-textarea
          :disabled="operation === 1"
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
      
    </a-form>
    </div>
    <div class="drawer-footer">
        <button v-if="false" @click="cancelDelete" class="cancel-modal">
          删除
        </button>
        &nbsp;&nbsp;&nbsp;
        <button @click="setTenantSynchronization" class="submit-gray">
          同步
        </button>
        &nbsp;&nbsp;&nbsp;
        <a-button @click="submitForm" type="primary" :loading="loading" class="submit-gray">
          {{ operation === 2 ? '新增' : '保存' }}
        </a-button>
        &nbsp;&nbsp;&nbsp;
        <button v-if="operation === 2" @click="cancelDelete" class="cancel-modal">
          取消
        </button>
      </div>
    </div>
    <div>
      <gaf-modal
        v-model="mapList"
        :width="1000"
        :footer="null"
        title="第三方组件映射"
      >
        <mapping-process
          :tenantRst="tenantRst"
          :MapDataId="dataId"
        ></mapping-process>
      </gaf-modal>
    </div>
  </div>
</template>

<script>
import moment from 'moment'
import MappingProcess from './MappingProcess'
export default {
  components: {
    MappingProcess
  },
  props: {
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
      tenantRst: [],
      mapList: false,
      loading: false
    }
  },
  computed: {
    title() {
      if (this.operation === 3) {
        return '编辑部门'
      } else if (this.operation === 2) {
        return '新增部门'
      } else {
        return ''
      }
    }
  },
  watch: {
    editData: function(newEditData) {
      this.setEditData({...newEditData})
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    this.setEditData({...this.editData})
  },
  methods: {
    moment,
    // 同步
    async setTenantSynchronization() {
      this.mapList = true
      this.id = this.dataId
      const url = `/authority/auth-p3-mapping-rule/list-by-type/`
      const res = await this.$axios.get(url + 2 + '/' + this.id)
      this.tenantRst = res.data.data
    },
    setEditData(newEditData) {
      this.dataId = newEditData.departmentId
      delete newEditData.departmentId
      delete newEditData.adminId
      delete newEditData.code
      delete newEditData.createdBy
      delete newEditData.status
      delete newEditData.tenantId
      delete newEditData.updatedBy
      delete newEditData.updatedTime
      delete newEditData.createdTime
      this.addOrEditForm.setFieldsValue({ ...newEditData })
    },
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = `/authority/auth-departments`
        const data = this.addOrEditForm.getFieldsValue()
        this.loading = true
        if (this.dataId) {
          url = url + '/' + this.dataId
          const rst = await this.$axios.put(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('更新成功')
            this.$emit('updateDepartmentSuccess', rst.data.data)
          } else {
            this.$message.error('更新失败:' + rst.data.message)
          }
        } else {
          const rst = await this.$axios.post(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('添加成功')
            this.$emit('addDepartmentSuccess', rst.data.data)
          } else {
            this.$message.error('添加失败:' + rst.data.message)
          }
        }
        this.loading = false
      })
    },
    async cancelDelete() {
      if (this.operation === 2) {
        this.$emit(
          'cancleWhenAddDepartment',
          this.addOrEditForm.getFieldValue('parentId')
        )
      } else if (this.operation === 3) {
        if (this.dataId) {
          if (this.editData.parentId && this.editData.parentId === '0') {
            this.$message.info('不能删除默认根部门')
          } else {
            const url = `/authority/auth-departments/` + this.dataId
            const rst = await this.$axios.delete(url)
            if (rst.data.isSuccessed) {
              this.$message.success('删除成功')
              this.$emit('deleteDepartmentSuccess', rst.data.data)
            } else {
              this.$message.error(`删除失败,原因：${rst.data.message}`)
            }
          }
        }
      }
    },
    resetFields() {
      this.addOrEditForm.resetFields()
    }
  }
}
</script>

<style lang="less" scoped>
// button {
//   width: 80px;
//   font-size: 12px;
//   cursor: pointer;
//   margin-right: 10px;
// }

.page-container {
  width: 100%;
  height: 100%;
}

.btn-div {
  text-align: center;
  margin: 15px 0;
}
.title {
  font-size: 14px;
  margin-top: 5px;
}
</style>
