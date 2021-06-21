<template>
  <div>
    <gaf-table-layout>
      <template #actions>
        <button @click="handleAdd" class="btn-fun blue btn-16">
          <span><a-icon type="plus-circle" /> 新增</span>
        </button>
        <a-popconfirm
          class="btn-fun blue"
          title="删除后无法恢复，确认是否继续?"
          ok-text="确认"
          cancel-text="取消"
          @confirm="() => batchDel()"
        >
          <button class="btn-fun blue">
            <span>批量删除</span>
          </button>
        </a-popconfirm>
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
            placeholder="searchedColumn === 'dict_name'? '请输入名称模糊查询': '请输入字典值查询'"
            size="large"
          >
          </a-input-search>
        </div>
      </template>
      <template #default>
        <div class="choose-box">
          <a-icon type="exclamation-circle" class="exclamation" /><span
            >已选择</span
          >
          <b>{{ selectRowLength }}</b>
          <span>项</span>
          <a-popconfirm
            @confirm="() => clearOptions(record)"
            title="清空后无法恢复，确认是否继续?"
            ok-text="确认"
            cancel-text="取消"
          >
            <a href="javascript:;">清空</a>
          </a-popconfirm>
        </div>
        <gaf-table-with-page
          :scroll="{ y: 508 , x: 1440}"
          :showXH="false"
          :pagination="pagination"
          :data-source="sysDictList"
          :loading="loading"
          @change="tableChange"
          :row-selection="{
            selectedRowKeys: selectedRowKeys,
            onChange: rowChange,
            onSelect: rowSelect,
            onSelectAll: rowSelectAll,
          }"
          :row-key="(r) => r.key"
          :columns="columns"
          class="table-style"
          size="middle"
        >
          <template slot="operation" slot-scope="text, record">
            <a
              @click.stop="() => handleAddChild(record)"
              class="btn-margin"
              href="javascript:;"
            >
              添加子项
            </a>
            <a
              @click.stop="() => handleDetail(record)"
              class="btn-margin"
              href="javascript:;"
            >
              详情
            </a>
            <a
              @click.stop="() => handleUpdate(record)"
              class="btn-margin"
              href="javascript:;"
            >
              编辑
            </a>
            <a-popconfirm
              @confirm="() => handleDelete(record)"
              :title="'确定要删除字典数据' + record.label + '及其子字典'"
              ok-text="确认"
              cancel-text="取消"
            >
              <a href="javascript:;"> 删除</a>
            </a-popconfirm>
          </template>
          <template v-if="timeFormat" slot="timeRender" slot-scope="text">
            {{ timeFormat(text) }}
          </template>
          <template slot="visibility" slot-scope="text">
            {{ text ? "可见" : "不可见" }}
          </template>
        </gaf-table-with-page>
      </template>
    </gaf-table-layout>
	<a-drawer
    :visible="open"
    :width="500"
    :footer="null"
    :centered="true"
    @close="handleBack"
    :closable="false"
    destroy-on-close
  >
    <add-edit-form
      :title="title"
      :editData="editData"
      @submit="handleSubmit"
      @back="handleBack"
      :operation="operation"
    >
    </add-edit-form>
  </a-drawer>
  </div>
</template>

<script>
import AddEditForm from "./DicForm";
// import treeUtil from '../../utils/tree/TreeUtil.js'

export default {
  components: {
    AddEditForm,
  },
  props: {
    dicTypeId: {
      type: String,
      default: null,
    },
    dictCode: {
      type: String,
      default: null,
    },
  },
  data() {
    return {
      selectRowLength: 0,
      // 标题
      title: "",
      // 编辑记录
      editData: {},
      selectedRowKeys: [],
      // ${functionName}表格数据
      sysDictList: [],
      // 是否显示添加修改弹出层
      open: false,
      // 分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0,
      },
      // 列表是否加载中
      loading: false,
      searchText: "",
      searchedColumn: "dict_name",
      sorter: {
        order: "ASC",
        field: "seq",
      },
      // 详情：1，新增：2，编辑：3
      operation: 0,
      columns: [
        {
          title: "名称",
          dataIndex: "label",
          key: "dict_name",
          width: 500,
          align: "left",
        },
        {
          title: "字典值",
          dataIndex: "value",
          key: "dict_value",
          align: "center",
          width: 125,
        },
        {
          title: "序号",
          sorter: true,
          defaultSortOrder: "ascend",
          sortDirections: ["descend", "ascend"],
          dataIndex: "seq",
          key: "seq",
          align: "center",
          width: 125,
        },
        {
          title: "可见性",
          dataIndex: "visibility",
          scopedSlots: { customRender: "visibility" },
          key: "visibility",
          align: "center",
          width: 125,
        },
        {
          title: "描述",
          dataIndex: "dictDesc",
          key: "dict_desc",
          width: 300,
        },
        {
          title: "操作",
          key: "operation",
          scopedSlots: { customRender: "operation" },
          fixed: "right",
          width: 400,
        },
      ],
    };
  },
  watch: {
    dicTypeId: function () {
      this.pagination.current = 1;
      this.getList();
    },
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
  created() {
    if (this.dicTypeId) {
      this.getList();
    }
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
    async onSearch(val) {
      this.searchText = val;
      this.pagination.current = 1;
      await this.getList();
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
      this.title = "添加字典数据";
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
      this.title = "修改字典数据";
      this.editData = {
        dataDictId: row.key,
        pid: row.parentId,
        dictCode: this.dictCode,
        dictName: row.label,
        dictValue: row.value,
        seq: row.seq,
        visibility: row.visibility,
        dictDesc: row.dictDesc,
        createdTime: row.createdTime,
        createdBy: row.createdBy,
        updatedTime: row.updatedTime,
        updatedBy: row.updatedBy,
        extProperties: row.extProperties,
      };
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
      this.editData = {
        dataDictId: row.key,
        pid: row.parentId,
        dictCode: this.dictCode,
        dictName: row.label,
        dictValue: row.value,
        seq: row.seq,
        visibility: row.visibility,
        dictDesc: row.dictDesc,
        createdTime: row.createdTime,
        createdBy: row.createdBy,
        updatedTime: row.updatedTime,
        updatedBy: row.updatedBy,
        extProperties: row.extProperties,
      };
    },
    // 删除数据
    async handleDelete(row) {
      const url = `/sys-mgt/sys-dicts/` + row.key;
      const rst = await this.$axios.delete(url);
      if (rst.data.isSuccessed) {
        this.$message.success("删除成功");
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`);
      }
      this.$nextTick(() => {
        if (this.pagination.current !== 1 && this.sysDictList.length === 1) {
          this.pagination.current--;
        }
        this.getList();
      });
    },
    // 批量删除
    async batchDel() {
      const url = `/sys-mgt/sys-dicts/`;
      const selectedRowKeys = this.selectedRowKeys;
      if (selectedRowKeys.length !== 0) {
        const rst = await this.$axios.delete(url, { data: selectedRowKeys });
        if (rst.data.isSuccessed) {
          this.$message.success("删除成功");
        } else {
          this.$message.error(`删除失败,原因:${rst.data.message}`);
        }
        this.$nextTick(() => {
          if (
            this.pagination.current !== 1 &&
            selectedRowKeys.length === this.sysDictList.length
          ) {
            this.pagination.current--;
          }
          this.getList();
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
      this.loading = true;
      let url = `/sys-mgt/sys-dicts?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`;
      if (this.dicTypeId) {
        url += `&pid=${this.dicTypeId}`;
      }
      if (this.dictCode) {
        url += `&dictCode=${this.dictCode}`;
      }
      if (
        this.searchText.trim() &&
        this.searchedColumn &&
        this.searchedColumn === "dict_value"
      ) {
        url += `&dictValue=${this.searchText.trim()}`;
      }
      if (
        this.searchText.trim() &&
        this.searchedColumn &&
        this.searchedColumn === "dict_name"
      ) {
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
        this.pagination.current = res.data.pageIndex;
        this.pagination.pageSize = res.data.pageSize;
        this.pagination.total = res.data.total;
        // children空数组置为null
        // if(res.data.content && res.data.content.length > 0) {
        //   treeUtil.deepFirstTraverseTree(null, res.data.content, (parentNode, node) => {
        //     if(node.children && node.children.length == 0) {
        //       node.children = null
        //     }
        //   })
        // }
        this.sysDictList = res.data.content;
      } else {
        this.$message.error(`查询失败,原因:${res.message}`);
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
