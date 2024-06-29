import http from '@/api'
import { BasicObj } from '@/components/showInfoCard/type'
import { operationResultFormat } from '@/utils/util'
import { message } from 'antd'
import { ReqPage, SecurityGroup, SecurityRule } from '../interface'
import { IOperatResult } from './virtualMachineService'

export const catchError = (error: string) => {
  message.error(`${error}`)
}

export const getSecurityGroupList = async (params: ReqPage): Promise<SecurityGroup.IListData> => {
  try {
    const result: SecurityGroup.IListApiResult = await http.post(
      `/htcloud/securityGroup/list`,
      params,
      {
        headers: { noLoading: true },
      },
    )
    const { page } = result || { page: {} }
    const { list = [], currPage = 1, pageSize = 10, totalCount = 0, totalPage = 0 } = page || {}
    return {
      currPage: currPage,
      pageSize: pageSize,
      totalCount: totalCount,
      totalPage: totalPage,
      list,
    }
  } catch (err: any) {
    message.error('获取安全组列表失败: ' + err.message)
    return {
      list: [],
      currPage: 1,
      pageSize: 10,
      totalCount: 0,
      totalPage: 0,
    }
  }
}

export const saveSecurityGroupData = async (params: BasicObj) => {
  try {
    const result: { msg: string; code: number } = await http.post(
      '/htcloud/securityGroup/save',
      params,
    )
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('新增安全组成功')
    }
    return result
  } catch (err) {
    return {}
  }
}

export const editSecurityGroupData = async (params: BasicObj) => {
  try {
    const result: { msg: string; code: number } = await http.post(
      '/htcloud/securityGroup/update',
      params,
    )
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('编辑安全组成功')
    }
    return result
  } catch (err) {
    return undefined
  }
}

export const deleteSecurityGroupData = async (
  params: Record<string, any>[] | (string | number)[],
) => {
  try {
    const result = await http.post<IOperatResult>('/htcloud/securityGroup/delete', params, {
      headers: {
        noLoading: true,
        noMessage: true,
      },
    })
    const { code, msg, sysLogList = [] } = result
    if (code === 0 && msg === 'success' && sysLogList.length > 0) {
      return operationResultFormat(sysLogList)
    }
    message.error('删除安全组失败:' + msg)
    return { flag: false, errorList: [], successList: [] }
  } catch (err) {
    message.error('删除安全组失败')
    return { flag: false, errorList: [], successList: [] }
  }
}

export const groupInfo = async (id: string) => {
  try {
    const result = await http.get<{ securityGroup: BasicObj }>(
      `/htcloud/securityGroup/info/${id}`,
      undefined,
      {
        headers: { noLoading: true },
      },
    )
    return result
  } catch (err) {
    return {
      securityGroup: { securityGroupName: '', securityGroupRemark: '' },
    }
  }
}

//安全规则
export const getSecurityRuleList = async (
  params: SecurityRule.IListParam,
): Promise<SecurityRule.IListData> => {
  try {
    const result: SecurityRule.IListApiResult = await http.post(
      `/htcloud/securityRule/list`,
      params,
      {
        headers: { noLoading: true },
      },
    )
    const { page } = result || { page: {} }
    const { list = [], currPage = 1, pageSize = 10, totalCount = 0, totalPage = 0 } = page || {}
    const newList = [...list].map(item => {
      const { inOutFlow, action } = item
      const newInOutFlow =
        inOutFlow === '1' ? '入方向' : inOutFlow === '2' ? '出方向' : '入方向、出方向'
      const newAction = action === 'accept' ? '允许' : '拒绝'
      return { ...item, inOutFlow: newInOutFlow, action: newAction }
    })
    return {
      currPage: currPage,
      pageSize: pageSize,
      totalCount: totalCount,
      totalPage: totalPage,
      list: newList,
    }
  } catch (err: any) {
    message.error('获取安全规则列表失败: ' + err.message)
    return {
      list: [],
      currPage: 1,
      pageSize: 10,
      totalCount: 0,
      totalPage: 0,
    }
  }
}

export const saveSecurityRuleData = async (params: BasicObj) => {
  try {
    const result: { msg: string; code: number } = await http.post(
      '/htcloud/securityRule/save',
      params,
    )
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('新增安全规则成功')
    }
    return result
  } catch (err) {
    return {}
  }
}

export const editSecurityRuleData = async (params: BasicObj) => {
  try {
    const result: { msg: string; code: number } = await http.post(
      '/htcloud/securityRule/update',
      params,
    )
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('编辑安全规则成功')
    }
    return result
  } catch (err) {
    return undefined
  }
}

export const deleteSecurityRuleData = async (
  params: Record<string, any>[] | (string | number)[],
) => {
  try {
    const result = await http.post<IOperatResult>('/htcloud/securityRule/delete', params, {
      headers: {
        noLoading: true,
        noMessage: true,
      },
    })
    const { code, msg, sysLogList = [] } = result
    if (code === 0 && msg === 'success' && sysLogList.length > 0) {
      return operationResultFormat(sysLogList)
    }
    message.error('删除安全规则失败:' + msg)
    return { flag: false, errorList: [], successList: [] }
  } catch (err) {
    message.error('删除安全规则失败')
    return { flag: false, errorList: [], successList: [] }
  }
}

export const ruleInfo = async (id: string) => {
  try {
    const result = await http.get<{ securityRule: BasicObj }>(
      `/htcloud/securityRule/info/${id}`,
      undefined,
      {
        headers: { noLoading: true },
      },
    )
    return result
  } catch (err) {
    return {
      securityRule: {},
    }
  }
}
