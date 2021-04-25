<template>
  <gaf-need-dealt />
</template>
<script>
import { GafNeedDealt } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    GafNeedDealt,
  },
}
</script>
