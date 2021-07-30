<template>
  <div>
    <gaf-table-layout>
      <template #actions>
        <button @click="handleAdd" class="btn-fun blue btn-16">
          <a-icon type="plus-circle" /><span>{{ addButtonName }}</span>
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
            title="是否清除勾选?"
            ok-text="确认"
            cancel-text="取消"
          >
            <a href="javascript:;"><u>清空</u></a>
          </a-popconfirm>
        </div>
        <gaf-table-with-page
          :scroll="{ y: 508, x: 1440 }"
          :showXH="false"
          :row-selection="{
            selectedRowKeys: selectedRowKeys,
            onChange: rowChange,
            onSelect: rowSelect,
            onSelectAll: rowSelectAll,
          }"
          :pagination="pagination"
          :data-source="LeafList2"
          :loading="loading"
          @change="tableChange"
          :row-key="(r) => r.key"
          :columns="
            columns.filter((item) => item.dataIndex !== 'resourceApiId')
          "
          class="table-style"
          size="middle"
        >
          <template slot="method" slot-scope="text, record">
            {{ getMethod(record.method) }}
          </template>
          <template slot="type" slot-scope="text, record">
            {{ record.type === "1" ? "应用组件资源" : "第三方资源" }}
          </template>
          <template slot="status" slot-scope="text, record">
            <a-switch
              :disabled="true"
              :checked="record.status"
              :style="{ background: record.status ? '#1890FF' : '#2F4F4F' }"
              checked-children="启用"
              un-checked-children="禁用"
            />
          </template>
          <template
            slot="operation"
            slot-scope="text, record"
            v-if="hasPKField"
          >
            <!-- <a
              @click.stop="() => handleDetail(record)"
              class="btn-view"
              href="javascript:;"
            >
              <a-icon type="profile" /> 详情
            </a>
            <a-divider type="vertical" />
            <a
              @click.stop="() => handleUpdate(record)"
              class="btn-edit"
              href="javascript:;"
            >
              <a-icon type="edit" /> 编辑
            </a>
            <a-divider type="vertical" /> -->
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
    <gaf-drawer
      :visible="modalVisible && openadd"
      :width="500"
      :footer="null"
      :centered="true"
      @close="cancleWhenAdd"
      :closable="false"
      placement="right"
      destroy-on-close
    >
      <add-edit-form
        ref="addEditForm"
        :editData="editData"
        :operation="operation1"
        :options="dataOfTree"
        :catalogType="catalogType"
        @add-success="afterFormAddSuccess"
        @update-success="afterFormUpdateSuccess"
        @delete-success="afterDeleteSuccess"
        @cancleWhenAdd="cancleWhenAdd"
      ></add-edit-form>
    </gaf-drawer>
  </div>
</template>

<script>
import AddEditForm from "../../views/SysCatalog/AddOrEditForm";
export default {
  components: {
    AddEditForm,
  },
  props: {
    component: {
      type: String,
      default: "",
    },
    dataOfTree: {
      type: Array,
      default: () => [],
    },
    LeafList: {
      type: Array,
      dafault: () => [],
    },
    editData: {
      type: Object,
      default: null,
    },
    catalogType: {
      type: String,
      default: "0",
    },
    operation1: {
      type: String,
      default: "edit",
    },
    addButtonName: {
      type: String,
      dafault: "",
    },
    openadd: {
      type: Boolean,
      dafault: false,
    },
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
      // 总条数
      total: 0,
      selectedRowKeys: [],
      selectRowLength: 0,
      // ${functionName}表格数据
      authResourceApiList: [],
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
      searchInput: null,
      searchedColumn: "name",
      sorter: {
        order: "",
        field: "",
      },
      // 详情：1，新增：2，编辑：3
      operation: 0,
      modalVisible: false,
      // 有无主键
      hasPKField: true,
      LeafList2: [],
      searchTextApiList: [],
    };
  },
  watch: {
    LeafList(newdata) {
      this.LeafList2 = JSON.parse(JSON.stringify(newdata)).map((item) => {
        delete item.children;
        return item;
      });
      this.searchTextApiList = JSON.parse(JSON.stringify(newdata)).map(
        (item) => {
          delete item.children;
          return item;
        }
      );
    },
  },
  computed: {
    columns: function () {
      const columns = [
        {
          title: "名称",
          dataIndex: "title",
          key: "title",
          // fixed: 'left',
        },
        {
          title: "操作",
          fixed: 'right',
          // width: 260,
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
  mounted() {
    this.LeafList2 = JSON.parse(JSON.stringify(this.LeafList)).map((item) => {
      delete item.children;
      return item;
    });
    this.searchTextApiList = JSON.parse(JSON.stringify(this.LeafList)).map(
      (item) => {
        delete item.children;
        return item;
      }
    );
  },
  methods: {
    // 清空
    clearOptions() {
      this.selectedRowKeys = [];
      this.selectRowLength = 0;
    },
    // 复选框
    rowChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
      this.selectRowLength = selectedRowKeys.length;
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
    // 功能未实现
    // 搜索查询
    onSearch(val) {
      this.searchText = val;
      this.pagination.current = 1;
      if (val === "") {
        this.LeafList2 = this.searchTextApiList;
      } else {
        this.LeafList2 = this.searchTextApiList.filter(
          (ltem) => ltem.title.includes(val) === true
        );
      }
    },
    // 重置查询
    /*  handleReset(clearFilters, key) {
      clearFilters()
      if (this.searchedColumn === key) {
        this.searchText = ''
        this.searchedColumn = ''
        this.clearFilters = null
      }
    }, */
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
      this.$emit("addSubCatalog");
      // this.operation = 2
      // this.operation1 = 'add'
      this.open = true;
      this.modalVisible = true;
      // this.title = '添加api资源'
    },
    // 添加修改提交后
    handleSubmit() {
      this.open = false;
      this.modalVisible = false;
      this.editData = {};
      this.getList();
    },
    // 添加修改返回后
    handleBack() {
      this.editData = {};
      this.open = false;
      this.modalVisible = false;
    },
    // 修改数据
    handleUpdate(row) {
      this.operation = 3;
      this.open = true;
      this.modalVisible = true;
      this.title = "修改api资源";
      this.editData = row;
    },
    handleDetail(row) {
      this.operation = 1;
      this.open = true;
      this.modalVisible = true;
      this.title = "详情展示";
      this.editData = row;
    },
    // 删除数据
    async handleDelete(row) {
      console.log(row);
      const url = `/sys-mgt/sys-catalogs/` + row.key;
      const rst = await this.$axios.delete(url);
      if (rst.data.isSuccessed) {
        this.LeafList.forEach((item, index) => {
          if (item.key === row.key) {
            this.LeafList.splice(index, 1);
          }
        });
        this.$message.success("删除成功");
        if (this.LeafList && this.LeafList.length === 0) {
          this.$emit("afterDeleteSuccess", rst.data.data, true);
        } else {
          this.$emit("afterDeleteSuccess", rst.data.data);
        }

        // this.$emit('delete-success', rst.data.data)
      } else {
        this.$message.error("删除失败，原因: " + rst.data.message);
      }
      this.$nextTick(() => {
        if (
          this.pagination.current !== 1 &&
          this.authResourceApiList.length === 1
        ) {
          this.pagination.current--;
        }
        // this.getList()
      });
    },
    // 批量删除
    async batchDel() {
      const url = "/sys-mgt/sys-catalogs/";
      const selectedRowKeys = this.selectedRowKeys;
      console.log(selectedRowKeys);
      if (selectedRowKeys.length !== 0) {
        const rst = await this.$axios.delete(url, { data: selectedRowKeys });
        if (rst.data.isSuccessed) {
          this.$message.success("删除成功");
          if (this.LeafList2 && this.LeafList2.length === 0) {
            this.$emit(
              "afterDeleteSuccess",
              { key: selectedRowKeys, parentId: this.LeafList[0].parentId },
              true
            );
          }
          this.$emit("afterDeleteSuccess", {
            key: selectedRowKeys,
            parentId: this.LeafList[0].parentId,
          });
          this.LeafList.forEach((item, index) => {
            if (this.selectedRowKeys.indexOf(item.key) > -1) {
              this.LeafList.splice(index, 1);
            }
          });
          this.selectedRowKeys = [];
        } else {
          this.$message.error(`删除失败,原因:${rst.data.message}`);
        }
        this.$nextTick(() => {
          if (
            this.pagination.current !== 1 &&
            selectedRowKeys.length === this.authResourceApiList.length
          ) {
            this.pagination.current--;
          }
          // this.getList()
        });
      } else {
        this.$message.warn("请选择您要删除的内容");
      }
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
      if (this.selectedRowKeys.length > 0) {
        this.multiple = false;
      } else {
        this.multiple = true;
      }
    },
    async getList() {
      this.loading = true;
      let url = `/authority/auth-resource-apis?pageSize=${this.pagination.pageSize}&pageNum=${this.pagination.current}`;
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
        this.searchTextApiList = res.data.pageList;
        this.authResourceApiList = res.data.pageList;
        this.pagination.total = res.data.totalCount;
      } else {
        this.$message.error(`查询失败,原因:${res.message}`);
      }
    },
    afterFormAddSuccess(data) {
      this.modalVisible = false;
      this.$emit("afterFormAddSuccess", data);
    },
    afterFormUpdateSuccess(data) {
      this.$emit("afterFormUpdateSuccess", data);
    },
    afterDeleteSuccess(data) {
      if (this.LeafList2 && this.LeafList2.length === 0) {
        this.$emit("afterDeleteSuccess", data, true);
      }
      this.$emit("afterDeleteSuccess", data);
    },
    cancleWhenAdd() {
      this.modalVisible = false;
      this.$emit("cancleWhenAdd", this.editData.parentId);
    },
  },
};
</script>
