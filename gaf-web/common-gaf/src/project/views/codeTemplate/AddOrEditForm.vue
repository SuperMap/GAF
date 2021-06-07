<template>
  <div>
    <template>
      <div style="margin: 7px 0; padding-left: 7px">
        <a-breadcrumb separator=">">
          <a-breadcrumb-item>
            <a class="text-bolder" @click="backToList">工程模板管理</a>
          </a-breadcrumb-item>
          <a-breadcrumb-item class="text-bolder">
            <span class="vertical-line">| </span>
            {{ title }}</a-breadcrumb-item
          >
        </a-breadcrumb>
      </div>
    </template>
    <a-form
      class="web-form"
      :form="addOrEditForm"
      layout="horizontal"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 10 }"
    >
      <a-form-item label="模板名称">
        <a-input
          v-decorator="[
            'templateName',
            {
              rules: [
                {
                  required: true,
                  message: '请输入模板名称',
                },
              ],
            },
          ]"
          placeholder="请输入模板名称"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="模板中文名称">
        <a-input
          v-decorator="[
            'templateNameCn',
            {
              rules: [
                {
                  required: true,
                  message: '请输入模板中文名称',
                },
              ],
            },
          ]"
          placeholder="请输入模板中文名称"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="模板类型">
        <a-select
          v-decorator="[
            'templateType',
            {
              rules: [
                {
                  required: true,
                  message: '请选择模板类型',
                },
              ],
              initialValue: '',
            },
          ]"
        >
          <a-select-option
            v-for="(templateType, index) in templateTypes"
            :key="index"
            :value="templateType.type"
            >{{ templateType.description }}</a-select-option
          >
        </a-select>
      </a-form-item>
      <a-form-item label="描述">
        <a-textarea
          v-decorator="['description']"
          laceholder="请输入描述"
          auto-size
        />
      </a-form-item>
      <a-form-item label="版本">
        <a-input
          v-decorator="[
            'version',
            {
              rules: [
                {
                  required: true,
                  message: '请输入版本',
                },
              ],
            },
          ]"
          placeholder="请输入版本"
          allow-clear
        />
      </a-form-item>
      <a-form-item label="模板">
        <a-input
          v-show="false"
          v-decorator="[
            'templatePath',
            {
              rules: [
                {
                  required: false,
                  message: '请上传模板',
                },
              ],
            },
          ]"
          :disabled="false"
          placeholder="模板地址"
        />
        <a-upload
          :multiple="false"
          :file-list="templateList"
          :show-upload-list="false"
          :before-upload="beforeUploadTemplate"
          accept=".zip"
        >
          <div class="upload-button">
            <svg class="arrow" width="40" height="40" viewBox="0 0 40 40">
              <!-- 进度 -->
              <circle
                cx="50%"
                cy="50%"
                r="19"
                fill="none"
                stroke="#ffffff"
                stroke-width="2"
              ></circle>
              <!-- 箭头 -->
              <!-- 左边的线 -->
              <polyline
                points="6,20 20,6 34,20"
                fill="none"
                stroke="#ffffff"
                stroke-width="2"
                class="arrow-top"
              ></polyline>
              <polyline
                points="8,20 18,30 30 12"
                stroke="#ffffff"
                stroke-width="2"
                fill="none"
                class="checkmark"
              ></polyline>
              <!-- 中间的线 -->
              <line
                x1="50%"
                y1="7"
                x2="50%"
                y2="34"
                stroke="#ffffff"
                stroke-width="2"
                class="middle-line"
              ></line>
            </svg>
            <div class="progress-bar"></div>
          </div>
          <!-- <a-button><a-icon type="upload" />上传模板</a-button> -->
        </a-upload>
        <a
          v-if="templatePath !== '' && templateList && templateList.length > 0"
          href="javascript:;"
          class="file-list-item"
        >
          <a-icon type="file-zip" />
          {{ templateList[0].name }}
        </a>
      </a-form-item>
      <div style="border: 1px soild blue">
        <a-button
          class="submit-gray"
          type="primary"
          style="margin-left: 43%"
          :loading="loading"
          @click="submitForm"
          >确定</a-button
        >
        <a-button class="cancel-modal" @click="backToList">取消</a-button>
      </div>
    </a-form>
  </div>
</template>

<script>
// @ts-ignore
import { token } from '@/project.config.json'
export default {
  props: {
    title: {
      type: String,
      default: '',
    },
    editData: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      templateList: [],
      dataId: '',
      templatePath: '',
      templateRalativePath: 'templates',
      headers: {
        authorization: `Bearer ${token}`,
      },
      templateTypes: [],
      loading: false,
    }
  },
  beforeMount() {
    this.addOrEditForm = this.$form.createForm(this, { name: 'addOrEditForm' })
  },
  mounted() {
    this.dataId = this.editData.projCodeTemplateId
    delete this.editData.projCodeTemplateId
    delete this.editData.createdTime
    delete this.editData.createdBy
    delete this.editData.updatedTime
    delete this.editData.updatedBy
    this.addOrEditForm.setFieldsValue({ ...this.editData })
    this.getAllTemplateType()
    if (this.dataId) {
      this.getTemplateById(this.dataId)
    }
  },
  methods: {
    submitForm() {
      this.addOrEditForm.validateFields(async (err) => {
        if (err) {
          event.preventDefault()
          return false
        }
        let url = '/proj/projcodetemplate'
        const data = this.addOrEditForm.getFieldsValue()
        this.loading = true
        if (this.dataId) {
          url = url + '/' + this.dataId
          const rst = await this.$axios.put(url, data)
          if (rst.data.successed) {
            this.$message.success('更新成功')
          } else {
            this.$message.error(`更新失败,原因:${rst.data.message}`)
          }
        } else {
          const rst = await this.$axios.post(url, data)
          if (rst.data.successed) {
            this.$message.success('添加成功')
          } else {
            this.$message.error(`添加失败,原因:${rst.data.message}`)
          }
        }
        this.loading = false
        this.addOrEditForm.resetFields()
        this.$emit('submit')
      })
    },
    // 从新增修改模态框返回列表
    backToList() {
      this.addOrEditForm.resetFields()
      this.$emit('back')
    },
    async getAllTemplateType() {
      const url = '/proj/projcodetemplate/templatetype'
      const rst = await this.$axios.$get(url)
      if (rst.successed) {
        this.templateTypes = rst.data
      } else {
        this.$message.error(`查询模板类型失败,原因:${rst.message}`)
      }
    },
    async getTemplateById(id) {
      const url = `/proj/projcodetemplate/${id}`
      const rst = await this.$axios.$get(url)
      if (rst.successed) {
        this.addOrEditForm.setFieldsValue({
          templateType: rst.data.templateType,
        })
      } else {
        this.$message.error(`查询模板信息失败,原因:${rst.message}`)
      }
    },
    // 上传模板
    async handleUploadtemplate() {
      // 获取上传按钮和进度条
      const uploadButton = document.querySelector('.upload-button')
      const progressBar = document.querySelector('.upload-button .progress-bar')
      // 进度条完成时的宽度
      const width = uploadButton.getBoundingClientRect().width
      // 是否上传完成
      let isUploaded = false
      uploadButton.classList.remove('uploaded')
      // 设置正在上传uploading样式
      uploadButton.classList.add('uploading')
      let start = null
      function grow(timestamp) {
        // 动画开始时已经过的时间戳
        if (!start) start = timestamp
        // 距离开始时已经过的时间戳
        const progress = timestamp - start
        // 按比例增加进度条宽度
        progressBar.style.width = `${Math.min(
          width * (progress / 100),
          width
        )}px`
        // 如果上传未完成，继续执行此函数，递归循环
        if (!isUploaded) window.requestAnimationFrame(grow)
      }

      // 开始执行grow函数
      window.requestAnimationFrame(grow)

      const formData = new FormData()
      formData.append('files', this.templateList[0])
      formData.append('path', this.templateRalativePath)
      formData.append('isPublic', true)
      const res = await this.$axios.$post(
        '/base/fileservice/file/upload2',
        formData
      )
      // console.log(res)
      if (res[0].successed) {
        this.templatePath = JSON.stringify(res[0].data)
        this.addOrEditForm.setFieldsValue({
          templatePath: this.templatePath,
        })
        this.$message.success('上传成功', 1)
      } else {
        this.$message.error('上传失败')
      }
      isUploaded = true
      uploadButton.classList.replace('uploading', 'uploaded')
    },
    beforeUploadTemplate(file) {
      this.templateList = []
      this.templateList.push(file)
      this.handleUploadtemplate()
      return false
    },
  },
}
</script>

<style scoped>
.ant-form-item {
  margin-bottom: 15px;
}

svg line,
svg polyline,
svg circle {
  display: inline-block;
  box-shadow: 1px 1px 1px rgba(0, 0, 0, 0.2);
  box-sizing: border-box;
  padding: 0;
  margin: 0;
  border: 1px solid #ccc;
  background-color: transparent;
  color: #262626;
}

.file-list-item {
  display: inline-block;
  width: 100%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  text-decoration: none;
  color: #919191;
  margin: 10px 0 0 0;
}

.file-list-item i {
  display: inline-block;
  padding-right: 5px;
  font-size: 20px;
}

button {
  width: 150px;
  vertical-align: middle;
  font-size: 12px;
  text-align: center;
}

/* 设置上传按钮样式 */
.upload-button {
  width: 110px;
  height: 50px;
  background-image: linear-gradient(160deg, #0093e9, #80d0c7);
  border-radius: 8px;
  cursor: pointer;
  box-shadow: 0 2px 10px rgba(0, 147, 233, 0.28);

  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

/* 对勾，默认隐藏 */
.upload-button .checkmark {
  opacity: 0;
}

/* 进度条样式 */
.upload-button .progress-bar {
  position: absolute;
  width: 0%;
  height: 2px;
  background-image: linear-gradient(45deg, #85ffbd, #fffb7d);
  bottom: 0;
  left: 0;
  border-radius: 4px;
}

/* 上传时，箭头中间线的动画，虚线上行 */
.upload-button.uploading .middle-line {
  stroke-dasharray: 4 3;
  animation: 0.8 linear dashLoop infinite;
}

/* 上传图标圆形闪烁 */
.upload-button.uploading circle {
  animation: 1.5s linear blink infinite;
}

/* 上传完成后箭头上半部分动画，画线擦除动画，最后透明度设置为0 */
.upload-button.uploaded .arrow-top {
  animation: 1s linear arrowTransform forwards;
}

/* 上传完成后对勾动画，画线效果 */
.upload-button.uploaded .checkmark {
  opacity: 1;
  stroke-dasharray: 100 100;
  stroke-dashoffset: 100;
  animation: 1s linear checkmarkTransform forwards 0.5s;
}

/* 上传完成后隐藏箭头中间的线 */
.upload-button.uploaded .middle-line {
  transition: 0.3s linear;
  opacity: 0;
}

/* 箭头中间线虚线上行动画 */
@keyframes dashLoop {
  from {
    stroke-dashoffset: 0;
  }
  to {
    stroke-dashoffset: 7;
  }
}

/* 圆形闪烁动画 */
@keyframes blink {
  from {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
  to {
    opacity: 1;
  }
}

/* 箭头变形动画 */
@keyframes arrowTransform {
  from {
    stroke-dasharray: 100 100;
    stroke-dashoffset: 0;
  }
  to {
    stroke-dasharray: 100 100;
    stroke-dashoffset: -100;
  }
}

/* 对勾动画 */
@keyframes checkmarkTransform {
  from {
    stroke-dasharray: 100 100;
    stroke-dashoffset: 100;
  }
  to {
    stroke-dasharray: 100 100;
    stroke-dashoffset: 0;
  }
}
</style>
