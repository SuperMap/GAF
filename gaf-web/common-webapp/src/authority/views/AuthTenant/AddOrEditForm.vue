<template>
  <div class="page-single">
    <template>
      <a-breadcrumb separator=">">
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
        <a-form-item label="租户名称">
          <a-input
            :disabled="operation === 1 || operation === 3"
            v-decorator="[
              'tenantName',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入租户名称'
                  }
                ]
              }
            ]"
            placeholder="请输入租户名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="租户描述">
          <a-textarea
            :disabled="operation === 1"
            v-decorator="['description']"
            placeholder="请输入租户描述"
            auto-size
          />
        </a-form-item>
        <a-form-item label="租户类别">
          <a-radio-group
            :disabled="operation === 1"
            v-decorator="['type', { initialValue: '1' }]"
            button-style="solid"
          >
            <a-radio-button value="1">
              开发运营类
            </a-radio-button>
            <a-radio-button value="2">
              运营类
            </a-radio-button>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="英文名称">
          <a-input
            :disabled="operation === 1"
            v-decorator="['nameEn']"
            placeholder="请输入英文名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="英文简称">
          <a-input
            :disabled="operation === 1 || operation === 3"
            v-decorator="[
              'briefNameEn',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入英文简称'
                  }
                ]
              }
            ]"
            placeholder="请输入英文简称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="编码">
          <a-input
            :disabled="operation === 1"
            v-decorator="['code']"
            placeholder="请输入编码"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="状态">
          <a-switch
            :disabled="operation === 1"
            v-decorator="[
              'status',
              { valuePropName: 'checked', initialValue: true }
            ]"
            checked-children="启用"
            un-checked-children="禁用"
          />
        </a-form-item>
        <a-form-item v-show="false" label="初始管理员">
          <a-input
            :disabled="operation === 1"
            v-decorator="['adminId']"
            allow-clear
          />
        </a-form-item>
        <a-form-item v-show="operation === 2 ? false : true" label="创建时间">
          <a-date-picker
            :disabled="operation === 1 || operation === 3"
            v-decorator="['createdTime']"
            show-time
          />
        </a-form-item>
        <a-form-item v-show="operation === 2 ? false : true" label="创建人">
          <a-input
            :disabled="operation === 1 || operation === 3"
            v-decorator="['createdBy']"
            allow-clear
          />
        </a-form-item>
        <a-form-item
          v-show="this.operation === 3 || this.operation === 2 ? false : true"
          label="修改时间"
        >
          <a-date-picker
            :disabled="operation === 1"
            v-decorator="['updatedTime']"
            :initialValue="moment()"
            format="YYYY-MM-DD HH:mm:SS"
            show-time
          />
        </a-form-item>
        <a-form-item
          v-show="this.operation === 3 || this.operation === 2 ? false : true"
          label="修改人"
        >
          <a-input
            :disabled="operation === 1"
            v-decorator="['updatedBy']"
            allow-clear
          />
        </a-form-item>
        <div style="text-align: center; margin-top: 15px;">
          <button v-show="operation !== 1" @click="submitForm" class="submit-gray">
            {{ submitTile }}
          </button>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <button @click="backToList" class="cancel-modal">{{this.operation === 1 ? "返回" : "取消"}}</button>
        </div>
      </a-form>
    </div>
    <gaf-modal
      v-model="createAdmin"
      :width="800"
      :footer="null"
      title="创建租户管理员"
    >
      <add-adm-from
        ref="myModal"
        :editData="editData"
        :operation="operation"
        @submit="handleAdminInfo"
        @back="handlerBack"
      />
    </gaf-modal>
  </div>
</template>

<script>
import moment from 'moment'
import addAdmFrom from './addAdmFrom'
export default {
  components: {
    addAdmFrom
  },
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
      createAdmin: false,
      adminInfo: {},
      submitTile: '下一步'
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    const copyData = { ...this.editData }
    this.dataId = copyData.tenantId
    if (this.dataId) {
      this.submitTile = '确定'
    }
    delete copyData.tenantId
    delete copyData.adminName
    if (copyData.createdTime) {
      copyData.createdTime = this.moment(new Date(copyData.createdTime))
    }
    if (copyData.updatedTime) {
      copyData.updatedTime = this.moment(new Date(copyData.updatedTime))
    }
    this.addOrEditForm.setFieldsValue({ ...copyData })
  },
  methods: {
    moment,
    async handleAdminInfo(val) {
      this.adminInfo = val
      const url = `/authority/auth-tenants/`
      const data = this.addOrEditForm.getFieldsValue()
      const tenantInitVo = {
        authTenant: data,
        authUser: this.adminInfo
      }
      const rst = await this.$axios.post(url + 'init', tenantInitVo)
      if (rst.data.isSuccessed) {
        this.$message.success('添加成功')
      } else {
        this.$message.error('添加失败!' + rst.data.message)
      }
      this.addOrEditForm.resetFields()
      this.$emit('submit')
    },
    handlerBack() {
      this.createAdmin = false
    },
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = `/authority/auth-tenants/`
        const data = this.addOrEditForm.getFieldsValue()
        if (this.dataId) {
          url = url + this.dataId
          const rst = await this.$axios.put(url, data)
          if (rst.data.isSuccessed) {
            this.$message.success('更新成功')
          } else {
            this.$message.error(`更新失败,原因:${rst.data.message}`)
          }
          this.addOrEditForm.resetFields()
          this.$emit('submit')
        } else {
          this.createAdmin = true
        }
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
  width: 100px;
  font-size: 12px;
  cursor: pointer;
}
</style>
