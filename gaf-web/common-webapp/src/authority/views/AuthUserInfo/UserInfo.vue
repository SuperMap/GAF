<template>
  <div class="center">
    <div>
      <!-- <h4 style="font-size:20px;">个人信息</h4> -->
      <a-breadcrumb separator=">">
        <span class="vertical-line">| </span>
        <a-breadcrumb-item class="text-bolder">个人信息</a-breadcrumb-item>
      </a-breadcrumb>
      <table border="1" class="table-style">
        <tr>
          <td>真实姓名</td>
          <td>{{ userInfo.realName }}</td>
          <td>用户名</td>
          <td>{{ userInfo.name }}</td>
        </tr>
        <tr>
          <td>手机号</td>
          <td>{{ userInfo.mobileNumber }}</td>
          <td>邮箱</td>
          <td>{{ userInfo.email }}</td>
        </tr>
        <tr>
          <td>身份证号</td>
          <td colspan="3">{{ userInfo.idNumber }}</td>
        </tr>
        <tr>
          <td>地址</td>
          <td colspan="3">{{ userInfo.address }}</td>
        </tr>
        <tr>
          <td>部门</td>
          <td>{{ userInfo.departmentName }}</td>
          <td>岗位</td>
          <td>{{ userInfo.postName }}</td>
        </tr>
        <tr>
          <td>描述</td>
          <td colspan="3">{{ userInfo.description }}</td>
        </tr>
      </table>
    </div>
    <div style="margin-top: 10px">
      <!-- <h4 style="font-size:20px;">安全设置</h4> -->
      <a-breadcrumb separator=">">
        <span class="vertical-line">| </span>
        <a-breadcrumb-item class="text-bolder">安全设置</a-breadcrumb-item>
      </a-breadcrumb>
      
      <a-button class="reset-password" @click="HandleClick">
        修改密码
      </a-button>
    </div>
    <a-modal
      v-model="visible"
      :destroyOnClose="true"
      :centered="true"
      title="修改密码"
      on-ok="handleOk"
    >
      <template slot="footer">
        <button key="back" @click="handleCancel" class="cancel-modal">
          返回
        </button>
        <a-button
          key="submit"
          type="primary"
          :loading="loading"
          @click="handleOk"
          class="submit-gray"
        >
          提交
        </a-button>
      </template>
      <change-password ref="passwordForm"></change-password>
    </a-modal>
  </div>
</template>

<script>
import moment from 'moment'
import ChangePassword from './ChangePassword'
export default {
  components: {
    ChangePassword
  },
  props: {
    userInfo: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      visible: false,
      loading: false
    }
  },
  computed: {},
  watch: {},
  beforeMount() {},
  mounted() {},
  methods: {
    moment,
    handleOk() {
      this.loading = true
      const form = this.$refs.passwordForm.form
      form.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        const url = `/authority/auth-users/password-change`
        const data = form.getFieldsValue()
        this.loading = true
        const rst = await this.$axios.put(url, data)
        if (rst.data.isSuccessed) {
          this.$message.success('密码变更成功')
          this.visible = false
        } else {
          this.$message.error(`密码变更失败,原因:${rst.data.message}`)
        }
        this.loading = false
      })
    },
    handleCancel() {
      this.visible = false
    },
    HandleClick() {
      this.visible = true
    },
    // 重置密码
    async resetPassword() {
      if (this.dataId) {
        const url = `/authority/auth-users/${this.dataId}/reset-password`
        const res = await this.$axios.post(url)
        if (res.data.isSuccessed) {
          this.$message.success('密码重置成功,请通知用户注意查看邮箱！')
        } else {
          this.$message.error(`密码重置失败,原因:${res.data.message}`)
        }
      }
    }
  }
}
</script>

<style lang="less" scoped>
.reset-password {
  background-color: rgb(95, 192, 221);
  color: white;
}
.center {
  margin: auto;
  width: 60%;
  padding: 10px;
}

.table-style {
  height: 100%;
  width: 100%;
  text-align: center;
}
td {
  padding: 10px;
}
</style>
