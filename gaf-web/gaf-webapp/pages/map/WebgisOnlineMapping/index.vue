<template>
  <webgis-online-mapping />
</template>
<script>
import { WebgisOnlineMapping } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    WebgisOnlineMapping,
  },
}
</script>
