<template>
  <gaf-handle-box />
</template>
<script>
import { GafHandleBox } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    GafHandleBox,
  },
}
</script>
