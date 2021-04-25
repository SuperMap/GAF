<template>
  <div ref="bgColor" class="bg-color">
    <div class="txt">{{ timeTxt }}{{ timer }}</div>
    <!-- <div class="time">{{ timer }}</div> -->
  </div>
</template>
<script>
export default {
  props: {
    endTime: {
      type: String,
      default: () => '',
    },
  },
  data() {
    return {
      timeTxt: '',
      timer: '',
      year: '',
      month: '',
      day: '',
      hour: '',
      minute: '',
      second: '',
    }
  },
  mounted() {
    this.timeSwitch(this.endTime)
  },
  methods: {
    timeSwitch(endTime) {
      this.parseDataTime(endTime)
      setInterval(this.leftTimer, 1000)
    },
    parseDataTime(endTime) {
      const deadLine = endTime
      const deadLineTime = deadLine.replace(/[:]/g, '').substr(11, 14)
      this.year = parseInt(deadLine.replace(/[-]/g, '').substr(0, 4))
      this.month = parseInt(deadLine.replace(/[-]/g, '').substr(4, 2))
      this.day = parseInt(deadLine.replace(/[-]/g, '').substr(6, 2))
      this.hour = parseInt(deadLineTime.replace(/[-]/g, '').substr(0, 2))
      this.minute = parseInt(deadLineTime.replace(/[-]/g, '').substr(2, 2))
      this.second = parseInt(deadLineTime.replace(/[-]/g, '').substr(4, 2))
    },
    leftTimer() {
      const leftTime =
        new Date(
          this.year,
          this.month - 1,
          this.day,
          this.hour,
          this.minute,
          this.second
        ) - new Date()
      let days = parseInt(leftTime / 1000 / 60 / 60 / 24, 10)
      let hours = parseInt((leftTime / 1000 / 60 / 60) % 24, 10)
      let minutes = parseInt((leftTime / 1000 / 60) % 60, 10)
      let seconds = parseInt((leftTime / 1000) % 60, 10)
      days = this.checkTxt(days)
      hours = this.checkTime(hours)
      minutes = this.checkTime(minutes)
      seconds = this.checkTime(seconds)
      this.timer =
        days + '天' + hours + '小时' + minutes + '分' + seconds + '秒'
    },
    checkTime(i) {
      if (i < 0) {
        if (Math.abs(i) < 10) {
          i = '0' + Math.abs(i)
        }
      }
      return Math.abs(i)
    },
    checkTxt(j) {
      if (j < 0) {
        this.$refs.bgColor.classList.add('txt-bg')
        this.timeTxt = '已超时：'
        if (Math.abs(j) < 10) {
          j = '0' + Math.abs(j)
        }
      } else if (j >= 0) {
        this.$refs.bgColor.classList.remove('txt-bg')
        this.timeTxt = '倒计时：'
      }
      return Math.abs(j)
    },
  },
}
</script>
<style scoped>
.txt-bg {
  background-color: rgb(251, 229, 230);
  color: rgb(212, 0, 15);
}

.txt {
  float: left;
  font-weight: bolder;
  padding-left: 10%;
}
.bg-color {
  height: 40px;
  line-height: 40px;
  border-radius: 15px;
  background-color: rgb(255, 241, 234);
  color: rgb(255, 121, 52);
}
</style>
