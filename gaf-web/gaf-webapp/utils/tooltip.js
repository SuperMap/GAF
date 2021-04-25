function createTooltip(frameDiv) {
  const Tooltip = function (frameDiv) {
    const div = document.createElement('DIV')
    div.className = 'twipsy right'

    const arrow = document.createElement('DIV')
    arrow.className = 'twipsy-arrow'
    div.appendChild(arrow)

    const title = document.createElement('DIV')
    title.className = 'twipsy-inner'
    div.appendChild(title)

    this._div = div
    this._title = title
    this.message = ''

    // add to frame div and display coordinates
    frameDiv.appendChild(div)
    const that = this
    div.onmousemove = function (evt) {
      that.showAt({ x: evt.clientX, y: evt.clientY }, that.message)
    }
  }

  Tooltip.prototype.setVisible = function (visible) {
    this._div.style.display = visible ? 'block' : 'none'
  }

  Tooltip.prototype.showAt = function (position, message) {
    if (position && message) {
      this.setVisible(true)
      this._title.innerHTML = message
      this._div.style.left = position.x + 10 + 'px'
      this._div.style.top = position.y - this._div.clientHeight / 2 + 'px'
      this.message = message
    }
  }

  return new Tooltip(frameDiv)
}

export { createTooltip }
