<template>
  <overlay-analysis />
</template>
<script>
import { OverlayAnalysis } from 'common-mapapp'
export default {
  middleware: 'auth',
  components: {
    OverlayAnalysis,
  },
}
</script>
