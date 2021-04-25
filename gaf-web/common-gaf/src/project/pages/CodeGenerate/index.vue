<template>
  <div class="app-container">
    <div class="page-single">
      <div style="padding: 25px; height: 100%">
        <div class="header" style="height: 16%">
          <!-- 步骤条 -->
          <div style="margin: auto; width: 100%; padding-bottom: 25px">
            <a-steps type="default" :current="step">
              <a-step
                title="选工程与填写数据源"
                description="若工程已绑定数据源，则选择后自动填充数据源"
              >
              </a-step>
              <a-step title="选表" description="选择一个数据库表"> </a-step>
              <a-step
                title="配置模板参数"
                description="选择模板,并填写模板参数"
              >
              </a-step>
              <a-step
                title="配置字段"
                description="配置类型,前端页面列表显示、表单显示等"
              >
              </a-step>
            </a-steps>
          </div>
        </div>
        <!-- 配置内容 -->
        <div style="height: 75%">
          <!-- 初始化界面，选择或输入工程数据源信息 -->
          <div v-show="step === 0" id="ds">
            <div id="app">
              <div>
                <div style="width: 40%; margin: auto">
                  <a-form
                    :form="dataSourceForm"
                    :label-col="{ span: 6 }"
                    :wrapper-col="{ span: 15 }"
                  >
                    <a-form-item label="工程：">
                      <a-select
                        v-decorator="['projectName']"
                        auto-focus
                        show-search
                        option-filter-prop="children"
                        placeholder="选择工程，若工程配置了数据源，下面表单会自动填写"
                        allow-clear
                        @focus="projectFocus"
                        @change="projectChange"
                      >
                        <a-select-option
                          v-for="item in projectMetadataInfos"
                          :key="item.projId"
                          :value="item.projId"
                        >
                          {{ item.projName }}
                        </a-select-option>
                      </a-select>
                    </a-form-item>
                    <a-form-item label="数据库类型：">
                      <a-select
                        v-decorator="[
                          'datasourceType',
                          {
                            rules: [
                              {
                                required: true,
                                message: '请选择数据库类型！',
                              },
                            ],
                          },
                        ]"
                        placeholder="请选择数据库类型"
                        @change="SQLTypeChange"
                      >
                        <a-select-option
                          v-for="t in SQLType"
                          :key="t"
                          :value="t"
                        >
                          {{ t }}
                        </a-select-option>
                      </a-select>
                    </a-form-item>
                    <a-form-item label="主机地址：">
                      <a-input
                        v-decorator="[
                          'host',
                          {
                            rules: [
                              {
                                required: true,
                                message: '请输入主机地址！',
                              },
                            ],
                          },
                        ]"
                        placeholder="请输入主机地址"
                      />
                    </a-form-item>
                    <a-form-item label="端口号：">
                      <a-input
                        v-decorator="[
                          'port',
                          {
                            rules: [
                              {
                                required: true,
                                message: '请输入端口号！',
                              },
                              {
                                pattern: /^([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/,
                                message: '端口号格式不正确！',
                              },
                            ],
                          },
                        ]"
                        placeholder="请输入端口号"
                      />
                    </a-form-item>
                    <a-form-item
                      v-if="!databaseOrServiceNameShow"
                      label="数据库名："
                    >
                      <a-input
                        v-decorator="[
                          'databaseOrServiceName',
                          {
                            rules: [
                              {
                                required: true,
                                message: '请输入数据库名！',
                              },
                            ],
                          },
                        ]"
                        placeholder="请输入数据库名"
                      />
                    </a-form-item>
                    <a-form-item label="用户名：">
                      <a-input
                        v-decorator="[
                          'username',
                          {
                            rules: [
                              {
                                required: true,
                                message: '请输入用户名！',
                              },
                              {
                                pattern: /^[a-zA-Z0-9_-]{4,16}$/,
                                message: '用户名为4-16位英数字！',
                              },
                            ],
                          },
                        ]"
                        placeholder="请输入用户名"
                      />
                    </a-form-item>
                    <a-form-item label="密码：">
                      <a-input-password
                        v-decorator="[
                          'password',
                          {
                            rules: [
                              { required: true, message: '请输入密码！' },
                            ],
                          },
                        ]"
                        placeholder="请输入密码"
                      />
                    </a-form-item>
                    <a-form-item label="模式：">
                      <a-input
                        v-decorator="['schema', { initialValue: 'public' }]"
                        placeholder="默认public"
                        :disabled="!schemaShow"
                      />
                    </a-form-item>
                    <a-form-item
                      v-show="databaseOrServiceNameShow"
                      label="服务名："
                    >
                      <a-input
                        v-decorator="[
                          'databaseOrServiceName',
                          {
                            rules: [
                              { required: true, message: '请输入服务名！' },
                            ],
                          },
                        ]"
                        placeholder="请输入服务名"
                        :disabled="!databaseOrServiceNameShow"
                      />
                    </a-form-item>
                    <div class="datasource-btns">
                      <button
                        class="cancel-modal"
                        style="margin: 10px 0 0 73%"
                        @click="datasourceReset"
                      >
                        <span><a-icon type="redo" />重置</span>
                      </button>
                    </div>
                  </a-form>
                </div>
              </div>
            </div>
          </div>
          <!-- 数据库表列表展示 -->
          <div v-show="step === 1" id="generator">
            <gaf-table-layout>
              <template #filter>
                <div class="search-position" style="margin-bottom: 15px">
                  <a-input-search
                    placeholder="请输入表名按回车查询"
                    size="large"
                    @search="onSearch"
                  >
                    <a-button slot="enterButton" class="btn-search">
                      搜索
                    </a-button>
                  </a-input-search>
                </div>
              </template>
              <!-- 列表区域 -->
              <gaf-table-with-page
                :show-x-h="false"
                :pagination="pagination"
                :data-source="tableList"
                :loading="tableLoading"
                :columns="tableColumns"
                :row-selection="{
                  type: 'radio',
                  columnTitle: '选择',
                  fixed: true,
                  onChange: onChange,
                  selectedRowKeys: selectedRowKeys,
                }"
                row-key="tableName"
                bordered
                size="small"
                @change="tableChange"
                @showSizeChange="tableSizeChange"
              >
              </gaf-table-with-page>
            </gaf-table-layout>
          </div>
          <!-- 单表前后端代码自动生成用户设置界面 -->
          <div v-show="step === 2" id="config">
            <div style="width: 40%; margin: auto">
              <a-form
                :form="configForm"
                :label-col="{ span: 6 }"
                :wrapper-col="{ span: 15 }"
              >
                <div>
                  <a-form-item label="模板：">
                    <a-select
                      v-decorator="[
                        'templateName',
                        {
                          rules: [
                            {
                              required: true,
                              message: '请选择模板',
                              trigger: 'blur',
                            },
                          ],
                          initialValue: `${template}`,
                        },
                      ]"
                      show-search
                      placeholder="请选择模板"
                      @change="templateChange"
                    >
                      <a-select-option
                        v-for="(item, index) in templateList"
                        :key="index"
                        :value="item ? item.projCodeTemplateId : index"
                      >
                        {{ item ? item.templateNameCn : '' }}
                      </a-select-option>
                    </a-select>
                  </a-form-item>
                  <a-form-item label="ArtifactID：">
                    <a-input
                      v-decorator="[
                        'artifactid',
                        {
                          rules: [
                            {
                              required: true,
                              message: '请填写ArtifactID！',
                              trigger: 'blur',
                            },
                          ],
                        },
                      ]"
                      placeholder="应该与工程的artifactId一致"
                    >
                    </a-input>
                  </a-form-item>
                  <a-form-item label="包路径：">
                    <a-input
                      v-decorator="[
                        'package',
                        { initialValue: `${packageName}` },
                      ]"
                      placeholder="应该与工程的包路径一致"
                    >
                    </a-input>
                  </a-form-item>
                  <a-form-item label="作者：">
                    <a-input
                      v-decorator="['author']"
                      placeholder="请输入作者（选填）"
                    >
                    </a-input>
                  </a-form-item>
                  <a-form-item label="表名称：">
                    <a-input
                      v-decorator="[
                        'tableName',
                        {
                          rules: [{ required: true, message: '请输入表名称' }],
                          initialValue: `${tableName}`,
                        },
                      ]"
                      placeholder="请输入表名称"
                      disabled
                    >
                    </a-input>
                  </a-form-item>
                  <a-form-item label="表描述：">
                    <a-input
                      v-decorator="['remarks']"
                      placeholder="请输入表描述（选填）"
                    >
                    </a-input>
                  </a-form-item>
                  <a-form-item label="实体类名称：">
                    <a-input
                      v-decorator="[
                        'tableUpperCamelName',
                        { initialValue: `${tableUpperCamelName}` },
                      ]"
                      placeholder="请输入实体类名称（选填）"
                    >
                    </a-input>
                  </a-form-item>
                  <a-form-item label="备注：">
                    <a-input
                      v-decorator="['comment']"
                      placeholder="请输入备注信息（选填）"
                    >
                    </a-input>
                  </a-form-item>
                </div>
              </a-form>
            </div>
          </div>
          <div v-if="step === 3">
            <gaf-table-with-page
              :pagination="fieldPagination"
              :data-source="tableMetadataInfos.fieldMetadataInfos"
              :columns="fieldColumns"
              row-key="fieldName"
              bordered
              style="width: 100%"
              size="small"
              @change="fieldTableChange"
              @showSizeChange="fieldSizeChange"
            >
              <template slot="remarks" slot-scope="text, record">
                <a-input
                  :default-value="record.remarks"
                  size="small"
                  style="width: 120px"
                  placeholder="请输入列表字段名称"
                  @blur="remarksChange(record, $event)"
                />
              </template>
              <template slot="javaType" slot-scope="text, record">
                <a-dropdown>
                  <a-input
                    size="small"
                    style="width: 80px"
                    :default-value="record.javaType"
                    :value="record.javaType"
                    @blur="javaTypeChange(record, $event)"
                    @keyup.enter.native="javaTypeChange(record, $event)"
                  >
                  </a-input>
                  <a-menu slot="overlay" @click="onClick(record, $event)">
                    <a-menu-item v-for="item in javaTypeList" :key="item.key">
                      {{ item.value }}
                    </a-menu-item>
                  </a-menu>
                </a-dropdown>
              </template>
              <!-- 最小值设置 -->
              <template
                v-if="minVisible"
                slot="minValue"
                slot-scope="text, record"
              >
                <a-tooltip>
                  <template
                    v-if="
                      record.isPrimaryKey ||
                      !record.isDisplayInForm ||
                      record.javaType === 'String' ||
                      record.javaType === 'Date' ||
                      record.javaType === 'Boolean' ||
                      inputErrorTip !== ''
                    "
                    slot="title"
                  >
                    {{
                      inputErrorTip === ''
                        ? '仅支持数值型字段校验'
                        : inputErrorTip
                    }}
                  </template>
                  <a-input
                    v-if="record.rules.min.editable"
                    v-focus
                    :default-value="record.rules.min.value"
                    :disabled="
                      record.isPrimaryKey ||
                      !record.isDisplayInForm ||
                      record.javaType === 'Date' ||
                      record.javaType === 'Boolean' ||
                      record.javaType === 'String'
                    "
                    size="small"
                    placeholder="最小值"
                    style="width: 60px"
                    @blur="minValueChange(record, $event)"
                    @keyup.enter.native="minValueChange(record, $event)"
                    @focus="minFocus(record, $event)"
                  />
                  <template v-if="!record.rules.min.editable">
                    <a
                      :disabled="
                        record.isPrimaryKey ||
                        !record.isDisplayInForm ||
                        record.javaType === 'String' ||
                        record.javaType === 'Date' ||
                        record.javaType === 'Boolean'
                      "
                      href="javascript:;"
                      @click="minValueClick(record)"
                    >
                      {{ record.rules.min.value }}
                    </a>
                  </template>
                </a-tooltip>
              </template>
              <!-- 最大值设置 -->
              <template
                v-if="maxVisible"
                slot="maxValue"
                slot-scope="text, record"
              >
                <a-tooltip>
                  <template
                    v-if="
                      record.isPrimaryKey ||
                      !record.isDisplayInForm ||
                      record.javaType === 'String' ||
                      record.javaType === 'Date' ||
                      record.javaType === 'Boolean' ||
                      inputErrorTip !== ''
                    "
                    slot="title"
                  >
                    {{
                      inputErrorTip === ''
                        ? '仅支持数值型字段校验'
                        : inputErrorTip
                    }}
                  </template>
                  <a-input
                    v-if="record.rules.max.editable"
                    v-focus
                    :default-value="record.rules.max.value"
                    :disabled="
                      record.isPrimaryKey ||
                      !record.isDisplayInForm ||
                      record.javaType === 'String' ||
                      record.javaType === 'Date' ||
                      record.javaType === 'Boolean'
                    "
                    size="small"
                    placeholder="最大值"
                    style="width: 60px"
                    @blur="maxValueChange(record, $event)"
                    @keyup.enter.native="maxValueChange(record, $event)"
                    @focus="maxFocus(record, $event)"
                  />
                  <template v-if="!record.rules.max.editable">
                    <a
                      :disabled="
                        record.isPrimaryKey ||
                        !record.isDisplayInForm ||
                        record.javaType === 'String' ||
                        record.javaType === 'Date' ||
                        record.javaType === 'Boolean'
                      "
                      href="javascript:;"
                      @click="maxValueClick(record)"
                    >
                      {{ record.rules.max.value }}
                    </a>
                  </template>
                </a-tooltip>
              </template>
              <!-- 字段长度设置 -->
              <template
                v-if="lenVisible"
                slot="columnSize"
                slot-scope="text, record"
              >
                <a-tooltip>
                  <template
                    v-if="
                      record.isPrimaryKey ||
                      !record.isDisplayInForm ||
                      record.javaType === 'Date' ||
                      record.javaType === 'Boolean' ||
                      inputErrorTip !== ''
                    "
                    slot="title"
                  >
                    {{
                      inputErrorTip === ''
                        ? '支持文本数值型字段校验'
                        : inputErrorTip
                    }}
                  </template>
                  <a-input
                    v-if="record.rules.len.editable"
                    v-focus
                    :default-value="record.rules.len.value"
                    :disabled="
                      record.isPrimaryKey ||
                      !record.isDisplayInForm ||
                      record.javaType === 'Date' ||
                      record.javaType === 'Boolean'
                    "
                    size="small"
                    placeholder="字段长度"
                    style="width: 80px"
                    @blur="lenValueChange(record, $event)"
                    @keyup.enter.native="lenValueChange(record, $event)"
                    @focus="lenFocus(record, $event)"
                  />
                  <template v-if="!record.rules.len.editable">
                    <a
                      :disabled="
                        record.isPrimaryKey ||
                        !record.isDisplayInForm ||
                        record.javaType === 'Date' ||
                        record.javaType === 'Boolean'
                      "
                      href="javascript:;"
                      @click="lenValueClick(record)"
                    >
                      {{ record.rules.len.value }}
                    </a>
                  </template>
                </a-tooltip>
              </template>
              <!-- 枚举类型设置 -->
              <template
                v-if="enumVisible"
                slot="enumValue"
                slot-scope="text, record"
              >
                <a-tooltip>
                  <template
                    v-if="
                      record.isPrimaryKey ||
                      !record.isDisplayInForm ||
                      record.javaType !== 'String' ||
                      inputErrorTip !== ''
                    "
                    slot="title"
                  >
                    {{
                      inputErrorTip === '' ? '仅支持文本型字段' : inputErrorTip
                    }}
                  </template>
                  <a-input
                    v-if="record.rules.enum.editable"
                    v-focus
                    :default-value="record.rules.enum.value"
                    :disabled="
                      record.isPrimaryKey ||
                      !record.isDisplayInForm ||
                      record.javaType !== 'String'
                    "
                    size="small"
                    placeholder="枚举项用逗号隔开"
                    style="width: 150px"
                    @blur="enumValueChange(record, $event)"
                    @keyup.enter.native="enumValueChange(record, $event)"
                    @focus="enumFocus(record, $event)"
                  />
                  <template v-if="!record.rules.enum.editable">
                    <a
                      :disabled="
                        record.isPrimaryKey ||
                        !record.isDisplayInForm ||
                        record.javaType !== 'String'
                      "
                      href="javascript:;"
                      @click="enumValueClick(record)"
                    >
                      {{ record.rules.enum.value }}
                    </a>
                  </template>
                </a-tooltip>
              </template>
              <!-- 必填字段设置 -->
              <template slot="isNullable" slot-scope="text, record">
                <a-checkbox
                  :default-checked="!record.isNullable"
                  :disabled="
                    record.isPrimaryKey ||
                    !record.isDisplayInForm ||
                    !record.canNullableEdit
                  "
                  @change="record.isNullable = !record.isNullable"
                ></a-checkbox>
              </template>
              <!-- 主键设置 -->
              <template slot="isPrimaryKey" slot-scope="text, record">
                <a-tooltip>
                  <template v-if="record.isPrimaryKey" slot="title">
                    主键默认不展示
                  </template>
                  <a-checkbox
                    :default-checked="record.isPrimaryKey"
                    :disabled="
                      !record.isPrimaryKey && tableMetadataInfos.pkName !== null
                    "
                    @change="primaryKeyChange(record, $event)"
                  ></a-checkbox>
                </a-tooltip>
              </template>
              <!-- 前端生成页面添加修改字段表单类型设置 -->
              <template slot="htmlType" slot-scope="text, record">
                <a-select
                  :value="record.htmlType"
                  :disabled="record.isPrimaryKey || !record.isDisplayInForm"
                  size="small"
                  placeholder="请选择表单类型"
                  style="width: 127px"
                  @change="htmlChange(record, $event)"
                >
                  <a-select-option
                    v-for="item in formtypeList"
                    :key="item"
                    :value="item"
                  >
                    {{ item }}
                  </a-select-option>
                </a-select>
              </template>
              <!-- 查询字段设置 -->
              <template slot="isSelect" slot-scope="text, record">
                <a-tooltip>
                  <template
                    v-if="record.isPrimaryKey || record.javaType !== 'String'"
                    slot="title"
                  >
                    仅支持文本查询
                  </template>
                  <a-checkbox
                    :default-checked="record.isSelect"
                    :disabled="
                      record.isPrimaryKey ||
                      record.javaType !== 'String' ||
                      !record.isDisplayInTable
                    "
                    @change="record.isSelect = !record.isSelect"
                  ></a-checkbox>
                </a-tooltip>
              </template>
              <!-- 排序字段设置 -->
              <template slot="isSort" slot-scope="text, record">
                <a-checkbox
                  :default-checked="record.isSort"
                  :disabled="record.isPrimaryKey || !record.isDisplayInTable"
                  @change="record.isSort = !record.isSort"
                ></a-checkbox>
              </template>
              <template slot="isDisplayInTable" slot-scope="text, record">
                <a-checkbox
                  :default-checked="
                    record.isDisplayInTable &&
                    !disabledTableList.includes(record.fieldLowCamelName)
                  "
                  :disabled="
                    record.isPrimaryKey ||
                    disabledTableList.includes(record.fieldLowCamelName)
                  "
                  @change="
                    () => {
                      record.isDisplayInTable = !record.isDisplayInTable
                      $forceUpdate()
                    }
                  "
                ></a-checkbox>
              </template>
              <!-- 字段是否在添加修改表单中显示设置 -->
              <template slot="isDisplayInForm" slot-scope="text, record">
                <a-checkbox
                  :default-checked="
                    record.isDisplayInForm &&
                    !disabledFormList.includes(record.fieldLowCamelName)
                  "
                  :disabled="
                    record.isPrimaryKey ||
                    disabledFormList.includes(record.fieldLowCamelName)
                  "
                  @change="
                    () => {
                      record.isDisplayInForm = !record.isDisplayInForm
                      $forceUpdate()
                    }
                  "
                ></a-checkbox>
              </template>
              <!-- 添加修改页面表单字段验证规则选择设置 -->
              <template slot="rules" slot-scope="text, record">
                <a-select
                  :default-value="record.validations"
                  :disabled="
                    record.isPrimaryKey ||
                    !record.isDisplayInForm ||
                    record.javaType === 'Boolean' ||
                    record.javaType === 'Date'
                  "
                  mode="multiple"
                  allow-clear
                  style="width: 190px"
                  size="small"
                  placeholder="请选择校验规则"
                  @change="ruleChange(record, $event)"
                >
                  <a-select-option
                    v-if="
                      record.javaType !== 'Date' &&
                      record.javaType !== 'Boolean'
                    "
                    value="len"
                  >
                    字段长度
                  </a-select-option>
                  <a-select-option
                    v-if="
                      record.javaType !== 'Date' &&
                      record.javaType !== 'Boolean' &&
                      record.javaType !== 'String'
                    "
                    value="min"
                    >最小值</a-select-option
                  >
                  <a-select-option
                    v-if="
                      record.javaType !== 'Date' &&
                      record.javaType !== 'Boolean' &&
                      record.javaType !== 'String'
                    "
                    value="max"
                    >最大值</a-select-option
                  >
                  <a-select-option
                    v-if="record.javaType === 'String'"
                    value="enum"
                  >
                    枚举类型
                  </a-select-option>
                  <a-select-option
                    v-if="record.javaType === 'String'"
                    value="pattern"
                  >
                    正则表达式校验
                  </a-select-option>
                </a-select>
              </template>
              <!-- 字段正则表达式规则设置 -->
              <template
                v-if="patternVisible"
                slot="regex"
                slot-scope="text, record"
              >
                <div
                  v-if="record.isDisplayInForm && record.javaType === 'String'"
                  style="width: 120px"
                >
                  <a-select
                    v-if="record.rules.pattern.editable"
                    :default-value="
                      record.rules.pattern.value !== ''
                        ? record.rules.pattern.value
                        : []
                    "
                    :disabled="record.isPrimaryKey || !record.isDisplayInForm"
                    size="small"
                    style="width: 120px"
                    placeholder="请选择正则表达式"
                    show-search
                    allow-clear
                    auto-focus
                    mode="multiple"
                    @change="patternChange(record, $event)"
                  >
                    <a-select-option
                      v-for="regex in regexList"
                      :key="regex.title"
                      :value="regex.title"
                      :text="regex.title"
                    >
                      {{ regex.title }}
                    </a-select-option>
                  </a-select>
                </div>
              </template>
            </gaf-table-with-page>
          </div>
        </div>
        <!-- 步骤切换按钮 -->
        <div style="text-align: center">
          <button
            v-if="step > 0"
            class="cancel-modal"
            style="margin: 0px 20px"
            @click="prev"
          >
            上一步
          </button>
          <button
            v-if="step < 3"
            style="margin: 0px 20px"
            class="submit-gray"
            @click="next"
          >
            下一步
          </button>
          <button
            v-if="step == 3"
            style="margin: 0px 20px"
            class="submit-gray"
            @click="genarateCode"
          >
            生成代码
          </button>
          <a-button
            v-if="step == 3"
            class="cancel-modal"
            style="margin: 0px 20px"
            @click="onBack"
          >
            返回
          </a-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// 特殊字符列表
const marksArray = [
  '`',
  '~',
  '!',
  '@',
  '#',
  '$',
  '%',
  '^',
  '&',
  '*',
  '(',
  ')',
  '_',
  "'",
  '+',
  '=',
  '<',
  '>',
  '?',
  ':',
  '"',
  '{',
  '}',
  '|',
  ',',
  '.',
  ';',
  '\\',
  '[',
  ']',
  '·',
  '~',
  '！',
  '@',
  '#',
  '￥',
  '%',
  '……',
  '&',
  '*',
  '（',
  '）',
  '——',
  '-',
  '+',
  '=',
  '{',
  '}',
  '|',
  '《',
  '》',
  '？',
  '：',
  '“',
  '”',
  '【',
  '】',
  '、',
  '；',
  '‘',
  '，',
  '。',
  '、',
]
export default {
  directives: {
    focus: {
      // 指令的定义
      inserted(el) {
        el.focus()
      },
    },
    click: {
      inserted(el) {
        el.click()
      },
    },
  },
  data() {
    return {
      // 选择的数据库表
      selectedRowKeys: [],
      selectedRows: [],
      // 选中的工程id
      selectProject: null,
      // 表名称
      tableName: '',
      artifactid: '',
      // 实体类名称
      tableUpperCamelName: '',
      // 包名
      packageName: '',
      // 当前步骤序号
      step: 0,
      // 正则表达式
      regexList: [
        { title: '字母', regex: '/[a-zA-Z]/' },
        { title: '中文', regex: '/^[\u4E00-\u9FA5]+$/' },
        { title: '字母数字下划线', regex: '/^(?!_)(?!.*?_$)[a-zA-Z0-9_]+$/' },
        {
          title: '邮箱',
          regex: '/^([a-zA-Z]|[0-9])(w|-)+@[a-zA-Z0-9]+.([a-zA-Z]{2,4})$/',
        },
        {
          title: '手机号',
          regex: '/^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))d{8}$/',
        },
        {
          title: '身份证',
          regex: '/(^d{15}$)|(^d{18}$)|(^d{17}(d|X|x)$)/',
        },
        {
          title: 'IP地址',
          regex:
            '/^(d{1,2}|1dd|2[0-4]d|25[0-5]).(d{1,2}|1dd|2[0-4]d|25[0-5]).(d{1,2}|1dd|2[0-4]d|25[0-5]).(d{1,2}|1dd|2[0-4]d|25[0-5])$/',
        },
        { title: '数字', regex: '/^(-)?(([0-9])|([1-9]([0-9]+)))(.[0-9]+)?$/' },
        { title: '整数', regex: '/^-?d+$/' },
        { title: '小数', regex: '/^-?d+.d+$/' },
        {
          title: '十六进制颜色',
          regex: '/^#([0-9a-fA-F]{6}|[0-9a-fA-F]{3})$/',
        },
        {
          title: 'RGBA颜色',
          regex:
            '/^[rR][gG][Bb][Aa]?[(]([s]*(2[0-4][0-9]|25[0-5]|[01]?[0-9][0-9]?),){2}[s]*(2[0-4][0-9]|25[0-5]|[01]?[0-9][0-9]?),?[s]*(0.d{1,2}|1|0)?[)]{1}$/g',
        },
        { title: '金额', regex: '/^(([^0][0-9]+|0).([0-9]{1,2}))$/' },
        {
          title: '文件路径',
          regex: '/(^[A-Za-z]{1}:/|^/)([w]*/)*w+.{1}[a-zA-Z]+$/',
        },
      ],
      // 数据库类型
      SQLType: ['POSTGRESQL', 'ORACLE', 'MYSQL', 'SQLSERVER'],
      // 是否显示schema表单控件
      schemaShow: false,
      databaseOrServiceNameShow: false,
      // 根据表名获取字段列表信息
      tableMetadataInfos: {},
      // 数据源对象
      dataSource: {},
      // 获取数据库表列表的参数对象
      tableList: [],
      // 工程列表对象
      projectMetadataInfos: [],
      // 数据库表格是否在加载中
      tableLoading: true,
      // 表格分页参数
      pagination: {
        pageSize: 10,
        current: 1,
        total: 0,
        showSizeChanger: true,
        showQuickJumper: true,
      },
      // 字段表格分页参数
      fieldPagination: {
        pageSize: 10,
        current: 1,
        total: 0,
        showSizeChanger: true,
        showQuickJumper: true,
      },
      // 选择要根据的模板生成单表代码
      template: '',
      // 数据库列表表格列
      tableColumns: [
        {
          title: '表名',
          dataIndex: 'tableName',
          key: 'tableName',
        },
        {
          title: '实体类名称',
          dataIndex: 'tableUpperCamelName',
          key: 'tableUpperCamelName',
        },
        {
          title: '表描述',
          dataIndex: 'remarks',
          key: 'remarks',
        },
      ],
      // 数据源表单对象
      dataSourceForm: this.$form.createForm(this, { name: 'datasource' }),
      // 生成代码配置项表单对象
      configForm: this.$form.createForm(this, { name: 'config' }),
      // 数据库表字段列表表格列
      fieldColumns: [
        {
          title: '字段名称',
          dataIndex: 'fieldName',
          key: 'fieldName',
        },
        {
          title: '字段类型',
          dataIndex: 'sqlType',
          key: 'sqlType',
        },
        {
          title: '实体属性',
          dataIndex: 'fieldLowCamelName',
          key: 'fieldLowCamelName',
        },
        {
          title: '实体类型',
          dataIndex: 'javaType',
          key: 'javaType',
          scopedSlots: { customRender: 'javaType' },
        },
        {
          title: '字段描述',
          dataIndex: 'remarks',
          key: 'remarks',
          scopedSlots: { customRender: 'remarks' },
        },
        {
          title: '主键',
          key: 'isPrimaryKey',
          dataIndex: 'isPrimaryKey',
          align: 'center',
          scopedSlots: { customRender: 'isPrimaryKey' },
        },
        {
          title: '列表显示',
          key: 'isDisplayInTable',
          dataIndex: 'isDisplayInTable',
          align: 'center',
          scopedSlots: { customRender: 'isDisplayInTable' },
        },
        {
          title: '查询',
          key: 'isSelect',
          dataIndex: 'isSelect',
          align: 'center',
          scopedSlots: { customRender: 'isSelect' },
        },
        {
          title: '排序',
          key: 'isSort',
          dataIndex: 'isSort',
          align: 'center',
          scopedSlots: { customRender: 'isSort' },
        },
        {
          title: '表单显示',
          key: 'isDisplayInForm',
          align: 'center',
          dataIndex: 'isDisplayInForm',
          scopedSlots: { customRender: 'isDisplayInForm' },
        },
        {
          title: '必填',
          key: 'isNullable',
          dataIndex: 'isNullable',
          align: 'center',
          scopedSlots: { customRender: 'isNullable' },
        },
        {
          title: '表单类型',
          key: 'htmlType',
          align: 'center',
          dataIndex: 'htmlType',
          scopedSlots: { customRender: 'htmlType' },
        },
        {
          title: '校验规则',
          key: 'rules',
          dataIndex: 'rules',
          scopedSlots: { customRender: 'rules' },
        },
      ],
      // 可供选择的表单类型
      formtypeList: [
        '文本框',
        '文本域',
        '数字输入框',
        '复选框',
        '密码框',
        '单选框',
        '开关',
        '下拉框',
        '级联选择器',
        '日期时间控件',
      ],
      // 模板列表
      templateList: [],
      // 全部表数据
      tableAllData: [],
      // 工程服务名
      projectService: '',
      // 用户输入有误提示
      inputErrorTip: '',
      // 控制最小值输入框可见
      minVisible: false,
      // 控制最大值输入框可见
      maxVisible: false,
      // 控制字段长度输入框可见
      lenVisible: false,
      // 控制正则表达式输入框可见
      patternVisible: false,
      // 控制枚举值输入框可见
      enumVisible: false,
      javaTypeList: [
        { key: 1, value: 'String' },
        { key: 2, value: 'Integer' },
        { key: 3, value: 'Double' },
        { key: 4, value: 'Boolean' },
        { key: 5, value: 'Date' },
        { key: 6, value: 'Float' },
        { key: 7, value: 'Long' },
        { key: 8, value: 'Char' },
      ],
      disabledTableList: [
        'createdTime',
        'createdBy',
        'updatedTime',
        'updatedBy',
      ],
      disabledFormList: [
        'createdTime',
        'createdBy',
        'updatedTime',
        'updatedBy',
      ],
    }
  },
  watch: {
    template: {
      handler(newVal) {
        if (newVal === '') {
          this.getTemplateList()
          this.template = this.templateList[0].projCodeTemplateId
        }
      },
    },
    // 控制最小值表格列是否可见
    minVisible: {
      handler(newValue) {
        if (newValue) {
          this.fieldColumns.push({
            title: '最小值',
            key: 'minValue',
            dataIndex: 'minValue',
            scopedSlots: { customRender: 'minValue' },
          })
        } else {
          this.fieldColumns = this.fieldColumns.filter(
            (item) => item.key !== 'minValue'
          )
        }
        this.$forceUpdate()
      },
    },
    // 控制最大值表格列是否可见
    maxVisible: {
      handler(newValue) {
        if (newValue) {
          this.fieldColumns.push({
            title: '最大值',
            key: 'maxValue',
            dataIndex: 'maxValue',
            scopedSlots: { customRender: 'maxValue' },
          })
        } else {
          this.fieldColumns = this.fieldColumns.filter(
            (item) => item.key !== 'maxValue'
          )
        }
        this.$forceUpdate()
      },
    },
    // 控制字段长度表格列是否可见
    lenVisible: {
      handler(newValue) {
        if (newValue) {
          this.fieldColumns.push({
            title: '字段长度',
            key: 'columnSize',
            dataIndex: 'columnSize',
            scopedSlots: { customRender: 'columnSize' },
          })
        } else {
          this.fieldColumns = this.fieldColumns.filter(
            (item) => item.key !== 'columnSize'
          )
        }
        this.$forceUpdate()
      },
    },
    // 控制正则表达式表格列是否可见
    patternVisible: {
      handler(newValue) {
        if (newValue) {
          this.fieldColumns.push({
            title: '正则表达式',
            key: 'regex',
            dataIndex: 'regex',
            scopedSlots: { customRender: 'regex' },
          })
        } else {
          this.fieldColumns = this.fieldColumns.filter(
            (item) => item.key !== 'regex'
          )
        }
        this.$forceUpdate()
      },
    },
    // 控制枚举类型表格列是否可见
    enumVisible: {
      handler(newValue) {
        if (newValue) {
          this.fieldColumns.push({
            title: '枚举类型',
            key: 'enumValue',
            dataIndex: 'enumValue',
            scopedSlots: { customRender: 'enumValue' },
          })
        } else {
          this.fieldColumns = this.fieldColumns.filter(
            (item) => item.key !== 'enumValue'
          )
        }
        this.$forceUpdate()
      },
    },
  },
  destroyed() {},
  methods: {
    // 下一步
    async next() {
      if (this.step === 0) {
        const isPass = await this.datasourceValidate()
        if (isPass) {
          this.dataSource = this.dataSourceForm.getFieldsValue()
          this.getTableList()
          this.step++
        }
      } else if (this.step === 1) {
        const isPass = this.afterSelectTable()
        if (isPass) {
          this.step++
        }
      } else if (this.step === 2) {
        const isPass = this.configFormValidate()
        if (isPass) {
          this.step++
        }
      }
    },
    // 上一步
    prev() {
      if (this.step === 2) {
        this.beforeBackToTableList()
      }
      this.step--
    },
    onBack() {
      this.beforeBackToTableList()
      this.step = 1
    },
    // 回到数据库表列表页
    beforeBackToTableList() {
      this.fieldPagination.current = 1
      this.template = ''
      this.configForm.resetFields()
      this.lenVisible = false
      this.minVisible = false
      this.maxVisible = false
      this.enumVisible = false
      this.patternVisible = false
    },
    // 当选择的表发生该变时
    onChange(selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
    // 实体类型下拉菜单点击事件
    onClick(record, event) {
      record.javaType = this.javaTypeList[event.key - 1].value
      this.$forceUpdate()
    },
    initFeildSetting() {
      if (
        this.patternVisible ||
        this.enumVisible ||
        this.lenVisible ||
        this.maxVisible ||
        this.minVisible
      ) {
        return false
      }
      this.showField()
    },
    // 最小值输入框focus事件
    minFocus(record, event) {
      event.target.value = record.rules.min.value
      event.target.select()
    },
    // 最大值输入框focus事件
    maxFocus(record, event) {
      event.target.value = record.rules.max.value
      event.target.select()
    },
    // 字段长度输入框focus事件
    lenFocus(record, event) {
      event.target.value = record.rules.len.value
      event.target.select()
    },
    // 枚举类型输入框focus事件
    enumFocus(record, event) {
      event.target.value = record.rules.enum.value
      event.target.select()
    },
    // 最小值a标签click事件
    minValueClick(record) {
      record.rules.min.editable = true
      this.$forceUpdate()
    },
    // 最大值a标签click事件
    maxValueClick(record) {
      record.rules.max.editable = true
      this.$forceUpdate()
    },
    // 字段长度a标签click事件
    lenValueClick(record) {
      record.rules.len.editable = true
      this.$forceUpdate()
    },
    // 枚举类型a标签click事件
    enumValueClick(record) {
      record.rules.enum.editable = true
      this.$forceUpdate()
    },
    // 正则表达式下拉框改变触发事件
    patternChange(record, event) {
      record.rules.pattern.value = event
      this.$forceUpdate()
    },
    // 校验规则改变触发事件
    ruleChange(record, event) {
      // 绑定字段校验选择框属性值
      record.validations = event
      // 字段长度输入框
      if (event[event.length - 1] === 'len') {
        if (record.rules.len.value === '') record.rules.len.editable = true
      } else if (!event.some((item) => item === 'len')) {
        record.rules.len.editable = false
        record.rules.len.value = ''
      }
      // 最大值输入框
      if (event[event.length - 1] === 'max') {
        if (record.rules.max.value === '') record.rules.max.editable = true
      } else if (!event.some((item) => item === 'max')) {
        record.rules.max.editable = false
        record.rules.max.value = ''
      }
      // 最小值输入框
      if (event[event.length - 1] === 'min') {
        if (record.rules.min.value === '') record.rules.min.editable = true
      } else if (!event.some((item) => item === 'min')) {
        record.rules.min.editable = false
        record.rules.min.value = ''
      }
      // 正则表达式输入框
      if (event[event.length - 1] === 'pattern') {
        if (record.rules.pattern.value === '')
          record.rules.pattern.editable = true
      } else if (!event.some((item) => item === 'pattern')) {
        record.rules.pattern.value = ''
        record.rules.pattern.editable = false
      }
      // 枚举输入框
      if (event[event.length - 1] === 'enum') {
        if (record.rules.enum.value === '') record.rules.enum.editable = true
      } else if (!event.some((item) => item === 'enum')) {
        record.rules.enum.editable = false
        record.rules.enum.value = ''
      }

      // 如果验证规则输入选择框可编辑或者有值信息时则表格显示该列
      this.lenVisible = this.tableMetadataInfos.fieldMetadataInfos.some(
        (item) => item.rules.len.editable || item.rules.len.value !== ''
      )
      this.minVisible = this.tableMetadataInfos.fieldMetadataInfos.some(
        (item) => item.rules.min.editable || item.rules.min.value !== ''
      )
      this.maxVisible = this.tableMetadataInfos.fieldMetadataInfos.some(
        (item) => item.rules.max.editable || item.rules.max.value !== ''
      )
      this.enumVisible = this.tableMetadataInfos.fieldMetadataInfos.some(
        (item) => item.rules.enum.editable || item.rules.enum.value !== ''
      )
      this.patternVisible = this.tableMetadataInfos.fieldMetadataInfos.some(
        (item) => item.rules.pattern.editable || item.rules.pattern.value !== ''
      )
      this.$forceUpdate()
    },
    javaTypeChange(record, event) {
      if (!event.target.value) return false
      record.javaType = event.target.value
      this.$forceUpdate()
    },
    // 字段中文名称属性数据绑定
    remarksChange(record, event) {
      if (!event.target.value) return false
      record.remarks = event.target.value
      this.$forceUpdate()
    },
    // 最小值属性数据绑定
    minValueChange(record, event) {
      this.inputErrorTip = ''
      if (!event.target.value) return false
      if (marksArray.some((item) => event.target.value.includes(item))) {
        this.inputErrorTip = '请输入数字！'
        this.$message.error(this.inputErrorTip, 1)
        return false
      }
      record.rules.min.value = event.target.value
      const min = parseFloat(record.rules.min.value)
      const max = parseFloat(record.rules.max.value)
      if (!isNaN(min)) {
        if (!isNaN(max)) {
          if (min > max) {
            this.inputErrorTip = '请输入小于等于最大值的数字！'
            event.target.value = ''
            event.target.focus()
            this.$message.error(this.inputErrorTip, 1)
            return false
          }
        }
      } else {
        this.inputErrorTip = '请输入数字！'
        event.target.value = ''
        event.target.focus()
        this.$message.error(this.inputErrorTip, 1)
        return false
      }
      record.rules.min.editable = false
      this.$forceUpdate()
    },
    // 最大值属性数据绑定
    maxValueChange(record, event) {
      this.inputErrorTip = ''
      if (!event.target.value) return false
      if (marksArray.some((item) => event.target.value.includes(item))) {
        this.inputErrorTip = '请输入数字！'
        this.$message.error(this.inputErrorTip, 1)
        return false
      }
      record.rules.max.value = event.target.value
      const min = parseFloat(record.rules.min.value)
      const max = parseFloat(record.rules.max.value)
      if (!isNaN(min)) {
        if (!isNaN(max)) {
          if (min > max) {
            this.inputErrorTip = '请输入大于最小值的数字！'
            event.target.value = ''
            event.target.focus()
            this.$message.error(this.inputErrorTip, 1)
            return false
          }
        } else {
          this.inputErrorTip = '请输入数字！'
          event.target.value = ''
          event.target.focus()
          this.$message.error(this.inputErrorTip, 1)
          return false
        }
      }
      record.rules.max.editable = false
      this.$forceUpdate()
    },
    // 字段长度属性数据绑定
    lenValueChange(record, event) {
      this.inputErrorTip = ''
      if (!event.target.value) return false
      if (marksArray.some((item) => event.target.value.includes(item))) {
        this.inputErrorTip = '请输入数字！'
        this.$message.error(this.inputErrorTip, 1)
        return false
      }
      const len = parseFloat(event.target.value)
      if (!isNaN(len)) {
        if (len <= 0) {
          this.inputErrorTip = '字段长度必须大于0！'
          event.target.value = ''
          event.target.focus()
          this.$message.error(this.inputErrorTip, 1)
        } else {
          record.rules.len.value = event.target.value
          record.rules.len.editable = false
          this.$forceUpdate()
        }
      } else {
        this.inputErrorTip = '请输入数字！'
        event.target.value = ''
        event.target.focus()
        this.$message.error(this.inputErrorTip, 1)
      }
    },
    // 枚举类型数据绑定
    enumValueChange(record, event) {
      if (!event.target.value) return false
      record.rules.enum.value = event.target.value
      record.rules.enum.editable = false
      this.$forceUpdate()
    },
    // 主键数组索引属性数据绑定
    primaryKeyChange(record, event) {
      if (event.target.checked) {
        record.isPrimaryKey = true
        this.tableMetadataInfos.pkJavaType = record.javaType
        this.tableMetadataInfos.pkJdbcType = record.jdbcDataType
        this.tableMetadataInfos.pkLowCamelName = record.fieldLowCamelName
        this.tableMetadataInfos.pkName = record.fieldName
        this.tableMetadataInfos.pkSqlType = record.sqlType
        this.tableMetadataInfos.pkUpperCamelName = record.fieldUpperCamelName
      }
      // 如果没选中，则为-1
      else {
        record.isPrimaryKey = false
        this.tableMetadataInfos.pkJavaType = null
        this.tableMetadataInfos.pkJdbcType = null
        this.tableMetadataInfos.pkLowCamelName = null
        this.tableMetadataInfos.pkName = null
        this.tableMetadataInfos.pkSqlType = null
        this.tableMetadataInfos.pkUpperCamelName = null
      }
      this.$forceUpdate()
    },
    // 页面加载时，工程名自动获取焦点，请求后端数据返回相关工程信息
    async projectFocus() {
      const url = '/proj/dev/project/lists?status=2'
      const res = await this.$axios.$get(url)
      if (res && res.successed) {
        this.projectMetadataInfos = res.data
      } else {
        this.$message.info('请手动填写数据源信息！')
      }
    },
    // 当选择工程发生变化时，自动填写数据库源表单信息
    projectChange(value) {
      if (!value) {
        this.selectProject = null
        return
      }
      // 暂时注释
      this.projectMetadataInfos = this.projectMetadataInfos.filter(
        (item) => item.projId === value
      )
      this.selectProject = this.projectMetadataInfos[0]
      // 重置数据源表单
      this.datasourceReset()
      const obj = JSON.parse(this.projectMetadataInfos[0].settingParams)
      if (obj) {
        if (obj.projSetting) {
          this.projectService = obj.projSetting.service
        }
        if (obj.dsInfo) {
          let hostandport = obj.dsInfo.host.split(':')
          this.dataSourceForm.setFieldsValue({
            projectName: value,
            datasourceType: obj.dsInfo.datasourceType,
            port: hostandport[1] ? hostandport[1]: '',
            host: hostandport[0] ? hostandport[0]: '',
            username: obj.dsInfo.username,
            password: obj.dsInfo.password,
            databaseOrServiceName: obj.dsInfo.databaseOrServiceName,
          })
        }
      }
    },
    // 控制数据源发生变化时schema的显示与否
    SQLTypeChange(val) {
      this.schemaShow = false
      this.databaseOrServiceNameShow = false
      if (val === 'POSTGRESQL') {
        this.schemaShow = true
      } else if (val === 'SQLSERVER') {
        this.databaseOrServiceNameShow = true
      }
    },
    // 提交数据源信息，请求后端接口获取工程下的所有数据表信息
    async datasourceValidate() {
      // 如果数据源信息表单验证不通过，则不提交
      let validatePass = false
      this.dataSourceForm.validateFields((err) => {
        if (err) {
          validatePass = false
          return false
        } else {
          validatePass = true
          return true
        }
      })
      // eslint-disable-next-line no-return-await
      return validatePass && (await this.checkDataSourceConnect())
    },
    async checkDataSourceConnect() {
      // 检查数据库连接是否正常
      const url = '/proj/table-code/connection-param/check'
      const dataSource = this.dataSourceForm.getFieldsValue()
      const res = await this.$axios.$post(url, dataSource)
      if (res.successed) {
        return true
      } else {
        this.$message.error(`${res.message}`)
        return false
      }
    },
    configFormValidate() {
      let validatePass = false
      this.configForm.validateFields((err) => {
        if (err) {
          validatePass = false
        } else {
          validatePass = true
        }
      })
      return validatePass
    },
    // 重置数据源表单
    datasourceReset() {
      this.dataSourceForm.resetFields()
    },
    // 下载模板
    async download() {
      const url =
        '/proj/table-code/generator-zip?projCodeTemplateId=' + this.template
      const author = this.tableMetadataInfos.author
      const packageName = this.tableMetadataInfos.packageName
      delete this.tableMetadataInfos.author
      delete this.tableMetadataInfos.packageName
      // 设置主键是否自动生成
      this.tableMetadataInfos.isGeneratedPrimaryKey =
        this.tableMetadataInfos.pkName !== null
      this.tableMetadataInfos.fieldMetadataInfos.forEach((item) => {
        delete item.rules.pattern.editable
        delete item.rules.len.editable
        delete item.rules.min.editable
        delete item.rules.max.editable
        delete item.rules.enum.editable
      })
      // 设置正则表达式内容
      this.tableMetadataInfos.fieldMetadataInfos.forEach((v) => {
        if (v.rules.pattern.value !== '') {
          const arr = []
          v.rules.pattern.value.forEach((e) => {
            const index = this.regexList.findIndex((item) => e === item.title)
            arr.push(this.regexList[index])
          })
          v.rules.pattern.value = arr
        }
      })

      if (this.tableMetadataInfos.tableName) {
        this.tableMetadataInfos.tableNameHyphen = this.tableMetadataInfos.tableName.replace(
          /_/g,
          '-'
        )
      }
      const artifactId = this.tableMetadataInfos.artifactid
        ? this.tableMetadataInfos.artifactid
        : null
      const res = await this.$axios.$post(
        url,
        {
          projectInfo: this.selectProject,
          artifactId,
          packagename:packageName,
          author,
          tableMetadataInfo: {
            ...this.tableMetadataInfos,
          },
        },
        {
          responseType: 'blob',
        }
      )
      if (res) {
        // 返回错误信息
        if (res.message) {
          this.$message.error(res.message, 1)
          return false
        }
        // 生成时间戳
        const timestamp3 = new Date().getTime()
        const fileName =
          this.tableMetadataInfos.tableName + '_' + timestamp3 + '.zip'
        const blob = new Blob([res]) // 如果是IE 通过msSaveBlob 下载   ，否则走h5下载
        if (navigator.msSaveBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          const a = document.createElement('a')
          a.download = fileName
          a.style.display = 'none'
          a.innerHTML = 'download'
          a.href = URL.createObjectURL(blob)
          document.body.appendChild(a)
          a.click()
          URL.revokeObjectURL(a.href)
          document.body.removeChild(a)
        }
      }
    },
    // 数据库表分页属性数据绑定
    tableChange(pageInfo) {
      if (pageInfo) {
        this.pagination.current = pageInfo.current
        this.pagination.pageSize = pageInfo.pageSize
      }
      this.$forceUpdate()
    },
    // 数据库表列表每页显示条数改变事件
    tableSizeChange(current, size) {
      if (size) {
        this.pagination.pageSize = size
      }
      this.$forceUpdate()
    },
    // 数据库表字段列表页面页码改变事件
    fieldTableChange(pageInfo) {
      if (pageInfo) {
        this.fieldPagination.current = pageInfo.current
        this.fieldPagination.pageSize = pageInfo.pageSize
      }
      this.$forceUpdate()
    },
    // 数据库表字段列表页面每页显示条数改变事件
    fieldSizeChange(current, size) {
      if (size) {
        this.fieldPagination.pageSize = size
      }
      this.$forceUpdate()
    },
    // 根据表名筛选数据表信息
    /* searchTableList(name, value) {
      this.tableLoading = true
      const keyword = value.trim().toLowerCase()
      if (keyword) {
        this.tableList = this.tableAllData.filter(e => {
          return e.tableName.indexOf(keyword) !== -1
        })
      } else {
        this.tableList = this.tableAllData
      }
      this.pagination.total = this.tableList.length
      this.tableLoading = false
      value = ''
    }, */
    onSearch(value) {
      this.tableLoading = true
      const keyword = value.trim().toLowerCase()
      if (keyword) {
        this.tableList = this.tableAllData.filter((e) => {
          return e.tableName.includes(keyword)
        })
      } else {
        this.tableList = this.tableAllData
      }
      this.pagination.total = this.tableList.length
      this.tableLoading = false
      value = ''
    },
    // 获取数据源所有表信息
    async getTableList() {
      this.tableLoading = true
      const url = '/proj/table-code/tables-metadata'
      const res = await this.$axios.$post(url, this.dataSource)
      if (res.successed) {
        this.tableList = res.data
        // 如果用户第一次连接此数据源，则给配置项信息赋初始值
        this.tableList.map((item) => {
          // 字段配置项
          item.fieldMetadataInfos = []
          // 备注信息
          item.comment = ''
          if (!item.remarks) {
            // 表描述
            item.remarks = ''
          }
          // 作者
          item.author = ''
        })
      } else {
        this.$message.error('获取数据库表信息失败！', 1)
        return false
      }
      this.tableAllData = res.data
      this.pagination.total = this.tableList.length
      this.tableLoading = false
    },
    async getTemplateList() {
      // 查询模板
      const url = '/proj/projcodetemplate/codetype'
      const templateResult = await this.$axios.$get(url)
      if (templateResult.successed) {
        this.templateList = templateResult.data
        this.template = this.templateList[0].projCodeTemplateId
      } else {
        this.$message.error(`获取模板信息失败,原因：${templateResult.message}`)
      }
    },
    async getDataTable(tableName) {
      // 根据表名查询字段信息
      const url = '/proj/table-code/field-metadata?tablename=' + tableName
      const res = await this.$axios.$post(url, this.dataSource)
      if (res.successed) {
        this.tableMetadataInfos = res.data
      } else {
        this.$message.error(`获取字段信息失败,原因：${res.message}`)
      }
    },
    // 选择数据库表后点击下一步
    afterSelectTable() {
      if (this.selectedRowKeys.length > 0 && this.selectedRows.length > 0) {
        this.getTableInfoAndSet(this.selectedRows[0])
        return true
      } else {
        this.$message.info('请选择一张表')
        return false
      }
    },
    async getTableInfoAndSet(record) {
      this.tableUpperCamelName = record.tableUpperCamelName
      this.tableName = record.tableName
      let remarks = record.remarks
      if (
        remarks &&
        remarks.length > 0 &&
        remarks.indexOf('表') === remarks.length - 1
      ) {
        remarks = remarks.substring(0, remarks.length - 1)
      }
      this.configForm.setFieldsValue({ remarks })
      if (this.selectProject) {
        const settingParams = JSON.parse(this.selectProject.settingParams)
        if (settingParams && settingParams.basic) {
          this.packageName = settingParams.basic.packagename
          this.artifactid = settingParams.basic.artifactId
          this.configForm.setFieldsValue({
            artifactid: settingParams.basic.artifactId,
            package: settingParams.basic.packagename,
          })
        }
      }
      this.getTemplateList()
      await this.getDataTable(record.tableName)
      this.initFeildSetting()
    },
    // 展示数据库表字段信息设置页面
    showField() {
      // 初始化默认字段配置信息
      this.fieldPagination.total = this.tableMetadataInfos.fieldMetadataInfos.length
      this.initFieldMetadataInfo()
    },
    // 生成代码
    genarateCode() {
      if (this.inputErrorTip !== '') {
        this.$message.error(this.inputErrorTip)
      } else {
        const values = this.configForm.getFieldsValue()
        this.tableMetadataInfos.packageName = values.package
        this.tableMetadataInfos.artifactid = values.artifactid
        this.tableMetadataInfos.tableUpperCamelName = values.tableUpperCamelName
        this.tableMetadataInfos.remarks = values.remarks
        this.tableMetadataInfos.comment = values.comment
        this.tableMetadataInfos.author = values.author
        this.tableMetadataInfos.pjService = values.artifactid
        this.download()
      }
    },
    // 生成模板属性数据绑定
    templateChange(value) {
      if (value) this.template = value
      this.configForm.setFieldsValue({
        templateName: value,
      })
    },
    // 初始化字段信息配置
    initFieldMetadataInfo() {
      if (
        this.tableMetadataInfos &&
        JSON.stringify(this.tableMetadataInfos) !== '{}'
      ) {
        if (
          this.tableMetadataInfos.fieldMetadataInfos &&
          this.tableMetadataInfos.fieldMetadataInfos.length > 0
        ) {
          this.tableMetadataInfos.fieldMetadataInfos.map((item) => {
            // 如果字段备注为空，则用字段名替代
            if (!item.remarks) {
              item.remarks = item.fieldName
            }
            // 表单类型
            switch (item.javaType) {
              case 'Long':
              case 'Integer':
              case 'Short':
              case 'byte':
              case 'Float':
              case 'Double':
              case 'BigDecimal':
                item.htmlType = '数字输入框'
                break
              case 'String':
                item.htmlType = item.columnSize >= 500 ? '文本域' : '文本框'
                if (item.fieldName.includes('password')) {
                  item.htmlType = '密码框'
                }
                break
              case 'Boolean':
                item.htmlType = '单选框'
                break
              case 'Date':
                item.htmlType = '日期时间控件'
                break
              default:
                item.htmlType = '下拉框'
                break
            }
            // 截取字段备注中的特殊字符之前的文字，特殊符号之后的文字舍去
            if (item.remarks) {
              let fieldCNName = item.remarks
              // 字段描述
              marksArray.map((mark) => {
                if (item.remarks.includes(mark)) {
                  const parentIndex = item.remarks.indexOf(mark)
                  fieldCNName = fieldCNName.substring(0, parentIndex)
                }
              })
              item.remarks = fieldCNName
            }
            if (item.isPrimaryKey) {
              // 主键在列表不显示
              item.isDisplayInTable = false
              // 主键在表单不显示
              item.isDisplayInForm = false
            } else {
              item.isDisplayInTable = true
              item.isDisplayInForm = true
              item.isPrimaryKey = false
            }
            // 是否允许用户修改字段必填的状态
            item.canNullableEdit = item.isNullable
            // 是否检索
            item.isSelect = false
            // 是否排序
            item.isSort = false
            // 字段校验
            item.validations = []
            // 校验规则
            item.rules = {
              len: {
                value: '',
                editable: false,
              },
              max: {
                value: '',
                editable: false,
              },
              min: {
                value: '',
                editable: false,
              },
              pattern: {
                value: '',
                editable: false,
              },
              enum: {
                value: '',
                editable: false,
              },
            }
          })
        }
      }
    },
    // 表单类型数据绑定
    htmlChange(record, event) {
      record.htmlType = event
      this.$forceUpdate()
    },
  },
}
</script>
