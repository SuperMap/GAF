<template>
  <gaf-assistance-son />
</template>
<script>
import { GafAssistanceSon } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    GafAssistanceSon,
  },
}
</script>
