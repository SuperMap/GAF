<template>
  <code-generate />
</template>
<script>
import { CodeGenerate } from 'common-gaf'
export default {
  middleware: 'auth',
  components: {
    CodeGenerate,
  },
}
</script>
