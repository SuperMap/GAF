<template>
  <div>
    <template>
      <a-breadcrumb separator=">" class="modal-line">
        <a-breadcrumb-item class="tree-catalog" style="line-height: 15px"
          ><span class="vertical-line">| </span>{{ title }}</a-breadcrumb-item
        >
      </a-breadcrumb>
    </template>
    <div class="page-modal-box">
      <a-form
        :form="addOrEditForm"
        layout="horizontal"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 15 }"
        hide-required-mark
      >
      <a-form-item label="选择服务地址类型" v-if="operation === 2">
        <a-select
          v-model="registryType"
          @change="handleSelectAddTypeChange"
          :options="registryTypeList"
          default-value="1"
          >
        </a-select>
      </a-form-item>
        <a-form-item label="服务名称" v-if="registryType === 'single'">
          <a-input
            v-decorator="[
              'name',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入名称',
                  },
                  {
                    pattern: /^.{0,255}$/,
                    message: '长度不能超过255',
                  },
                ],
              },
            ]"
            placeholder="请输入服务名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="服务地址">
          <a-input
            v-decorator="[
              'address',
              {
                rules: [
                  {
                    required: true,
                    message: '服务地址不能为空',
                  },
                  {
                    pattern: /^(https?|ftp|file):\/\/[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]/,
                    message: '服务地址输入不正确',
                  },
                ],
              },
            ]"
            :placeholder="addressPlaceholder"
            allow-clear
          />
        </a-form-item>

        <a-form-item label="服务类型" v-if="registryType !== 'server'">
          <a-select
            v-decorator="[
              'typeCode',
              {
                rules: [
                  {
                    required: true,
                    message: '服务类型不能为空',
                  }
                ],
              },
            ]"
            allow-clear
            placeholder="请选择服务类型"
            @change="handleSelectChange"
            :disabled="operation === 3"
            :options="optionList"
          >
            <!-- <a-select-option
              v-for="i in optionList"
              :key="i.key"
              :value="i.key"
              :disabled="i.key === '-1'"
            >
              {{ i.title }}
            </a-select-option> -->
          </a-select>
        </a-form-item>
        <a-form-item label="时态" v-if="registryType !== 'server'">
          <a-date-picker
            v-decorator="['timeAttribute']"
            placeholder="请选择时态"
          />
        </a-form-item>
        <a-form-item v-if="tianditu || editData.typeCode === 'MAPWORLD'" label="天地图服务类型">
          <a-input
            v-decorator="['tiandituServiceType']"
            placeholder="请输入天地图服务类型"
          />
        </a-form-item>
        <a-form-item label="行政区划" v-if="registryType !== 'server'">
          <a-cascader
            v-decorator="['regionCode']"
            :options="options"
            :load-data="loadData"
            placeholder="请选择地区"
            change-on-select
          />
        </a-form-item>
        <a-form-item v-if="serviceType !== 1 && registryType !== 'server'" label="显示属性" >
          <a-row v-if="serviceType === 2" :gutter="16">
            <a-col :span="8">
              <a-input
                v-model="displayAttrsList.minimumTerrainLevel"
                placeholder="最小显示级别"
                style="width: 100%"
              ></a-input>
            </a-col>
            <a-col :span="8">
              <a-input
                v-model="displayAttrsList.maximumTerrainLevel"
                placeholder="最大显示级别"
                style="width: 100%"
              ></a-input>
            </a-col>
          </a-row>
          <a-row v-if="serviceType === 3" :gutter="8">
            <a-row
              v-for="(item, index) in displayAttrsList"
              :key="index"
              :gutter="8"
            >
              <a-col :span="8">
                <a-input
                  v-model="displayAttrsList[index].name"
                  placeholder="名称"
                  style="width: 100%"
                ></a-input>
              </a-col>
              <a-col :span="8">
                <a-input
                  v-model="displayAttrsList[index].visibleDistanceMin"
                  placeholder="最小相机高度"
                  style="width: 100%"
                ></a-input>
              </a-col>
              <a-col :span="8">
                <a-row :gutter="8">
                  <a-col :span="20">
                    <a-input
                      v-model="displayAttrsList[index].visibleDistanceMax"
                      placeholder="最大相机高度"
                      style="width: 100%"
                    ></a-input>
                  </a-col>
                  <a-col :span="4">
                    <a-button @click="removeRow(index)">
                      <a-icon type="minus-circle" />
                    </a-button>
                  </a-col>
                </a-row>
              </a-col>
            </a-row>
            <a-row>
              <a-button type="dashed" style="width: 60%" @click="add">
                <a-icon type="plus" /> 添加
              </a-button>
            </a-row>
          </a-row>
        </a-form-item>
        <a-form-item label="扩展属性" v-if="registryType !== 'server'">
          <a-textarea
            v-decorator="['moreProperties',
            {
                rules: [
                  {
                    pattern: /^.{0,500}$/,
                    message: '长度不能超过500',
                  },
                  {
                    message: '请输入符合标准格式的json格式的字符串',
                    validator: extValidater
                  },
                ],
              },
            ]"
            placeholder="请输入json格式字符串"
            :auto-size="{ minRows: 3, maxRows: 5 }"
          />
        </a-form-item>
        <a-form-item label="描述" v-if="registryType !== 'server'">
          <a-textarea
            v-decorator="[
              'description',
              {
                rules: [
                  {
                    pattern: /^.{0,500}$/,
                    message: '长度不能超过500',
                  },
                ],
              },
            ]"
            placeholder="请输入描述"
            auto-size
          />
        </a-form-item>
        <div class="btn-div">
          <a-button
            class="submit-gray"
            style="margin-left: 53%"
            @click="submitForm"
            v-preventReClick
          >
            确定
          </a-button>
          <button class="cancel-modal" @click="backToList">取消</button>
        </div>
      </a-form>
    </div>
    <a-modal
      :title="titleModal"
      v-model="visible"
      :footer="null"
    >
      <div style="background: #ECECEC">
        <a-row>
          <a-col :span="8">
            <a-card :bordered="false">
              <a-statistic title="注册成功" :value="registrationResults ? registrationResults.data.succeeded : ''" :value-style="{ color: '#3f8600',fontSize: '30px' }" />
            </a-card>
          </a-col >
          <a-col :span="8">
            <a-card :bordered="false">
              <a-statistic title="已存在"  :value="registrationResults ? registrationResults.data.existed : ''" :value-style="{ color: '#c46420',fontSize: '30px' }" />
            </a-card>
          </a-col>
          <a-col :span="8">
            <a-card :bordered="false">
              <a-statistic title="失败"  :value="registrationResults ? registrationResults.data.failed : ''" :value-style="{ color: '#cf1322',fontSize: '30px' }" />
            </a-card>
          </a-col>
        </a-row>
      </div>
      <!-- <p>{{ registrationResults ? `已经成功注册${registrationResults.data.succeeded}个服务！已存在${registrationResults.data.existed}个,失败${registrationResults.data.failed}个` : '' }}</p> -->
      <button
            class="submit-gray"
            style="margin-left: 40%"
            @click="handleCancel"
          >
            确定
          </button>
    </a-modal>
  </div>
</template>

<script>
    import moment from 'moment'

    export default {
  props: {
    title: {
      type: String,
      default: '',
    },
    editData: {
      type: Object,
      default: null,
    },
    operation: {
      type: Number,
      default: 0,
    },
    dataOfTree: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      btnloading: false,
      titleModal: '',
      visible: false,
      registrationResults: '',
      dataId: '',
      optionList: this.dataOfTree.slice(1),
      registryTypeList: [{value:'single',title:'单个服务'},{value:'server',title:'iServer服务地址'}],
      displayAttrsList: [],
      // 三维：3，二维：2，其他：1
      serviceType: 1,
      tianditu: false,
      options: [],
      registryType: 'single',
      addressPlaceholder: '例:http://nsip.net.cn/iserver/services/3D-CBD/rest/realspace/datas/Ground@CBD'
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
    this.getOptions()
  },
  mounted() {
    const copyData = { ...this.editData }
    this.dataId = copyData.gisServiceId
    delete copyData.gisServiceId
    delete copyData.nameEn
    delete copyData.status
    delete copyData.createdTime
    delete copyData.updatedTime
    delete copyData.createdBy
    delete copyData.updatedBy
    delete copyData.resourceApiId
    if (copyData.displayAttrs) {
      copyData.displayAttrs = JSON.parse(copyData.displayAttrs)
      if (copyData.typeCode === 'RESTREALSPACE') {
        this.serviceType = 3
      } else {
        this.serviceType = 2
      }
      this.displayAttrsList = copyData.displayAttrs
    }
    if (copyData.regionCode) {
      copyData.regionCode = copyData.regionCode.split(',')
      this.getvalue(copyData.regionCode[0])
    }
    if (copyData.timeAttribute)
      copyData.timeAttribute = moment(new Date(copyData.timeAttribute))
    this.addOrEditForm.setFieldsValue({ ...copyData })
  },
  methods: {
    moment,
    extValidater(rule, value, callback) {
      if(!value || value.trim() === '') {
        callback()
      } else {
        try{
          JSON.parse(value)
          callback()
        } catch (error) {
          callback(false)
        }
      }
    },
    submitForm() {
      const that = this
      that.addOrEditForm.validateFields(async(err) => {
        if (err) {
          event.preventDefault()
          return false
        }
      let url = `/map/webgis-services`
      const data = this.addOrEditForm.getFieldsValue()
      if (this.serviceType !== 1) {
        data.displayAttrs = JSON.stringify(this.displayAttrsList)
      }
      if (data.regionCode) {
        data.regionCode = data.regionCode.join(',')
      }
      if (this.dataId) {
        url = url +"/" + this.dataId
        const rst = await this.$axios.put(url, data)
        if (rst.data.isSuccessed) {
          this.$emit('submit')
          this.$message.success('更新成功')
        } else {
          this.$emit('submit')
          this.$message.error(`更新失败,原因:${rst.data.message}`)
        }
      } else {
        url = url + `?registryType=${this.registryType}`
        const rst = await this.$axios.post(url, data)
        let rst2 = null
        if (rst.data.isSuccessed) {
          if (this.registryType && this.registryType !== 'single') {
            let timeInterval = setInterval(async function(){
              rst2 = await that.$axios.get(`/map/webgis-services/result/${rst.data.data}`)
              if (rst2.data.isSuccessed) {
                that.registrationResults = rst2.data
                that.titleModal = '信息提示'
                that.visible = true
                if (rst2.data.data.status === 'done') {
                  clearInterval(timeInterval)
                  that.titleModal = '注册完成'
                  that.$message.success('全部注册完成')
                  that.addOrEditForm.resetFields()
                } else if (rst2.data.data.status === 'error') {
                  clearInterval(timeInterval)
                  that.visible = false
                  that.$emit('submit')
                  that.$message.error(`注册失败`)
                  that.addOrEditForm.resetFields()
                }
              } else {
                clearInterval(timeInterval)
                that.visible = false
                that.$emit('submit')
                that.$message.error(`注册失败，原因${rst2.data.message}`)
                that.addOrEditForm.resetFields()
              }
            },1000)

          } else {
            this.$emit('submit')
            this.$message.success('注册成功')
            that.addOrEditForm.resetFields()
          }
        } else {
          this.$message.error(`注册失败,原因:${rst.data.message}`)
          this.$emit('submit')
          that.addOrEditForm.resetFields()
        }
      }
      })
    },
    handleCancel() {
      this.visible = false
      this.$emit('submit')
    },
    // 从新增修改模态框返回列表
    backToList() {
      this.addOrEditForm.resetFields()
      this.$emit('back')
    },
    removeRow(index) {
      this.displayAttrsList.splice(index, 1)
    },
    add() {
      this.displayAttrsList.push({
        name: '',
        visibleDistanceMin: '',
        visibleDistanceMax: '',
      })
    },
    handleSelectAddTypeChange(value) {
      switch(value){
        case 'single':
          this.addressPlaceholder = "例:http://nsip.net.cn/iserver/services/3D-CBD/rest/realspace/datas/Ground@CBD"
          break;
        case 'server':
          this.addressPlaceholder = "例:http://support.supermap.com:8090/iserver"
          break
      }
    },
    handleSelectChange(value, option) {
      if (option){
        const extProperties = JSON.parse(option.data.props.extProperties)
        if ( option.data.key === undefined) {
          this.serviceType = 1
          this.tianditu = false
        } else if (extProperties && extProperties.is3D === true) {
          this.serviceType = 3
          this.displayAttrsList = []
          this.tianditu = false
        } else {
          this.serviceType = 2
          this.displayAttrsList = {}
          this.tianditu = false
        }
        if (option.data.key === 'MAPWORLD') {
          this.tianditu = true
        }
      }
    },
    async getOptions() {
      const url = `/sys-mgt/sys-dicts/XZQH/tree?level=1`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.options = res.data
      } else {
        this.$message.error('加载数据失败,原因：' + res.message)
      }
    },
    async loadData(selectedOptions) {
      const targetOption = selectedOptions[selectedOptions.length - 1]
      targetOption.loading = true
      const url = `/sys-mgt/sys-dicts/XZQH/tree?level=1&value=${targetOption.value}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        targetOption.loading = false
        targetOption.children = res.data
      } else {
        this.$message.error('加载数据失败,原因：' + res.message)
      }
    },
    async getvalue(targetOption) {
      const url = `/sys-mgt/sys-dicts/XZQH/tree?value=${targetOption}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.options.forEach(element => {
          if (element.value === targetOption) {
            element.children = res.data
            return
          }
        });
      } else {
        this.$message.error('加载数据失败,原因：' + res.message)
      }
    },
  },
}
</script>
