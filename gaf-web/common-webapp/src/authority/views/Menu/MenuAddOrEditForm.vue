<template>
  <div class="page-container">
    <div class="grid-container">
      <div class="drawer-header">
        <template>
          <a-breadcrumb separator=">" class="modal-line">
            <a-breadcrumb-item class="text-bolder">{{
              title
            }}</a-breadcrumb-item>
          </a-breadcrumb>
        </template>
      </div>
      <div class="drawer-content">
        <a-form
          :form="addOrEditForm"
          :label-col="{ span: 5 }"
          :wrapper-col="{ span: 18 }"
          layout="horizontal"
        >
          <a-form-item label="上级菜单">
            <a-tree-select
              v-decorator="['parentId']"
              allow-clear
              show-search
              :replaceFields="{
                children: 'children',
                title: 'name',
                key: 'key',
                value: 'key',
              }"
              :tree-data="menuTree"
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              tree-node-filter-prop="title"
              style="width: 100%"
              placeholder="请选择上级菜单"
            >
            </a-tree-select>
          </a-form-item>
          <a-form-item label="菜单名称">
            <a-input
              v-decorator="[
                'name',
                {
                  rules: [
                    {
                      required: true,
                      message: '菜单名不能为空',
                    },
                    {
                      max: 255,
                      message: '长度不能超过255个字符',
                    },
                  ],
                },
              ]"
              placeholder="请输入菜单名"
              allow-clear
            />
          </a-form-item>
          <a-form-item label="打开方式">
            <a-radio-group
              v-decorator="[
                'target',
                {
                  rules: [
                    {
                      required: true,
                      message: '请选择打开方式',
                    },
                  ],
                  initialValue: '0',
                },
              ]"
              @change="targetChange"
              button-style="solid"
            >
              <a-radio-button value="0"> 当前窗口 </a-radio-button>
              <a-radio-button value="1"> 新窗口打开 </a-radio-button>
            </a-radio-group>
          </a-form-item>
          <a-form-item v-show="target === '0'" label="路径">
            <a-input
              v-decorator="['path']"
              placeholder="请输入路径"
              allow-clear
            />
          </a-form-item>
          <a-form-item v-show="target === '1'" label="地址">
            <a-input v-decorator="['url']" placeholder="请输入地址" auto-size />
          </a-form-item>
          <a-form-item label="图标">
            <gaf-icon-picker
              v-decorator="['icon']"
              placeholder="请选择图标"
            ></gaf-icon-picker>
          </a-form-item>
          <a-form-item label="排序序号">
            <a-input-number
              :min="1"
              :precision="0"
              v-decorator="[
                'sortSn',
                {
                  rules: [
                    {
                      required: true,
                      message: '排序序号不能为空',
                    },
                  ],
                  initialValue: 1,
                },
              ]"
            />
          </a-form-item>
          <a-form-item label="描述">
            <a-textarea
              v-decorator="[
                'description',
                {
                  rules: [
                    {
                      max: 500,
                      message: '长度不能超过500个字符',
                    },
                  ],
                },
              ]"
              placeholder="请输入描述"
              auto-size
            />
          </a-form-item>
        </a-form>
      </div>
      <div class="drawer-footer">
        <div class="drawer-footer-div">
          <button @click="backToList" class="cancel-modal">取消</button>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <a-button
            @click="submitForm"
            type="primary"
            :loading="loading"
            class="submit-gray"
          >
            确定
          </a-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
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
    menuTree: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      dataId: undefined,
      loading: false,
      target: "0",
    };
  },
  computed: {},
  created() {
    if (this.editData.target) {
      this.target = this.editData.target;
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: "addOrEditForm" });
  },
  mounted() {
    const copyDate = { ...this.editData };
    this.dataId = copyDate.resourceMenuId;
    delete copyDate.resourceMenuId;
    delete copyDate.status;
    this.addOrEditForm.setFieldsValue({ ...copyDate });
  },
  methods: {
    targetChange(e) {
      this.target = e.target.value;
    },
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault();
          return false;
        }
        let url = `/authority/auth-resource-menus/`;
        const data = this.addOrEditForm.getFieldsValue();
        this.loading = true;
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
        this.loading = false;
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

<style lang="less" scoped>
.page-container {
  width: 100%;
  height: 100%;
}
</style>
