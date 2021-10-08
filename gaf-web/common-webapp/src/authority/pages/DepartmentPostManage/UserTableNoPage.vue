<template>
  <div>
    <gaf-table-layout>
      <template v-if="isUser" #actions>
        <button @click="handleAdd" class="btn-fun blue btn-l1">
          <span><a-icon type="plus-circle" /> 新增</span>
        </button>
        <!-- <button @click="batchDel" class="btn-fun red">
          <span><a-icon type="delete" />
          批量删除</span>
        </button> -->
      </template>
      <template #default>
        <!-- <div class="choose-box">
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
            <a href="javascript:;">清空</a>
          </a-popconfirm>
        </div> -->
        <gaf-table-no-page
          :scroll="{ y: 508, x: 1440 }"
          :data-source="authUserList"
          :loading="loading"
          :columns="
            isDepartmentId
              ? columns.filter((item) => item.dataIndex !== 'belongsParttime')
              : columns.filter((item) => item.dataIndex !== 'userId')
          "
          :row-key="(r, i) => r.userId"
          class="table-style"
          size="middle"
        >
          <template slot="status" slot-scope="text, record">
            <a-switch
              :disabled="!isUser"
              v-model="record.status"
              :default-checked="record.status"
              :style="{ background: record.status ? '#1890FF' : '#BFBFBF' }"
              @click="statusClick($event, record)"
              checked-children="已启用"
              size="small"
              un-checked-children="已禁用"
            />
          </template>
          <template slot="operation" slot-scope="text, record">
            <a
              v-if="isUser"
              @click.stop="() => setTenantSynchronization(record)"
              href="javascript:;"
            >
              <u>同步</u>
            </a>
            <!-- <a-divider type="vertical" />
        <a-popconfirm
          v-if="isUser"
          @confirm="() => handleDelete(record)"
          title="删除后无法恢复，确认是否继续?"
          ok-text="确认"
          cancel-text="取消"
        >
          <a href="javascript:;" class="btn-del"><a-icon type="delete" /> 删除</a>
        </a-popconfirm> -->
          </template>
          <template slot="isThirdParty" slot-scope="text, record">
            <span>{{ record.isThirdParty ? "是" : "否" }}</span>
          </template>
          <template slot="timeRender" v-if="timeFormat" slot-scope="text">
            {{ timeFormat(text) }}
          </template>
          <template slot="belongsParttime" slot-scope="text, record">
            <span>{{ record.belongsParttime ? "是" : "否" }}</span>
          </template>
        </gaf-table-no-page>
      </template>
    </gaf-table-layout>
    <gaf-modal
      v-model="mapList"
      :width="1000"
      :footer="null"
      title="第三方组件映射"
    >
      <mapping-process
        :tenantIdData="tenantIdData"
        :tenantRst="tenantRst"
      ></mapping-process>
    </gaf-modal>
  </div>
</template>
<script>
import MappingProcess from "../../views/AuthUser/MappingProcess";
export default {
  name: "UserTableNoPage",
  components: {
    MappingProcess,
  },
  props: {
    id: {
      type: String,
      required: true,
    },
    isDepartmentId: {
      type: Boolean,
      required: true,
    },
    isUser: {
      type: Boolean,
      required: false,
    },
  },
  data() {
    return {
      mapList: false,
      authUserList: [],
      // 列表是否加载中
      loading: true,
      hasPKField: true,
      userStatus: true,
      tenantIdData: "",
      tenantRst: [],
      selectedRowKeys: [],
      selectRowLength: 0,
      columns: []
    };
  },
  computed: {
    initColumns: function () {
      const columns = [
        {
          title: "登录用户名",
          dataIndex: "name",
          key: "name",
          width: 150
        },
        {
          title: "真实姓名",
          dataIndex: "realName",
          key: "real_name",
          width: 160
        },
        {
          title: "状态",
          dataIndex: "status",
          key: "status",
          scopedSlots: { customRender: "status" },
          width: 100
        },

        // {
        //   title: '部门',
        //   dataIndex: 'departmentId',
        //   key: 'department_id'
        // },
        {
          title: "部门",
          dataIndex: "departmentName",
          key: "department_name",
          width: 150
        },
        // {
        //   title: '岗位id',
        //   dataIndex: 'postId',
        //   key: 'post_id'
        // },
        {
          title: "岗位",
          dataIndex: "postName",
          key: "post_name",
          width: 150
        },
        {
          title: "是否第三方",
          dataIndex: "isThirdParty",
          key: "is_third_party",
          scopedSlots: { customRender: "isThirdParty" },
          width: 100
        },
        {
          title: "是否挂职",
          dataIndex: "belongsParttime",
          key: "belongs-parttime",
          scopedSlots: { customRender: "belongsParttime" },
          width: 100
        },
        {
          title: "授权截止时间",
          scopedSlots: {
            customRender: "timeRender",
          },
          dataIndex: "expirationTime",
          key: "expiration_time",
          width: 170
        },
        {
          title: "上次登录时间",
          scopedSlots: {
            customRender: "timeRender",
          },
          dataIndex: "lastLoginTime",
          key: "last_login_time",
          width: 170
        },
        {
          title: "操作",
          fixed: "right",
          width: 100,
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
          console.log(hour, "hour");
          return `${year}/${month}/${date} ${hour}:${minute}:${second}`;
        };
      }
      return null;
    },
  },
  created() {
    this.columns = this.initColumns
    this.columns = this.isUser ? this.initColumns : this.initColumns.slice(0, -1)
  },
  mounted() {
    // this.getList()
  },
  methods: {
    getList() {
      this.loading = true;
      let url = "";
      if (this.isDepartmentId) {
        url = `/authority/auth-users/list-by-department/${this.id}`;
      } else {
        url = `/authority/auth-users/list-by-post/${this.id}`;
      }
      this.$axios.$get(url).then((res) => {
        if (res.isSuccessed) {
          this.authUserList = res.data;
          this.loading = false;
        } else {
          this.$message.error(`查询失败,原因:${res.message}`);
        }
      });
    },
    // 同步
    async setTenantSynchronization(row) {
      this.mapList = true;
      this.mappingType = row.type;
      this.id = row.userId;
      this.tenantIdData = this.id;
      const url = `/authority/auth-p3-mapping-rule/list-by-type/`;
      const res = await this.$axios.get(url + 3 + "/" + this.id);
      this.tenantRst = res.data.data;
    },
    statusClick(value, record) {
      record.status = !value;
      if (!value) {
        const currentVueObj = this;
        this.$confirm({
          title: "确定要禁用该用户吗",
          content: () => (
            <div style="color:red;">
              禁用该用户会清空用户绑定的所有岗位、兼职和角色，该用户无法登录
            </div>
          ),
          okText: "确认",
          cancelText: "取消",
          onOk() {
            if (record.userId) {
              const url = `/authority/auth-users/${record.userId}`;
              return currentVueObj.$axios.delete(url).then((p) => {
                const result = p.data;
                if (result.isSuccessed) {
                  record.status = value;
                  currentVueObj.$message.success("禁用成功");
                  currentVueObj.$emit("deleteUserSuccess", record);
                } else {
                  currentVueObj.$message.error(
                    `禁用失败,原因：${result.message}`
                  );
                }
                currentVueObj.getList();
              });
            }
          },
          onCancel() {},
          centered: true,
          class: "test",
        });
      } else if (record.userId) {
        const url = `/authority/auth-users/active/${record.userId}`;
        this.$axios.post(url).then((p) => {
          const result = p.data;
          if (result.isSuccessed) {
            record.status = value;
            this.getList();
            this.$emit("activeUserSuccess", result.data);
          } else {
            this.$message.error(`启用失败，原因:${result.message}`);
          }
        });
      }
    },
    handleAdd() {
      // this.operation = 2
      // this.open = true
      // this.title = '添加模块API关联'
      this.$emit("handleAddUser");
    },
    async batchDel() {
      const url = "/authority/auth-users/batch";
      const selectedRowKeys = this.selectedRowKeys;
      if (selectedRowKeys.length !== 0) {
        const rst = await this.$axios.delete(url, { data: selectedRowKeys });
        if (rst.data.isSuccessed) {
          this.$message.success("删除成功");
        } else {
          this.$message.error(`删除失败,原因:${rst.data.message}`);
        }
        this.$nextTick(() => {
          // this.pagination.current = 1
          this.getList();
        });
      } else {
        this.$message.warn("请选择您要删除的内容");
      }
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
    rowSelect(record, selected, selectedRows) {
      console.log(record, selected, selectedRows);
    },
    rowSelectAll(selected, selectedRows, changeRows) {
      console.log(selected, selectedRows, changeRows);
    },
  },
};
</script>
<style scoped>
.btn-l1 {
  margin-left: 2%;
  /* border: 1px solid red;   */
}
</style>