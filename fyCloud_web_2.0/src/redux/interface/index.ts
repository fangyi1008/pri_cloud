import type { SizeType } from 'antd/lib/config-provider/SizeContext'
import { Host, ITaskType, User } from '@/api/interface'
import { FormDataType } from '@/components/multipleFormItem/type'
import { IProTableListRef } from '@/components/proTable/type'

export interface ThemeConfigProp {
  primary: string
  isDark: boolean
  weakOrGray: string
  breadcrumb: boolean
  tabs: boolean
  footer: boolean
}

export interface GlobalState {
  token: string
  userInfo?: User.UerrInfo
  assemblySize: SizeType
  language: string
  themeConfig: ThemeConfigProp
}

export interface MenuState {
  isCollapse: boolean
  menuList: Menu.MenuOptions[]
  loading?: boolean
}

export interface TabsState {
  tabsActive: string
  tabsList: Menu.MenuOptions[]
}

/* BreadcrumbState */
export interface BreadcrumbState {
  breadcrumbList: {
    [propName: string]: any
  }
}
/* AuthState */
export interface AuthState {
  authButtons: {
    [propName: string]: any
  }
  authRouter: string[]
}

/* deviceManagement */
export type DeviceAddOrEditType = 'add' | 'edit'

export type DeviceManagementAddOrEdit = {
  visible: boolean
  type: DeviceAddOrEditType
  formData: FormDataType[]
  initialValues: Record<string, any>
}
export interface DeviceManagementState {
  cluster: DeviceManagementAddOrEdit
  dataCenter: DeviceManagementAddOrEdit
  host: DeviceManagementAddOrEdit
  vm: DeviceManagementAddOrEdit
}

export type TipsDeviceType = {
  visible: boolean
  title: string
  content: string
  values: (string | number)[]
}

export type TipsDeleteDeviceType = {
  visible: boolean
  title: string
  content: string
  formData: FormDataType[]
  values: { hostId?: string; ids?: (string | number)[] }
}

export type startUpType = {
  visible: boolean
  title: string
  content: string
  values: Host.startUpHost[]
}

export type startPoolType = {
  visible: boolean
  title: string
  content: string
  values: { poolType: string; storagePoolId: string }
}

export type formatType = {
  visible: boolean
  title: string
  content: string
  values: { mkfsFormat: string; storagePoolId: string; iqnFormat: string }
}

export type DownloadType = {
  visible: boolean
  title: string
  content: string
  values: { storageId: string; storageVolumeName: string; filesystem: string }
}
export type DeleteClusterType = {
  visible: boolean
  title: string
  content: string
  values: { dataCenterId: string; ids: (string | number)[] }
}
export interface ClusterOperateState {
  edit: DeviceManagementAddOrEdit
  delete: DeleteClusterType
}
export type DeviceManagement = {
  title: string
  visible: boolean
  formData: FormDataType[]
  initialValues: Record<string, any>
}

export interface RecycleOperateState {
  edit: TipsDeviceType
  delete: TipsDeviceType
}

export interface BaseOperateState {
  edit: DeviceManagementAddOrEdit
  delete: TipsDeviceType
}

export type DeleteHostType = {
  visible: boolean
  title: string
  content: string
  values: { dataCenterId: string; clusterId: string; ids: (string | number)[] }
}
export interface HostBaseOperateState {
  edit: DeviceManagementAddOrEdit
  delete: DeleteHostType
}
export interface HostOperateState extends HostBaseOperateState {
  enterMaintain: DeviceManagement
  exitMaintain: TipsDeviceType
  startUp: startUpType
  close: TipsDeviceType
  restart: TipsDeviceType
}

export interface VmOperateState {
  edit: VirtualMachineAddOrEdit
  delete: TipsDeleteDeviceType
  startUp: TipsDeviceType
  close: TipsDeviceType
  restart: TipsDeviceType
  destroy: TipsDeviceType
  move: IMoveVirtualMachine
  suspend: TipsDeviceType
  resume: TipsDeviceType
}

export interface IVirtualSwitchOperateState {
  edit: VirtualMachineAddOrEdit
  delete: TipsDeviceType
}

export interface poolOperateState {
  add: DeviceManagementAddOrEdit
  edit: DeviceManagement
  delete: TipsDeviceType
  start: startPoolType
  format: DeviceManagement
  stop: startPoolType
}

export interface loadingType {
  isloading: boolean
  percent: number
}
export interface volumeOperateState {
  upload: DeviceManagement
  download: DownloadType
  edit: DeviceManagementAddOrEdit
  delete: DeviceManagement
  downloadStorageVolume: loadingType
}

export interface IStepFormDataType {
  key: string
  title: string
  data: FormDataType[]
}

export interface VirtualMachineAddOrEdit {
  visible: boolean
  type: DeviceAddOrEditType
  formData: IStepFormDataType[]
  initialValues: Record<string, any>
}

export interface IMoveVirtualMachine extends Omit<VirtualMachineAddOrEdit, 'type'> {
  title: string
}

export interface IPageInfo<T = any> {
  listData: T[]
  pageSize: number
  current: number
  total: number
}

export interface IBaseList<T = any> {
  loading: boolean
  pageInfo: IPageInfo<T>
  selectedRowKeys: string[]
}

export interface IHostInfo {
  bmcIp: string | null
  clusterId?: string | null
  cpuType?: string | null
  createTime?: number | null
  createUserId?: string | null
  dataCenterId?: string | null
  hostId: string | null
  hostName?: string | null
  hostPassword: string | null
  hostUser: string | null
  osIp?: string | null
  state?: string | null
  virtualVersion?: string | null
}
export interface IStorageList {
  storagePool: IBaseList
  storage: IBaseList
}

export interface IDataCenterList<T = any> {
  refresh: {
    list: T[]
    currPage: number
    pageSize: number
    totalCount: number
    totalPage?: number
  }
  listRefRefresh: IProTableListRef | null
}

export interface ITaskBoardState {
  open: boolean
  list: ITaskType[]
}
