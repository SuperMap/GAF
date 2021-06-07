<template>
  <div class="page-modal-box">
    <a-form
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 15 }"
      :form="addOrEditForm"
      layout="horizontal"
    >
      <a-form-item label="工程组名称">
        <a-input
          v-decorator="[
            'name',
            {
              rules: [
                {
                  required: true,
                  message: '请输入工程组名称',
                },
              ],
            },
          ]"
          placeholder="请输入工程组名称"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="工程组路径">
        <div style="margin-bottom: 16px">
          <a-input
            v-decorator="[
              'path',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入工程组名称',
                  },
                ],
              },
            ]"
            placeholder="请输入工程组路径"
            allow-clear
          >
            <a-select
              slot="addonBefore"
              v-model="tenantBriefNameEn"
              @change="changeTenantBriefHandler"
            >
              <a-select-option
                v-for="(item, index) in tenants"
                :key="index"
                :value="item.briefNameEn"
              >
                {{ item.briefNameEn }}/
              </a-select-option>
            </a-select>
          </a-input>
        </div>
      </a-form-item>
      <a-form-item label="可见性">
        <a-select
          v-decorator="[
            'visibility',
            {
              initialValue: 'PRIVATE',
            },
          ]"
        >
          <a-select-option value="PRIVATE"> 私有 </a-select-option>
          <a-select-option value="PUBLIC"> 公开 </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="描述">
        <a-textarea
          v-decorator="['describe']"
          placeholder="请输入描述"
          style="height: 80px"
        />
      </a-form-item>
      <div>
        <a-button
          style="margin: 15px 0 15px 56%"
          class="submit-gray"
          type="primary"
          :loading="loading"
          @click="submitForm"
        >
          确定
        </a-button>
        <a-button
          class="cancel-modal"
          @click="
            () => {
              addOrEditForm.resetFields()
              $emit('back')
            }
          "
        >
          取消
        </a-button>
      </div>
    </a-form>
  </div>
</template>

<script>
import qs from 'qs'

export default {
  props: {
    editData: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      dataId: '',
      tenantBriefNameEn: '',
      tenants: [],
      loading: false,
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
    this.getTenants()
  },
  mounted() {
    this.dataId = this.editData.projGroupId
    delete this.editData.projGroupId
    this.addOrEditForm.setFieldsValue({ ...this.editData })
  },
  methods: {
    async getTenants() {
      const res = await this.$axios.$get('/authority/auth-tenants/current-user')
      if (res.isSuccessed) {
        this.tenants = [res.data]
        this.tenantBriefNameEn = this.tenants[0].briefNameEn
      } else {
        this.$message.error(`查询租户失败,原因:${res.message}`)
      }
    },
    changeTenantBriefHandler(val) {
      this.tenantBriefNameEn = val
    },
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        const data = this.addOrEditForm.getFieldsValue()
        this.loading = true
        data.path = this.tenantBriefNameEn + '/' + data.path
        data.tenantId = this.tenants.filter(
          (item) => item.briefNameEn === this.tenantBriefNameEn
        )[0].id
        const url = '/proj/groups' + '?' + qs.stringify(data)
        const rst = await this.$axios.post(url)
        if (rst.data.isSuccessed) {
          this.$message.success('添加成功')
        } else {
          this.$message.error(`添加失败,原因:${rst.data.message}`)
        }
        this.loading = false
        this.addOrEditForm.resetFields()
        this.$emit('submit')
      })
    },
  },
}
</script>
