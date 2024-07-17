import http from '@/api'
import { message } from 'antd'
import { NetworkCard } from '../interface'

export const catchError = (error: string) => {
  message.error(`${error}`)
}

export const getPhysicalNetworkList = async (hostId: any): Promise<NetworkCard.IListData> => {
  try {
    const result: NetworkCard.IListApiResult = await http.get(
      `/htcloud/network/netMachineInfo/${hostId}`,
      {},
      { headers: { noMessage: true } },
    )
    const { code, msg, networkList } = result
    if (code === 0 && msg === 'success' && networkList) {
      const result = JSON.parse(
        networkList.replaceAll("'", ''),
      ) as never as NetworkCard.INetworkCardType[]
      return {
        list: result.map(item => {
          const { speed } = item
          return { ...item, speed: speed === '-1' ? '' : `${speed}Mb/s` }
        }),
      }
    }
    message.error(`${msg}`)
    return {
      list: [],
    }
  } catch (e: any) {
    catchError(`物理网卡列表获取失败`)
    return {
      list: [],
    }
  }
}

export const getVmSwitchPhysicalNetworkList = async (hostId: string): Promise<string[]> => {
  try {
    const result: NetworkCard.ISWListApiResult = await http.get(
      `/htcloud/vmSwitch/netMachineInfo/${hostId}`,
      {},
      { headers: { noMessage: true } },
    )
    const { code, msg, list = [] } = result
    if (code === 0 && msg === 'success' && list) {
      return list
    }
    message.error(`${msg}`)
    return []
  } catch (e: any) {
    catchError(`物理网卡列表获取失败`)
    return []
  }
}
