import request from '@/utils/request'

// 查询用户学习记录列表
export function getStatistics() {
  return request({
    url: '/statistics/getStatisticsOfCount',
    method: 'get',
  })
}

// 查询每月的学习记录
export function getStudyRecordOfMonth() {
  return request({
    url: '/statistics/getStudyRecordOfMonth',
    method: 'get',
  })
}

// 查询每月的学习单词数量
export function getUserWordOfMonth() {
  return request({
    url: '/statistics/getUserWordOfMonth',
    method: 'get',
  })
}

// 查询单词的阶段数量
export function getUserWordPeriodCount() {
  return request({
    url: '/statistics/getUserWordPeriodCount',
    method: 'get',
  })
}

// 查询当月每天的期望值与实际值
export function getExceptAndActualValue() {
  return request({
    url: '/statistics/getExceptAndActualValue',
    method: 'get',
  })
}

// 查询用户总单词数，及未学习的新单词数
export function getTotalAndNotStudyNum() {
  return request({
    url: '/statistics/getTotalAndNotStudyNum',
    method: 'get',
  })
}

// 查询用户今日需复习单词总数、已复习单词数
export function getNeedReviewAnHaveReviewNum() {
  return request({
    url: '/statistics/getNeedReviewAnHaveReviewNum',
    method: 'get',
  })
}
