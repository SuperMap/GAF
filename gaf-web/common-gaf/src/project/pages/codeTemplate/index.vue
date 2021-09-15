<template>
  <div class="app-container">
    <div class="page-single">
      <gaf-table-layout v-show="!open">
        <template #actions>
          <a-button
            class="btn-fun blue"
            icon="plus"
            visible="true"
            @click="handleAdd"
          >
            新增
          </a-button>
          <div class="search-position">
            <a-input-search
              placeholder="请输入模板名称查询"
              size="large"
              @search="onSearch"
            >
              <a-button slot="enterButton" class="btn-search"> 搜索 </a-button>
            </a-input-search>
          </div>
        </template>
        <template #default>
          <gaf-table-with-page
            :pagination="pagination"
            :data-source="projCodeTemplateList"
            :loading="loading"
            :columns="
              columns.filter((item) => item.dataIndex !== 'projCodeTemplateId')
            "
            :row-key="(r, i) => i.toString()"
            style="width: 100%"
            bordered
            size="small"
            align="center"
            @change="tableChange"
          >
            <template
              v-if="hasPKField"
              slot="operation"
              slot-scope="text, record"
            >
              <a
                class="btn-configure"
                href="javascript:;"
                @click.stop="() => handleQueryParamGroup(record)"
              >
                <a-icon type="tool" /> 配置模板参数
              </a>
              <a-divider type="vertical" />
              <a
                class="btn-edit"
                href="javascript:;"
                @click.stop="() => handleUpdate(record)"
              >
                <a-icon type="edit" /> 编辑
              </a>
              <!--<a-divider type="vertical" />-->
              <!--<a>-->
              <!--<a-popconfirm-->
              <!--@confirm="handleDelete(record)"-->
              <!--:title="'确定删除吗?删除后无法恢复'"-->
              <!--ok-text="确定"-->
              <!--cancel-text="取消"-->
              <!--&gt;-->
              <!--<a-icon type="delete" />-->
              <!--删除-->
              <!--</a-popconfirm>-->
              <!--</a>-->
            </template>
            <template v-if="timeFormat" slot="timeRender" slot-scope="text">
              {{ timeFormat(text) }}
            </template>
          </gaf-table-with-page>
        </template>
      </gaf-table-layout>
      <add-edit-form
        v-if="open"
        :title="title"
        :edit-data="editData"
        @submit="handleSubmit"
        @back="handleBack"
      >
      </add-edit-form>

      <gaf-modal v-model="configParamVisible" :width="1000" :footer="null">
        <config-template-param
          :key="selectedTemplateId"
          title="模板参数配置"
          :template-id="selectedTemplateId"
          :template-name="selectedTemplateName"
          @cancel="handleCancel"
          @back="handlePreStep"
          @saveConfig="handlerSaveConfig"
        />
      </gaf-modal>
    </div>
  </div>
</template>

<script>
// @ts-nocheck
//test
// @ts-ignore
import AddEditForm from '../../views/codeTemplate/AddOrEditForm'
// @ts-ignore
import ConfigTemplateParam from '../../views/codeTemplate/ConfigTemplateParam'

export default {
  components: {
    ConfigTemplateParam,
    AddEditForm,
  },
  data() {
    return {
      searchedColumn: 'template_name',
      configParamVisible: false,
      groupVisible: false,
      clearFilters: null,
      // 非多个禁用
      multiple: true,
      // 标题
      title: '',
      // 编辑记录
      editData: {},
      // 总条数
      total: 0,
      // ${functionName}表格数据
      projCodeTemplateList: [],
      // 是否显示添加修改弹出层
      open: false,
      // 分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0,
      },
      // 列表是否加载中
      loading: true,
      searchText: '',
      searchInput: null,
      sorter: {
        order: '',
        field: '',
      },
      // 有无主键
      hasPKField: true,
      paramGroupTableIsVisiable: false,
      selectedTemplateId: '',
      selectedTemplateName: '',
    }
  },
  computed: {
    columns() {
      const columns = [
        {
          title: '代码模板id',
          dataIndex: 'projCodeTemplateId',
          key: 'proj_code_template_id',
        },
        {
          title: '模板名称',
          scopedSlots: {
            filterDropdown: 'filterDropdown',
            filterIcon: 'filterIcon',
            customRender: 'customRender',
          },
          dataIndex: 'templateName',
          key: 'template_name',
        },
        {
          title: '模板中文名称',
          scopedSlots: {
            filterDropdown: 'filterDropdown',
            filterIcon: 'filterIcon',
            customRender: 'customRender',
          },
          dataIndex: 'templateNameCn',
          key: 'template_name_cn',
        },
        {
          title: '模板类型',
          dataIndex: 'templateType',
          key: 'template_type',
        },
        // {
        //   title: '描述',
        //   dataIndex: 'description',
        //   key: 'description'
        // },
        {
          title: '版本',
          scopedSlots: {
            filterDropdown: 'filterDropdown',
            filterIcon: 'filterIcon',
            customRender: 'customRender',
          },
          dataIndex: 'version',
          key: 'version',
        },
        {
          title: '操作',
          scopedSlots: { customRender: 'operation' },
        },
      ]
      return this.hasPKField ? columns : columns.slice(0, columns.length - 2)
    },
    timeFormat() {
      if (
        this.columns.filter(
          (item) =>
            item.scopedSlots && item.scopedSlots.customRender === 'timeRender'
        ).length > 0
      ) {
        return function (str) {
          const dt = new Date(str)
          const year = dt.getFullYear()
          let month = dt.getMonth() + 1
          let date = dt.getDate()
          let hour = dt.getHours()
          let minute = dt.getMinutes()
          let second = dt.getSeconds()

          // @ts-ignore
          month = month < 10 ? '0' + month : month
          date = date < 10 ? '0' + date : date
          hour = hour < 10 ? '0' + hour : hour
          minute = minute < 10 ? '0' + minute : minute
          second = second < 10 ? '0' + second : second

          return `${year}/${month}/${date} ${hour}:${minute}:${second}`
        }
      }
      return null
    },
  },
  created() {
    this.getList()
  },

  methods: {
    handlerSaveConfig() {
      this.editData = {}
      this.configParamVisible = false
    },
    handleCancel() {
      this.editData = {}
      this.selectedTemplateId = ''
      this.configParamVisible = false
    },
    handlePreStep() {
      this.configParamVisible = false
    },
    // 根据搜索文本拆分单元格文本内容
    splitCellWithSearchText(text) {
      const str = text === null ? '' : text
      return str
        .toString()
        .split(
          new RegExp(`(?<=${this.searchText})|(?=${this.searchText})`, 'i')
        )
    },
    // 搜索查询
    async onSearch(val) {
      console.log(val)
      this.searchText = val
      this.pagination.current = 1
      await this.getList()
    },
    // 重置查询
    handleReset(clearFilters, key) {
      clearFilters()
      if (this.searchedColumn === key) {
        this.searchText = ''
        this.searchedColumn = ''
        this.clearFilters = null
      }
    },
    // 页码，排序项发生改变时，重新获取列表数据
    tableChange(pageInfo, filters, sorter) {
      if (pageInfo) {
        this.pagination.current = pageInfo.current
        this.pagination.pageSize = pageInfo.pageSize
      }
      if (sorter) {
        this.sorter.order = sorter.order === 'descend' ? 'DESC' : 'ASC'
        this.sorter.field = sorter.columnKey
      }
      this.getList()
    },
    // 添加数据
    handleAdd() {
      this.open = true
      this.title = '添加模板表'
    },
    // 添加修改提交后
    handleSubmit() {
      this.open = false
      this.editData = {}
      this.getList()
    },
    // 添加修改返回后
    handleBack() {
      this.editData = {}
      this.getList()
      this.open = false
    },
    // 修改数据
    handleUpdate(row) {
      this.open = true
      this.title = '修改模板表'
      this.editData = row
    },
    // 删除数据
    async handleDelete(row) {
      const url = '/proj/projcodetemplate/' + row.projCodeTemplateId
      const rst = await this.$axios.delete(url)
      if (rst.data.isSuccessed) {
        this.$message.success('删除成功')
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`)
      }
      this.$nextTick(() => {
        this.getList()
      })
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
      if (this.selectedRowKeys.length > 0) {
        this.multiple = false
      } else {
        this.multiple = true
      }
    },
    // 查询模板信息
    async getList() {
      this.loading = true
      let url = `/proj/projcodetemplate?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`
      if (this.searchText.trim() && this.searchedColumn) {
        url =
          url +
          '&searchFieldName=' +
          this.searchedColumn +
          '&searchFieldValue=' +
          this.searchText.trim()
      }
      if (this.sorter.order && this.sorter.field) {
        url =
          url +
          '&orderFieldName=' +
          this.sorter.field +
          '&orderMethod=' +
          this.sorter.order
      }
      const res = await this.$axios.$get(url)
      this.loading = false
      if (res.isSuccessed) {
        this.projCodeTemplateList = res.data.pageList
        this.pagination.total = res.data.totalCount
      } else {
        this.$message.error(`查询失败,原因:${res.message}`)
      }
    },
    // 查询所属当前模板的模板参数分组
    handleQueryParamGroup(row) {
      this.editData = row
      this.selectedTemplateId = row.projCodeTemplateId
      this.selectedTemplateName = row.templateName
      this.configParamVisible = true
    },
  },
}
</script>

<style scoped>
.highlight {
  background-color: rgb(255, 192, 105);
  padding: 0px;
}
</style>
