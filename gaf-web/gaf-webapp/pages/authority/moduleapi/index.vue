<template>
  <module-api />
</template>
<script>
import { ModuleApi } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    ModuleApi,
  },
}
</script>
