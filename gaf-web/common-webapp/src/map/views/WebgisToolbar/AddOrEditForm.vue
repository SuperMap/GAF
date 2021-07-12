<template>
  <div class="page-container">
    <!-- <template>
      <a-breadcrumb separator=">" class="modal-line">
        <a-breadcrumb-item class="tree-catalog" style="line-height: 15px"
          ><span class="vertical-line">| </span>{{ title }}</a-breadcrumb-item
        >
      </a-breadcrumb>
    </template> -->
    <div class="page-modal-box">
      <a-form
        :form="addOrEditForm"
        layout="horizontal"
        :label-col="{ span: 5 }"
        :wrapper-col="{ span: 10 }"
        hide-required-mark
      >
        <a-form-item label="工具条名称">
          <a-input
            v-decorator="[
              'name',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入工具条名称',
                  },
                  {
                    pattern: /^.{0,255}$/,
                    message: '长度不能超过255',
                  },
                ],
              },
            ]"
            placeholder="请输入工具条名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="工具条类型">
          <a-select
            v-decorator="['type']"
            :disabled="operation === 3"
            :default-value="{ key: '' }"
            allow-clear
            @change="handleChange"
          >
            <!-- <a-select-option :value="''">-全部-</a-select-option> -->
            <a-select-option :value="'1'">-基础类-</a-select-option>
            <a-select-option :value="'2'">-业务类-</a-select-option>
          </a-select>
        </a-form-item>
        <div>
          <a-transfer
            class="transfer-style"
            :render="(item) => item.title"
            :data-source="mockData"
            :target-keys="targetKeys"
            :filter-option="
              (inputValue, item) => item.title.indexOf(inputValue) !== -1
            "
            :show-select-all="false"
            @change="onChange"
          >
            <template
              slot="children"
              slot-scope="{
                props: { direction, filteredItems, selectedKeys },
                on: { itemSelectAll, itemSelect },
              }"
            >
              <a-table
                :row-selection="
                  getRowSelection({
                    selectedKeys,
                    itemSelectAll,
                    itemSelect,
                  })
                "
                :columns="direction === 'left' ? leftColumns : rightColumns"
                :data-source="filteredItems"
                size="small"
                :custom-row="
                  ({ key, disabled: itemDisabled }) => ({
                    on: {
                      click: () => {
                        itemSelect(key, !selectedKeys.includes(key));
                      },
                    },
                  })
                "
              >
                <template slot="optinTable" slot-scope="text, record">
                  <button
                    ref="ToolbarTransfer"
                    class="btn-edit"
                    @click.stop="handleUpdate(record)"
                  >
                    编辑
                  </button>
                </template>
              </a-table>
            </template>
          </a-transfer>
        </div>
        <!-- <div v-if="operation === 2 || operation === 3">
          <button
            style="margin: 0 5px 0 69%"
            class="btn-ok"
            @click="submitForm"
          >
            保存
          </button>
          <button class="btn-cancel" @click="backToList">取消</button>
        </div> -->
      </a-form>
    </div>
    <div>
      <gaf-drawer
        title="编辑按钮"
        :width="450"
        :visible="visible"
        @close="onClose"
        placement="right"
        :closable="false"
      >
        <a-form
          :form="form"
          layout="horizontal"
          hide-required-mark
          :label-col="{ span: 7 }"
          :wrapper-col="{ span: 15 }"
        >
          <a-row :gutter="24">
            <a-col :span="20">
              <a-form-item label="图标">
                <a-input v-decorator="['icon']" placeholder="请输入图标" />
              </a-form-item>
            </a-col>
            <a-col :span="20">
              <a-form-item label="顺序">
                <a-input-number
                  id="inputNumber"
                  v-model="value"
                  v-decorator="['sortSn']"
                  :min="1"
                />
              </a-form-item>
            </a-col>
            <a-col :span="20">
              <a-form-item label="参数">
                <a-input v-decorator="['params']" placeholder="请输入参数" />
              </a-form-item>
            </a-col>
            <a-col :span="20">
              <a-form-item label="其他方法">
                <a-input v-decorator="['actions']" placeholder="请输入方法" />
              </a-form-item>
            </a-col>
            <a-col :span="20">
              <a-form-item label="关联子工具条">
                <a-select v-decorator="['subToolbarId']">
                  <a-select-option
                    v-for="i in typeList"
                    :key="i.toolbarId"
                    :value="i.toolbarId"
                  >
                    {{ i.name }}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="20">
              <a-form-item label="更多属性">
                <a-input v-decorator="['moreProperties']" type="textarea" />
              </a-form-item>
            </a-col>
            <a-col :span="20">
              <a-form-item label="二次单击关闭">
                <a-switch
                  v-decorator="[
                    'toggle',
                    { valuePropName: 'checked', initialValue: false },
                  ]"
                  checked-children="开"
                  un-checked-children="关"
                />
              </a-form-item>
            </a-col>
            <a-col :span="20">
              <a-form-item label="关闭其他面板">
                <a-switch
                  v-decorator="[
                    'closeOtherPanel',
                    { valuePropName: 'checked', initialValue: false },
                  ]"
                  checked-children="开"
                  un-checked-children="关"
                  default-checked
                />
              </a-form-item>
            </a-col>
          </a-row>
        </a-form>
        <div
          :style="{
            position: 'absolute',
            right: 0,
            bottom: 0,
            width: '100%',
            borderTop: '1px solid #e9e9e9',
            padding: '10px 16px',
            background: '#fff',
            textAlign: 'center',
            zIndex: 1,
          }"
        >
          <button @click="onClose" class="cancel-modal">取消</button>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <button @click="saveClose" class="submit-gray">确定</button>
        </div>
      </gaf-drawer>
    </div>
  </div>
</template>
<script>
import moment from "moment";
import difference from "lodash/difference";
export default {
  props: {
    title: {
      type: String,
      default: "",
    },
    editData: {
      type: Object,
      default: null,
    },
    operation: {
      type: Number,
      default: 0,
    },
  },
  data() {
    return {
      typeList: [],
      value: undefined,
      type: "",
      searchedColumn: "name",
      searchText: "",
      targetKeys: [],
      addAttr: [],
      buttonIdKey: "",
      webgisToolbarButtons: [],
      toggleCheck: true,
      closeCheck: true,
      dataId: "",
      mockData: [],
      form: this.$form.createForm(this),
      visible: false,
      leftColumns: [
        {
          dataIndex: "title",
          title: "可选按钮列表",
        },
      ],
      rightColumns: [
        {
          dataIndex: "title",
          title: "已选按钮列表",
        },
        {
          dataIndex: "description",
          title: "操作",
          scopedSlots: { customRender: "optinTable" },
        },
      ],
    };
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: "addOrEditForm" });
  },
  created() {
    this.getList();
  },
  mounted() {
    const copyData = { ...this.editData };
    this.dataId = copyData.toolbarId;
    const { name, type } = copyData;
    this.addOrEditForm.setFieldsValue({ name, type });
    if (this.operation === 3) {
      this.type = this.editData.type;
    }
    this.nextEditor();
  },
  methods: {
    // // 二次关闭 开关
    // toggleChange(checked) {
    //   this.toggleCheck = `${checked}`
    // },
    // // 关闭其他窗口
    // closeChange(checked) {
    //   this.closeCheck = `${checked}`
    // },
    // 顺序 切换
    /* numChange() {
    }, */
    // 转换箱
    onChange(nextTargetKeys) {
      this.targetKeys = nextTargetKeys;
    },
    getRowSelection({ selectedKeys, itemSelectAll, itemSelect }) {
      return {
        onSelectAll(selected, selectedRows) {
          const treeSelectedKeys = selectedRows
            .filter((item) => !item.disabled)
            .map(({ key }) => key);
          const diffKeys = selected
            ? difference(treeSelectedKeys, selectedKeys)
            : difference(selectedKeys, treeSelectedKeys);
          itemSelectAll(diffKeys, selected);
        },
        onSelect({ key }, selected) {
          itemSelect(key, selected);
        },
        selectedRowKeys: selectedKeys,
      };
    },
    // 下一步  新增
    async handleChange(value) {
      this.type = value;
      let url = `/map/webgis-buttons?pageNum=0&pageSize=0`;
      if (value !== "") {
        url = url + `&type=` + value;
      }
      const res = await this.$axios.$get(url);
      const btnList = res.data.pageList;
      this.mockData = [];
      for (let i = 0; i < btnList.length; i++) {
        this.mockData.push({
          key: btnList[i].buttonId,
          title: btnList[i].name,
        });
      }
    },
    // 下一步  编辑
    async nextEditor() {
      this.getTransfer();
      const existData = this.editData.toolbarId;
      const url = `/map/webgis-toolbars/do/` + existData;
      const res = await this.$axios.get(url);
      const data = res.data.data;
      if (
        data &&
        data.webgisToolbarButtonDos &&
        data.webgisToolbarButtonDos.length > 0
      ) {
        this.targetKeys = data.webgisToolbarButtonDos.map((item) => {
          return item.buttonId;
        });
        this.webgisToolbarButtons.push(...data.webgisToolbarButtonDos);
      }
    },
    async getTransfer() {
      let url = `/map/webgis-buttons?pageNum=0&pageSize=0`;
      if (this.type !== "") {
        url = url + `&type=` + this.type;
      }
      const res = await this.$axios.$get(url);
      const btnList = res.data.pageList;
      this.mockData = [];
      for (let i = 0; i < btnList.length; i++) {
        this.mockData.push({
          key: btnList[i].buttonId,
          title: btnList[i].name,
        });
      }
    },
    // 编辑转换框数据
    async handleUpdate(data) {
      this.visible = true;
      this.buttonIdKey = data.key;
      const hit = this.webgisToolbarButtons.filter(
        (item) => item.buttonId === data.key
      );
      if (hit.length > 0) {
        this.$nextTick(function () {
          const {
            actions,
            subToolbarId,
            sortSn,
            closeOtherPanel,
            icon,
            moreProperties,
            params,
            toggle,
          } = hit[0];
          this.form.setFieldsValue({
            actions,
            subToolbarId,
            sortSn,
            closeOtherPanel,
            icon,
            moreProperties,
            params,
            toggle,
          });
        });
      } else {
        const url = `/map/webgis-buttons?pageNum=0&pageSize=0`;
        const res = await this.$axios.$get(url);
        const btnList = res.data.pageList;
        this.buttonIdKey = data.key;
        const hit = btnList.filter((item) => item.buttonId === data.key);
        this.$nextTick(function () {
          const {
            actions,
            subToolbarId,
            closeOtherPanel,
            icon,
            moreProperties,
            params,
            toggle,
            sortSn,
          } = hit[0];
          this.form.setFieldsValue({
            actions,
            subToolbarId,
            closeOtherPanel,
            icon,
            moreProperties,
            params,
            toggle,
            sortSn,
          });
        });
      }
    },
    // 抽屉 关闭
    onClose() {
      this.visible = false;
      this.form.resetFields();
    },
    // 抽屉 确定
    saveClose() {
      this.form.validateFields((valid) => {
        if (!valid) {
          const addAttr = this.form.getFieldsValue();
          addAttr.buttonId = this.buttonIdKey;
          for (let i = this.webgisToolbarButtons.length - 1; i >= 0; i--) {
            if (this.webgisToolbarButtons[i].buttonId === addAttr.buttonId) {
              this.webgisToolbarButtons.splice(i, 1);
            }
          }
          this.webgisToolbarButtons.push(addAttr);
          this.visible = false;
          this.form.resetFields();
        } else {
          return false;
        }
      });
    },
    moment,
    // 工具条名称获取
    async getList() {
      const url = `/map/webgis-toolbars?pageSize`;
      const rst = await this.$axios.get(url);
      if (rst.data.isSuccessed) {
        this.typeList = rst.data.data.pageList;
        const emptArr = {};
        emptArr.name = "无";
        emptArr.toolbarId = "";
        this.typeList.unshift(emptArr);
      }
    },
    // 新增工具条 提交表单 保存
    submitForm() {
      if (this.operation !== 3) {
        this.addOrEditForm.validateFields(async (err) => {
          if (err) {
            event.preventDefault();
            return false;
          }
          const url = `/map/webgis-buttons?pageNum=0&pageSize=0`;
          const res = await this.$axios.$get(url);
          const btnList = res.data.pageList;
          const notEditButtons = btnList
            .filter((item) => {
              for (let i = 0; i < this.targetKeys.length; i++) {
                if (item.buttonId === this.targetKeys[i]) {
                  return true;
                }
              }
              return false;
            }, this)
            .map((item) => {
              const { buttonId, icon, sortSn } = item;
              return { buttonId, icon, sortSn };
            });
          const data = this.addOrEditForm.getFieldsValue();
          for (let j = 0; j < this.webgisToolbarButtons.length; j++) {
            for (let i = 0; i < notEditButtons.length; i++) {
              if (
                notEditButtons[i].buttonId ===
                this.webgisToolbarButtons[j].buttonId
              ) {
                notEditButtons.splice(i, 1);
                notEditButtons.push(this.webgisToolbarButtons[j]);
                break
              }
            }
          }
          data.webgisToolbarButtons = notEditButtons;
          if (data.webgisToolbarButtons.length === 0) {
            this.$message.error(`未选择按钮`);
            event.preventDefault();
            return false;
          }
          const urlPost = `/map/webgis-toolbars`;
          const rst = await this.$axios.post(urlPost, data);
          if (rst.data.isSuccessed) {
            this.$message.success("添加成功");
          } else {
            this.$message.error(`添加失败,原因:${rst.data.message}`);
          }
          this.addOrEditForm.resetFields();
          this.$emit("submit");
        });
      } else {
        this.addOrEditForm.validateFields(async (err) => {
          if (err) {
            event.preventDefault();
            return false;
          }
          const url = `/map/webgis-buttons?pageNum=0&pageSize=0`;
          const res = await this.$axios.$get(url);
          const btnList = res.data.pageList;
          const notEditButtons = btnList
            .filter((item) => {
              for (let i = 0; i < this.targetKeys.length; i++) {
                if (item.buttonId === this.targetKeys[i]) {
                  return true;
                }
              }
              return false;
            }, this)
            .map((item) => {
              const { buttonId, icon } = item;
              return { buttonId, icon };
            });
          const data = this.addOrEditForm.getFieldsValue();
          for (let j = 0; j < this.webgisToolbarButtons.length; j++) {
            for (let i = 0; i < notEditButtons.length; i++) {
              if (
                notEditButtons[i].buttonId ===
                this.webgisToolbarButtons[j].buttonId
              ) {
                notEditButtons.splice(i, 1);
                notEditButtons.push(this.webgisToolbarButtons[j]);
                break
              }
            }
          }
          data.webgisToolbarButtons = notEditButtons;
          let urlPost = `/map/webgis-toolbars/`;
          if (this.dataId) {
            urlPost = urlPost + this.dataId;
            const rst = await this.$axios.put(urlPost, data);
            if (rst.data.isSuccessed) {
              this.$message.success("更新成功");
            } else {
              this.$message.error(`更新失败,原因:${rst.data.message}`);
            }
          } else {
            const rst = await this.$axios.post(urlPost, data);
            if (rst.data.isSuccessed) {
              this.$message.success("添加成功");
            } else {
              this.$message.error(`添加失败,原因:${rst.data.message}`);
            }
          }
          this.addOrEditForm.resetFields();
          this.$emit("submit");
        });
      }
    },
    // 从新增修改模态框返回列表
    backToList() {
      this.addOrEditForm.resetFields();
      this.$emit("back");
    },
  },
};
</script>
<style scoped>
.ant-transfer-list {
  width: 300px;
  height: 260px;
  overflow: auto;
}
.ant-table-small {
  height: 232px;
}
.transfer-style {
  padding: 30px 0 30px 0;
}
</style>
