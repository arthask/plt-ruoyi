import request from '@/utils/request'

// 查询单词列表
export function getSentence(query) {
  return request({
    url: '/pltool/wordSentence/getSentence',
    method: 'get',
    params: query
  })
}

// 新增
export function addSentenceOfWord(data) {
  return request({
    url: '/pltool/wordSentence/addSentenceOfWord',
    method: 'post',
    data: data
  })
}

// 修改
export function editSentenceOfWord(data) {
  return request({
    url: '/pltool/wordSentence/editSentenceOfWord',
    method: 'post',
    data: data
  })
}
