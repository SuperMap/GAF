<template>
  <gaf-table-with-page
    :pagination="pagination"
    :data-source="administratorList"
    :row-key="(r, i) => i.toString()"
    :columns="columns"
    style="width: 100%;"
    bordered
    size="small"
    align="center"
  >
    <template slot="operation" slot-scope="text, record">
      <a @click.stop="() => handleResetPassword(record)" href="javascript:;" class="btn-configure">
        <a-icon type="setting" /> 重置密码
      </a>
    </template>
  </gaf-table-with-page>
</template>

<script>
export default {
  name: 'Administrators',
  props: {
    tenantId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      // 分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0
      },
      administratorList: [],
      columns: [
        {
          title: '姓名',
          dataIndex: 'name',
          key: 'name'
        },
        {
          title: '电话',
          dataIndex: 'mobileNumber',
          key: 'mobile_number'
        },
        {
          title: '邮箱',
          dataIndex: 'email',
          key: 'email'
        },
        {
          title: '地址',
          dataIndex: 'address',
          key: 'address'
        },
        {
          title: '操作',
          scopedSlots: { customRender: 'operation' }
        }
      ]
    }
  },
  watch: {
    tenantId() {
      this.getAdmins()
    }
  },
  beforeMount() {
    this.getAdmins()
  },
  methods: {
    async getAdmins() {
      const url = `/authority/auth-tenants/all-admin/` + this.tenantId
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.administratorList = res.data
      }
    },
    async handleResetPassword(data) {
      if (data.userId) {
        const url = `/authority/auth-users/${data.userId}/reset-password`
        const res = await this.$axios.post(url)
        if (res.data.isSuccessed) {
          this.$message.success('密码重置成功,请通知用户注意查看邮箱！')
        } else {
          this.$message.error(`密码重置失败,原因:${res.data.message}`)
        }
      }
    },
  }
}
</script>

<style scoped></style>
