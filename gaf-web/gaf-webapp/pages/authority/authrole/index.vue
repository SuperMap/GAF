<template>
  <auth-role />
</template>
<script>
import { AuthRole } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    AuthRole,
  },
}
</script>
