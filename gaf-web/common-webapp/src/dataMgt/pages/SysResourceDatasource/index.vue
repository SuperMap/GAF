<template>
  <div class="page-single">
    <gaf-table-layout>
      <template #actions>
        <button
          @click="handleAdd"
          type="primary"
          visible="true"
          class="btn-fun blue btn-16"
        >
         <a-icon type="plus-circle" /><span>新增</span>
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
        <div class="search-position">
          <a-input-search
            @search="onSearch"
            placeholder="请输入规则名称查询"
            size="large"
          >
          </a-input-search>
        </div>
      </template>
      <template #default>
        <gaf-table-head :selectedRowKeys="selectedRowKeys" @clearOptions="clearOptions" />
        <gaf-table-with-page
          :scroll="{ y: 508, x : 1440}"
          :pagination="pagination"
          :row-selection="{
            selectedRowKeys: selectedRowKeys,
            onChange: onSelectChange,
            onSelect: rowSelect,
            onSelectAll: rowSelectAll,
          }"
          :data-source="sysResourceDatasourceList"
          :loading="loading"
          @change="tableChange"
          :row-key="(r, i) => r.datasourceId"
          :columns="columns.filter((item) => item.dataIndex !== 'datasourceId')"
          class="table-style"
          size="middle"
        >
          <template slot="databaseType" slot-scope="text">
            <span>
              {{ databaseTypeMap.get(text) }}
            </span>
          </template>
          <template
            slot="customRender"
            slot-scope="text, record, index, column"
          >
            <span v-if="searchText && searchedColumn === column.key">
              <template v-for="(fragment, i) in splitCellWithSearchText(text)">
                <mark
                  v-if="fragment.toLowerCase() === searchText.toLowerCase()"
                  :key="i"
                  class="highlight"
                  >{{ fragment }}</mark
                >
                <template v-else>{{ fragment }}</template>
              </template>
            </span>
            <template v-else>
              {{ text }}
            </template>
          </template>

          <template
            slot="operation"
            slot-scope="text, record"
            v-if="hasPKField"
          >
            <a
              @click.stop="() => handleDetail(record)"
              href="javascript:;"
              class="btn-margin"
            >
              详情
            </a>
            <a
              @click.stop="() => handleUpdate(record)"
              href="javascript:;"
              class="btn-margin"
              >编辑
            </a>

            <a-popconfirm
              @confirm="() => handleDelete(record)"
              title="删除后无法恢复，确认是否继续?"
              ok-text="确认"
              cancel-text="取消"
            >
              <a href="javascript:;">删除</a>
            </a-popconfirm>
          </template>

          <template slot="timeRender" v-if="timeFormat" slot-scope="text">
            {{ timeFormat(text) }}
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
      placement="right"
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
import AddEditForm from "../../views/SysResourceDatasource/AddOrEditForm";
import "../../../common/css/common.css";

export default {
  components: {
    AddEditForm,
  },
  data() {
    return {
      // 搜索项
      searchKey: "",
      clearFilters: null,
      // 非多个禁用
      multiple: true,
      // 标题
      title: "",
      // 编辑记录
      editData: {},
      // 总条数
      total: 0,
      selectedRowKeys: [],
      selectRowLength: 0,
      // ${functionName}表格数据
      sysResourceDatasourceList: [],
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
      searchInput: null,
      searchedColumn: "ds_name",
      sorter: {
        order: "",
        field: "",
      },
      // 详情：1，新增：2，编辑：3
      operation: 0,
      // 有无主键
      hasPKField: true,

      databaseTypeMap: new Map([
        ["1", "POSTGRESQL"],
        ["3", "MYSQL"],
        ["2", "ORACLE"],
        ["4", "SQL_SERVER"],
      ]),
    };
  },
  computed: {
    columns: function () {
      const columns = [
        {
          title: "数据源id",
          dataIndex: "datasourceId",
          key: "datasource_id",
        },
        {
          title: "数据源名称",
          scopedSlots: {
            filterDropdown: "filterDropdown",
            filterIcon: "filterIcon",
            customRender: "customRender",
          },
          dataIndex: "dsName",
          key: "ds_name",
        },
        {
          title: "类型",
          sorter: true,
          sortDirections: ["descend", "ascend"],
          dataIndex: "typeCode",
          key: "type_code",
          // scopedSlots: { customRender: 'databaseType' }
        },
        {
          title: "地址",
          dataIndex: "addr",
          key: "addr",
        },
        // {
        //   title: '端口',
        //   dataIndex: 'port',
        //   key: 'port'
        // },
        {
          title: "数据库名称",
          dataIndex: "dbName",
          key: "db_name",
        },
        {
          title: "用户名",
          dataIndex: "userName",
          key: "user_name",
        },
        {
          title: "描述",
          dataIndex: "description",
          key: "description",
        },
        {
          title: "操作",
          fixed: 'right',
          scopedSlots: { customRender: "operation" },
        },
      ];
      return this.hasPKField ? columns : columns.slice(0, columns.length - 2);
    },
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
  watch: {},
  created() {
    this.getList();
  },
  methods: {
    async onSearch(val) {
      this.searchText = val;
      this.pagination.current = 1;
      await this.getList();
    },
    async batchDel() {
      const url = "/data-mgt/sys-resource-datasources/";
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
            selectedRowKeys.length === this.sysResourceDatasourceList.length
          ) {
            this.pagination.current--;
          }
          this.selectedRowKeys = []
          this.getList();
        });
      } else {
        this.$message.warn("请选择您要删除的内容");
      }
    },
    rowSelect(record, selected, selectedRows) {
      console.log(record, selected, selectedRows);
    },
    rowSelectAll(selected, selectedRows, changeRows) {
      console.log(selected, selectedRows, changeRows);
    },
    // 根据搜索文本拆分单元格文本内容
    splitCellWithSearchText(text) {
      const str = text === null ? "" : text;
      return str
        .toString()
        .split(
          new RegExp(`(?<=${this.searchText})|(?=${this.searchText})`, "i")
        );
    },
    handleSearchFieldChange(value) {
      this.searchedColumn = value;
    },
    async handleFilterChange(value) {
      this.searchText = value;
      this.pagination.current = 1;
      await this.getList();
    },
    // 搜索查询
    handleSearch(selectedKeys, confirm, key, clearFilters) {
      if (this.searchedColumn !== key && this.clearFilters) this.clearFilters();
      confirm();
      this.searchText = selectedKeys[0];
      this.searchedColumn = key;
      this.clearFilters = clearFilters;
    },
    // 重置查询
    handleReset(clearFilters, key) {
      clearFilters();
      if (this.searchedColumn === key) {
        this.searchText = "";
        this.searchedColumn = "";
        this.clearFilters = null;
      }
    },
    // 页码，排序项发生改变时，重新获取列表数据
    tableChange(pageInfo, filters, sorter) {
      if (pageInfo) {
        this.pagination.current = pageInfo.current;
        this.pagination.pageSize = pageInfo.pageSize;
      }
      if (sorter) {
        this.sorter.order = sorter.order === "descend" ? "DESC" : "ASC";
        this.sorter.field = sorter.columnKey;
      }
      this.getList();
    },
    // 添加数据
    handleAdd() {
      this.open = true;
      this.operation = 2;
      this.title = "添加数据源";
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
      this.title = "修改数据源";
      this.editData = row;
    },
    handleDetail(row) {
      this.operation = 1;
      this.open = true;
      this.title = "详情展示";
      this.editData = row;
    },
    // 删除数据
    async handleDelete(row) {
      const url = `/data-mgt/sys-resource-datasources/` + row.datasourceId;
      const rst = await this.$axios.delete(url);
      if (rst.data.isSuccessed) {
        this.$message.success("删除成功");
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`);
      }
      this.$nextTick(() => {
        if (
          this.pagination.current !== 1 &&
          this.sysResourceDatasourceList.length === 1
        ) {
          this.pagination.current--;
        }
        this.selectedRowKeys = this.selectedRowKeys.filter(item => {
          return item !== row.datasourceId
        })
        this.getList();
      });
    },
    // 清空
    clearOptions() {
      this.selectedRowKeys = [];
      this.selectRowLength = 0;
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
      this.selectRowLength = selectedRowKeys.length;
      if (this.selectedRowKeys.length > 0) {
        this.multiple = false;
      } else {
        this.multiple = true;
      }
    },
    async getList() {
      this.loading = true;
      let url = `/data-mgt/sys-resource-datasources?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}&isSdx=false`;
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
        this.pagination.current = res.data.pageIndex;
        this.pagination.pageSize = res.data.pageSize;
        this.pagination.total = res.data.total;
        this.sysResourceDatasourceList = res.data.content;
        // this.sysResourceDatasourceList = res.data.content.filter(element => {
        //   return element.isSdx === false
        // })
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
.add-user-btn {
  border: none;
  background-color: rgb(47, 117, 249);
  color: rgba(255, 255, 255, 0.7);
  box-shadow: 0 0 2px 2px rgba(84, 92, 100, 0.55);
  transition: linear 0.2s;
  box-shadow: 3px 3px 0 rgba(128, 128, 128, 0.3);
}
</style>
