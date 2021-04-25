<template>
  <proj-group />
</template>
<script>
import { projGroup } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    projGroup,
  },
}
</script>
