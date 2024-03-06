import request from '@/utils/request'

// 获取复习单词
export function getReviewWord() {
  return request({
    url: '/review/getReviewWord',
    method: 'get',
  })
}

// 获取复习单词
export function getReviewWordByIndex(index) {
  return request({
    url: '/review/getReviewWordByIndex/'+index,
    method: 'get',
  })
}
