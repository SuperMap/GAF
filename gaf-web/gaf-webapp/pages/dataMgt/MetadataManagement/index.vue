<template>
  <metadata-management />
</template>
<script>
import { MetadataManagement } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    MetadataManagement,
  },
}
</script>
