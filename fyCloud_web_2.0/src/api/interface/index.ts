import Vm from '@/pages/vm'
import { number } from 'echarts'

// * 请求响应参数(不包含data)
export interface Result {
  code: string
  msg: string
}

// * 请求响应参数(包含data)
export interface ResultData<T = any> extends Result {
  data?: T
}

// * 分页响应参数
export interface ResPage<T> {
  datalist: T[]
  pageNum: number
  pageSize: number
  total: number
}

// * 分页请求参数
export interface ReqPage {
  page: string
  limit: string
}

// * 登录
export namespace Login {
  export interface ReqLoginForm {
    username: string
    password: string
    uuid: string
  }
  export interface ResLogin {
    code: number
    expire: number
    msg: string
    token: string
  }
  export interface ResAuthButtons {
    [propName: string]: any
  }
}

export namespace Datacenter {
  export interface TreeList {
    msg: string
    code: number
    centerList: DatecenterInfo[]
  }

  export interface DatecenterInfo {
    dataCenterId: string
    dataCenterName: string
    centerClusterList: Cluster.ClusterInfo[]
    centerHostList: Host.HostInfo[]
  }

  export interface IListApiResult {
    msg: string
    code: number
    dataCenterList: IDatecenterType[]
    totalCount: number
  }

  export interface IListData {
    currPage: number
    pageSize: number
    totalCount: number
    totalPage?: number
    list: IDatecenterType[]
  }

  export interface IDatecenterType {
    dataCenterId: string
    dataCenterName: string
    createTime: string
    createUserId: number
    key: string
    username: string
  }

  export interface PageParam {
    page: string
    limit: string
  }
}
export interface BaseListResultType<T> {
  totalCount: number
  pageSize: number
  totalPage: number
  currPage: number
  list: T[]
}

export interface securityPoolListResultType<T> {
  total: number
  size: number
  pages: number
  current: number
  records: T[]
}

export namespace Cluster {
  export interface ClusterInfo {
    clusterId: string
    clusterName: string
    clusterHostList?: Host.HostInfo[]
  }
  export interface IListApiResult {
    msg: string
    code: number
    clusterList: IClusterType[]
    totalCount: number
  }

  export interface IListData {
    currPage: number
    pageSize: number
    totalCount: number
    totalPage?: number
    list: IClusterType[]
  }

  export interface IClusterType {
    clusterId: string
    clusterName: string
    createTime: string
    createUserId: number | string
    createUserName: string
    dataCenterId: string
    dataCenterName: string
    drsSwitch: string
    haSwitch: string
  }

  export interface IListParam extends ReqPage {
    dataCenterId?: string
  }
}

export namespace Host {
  export interface HostInfo {
    hostId: string
    hostName: string
    state: string
    vmList?: Vm.VmInfo[]
  }

  export interface IListApiResult {
    msg: string
    code: number
    totalCount: number
    hostList: IHostType[]
  }
  export interface IListData {
    currPage: number
    pageSize: number
    totalCount: number
    totalPage?: number
    list: IHostType[]
  }

  export interface IHostType {
    bmcIp: string
    clusterId: string
    clusterName: string
    cpuType: string
    createTime: string
    createUserId: string
    createUserName: string
    dataCenterId: string
    dataCenterName: string
    hostId: string
    hostName: string
    hostPassword: string
    hostUser: string
    osIp: string
    state: string //1为运行 2为关机 3为维护模式 4为异常 5为失联
    virtualVersion: string
  }

  export interface startUpHost {
    hostId: string
    bmcIp: string
    hostUser: string
    hostPassword: string
    hostName?: string
    state?: string
  }

  export interface IListParam extends ReqPage {
    dataCenterId?: string
    clusterId?: string
  }
}

export namespace Vm {
  export interface VmInfo {
    vmId: string
    vmName: string
    state: string
  }

  export interface IListParam extends ReqPage {
    dataCenterId?: string
    clusterId?: string
    hostId?: string
  }

  export interface IListApiResult {
    msg: string
    code: number
    totalCount: number
    vmList: IVmType[]
  }
  export interface IListData {
    currPage: number
    pageSize: number
    totalCount: number
    totalPage?: number
    list: IVmType[]
  }

  export interface IVmType {
    clusterId: string
    clusterName: string
    createTime: string
    createUserId: string
    createUserName: string
    dataCenterId: string
    dataCenterName: string
    hostId: string
    hostName: string
    osIp: string
    state: string
    storageVolumeId: string
    vmId: string
    vmName: string
    vmMark: string
    vmOs?: string
  }
}

export namespace NetworkCard {
  export interface IListApiResult {
    msg: string
    code: number
    networkList: string
  }
  export interface IListData {
    list: INetworkCardType[]
  }

  export interface INetworkCardType {
    name: string
    model: string
    mac: string
    speed: string
    state: string
    mtu: string
    numa_node: string
    pci: string
  }
  export interface ISWListApiResult {
    msg: string
    code: number
    list: string[]
  }
}

export namespace StoragePool {
  export interface IListParam extends ReqPage {
    hostId?: string
    storagePoolName?: string
  }

  export interface ISaveDirParam {
    storagePoolName: string
    poolShowName: string
    poolType: string
    judge: string
    hostId: string
    clusterId: string
    storagePoolPath: string
  }

  export interface ISaveIscsiParam extends ISaveDirParam {
    storageIp: string
    mkfsFormat: string
    iqnFormat: string
    iqn: string
  }

  export interface ISaveLvmParam {
    storagePoolName: string
    poolShowName: string
    poolType: string
    judge: string
    hostId: string
    storagePoolPath: string
  }

  export interface IListApiResult {
    code: number
    msg: string
    totalCount: number
    poolList: IStoragePoolType[]
  }

  export interface IListData {
    currPage: number
    pageSize: number
    totalCount: number
    totalPage?: number
    list: IStoragePoolType[]
  }

  export interface IStoragePoolType {
    capacity: string
    clusterId: string
    createTime: string
    createUserId: string
    createUserName: string
    freeSpace: string
    hostId: string
    iqn: string
    iqnFormat: string
    judge: string
    mkfsFormat: string
    osIp: string
    poolShowName: string
    poolType: string
    poolUuid: string
    status: number | string // (1为活跃，2为不活跃，3为需要格式化,4为暂停)
    storageIp: string
    storagePoolId: string
    storagePoolName: string
    storagePoolPath: string
    usedSpace: string
    vmTemplateType: string
  }
  export interface ITargetByIpResult {
    msg: string
    code: number
    discoveryResult: string
    iqnFormatList: string[]
  }
}

export namespace StorageVolume {
  export interface IListParam extends ReqPage {
    storagePoolId?: string
    hostId?: string
    storageVolumeName?: string
    status?: number
  }

  export interface IListApiResult {
    msg: string
    code: number
    storageList: IStorageVolumeType[]
    totalCount: number
  }
  export interface IListData {
    currPage: number
    pageSize: number
    totalCount: number
    totalPage?: number
    list: IStorageVolumeType[]
  }

  export interface IStorageVolumeType {
    storageId: string
    volumeUuid: string
    storageVolumeName: string
    filesystem: string
    fileSize: string
    capacity: string
    storageType: string // 1为本地存储，2为NAS存储、3为ISCSI存储，4为FC存储
    storagePath: string
    basicVolumeId: string
    createFormat: string
    storagePoolId: string
    createTime: string
    createUserId: string
    judge: string
    identifier: string
    status: string // 1为正常，2为逻辑删除即放入回收站
    poolStatus: number
  }

  export interface ISaveParam {
    capacity: string
    filesystem: string
    storagePoolId: string
    storageType: string //根据存储池pooType， 1为本地存储，2为NAS存储、3为ISCSI存储，4为FC存储
    storageVolumeName: string
    createFormat: string //与存储卷类型对应
    basicVolumePath?: string
    basicVolumeId?: string
  }
}

export namespace VmSwitch {
  export interface IListParam extends ReqPage {
    hostId?: string
  }

  export interface IListApiResult {
    msg: string
    code: number
    page: BaseListResultType<IVmSwitchType>
  }

  export interface IListData {
    currPage: number
    pageSize: number
    totalCount: number
    totalPage: number
    list: IVmSwitchType[]
  }

  export interface IGetVmSwitchResult {
    code: string | number
    msg: string
    vmSwitch: IVmSwitchType
  }

  export interface IVmSwitchType {
    vmSwitchId: string
    vmSwitchName: string
    networkType: string // 1为管理网络，2为业务网络，3为存储网络
    mtuSize: string
    hostId: string
    netMachine: string
    ip: string
    gateway: string
    netMask: string
    linkMode: string // 静态、动态
    loadMode: string // 均衡、主备
    createTime: string
    createUserId: string
  }
}

export namespace PortFlux {
  export interface IListApiResult {
    msg: string
    code: number
    portFluxList: string
  }

  export interface IPortFluxhData {
    list: IPortFluxhType[]
  }
  export interface IPortFluxhType {
    rxBytes: string
    rxPackets: string
    rxErrors: string
    txBytes: string
    txPackets: string
    txErrors: string
  }
}

export namespace User {
  export interface UerrInfo {
    userId: string
    username: string
    password: string
    salt: string
    email: string
    mobile: string
    status: number
    roleIdList: any[]
    createUserId: string
    createTime: number
  }

  export interface ResLogout {
    msg: string
    code: number
  }

  export interface ResUser {
    code: number
    msg: string
    user: UerrInfo
  }
}

export namespace SecurityGroup {
  export interface IListApiResult {
    msg: string
    code: number
    page: BaseListResultType<ISecurityGroupType>
  }
  export interface IListData {
    currPage: number
    pageSize: number
    totalCount: number
    totalPage: number
    list: ISecurityGroupType[]
  }

  export interface ISecurityGroupType {
    createTime: number
    createUserId: string
    securityGroupId: string
    securityGroupName: string
    securityGroupRemark: string
  }
}

export namespace SecurityRule {
  export interface IListApiResult {
    msg: string
    code: number
    page: BaseListResultType<ISecurityRuleType>
  }
  export interface IListData {
    currPage: number
    pageSize: number
    totalCount: number
    totalPage: number
    list: ISecurityRuleType[]
  }

  export interface ISecurityRuleType {
    action: string
    agreeType: string
    createTime: number
    createUserId: string
    destIp: string
    destMask: string
    destPort: number
    inOutFlow: string
    ruleId: string
    securityGroupId: string
    sourceIp: string
    sourceMask: string
    sourcePort: number
  }

  export interface IListParam extends ReqPage {
    securityGroupId?: string
  }
}

export interface IBaseResult {
  code: number
  msg: string
}

export namespace MonitorCluster {
  export interface IMonitorClusterResult {
    clusterName: string
    cpuNum: number
    hostNum: number
    memTotal: string
    vmNum: number
    vmSummary: number[]
  }

  export interface IMonitorCluster {
    clusterName: string
    cpuNum: number
    hostNum: number
    memTotal: string
    vmNum: number
    closeNum: number
    otherNum: number
  }
}

export namespace MonitorHost {
  export interface IMonitorHostResult {
    cpuModel: string
    cpuNum: number
    cpuRate: string
    diskTotal: string
    hostModel: string
    hostName: string
    memRate: string
    memTotal: string
    runTime: string
    state: string
    vmSummary: number[]
  }

  export interface IMonitorHost {
    cpuModel: string
    cpuNum: number
    cpuRate: string
    diskTotal: string
    hostModel: string
    hostName: string
    memRate: string
    memTotal: string
    runTime: string
    state: string
    closeNum: number
    otherNum: number
  }
}

export namespace MonitorStorage {
  export interface IMonitorStorageResult {
    diskFreeNum: string
    diskRate: string
    diskTotal: string
    hostName: string
    sourcePath: string
    state: string
    vmDiskName: string
    vmShowName: string
  }
}
export namespace MonitorVM {
  export interface IMonitorVMResult {
    cpuNum: number
    cpuRate: string
    createTime: string
    hostName: string
    memRate: string
    memTotal: string
    os: string
    state: string
    vmDiskName: string
    vmShowName: string
  }
}
export namespace MonitorIP {
  export interface IMonitorIPResult {
    vmShowName: string
    vmDesc: string
    mac: string
    ip: string
    osName: string
  }
}

export namespace MonitorVlan {
  export interface IMonitorVlanResult {
    vlan: string
    ipMonitorList: MonitorIP.IMonitorIPResult[]
  }
}
export interface ITaskType {
  id: string
  createDate: number
  ip: string
  operMark: string
  operObj: string
  operation: string
  result: string
  username: string
  errorMsg: string
  vmId: string
  storagePoolId: string
}
