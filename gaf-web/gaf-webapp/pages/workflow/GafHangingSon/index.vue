<template>
  <gaf-hanging-son />
</template>
<script>
import { GafHangingSon } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    GafHangingSon,
  },
}
</script>
