<template>
  <portal-custom />
</template>
<script>
import { portalCustom } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    portalCustom,
  },
}
</script>
