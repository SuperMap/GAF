<template>
  <webgis-toolbar-button />
</template>
<script>
import { WebgisToolbarButton } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    WebgisToolbarButton,
  },
}
</script>
