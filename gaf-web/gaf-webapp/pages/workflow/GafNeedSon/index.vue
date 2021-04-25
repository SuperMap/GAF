<template>
  <gaf-need-son />
</template>
<script>
import { GafNeedSon } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    GafNeedSon,
  },
}
</script>
