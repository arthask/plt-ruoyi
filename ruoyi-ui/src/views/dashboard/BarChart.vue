<template>
  <div :class="className" :style="{height:height,width:width}"/>
</template>

<script>
import * as echarts from 'echarts'

require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'
import {getStudyRecordOfMonth} from '@/api/statistics/statistics'
import {getUserWordOfMonth} from '@/api/statistics/statistics'


const animationDuration = 6000

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
      studyRecordCountArray: [],
      userWordCountArray: []
    }
  },
  created() {
    this.$watch(
      () => this.$route.params,
      () => {
        this.getCountOfMonth();
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
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
    },
    setChartOptions: function () {
      this.chart.setOption({
        title: {
          text: '学习记录统计'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          top: 50,
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
          axisTick: {
            alignWithLabel: true
          }
        }],
        yAxis: [{
          type: 'value',
          axisTick: {
            show: false
          }
        }],
        series: [{
            name: '学习记录',
            type: 'bar',
            // stack: 'vistors',
            barWidth: '40%',
            data: this.studyRecordCountArray,
          },
          {
            name: '我的单词',
            type: 'bar',
            // stack: 'vistors',
            barWidth: '40%',
            data: this.userWordCountArray,
          }
        ]
      })
    },
    async getCountOfMonth() {
      await getStudyRecordOfMonth().then(res => {
        this.studyRecordCountArray = res.data;
        console.log("=========" + this.studyRecordCountArray);
      });
      await getUserWordOfMonth().then(res => {
        this.userWordCountArray = res.data;
        console.log("=========" + this.userWordCountArray);
      });
      this.setChartOptions();
    }
  }
}
</script>
