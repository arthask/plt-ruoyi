import request from '@/utils/request'

// 查询用户学习记录列表
export function listRecord(query) {
  return request({
    url: '/system/record/list',
    method: 'get',
    params: query
  })
}

// 查询用户学习记录详细
export function getRecord(id) {
  return request({
    url: '/system/record/' + id,
    method: 'get'
  })
}

// 新增用户学习记录
export function addRecord(data) {
  return request({
    url: '/system/record',
    method: 'post',
    data: data
  })
}

// 修改用户学习记录
export function updateRecord(data) {
  return request({
    url: '/system/record',
    method: 'put',
    data: data
  })
}

// 删除用户学习记录
export function delRecord(id) {
  return request({
    url: '/system/record/' + id,
    method: 'delete'
  })
}

export function listWordOfDay(day) {
  return request({
    url: '/system/record/listWordOfDay/'+day,
    method: 'get'
  })
}

export function getStudyRecordsOfWord(query) {
  return request({
    url: '/system/record/getStudyRecordsOfWord',
    method: 'get',
    params: query
  })
}
