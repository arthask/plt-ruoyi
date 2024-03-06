import request from '@/utils/request'


// 获取对话回复
export function getReplayInfo(index) {
  return request({
    url: '/conversation/getReplayInfo/' + index,
    method: 'get'
  })
}


