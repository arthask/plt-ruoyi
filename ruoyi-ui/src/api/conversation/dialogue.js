import request from '@/utils/request'

// 查询对话列表
export function listDialogue(query) {
  return request({
    url: '/dialogue/list',
    method: 'get',
    params: query
  })
}

// 查询对话详细
export function getDialogue(id) {
  return request({
    url: '/dialogue/' + id,
    method: 'get'
  })
}

// 新增对话
export function addDialogue(data) {
  return request({
    url: '/dialogue',
    method: 'post',
    data: data
  })
}

// 修改对话
export function updateDialogue(data) {
  return request({
    url: '/dialogue',
    method: 'put',
    data: data
  })
}

// 删除对话
export function delDialogue(id) {
  return request({
    url: '/dialogue/' + id,
    method: 'delete'
  })
}
