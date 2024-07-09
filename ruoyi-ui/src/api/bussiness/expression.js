import request from '@/utils/request'

// 查询对话场景列表
export function listExpression(query) {
  return request({
    url: '/expression/list',
    method: 'get',
    params: query
  })
}

// 查询对话场景详细
export function getExpression(params) {
  return request({
    url: '/expression/getInfo',
    method: 'get',
    params: params
  })
}


// 删除对话场景
export function delExpression(id) {
  return request({
    url: '/expression/' + id,
    method: 'delete'
  })
}

// 新增对话场景
export function addExpression(data) {
  return request({
    url: '/expression/add',
    method: 'post',
    data: data
  })
}

// 修改对话场景
export function updateExpression(data) {
  return request({
    url: '/expression/update',
    method: 'post',
    data: data
  })
}

