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
export function getOneWord(uuid, index) {
  return request({
    url: '/system/word/getOneWord/'+uuid+"/"+index,
    method: 'get'
  })
}

// 随机获取一个单词
export function getOneWordInCollection(uuid, index) {
  return request({
    url: '/system/word/getOneWordInCollection/'+uuid+"/"+index,
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
// 搜索单词
export function searchWord(query) {
  return request({
    url: '/system/word/searchWord',
    method: 'get',
    params: query
  })
}

// 搜索单词
export function getWordInfo(query) {
  return request({
    url: '/system/word/getWordInfo',
    method: 'get',
    params: query
  })
}
