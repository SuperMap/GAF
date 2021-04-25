<template>
  <div>
    <a-select
      label-in-value
      style="width: 100%"
      size="small"
      :show-arrow="false"
      dropdown-class-name="gaf-color-picker"
      @change="change"
    >
      <a-select-option key="value1">
        <div class="colorBack1 content" />
      </a-select-option>
      <a-select-option key="value2">
        <div class="colorBack2 content" />
      </a-select-option>
      <a-select-option key="value3">
        <div class="colorBack3 content" />
      </a-select-option>
      <a-select-option key="value4">
        <div class="colorBack4 content" />
      </a-select-option>
    </a-select>
  </div>
</template>

<script>
export default {
  props: {
    // 当前颜色值
    value: {
      type: Object,
      required: true,
    },
    count: {
      type: Number,
      required: true,
      default: 4,
    },
  },
  data() {
    return {
      result: [],
      values: {
        value1: [
          '144, 238, 144',
          '108, 204, 108',
          '72, 170, 72',
          '36, 136, 36',
          '0, 100, 0',
        ],
        value2: [
          '255, 0, 0',
          '223, 0, 0',
          '191, 0, 0',
          '160, 0, 0',
          '128, 0, 0',
        ],
        value3: [
          '255, 250, 240',
          '188, 238, 104',
          '132, 112, 255',
          '64, 224, 208',
          '0, 191, 255',
        ],
        value4: [
          '255, 255, 0',
          '205, 38, 38',
          '138, 43, 226',
          '67, 110, 238',
          '0, 255, 0',
        ],
      },
      arrayColor: [],
    }
  },
  watch: {
    count: {
      handler() {
        if (this.arrayColor.length) {
          this.getColor(this.count, this.arrayColor)
          this.$emit('change', this.result)
        }
      },
      immediate: true,
    },
  },
  methods: {
    change(value) {
      this.arrayColor = this.values[value.key]
      this.getColor(this.count, this.values[value.key])
      this.$emit('change', this.result)
    },
    getColor(count, array) {
      if (array && array.length) {
        this.result = []
        const length = array.length
        const c = Math.ceil(count / (length - 1))
        for (let i = 0; i < array.length - 1; i++) {
          const arrStart = array[i].split(',')
          const arrEnd = array[i + 1].split(',')
          let r = parseInt(arrStart[0])
          let g = parseInt(arrStart[1])
          let b = parseInt(arrStart[2])
          const dr = Math.floor((arrEnd[0] - r) / (c + 1))
          const dg = Math.floor((arrEnd[1] - g) / (c + 1))
          const db = Math.floor((arrEnd[2] - b) / (c + 1))
          for (let i = 0; i < c && count > 0; i++) {
            r += dr
            g += dg
            b += db
            const rgb = 'rgb(' + r + ',' + g + ',' + b + ')'
            const color = this.colorHex(rgb)
            this.result.push(color)
            count -= 1
          }
        }
      }
    },
    /* RGB颜色转换为16进制 */
    colorHex(value) {
      const reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/
      const that = value
      if (/^(rgb|RGB)/.test(that)) {
        const aColor = that.replace(/(?:\(|\)|rgb|RGB)*/g, '').split(',')
        let strHex = '#'
        for (let i = 0; i < aColor.length; i++) {
          let hex = Number(aColor[i]).toString(16)
          if (hex === '0') {
            hex += hex
          }
          if (hex.length === 1) {
            hex = '0' + hex
          }
          if (hex.includes('-')) {
            hex = '00'
          }
          strHex += hex
        }
        if (strHex.length !== 7) {
          strHex = that
        }
        return strHex
      } else if (reg.test(that)) {
        const aNum = that.replace(/#/, '').split('')
        if (aNum.length === 6) {
          return that
        } else if (aNum.length === 3) {
          let numHex = '#'
          for (let i = 0; i < aNum.length; i += 1) {
            numHex += aNum[i] + aNum[i]
          }
          return numHex
        }
      } else {
        return that
      }
    },
  },
}
</script>

<style lang="less" scoped>
.gaf-color-picker {
  & .ant-select-dropdown-menu-item {
    height: 20px;
  }
}
</style>
<style scoped>
.colorBack1 {
  background: linear-gradient(
    to right,
    rgb(144, 238, 144),
    rgb(108, 204, 108),
    rgb(72, 170, 72),
    rgb(36, 136, 36),
    rgb(0, 100, 0)
  );
}
.colorBack2 {
  background: linear-gradient(
    to right,
    rgb(255, 0, 0),
    rgb(223, 0, 0),
    rgb(191, 0, 0),
    rgb(160, 0, 0),
    rgb(128, 0, 0)
  );
}
.colorBack3 {
  background: linear-gradient(
    to right,
    rgb(255, 250, 240),
    rgb(188, 238, 104),
    rgb(132, 112, 255),
    rgb(64, 224, 208),
    rgb(0, 191, 255)
  );
}
.colorBack4 {
  background: linear-gradient(
    to right,
    rgb(255, 255, 0),
    rgb(205, 38, 38),
    rgb(138, 43, 226),
    rgb(67, 110, 238),
    rgb(0, 255, 0)
  );
}
.content {
  position: absolute;
  margin: auto;
  top: 5px;
  left: 0;
  bottom: 5px;
  right: 5px;
  width: 100%;
  height: 15px;
}
</style>
