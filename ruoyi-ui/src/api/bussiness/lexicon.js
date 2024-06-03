import request from '@/utils/request'

// 查询词库列表
export function listLexicon(query) {
  return request({
    url: '/system/lexicon/list',
    method: 'get',
    params: query
  })
}

// 查询词库详细
export function getLexicon(id) {
  return request({
    url: '/system/lexicon/' + id,
    method: 'get'
  })
}

// 新增词库
export function addLexicon(data) {
  return request({
    url: '/system/lexicon',
    method: 'post',
    data: data
  })
}

// 修改词库
export function updateLexicon(data) {
  return request({
    url: '/system/lexicon',
    method: 'put',
    data: data
  })
}

// 删除词库
export function delLexicon(id) {
  return request({
    url: '/system/lexicon/' + id,
    method: 'delete'
  })
}
