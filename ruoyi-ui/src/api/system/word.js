import request from '@/utils/request'

// 查询单词列表
export function listWord(query) {
  return request({
    url: '/system/word/list',
    method: 'get',
    params: query
  })
}

// 查询单词详细
export function getWord(id) {
  return request({
    url: '/system/word/' + id,
    method: 'get'
  })
}

// 新增单词
export function addWord(data) {
  return request({
    url: '/system/word',
    method: 'post',
    data: data
  })
}

// 修改单词
export function updateWord(data) {
  return request({
    url: '/system/word',
    method: 'put',
    data: data
  })
}

// 删除单词
export function delWord(id) {
  return request({
    url: '/system/word/' + id,
    method: 'delete'
  })
}

// 导入单词
export function importTemplate(id) {
  return request({
    url: '/system/word/' + id,
    method: 'delete'
  })
}

// 随机获取一个单词
export function getOneWord(index) {
  return request({
    url: '/system/word/getOneWord/'+index,
    method: 'get'
  })
}

// 搜索单词
export function searchWordByCN(query) {
  return request({
    url: '/system/word/searchWordByCN',
    method: 'get',
    params: query
  })
}
