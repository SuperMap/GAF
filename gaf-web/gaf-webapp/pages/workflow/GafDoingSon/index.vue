<template>
  <gaf-doing-son />
</template>
<script>
import { GafDoingSon } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    GafDoingSon,
  },
}
</script>
