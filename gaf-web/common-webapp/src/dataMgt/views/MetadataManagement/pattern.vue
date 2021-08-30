<template>
  <div>
    <gaf-table-layout>
      <template #actions>
        <button @click="handleAdd" class="btn-fun blue btn-16">
          <a-icon type="plus-circle" /><span>新增</span>
        </button>
        <!-- <a-popconfirm
          class="btn-fun blue"
          title="删除后无法恢复，确认是否继续?"
          ok-text="确认"
          cancel-text="取消"
          @confirm="() => batchDel()"
        >
          <button class="btn-fun blue">
            <span>批量删除</span>
          </button>
        </a-popconfirm> -->
      </template>
      <template #filter>
        <!-- <div style="margin-top: 5px">
          <a-select
            size="large"
            style="
              width: 150px;
              box-shadow: 3px 3px 0 rgba(128, 128, 128, 0.1);
            "
            @change="handleSearchFieldChange"
            v-model="searchedColumn"
          >
            <a-select-option value="dict_name">
              名称
            </a-select-option>
            <a-select-option value="dict_value">
              字典值
            </a-select-option>
          </a-select>
          <a-input-search
            @search="onSearch"
            style="width: 320px"
            :placeholder="searchedColumn === 'dict_name'? '请输入名称模糊查询': '请输入字典值查询'"
            size="large"
          >
            <button slot="enterButton" class="btn-search">
              搜索
            </button>
          </a-input-search>
        </div> -->
        <div class="search-position">
          <a-input-search
            @search="onSearch"
            placeholder="输入查询条件"
            size="large"
          >
          </a-input-search>
        </div>
      </template>
      <template #default>
        <gaf-table-head :selectedRowKeys="selectedRowKeys" @clearOptions="clearOptions" />
        <gaf-table-with-page
          :showXH="false"
          :pagination="pagination"
          :data-source="fieldList"
          :loading="loading"
          @change="tableChange"
          :row-selection="{
            selectedRowKeys: selectedRowKeys,
            onChange: rowChange,
            onSelect: rowSelect,
            onSelectAll: rowSelectAll,
          }"
          :row-key="(r, i) => r.fieldName"
          :columns="columns"
          class="table-style"
          size="middle"
        >
          <template slot="fieldType" slot-scope="text">
            {{ infoBox.map.get(text)}}
          </template>
          <template slot="operation" slot-scope="text, record">
            <!-- <a
              @click.stop="() => handleAddChild(record)"
              class="btn-margin"
              href="javascript:;"
            >
              <u>添加子项</u>
            </a>
            <a
              @click.stop="() => handleDetail(record)"
              class="btn-margin"
              href="javascript:;"
            >
              <u>详情</u>
            </a> -->
            <a
              @click.stop="() => handleUpdate(record)"
              class="btn-margin"
              href="javascript:;"
            >
              <u>编辑</u>
            </a>
            <a-popconfirm
              @confirm="() => handleDelete(record)"
              title="删除后无法恢复，确认是否继续?"
              ok-text="确认"
              cancel-text="取消"
            >
              <a href="javascript:;"> <u>删除</u></a>
            </a-popconfirm>
          </template>
          <template v-if="timeFormat" slot="timeRender" slot-scope="text">
            {{ timeFormat(text) }}
          </template>
          <template slot="fieldNotNull" slot-scope="text">
            {{ text ? "是" : "否" }}
          </template>
          <template slot="fieldPrimaryKey" slot-scope="text">
            {{ text ? "是" : "否" }}
          </template>
        </gaf-table-with-page>
      </template>
    </gaf-table-layout>
	<gaf-drawer
    :visible="open"
    :width="500"
    :footer="null"
    :centered="true"
    @close="handleBack"
    :closable="false"
    placement="right"
    destroy-on-close
  >
    <add-edit-form
      :title="title"
      :editData="editData"
      :tableId="tableId"
      :modelType="modelType"
      @submit="handleSubmit"
      @back="handleBack"
      :operation="operation"
    >
    </add-edit-form>
  </gaf-drawer>
  </div>
</template>

<script>
import AddEditForm from "./patternAddOrEditForm";

export default {
  components: {
    AddEditForm,
  },
  props: {
    tableId: {
      type: String,
      default: null,
    },
    modelId: {
      type: String,
      default: null,
    },
    dictCode: {
      type: String,
      default: null,
    },
  },
  inject: ['infoBox'],
  data() {
    return {
      modelType: '',
      treeData: [],
      selectRowLength: 0,
      // 标题
      title: "",
      // 编辑记录
      editData: {},
      selectedRowKeys: [],
      // ${functionName}表格数据
      fieldList: [],
      tempFieldList: [],
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
      searchText: "",
      searchedColumn: "field_name",
      sorter: {
        order: "ASC",
        field: "sort_sn",
      },
      // 详情：1，新增：2，编辑：3
      operation: 0,
      columns: [
        {
          title: "字段名称",
          dataIndex: "fieldName",
          key: "field_name",
          width: 150,
        },
        {
          title: "字段类型",
          dataIndex: "fieldType",
          key: "field_type",
          scopedSlots: { customRender: "fieldType" },
          align: "center",
          width: 125,
        },
        {
          title: "长度",
          dataIndex: "fieldLength",
          key: "field_length",
          align: "center",
          width: 125,
        },
        {
          title: "非空",
          dataIndex: "fieldNotNull",
          scopedSlots: { customRender: "fieldNotNull" },
          key: "field_not_null",
          align: "center",
          width: 125,
        },
        {
          title: "主键",
          dataIndex: "fieldPrimaryKey",
          key: "field_primary_key",
          scopedSlots: { customRender: "fieldPrimaryKey" },
          align: "center",
          width: 125,
        },
        {
          title: "备注",
          dataIndex: "dictDesc",
          key: "dict_desc",
        },
        {
          title: "操作",
          key: "operation",
          scopedSlots: { customRender: "operation" },
          // fixed: "right",
          // width: 400,
        },
      ],
    };
  },
  watch: {
    tableId: async function () {
      this.pagination.current = 1;
      await this.getTreeData()
      this.getList();
    },
    modelId: function() {
      this.getModel()
    }
  },
  computed: {
    timeFormat: function () {
      if (
        this.columns.filter(
          (item) =>
            item.scopedSlots && item.scopedSlots.customRender === "timeRender"
        ).length > 0
      ) {
        return function (str) {
          if (!str || str === "") {
            return "";
          }
          const dt = new Date(str);
          const year = dt.getFullYear();
          let month = dt.getMonth() + 1;
          let date = dt.getDate();
          let hour = dt.getHours();
          let minute = dt.getMinutes();
          let second = dt.getSeconds();

          month = month < 10 ? "0" + month : month;
          date = date < 10 ? "0" + date : date;
          hour = hour < 10 ? "0" + hour : hour;
          minute = minute < 10 ? "0" + minute : minute;
          second = second < 10 ? "0" + second : second;

          return `${year}/${month}/${date} ${hour}:${minute}:${second}`;
        };
      }
      return null;
    },
  },
  async created() {
    // await this.getTreeData()
    this.treeData = this.infoBox.treeData
    this.fieldList = this.infoBox.data.spatialInfo.fields;
    this.tempFieldList = this.infoBox.data.spatialInfo.fields;
  },
  methods: {
    clearOptions() {
      this.selectedRowKeys = []
      this.selectRowLength = 0
    },
    handleSearchFieldChange(value) {
      this.searchedColumn = value;
    },
    // 搜索查询
    onSearch(val) {
      this.searchText = val;
      this.pagination.current = 1;
      if (val === "") {
        this.fieldList = this.tempFieldList;
      } else {
        this.fieldList = this.tempFieldList.filter(
          (ltem) => ltem.fieldName.includes(val) === true
        );
      }
    },
    // 页码，排序项发生改变时，重新获取列表数据
    tableChange(pageInfo, filters, sorter) {
      if (pageInfo) {
        this.pagination.current = pageInfo.current;
        this.pagination.pageSize = pageInfo.pageSize;
      }
      if (sorter.order) {
        this.sorter.order = sorter.order === "descend" ? "DESC" : "ASC";
        this.sorter.field = sorter.columnKey;
      } else {
        this.sorter.order = null;
        this.sorter.field = null;
      }
      this.getList();
    },
    // onExpandedRowsChange(expandedRows) {
    //   if(!expandedRows || expandedRows.length == 0) {
    //     this.expandedRowKeys = []
    //   } else {
    //     this.expandedRowKeys = expandedRows.map(el=> el.dictId);
    //   }
    // },
    // 添加数据
    handleAdd() {
      this.operation = 2;
      this.open = true;
      this.editData = { pid: this.dicTypeId, dictCode: this.dictCode };
      this.title = "添加字段";
    },
    // 添加修改提交后
    handleSubmit() {
      this.open = false;
      this.editData = {};
      this.getList();
    },
    // 添加修改返回后
    handleBack() {
      this.editData = {};
      this.open = false;
    },
    // 修改数据
    handleUpdate(row) {
      this.operation = 3;
      this.open = true;
      this.title = "修改字段";
      this.editData = row;
    },
    handleAddChild(row) {
      this.operation = 2;
      this.open = true;
      this.editData = { dictCode: this.dictCode, pid: row.key };
      this.title = "添加字典数据";
    },
    handleDetail(row) {
      this.operation = 1;
      this.open = true;
      this.title = "详情展示";
      this.editData = row;
    },
    // 删除数据
    async handleDelete(row) {
      const url = `/data-mgt/datasource/${this.infoBox.data.datasourceId}/datasets/${this.infoBox.data.recordName}/fields/${row.fieldName}`;
      const rst = await this.$axios.delete(url);
      if (rst.data.isSuccessed) {
        this.$message.success("删除成功");
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`);
      }
      this.$nextTick(() => {
        // if (this.pagination.current !== 1 && this.fieldList.length === 1) {
        //   this.pagination.current--;
        // }
        this.selectedRowKeys = this.selectedRowKeys.filter(item => {
          return item !== row.key
        })
        this.getList();
        this.infoBox.updatedData()
      });
    },
    // 批量删除
    async batchDel() {
      const url = `/data-mgt/model-manage/fields`;
      const selectedRowKeys = this.selectedRowKeys;
      if (selectedRowKeys.length !== 0) {
        const rst = await this.$axios.delete(url, { data: selectedRowKeys });
        if (rst.data.isSuccessed) {
          this.$message.success("删除成功");
        } else {
          this.$message.error(`删除失败,原因:${rst.data.message}`);
        }
        this.$nextTick(() => {
          // if (
          //   this.pagination.current !== 1 &&
          //   selectedRowKeys.length === this.fieldList.length
          // ) {
          //   this.pagination.current--;
          // }
          this.getList();
          this.selectedRowKeys = []
        });
      } else {
        this.$message.warn("请选择您要删除的内容");
      }
    },
    // 复选框
    rowChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
      this.selectRowLength = selectedRowKeys.length
    },
    rowSelect(record, selected, selectedRows) {
      // eslint-disable-next-line no-console
      console.log(record, selected, selectedRows);
    },
    rowSelectAll(selected, selectedRows, changeRows) {
      // eslint-disable-next-line no-console
      console.log(selected, selectedRows, changeRows);
    },
    async getList() {
      console.log(this.pagination)
      this.loading = true;
      let url = `/data-mgt/datasource/${this.infoBox.data.datasourceId}/datasets/${this.infoBox.data.recordName}/fields?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`;
      if (this.searchText.trim() && this.searchedColumn) {
        url =
          url +
          "&searchFieldName=" +
          this.searchedColumn +
          "&searchFieldValue=" +
          this.searchText.trim();
      }
      if (this.sorter.order && this.sorter.field) {
        url =
          url +
          "&orderFieldName=" +
          this.sorter.field +
          "&orderMethod=" +
          this.sorter.order;
      }
      const res = await this.$axios.$get(url);
      this.loading = false;
      if (res.isSuccessed) {
        // this.pagination.current = res.data.pageNum;
        // this.pagination.pageSize = res.data.pageSize;
        // this.pagination.total = res.data.total;
        // children空数组置为null
        // if(res.data.content && res.data.content.length > 0) {
        //   treeUtil.deepFirstTraverseTree(null, res.data.content, (parentNode, node) => {
        //     if(node.children && node.children.length == 0) {
        //       node.children = null
        //     }
        //   })
        // }
        this.tempFieldList = res.data
        this.fieldList = res.data;
      } else {
        this.$message.error(`查询失败,原因:${res.message}`);
      }
    },
    async getModel() {
      // const url = `/data-mgt/model-manage/${modelId}`
      const url = `/data-mgt/model-manage/models/${this.modelId}`
      const res = await this.$axios.$get(url)
      if(res.isSuccessed) {
        this.modelType = res.data.modelType
      } else {
        this.$message.error(`查询模型失败，原因:${res.message}`)
        return null
      }
    },
  },
};
</script>

<style scoped>
.app-container {
  height: 100%;
}
</style>
