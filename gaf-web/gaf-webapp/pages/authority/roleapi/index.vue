<template>
  <role-api />
</template>
<script>
import { RoleApi } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    RoleApi,
  },
}
</script>
