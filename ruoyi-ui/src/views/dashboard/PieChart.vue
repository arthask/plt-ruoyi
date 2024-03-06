<template>
  <div :class="className" :style="{height:height,width:width}"/>
</template>

<script>
import * as echarts from 'echarts'

require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import { getUserWordPeriodCount } from '@/api/statistics/statistics'


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
      default: '300px'
    }
  },
  data() {
    return {
      chart: null,
      statisticData: []
    }
  },
  created() {
    this.$watch(
      () => this.$route.params,
      () => {
        this.getPeriodCount()
      },
      // 组件创建完后获取数据，
      // 此时 data 已经被 observed 了
      { immediate: true }
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
    setChartOptions() {
      this.chart.setOption({
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          left: 'center',
          data: ['应用', '掌握', '熟悉', '了解', '新学习']
        },
        series: [
          {
            name: '各阶段单词占比',
            type: 'pie',
            radius: [15, 95],
            data: this.statisticData,
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ]
      })
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
    },
    async getPeriodCount() {
      await getUserWordPeriodCount().then(res => {
        this.statisticData = res.data
        this.setChartOptions()
      })
    }
  }
}
</script>
