<template>
  <div>
    <div>
      <div class="modal-line">
        <a-row style="margin: 20px 0 15px 0">
          <a-col :span="12">
            <span class="vertical-line">| </span
            ><span class="text-bolder">{{ title }}</span>
          </a-col>
        </a-row>
      </div>
      <div style="margin-top: 15px; position: relative">
        <div style="width: 200px; position: absolute">
          <a-table
            :data-source="paramsGroupList"
            :columns="groupColumns"
            :pagination="false"
            row-key="paramGroupNameCn"
            size="small"
          >
            <span slot="customTitle" class="title">
              参数组<a @click="handleAddGrp"><a-icon type="plus" /></a>
            </span>
            <!-- 参数组 -->
            <template slot="paramGroupNameCn" slot-scope="text, record">
              <a
                :class="[
                  { active: record.codeTemplateParamGrpId === isActive },
                  'param-group',
                ]"
                href="javascript:;"
                @click="handleClick(record)"
              >
                {{ record.paramGroupNameCn }}
              </a>
            </template>
            <template slot="paramGroupOperation" slot-scope="text, record">
              <a href="javascript:;" @click.stop="() => handleUpdateGrp(record)">
                <a-icon type="edit" />
              </a>
              <a href="javascript:;">
                <a-divider type="vertical" />
                <a-popconfirm
                  title="删除后无法恢复，是否继续?"
                  ok-text="确认"
                  cancel-text="取消"
                  @confirm="() => handleDeleteGrp(record)"
                >
                  <a-icon type="delete" />
                </a-popconfirm>
              </a>
            </template>
          </a-table>
        </div>
        <div style="width: 750px; margin-left: 210px">
          <a-table
            :data-source="paramsList"
            :columns="paramColumns"
            :pagination="false"
            size="small"
            row-key="paramName"
          >
            <span slot="customTitle1" class="title">
              参数设置
              <a :disabled="currentGrpId === '' || paramRepeat ? true : false">
                <a-icon type="plus" @click="addParam" />
              </a>
            </span>
            <!-- 参数名 -->
            <template slot="paramName" slot-scope="text, record">
              <div class="paramName">
                <a-tooltip v-if="!record.isEditAble" class="shortcutTip">
                  <template slot="title">
                    {{ record.paramName }}
                  </template>
                  <span>
                    {{ record.paramName }}
                  </span>
                </a-tooltip>
                <a-input
                  v-else
                  placeholder="请输入参数名"
                  size="small"
                  allow-clear
                  :default-value="record.paramName"
                  @blur="inputEnd(record, $event)"
                >
                </a-input>
              </div>
            </template>

            <!-- 参数中文名 -->
            <template slot="paramNameCn" slot-scope="text, record">
              <div class="paramNameCn">
                <a-tooltip v-if="!record.isEditAble" class="shortcutTip">
                  <template slot="title">
                    {{ record.paramNameCn }}
                  </template>
                  <span>
                    {{ record.paramNameCn }}
                  </span>
                </a-tooltip>
                <a-input
                  v-else
                  placeholder="请输入参数中文名"
                  size="small"
                  allow-clear
                  :default-value="record.paramNameCn"
                  @change="inputChange3(record, $event)"
                  @blur="inputChange3(record, $event)"
                >
                </a-input>
              </div>
            </template>

            <!-- 参数值 -->
            <template slot="paramValue" slot-scope="text, record">
              <div class="paramValue">
                <a-tooltip v-if="!record.isEditAble" class="shortcutTip">
                  <template slot="title">
                    {{ record.defaultValue }}
                  </template>
                  <span>
                    {{ record.defaultValue }}
                  </span>
                </a-tooltip>
                <a-input
                  v-else
                  placeholder="请输入参数默认值"
                  size="small"
                  allow-clear
                  :default-value="record.defaultValue"
                  @change="inputChange2(record, $event)"
                  @blur="inputChange2(record, $event)"
                >
                </a-input>
              </div>
            </template>
            <!-- 描述 -->
            <template slot="description" slot-scope="text, record">
              <div class="description">
                <a-tooltip v-if="!record.isEditAble" placement="topLeft" class="shortcutTip">
                  <template slot="title">
                    {{ record.description }}
                  </template>
                  <span>
                    {{ record.description }}
                  </span>
                </a-tooltip>
                <a-input
                  v-else
                  placeholder="描述"
                  size="small"
                  allow-clear
                  :default-value="record.description"
                  @change="inputChange4(record, $event)"
                  @blur="inputChange4(record, $event)"
                >
                </a-input>
              </div>
            </template>
            <!-- 操作 -->
            <template slot="operation" slot-scope="text, record">
              <div
                v-if="record.projCodeTemplateParamId == undefined"
                class="hasNotSaved"
              >
                <a-popconfirm
                  v-if="
                    record &&
                    ((record.paramName && record.paramName.trim() !== '') ||
                      (record.paramNameCn && record.paramNameCn.trim() === '') ||
                      (record.paramValue && record.paramValue.trim() === '') ||
                      (record.description && record.description.trim() === ''))
                  "
                  title="确认删除未提交的参数?"
                  ok-text="确认"
                  cancel-text="取消"
                  @confirm="() => deleteNewParam(record)"
                >
                  <a href="javascript:;"><a-icon type="close" /></a>
                </a-popconfirm>
                <a v-else href="javascript:;" @click="deleteNewParam(record)">
                  <a-icon type="close" />
                </a>
              </div>
              <div v-else>
                <a href="javascript:;" @click="updateOldParam(record)">
                  <a-icon :type="record.isEditAble ? 'save' : 'edit'" />
                </a>
                <a-divider type="vertical" />
                <a-popconfirm
                  title="删除后无法恢复，是否继续?"
                  ok-text="确认"
                  cancel-text="取消"
                  @confirm="() => deleteOldParam(record)"
                >
                  <a href="javascript:;"><a-icon type="close" /></a>
                </a-popconfirm>
              </div>
            </template>
          </a-table>
        </div>
      </div>
      <div style="float: right; margin-top: 15px">
        <a-button class="submit-gray" @click="handleSubmitParams">
          提交
        </a-button>
        <a-button class="cancel-modal" @click="handleCancel">取消</a-button>
      </div>
    </div>
    <gaf-modal
      v-model="grpFormVisible"
      :width="500"
      :footer="null"
      :title="grpFormTitle"
    >
      <add-edit-form
        :title="grpFormTitle"
        :edit-data="editData"
        :template-name="templateName"
        :template-id="templateId"
        :grp-list="paramsGroupList"
        @submit="handleGrpSubmit"
        @back="handleBack"
      >
      </add-edit-form>
    </gaf-modal>
  </div>
</template>

<script>
// @ts-nocheck
import AddEditForm from './AddOrEditParamGrp'
export default {
  components: {
    AddEditForm,
  },
  props: {
    title: {
      type: String,
      default: '',
    },
    templateId: {
      type: String,
      default: '',
    },
    templateName: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      // 存在重复参数的分组
      reFlush: false,
      grpRepeatParams: [],
      isActive: '',
      toUpdateParams: [],
      deleteParamIds: [],
      updateParamIds: [],
      updateOrAddParams: [],
      paramFormTitle: '',
      // currentParamId: '',
      paramFormVisible: false,
      currentGrpId: '',
      currentGrpName: '',
      // 新增参数列表
      newParamsLst: [],
      addIndex: 0,
      // 参数编辑记录
      editParamData: {},
      // 参数组编辑记录
      editData: {},
      // 分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0,
        showQuickJumper: true,
      },
      grpFormTitle: '',
      grpFormVisible: false,
      // 输入错误提示
      inputError: '',
      // 参数组列表
      paramsGroupList: [],
      // 参数列表
      paramsList: [],
      // 列表显示字段
      groupColumns: [
        {
          dataIndex: 'paramGroupNameCn',
          key: 'paramGroupNameCn',
          scopedSlots: {
            title: 'customTitle',
            customRender: 'paramGroupNameCn',
          },
        },
        {
          dataIndex: 'paramGroupOperation',
          key: 'paramGroupOperation',
          scopedSlots: {
            customRender: 'paramGroupOperation',
          },
        },
      ],
      paramColumns: [
        {
          scopedSlots: {
            title: 'customTitle1',
          },
          children: [
            {
              title: '参数名',
              dataIndex: 'paramName',
              key: 'paramName',
              scopedSlots: { customRender: 'paramName' },
            },
            {
              title: '参数中文名',
              dataIndex: 'paramNameCn',
              key: 'paramNameCn',
              scopedSlots: { customRender: 'paramNameCn' },
            },
            {
              title: '参数默认值',
              dataIndex: 'defaultValue',
              key: 'defaultValue',
              scopedSlots: { customRender: 'paramValue' },
            },
            {
              title: '描述',
              dataIndex: 'description',
              key: 'description',
              ellipsis: true,
              scopedSlots: { customRender: 'description' },
            },
            {
              title: '操作',
              dataIndex: 'operation',
              key: 'operation',
              fixed: 'right',
              ellipsis: true,
              scopedSlots: { customRender: 'operation' },
            },
          ],
        },
      ],
      selectedRowKeys: [],
      selectedGroupIndex: -1,
      requiredRowKeys: null,
    }
  },
  computed: {
    paramRepeat() {
      let paramRepeat = false
      if (
        this.grpRepeatParams.findIndex(
          (item) => item.grpId === this.currentGrpId
        ) !== -1
      ) {
        paramRepeat = true
      }
      return paramRepeat
    },
  },
  watch: {
    templateId: {
      handler() {
        this.getParamsGroupList()
      },
    },
  },
  mounted() {
    this.getParamsGroupList()
  },
  methods: {
    f1(index) {
      this.isActive = index
    },
    CancelUpdateParam() {
      this.editParamData = {}
      this.paramFormVisible = false
    },
    handleSubmitUpdate() {
      this.editParamData = {}
      this.paramFormVisible = false
    },
    deleteNewParam(row) {
      const index = this.paramsList.findIndex(
        (item) => item.paramIndex === row.paramIndex
      )
      const newIndex = this.newParamsLst.findIndex(
        (item) => item.paramIndex === row.paramIndex
      )
      this.paramsList.splice(index, 1)
      this.newParamsLst.splice(newIndex, 1)
      const ind = this.grpRepeatParams.findIndex(
        (item) =>
          item.paramIndex === row.paramIndex && this.currentGrpId === item.grpId
      )
      if (ind !== -1) {
        this.grpRepeatParams.splice(ind, 1)
      }
    },
    // 删除参数
    deleteOldParam(row) {
      const paramindex = this.paramsList.findIndex(
        (item) => item.projCodeTemplateParamId === row.projCodeTemplateParamId
      )
      if (paramindex !== -1) {
        this.deleteParamIds.push(row.projCodeTemplateParamId)
        this.paramsList.splice(paramindex, 1)
      }
      const ind = this.grpRepeatParams.findIndex(
        (item) =>
          item.projCodeTemplateParamId === row.projCodeTemplateParamId &&
          this.currentGrpId === item.grpId
      )
      if (ind !== -1) {
        this.grpRepeatParams.splice(ind, 1)
      }
    },
    addParam() {
      this.addIndex += 1
      const newData = {
        isAddParam: true,
        isEditAble: true,
        codeTemplateId: this.templateId,
        paramGroupId: this.currentGrpId,
        paramNameCn: '',
        description: '',
        paramName: '',
        defaultValue: '',
        paramIndex: this.addIndex,
      }
      this.paramsList.push(newData)
      this.newParamsLst.push(newData)
    },
    handleAddGrp() {
      this.editData = {}
      this.grpFormVisible = true
      this.grpFormTitle = '新增分组'
    },
    handleUpdateGrp(row) {
      this.grpFormVisible = true
      this.grpFormTitle = '编辑分组'
      this.editData = row
    },
    updateOldParam(row) {
      const grpIndex = this.paramsGroupList.findIndex(
        (item) => item.codeTemplateParamGrpId === this.currentGrpId
      )
      if (grpIndex === -1) return false
      const paramsIndex = this.paramsGroupList[grpIndex].paramVos.findIndex(
        (item) => item.projCodeTemplateParamId === row.projCodeTemplateParamId
      )
      if (paramsIndex === -1) return false
      this.toUpdateParams.push(
        this.paramsGroupList[grpIndex].paramVos[paramsIndex]
      )
      this.updateParamIds.push(row.projCodeTemplateParamId)
      row.isEditAble = !row.isEditAble
      this.$forceUpdate()
    },
    async handleDeleteGrp(row) {
      const url = '/proj/paramgroup/' + row.codeTemplateParamGrpId
      const rst = await this.$axios.delete(url)
      if (rst.data.isSuccessed) {
        this.$message.success('删除成功')
        this.$nextTick(() => {
          this.getParamsGroupList()
        })
        this.paramsList = []
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`)
      }
    },
    handleBack() {
      this.editData = {}
      this.grpFormVisible = false
    },
    async getParamsGroupList() {
      this.loading = true
      const url = `/proj/paramgroup/groupandparam/${this.templateId}?pageSize=10&pageNum=1`
      const res = await this.$axios.$get(url)
      this.loading = false
      if (res.successed) {
        if (res.data !== null) {
          this.paramsGroupList = res.data
          this.reFlush = !this.reFlush
          this.handleClick(this.paramsGroupList[0])
          this.pagination.total = this.paramsGroupList.length
        }
      } else {
        this.$message.error(`查询失败,原因:${res.message}`)
      }
      this.$forceUpdate()
    },
    onSelectChange(selectedRowKeys) {
      const temp = new Set([])
      selectedRowKeys.forEach((item) => {
        if (!this.requiredRowKeys.has(item) && !temp.has(item)) {
          temp.add(item)
        }
      })
      this.selectedRowKeys = [
        ...Array.from(this.requiredRowKeys),
        ...Array.from(temp),
      ]
    },
    handleClick(record) {
      this.currentGrpId = record.codeTemplateParamGrpId
      this.currentGrpName = record.paramGroupName
      this.isActive = record.codeTemplateParamGrpId
      const index = this.paramsGroupList.findIndex(
        (item) => item.codeTemplateParamGrpId === record.codeTemplateParamGrpId
      )
      this.selectedGroupIndex = index
      this.paramsList =
        this.paramsGroupList[index].paramVos === null
          ? []
          : this.paramsGroupList[index].paramVos
      this.paramsList = this.paramsList.concat(this.getGrpNewParams())
    },
    getGrpNewParams() {
      return this.newParamsLst.filter(
        (item) => item.paramGroupId === this.currentGrpId
      )
    },
    inputEnd(record, event) {
      if (
        this.paramsList.some((item) => {
          if (
            item.paramName === event.target.value.trim() &&
            (item.paramIndex !== record.paramIndex ||
              record.projCodeTemplateParamId !== item.projCodeTemplateParamId)
          ) {
            this.$message.info('参数已存在！')
            return true
          }
        })
      ) {
        if (record.projCodeTemplateParamId === undefined) {
          const data = {
            grpId: this.currentGrpId,
            paramIndex: record.paramIndex,
          }
          if (
            this.grpRepeatParams.findIndex(
              (item) =>
                item.paramIndex === data.paramIndex && item.grpId === data.grpId
            ) === -1
          ) {
            this.grpRepeatParams.push(data)
          }
        } else {
          const data = {
            grpId: this.currentGrpId,
            projCodeTemplateParamId: record.projCodeTemplateParamId,
          }
          if (
            this.grpRepeatParams.findIndex(
              (item) =>
                item.projCodeTemplateParamId === data.projCodeTemplateParamId &&
                item.grpId === data.grpId
            ) === -1
          ) {
            this.grpRepeatParams.push(data)
          }
        }
      } else if (this.grpRepeatParams.length > 0) {
        let ind
        if (record.projCodeTemplateParamId === undefined) {
          ind = this.grpRepeatParams.findIndex(
            (item) =>
              item.paramIndex === record.paramIndex &&
              this.currentGrpId === item.grpId
          )
        } else {
          ind = this.grpRepeatParams.findIndex(
            (item) =>
              item.projCodeTemplateParamId === record.projCodeTemplateParamId &&
              this.currentGrpId === item.grpId
          )
        }

        if (ind !== -1) {
          this.grpRepeatParams.splice(ind, 1)
        }
      }

      if (record.projCodeTemplateParamId == null) {
        const paramsIndex = this.paramsList.findIndex(
          (item) => item.paramIndex === record.paramIndex
        )
        const newParamsIndex = this.newParamsLst.findIndex(
          (item) => item.paramIndex === record.paramIndex
        )
        if (paramsIndex !== -1) {
          this.$nextTick(() => {
            this.paramsList[paramsIndex].paramName = event.target.value
          })
        }
        if (newParamsIndex !== -1) {
          this.$nextTick(() => {
            this.newParamsLst[newParamsIndex].paramName = event.target.value
          })
        }
      } else {
        const paramsIndex = this.toUpdateParams.findIndex(
          (item) =>
            item.projCodeTemplateParamId === record.projCodeTemplateParamId
        )
        if (paramsIndex !== -1) {
          this.$nextTick(() => {
            this.toUpdateParams[paramsIndex].paramName = event.target.value
          })
        }
      }
    },
    inputChange(record, event) {
      if (record.projCodeTemplateParamId == null) {
        const paramsIndex = this.paramsList.findIndex(
          (item) => item.paramIndex === record.paramIndex
        )
        const newParamsIndex = this.newParamsLst.findIndex(
          (item) => item.paramIndex === record.paramIndex
        )
        if (paramsIndex !== -1) {
          this.$nextTick(() => {
            this.paramsList[paramsIndex].paramName = event.target.value
          })
        }
        if (newParamsIndex !== -1) {
          this.$nextTick(() => {
            this.newParamsLst[newParamsIndex].paramName = event.target.value
          })
        }
      } else {
        const paramsIndex = this.toUpdateParams.findIndex(
          (item) =>
            item.projCodeTemplateParamId === record.projCodeTemplateParamId
        )
        if (paramsIndex !== -1) {
          this.$nextTick(() => {
            this.toUpdateParams[paramsIndex].paramName = event.target.value
          })
        }
      }
      this.$forceUpdate()
    },
    inputChange2(record, event) {
      if (record.projCodeTemplateParamId == null) {
        const paramsIndex = this.paramsList.findIndex(
          (item) => item.paramIndex === record.paramIndex
        )
        const newParamsIndex = this.newParamsLst.findIndex(
          (item) => item.paramIndex === record.paramIndex
        )
        if (paramsIndex !== -1) {
          this.$nextTick(() => {
            this.paramsList[paramsIndex].defaultValue = event.target.value
          })
        }
        if (newParamsIndex !== -1) {
          this.$nextTick(() => {
            this.newParamsLst[newParamsIndex].defaultValue = event.target.value
          })
        }
      } else {
        const paramsIndex = this.toUpdateParams.findIndex(
          (item) =>
            item.projCodeTemplateParamId === record.projCodeTemplateParamId
        )
        if (paramsIndex !== -1) {
          this.$nextTick(() => {
            this.toUpdateParams[paramsIndex].defaultValue = event.target.value
          })
        }
      }
      this.$forceUpdate()
    },
    inputChange3(record, event) {
      if (record.projCodeTemplateParamId == null) {
        const paramsIndex = this.paramsList.findIndex(
          (item) => item.paramIndex === record.paramIndex
        )
        const newParamsIndex = this.newParamsLst.findIndex(
          (item) => item.paramIndex === record.paramIndex
        )
        if (paramsIndex !== -1) {
          this.$nextTick(() => {
            this.paramsList[paramsIndex].paramNameCn = event.target.value
          })
        }
        if (newParamsIndex !== -1) {
          this.$nextTick(() => {
            this.newParamsLst[newParamsIndex].paramNameCn = event.target.value
          })
        }
      } else {
        const paramsIndex = this.toUpdateParams.findIndex(
          (item) =>
            item.projCodeTemplateParamId === record.projCodeTemplateParamId
        )
        if (paramsIndex !== -1) {
          this.$nextTick(() => {
            this.toUpdateParams[paramsIndex].paramNameCn = event.target.value
          })
        }
      }
      this.$forceUpdate()
    },
    inputChange4(record, event) {
      if (record.projCodeTemplateParamId == null) {
        const paramsIndex = this.paramsList.findIndex(
          (item) => item.paramIndex === record.paramIndex
        )
        const newParamsIndex = this.newParamsLst.findIndex(
          (item) => item.paramIndex === record.paramIndex
        )
        if (paramsIndex !== -1) {
          this.$nextTick(() => {
            this.paramsList[paramsIndex].description = event.target.value
          })
        }
        if (newParamsIndex !== -1) {
          this.$nextTick(() => {
            this.newParamsLst[newParamsIndex].description = event.target.value
          })
        }
      } else {
        const paramsIndex = this.toUpdateParams.findIndex(
          (item) =>
            item.projCodeTemplateParamId === record.projCodeTemplateParamId
        )
        if (paramsIndex !== -1) {
          this.$nextTick(() => {
            this.toUpdateParams[paramsIndex].description = event.target.value
          })
        }
      }
      this.$forceUpdate()
    },
    async handleSubmitParams() {
      if (this.grpRepeatParams.length > 0) {
        this.$message.info('请检查是否存在同名参数的参数组')
        return false
      }
      this.updateOrAddParams = this.newParamsLst.concat(this.toUpdateParams)
      if (
        this.updateOrAddParams.some((item) => {
          if (item.paramName === '' || item.paramNameCn === '') {
            this.$message.info('请检查参数名或参数中文名是否为空')
            return false
          }
        })
      ) {
        return false
      }

      if (this.updateOrAddParams.length > 0 || this.deleteParamIds.length > 0) {
        const url = '/proj/projcodetemplateparam/batchHandlerParams'
        const postData = {
          deleteIdsLst: this.deleteParamIds,
          templateParamsLst: this.updateOrAddParams,
        }
        const rst = await this.$axios.post(url, postData)

        if (rst.data.successed) {
          this.deleteParamIds = []
          this.newParamsLst = []
          this.toUpdateParams = []
          const url2 = `/proj/paramgroup/groupandparam/${this.templateId}?pageSize=10&pageNum=1`
          const res2 = await this.$axios.$get(url2)
          if (res2.successed) {
            this.paramsGroupList = res2.data
            const index = this.paramsGroupList.findIndex(
              (item) => item.codeTemplateParamGrpId === this.currentGrpId
            )
            this.paramsList =
              this.paramsGroupList[index].paramVos === null
                ? []
                : this.paramsGroupList[index].paramVos
            this.$message.success('操作成功')
            this.$emit('saveConfig')
          } else {
            this.$message.error(`查询失败,原因:${res2.message}`)
          }
        } else {
          this.$message.error(`添加失败,原因:${rst.data.message}`)
        }
      } else {
        this.$message.success('操作成功')
        this.$emit('saveConfig')
      }
    },
    handleGrpSubmit() {
      if (this.inputError !== '') {
        this.$message.error(this.inputError, 1)
        return false
      }
      this.grpFormVisible = false
      this.getParamsGroupList()
    },
    handleCancel() {
      this.currentGrpId = ''
      this.editData = {}
      this.editParamData = []
      this.$emit('cancel')
    },
  },
}
</script>

<style scoped>
/* a {
  text-decoration: none;
}

.footer {
  text-align: center;
  margin: 0;
  padding: 0;
  width: 100%;
  display: flex;
  justify-content: center;
}

.footer button {
  width: 80px;
  vertical-align: middle;
  font-size: 12px;
  margin: 15px 5px;
}

.container {
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
}

.monitor-title {
  height: 50px;
  line-height: 50px;
  font-size: 16px;
}

.monitor-title-back {
  text-align: right;
  font-size: 14px;
  padding-right: 30px;
}

::-webkit-scrollbar {
  width: 0px;
}

.title {
  position: relative;
}

.title a {
  display: inline-block;
  color: #262626;
}

.title a i {
  font-size: 10px;
  border: 1px solid #ccc;
  width: 18px;
  height: 16px;
  margin: 0 5px;
  padding: 2px 0;
  border-radius: 6px;
  position: absolute;
  top: 2px;
  box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.2);
}

.title a:hover i {
  color: #559df5;
}

.group-table {
  width: 200px;
}

.param-group {
  color: #262626;
}

.param-group:hover {
  color: #559df5;
}

.active {
  color: #559df5;
  border-bottom: 3px solid #559df5;
  border-radius: 1px;
}

.paramsDiv {
  height: fit-content;
}

.hasNotSaved {
  display: flex;
  justify-content: flex-end;
  margin-right: 6px;
  align-items: flex-start;
}
*/
.paramValue {
  overflow: hidden;
  text-overflow: ellipsis;
  width: 150px;
}

.paramNameCn {
  overflow: hidden;
  text-overflow: ellipsis;
  width: 120px;
}

.paramName {
  overflow: hidden;
  text-overflow: ellipsis;
  width: 120px;
}
.description {
  overflow: hidden;
  text-overflow: ellipsis;
  width: 180px;
}

.shortcutTip {
  width: 370px;
} 
</style>
