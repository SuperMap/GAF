<template>
  <a-row>
    <a-col :span="16">
      <a-row>
        <a-col :span="24">
          <a-card class="box-card" title="平台概况">
            <resource-monitor />
          </a-card>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="24">
          <a-card class="box-card" title="我的应用">
            <func-list />
          </a-card>
        </a-col>
      </a-row>
    </a-col>
    <a-col :span="8">
      <a-card class="box-card" title="通知公告">
        <notice />
      </a-card>
      <a-card class="box-card" title="产品日志">
        <product-log />
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
import FuncList from './components/FuncList'
import ResourceMonitor from './components/ResourceMonitor'
import Notice from './components/Notice'
import ProductLog from './components/ProductLog'
export default {
  components: {
    Notice,
    FuncList,
    ProductLog,
    ResourceMonitor,
  },
  data() {
    return {
      activeName: 'first',
    }
  },
}
</script>
<style scoped>
.box-card {
  margin-bottom: 15px;
  border-width: 0;
  box-shadow: none;
}
@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
.flex {
  display: flex;
  justify-content: space-around;
}
.resource-item {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 25%;
}
.flex-vertical {
  display: flex;
  flex-direction: column;
}
.title {
  font-size: 15px;
  font-weight: bold;
}
</style>
