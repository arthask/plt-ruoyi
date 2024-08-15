import request from '@/utils/request'

// 查询标签列表
export function listLabel(query) {
  return request({
    url: '/system/label/list',
    method: 'get',
    params: query
  })
}

// 新增标签
export function addLabel(data) {
  return request({
    url: '/system/label/add',
    method: 'post',
    data: data
  })
}

// 修改标签
export function updateLabel(data) {
  return request({
    url: '/system/label/update',
    method: 'post',
    data: data
  })
}

// 获取标签详情
export function getInfo(query) {
  return request({
    url: '/system/label/getInfo',
    method: 'get',
    params: query
  })
}
