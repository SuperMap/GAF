<template>
  <gaf-accept-box />
</template>
<script>
import { GafAcceptBox } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    GafAcceptBox,
  },
}
</script>
