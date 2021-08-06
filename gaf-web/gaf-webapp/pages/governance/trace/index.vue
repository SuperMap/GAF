<template>
  <trace />
</template>
<script>
import { trace } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    trace,
  },
}
</script>
