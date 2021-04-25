<template>
  <projects />
</template>
<script>
import { projects } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    projects,
  },
}
</script>
