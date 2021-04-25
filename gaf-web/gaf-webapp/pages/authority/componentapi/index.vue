<template>
  <component-api />
</template>
<script>
import { ComponentApi } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    ComponentApi,
  },
}
</script>
