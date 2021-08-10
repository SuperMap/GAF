<template>
  <containers />
</template>
<script>
import { containers } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    containers,
  },
}
</script>
