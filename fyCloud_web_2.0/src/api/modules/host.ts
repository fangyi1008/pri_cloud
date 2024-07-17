import http from '@/api'
import { Host } from '../interface'
import { message } from 'antd'
import { BasicObj } from '@/components/multipleForm/type'
import { operationResultFormat } from '@/utils/util'

// * 接口
export const getHostList = async (params: Host.IListParam): Promise<Host.IListData> => {
  try {
    const result: Host.IListApiResult = await http.post('/htcloud/host/list', params, {
      headers: { noLoading: true, noMessage: true },
    })
    const { hostList = [], totalCount } = result || {}
    const { limit, page } = params
    return {
      currPage: Number(page),
      pageSize: Number(limit),
      totalCount: totalCount,
      list: hostList,
    }
  } catch (err: any) {
    message.error('获取主机列表失败: ' + err.message)
    return {
      list: [],
      currPage: 1,
      pageSize: 10,
      totalCount: 0,
      totalPage: 0,
    }
  }
}

export const hostDataInfo = async (id: string) => {
  try {
    const result = await http.get<{ host: Host.IHostType; nodeInfo: any }>(
      `/htcloud/host/info/${id}`,
      {},
      {
        headers: { noLoading: true, noMessage: true },
      },
    )
    return result
  } catch (err) {
    return undefined
  }
}

export const saveAsyncHostData = async (params: BasicObj) => {
  try {
    const result: { msg: string; code: number } = await http.post('/htcloud/host/save', params)
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('新增主机成功')
    }
    return result
  } catch (err) {
    return {}
  }
}

export const editAsyncHostData = async (params: BasicObj) => {
  try {
    const result: { msg: string; code: number } = await http.post('/htcloud/host/update', params)
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('编辑主机成功')
    }
    return result
  } catch (err) {
    return undefined
  }
}

export const deleteAsyncHostData = async (params: Record<string, any>[] | (string | number)[]) => {
  try {
    const result: { msg: string; code: number; sysLogList: BasicObj[] } = await http.post(
      '/htcloud/host/delete',
      params,
      {
        headers: {
          noLoading: true,
          noMessage: true,
        },
      },
    )
    const { code, msg, sysLogList = [] } = result
    if (code === 0 && msg === 'success' && sysLogList.length > 0) {
      return operationResultFormat(sysLogList)
    }
    message.error('删除主机失败:' + msg)
    return { flag: false, errorList: [], successList: [] }
  } catch (err) {
    message.error('删除主机失败')
    return { flag: false, errorList: [], successList: [] }
  }
}

export const inMaintenance = async (params: { hostId: string; moveFlag: boolean }) => {
  try {
    const result: { msg: string; code: number } = await http.post(
      '/htcloud/host/inMaintenance',
      params,
    )
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('进入维护模式成功')
    }
    return result
  } catch (err) {
    return {}
  }
}

export const outMaintenance = async (params: { hostId: string }) => {
  try {
    const result: { msg: string; code: number } = await http.post(
      '/htcloud/host/outMaintenance',
      params,
    )
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('退出维护模式成功')
    }
    return result
  } catch (err) {
    return {}
  }
}

export const reboot = async (params: { hostId: string }) => {
  try {
    const result: { msg: string; code: number } = await http.post('/htcloud/host/reboot', params, {
      headers: { noLoading: true },
    })
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('重启成功')
    }
    return result
  } catch (err) {
    return {}
  }
}

export const hostStartUp = async (params: Host.startUpHost) => {
  try {
    const result: { msg: string; code: number } = await http.post('/htcloud/host/startUp', params, {
      headers: { noLoading: true },
    })
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('开机成功')
    }
    return result
  } catch (err) {
    return {}
  }
}

export const turnOff = async (params: { hostId: string }) => {
  try {
    const result: { msg: string; code: number } = await http.post('/htcloud/host/turnOff', params, {
      headers: { noLoading: true },
    })
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('关机成功')
    }
    return result
  } catch (err) {
    return {}
  }
}
