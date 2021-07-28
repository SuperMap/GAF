<template>
  <page-layout :keep-alive="false" />
</template>

<script>
export default {
  async beforeMount() {
    try {
      const routePath = this.$route.path
      if (routePath === '/authentication/login') {
        return
      }
      const url = '/portal/user/profile/detail'
      const data = await this.$axios.$get(url)
      // const data = {
      //   user: window.GAF.user,
      //   config: window.GAF.config,
      //   navs: window.GAF.navs,
      // }
      // console.log('map', data)
      if (data !== null) {
        data.navs = []
      }
      this.$store.commit('updateGlobalInfo', data)
      this.setUser(data.user)
    } catch (e) {
      // this.$message.error('获取用户信息失败')
      // debugger
      // this.$router.push('/authentication/login')
    }
  },
  methods: {
    setUser(user) {
      window.localStorage.setItem('luser', JSON.stringify(user))
    },
  },
}
</script>
