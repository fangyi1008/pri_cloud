import http from '@/api'
import { operationResultFormat } from '@/utils/util'
import { message } from 'antd'
import { ITaskType, StoragePool, Vm } from '../interface'

export const catchError = (error: string) => {
  message.error(`${error}`)
}

export interface PageType {
  page?: string
  limit?: string
}
export interface GetVirtualSwitchDataParam extends PageType {
  hostId?: string
}

export interface IDataList<T = any> {
  list: T[]
  totalCount: number
  pageSize: number
  currPage: number
}

export interface IResult<T = any> {
  code: number
  page: IDataList<T>
  storagePoolList?: any[]
}

export const getVirtualSwitchData = async (param: GetVirtualSwitchDataParam) => {
  try {
    const result = await http.post<IResult>(`/htcloud/vmSwitch/list`, param, {
      headers: { noLoading: true },
    })
    const { code, page } = result
    if (code === 0 && page) {
      return page
    }
  } catch (e: any) {
    catchError(`获取数据失败:${e.msg}`)
  }
}

export interface GetSecurityGroupsDataParam extends PageType {
  [key: string]: any
}

export const getSecurityGroupsData = async (param: GetSecurityGroupsDataParam) => {
  try {
    const result = await http.post<IResult>(`/htcloud/securityGroup/list`, param)
    const { code, page } = result
    if (code === 0 && page) {
      return page
    }
  } catch (e: any) {
    catchError(`获取数据失败:${e.msg}`)
  }
}

export const formatArrayData = (
  originalData: { [key: string]: any }[] = [],
  fieldsToFn: { [key: string]: (value: any) => any },
) => {
  const fieldsToFnEntries = Object.entries(fieldsToFn)
  return originalData.map((item: any) => {
    const temData: { [key: string]: any } = {}
    for (let i = 0; i < fieldsToFnEntries.length; i++) {
      const [key, value] = fieldsToFnEntries[i]
      temData[`${key}Text`] = value(item[key])
    }
    return {
      ...item,
      ...temData,
    }
  })
}

export const splitUnit = (sizeStr: string) => {
  let unit = ''
  const isHas = ['GB', 'BM', 'TB'].some((sizeStr: string) => {
    const flag = sizeStr.indexOf(sizeStr) > -1
    if (flag) {
      unit = sizeStr
    }
    return flag
  })
  const size = sizeStr.replace(unit, '')
  return { unit, size }
}

const formatSize = (sizeStr: string) => {
  if (!sizeStr) return '--'
  const { unit, size } = splitUnit(sizeStr)
  if (unit && size) {
    const sizeNumber = Number(size.match(/^\d+(?:\.\d{0,2})?/))
    return `${size}${unit}`
  }
  return sizeStr
}

export interface GetSelectStorageParam extends PageType {
  poolType?: 'dir' | 'lvm'
  hostId: string
  status?: number
  storagePoolId?: string
}

export const getSelectStorageInfo = async (param: GetSelectStorageParam) => {
  try {
    const result = await http.post<IResult>(`/htcloud/storage/selectStorage`, param, {
      headers: { noLoading: true },
    })
    const { code, page, storagePoolList = [] } = result

    const newStoragePoolList = formatArrayData(storagePoolList, {
      poolType: (poolType: any): string => (poolType === 'dir' ? '本地文件目录' : 'LVM逻辑存储卷'),
      freeSpace: (freeSpace: any) => `实际可用空间 ${formatSize(freeSpace)}`,
    })
    if (code === 0 && page) {
      return { page: { ...page }, storagePoolList: newStoragePoolList }
    }
  } catch (e: any) {
    catchError(`获取数据失败:${e.msg}`)
  }
}

export interface GetStoragePoolListParam extends PageType {
  hostId: string
  status?: number
}

export const storagePoolStatus: { [key: string]: string } = {
  1: '活跃',
  2: '不活跃',
  3: '需要格式化',
  4: '暂停',
}

export const getStoragePoolList = async (
  param: GetStoragePoolListParam,
): Promise<StoragePool.IListData> => {
  try {
    const result = await http.post<StoragePool.IListApiResult>(`/htcloud/storagePool/list`, param, {
      headers: { noLoading: true },
    })
    const { code, totalCount, poolList } = result
    const { page, limit } = param
    if (code === 0 && poolList) {
      const newList = formatArrayData(poolList, {
        poolType: (poolType: any): string =>
          poolType === 'dir' ? '本地文件目录' : 'LVM逻辑存储卷',
        status: (status: any) => storagePoolStatus[status as number] ?? '--',
        capacity: (capacity: any) => formatSize(capacity),
      })
      return {
        totalCount,
        pageSize: Number(limit),
        currPage: Number(page),
        list: newList,
      }
    }
    return {
      totalCount: 0,
      currPage: 1,
      pageSize: 10,
      list: [],
    }
  } catch (e: any) {
    catchError(`获取数据失败:${e.msg}`)
    return {
      totalCount: 0,
      currPage: 1,
      pageSize: 10,
      list: [],
    }
  }
}

export interface IVMRandomMacResult {
  code: number
  mac: string
}
export const getVMRandomMac = async () => {
  try {
    const result = await http.post<IVMRandomMacResult>(
      `/htcloud/vm/randomMac`,
      {},
      { headers: { noLoading: true } },
    )
    const { code, mac } = result
    if (code === 0 && mac) {
      return { mac }
    }
  } catch (e: any) {
    catchError(`获取数据失败:${e.msg}`)
  }
}

export interface IBaseResult {
  code: number
  msg: string
}

export const selectBasicVol = async (param: string) => {
  try {
    const result = await http.post<IBaseResult>(`/htcloud/storage/selectBasicVol`, param, {
      headers: {
        'content-type': 'application/json;charset=UTF-8',
        noLoading: true,
        noMessage: true,
      },
    })
    const { code, msg } = result
    if (code === 0) {
      return true
    }
    catchError(msg)
    return false
  } catch (e: any) {
    catchError(`获取数据失败:${e.msg}`)
    return false
  }
}

export const selectCdIso = async (param: string) => {
  try {
    const result = await http.post<IBaseResult>(`/htcloud/storage/selectCdIso`, param, {
      headers: {
        'content-type': 'application/json;charset=UTF-8',
        noLoading: true,
        noMessage: true,
      },
    })
    const { code, msg } = result
    if (code === 0) {
      return true
    }
    catchError(msg)
    return false
  } catch (e: any) {
    catchError(e.msg)
    return false
  }
}

export const saveVirtualMachine = async (param: any) => {
  try {
    const result = await http.post<IBaseResult>(`/htcloud/vm/addVm`, param)
    const { code, msg } = result
    if (code === 0) {
      return true
    }
    catchError(msg)
    return false
  } catch (e: any) {
    catchError(`保存失败:${e.msg}`)
    return false
  }
}

export const updateVirtualMachine = async (param: any) => {
  try {
    const result = await http.post<IBaseResult>(`/htcloud/vm/updateVm`, param, {
      headers: { noLoading: true, noMessage: true },
    })
    const { code, msg } = result
    if (code === 0) {
      return true
    }
    catchError(msg)
    return false
  } catch (e: any) {
    catchError(`修改失败:${e.msg}`)
    return false
  }
}

export interface IOperatResult {
  code: number
  msg: string
  sysLogList: ITaskType[]
}

export const deleteVm = async (param: { vmIds: (string | number)[]; rbFlag: boolean }) => {
  try {
    const result = await http.post<IOperatResult>('/htcloud/vm/deleteVm', param, {
      headers: {
        noLoading: true,
        noMessage: true,
      },
    })
    const { code, msg, sysLogList = [] } = result
    if (code === 0 && msg === 'success' && sysLogList.length > 0) {
      return operationResultFormat(sysLogList)
    }
    return { flag: false, errorList: [], successList: [] }
  } catch (e: any) {
    catchError(`虚拟机删除失败:${e.msg}`)
    return { flag: false, errorList: [], successList: [] }
  }
}

export const deleteDbVm = async (param: (string | number)[]) => {
  try {
    const result = await http.post<IOperatResult>('/htcloud/vm/deleteDbVm', param, {
      headers: { noLoading: true, 'content-type': 'application/json;charset=UTF-8' },
    })
    const { code, msg, sysLogList = [] } = result
    if (code === 0 && msg === 'success') {
      return true
    }
    catchError(msg)
    return false
  } catch (e: any) {
    catchError(`虚拟机删除失败:${e.msg}`)
    return false
  }
}

export const startVm = async (param: (string | number)[]) => {
  try {
    const result = await http.post<IOperatResult>('/htcloud/vm/startVm', param, {
      headers: { noLoading: true, 'content-type': 'application/json;charset=UTF-8' },
    })
    const { code, msg, sysLogList = [] } = result
    if (code === 0 && msg === 'success' && sysLogList.length > 0) {
      return operationResultFormat(sysLogList)
    }
    catchError(msg)
    return { flag: false, errorList: [], successList: [] }
  } catch (e: any) {
    catchError(`虚拟机启动失败:${e.msg}`)
    return { flag: false, errorList: [], successList: [] }
  }
}

export const shutDownVm = async (param: (string | number)[]) => {
  try {
    const result = await http.post<IOperatResult>('/htcloud/vm/shutDownVm', param, {
      headers: { noLoading: true, 'content-type': 'application/json;charset=UTF-8' },
    })
    const { code, msg, sysLogList = [] } = result
    if (code === 0 && msg === 'success' && sysLogList.length > 0) {
      return operationResultFormat(sysLogList)
    }
    catchError(msg)
    return { flag: false, errorList: [], successList: [] }
  } catch (e: any) {
    catchError(`虚拟机关闭失败:${e.msg}`)
    return { flag: false, errorList: [], successList: [] }
  }
}

export const suspendVm = async (param: (string | number)[]) => {
  try {
    const result = await http.post<IOperatResult>('/htcloud/vm/suspendVm', param, {
      headers: { noLoading: true, 'content-type': 'application/json;charset=UTF-8' },
    })
    const { code, msg, sysLogList = [] } = result
    if (code === 0 && msg === 'success' && sysLogList.length > 0) {
      return operationResultFormat(sysLogList)
    }
    catchError(msg)
    return { flag: false, errorList: [], successList: [] }
  } catch (e: any) {
    catchError(`虚拟机暂停失败:${e.msg}`)
    return { flag: false, errorList: [], successList: [] }
  }
}

export const resumeVm = async (param: (string | number)[]) => {
  try {
    const result = await http.post<IOperatResult>('/htcloud/vm/resumeVm', param, {
      headers: { noLoading: true, 'content-type': 'application/json;charset=UTF-8' },
    })
    const { code, msg, sysLogList = [] } = result
    if (code === 0 && msg === 'success' && sysLogList.length > 0) {
      return operationResultFormat(sysLogList)
    }
    catchError(msg)
    return { flag: false, errorList: [], successList: [] }
  } catch (e: any) {
    catchError(`虚拟机恢复失败:${e.msg}`)
    return { flag: false, errorList: [], successList: [] }
  }
}

export const restartVm = async (param: (string | number)[]) => {
  try {
    const result = await http.post<IOperatResult>('/htcloud/vm/restartVm', param, {
      headers: { noLoading: true, 'content-type': 'application/json;charset=UTF-8' },
    })
    const { code, msg, sysLogList = [] } = result
    if (code === 0 && msg === 'success' && sysLogList.length > 0) {
      return operationResultFormat(sysLogList)
    }
    catchError(msg)
    return { flag: false, errorList: [], successList: [] }
  } catch (e: any) {
    catchError(`虚拟机启动失败:${e.msg}`)
    return { flag: false, errorList: [], successList: [] }
  }
}

export const destroyVm = async (param: (string | number)[]) => {
  try {
    const result = await http.post<IOperatResult>('/htcloud/vm/destroyVm', param, {
      headers: { noLoading: true, 'content-type': 'application/json;charset=UTF-8' },
    })
    const { code, msg, sysLogList = [] } = result
    if (code === 0 && msg === 'success' && sysLogList.length > 0) {
      return operationResultFormat(sysLogList)
    }
    catchError(msg)
    return { flag: false, errorList: [], successList: [] }
  } catch (e: any) {
    catchError(`关闭电源失败:${e.msg}`)
    return { flag: false, errorList: [], successList: [] }
  }
}

export const isNotNullArray = (data: any[]): boolean => {
  if (Array.isArray(data)) {
    return data.length > 0
  }
  return false
}

export const transformHost = (hostList: any[]) =>
  hostList.map((clusterItem: any) => {
    const { hostId, hostName, state } = clusterItem
    return {
      key: hostId,
      title: hostName,
      selectable: true,
      state,
      type: 'host',
    }
  })

export const transformCluster = (clusterList: any[]) =>
  clusterList.map((clusterItem: any) => {
    const { clusterId, clusterName, clusterHostList } = clusterItem
    const childrenData = transformHost(clusterHostList)
    return {
      key: clusterId,
      title: clusterName,
      type: 'cluster',
      selectable: false,
      children: childrenData,
    }
  })

export interface IMoveVmListResult {
  code: number
  msg: string
  centerList: any[]
}

export const moveVmList = async () => {
  try {
    const result = await http.post<IMoveVmListResult>(
      '/htcloud/datacenter/moveVmList',
      {},
      { headers: { noLoading: true } },
    )
    const { code, msg, centerList } = result

    if (code === 0 && msg === 'success') {
      const data = centerList.map((centerItem: any) => {
        const {
          dataCenterId,
          dataCenterName,
          centerClusterList = [],
          centerHostList = [],
        } = centerItem
        if (isNotNullArray(centerClusterList) || isNotNullArray(centerHostList)) {
          const clusterData = transformCluster(centerClusterList || [])
          const hostData = transformHost(centerHostList || [])
          return {
            key: dataCenterId,
            title: dataCenterName,
            dataCenterId,
            selectable: false,
            type: 'dataCenter',
            children: [...clusterData, ...hostData],
          }
        }
        return {
          key: dataCenterId,
          title: dataCenterName,
          selectable: false,
          type: 'dataCenter',
        }
      })
      return data
    }
    catchError(msg)
    return []
  } catch (e: any) {
    catchError(`获取迁移列表错误:${e.msg}`)
    return []
  }
}

export const moveVm = async (param: any) => {
  try {
    const result = await http.post<IOperatResult>('/htcloud/vm/moveVm', param, {})
    const { code, msg, sysLogList = [] } = result
    if (code === 0 && msg === 'success' && sysLogList.length > 0) {
      return operationResultFormat(sysLogList)
    }
    catchError(msg)
    return { flag: false, errorList: [], successList: [] }
  } catch (e: any) {
    catchError(`迁移失败:${e.msg}`)
    return { flag: false, errorList: [], successList: [] }
  }
}

export const vncUrl = async (param: string) => {
  try {
    const result = await http.post<IBaseResult>('/htcloud/vm/vncUrl', param, {
      headers: {
        noLoading: true,
        noMessage: true,
        'content-type': 'application/json;charset=UTF-8',
      },
    })
    const { code, msg } = result
    console.log(result)
    if (code === 0) {
      return msg
    }
    catchError(msg)
    return ''
  } catch (e: any) {
    return ''
  }
}

export interface IHostMemoryResult {
  code: number
  nodeInfo: { memory: number }
  msg: string
}

export const getHostMemory = async (hostId: string) => {
  try {
    const result = await http.get<IHostMemoryResult>(
      `/htcloud/host/info/${hostId}`,
      {},
      {
        headers: { noLoading: true },
      },
    )
    const { code, nodeInfo, msg } = result
    if (code === 0) {
      const { memory } = nodeInfo
      return memory
    }
    catchError(msg)
    return 0
  } catch (e: any) {
    catchError(`主机内存获取失败:${e.msg}`)
    return 0
  }
}

export const getVmList = async (param: Vm.IListParam): Promise<Vm.IListData> => {
  try {
    const result: Vm.IListApiResult = await http.post(`/htcloud/vm/list`, param, {
      headers: { noLoading: true },
    })
    const { page, limit } = param
    const { code, msg, totalCount, vmList } = result
    if (code === 0 && msg === 'success') {
      return {
        currPage: Number(page),
        list: vmList,
        pageSize: Number(limit),
        totalCount,
        totalPage: 0,
      }
    }
    catchError(msg)
    return {
      currPage: 0,
      list: [],
      pageSize: 10,
      totalCount: 0,
      totalPage: 0,
    }
  } catch (e: any) {
    catchError(`虚拟机列表获取失败:${e.msg}`)
    return {
      currPage: 0,
      list: [],
      pageSize: 10,
      totalCount: 0,
      totalPage: 0,
    }
  }
}

export interface IVMInfoResult {
  msg: string
  code: number
  storagePool: any
  storageEntity: any
  vm: Vm.IVmType
  vmHardware: any
  vmSwitchEntity: any
  securityGroup: any
  basicStorageEntity: any
}

export const getVMInfo = async (vmId: string) => {
  try {
    const result = await http.get<IVMInfoResult>(
      `/htcloud/vm/info/${vmId}`,
      {},
      { headers: { noLoading: true, noMessage: true } },
    )
    const {
      msg,
      code,
      storagePool,
      storageEntity,
      vm,
      vmHardware,
      vmSwitchEntity,
      securityGroup,
      basicStorageEntity,
    } = result || {}
    const { clusterId, dataCenterId, hostId } = vm
    const host = {
      clusterId,
      dataCenterId,
      hostId,
    }
    if (msg === 'success' && code === 0) {
      return {
        storagePool,
        storageEntity,
        vm,
        vmHardware,
        vmSwitchEntity,
        securityGroup,
        basicStorageEntity,
        host,
      }
    }
    catchError(msg)
  } catch (e: any) {
    catchError(`虚拟机获取失败:${e.msg}`)
  }
}
