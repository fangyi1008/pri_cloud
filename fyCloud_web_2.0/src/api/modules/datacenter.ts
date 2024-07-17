import http from '@/api'
import { Datacenter, Cluster, Host, Vm } from '../interface'
import { BasicObj } from '@components/multipleForm/type'
import { message } from 'antd'
import { IOperatResult } from './virtualMachineService'
import { operationResultFormat } from '@/utils/util'

// * 接口
export const getAsyncDataCenterData = async (
  params: Datacenter.PageParam,
): Promise<Datacenter.IListData> => {
  try {
    const result: Datacenter.IListApiResult = await http.post('/htcloud/datacenter/list', params, {
      headers: { noLoading: true },
    })
    const { dataCenterList = [], totalCount } = result
    const newList = dataCenterList
      ? dataCenterList.map((item: Datacenter.IDatecenterType) => {
          const { createUserId } = item || {}
          return {
            ...item,
            key: item.dataCenterId,
            username: createUserId + '',
          }
        })
      : []
    const { page, limit } = params
    return {
      currPage: Number(page),
      pageSize: Number(limit),
      totalCount: totalCount,
      list: newList,
    }
  } catch (err) {
    message.error('数据中心列表获取失败')
    return {
      list: [],
      currPage: 1,
      pageSize: 10,
      totalCount: 0,
      totalPage: 0,
    }
  }
}
export const saveAsyncDataCenterData = async (params: BasicObj) => {
  try {
    const result: { msg: string; code: number } = await http.post(
      '/htcloud/datacenter/save',
      params,
    )
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('新增数据中心成功')
    }
    return result
  } catch (err) {
    message.error('新增数据中心失败')
  }
}

export const editAsyncDataCenterData = async (params: BasicObj) => {
  try {
    const result: { msg: string; code: number } = await http.post(
      '/htcloud/datacenter/update',
      params,
    )
    const { msg, code } = result
    if (msg === 'success' && code === 0) {
      message.success('编辑数据中心成功')
    } else {
      message.error('编辑数据中心失败')
    }
    return result
  } catch (err) {
    message.error('编辑数据中心失败')
  }
}

export const deleteAsyncDataCenterData = async (
  params: Record<string, any>[] | (string | number)[],
) => {
  try {
    const result = await http.post<IOperatResult>('/htcloud/datacenter/delete', params, {
      headers: {
        noLoading: true,
        noMessage: true,
      },
    })
    const { code, msg, sysLogList = [] } = result
    if (code === 0 && msg === 'success' && sysLogList.length > 0) {
      return operationResultFormat(sysLogList)
    }
    message.error('删除数据中心失败:' + msg)
    return { flag: false, errorList: [], successList: [] }
  } catch (err) {
    message.error('删除数据中心失败')
    return { flag: false, errorList: [], successList: [] }
  }
}

export const dataCenterDataInfo = async (id: string) => {
  try {
    const result = await http.get<{ datacenter: BasicObj }>(
      `/htcloud/datacenter/info/${id}`,
      undefined,
      {
        headers: { noLoading: true },
      },
    )
    return result
  } catch (err) {
    return { datacenter: {} }
  }
}

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
const metaRouters = require.context('../../pages/compoentView', false, /\.tsx$/)
const fileUrls = metaRouters.keys() || []
const exampleMenuArray = fileUrls.map((url: string) => url.match(/([^\/]+)\.\w+$/)?.[1])

export const isNotNullArray = (data: any[]): boolean => {
  if (Array.isArray(data)) {
    return data.length > 0
  }
  return false
}

export const transformVM = (
  vmList: Vm.VmInfo[],
  parentInfo: {
    dataCenterId: string
    clusterId?: string
    hostId: string
  },
) => {
  return vmList.map(vmList => {
    const { vmId, vmName, state } = vmList
    return {
      path: `/vm/${vmId}`,
      icon: 'IconVmMonitor',
      ...parentInfo,
      key: vmId,
      vmId,
      title: vmName,
      state,
      type: 'vm',
    }
  })
}

export const transformHost = (
  hostList: Host.HostInfo[],
  parentInfo: {
    dataCenterId: string
    clusterId?: string
  },
) =>
  hostList.map((clusterItem: any) => {
    const { hostId, hostName, state, vmList = [] } = clusterItem
    const childrenData = isNotNullArray(vmList)
      ? transformVM(vmList, { hostId, ...parentInfo })
      : []
    return {
      path: `/host/${hostId}`,
      icon: 'IconHost',
      key: hostId,
      ...parentInfo,
      title: hostName,
      hostId,
      state,
      type: 'host',
      children: childrenData,
    }
  })

const findCanFirstUseHost = (hostList: Host.HostInfo[]) => {
  const { hostId } = hostList.find(item => item.state === '1') || {}
  return hostId
}

export const transformCluster = (
  clusterList: Cluster.ClusterInfo[],
  parentInfo: { dataCenterId: string },
) =>
  clusterList.map((clusterItem: any) => {
    const { clusterId, clusterName, clusterHostList } = clusterItem
    const childrenData = isNotNullArray(clusterHostList)
      ? transformHost(clusterHostList, { clusterId, ...parentInfo })
      : []

    return {
      path: `/cluster/${clusterId}`,
      icon: 'IconCluster',
      key: clusterId,
      clusterId,
      canUseHostId: findCanFirstUseHost(clusterHostList),
      ...parentInfo,
      title: clusterName,
      type: 'cluster',
      children: childrenData,
    }
  })

export const getMenuListApi = async (): Promise<Menu.MenuOptions[]> => {
  const result = await http.post<Datacenter.TreeList>(`/htcloud/datacenter/treeList`)
  const { centerList = [] } = result

  const children = centerList.map((centerItem: Datacenter.DatecenterInfo) => {
    const { dataCenterId, dataCenterName, centerClusterList = [], centerHostList = [] } = centerItem
    if (isNotNullArray(centerClusterList) || isNotNullArray(centerHostList)) {
      const clusterData = transformCluster(centerClusterList || [], { dataCenterId })
      const hostData = transformHost(centerHostList || [], { dataCenterId })
      return {
        path: `/dataCenter/${dataCenterId}`,
        icon: 'IconDatacenter',
        key: dataCenterId,
        title: dataCenterName,
        dataCenterId,
        type: 'dataCenter',
        children: [...clusterData, ...hostData],
      }
    }
    return {
      path: `/dataCenter/${dataCenterId}`,
      icon: 'IconDatacenter',
      key: dataCenterId,
      dataCenterId,
      title: dataCenterName,
      type: 'dataCenter',
    }
  })
  const exampleMenu: {
    path: string
    key: string
    icon: string
    title: string
    children: any[]
    type: string
  } = {
    path: '/exampleMenu',
    key: 'exampleMenu',
    icon: 'CloudOutlined',
    type: 'empty',
    title: '组件',
    children: [],
  }
  if (process.env.NODE_ENV === 'development') {
    for (let i = 0; i < exampleMenuArray.length; i++) {
      const name = exampleMenuArray[i]
      exampleMenu.children.push({
        path: `/${name}`,
        key: `${name}`,
        icon: 'CloudOutlined',
        title: `${name}`,
      })
    }
  }

  const baseMenu = [
    { path: '/home/index', key: 'home/index', icon: 'IconShouyeshouye', title: '首页' },
    {
      path: '/compute',
      key: 'compute',
      icon: 'IconYunjisuan',
      title: '计算',
      type: 'compute',
      children: children,
    },
    {
      path: '/network',
      icon: 'IconWangluo',
      title: '网络',
      type: 'empty',
      children: [
        {
          key: 'securityGroups',
          path: '/securityGroups',
          icon: 'IconAnquanzu',
          title: '安全组',
        },
        // {
        //   key: 'securityPolicy',
        //   path: '/securityPolicy',
        //   icon: 'ClusterOutlined',
        //   title: '安全策略',
        // },
        // {
        //   key: 'qosSpeedLimitPolicy',
        //   path: '/qosSpeedLimitPolicy',
        //   icon: 'CloudServerOutlined',
        //   title: 'Qos限速策略',
        // },
      ],
    },
    {
      path: '/',
      icon: 'IconYuncunchu',
      title: '存储',
      type: 'empty',
      children: [
        {
          key: 'storage',
          path: '/storage',
          icon: 'IconYuncunchu',
          title: '存储池',
        },
        {
          key: 'recycle',
          path: '/recycle',
          icon: 'RestOutlined',
          title: '回收站',
        },
      ],
    },
    {
      path: '/monitor',
      icon: 'IconYunjiankong',
      title: '监控',
      type: 'empty',
      children: [
        {
          key: 'clusterMonitor',
          path: '/monitor/cluster',
          icon: 'IconClusterMonitor',
          title: '集群监控',
        },
        {
          key: 'hostMonitor',
          path: '/monitor/host',
          icon: 'IconHostrMonitor',
          title: '主机监控',
        },
        {
          key: 'ipMonitor',
          path: '/monitor/ip',
          icon: 'IconIprMonitor',
          title: 'IP监控',
        },
        {
          key: 'storageMonitor',
          path: '/monitor/storage',
          icon: 'IconStoragerMonitor',
          title: '存储监控',
        },
        {
          key: 'vlanMonitor',
          path: '/monitor/vlan',
          icon: 'IconVlanMonitor',
          title: 'vlan监控',
        },
        {
          key: 'vmMonitor',
          path: '/monitor/vm',
          icon: 'IconVmMonitor',
          title: '虚拟机监控',
        },
      ],
    },
  ]

  const menuData = exampleMenu.children.length > 0 ? [...baseMenu, exampleMenu] : baseMenu
  console.log('menuData===>', menuData)
  return menuData
}
