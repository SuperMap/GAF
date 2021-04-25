<template>
  <div ref="c" class="side-bar animated">
    <button @click="ontoggle">toggle</button>
  </div>
</template>

<script>
export default {
  name: 'SideBar',
  data() {
    return {
      ztai: true,
    }
  },
  methods: {
    ontoggle() {
      if (this.ztai) {
        this.$refs.c.style.width = '0'
      } else {
        this.$refs.c.style.width = '200px'
      }
      this.ztai = !this.ztai
    },
  },
}
</script>

<style scoped>
.side-bar {
  position: fixed;
  top: 100px;
  left: 0;
  width: 200px;
  height: 300px;
  z-index: 1;
  background-color: #ebebeb;
  transition: width 0.3s linear;
}
</style>
