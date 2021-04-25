<template>
  <auth-user />
</template>
<script>
import { AuthUser } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    AuthUser,
  },
}
</script>
