<template>
  <div :class="className" :style="{height:height,width:width}"></div>
</template>

<script>
import * as echarts from 'echarts'

require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import {getExceptAndActualValue} from '@/api/statistics/statistics'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: false
    },
    chartData: {
      type: Object,
      required: false
    }
  },
  data() {
    return {
      chart: null,
      actualData: [],
      exceptData: [],
      xAisData: [],
    }
  },
  created() {
    this.$watch(
      () => this.$route.params,
      () => {
        this.getShowData();
      },
      // 组件创建完后获取数据，
      // 此时 data 已经被 observed 了
      {immediate: true}
    )
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
    },
    setOptions() {
      this.chart.setOption({
        title: {
          text: '学习曲线'
        },
        xAxis: {
          data: this.xAisData,
          boundaryGap: false,
          axisTick: {
            show: false
          }
        },
        grid: {
          left: 10,
          right: 10,
          bottom: 20,
          top: 50,
          containLabel: true
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          },
          padding: [5, 8]
        },
        yAxis: {
          axisTick: {
            show: false
          }
        },
        legend: {
          data: ['expected', 'actual']
        },
        series: [{
          name: 'expected', itemStyle: {
            normal: {
              color: '#FF005A',
              lineStyle: {
                color: '#FF005A',
                width: 2
              }
            }
          },
          smooth: true,
          type: 'line',
          data: this.exceptData,
          animationDuration: 5000,
          animationEasing: 'cubicInOut'
        },
          {
            name: 'actual',
            smooth: true,
            type: 'line',
            itemStyle: {
              normal: {
                color: '#3888fa',
                lineStyle: {
                  color: '#3888fa',
                  width: 2
                },
                areaStyle: {
                  color: '#f3f8ff'
                }
              }
            },
            data: this.actualData,
            animationDuration: 5000,
            animationEasing: 'quadraticOut'
          }]
      })
    },
    async getShowData() {
      await getExceptAndActualValue().then(res => {
        console.log(res.data)
        this.exceptData = res.data.except;
        this.actualData = res.data.actual;
        this.xAisData = [];
        for (let i = 1; i <= this.exceptData.length; i++) {
          this.xAisData.push(i);
        }
      })
      this.setOptions();
      var self = this;
      this.chart.on('click', {
        seriesName: 'actual'
      }, (params) => {
        var day = params.dataIndex + 1;
        console.log(day)
        this.$emit("showDataDialog", day)
      })
    }
  }
}
</script>
