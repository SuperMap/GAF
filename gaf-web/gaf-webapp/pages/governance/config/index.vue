<template>
  <config />
</template>
<script>
import { config } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    config,
  },
}
</script>
