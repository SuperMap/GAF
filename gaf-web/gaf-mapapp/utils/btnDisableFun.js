const btnDisableFun = () => {
  this.btnDisable = true
  setTimeout(() => {
    this.btnDisable = false // 点击一次时隔两秒后才能再次点击
  }, 2000)
}
export { btnDisableFun }
