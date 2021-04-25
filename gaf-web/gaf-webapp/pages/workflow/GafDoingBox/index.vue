<template>
  <gaf-doing-box />
</template>
<script>
import { GafDoingBox } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    GafDoingBox,
  },
}
</script>
