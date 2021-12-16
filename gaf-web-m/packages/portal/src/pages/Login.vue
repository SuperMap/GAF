<template>
  <div>
    <a-button @click="onLogin">login</a-button>
    <a-button @click="validate">validate</a-button>
  </div>
</template>

<script>
export default {
  methods: {
    async onLogin () {
      try {
        const res = await this.$axios({
          method: 'post',
          url: '/authentication/token',
          data: 'username=sys_admin&password=123456',
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
        this.$router.push('/')
      } catch (ex) {
        console.error(ex)
      }
    },
    async validate () {
      const data = await this.$axios.$get('/data-mgt/sys-resource-datasources?pageSize=10&pageNum=1&isSdx=true')
      console.log(data)
    }
  }
}
</script>
