<template>
  <gaf-detail>
    <template #breadcrumb>
      <a-breadcrumb separator=">">
        <a-breadcrumb-item>角色详情</a-breadcrumb-item>
      </a-breadcrumb>
    </template>
    <a-row>
      <a-col :span="2" class="title">名称：</a-col>
      <a-col :span="8">{{ roleItem.name }}</a-col>
    </a-row>
    <a-row>
      <a-col :span="2" class="title">描述：</a-col>
      <a-col :span="8">{{ roleItem.desc }}</a-col>
    </a-row>
  </gaf-detail>
</template>

<script>
export default {
  name: 'RoleDetail',
  props: {
    // eslint-disable-next-line vue/require-default-prop
    roleInfo: {
      type: Object,
      required: false
    }
  },
  data() {
    return {
      labelCol: { span: 5 },
      valueCol: { span: 18 }
    }
  },
  computed: {
    roleItem() {
      return this.roleInfo || {}
    }
  }
}
</script>

<style scoped>
.title {
  text-align: center;
  margin-bottom: 5px;
}
</style>
