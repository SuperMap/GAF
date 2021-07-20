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
        <a-form-item label="名称">
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
                  {
                    pattern: /^[^\u4e00-\u9fa5]+$/,
                    message: '不能使用中文字符'
                  }
                ],
              },
            ]"
            :disabled="operation === 3"
            placeholder="请输入名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="地址">
          <a-input
            v-decorator="[
              'serviceEndpoint',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入s3协议地址',
                  },
                  {
                    pattern: /^.{0,255}$/,
                    message: '长度不能超过255',
                  },
                ],
              },
            ]"
            :disabled="operation === 1"
            placeholder="请输入s3协议地址"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="bucket名称">
          <a-input
            v-decorator="[
              'bucketName',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入s3协议bucket名称',
                  },
                  {
                    pattern: /^.{0,255}$/,
                    message: '长度不能超过255',
                  },
                ],
              },
            ]"
            :disabled="operation === 1"
            placeholder="请输入s3协议bucket名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="用户名">
          <a-input
            v-decorator="[
              'accessKey',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入用户名',
                  },
                  {
                    pattern: /^.{0,255}$/,
                    message: '长度不能超过255',
                  },
                ],
              },
            ]"
            :disabled="operation === 1"
            placeholder="请输入用户名"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="密码">
          <a-input
            v-decorator="[
              'secretKey',
              {
                rules: [
                  {
                    required: true,
                    message: '请输入密码',
                  },
                  {
                    pattern: /^.{0,255}$/,
                    message: '长度不能超过255',
                  },
                ],
              },
            ]"
            :disabled="operation === 1"
            placeholder="请输入密码"
            auto-clear
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
      &nbsp;&nbsp;&nbsp;
      <a-button
        class="submit-gray"
        type="primary"
        :loading="loading"
        @click="submitForm"
      >
        确定
      </a-button>
      &nbsp;&nbsp;&nbsp;
      <a-button
        class="submit-gray"
        type="primary"
        :loading="loading2"
        @click="check"
      >
        验证
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
      loading2: false,
      loading: false
    };
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: "addOrEditForm" });
  },
  mounted() {
    const copyData = { ...this.editData };
    this.dataId = copyData.id;
    delete copyData.id;
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
        let url = `/storage/tenant-server-configs/`;
        const data = this.addOrEditForm.getFieldsValue();
        this.loading = true
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
        this.loading = false
        this.addOrEditForm.resetFields();
        this.$emit("submit");
      });
    },
    check() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault();
          return false;
        }
        let url = `/storage/validate-s3config/`;
        const data = this.addOrEditForm.getFieldsValue();
        this.loading2 = true
        const rst = await this.$axios.post(url, data);
        if (rst.data.isSuccessed) {
          this.$message.success("验证通过");
        } else {
          this.$message.error(`验证失败`);
        }
        this.loading2 = false
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
