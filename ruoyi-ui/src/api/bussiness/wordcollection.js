import request from '@/utils/request'

// 查询列表
export function listWordCollection(query) {
  return request({
    url: '/wordCollection/list',
    method: 'get',
    params: query
  })
}

// 查询单条
export function getWordCollection(query) {
  return request({
    url: '/wordCollection/getInfo',
    method: 'get',
    params: query
  })
}

// 更新
export function updateWordCollection(data) {
  return request({
    url: '/wordCollection/updateWordCollectionData',
    method: 'post',
    data: data
  })
}

// 查询列表
export function getWordsOfCollection(query) {
  return request({
    url: '/wordCollection/getWordsOfCollection',
    method: 'get',
    params: query
  })
}

export function getAllLabels(query) {
  return request({
    url: '/wordCollection/getAllLabels',
    method: 'get',
    params: query
  })
}

export function addWordToCollection(data) {
  return request({
    url: '/wordCollection/addWordToCollection',
    method: 'post',
    data: data
  })
}

export function getCollectionsOfPackage(query) {
  return request({
    url: '/wordCollection/getCollectionsOfPackage',
    method: 'get',
    params: query
  })
}


export function removeWordOfCollection(data) {
  return request({
    url: '/wordCollection/removeWordOfCollection',
    method: 'post',
    data: data
  })
}
