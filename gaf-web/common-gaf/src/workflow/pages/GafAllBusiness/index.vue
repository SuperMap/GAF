/* eslint-disable eqeqeq */
<template>
  <div class="app-container">
    <div class="page-single">
      <div class="bread-txt">
        <a-breadcrumb separator=">">
          <a-breadcrumb-item>
            <!-- <b class="nowTxt">当前：</b> -->
            <span class="vertical-line">| </span>
            当前：
            <button class="btn-fun blue" @click="changeEvent({ Id: '-1' })">
              全部
            </button>
          </a-breadcrumb-item>
          <a-breadcrumb-item v-for="(item, index) in dataNav" :key="index">
            <button class="btn-fun blue" @click="changeEvent(item)">
              {{ item.Name }}
            </button>
          </a-breadcrumb-item>
        </a-breadcrumb>
      </div>
      <gaf-btn-skip
        v-for="item in allBussinessData"
        :key="item.Id"
        :data="item"
        :is-leaf="!parentIds.has(item.Id)"
        @click="onClick"
      ></gaf-btn-skip>
      <a-drawer
        :closable="false"
        :visible="visible"
        :after-visible-change="afterVisibleChange"
        placement="right"
        width="400px"
        class="drawerBox"
        @close="onClose"
      >
        <a-form-model
          :model="form"
          :label-col="labelCol"
          :wrapper-col="wrapperCol"
        >
          <a-form-model-item label="项目名称：">
            <a-input v-model="form.Name" />
          </a-form-model-item>
          <a-form-model-item label="受理流程：">
            <a-input v-model="form.Name1" disabled />
          </a-form-model-item>
          <a-form-model-item label="业务类别：">
            <a-input v-model="form.OperationType" disabled />
          </a-form-model-item>
          <a-form-model-item label="紧急程度：">
            <a-radio-group v-model="form.value">
              <a-radio :value="1010"> 正常件 </a-radio>
              <a-radio :value="2010" style="color: red"> 加急件 </a-radio>
              <a-radio :value="4010" style="color: green"> 绿色件 </a-radio>
            </a-radio-group>
          </a-form-model-item>
          <a-form-model-item label="描述：">
            <a-input v-model="form.desc" class="model-txt" type="textarea" />
          </a-form-model-item>
          <a-form-model-item>
            <div style="position: relative; left: 40px; line-height: 32px">
              <button
                class="submit-gray"
                style="margin: 0 10px 0 47px"
                @click="onSubmit"
              >
                受理
              </button>
              <button class="cancel-modal" @click="cancle">关闭</button>
            </div>
          </a-form-model-item>
        </a-form-model>
      </a-drawer>
    </div>
  </div>
</template>
<script>
//test
import '../../views/css/workFlow.css'
import GafBtnSkip from '../../views/GafAllBusiness/GafBtnSkip'
export default {
  name: 'GafAllBusiness',
  components: {
    GafBtnSkip,
  },
  data() {
    return {
      dataNav: [],
      dataSource: [],
      allBussinessData: [],
      parentIds: new Set(),
      visible: false,
      labelCol: { span: 6 },
      wrapperCol: { span: 18 },
      form: {
        Name: '',
        Name1: '',
        OperationType: '',
        delivery: false,
        value: '',
        desc: '',
        Id: '',
        ParentId: '',
        Tag: '',
        operationtype: '',
        url: '',
        ProVersion_Addres: '',
      },
    }
  },
  created() {
    this.getTable()
  },
  methods: {
    // 抽屉 提交
    async onSubmit() {
      const dataSubmit = {
        Name: this.form.Name1,
        OperationType: this.form.OperationType,
        ProDef_ID: this.form.Id,
        ProInst_Desc: this.form.desc,
        ProInst_Name: this.form.Name,
        ProInst_Type: this.form.value,
        Tag: this.form.Tag,
        ProInst_Origin: 0,
        ProVersion_Addres: this.form.ProVersion_Addres,
        url: this.form.url,
      }
      const url = `/workflow/execute/process/start`
      if (this.form.value === null) {
        this.$message.warn('紧急程度不能为空')
      } else {
        const dataS = await this.$axios.post(url, dataSubmit)
        if (dataS.data.isSuccessed) {
          this.$message.success('成功')
        } else {
          this.$message.error(dataS.data.message)
        }
        this.visible = false
        this.$router.push({
          path: '../GafAcceptBox',
        })
      }
    },
    // 抽屉 显示
    onClose() {
      this.visible = false
    },
    cancle() {
      this.visible = false
    },
    onClick(data, isLeaf) {
      // console.log('数据', data, isLeaf)
      if (!isLeaf) {
        this.allBussinessData = this.getChildren(this.dataSource, data)
        this.dataNav.push(data)
      } else {
        this.visible = true
        this.form.Name = data.Name
        this.form.Name1 = data.Name
        this.form.OperationType = data.OperationType
        this.form.value = data.operationtype
        this.form.Id = data.Id
        this.form.Tag = data.Tag
        this.form.ProVersion_Addres = data.url
        this.form.url = data.url
      }
    },
    /* 面包屑 */
    changeEvent(item) {
      // eslint-disable-next-line eqeqeq
      if (item.Id == -1) {
        this.dataNav.splice(0, this.dataNav.length)
      } else {
        let i = 0
        for (; i < this.dataNav.length; i++) {
          // eslint-disable-next-line eqeqeq
          if (this.dataNav[i].Id == item.Id) {
            break
          }
        }
        this.dataNav.splice(i + 1, this.dataNav.length - i + 1)
      }
      this.allBussinessData = this.getChildren(this.dataSource, item)
    },

    getChildren(allData, data) {
      // eslint-disable-next-line eqeqeq
      return allData.filter((item) => item.ParentId == data.Id)
    },
    async getTable() {
      const url = `/workflow/query/regular-business`
      const rst = await this.$axios.get(url, {})
      if (rst.data.isSuccessed) {
        // this.$message.success('成功')
      } else {
        this.$message.error('更新失败')
      }
      this.dataSource = rst.data.data
      const arrDataAll = []
      arrDataAll.push(...this.dataSource)
      const allBussinessData = []
      for (let i = 0; i < arrDataAll.length; i++) {
        // eslint-disable-next-line eqeqeq
        if (arrDataAll[i].ParentId == -1) {
          allBussinessData.push(arrDataAll[i])
        }
        this.parentIds.add(arrDataAll[i].ParentId)
      }
      this.allBussinessData = allBussinessData
    },
  },
}
</script>
<style scoped>
button {
  border-radius: 4px;
}
</style>
