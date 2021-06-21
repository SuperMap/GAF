<template>
  <div class="page-single">
    <gaf-table-layout v-show="!open">
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
        <div class="search-position">
          <a-input-search
            @search="onSearch"
            placeholder="请输入名称查询"
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
          :scroll="{ y: 508, x: 1440 }"
          :pagination="pagination"
          :data-source="authResourceModuleList"
          :loading="loading"
          @change="tableChange"
          :row-selection="{
            selectedRowKeys: selectedRowKeys,
            onChange: onSelectChange,
            onSelect: rowSelect,
            onSelectAll: rowSelectAll,
          }"
          :row-key="(r, i) => r.resourceModuleId"
          :columns="
            columns.filter((item) => item.dataIndex !== 'resourceModuleId')
          "
          class="table-style"
          size="middle"
        >
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
    <add-edit-form
      :title="title"
      :editData="editData"
      @submit="handleSubmit"
      @back="handleBack"
      v-if="open"
      :operation="operation"
    >
    </add-edit-form>
  </div>
</template>

<script>
import AddEditForm from "../../views/AuthResourceModule/AddOrEditForm";
import "~/assets/css/common.css";

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
      // ${functionName}表格数据
      authResourceModuleList: [],
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
      searchedColumn: "name",
      sorter: {
        order: "",
        field: "",
      },
      // 详情：1，新增：2，编辑：3
      operation: 0,
      // 有无主键
      hasPKField: true,
      selectRowLength: 0,
    };
  },
  computed: {
    columns: function () {
      const columns = [
        {
          title: "接口资源id",
          dataIndex: "resourceModuleId",
          key: "resource_module_id",
        },
        {
          title: "所属组件id",
          dataIndex: "sysComponentId",
          key: "sys_component_id",
        },
        {
          title: "API组id",
          dataIndex: "moduleCatalogId",
          key: "module_catalog_id",
        },
        {
          title: "名称",
          dataIndex: "name",
          key: "name",
        },
        {
          title: "类型",
          dataIndex: "type",
          key: "type",
        },
        {
          title: "地址",
          dataIndex: "moduleUrl",
          key: "module_url",
        },
        {
          title: "图标地址",
          dataIndex: "iconUrl",
          key: "icon_url",
        },
        {
          title: "状态",
          dataIndex: "status",
          key: "status",
        },
        {
          title: "排序序号",
          dataIndex: "sortSn",
          key: "sort_sn",
        },
        {
          title: "描述",
          dataIndex: "description",
          key: "description",
        },
        {
          title: "操作",
          fixed: "right",
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
  created() {
    this.getList();
  },
  methods: {
    async onSearch(val) {
      console.log(val);
      this.searchText = val;
      this.pagination.current = 1;
      await this.getList();
    },
    async batchDel() {
      const url = "/authority/auth-resource-modules/";
      const selectedRowKeys = this.selectedRowKeys;
      console.log(selectedRowKeys);
      if (selectedRowKeys.length !== 0) {
        const rst = await this.$axios.delete(url, { data: selectedRowKeys });
        if (rst.data.isSuccessed) {
          this.$message.success("删除成功");
        } else {
          this.$message.error(`删除失败,原因:${rst.data.message}`);
        }
        this.$nextTick(() => {
          this.pagination.current = 1;
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
      this.operation = 2;
      this.open = true;
      this.title = "添加模块";
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
      this.title = "修改模块";
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
      const url = "/authority/auth-resource-modules/" + row.resourceModuleId;
      const rst = await this.$axios.delete(url);
      if (rst.data.isSuccessed) {
        this.$message.success("删除成功");
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`);
      }
      this.$nextTick(() => {
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
      let url = "/authority/auth-resource-modules?1=1";
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
        this.authResourceModuleList = res.data.pageList;
        this.pagination.total = this.authResourceModuleList.length;
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
