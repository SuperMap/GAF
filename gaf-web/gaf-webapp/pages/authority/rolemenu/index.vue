<template>
  <role-menu />
</template>
<script>
import { RoleMenu } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    RoleMenu,
  },
}
</script>
