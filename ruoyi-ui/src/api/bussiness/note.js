import request from '@/utils/request'

// 查询笔记列表
export function listNote(query) {
  return request({
    url: '/system/note/list',
    method: 'get',
    params: query
  })
}

// 查询笔记详细
export function getNote(id) {
  return request({
    url: '/system/note/' + id,
    method: 'get'
  })
}

// 新增笔记
export function addNote(data) {
  return request({
    url: '/system/note/addNote',
    method: 'post',
    data: data
  })
}

// 修改笔记
export function updateNote(data) {
  return request({
    url: '/system/note',
    method: 'put',
    data: data
  })
}

// 删除笔记
export function delNote(id) {
  return request({
    url: '/system/note/' + id,
    method: 'delete'
  })
}

// 获取笔记详情
export function getNoteInfo(id) {
  return request({
    url: '/system/note/getNoteInfo/' + id,
    method: 'get'
  })
}

// 更新笔记
export function updateNoteInfo(data) {
  return request({
    url: '/system/note/updateNoteInfo',
    method: 'post',
    data: data
  })
}
