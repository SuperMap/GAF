<template>
  <routes />
</template>
<script>
import { routes } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    routes,
  },
}
</script>
