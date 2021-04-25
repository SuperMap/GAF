<template>
  <gaf-all-business />
</template>
<script>
import { GafAllBusiness } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    GafAllBusiness,
  },
}
</script>
