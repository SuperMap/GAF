<template>
  <role-module />
</template>
<script>
import { RoleModule } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    RoleModule,
  },
}
</script>
