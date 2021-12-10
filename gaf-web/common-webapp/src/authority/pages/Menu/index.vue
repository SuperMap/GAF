<template>
  <div class="app-container">
    <div class="page-single">
      <gaf-table-layout>
        <template #actions>
          <button @click="handleAdd" class="btn-fun blue btn-16">
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
              placeholder="请输入菜单名称模糊查询"
              size="large"
            >
            </a-input-search>
          </div>
        </template>
        <template #default>
          <gaf-table-head
            :selectedRowKeys="selectedRowKeys"
            @clearOptions="clearOptions"
          />
          <a-table
            :scroll="{ y: 553, x: 1440 }"
            :data-source="menuTree"
            :loading="loading"
            :row-selection="{
              selectedRowKeys: selectedRowKeys,
              onChange: rowChange,
            }"
            :row-key="(r) => r.key"
            :columns="columns"
            :pagination="false"
            class="table-style"
            size="middle"
          >
            <template slot="icon" slot-scope="text">
              <a-icon v-if="text && text !== ''" :type="text" /> 
            </template>
            <template slot="target" slot-scope="text">
              {{text == '0'? '当前窗口': '新窗口打开'}}
            </template>
            <template slot="operation" slot-scope="text, record">
              <a
                @click.stop="() => handleAddChild(record)"
                class="btn-margin"
                href="javascript:"
              >
                <u>添加子菜单</u>
              </a>
              <a
                @click.stop="() => handleUpdate(record)"
                class="btn-margin"
                href="javascript:"
              >
                <u>编辑</u>
              </a>
              <a-popconfirm
                @confirm="() => handleDelete(record)"
                :title="'确定要删除菜单 ' + record.name + ' 及其子菜单'"
                placement="topRight"
                ok-text="确认"
                cancel-text="取消"
              >
                <a href="javascript:"> <u>删除</u></a>
              </a-popconfirm>
            </template>
          </a-table>
        </template>
      </gaf-table-layout>
    </div>
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
        :menuTree="menuTree"
        @submit="handleSubmit"
        @back="handleBack"
        :operation="operation"
      >
      </add-edit-form>
    </a-drawer>
  </div>
</template>
<script>
import AddEditForm from "../../views/Menu/MenuAddOrEditForm.vue"
import "../../../common/css/common.css"
export default {
  components: {
    AddEditForm
  },
  data() {
    return {
      open: false,
      title: '',
      editData: {},
      operation: 2,
      searchText: undefined,
      loading: false,
      selectedRowKeys: [],
      menuTree: [],
      columns: [
        {
          title: "菜单名称",
          dataIndex: "name",
          key: "name",
        },
        {
          title: "图标",
          dataIndex: "icon",
          key: "icon",
          scopedSlots: { customRender: "icon" },
        },
        {
          title: "排序序号",
          dataIndex: "sortSn",
          key: "sort_sn",
          width: 150,
        },
        {
          title: "打开方式",
          dataIndex: "target",
          key: "target",
          scopedSlots: { customRender: "target" },
        },
        {
          title: "路径",
          dataIndex: "path",
          key: "path",
        },
        {
          title: "地址",
          dataIndex: "url",
          key: "module_url",
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
      ],
    }
  },
  created() {
    this.getMenuTree()
  },
  mounted() {},
  methods: {
    rowChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    onSearch(val) {
      this.searchText = val
      this.clearOptions()
      this.getMenuTree()
    },
    handleAdd() {
      this.operation = 2;
      this.title = "新增菜单";
      this.open = true;
    },
    async batchDel() {
      const url = `/authority/auth-resource-menus/`;
      const selectedRowKeys = this.selectedRowKeys;
      if (selectedRowKeys.length !== 0) {
        const rst = await this.$axios.delete(url, { data: selectedRowKeys });
        if (rst.data.isSuccessed) {
          this.$message.success("删除成功");
          this.selectedRowKeys = []
          this.getMenuTree();
        } else {
          this.$message.error(`删除失败,原因:${rst.data.message}`);
        }
      } else {
        this.$message.info("请选择您要删除的内容");
      }

    },
    handleAddChild(record) {
      this.operation = 2;
      this.title = "新增菜单";
      this.editData = {parentId: record.resourceMenuId}
      this.open = true;
    },
    handleUpdate(record) {
      this.operation = 3
      this.title = "修改菜单"
      this.editData = record
      this.open = true
    },
    async handleDelete(record) {
      const url = `/authority/auth-resource-menus/${record.resourceMenuId}`
      const res = await this.$axios.$delete(url);
      if (res.isSuccessed) {
        this.$message.success("删除成功");
        this.getMenuTree();
      } else {
        this.$message.error(`删除失败，原因:${res.message}`);
      }
    },
    handleBack() {
      this.editData = {}
      this.open = false
    },
    handleSubmit() {
      this.open = false
      this.editData = {}
      this.getMenuTree()
    },
    clearOptions() {
      this.selectedRowKeys = []
    },
    async getMenuTree() {
      let url = "/authority/auth-resource-menus/tree/condition"
      if(this.searchText && this.searchText !== '') {
        url += "?name=" + this.searchText
      }
      const res = await this.$axios.$get(url)
      if (res.isSuccessed) {
        this.menuTree = res.data
      } else {
        this.$message.error(`查询失败,原因:${res.message}`)
      }
    },
  },
}
</script>
<style scoped>
.app-container {
  height: 100%
}
</style>
