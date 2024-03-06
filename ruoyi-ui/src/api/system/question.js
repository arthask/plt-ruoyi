import request from '@/utils/request'

// 查询问题列表
export function listQuestion(query) {
  return request({
    url: '/system/question/list',
    method: 'get',
    params: query
  })
}

// 查询问题详细
export function getQuestion(id) {
  return request({
    url: '/system/question/' + id,
    method: 'get'
  })
}

// 新增问题
export function addQuestion(data) {
  return request({
    url: '/system/question',
    method: 'post',
    data: data
  })
}

// 修改问题
export function updateQuestion(data) {
  return request({
    url: '/system/question',
    method: 'put',
    data: data
  })
}

// 删除问题
export function delQuestion(id) {
  return request({
    url: '/system/question/' + id,
    method: 'delete'
  })
}

// 修改笔记
export function updateNoteInfo(data) {
  return request({
    url: '/system/question/updateNoteInfo',
    method: 'post',
    data: data
  })
}
