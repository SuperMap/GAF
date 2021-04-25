<template>
  <div>
    <a-form layout="horizontal" :form="form">
      <a-form-item
        label="地图名称"
        :label-col="{ span: 3 }"
        :wrapper-col="{ span: 21 }"
      >
        <a-input
          v-decorator="[
            'mapName',
            {
              rules: [
                {
                  required: true,
                  message: '请输入地图名称!',
                },
              ],
            },
          ]"
          size="small"
        />
      </a-form-item>
      <a-form-item
        label="地图标识"
        :label-col="{ span: 3 }"
        :wrapper-col="{ span: 21 }"
      >
        <a-input
          v-decorator="[
            'code',
            {
              rules: [
                {
                  required: true,
                  message: '请输入地图标识!',
                },
              ],
            },
          ]"
          size="small"
        />
      </a-form-item>
      <a-form-item v-show="false">
        <a-input
          v-decorator="[
            'type',
            {
              rules: [
                {
                  required: true,
                  message: '请选择地图类型!',
                },
              ],
            },
          ]"
          size="small"
        />
      </a-form-item>
      <a-form-item>
        <a-row>
          <a-col :span="8">
            <a-card
              hoverable
              :class="
                selectTemplateMap === 'MAP3D' ? 'selectedCard' : 'noneCard'
              "
              @click="card1Click"
            >
              <img
                slot="cover"
                alt="example"
                :src="relolveImgPath('/img/mapview3d.jpg')"
              />
              <a-card-meta title="三维地图" />
            </a-card>
          </a-col>
          <a-col :span="8">
            <a-card
              hoverable
              :class="
                selectTemplateMap === 'MAP2D' ? 'selectedCard' : 'noneCard'
              "
              @click="card2Click"
            >
              <img
                slot="cover"
                alt="example"
                :src="relolveImgPath('/img/mapview.png')"
              />
              <a-card-meta title="二维地图" />
            </a-card>
          </a-col>
          <a-col :span="8">
            <a-card
              hoverable
              :class="
                selectTemplateMap === 'CUSTOM' ? 'selectedCard' : 'noneCard'
              "
              @click="card3Click"
            >
              <img
                slot="cover"
                alt="example"
                :src="relolveImgPath('/img/userdefinemap.jpg')"
              />
              <a-card-meta title="自定义地图" />
            </a-card>
          </a-col>
        </a-row>
      </a-form-item>
    </a-form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      selectTemplateMap: null,
    }
  },
  beforeCreate() {
    this.form = this.$form.createForm(this)
  },
  methods: {
    relolveImgPath(img) {
      const isDev = this.$store.state.isDev
      return isDev ? img : `.${img}`
    },
    card1Click() {
      this.selectTemplateMap = 'MAP3D'
      this.form.setFieldsValue({
        type: 'MAP3D',
      })
    },
    card2Click() {
      this.selectTemplateMap = 'MAP2D'
      this.form.setFieldsValue({
        type: 'MAP2D',
      })
    },
    card3Click() {
      this.selectTemplateMap = 'CUSTOM'
      this.form.setFieldsValue({
        type: 'CUSTOM',
      })
    },
  },
}
</script>

<style scoped>
.selectedCard {
  width: 240px;
  border: 1px solid #1890ff;
}
.noneCard {
  width: 240px;
  border: 1px solid #e8e8e8;
}
</style>
