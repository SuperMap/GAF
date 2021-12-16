<template>
  <gaf-page-layout :keep-alive="true" />
</template>

<script>
window.timoutId = null
window.requestAnimaFrame = window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame ||
function (callback) {
  return window.setTimeout(callback, 1000 / 60)
}
window.cancelAnimaFrame = window.cancelAnimationFrame || window.webkitCancelAnimationFrame || window.mozCancelAnimationFrame || window.clearTimeout
export default {
  async beforeMount () {
    try {
      const url = '/portal/user/profile/detail'
      const data = await this.$axios.$get(url)
      this.$store.commit('updateGlobalInfo', data)
    } catch (e) {
      // this.$message.error('获取用户信息失败')
    }
  },
  mounted () {
    window.timeoutId = window.requestAnimaFrame(this.refreshToken)
  },
  beforeDestroy () {
    window.timoutId && window.cancelAnimaFrame(window.timoutId)
  },
  methods: {
    refreshToken () {
      this.getRefreshToken()
      window.timeoutId = window.requestAnimaFrame(this.refreshToken)
    },
    async getRefreshToken () {
      try {
        const strRefreshToken = window.localStorage.getItem('token') || '{}'
        const refreshToken = JSON.parse(strRefreshToken)
        const isLogin = Object.keys(this.$store.state.user).length > 0
        if (isLogin && refreshToken.expiresIn < 600 && refreshToken.refreshToken) {
          const res = await this.$axios({
            method: 'post',
            url: '/authentication/token/refresh',
            data: `refresh_token=${refreshToken.refreshToken}`,
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            }
          })
          const { data: { data } } = res
          const token = {
            refreshToken: data.refresh_token,
            type: data.token_type,
            accessToken: data.access_token,
            expiresIn: data.expires_in
          }
          window.localStorage.setItem('token', JSON.stringify(token))
        }
      } catch {
        console.error('刷新token失败')
      }
    }
  }
}
</script>
