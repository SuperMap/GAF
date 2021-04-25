<template>
  <gaf-accept-son />
</template>
<script>
import { GafAcceptSon } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    GafAcceptSon,
  },
}
</script>
