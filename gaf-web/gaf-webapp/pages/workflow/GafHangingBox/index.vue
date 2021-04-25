<template>
  <gaf-hanging-box />
</template>
<script>
import { GafHangingBox } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    GafHangingBox,
  },
}
</script>
