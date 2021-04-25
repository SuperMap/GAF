<template>
  <properties :item="item" />
</template>

<script>
import Properties from '../Properties'
export default {
  name: 'GafProperties',
  components: {
    Properties,
  },
  props: {
    item: {
      type: Object,
      required: false,
      default: null,
    },
  },
}
</script>
