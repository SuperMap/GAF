<template>
  <login :login-title="loginTitle" :login-bg="loginBg" />
</template>
<script>
import { login } from 'common-webbase'
export default {
  layout: 'login',
  components: {
    login,
  },
  data() {
    return {
      loginTitle: './img/GAF.png',
      loginBg: './img/bg2.jpeg',
    }
  },
}
</script>
