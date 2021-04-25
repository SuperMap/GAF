function setSkylineChart(object) {
  const chartOptins = {
    title: {
      text: '二维天际线',
      show: false,
    },
    tooltip: {
      trigger: 'axis',
      textStyle: {
        fontSize: '12px',
      },
      borderWidth: 1,
      borderColor: '#1aac75',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#6a7985',
        },
      },
    },
    calculable: true,
    legend: {
      show: false,
    },
    grid: {
      left: '30px',
      right: '20px',
      top: '20px',
      bottom: '30px',
    },
    xAxis: [
      {
        type: 'category',
        boundaryGap: false,
        data: object.x,
        show: false,
        // axisLabel: {
        //   show: true,
        //   textStyle: {
        //     fontFamily: 'arial',
        //     color: '#fff', // 更改坐标轴文字颜色
        //     fontSize: 12 // 更改坐标轴文字大小
        //   }
        // },
        // axisLine: {
        //   lineStyle: {
        //     color: 'rgba(220, 220, 220, 0.8)' // 更改坐标轴颜色
        //   }
        // },
        // splitLine: {
        //   show: true,
        //   lineStyle: {
        //     color: ['rgba(220, 220, 220, 0.1)'],
        //     width: 1,
        //     type: 'solid'
        //   }
        // }
      },
    ],
    yAxis: [
      {
        type: 'value',
        axisLabel: {
          show: true,
          textStyle: {
            fontFamily: 'arial',
            color: '#fff', // 更改坐标轴文字颜色
            fontSize: 12, // 更改坐标轴文字大小
          },
        },
        axisLine: {
          lineStyle: {
            color: 'rgba(220, 220, 220, 0.8)', // 更改坐标轴颜色
          },
        },
        splitLine: {
          show: true,
          lineStyle: {
            color: ['rgba(220, 220, 220, 0.8)'],
            width: 1,
            type: 'solid',
          },
        },
      },
    ],
    color: [
      '#069c97',
      '#2499f9',
      '#e2a042',
      '#eb5060',
      '#ff4534',
      '#1aac75',
      '#9b14c7',
    ],
    series: [
      {
        name: '',
        type: 'line',
        data: object.y,
      },
    ],
    toolbox: {
      right: 10,
      top: -10,
      iconStyle: {
        borderColor: '#fff',
      },
      feature: {
        saveAsImage: {
          pixelRatio: 2,
        },
      },
    },
  }

  return chartOptins
}
export default setSkylineChart
