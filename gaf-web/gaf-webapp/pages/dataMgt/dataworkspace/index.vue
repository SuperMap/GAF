<template>
  <data-workspace />
</template>
<script>
import { DataWorkspace } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    DataWorkspace,
  },
}
</script>
