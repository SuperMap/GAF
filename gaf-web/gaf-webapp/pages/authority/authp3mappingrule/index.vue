<template>
  <auth-p3-mapping-rule />
</template>
<script>
import { AuthP3MappingRule } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    AuthP3MappingRule,
  },
}
</script>
