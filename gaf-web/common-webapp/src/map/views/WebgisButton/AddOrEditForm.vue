<template>
  <div class="page-container">
    <div class="grid-container">
    <div class="drawer-header">
    <template>
      <a-breadcrumb separator=">" class="modal-line">
        <a-breadcrumb-item class="text-bolder"
          >{{ title }}</a-breadcrumb-item
        >
      </a-breadcrumb>
    </template>
    </div>
    <div class="drawer-content">
      <a-form
        :form="addOrEditForm"
        layout="horizontal"
        :label-col="{ span: 4 }"
        :wrapper-col="{ span: 19 }"
        hide-required-mark
      >
        <a-form-item label="按钮名称">
          <a-input
            v-decorator="[
              'name',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入名称',
                  },
                  {
                    pattern: /^.{0,255}$/,
                    message: '长度不能超过255',
                  },
                ],
              },
            ]"
            :disabled="operation === 1"
            placeholder="请输入名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="按钮类别">
          <a-select
            v-decorator="['type']"
            :disabled="operation === 1 || operation === 3"
            placeholder="请选择类别"
            allow-clear
          >
            <a-select-option value="1">-基础类-</a-select-option>
            <a-select-option value="2">-业务类-</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item label="方法">
          <a-input
            v-decorator="[
              'method',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入方法',
                  },
                  {
                    pattern: /^.{0,255}$/,
                    message: '长度不能超过255',
                  },
                ],
              },
            ]"
            :disabled="operation === 1"
            placeholder="请输入方法"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="图标">
          <a-input
            v-decorator="[
              'icon',
              {
                rules: [
                  {
                    pattern: /^.{0,255}$/,
                    message: '长度不能超过255',
                  },
                ],
              },
            ]"
            :disabled="operation === 1"
            placeholder="请输入图标"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="描述">
          <a-textarea
            v-decorator="[
              'description',
              {
                rules: [
                  {
                    pattern: /^.{0,500}$/,
                    message: '长度不能超过500',
                  },
                ],
              },
            ]"
            :disabled="operation === 1"
            placeholder="请输入描述"
            auto-size
          />
        </a-form-item>
        <div v-if="operation === 1">
          <a-form-item label="创建时间">
            <a-date-picker
              v-decorator="['createdTime']"
              show-time
              placeholder="请选择创建时间"
              disabled
            />
          </a-form-item>
          <a-form-item label="创建人">
            <a-input
              v-decorator="['createdBy']"
              placeholder="请输入创建人"
              allow-clear
              disabled
            />
          </a-form-item>
          <a-form-item label="修改时间">
            <a-date-picker
              v-decorator="['updatedTime']"
              show-time
              placeholder="请选择修改时间"
              disabled
            />
          </a-form-item>
          <a-form-item label="修改人">
            <a-input
              v-decorator="['updatedBy']"
              placeholder="请输入修改人"
              allow-clear
              disabled
            />
          </a-form-item>
        </div>
      </a-form>
    </div>
    <div class="drawer-footer">
      <div class="drawer-footer-div">
      <button class="cancel-modal" @click="backToList">
        {{ this.operation === 1 ? "返回" : "取消" }}
      </button>
      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <a-button
        class="submit-gray"
        type="primary"
        :loading="loading"
        @click="submitForm"
      >
        确定
      </a-button>
      </div>
    </div>
    </div>
  </div>
</template>

<script>
import "~/assets/css/common.css";
import moment from "moment";
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
      dataId: "",
    };
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: "addOrEditForm" });
  },
  mounted() {
    const copyData = { ...this.editData };
    this.dataId = copyData.buttonId;
    delete copyData.buttonId;
    delete copyData.status;
    if (this.operation !== 1) {
      delete copyData.createdBy;
      delete copyData.updatedBy;
      delete copyData.createdTime;
      delete copyData.createdBy;
    }
    if (copyData.createdTime)
      copyData.createdTime = moment(new Date(copyData.createdTime));
    if (copyData.updatedTime)
      copyData.updatedTime = moment(new Date(copyData.updatedTime));
    this.addOrEditForm.setFieldsValue({ ...copyData });
  },
  methods: {
    moment,
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault();
          return false;
        }
        let url = `/map/webgis-buttons/`;
        const data = this.addOrEditForm.getFieldsValue();
        if (this.dataId) {
          url = url + this.dataId;
          const rst = await this.$axios.put(url, data);
          if (rst.data.isSuccessed) {
            this.$message.success("更新成功");
          } else {
            this.$message.error(`更新失败,原因:${rst.data.message}`);
          }
        } else {
          const rst = await this.$axios.post(url, data);
          if (rst.data.isSuccessed) {
            this.$message.success("添加成功");
          } else {
            this.$message.error(`添加失败,原因:${rst.data.message}`);
          }
        }
        this.addOrEditForm.resetFields();
        this.$emit("submit");
      });
    },
    // 从新增修改模态框返回列表
    backToList() {
      this.addOrEditForm.resetFields();
      this.$emit("back");
    },
  },
};
</script>
