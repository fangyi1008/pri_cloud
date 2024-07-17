import {
  setDataCenterInfo,
  setDeleteDataCenter,
} from '@redux/modules/deviceManagement/dataCenterOperate'
import { setClusterInfo, setDeleteCluster } from '@redux/modules/deviceManagement/clusterOperate'
import {
  setHostInfo,
  setDeleteHost,
  setEnterMaintain,
  setExitMaintain,
  setStartUp,
  setClose,
  setRestart,
} from '@redux/modules/deviceManagement/hostOperate'
import {
  setVmInfo,
  setDeleteVm,
  setStartUpVm,
  setCloseVm,
  setRestartVm,
  setDestroyVm,
  setMoveVm,
  setSuspendVm,
  setResumeVm,
} from '@redux/modules/deviceManagement/vmOperate'
import {
  setVirtualSwitchDelete,
  setVirtualSwitchInfo,
} from '@redux/modules/deviceManagement/virtualSwitch'
import { dataCenterFormData } from '../deviceManagement/dataCenter'
import { clusterFormData } from '../deviceManagement/cluster'
import { hostFormData, edmitHostFormData, enterMaintainFormData } from '../deviceManagement/host'
import {
  addVirtualMachineItem,
  editVirtualMachineItem,
  changeHostItem,
  virtualMachineInitValue,
  DeleteFormData,
} from '../deviceManagement/vm'
import { dataCenterDataInfo, getAsyncDataCenterData } from '@/api/modules/datacenter'
import { clusterDataInfo } from '@/api/modules/cluster'
import { getHostList, hostDataInfo } from '@/api/modules/host'
import { getVMInfo, splitUnit } from '@/api/modules/virtualMachineService'
import { Host, StorageVolume } from '@/api/interface'
import {
  setDeletePool,
  setEditPool,
  setFormatPool,
  setPoolInfo,
  setStartPool,
  setStopPool,
} from '@/redux/modules/deviceManagement/storagePool'
import { poolDataInfo } from '@/api/modules/storagePools'
import { editPoolFormData, formatPoolFormData, oneFormData } from '../deviceManagement/storagePool'
import {
  setDeleteVolume,
  setDownloadVolume,
  setVolumeInfo,
} from '@/redux/modules/deviceManagement/storageVolume'
import { deleteVolumeFormData, volumeFormData } from '../deviceManagement/storageVolume'
import { BasicObj } from '@/components/showInfoCard/type'
import { virtualSwitchFromData } from '../deviceManagement/VirtualSwitch'
import { getVirtualSwitchInfo } from '@/api/modules/virtualSwitch'
import { verticalLineStrToArray } from '@/utils/util'
import { groupFormData } from '../deviceManagement/securityGroup'
import { setDeleteGroup, setGroupInfo } from '@/redux/modules/deviceManagement/securityGroup'
import { groupInfo, ruleInfo } from '@/api/modules/network'
import { setDeleteRule, setRuleInfo } from '@/redux/modules/deviceManagement/securityRule'
import { ruleformData } from '../deviceManagement/securityRule'
import { setDeleteRecycle, setResumeStorage } from '@/redux/modules/deviceManagement/recycleOperate'

export const operateToDispath = {
  addDataCenter: async (dispatch: any) => {
    dispatch(
      setDataCenterInfo({
        visible: true,
        initialValues: {},
        type: 'add',
        formData: dataCenterFormData,
      }),
    )
  },
  editDataCenter: async (dispatch: any, value: { dataCenterId: string }) => {
    const { dataCenterId } = value || {}
    const result = await dataCenterDataInfo(dataCenterId)
    const { datacenter } = result || { datacenter: {} }
    await dispatch(
      setDataCenterInfo({
        visible: true,
        initialValues: datacenter,
        type: 'edit',
        formData: dataCenterFormData,
      }),
    )
  },
  deleteDataCenter: async (dispatch: any, values: (string | number)[]) => {
    dispatch(
      setDeleteDataCenter({
        visible: true,
        title: '删除数据中心',
        content: '您确定要删除该数据中心吗?',
        values: values,
      }),
    )
  },
  addCluster: async (dispatch: any, isDataCenter?: string | undefined) => {
    const getSelectList = await getAsyncDataCenterData({ page: '1', limit: '1000' })
    const { list } = getSelectList
    const selectList = list.map(item => ({ label: item.dataCenterName, value: item.dataCenterId }))
    const initialValues = isDataCenter
      ? { dataCenterId: isDataCenter, drsSwitch: 1, haSwitch: 1 }
      : { drsSwitch: 1, haSwitch: 1 }
    dispatch(
      setClusterInfo({
        visible: true,
        initialValues,
        type: 'add',
        formData: clusterFormData(selectList, isDataCenter),
      }),
    )
  },
  editCluster: async (dispatch: any, clusterId: string | number) => {
    const getSelectList = await getAsyncDataCenterData({ page: '1', limit: '1000' })
    const { list } = getSelectList
    const selectList = list.map(item => ({ label: item.dataCenterName, value: item.dataCenterId }))
    const result = await clusterDataInfo(String(clusterId))
    const { cluster } = result || { cluster: {} }
    await dispatch(
      setClusterInfo({
        visible: true,
        initialValues: cluster || {},
        type: 'edit',
        formData: clusterFormData(selectList),
      }),
    )
  },
  deleteCluster: async (
    dispatch: any,
    values: { dataCenterId: string; ids: (string | number)[] },
  ) => {
    dispatch(
      setDeleteCluster({
        visible: true,
        title: '删除集群',
        content: '您确定要删除该集群吗?',
        values: values,
      }),
    )
  },
  addHost: async (dispatch: any, param: BasicObj) => {
    const { dataCenterId, clusterId } = param
    dispatch(
      setHostInfo({
        visible: true,
        initialValues: { dataCenterId, clusterId },
        type: 'add',
        formData: hostFormData,
      }),
    )
  },
  editHost: async (dispatch: any, hostId: string | number) => {
    const result = await hostDataInfo(String(hostId))
    const { host } = result || { host: { hostName: '' } }
    const { hostName } = host
    await dispatch(
      setHostInfo({
        visible: true,
        initialValues: { hostName, hostId },
        type: 'edit',
        formData: edmitHostFormData,
      }),
    )
  },
  deleteHost: async (
    dispatch: any,
    values: { dataCenterId: string; clusterId: string; ids: (string | number)[] },
  ) => {
    dispatch(
      setDeleteHost({
        visible: true,
        title: '删除主机',
        content: '您确定要删除该主机吗?',
        values: values,
      }),
    )
  },
  enterMaintain: async (dispatch: any, hostId: string) => {
    dispatch(
      setEnterMaintain({
        visible: true,
        title: '进入维护模式',
        formData: enterMaintainFormData,
        initialValues: { hostId, moveFlag: false },
      }),
    )
  },
  exitMaintain: async (dispatch: any, values: (string | number)[]) => {
    dispatch(
      setExitMaintain({
        visible: true,
        title: '退出维护模式',
        content: '您确定要退出维护模式吗?',
        values,
      }),
    )
  },
  openHost: async (dispatch: any, values: string[]) => {
    const [hostId] = values
    const result: { host: Host.IHostType } | undefined = await hostDataInfo(String(hostId))
    const { host } = result || { host: { hostId: '', bmcIp: '', hostUser: '', hostPassword: '' } }
    dispatch(
      setStartUp({
        visible: true,
        title: '开机',
        content: '您确定要开机吗?',
        values: [host],
      }),
    )
  },
  closeHost: async (dispatch: any, values: (string | number)[]) => {
    dispatch(
      setClose({
        visible: true,
        title: '关闭',
        content: '您确定要关闭吗?',
        values,
      }),
    )
  },
  restartHost: async (dispatch: any, values: (string | number)[]) => {
    dispatch(
      setRestart({
        visible: true,
        title: '重启',
        content: '您确定要重启吗?',
        values,
      }),
    )
  },
  addVm: async (dispatch: any, param: any) => {
    const { dataCenterId, hostId, clusterId } = param
    dispatch(
      setVmInfo({
        visible: true,
        initialValues: {
          dataCenterId,
          hostId,
          clusterId,
          ...virtualMachineInitValue,
        },
        type: 'add',
        formData: addVirtualMachineItem,
      }),
    )
  },
  editVirtualMachine: async (dispatch: any, vmId: string) => {
    const vmInfo = await getVMInfo(vmId)
    if (!vmInfo) return
    const { vm, vmSwitchEntity = {}, vmHardware = {}, securityGroup, host } = vmInfo || {}
    const { unit, size } = splitUnit(vmHardware?.vmMemSize || '')
    const vmCpuSockets = vmHardware.vmCpuNum / vmHardware.vmCpuAduit
    const { hostId } = host
    const { state: vmState } = vm

    const vmEditFormInitValue = {
      cdPath: vmHardware.vmOsPath,
      vmId: vm?.vmId,
      vmMark: vm?.vmMark,
      vmName: vm?.vmName,
      os: 'win',
      version: 'Microsoft Windows Server 2016(64位)',
      vmCpuAduit: { inputValue: vmHardware.vmCpuAduit, unitValue: '核' },
      vmCpuSockets: { inputValue: vmCpuSockets, unitValue: '个' },
      vmMemSize: { inputValue: size, unitValue: unit },
      vmDiskSize: { inputValue: vmHardware?.vmDiskSize, unitValue: 'GB' },
      vmNetworkMac: vmHardware?.vmNetworkMac,
      MTU: vmSwitchEntity?.mtuSize,
      diskCreateType: vmHardware?.diskCreateType,
      securityGroup,
      vmSwitch: vmSwitchEntity,
      hostId,
      vmState,
      vmInfo,
    }
    dispatch(
      setVmInfo({
        visible: true,
        initialValues: vmEditFormInitValue,
        type: 'edit',
        formData: editVirtualMachineItem,
      }),
    )
  },
  closeVm: async (dispatch: any, values: (string | number)[]) => {
    dispatch(
      setCloseVm({
        visible: true,
        title: '关闭',
        content: '是否关闭虚拟机?',
        values,
      }),
    )
  },
  startUpVm: async (dispatch: any, values: (string | number)[]) => {
    dispatch(
      setStartUpVm({
        visible: true,
        title: '启动',
        content: '是否启动虚拟机?',
        values,
      }),
    )
  },
  deleteVm: async (dispatch: any, values: { hostId: string; ids: (string | number)[] }) => {
    dispatch(
      setDeleteVm({
        formData: DeleteFormData,
        visible: true,
        title: '删除虚拟机',
        content: '您确定要删除该虚拟机吗?',
        values,
      }),
    )
  },
  destroyVm: async (dispatch: any, values: (string | number)[]) => {
    dispatch(
      setDestroyVm({
        visible: true,
        title: '关闭电源',
        content: '是否关闭虚拟机电源?',
        values,
      }),
    )
  },
  moveVirtualMachine: async (dispatch: any, values: { hostId: string; vmId: string }) => {
    dispatch(
      setMoveVm({
        visible: true,
        title: '迁移虚拟机',
        initialValues: values,
        formData: changeHostItem,
      }),
    )
  },
  suspendVm: async (dispatch: any, values: (string | number)[]) => {
    dispatch(
      setSuspendVm({
        visible: true,
        title: '暂停',
        content: '是否暂停虚拟机?',
        values,
      }),
    )
  },
  resumeVm: async (dispatch: any, values: (string | number)[]) => {
    dispatch(
      setResumeVm({
        visible: true,
        title: '恢复',
        content: '是否恢复虚拟机?',
        values,
      }),
    )
  },
  restartVm: async (dispatch: any, values: (string | number)[]) => {
    dispatch(
      setRestartVm({
        visible: true,
        title: '重启',
        content: '是否重启虚拟机?',
        values,
      }),
    )
  },
  addVirtualSwitch: async (dispatch: any, param: any) => {
    dispatch(
      setVirtualSwitchInfo({
        visible: true,
        initialValues: {
          ...param,
        },
        type: 'add',
        formData: virtualSwitchFromData,
      }),
    )
  },
  editVirtualSwitch: async (dispatch: any, vsId: string) => {
    const vsInfo = await getVirtualSwitchInfo(vsId)
    const { flag, info } = vsInfo
    if (flag && info) {
      const {
        vmSwitchId,
        vmSwitchName,
        networkType, // 1为管理网络，2为业务网络，3为存储网络
        mtuSize,
        hostId,
        netMachine,
        ip,
        gateway,
        netMask,
        linkMode, // 静态、动态
        loadMode, // 均衡、主备
      } = info
      const initialValues = {
        vmSwitchId,
        vmSwitchName,
        networkType: verticalLineStrToArray(networkType),
        mtuSize,
        hostId,
        netMachine: verticalLineStrToArray(netMachine),
        ip,
        gateway,
        netMask,
        linkMode: verticalLineStrToArray(linkMode),
        loadMode: verticalLineStrToArray(loadMode),
      }
      dispatch(
        setVirtualSwitchInfo({
          visible: true,
          initialValues: initialValues,
          type: 'edit',
          formData: virtualSwitchFromData,
        }),
      )
    }
  },
  deleteVirtualSwitch: async (dispatch: any, values: (string | number)[]) => {
    dispatch(
      setVirtualSwitchDelete({
        visible: true,
        title: '删除虚拟交换机',
        content: '您确定要删除虚拟交换机吗?',
        values,
      }),
    )
  },
  addPool: async (dispatch: any, inHost: string | undefined) => {
    const getSelectList = await getHostList({ page: '1', limit: '1000' })
    const { list } = getSelectList
    const selectList = list.map(item => ({ label: item.hostName, value: item.hostId }))
    const initialValues = inHost ? { hostId: inHost } : {}
    dispatch(
      setPoolInfo({
        visible: true,
        initialValues,
        type: 'add',
        formData: oneFormData(selectList, inHost),
      }),
    )
  },
  deletePool: async (dispatch: any, values: (string | number)[]) => {
    dispatch(
      setDeletePool({
        visible: true,
        title: '删除存储池',
        content: '您确定要删除该存储池吗?',
        values,
      }),
    )
  },
  editPool: async (dispatch: any, id: string | number) => {
    const result = await poolDataInfo(String(id))
    const { storagePool } = result || { storagePool: { poolShowName: '' } }
    const { poolShowName } = storagePool
    await dispatch(
      setEditPool({
        visible: true,
        initialValues: { poolShowName, storagePoolId: id },
        title: '编辑存储池',
        formData: editPoolFormData,
      }),
    )
  },
  startPool: async (dispatch: any, id: string | number) => {
    const result = await poolDataInfo(String(id))
    const { storagePool } = result || { storagePool: { poolType: '' } }
    const { poolType } = storagePool
    await dispatch(
      setStartPool({
        visible: true,
        title: '启动存储池',
        content: '您确定要启动该存储池吗?',
        values: { poolType, storagePoolId: String(id) },
      }),
    )
  },
  stopPool: async (dispatch: any, id: string | number) => {
    const result = await poolDataInfo(String(id))
    const { storagePool } = result || { storagePool: { poolType: '' } }
    const { poolType } = storagePool
    await dispatch(
      setStopPool({
        visible: true,
        title: '暂停存储池',
        content: '您确定要暂停该存储池吗?',
        values: { poolType, storagePoolId: String(id) },
      }),
    )
  },
  formatPool: async (dispatch: any, id: string | number) => {
    const result = await poolDataInfo(String(id))
    const { storagePool } = result || { storagePool: { storageIp: '', hostId: '' } }
    const { storageIp, hostId } = storagePool
    const formatList =
      storageIp && hostId
        ? formatPoolFormData({ ip: storageIp, hostId })
        : formatPoolFormData(undefined)
    await dispatch(
      setFormatPool({
        visible: true,
        initialValues: { storagePoolId: id },
        title: '格式化',
        formData: formatList,
      }),
    )
  },
  addStorage: async (dispatch: any, value: BasicObj) => {
    const { storagePoolId, poolType } = value || {}
    const type = poolType === 'dir' ? '1' : poolType === 'iscsi' ? '3' : '5'
    dispatch(
      setVolumeInfo({
        visible: true,
        initialValues: { storagePoolId, storageType: type },
        type: 'add',
        formData: volumeFormData,
      }),
    )
  },
  delStorage: async (dispatch: any, values: (string | number)[]) => {
    dispatch(
      setDeleteVolume({
        visible: true,
        title: '删除存储卷',
        initialValues: { storageIds: values, rbFlag: true },
        formData: deleteVolumeFormData,
      }),
    )
  },
  downLoadStorage: async (dispatch: any, values: StorageVolume.IStorageVolumeType) => {
    const { storageId, storageVolumeName, filesystem } = values
    dispatch(
      setDownloadVolume({
        visible: true,
        title: '下载存储卷',
        content: '您确定要下载该存储卷吗?',
        values: { storageId, storageVolumeName, filesystem },
      }),
    )
  },
  delRecycleStorage: async (dispatch: any, values: (string | number)[]) => {
    dispatch(
      setDeleteRecycle({
        visible: true,
        title: '删除存储卷',
        content: '您确定要删除存储卷吗?',
        values,
      }),
    )
  },
  resumeStorage: async (dispatch: any, values: (string | number)[]) => {
    dispatch(
      setResumeStorage({
        visible: true,
        title: '恢复存储卷',
        content: '您确定要恢复存储卷吗?',
        values,
      }),
    )
  },
  addGroup: async (dispatch: any) => {
    dispatch(
      setGroupInfo({
        visible: true,
        initialValues: {},
        type: 'add',
        formData: groupFormData,
      }),
    )
  },
  editGroup: async (dispatch: any, id: string | number) => {
    const result = await groupInfo(String(id))
    const { securityGroup } = result || {
      securityGroup: { securityGroupName: '', securityGroupRemark: '' },
    }
    const { securityGroupName, securityGroupRemark } = securityGroup
    await dispatch(
      setGroupInfo({
        visible: true,
        initialValues: { securityGroupName, securityGroupRemark, securityGroupId: String(id) },
        type: 'edit',
        formData: groupFormData,
      }),
    )
  },
  deleteGroup: async (dispatch: any, values: (string | number)[]) => {
    dispatch(
      setDeleteGroup({
        visible: true,
        title: '删除安全组',
        content: '您确定要删除该安全组吗?',
        values,
      }),
    )
  },
  addRule: async (dispatch: any, values: BasicObj) => {
    const { securityGroupId } = values
    dispatch(
      setRuleInfo({
        visible: true,
        initialValues: { securityGroupId },
        type: 'add',
        formData: ruleformData,
      }),
    )
  },
  editRule: async (dispatch: any, id: string | number) => {
    const result = await ruleInfo(String(id))
    const { securityRule } = result || {
      securityRule: {},
    }
    await dispatch(
      setRuleInfo({
        visible: true,
        initialValues: { ...securityRule },
        type: 'edit',
        formData: ruleformData,
      }),
    )
  },
  deleteRule: async (dispatch: any, values: (string | number)[]) => {
    dispatch(
      setDeleteRule({
        visible: true,
        title: '删除安全规则',
        content: '您确定要删除该安全规则吗?',
        values,
      }),
    )
  },
}
