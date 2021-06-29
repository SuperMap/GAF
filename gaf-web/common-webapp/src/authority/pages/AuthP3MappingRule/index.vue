<template>
  <div class="app-container">
    <div class="page-single">
      <gaf-table-layout v-show="!open">
        <template #actions>
          <button class="btn-fun blue btn-16" @click="handleAdd">
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
              placeholder="请输入规则名称查询"
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
            :row-selection="{
              selectedRowKeys: selectedRowKeys,
              onChange: onSelectChange,
              onSelect: rowSelect,
              onSelectAll: rowSelectAll,
            }"
            :data-source="authP3MappingRuleList"
            :loading="loading"
            @change="tableChange"
            :row-key="(r, i) => r.mappingRuleId"
            :columns="
              columns.filter((item) => item.dataIndex !== 'mappingRuleId')
            "
            class="table-style"
            size="middle"
          >
            <template slot="mapType" slot-scope="text, record">
              <slot v-bind:ot="{ text, record }">
                <div v-if="record.mappingType == 1">租户</div>
                <div v-else-if="record.mappingType == 2">部门</div>
                <div v-else-if="record.mappingType == 3">用户</div>
              </slot>
            </template>
            <template slot="mapMethod" slot-scope="text, record">
              <slot v-bind:ot="{ text, record }">
                <div v-if="record.mappingMethod == 1">推</div>
                <div v-else-if="record.mappingMethod == 2">拉</div>
              </slot>
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
                <u>详情</u>
              </a>
              <a
                @click.stop="() => handleUpdate(record)"
                href="javascript:;"
                class="btn-margin"
                ><u>编辑</u>
              </a>

              <a-popconfirm
                @confirm="() => handleDelete(record)"
                title="删除后无法恢复，确认是否继续?"
                ok-text="确认"
                cancel-text="取消"
              >
                <a href="javascript:;"><u>删除</u></a>
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
        :addData="addData"
        @submit="handleSubmit"
        @back="handleBack"
        v-if="open"
        :operation="operation"
      >
      </add-edit-form>
    </div>
  </div>
</template>

<script>
import AddEditForm from "../../views/AuthP3MappingRule/AddOrEditForm";
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
      addData: {},
      // 总条数
      total: 0,
      selectedRowKeys: [],
      // ${functionName}表格数据
      authP3MappingRuleList: [],
      // 是否显示添加修改弹出层
      open: false,
      // 分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: "",
      },
      // 列表是否加载中
      loading: true,
      searchText: "",
      searchInput: null,
      searchedColumn: "mapping_rule_name",
      sorter: {
        order: "",
        field: "",
      },
      // 详情：1，查看 2. 新增：3. 编辑
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
          title: "规则名称",
          width: '16%',
          dataIndex: "mappingRuleName",
          key: "mapping_rule_name",
        },
        {
          title: "第三方组件",
          width: '8%',
          dataIndex: "p3ComponentName",
          key: "p3_component_name",
        },
        {
          width: '8%',
          title: "映射类型",
          dataIndex: "mappingType",
          key: "mapping_type",
          scopedSlots: { customRender: "mapType" },
        },
        {
          title: "同步方式",
          width: '8%',
          dataIndex: "mappingMethod",
          key: "mapping_method",
          scopedSlots: { customRender: "mapMethod" },
        },
        {
          title: "状态",
          width: '7%',
          dataIndex: "status",
          key: "status",
        },
        {
          title: "描述",
          width: '7%',
          dataIndex: "description",
          key: "description",
        },
        {
          title: "创建时间",
          width: '12%',
          scopedSlots: {
            customRender: "timeRender",
          },
          dataIndex: "createdTime",
          key: "created_time",
        },
        {
          title: "创建人",
          width: '7%',
          dataIndex: "createdBy",
          key: "created_by",
        },
        {
          title: "操作",
          // fixed: "right",
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
      const url = "/authority/auth-p3-mapping-rule/";
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
            selectedRowKeys.length === this.authP3MappingRuleList.length
          ) {
            this.pagination.current--;
          }
          this.getList();
          this.selectedRowKeys = [];
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
      console.log(pageInfo, filters, sorter);
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
      console.log(this.editData);
      this.operation = 2;
      this.open = true;
      this.title = "添加记录";
      this.addData = {};
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
      this.title = "修改记录";
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
      console.log(row);
      const url = "/authority/auth-p3-mapping-rule/" + row.mappingRuleId;
      const rst = await this.$axios.delete(url);
      if (rst.data.isSuccessed) {
        this.$message.success("删除成功");
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`);
      }
      this.$nextTick(() => {
        if (
          this.pagination.current !== 1 &&
          this.authP3MappingRuleList.length === 1
        ) {
          this.pagination.current--;
        }
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
      let url = `/authority/auth-p3-mapping-rule?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`;
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
        this.pagination.total = res.data.totalCount;
        this.authP3MappingRuleList = res.data.pageList;
        console.log("res.data329", this.authP3MappingRuleList);
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
