import request from '@/utils/request'

export function listCards(query) {
  return request({
    url: '/flashcard/list',
    method: 'get',
    params: query
  })
}

export function getCardInfo(id) {
  return request({
    url: '/flashcard/getCardInfo/' + id,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/flashcard/add',
    method: 'post',
    data: data
  })
}

export function update(data) {
  return request({
    url: '/flashcard/update',
    method: 'post',
    data: data
  })
}

export function del(uuid) {
  return request({
    url: '/flashcard/delete/'+uuid,
    method: 'delete',
  })
}

export function getCardOfPackage(params) {
  return request({
    url: '/flashcard/getCardOfPackage',
    method: 'get',
    params: params
  })
}

export function studyCard(params) {
  return request({
    url: '/flashcardAttribute/studyCard',
    method: 'post',
    data: params
  })
}

export function getClassifyCount(params) {
  return request({
    url: '/flashcardAttribute/getClassifyCount',
    method: 'get',
    params: params
  })
}

export function searchClassifyCard(params) {
  return request({
    url: '/flashcard/searchClassifyCard',
    method: 'get',
    params: params
  })
}

export function getPackageInfoOfCard(params) {
  return request({
    url: '/flashcard/getPackageInfoOfCard',
    method: 'get',
    params: params
  })
}
export function addCardsToPackage(data) {
  return request({
    url: '/flashcard/addCardsToPackage',
    method: 'post',
    data: data
  })
}

export function batchAddCard(data) {
  return request({
    url: '/flashcard/batchAddCard',
    method: 'post',
    data: data
  })
}

export function getCardListInPackage(params) {
  return request({
    url: '/flashcard/getCardListInPackage',
    method: 'get',
    params: params
  })
}

export function cancelAssociation2Package(data) {
  return request({
    url: '/flashcard/cancelAssociation2Package',
    method: 'post',
    data: data
  })
}

