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
          <span>{{ PDTN.record.ProDef_Name }}</span>
          <br />
          <span>{{ PDTN.record.ProDef_TypeName }}</span>
        </template>
        <template v-slot:ActInstEndDate="{ AIED }">
          <gaf-time-switch
            :end-time="AIED.record.ActInst_EndDate"
          ></gaf-time-switch>
        </template>
        <template v-slot:operation="{ ot }">
          <!-- <div id="handle-func"> -->
          <a
            href="javascript:void(0);"
            rel="external nofollow"
            class="btn-view"
            @click.stop="toMallInfo(ot.record)"
            ><a-icon type="edit" /> 办理</a
          >
          <a-divider type="vertical" />
          <!-- </div>
      <div> -->
          <a
            class="btn-del"
            href="javascript:;"
            @click.stop="() => showModal(ot.record)"
          >
            <a-icon type="delete" />删除
          </a>
          <a-modal
            id="model-style"
            v-model="visible"
            :footer="null"
            :mask="false"
            title="您确认要删除该业务吗？"
          >
            <a-textarea
              v-model="value"
              :auto-size="{ minRows: 7, maxRows: 6 }"
              style="margin-bottom: 10px"
              placeholder=""
            />
            <button class="submit-gray" @click="handleOk(ot.record)">
              确认
            </button>
            <button class="cancel-modal" @click="handleCancel">取消</button>
          </a-modal>
          <!-- </div> -->
        </template>
      </gaf-business-box>
    </div>
  </div>
</template>
<script>
import '~/assets/css/common.css'
import '../../views/css/workFlow.css'
import GafTimeSwitch from '../../views/GafWorkflowCommon/GafTimeSwitch'
import GafBusinessBox from '../../views/GafWorkflowCommon/GafBusinessBox'
export default {
  name: 'GafAcceptBox',
  components: {
    GafBusinessBox,
    GafTimeSwitch,
  },
  data() {
    return {
      dataSource: [],
      countDownTime: '',
      value: '',
      visible: false,
      id: '',
    }
  },
  created() {
    this.getTable()
  },
  methods: {
    /* 查询 接口 */
    async searchValue(keyword) {
      const url = `/workflow/query/accept-process`
      const rst = await this.$axios.post(url, { keyword })
      if (rst.data.isSuccessed) {
        this.dataSource = rst.data.data.content
      }
    },
    /* 删除 接口 */
    showModal(record) {
      this.id = record.NowActInst_ID
      this.visible = true
    },
    handleCancel() {
      this.visible = false
      this.value = ''
    },
    async handleOk() {
      this.visible = false
      const url = `/workflow/execute/process/remove`
      const delRecord = await this.$axios.post(url, {
        nowActInstId: this.id,
        proInstDesc: this.value,
      })
      if (delRecord.data.isSuccessed && this.value !== '') {
        this.$message.success('删除成功')
        this.value = ''
      } else {
        this.$message.warn(delRecord.data.message)
        this.value = ''
      }
      this.getTable()
    },
    /* 办理 功能 接口 */
    toMallInfo(record) {
      const proInstID = record.ProInst_ID
      const ActInstID = record.ActInst_ID
      const nowActInstId = record.NowActInst_ID
      this.$router.push({
        path: './GafAcceptSon',
        query: {
          proInstId: proInstID,
          actInstId: ActInstID,
          nowActInstId,
        },
      })
    },
    /* 获取后台的数据 接口 */
    async getTable() {
      const url = `/workflow/query/accept-process`
      const rst = await this.$axios.post(url, {})
      if (rst.data.isSuccessed) {
        // this.$message.success('成功')
      } else {
        this.$message.error('更新失败')
      }
      // console.log(rst.data.data.content)
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
