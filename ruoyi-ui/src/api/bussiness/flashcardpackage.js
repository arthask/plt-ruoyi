import request from '@/utils/request'

export function listPackages(query) {
  return request({
    url: '/flashcardPackage/list',
    method: 'get',
    params: query
  })
}

export function getPackageInfo(id) {
  return request({
    url: '/flashcardPackage/getPackageInfo' + id,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/flashcardPackage/add',
    method: 'post',
    data: data
  })
}

export function update(data) {
  return request({
    url: '/flashcardPackage/update',
    method: 'post',
    data: data
  })
}

export function del(uuid) {
  return request({
    url: '/flashcardPackage/delete/'+uuid,
    method: 'delete',
  })
}
