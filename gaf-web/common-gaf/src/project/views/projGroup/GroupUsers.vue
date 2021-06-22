<template>
  <div>
    <gaf-detail style="padding-left: 15px">
      <template>
        <div style="margin: 7px 0">
          <a-breadcrumb separator=">">
            <a-breadcrumb-item class="text-bolder">
              <a @click="$emit('back')">工程组管理</a>
            </a-breadcrumb-item>
            <a-breadcrumb-item class="text-bolder">
              <span class="vertical-line">| </span>
              工程组成员管理</a-breadcrumb-item
            >
          </a-breadcrumb>
        </div>
      </template>
      <gaf-table-layout>
        <template #actions>
          <a-button
            class="btn-fun blue btn-16"
            icon="plus-circle"
            visible="true"
            @click="handleAddMember"
          >
            新增
          </a-button>
        </template>
        <template #default>
          <gaf-table-with-page
            :scroll="{ y: 508, X: 1440 }"
            :pagination="pagination"
            :data-source="projGroupList"
            :loading="loading"
            :row-key="(r, i) => i.toString()"
            :columns="columns"
            class="table-style"
            size="middle"
            @change="tableChange"
          >
            <template slot="operation" slot-scope="text, record">
              <a
                href="javascript:;"
                class="btn-margin"
                @click.stop="() => handleUpdate(record)"
              >
                编辑
              </a>
              <!-- <span class="divider">|</span> -->
              <a href="javascript:;" @click.stop="() => handleDelete(record)">
                删除
              </a>
            </template>
          </gaf-table-with-page>
        </template>
      </gaf-table-layout>
    </gaf-detail>

    <gaf-modal
      v-model="addMemberVisible"
      :width="800"
      :footer="null"
      :title="userTitle"
    >
      <add-or-edit-members-form
        :title="userTitle"
        :record-item="recordItem"
        :edit-user-data="editUserData"
        :page-visible="addMemberVisible"
        @submit="handleSubmit"
        @cancel="handleCancel"
      >
      </add-or-edit-members-form>
    </gaf-modal>
  </div>
</template>

<script>
import AddOrEditMembersForm from "./AddOrEditMembersForm";

export default {
  components: {
    AddOrEditMembersForm,
  },
  props: {
    recordItem: {
      type: Object,
      default: null,
    },
    isRequesting: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      // 标题
      userTitle: "",
      // 编辑记录
      editUserData: {},
      // ${functionName}表格数据
      projGroupList: [],
      // 分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0,
      },
      // 列表是否加载中
      loading: true,
      // 有无主键
      generalVisible: false,
      showGroupUsers: false,
      addMemberVisible: false,
    };
  },
  computed: {
    columns: () => {
      const arr = [
        {
          title: "用户名称",
          dataIndex: "userName",
          key: "user_name",
        },
        {
          title: "用户权限",
          dataIndex: "projRole",
          key: "proj_role",
          customRender(projRole) {
            const accessLevelEnum = {
              10: "访客",
              20: "报告者",
              30: "开发者",
              40: "维护者",
              50: "所有者",
            };
            return accessLevelEnum[projRole];
          },
        },
        {
          title: "操作",
          fixed: "right",
          scopedSlots: { customRender: "operation" },
        },
      ];
      return arr;
    },
  },
  watch: {
    recordItem(val) {
      this.getMemberList(val);
    },
  },
  methods: {
    handleCancel() {
      this.addMemberVisible = false;
    },
    // 页码，排序项发生改变时，重新获取列表数据
    tableChange(pageInfo) {
      if (pageInfo) {
        this.pagination.current = pageInfo.current;
        this.pagination.pageSize = pageInfo.pageSize;
      }
      this.getMemberList(this.recordItem);
    },
    handleAddMember() {
      this.userTitle = `添加工程组成员`;
      this.addMemberVisible = true;
    },
    // 添加修改提交后
    handleSubmit() {
      this.editUserData = {};
      this.getMemberList(this.recordItem);
      this.addMemberVisible = false;
    },
    // 修改数据
    handleUpdate(row) {
      this.userTitle = "编辑工程组成员";
      this.editUserData = row;
      this.addMemberVisible = true;
    },
    // 删除数据
    async handleDelete(row) {
      const url = `/proj/groups/${this.recordItem.projGroupId}/members/${row.projGroupMemberId}`;
      const rst = await this.$axios.delete(url);
      if (rst.data.isSuccessed) {
        this.$message.success("删除成功");
      } else {
        this.$message.error(`删除失败,原因:${rst.data.message}`);
      }
      this.$nextTick(() => {
        this.getMemberList(this.recordItem);
      });
    },
    async getMemberList(item) {
      if (!this.isRequesting) return false;
      this.loading = true;
      const url = `/proj/groups/${item.projGroupId}/members`;
      const res = await this.$axios.$get(url);
      this.loading = false;
      if (res.isSuccessed) {
        this.projGroupList = res.data;
      } else {
        this.$message.error(`成员查询失败,原因:${res.message}`);
      }
    },
  },
};
</script>

<style lang="less" scoped>
.divider {
  background: #e8e8e8;
  color: transparent;
  font-size: 1px;
  margin: 0 1px;
}
</style>
