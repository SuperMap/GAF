<template>
  <div class="page-single">
    <auth-user-info :userInfo="userInfo"></auth-user-info>
  </div>
</template>

<script>
import AuthUserInfo from '../../views/AuthUserInfo/UserInfo'
import '~/assets/css/common.css'

export default {
  components: {
    AuthUserInfo
  },
  data() {
    return {
      userInfo: {}
    }
  },
  computed: {},
  watch: {},
  created() {
    this.getUserInfo()
  },
  methods: {
    async getUserInfo() {
      const url = `/authority/auth-users/info`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.userInfo = res.data
      } else {
        this.$message.error(`获取个人信息失败,原因：${res.message}`)
      }
    }
  }
}
</script>
