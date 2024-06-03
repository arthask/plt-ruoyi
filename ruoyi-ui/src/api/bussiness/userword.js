import request from '@/utils/request'

// 查询用户单词列表
export function listWord(query) {
  return request({
    url: '/system/userword/list',
    method: 'get',
    params: query
  })
}

// 查询用户单词详细
export function getWord(id) {
  return request({
    url: '/system/userword/' + id,
    method: 'get'
  })
}

// 新增用户单词
export function addUserWord(data) {
  return request({
    url: '/system/userword',
    method: 'post',
    data: data
  })
}

// 修改用户单词
export function updateWord(data) {
  return request({
    url: '/system/userword',
    method: 'put',
    data: data
  })
}

// 删除用户单词
export function delWord(id) {
  return request({
    url: '/system/userword/' + id,
    method: 'delete'
  })
}

export function collect(query) {
  return request({
    url: '/system/userword/collect',
    method: 'post',
    data: query
  })
}
