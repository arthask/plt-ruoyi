import request from '@/utils/request'

// 查询对话场景列表
export function listScene(query) {
  return request({
    url: '/dialogueScene/list',
    method: 'get',
    params: query
  })
}

// 查询对话场景详细
export function getScene(params) {
  return request({
    url: '/dialogueScene/getDialogueSceneInfo',
    method: 'get',
    params: params
  })
}

// 修改对话场景
export function updateScene(data) {
  return request({
    url: '/dialogueScene/updateDialogueScene',
    method: 'post',
    data: data
  })
}

// 删除对话场景
export function delScene(id) {
  return request({
    url: '/dialogueScene/' + id,
    method: 'delete'
  })
}

// 新增对话场景
export function addDialogueScene(data) {
  return request({
    url: '/dialogueScene/addDialogueScene',
    method: 'post',
    data: data
  })
}

