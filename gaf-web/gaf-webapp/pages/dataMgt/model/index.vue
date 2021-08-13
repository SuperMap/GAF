<template>
  <model />
</template>
<script>
import { model } from 'common-webbase'
export default {
  middleware: 'auth',
  components: {
    model,
  },
}
</script>
