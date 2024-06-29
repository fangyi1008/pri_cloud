import http from '@/api'
import { BasicObj } from '@/components/multipleForm/type'
import { message } from 'antd'
import { StoragePool, StorageVolume } from '../interface'
import qs from 'qs'
import { operationResultFormat } from '@/utils/util'
import { IOperatResult } from './virtualMachineService'
export const catchError = (error: string) => {
  message.error(`${error}`)
}

export const getStoragePoolList = async (
  params: StoragePool.IListParam,
): Promise<StoragePool.IListData> => {
  try {
    const result: StoragePool.IListApiResult = await http.post(
      `/htcloud/storagePool/list`,
      params,
      {
        headers: { noLoading: true },
      },
    )
    const { totalCount, poolList } = result
    const { page, limit } = params
    return {
      currPage: Number(page),
      pageSize: Number(limit),
      totalCount,
      list: poolList,
    }
  } catch (err: any) {
    message.error('获取存储池列表失败: ' + err.message)
    return {
      list: [],
      currPage: 1,
      pageSize: 10,
      totalCount: 0,
      totalPage: 0,
    }
  }
}

export const unFormatDev = async (hostId: string) => {
  try {
    const result = await http.post<{ msg: string; code: number; unFormatList: string[] }>(
      '/htcloud/storagePool/unFormatDev',
      JSON.stringify(hostId),
      {
        headers: {
          noLoading: true,
          'Content-Type': 'application/json;charset=UTF-8',
          noMessage: true,
        },
      },
    )
    return result
  } catch (err) {
    message.error('获取未格式化列表失败')
  }
}

export const saveStoragePoolData = async (params: BasicObj) => {
  try {
    const result: { msg: string; code: number } = await http.post(
      '/htcloud/storagePool/save',
      params,
    )
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('新增存储池成功')
    }
    return result
  } catch (err) {
    return {}
  }
}

export const editStoragePoolData = async (params: BasicObj) => {
  try {
    const result: { msg: string; code: number } = await http.post(
      '/htcloud/storagePool/update',
      params,
    )
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('编辑存储池成功')
    }
    return result
  } catch (err) {
    return undefined
  }
}

export const deleteStoragePoolData = async (
  params: Record<string, any>[] | (string | number)[],
) => {
  try {
    const result: { msg: string; code: number; sysLogList: BasicObj[] } = await http.post(
      '/htcloud/storagePool/delete',
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
    message.error('删除存储池失败:' + msg)
    return { flag: false, errorList: [], successList: [] }
  } catch (err) {
    message.error('删除存储池失败')
    return { flag: false, errorList: [], successList: [] }
  }
}

export const storagePoolTargetByIp = async (params: {
  ip: string
  hostId: string
}): Promise<StoragePool.ITargetByIpResult> => {
  try {
    const result = await http.post<StoragePool.ITargetByIpResult>(
      '/htcloud/storagePool/targetByIp',
      params,
      {
        headers: { noLoading: true },
      },
    )
    const { code, msg } = result
    if (code === 0 && msg === 'success') {
      message.success('获取磁盘成功')
    }
    return result
  } catch (err) {
    return {
      msg: '',
      code: 0,
      discoveryResult: '',
      iqnFormatList: [],
    }
  }
}

export const storagePoolFormat = async (params: {
  storagePoolId: string
  mkfsFormat: string
  iqnFormat: string
}) => {
  try {
    const result: { msg: string; code: number } = await http.post(
      '/htcloud/storagePool/iscsiFormat',
      params,
    )
    return result
  } catch (err) {
    return undefined
  }
}

export const startStoragePool = async (params: { poolType: string; storagePoolId: string }) => {
  try {
    const result: { msg: string; code: number } = await http.post(
      '/htcloud/storagePool/startStoragePool',
      params,
    )
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('开启存储池成功')
    }
    return result
  } catch (err) {
    return {}
  }
}

export const stopStoragePool = async (params: { poolType: string; storagePoolId: string }) => {
  try {
    const result: { msg: string; code: number } = await http.post(
      '/htcloud/storagePool/stopStoragePool',
      params,
    )
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('暂停存储池成功')
    }
    return result
  } catch (err) {
    return {}
  }
}

export const poolDataInfo = async (id: string) => {
  try {
    const result = await http.get<{
      msg: string
      code: number
      storagePool: BasicObj
      storagePoolInfo: BasicObj
    }>(`/htcloud/storagePool/info/${id}`, undefined, {
      headers: { noLoading: true },
    })
    return result
  } catch (err) {
    return undefined
  }
}

//存储卷
export const getStorageVolumeList = async (
  params: StorageVolume.IListParam,
): Promise<StorageVolume.IListData> => {
  try {
    const result: StorageVolume.IListApiResult = await http.post(`/htcloud/storage/list`, params, {
      headers: { noLoading: true },
    })
    const { totalCount, storageList = [] } = result
    const { page, limit } = params
    return {
      currPage: Number(page),
      pageSize: Number(limit),
      totalCount: totalCount,
      list: storageList,
    }
  } catch (err: any) {
    message.error('获取存储卷列表失败: ' + err.message)
    return {
      list: [],
      currPage: 1,
      pageSize: 10,
      totalCount: 0,
      totalPage: 0,
    }
  }
}

export const saveStorageData = async (params: StorageVolume.ISaveParam) => {
  try {
    const result: { msg: string; code: number } = await http.post('/htcloud/storage/save', params)
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('新增存储卷成功')
    }
    return result
  } catch (err) {
    return {}
  }
}

export const getSelectStorageData = async (params: StorageVolume.IListParam) => {
  try {
    const result: {
      msg: string
      code: number
      page: BasicObj
      storagePoolList: StoragePool.IStoragePoolType[]
    } = await http.post('/htcloud/storage/selectStorage', params, { headers: { noLoading: true } })
    const { page, storagePoolList } = result || { page: {} }
    const { list = [], currPage = 1, pageSize = 10, totalCount = 0, totalPage = 0 } = page || {}
    const newStoragePoolList = [...storagePoolList].map(item => {
      const { status } = item
      const newStatus =
        status == '1' ? '活跃' : status == '2' ? '不活跃' : status == '3' ? '未格式化' : '暂停'
      return { ...item, status: newStatus }
    })
    const newList = list.map((item: any) => ({ ...item, key: item.storageId }))
    return {
      storagePoolList: newStoragePoolList,
      currPage: currPage,
      pageSize: pageSize,
      totalCount: totalCount,
      totalPage: totalPage,
      list: newList,
    }
  } catch (err: any) {
    message.error('获取基础镜像列表失败' + err.message)
    return {
      storagePoolList: [],
      list: [],
      currPage: 1,
      pageSize: 10,
      totalCount: 0,
      totalPage: 0,
    }
  }
}

export const selectBasicVol = async (params: string) => {
  try {
    const result: { code: number; msg: string } = await http.post(
      '/htcloud/storage/selectBasicVol',
      JSON.stringify(params),
      {
        headers: { noLoading: true, 'Content-Type': 'application/json;charset=UTF-8' },
      },
    )
    const { code, msg } = result
    if (code === 0 && msg === 'success') {
      return true
    }
    message.error(msg)
    return false
  } catch (err: any) {
    return false
  }
}

export const uplodeFile = async (params: any) => {
  try {
    const result = await http.post('/htcloud/storage/uploadIso', params)
    return result
  } catch (err) {
    message.error('上传文件失败')
  }
}

export const uploadCheck = async (params: {
  storageVolumeName: string
  storagePoolId: string
  identifier: string
  fileSize: number
}) => {
  try {
    const result = await http.get('/htcloud/storage/upload', params, {
      header: { noLoading: true, noMessage: true },
    })
    return result
  } catch (err) {
    console.log(err)
  }
}

export const uploadMerge = async (params: {
  storageVolumeName: string
  storagePoolId: string
  identifier: string
  fileSize: number
}) => {
  try {
    const result = await http.post('/htcloud/storage/merge', params, {
      header: { noLoading: true, noMessage: true },
    })
    return result
  } catch (err: any) {
    return err
  }
}

export const downloadFile = async (
  obj: { storageId: string; storageVolumeName: string; filesystem: string },
  loadingState: (evt: any) => void,
) => {
  const { storageId } = obj
  const result: any = await http.post(
    '/htcloud/storage/downloadFile',
    qs.stringify({ storageId }),
    {
      headers: {
        noLoading: true,
        'Content-type': 'application/x-www-form-urlencoded',
        noMessage: true,
      },
      responseType: 'blob',
      onDownloadProgress: (evt: any) => {
        loadingState(evt)
      },
      timeout: 1000000,
    },
  )
  return result
}

export const deleteStorageData = async (params: {
  storageIds: (string | number)[]
  rbFlag: boolean
}) => {
  try {
    const result = await http.post<IOperatResult>('/htcloud/storage/delete', params, {
      headers: { noLoading: true, noMessage: true },
    })
    const { code, msg, sysLogList = [] } = result
    if (code === 0 && msg === 'success' && sysLogList.length > 0) {
      return operationResultFormat(sysLogList)
    }
    message.error('删除存储卷失败:' + msg)
    return { flag: false, errorList: [], successList: [] }
  } catch (err) {
    message.error('删除存储卷失败')
    return { flag: false, errorList: [], successList: [] }
  }
}

export const resumeStorage = async (params: (string | number)[]) => {
  try {
    const result = await http.post<IOperatResult>('/htcloud/storage/update', params, {
      headers: { noLoading: true, noMessage: true },
    })
    const { code, msg, sysLogList = [] } = result
    if (code === 0 && msg === 'success' && sysLogList.length > 0) {
      return operationResultFormat(sysLogList)
    }
    message.error('恢复存储卷失败:' + msg)
    return { flag: false, errorList: [], successList: [] }
  } catch (err) {
    message.error('恢复存储卷失败')
    return { flag: false, errorList: [], successList: [] }
  }
}
