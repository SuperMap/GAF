<template>
  <storage-management />
</template>
<script>
import { StorageManagement } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    StorageManagement,
  },
}
</script>
