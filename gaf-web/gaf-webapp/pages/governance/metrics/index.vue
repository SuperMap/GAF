<template>
  <metrics />
</template>
<script>
import { metrics } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    metrics,
  },
}
</script>
