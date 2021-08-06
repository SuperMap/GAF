<template>
  <hosts />
</template>
<script>
import { hosts } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    hosts,
  },
}
</script>
