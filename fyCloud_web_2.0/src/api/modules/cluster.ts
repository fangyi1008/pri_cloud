import http from '@/api'
import { Cluster, Host } from '../interface'
import { message } from 'antd'
import { BasicObj } from '@/components/multipleForm/type'
import { operationResultFormat } from '@/utils/util'
import { IOperatResult } from './virtualMachineService'

// export function getDeleteResult(sysLogList: BasicObj[]) {
//   let successNumber = 0
//   let faildNumber = 0
//   sysLogList.forEach((logItem: any) => {
//     const { result } = logItem
//     result === '成功' ? successNumber++ : faildNumber++
//   })
//   if (successNumber > 0 && faildNumber > 0) {
//     message.error(`${faildNumber}项删除失败`)
//     message.success(`${successNumber}项删除成功`)
//   } else if (successNumber > 0) {
//     message.success(`${successNumber}项删除成功`)
//   } else {
//     message.error(`${faildNumber}项删除失败`)
//   }
//   return successNumber
// }
// * 接口
export const getClusterList = async (params: Cluster.IListParam): Promise<Cluster.IListData> => {
  try {
    const result: Cluster.IListApiResult = await http.post('/htcloud/cluster/list', params, {
      headers: { noLoading: true },
    })
    const { clusterList = [], totalCount } = result || { page: {} }
    const { page, limit } = params
    const newList = clusterList
      ? clusterList.map((item: Cluster.IClusterType) => {
          const { drsSwitch, haSwitch } = item || {}
          return {
            ...item,
            drsSwitch: drsSwitch ? '开启' : '关闭',
            haSwitch: haSwitch ? '开启' : '关闭',
          }
        })
      : []
    return {
      currPage: Number(page),
      pageSize: Number(limit),
      totalCount: totalCount,
      list: newList,
    }
  } catch (err) {
    message.error('集群中心列表获取失败')
    return {
      list: [],
      currPage: 1,
      pageSize: 10,
      totalCount: 0,
      totalPage: 0,
    }
  }
}

export const saveAsyncClusterData = async (params: BasicObj) => {
  try {
    const result: { msg: string; code: number } = await http.post('/htcloud/cluster/save', params)
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('新增集群成功')
    }
    return result
  } catch (err) {
    return {}
  }
}

export const editAsyncClusterData = async (params: BasicObj) => {
  try {
    const result: { msg: string; code: number } = await http.post('/htcloud/cluster/update', params)
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('编辑集群成功')
    }
    return result
  } catch (err) {
    return undefined
  }
}

export const deleteAsyncClusterData = async (
  params: Record<string, any>[] | (string | number)[],
) => {
  try {
    const result = await http.post<IOperatResult>('/htcloud/cluster/delete', params, {
      headers: {
        noLoading: true,
        noMessage: true,
      },
    })
    const { code, msg, sysLogList = [] } = result
    if (code === 0 && msg === 'success' && sysLogList.length > 0) {
      return operationResultFormat(sysLogList)
    }
    message.error('删除集群失败:' + msg)
    return { flag: false, errorList: [], successList: [] }
  } catch (err) {
    message.error('删除集群失败')
    return { flag: false, errorList: [], successList: [] }
  }
}

export const clusterDataInfo = async (
  id: string,
): Promise<{
  cluster?: Cluster.IClusterType
  host?: Host.IHostType
}> => {
  try {
    const result = await http.get<{
      code: number
      msg: string
      cluster: Cluster.IClusterType
      host?: Host.IHostType
    }>(`/htcloud/cluster/info/${id}`, undefined, {
      headers: { noLoading: true },
    })
    const { code, msg, host, cluster } = result
    if (code === 0 && msg === 'success') {
      return {
        cluster,
        host,
      }
    } else {
      message.error(msg)
      return {
        cluster: undefined,
        host: undefined,
      }
    }
  } catch (err) {
    return {
      cluster: undefined,
      host: undefined,
    }
  }
}
