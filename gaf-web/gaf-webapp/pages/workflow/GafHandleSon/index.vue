<template>
  <gaf-handle-son />
</template>
<script>
import { GafHandleSon } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    GafHandleSon,
  },
}
</script>
