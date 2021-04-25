<template>
  <webgis-service />
</template>
<script>
import { WebgisService } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    WebgisService,
  },
}
</script>
