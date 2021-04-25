<template>
  <div>
    <button class="btn-skip" @click="btnSkip">
      <a-icon
        :type="isLeaf ? 'control' : 'profile'"
        style="color: rgb(24, 113, 186); fontsize: 32px"
        class="svg-left"
      />{{ data.Name
      }}<a-icon
        :type="isLeaf ? '' : 'right'"
        style="color: rgb(24, 113, 186); fontsize: 32px"
        class="svg-right"
      />
    </button>
  </div>
</template>
<script>
import '../css/workFlow.css'
export default {
  name: 'GafBtnSkip',
  props: {
    data: {
      type: Object,
      default: () => {},
    },
    isLeaf: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {}
  },
  methods: {
    btnSkip() {
      this.$emit('click', this.data, this.isLeaf)
      // console.log(this.isLeaf)
    },
  },
}
</script>
<style scoped>
.btn-skip {
  cursor: pointer;
  border: none;
  width: 23%;
  height: 70px;
  float: left;
  margin: 1%;
  padding-left: 2%;
  font-size: 24px;
  font-weight: bolder;
  background-color: rgb(240, 242, 245);
  overflow: hidden;
  border-top-right-radius: 35px;
  border-bottom-left-radius: 35px;
  text-align: left;
  transition: all 0.2s linear;
}

.btn-skip:hover {
  background-color: rgb(24, 113, 186);
  color: white;
  font-size: 26px;
}
</style>
