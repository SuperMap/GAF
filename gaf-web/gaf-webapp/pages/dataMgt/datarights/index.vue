<template>
  <data-rights />
</template>
<script>
import { DataRights } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    DataRights,
  },
}
</script>
