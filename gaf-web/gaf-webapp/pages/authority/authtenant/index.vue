<template>
  <auth-tenant />
</template>
<script>
import { AuthTenant } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    AuthTenant,
  },
}
</script>
