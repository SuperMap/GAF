<template>
  <div class="page-container">
    <div class="grid-container">
    <div>
      <a-form
        :form="addOrEditForm"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 19 }"
        layout="horizontal"
      >
        <a-form-item label="当前表">
          <a-tree-select
            v-decorator="[
              'sourceFieldTableId',
              {
                rules: [
                  {
                    required: true,
                    message: '当前表不能为空'
                  }
                ],
              }
            ]"
            :disabled="true"
            :tree-data="currentTableOpetions"
            :replaceFields="{children:'children', title:'tableName', key:'tableId', value: 'tableId' }"
            placeholder="请选择当前表"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="目标表">
          <a-tree-select
            v-decorator="[
              'targetFieldTableId',
              {
                rules: [
                  {
                    required: true,
                    message: '目标表不能为空'
                  }
                ],
              }
            ]"
            :tree-data="targetTableOpetions"
            :replaceFields="{children:'children', title:'tableName', key:'tableId', value: 'tableId' }"
            placeholder="请选择目标表"
            @change="treeSelectChange"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="新增字段关系">
          <a-button type="dashed" style="width: 60%" @click="add">
            <a-icon type="plus" /> Add field relationship
          </a-button>
        </a-form-item>
        <gaf-table-no-page
          :scroll="{ y: 250}"
          :showXH="false"
          :data-source="dataList"
          :loading="loading"
          :columns="columns"
          :row-key="(r, i) => r.Id"
          class="table-style"
          size="middle"
          :bordered="false"
        >
          <template slot="currentTable" slot-scope="text, record, index">
            <a-form-item>
              <a-tree-select
                v-decorator="[
                  `relationship[${index}].sourceFieldId`,
                  {
                    rules: [
                      {
                        required: true,
                        message: '栅格分块不能为空'
                      }
                    ],
                  }
                ]"
                :tree-data="currentTableFieldOpetions"
                :replaceFields="{children:'children', title:'fieldName', key:'fieldId', value: 'fieldId' }"
                :placeholder="text"
                allow-clear
              />
              </a-form-item>
          </template>
          <template slot="relationship">
            on
          </template>
          <template slot="targetTable" slot-scope="text, record, index">
            <a-form-item>
              <a-tree-select
                v-decorator="[
                  `relationship[${index}].targetFieldId`,
                  {
                    rules: [
                      {
                        required: true,
                        message: '栅格分块不能为空'
                      }
                    ],
                  }
                ]"
                :tree-data="targetTableFieldOpetions"
                :replaceFields="{children:'children', title:'fieldName', key:'fieldId', value: 'fieldId' }"
                :placeholder="text"
                allow-clear
              />
              </a-form-item>
          </template>
          <template slot="operation" slot-scope="text, record, index">
            <a-icon
              class="dynamic-delete-button"
              type="minus-circle-o"
              @click="() => remove(index)"
            />
          </template>
        </gaf-table-no-page>
        <div v-if="operation === 1">
          <a-form-item label="创建时间">
            <a-date-picker
              v-decorator="['createdTime']"
              show-time
              placeholder="请选择创建时间"
              disabled
            />
          </a-form-item>
          <a-form-item label="创建人">
            <a-input
              v-decorator="[
                'createdBy'            ]"
              placeholder="请输入创建人"
              allow-clear
              disabled
            />
          </a-form-item>
          <a-form-item label="修改时间">
            <a-date-picker
              v-decorator="['updatedTime']"
              show-time
              placeholder="请选择修改时间"
              disabled
            />
          </a-form-item>
          <a-form-item label="修改人">
            <a-input
              v-decorator="[
                'updatedBy'            ]"
              placeholder="请输入修改人"
              allow-clear
              disabled
            />
          </a-form-item>
        </div>
      </a-form>
    </div>
    </div>
  </div>
</template>

<script>

export default {
  props: {
    // 当parentId为'0'或为空时表示一级分类目录
    editData: {
      type: Object,
      default: null
    },
    // add表示新增 edit表示表示编辑 detail表示详情
    tableId: {
      type: String,
      default: '5f5c0fcc-b970-4514-bb51-ca5b0bc38595'
    },
    modelId: {
      type: String,
      dafault: 'c2c08f76-cd99-46c7-b4ad-58e98eb1bc99'
    }
  },
  data() {
    return {
      targetTableOpetions: [],
      currentTableOpetions: [],
      targetTableFieldOpetions: [],
      currentTableFieldOpetions: [],
      dataId: null,
      loading: false,
      dataList: [],
      relationshipData: [],
      operation: 4
    }
  },
  computed: {
    columns: function () {
      const columns = [
        {
          title: "当前表",
          dataIndex: "currentTable",
          scopedSlots: { customRender: "currentTable" },
          key: "current_table",
          align: "center",
        },
        {
          title: "",
          dataIndex: "relationship",
          scopedSlots: { customRender: "relationship" },
          key: "relationship",
          align: "center",
          width: 40
        },
        {
          title: "目标表",
          dataIndex: "targetTable",
          scopedSlots: { customRender: "targetTable" },
          key: "target_table",
          align: "center",
        },
        {
          title: "",
          align: "center",
          width: 45,
          scopedSlots: { customRender: "operation" },
        },
      ];
      return columns
    },
  },
  watch: {
  },
  created() {
    this.getCurrentTableField(this.tableId)
    this.getTargetTableOpetions(this.modelId)
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    this.addOrEditForm.setFieldsValue({sourceFieldTableId: this.tableId})
  },
  methods: {
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }        
        let data = this.addOrEditForm.getFieldsValue()
        let url = `/data-mgt/model-manage/field-associates/refresh-by-tables?sourceFieldTableId=${data.sourceFieldTableId}&targetFieldTableId=${data.targetFieldTableId}`
        data['modelId'] = this.modelId
        if (data.relationship) {
          data.relationship.forEach(item => {
            item['modelId'] = this.modelId
            item['sourceFieldTableId'] = data.sourceFieldTableId
            item['targetFieldTableId'] = data.targetFieldTableId
          })
        } else {
          data.relationship = []
        }
        this.loading = true
        if (this.dataId) {
          url = url + this.dataId
          const rst = await this.$axios.put(url, data.relationship)
          if (rst.data.isSuccessed) {
            this.$message.success('更新成功')
            this.$emit('update-success', rst.data.data)
          } else {
            this.$message.error(`更新失败,原因:${rst.data.message}`)
          }
        } else {
          const rst = await this.$axios.put(url, data.relationship)
          if (rst.data.isSuccessed) {
            this.$message.success('添加成功')
            this.$emit('add-success', rst.data.data)
          } else {
            this.$message.error(`添加失败,原因:${rst.data.message}`)
          }
        }
        this.loading = false
        this.$emit('handleCancel')
      })
    },
    resetFormFields() {
      this.addOrEditForm.resetFields()
    },
    handleBack() {
      this.addOrEditForm.resetFields()
      this.$emit('back')
    },
    async getTargetTableOpetions(value) {
      const url = `/data-mgt/model-manage/logic-tables?modelId=${value}`
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.currentTableOpetions = res.data.pageList
        this.targetTableOpetions = this.currentTableOpetions.filter(item => item.tableId !== this.tableId)
      } else {
        this.$message.error('加载API分组树失败,原因：' + res.message)
      }
    },
    async treeSelectChange(value) {
      const url = `/data-mgt/model-manage/fields?tableId=${value}`
      const res = await this.$axios.$get(url)
      this.loading = true
      if(res.isSuccessed) {
        this.targetTableFieldOpetions = res.data.pageList
        await this.getRe(value)
      } else {
        this.$message.error(`查询失败,原因: ${res.message}`)
      }
      this.loading = false
    },
    async getRe(value) {
      this.dataList = []
      const url = `/data-mgt/model-manage/field-associates?sourceFieldTableId=${this.tableId}&targetFieldTableId=${value}&pageSize=10000`
      const res = await this.$axios.$get(url)
      if(res.isSuccessed) {
        res.data.pageList.forEach((item) => {
          this.dataList.push({sourceFieldId: item.sourceFieldId, targetFieldId: item.targetFieldId, fieldAssociateId: item.fieldAssociateId})
        })
        // this.$nextTick(() => {
        //   this.addOrEditForm.setFieldsValue({relationship: this.dataList})
        // })
        setTimeout(() => {
          this.addOrEditForm.setFieldsValue({relationship: this.dataList})
        }, 10)       
      } else {
        this.$message.error(`查询失败,原因: ${res.message}`)
      }
    },
    async getCurrentTableField(value) {
      const url = `/data-mgt/model-manage/fields?tableId=${value}`
      const res = await this.$axios.$get(url)
      if(res.isSuccessed) {
        this.currentTableFieldOpetions = res.data.pageList
      } else {
        this.$message.error(`查询失败,原因: ${res.message}`)
      }
    },
    add() {
      this.dataList.push({index: this.dataList.length})
    },
    remove(index) {
      this.dataList.splice(index, 1)
      setTimeout(() => {
        this.addOrEditForm.setFieldsValue({relationship: this.dataList})
      }, 10)
    }
  }
}
</script>

<style  lang="less" scoped>
.form-foot {
  position: relative;
  left: 30%;
}
.sysbtn-style {
  text-align: center;
  margin: 10px auto;
  width: 100px;
  font-size: 12px;
  cursor: pointer;
}
.dynamic-delete-button {
  cursor: pointer;
  position: relative;
  top: 4px;
  font-size: 24px;
  color: #999;
  transition: all 0.3s;
}
.dynamic-delete-button:hover {
  color: #777;
}
.dynamic-delete-button[disabled] {
  cursor: not-allowed;
  opacity: 0.5;
}
/deep/ .ant-table-tbody .ant-row.ant-form-item .ant-form-item-control-wrapper {
  width: 100%;
}
/deep/ .ant-table-tbody .ant-row.ant-form-item {
  margin-bottom: 0
}
</style>
