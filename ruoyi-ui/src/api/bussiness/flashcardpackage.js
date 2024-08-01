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
    url: '/flashcardPackage/getPackageInfo/' + id,
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
    url: '/flashcardPackage/delete/' + uuid,
    method: 'delete',
  })
}

export function getPackageList(params) {
  return request({
    url: '/flashcardPackage/getPackageList',
    method: 'get',
    params: params
  })
}


export function addCollectionToPackage(data) {
  return request({
    url: '/wordCollection/addCollectionToPackage',
    method: 'post',
    data: data
  })
}
export function removeCollectionOfPackage(data) {
  return request({
    url: '/wordCollection/removeCollectionOfPackage',
    method: 'post',
    data: data
  })
}
