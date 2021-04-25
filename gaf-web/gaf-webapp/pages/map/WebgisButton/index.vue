<template>
  <webgis-button />
</template>
<script>
import { WebgisButton } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    WebgisButton,
  },
}
</script>
