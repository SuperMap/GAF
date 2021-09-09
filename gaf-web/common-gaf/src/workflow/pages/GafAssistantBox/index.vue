<template>
  <div class="app-container">
    <div class="page-single">
      <gaf-business-box :data-source="dataSource" @searchValue="searchValue">
        <template v-slot:ProInstCode="{ PIC }">
          <span>{{ PIC.record.ProInst_Code }}</span>
          <br />
          <span class="ab-pic" v-html="PIC.record.ProActTypeIcon"></span>
        </template>
        <template v-slot:ProInstName="{ PIN }">
          <span class="ab-pin">{{ PIN.record.ProInst_Name }}</span>
          <br />
          <span>收件时间：{{ PIN.record.ProInst_StartDate }}</span>
        </template>
        <template v-slot:ActInstName="{ AIN }">
          <a-tag color="green">{{ AIN.record.ActInst_Name }}</a-tag>
        </template>
        <template v-slot:ProDefTypeName="{ PDTN }">
          <span>{{ PDTN.record.ProInst_Name }}</span>
          <br />
          <span>{{ PDTN.record.ProDef_TypeName }}</span>
        </template>
        <template v-slot:ActInstEndDate="{ AIED }">
          <gaf-time-switch
            :end-time="AIED.record.ActInst_EndDate"
          ></gaf-time-switch>
        </template>
        <template v-slot:operation="{ ot }">
          <a
            href="javascript:void(0);"
            rel="external nofollow"
            class="btn-edit"
            @click.stop="toMallInfo(ot.record)"
            ><a-icon type="edit" /> 办理</a
          >
        </template>
      </gaf-business-box>
    </div>
  </div>
</template>
<script>
//test
import GafTimeSwitch from '../../views/GafWorkflowCommon/GafTimeSwitch'
import GafBusinessBox from '../../views/GafWorkflowCommon/GafBusinessBox'
export default {
  name: 'GafAssistantBox',
  components: {
    GafBusinessBox,
    GafTimeSwitch,
  },
  data() {
    return {
      dataSource: [],
    }
  },
  methods: {
    /* 查询 接口 */
    async searchValue(keyword) {
      const url = `/workflow/query/assistance-process`
      const rst = await this.$axios.post(url, { keyword })
      if (rst.data.isSuccessed) {
        this.dataSource = rst.data.data.content
      }
    },
    toMallInfo(record) {
      const proInstID = record.ProInst_ID
      const ActInstID = record.ActInst_ID
      const nowActInstId = record.NowActInst_ID
      this.$router.push({
        path: './GafAssistanceSon',
        query: {
          proInstId: proInstID,
          actInstId: ActInstID,
          nowActInstId,
        },
      })
    },
    /* 获取后台的数据 接口 */
    async getTable() {
      const url = `/workflow/query/assistance-process`
      const rst = await this.$axios.post(url, {})
      if (rst.data.isSuccessed) {
        // this.$message.success('成功')
      } else {
        this.$message.error('更新失败')
      }
      this.dataSource = rst.data.data.content
    },
  },
}
</script>
<style scoped>
.ab-pic {
  background: rgb(66, 105, 233);
  color: white;
  padding: 0 3%;
  border-radius: 5px;
}
.ab-pin {
  font-weight: bolder;
}
</style>
