<template>
  <auth-user-info />
</template>
<script>
import { AuthUserInfo } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    AuthUserInfo,
  },
}
</script>
