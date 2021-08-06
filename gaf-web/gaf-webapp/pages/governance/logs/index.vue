<template>
  <logs />
</template>
<script>
import { logs } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    logs,
  },
}
</script>
