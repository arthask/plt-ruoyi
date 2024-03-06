import request from '@/utils/request'

// 查询对话场景列表
export function listScene(query) {
  return request({
    url: '/system/scene/list',
    method: 'get',
    params: query
  })
}

// 查询对话场景详细
export function getScene(id) {
  return request({
    url: '/system/scene/' + id,
    method: 'get'
  })
}

// 新增对话场景
export function addScene(data) {
  return request({
    url: '/system/scene',
    method: 'post',
    data: data
  })
}

// 修改对话场景
export function updateScene(data) {
  return request({
    url: '/system/scene',
    method: 'put',
    data: data
  })
}

// 删除对话场景
export function delScene(id) {
  return request({
    url: '/system/scene/' + id,
    method: 'delete'
  })
}

// 新增对话场景
export function addDialogueScene(data) {
  return request({
    url: '/system/scene/addDialogueScene',
    method: 'post',
    data: data
  })
}

// 查询对话场景详细
export function getReplayInfo(query) {
  return request({
    url: '/system/scene/getReplayInfo',
    method: 'get',
    params: query
  })
}
