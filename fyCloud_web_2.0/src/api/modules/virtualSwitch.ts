import http from '@/api'
import { BasicObj } from '@/components/showInfoCard/type'
import { operationResultFormat } from '@/utils/util'
import { message } from 'antd'
import { IBaseResult, PortFlux, VmSwitch } from '../interface'
import { IOperatResult } from './virtualMachineService'

export const catchError = (error: string) => {
  message.error(`${error}`)
}

export const getVmSwitchList = async (params: VmSwitch.IListParam): Promise<VmSwitch.IListData> => {
  try {
    const result: VmSwitch.IListApiResult = await http.post('/htcloud/vmSwitch/list', params, {
      headers: { noLoading: true },
    })
    const { page } = result || { page: {} }
    const { list = [], currPage = 1, pageSize = 10, totalCount = 0, totalPage = 0 } = page || {}
    return {
      currPage: currPage,
      pageSize: pageSize,
      totalCount: totalCount,
      totalPage: totalPage,
      list: list,
    }
  } catch (err) {
    message.error('虚拟交换机列表获取失败')
    return {
      list: [],
      currPage: 1,
      pageSize: 10,
      totalCount: 0,
      totalPage: 0,
    }
  }
}

export const getPortFluxList = async (params: string): Promise<PortFlux.IPortFluxhData> => {
  try {
    const result: PortFlux.IListApiResult = await http.post('/htcloud/vmSwitch/portFlux', params, {
      headers: { noLoading: true, 'content-type': 'application/json;charset=UTF-8' },
    })
    const { code, msg, portFluxList } = result

    if (code === 0 && msg === 'success' && portFluxList) {
      const result = JSON.parse(portFluxList.replaceAll("'", ''))
      return {
        list: result,
      }
    }
    return { list: [] }
  } catch (err) {
    message.error('虚拟交换端口数据获取失败')
    return {
      list: [],
    }
  }
}

export const saveVirtualSwitchData = async (params: BasicObj): Promise<{ flag: boolean }> => {
  try {
    const result = await http.post<IBaseResult>('/htcloud/vmSwitch/save', params, {
      headers: { noMessage: true },
    })
    const { msg, code } = result || {}
    if (msg === 'success' && code === 0) {
      return { flag: true }
    }
    message.error('新增虚拟交换机失败:' + msg)
    return { flag: false }
  } catch (err: any) {
    message.error('新增虚拟交换机失败:' + err.msg)
    return { flag: false }
  }
}

export const getVirtualSwitchInfo = async (
  vmSwitchId: string,
): Promise<{ flag: boolean; info?: VmSwitch.IVmSwitchType }> => {
  try {
    const result: VmSwitch.IGetVmSwitchResult = await http.get(
      `/htcloud/vmSwitch/info/${vmSwitchId}`,
      {},
      {
        headers: { noMessage: true },
      },
    )
    const { msg, code, vmSwitch } = result
    console.log('result===>', result)
    if (msg === 'success' && code === 0) {
      return { flag: true, info: vmSwitch }
    }
    message.error('获取虚拟交换机信息失败:' + msg)
    return { flag: false }
  } catch (err: any) {
    message.error('获取虚拟交换机信息失败:' + err.msg)
    return { flag: false }
  }
}

export const editVirtualSwitchData = async (params: BasicObj) => {
  try {
    const result: { msg: string; code: number } = await http.post(
      '/htcloud/vmSwitch/update',
      params,
    )
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      return { flag: true }
    }
    message.error('编辑虚拟交换机失败:' + msg)
    return { flag: false }
  } catch (err) {
    message.error('编辑虚拟交换机失败')
    return { flag: false }
  }
}

export const deleteVirtualSwitchData = async (params: (string | number)[]) => {
  try {
    const result: IOperatResult = await http.post('/htcloud/vmSwitch/delete', params, {
      headers: { noMessage: true },
    })
    const { code, msg, sysLogList = [] } = result
    if (code === 0 && msg === 'success' && sysLogList.length > 0) {
      return operationResultFormat(sysLogList)
    }
    message.error('删除虚拟交换机失败:' + msg)
    return { flag: false, errorList: [], successList: [] }
  } catch (err: any) {
    message.error('删除虚拟交换机失败:' + err.msg)
    return { flag: false, errorList: [], successList: [] }
  }
}
