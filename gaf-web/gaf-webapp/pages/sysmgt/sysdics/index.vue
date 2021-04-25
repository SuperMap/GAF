<template>
  <dics />
</template>
<script>
import { dics } from 'common-webbase'

export default {
  middleware: 'auth',
  components: {
    dics,
  },
}
</script>
