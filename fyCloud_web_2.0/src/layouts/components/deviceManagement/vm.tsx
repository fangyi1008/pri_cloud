import { useState } from 'react'
import { AppDispatch, useAppDispatch, useAppSelector } from '@redux/store'
import StepPopup from '@components/stepPopup/index'
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
  OsVersion,
  SelectNetwork,
  SelectSecurityGroup,
  HardDiskPool,
  HardDiskFile,
  VMMac,
  SelectHost,
  SelectCD,
  SelectMem,
  SelecctCpu,
} from './components'
import { FormDataType } from '@/components/multipleFormItem/type'
import { DeleteModal } from './components'
import win from '@/assets/icons/icon_os_windows.svg'
import linux from '@/assets/icons/icon_os_linux.svg'
import eventEmitter from '@/events/index'
import {
  saveVirtualMachine,
  updateVirtualMachine,
  deleteVm,
  deleteDbVm,
  startVm,
  shutDownVm,
  destroyVm,
  suspendVm,
  resumeVm,
  restartVm,
  moveVm,
} from '@/api/modules/virtualMachineService'
import { getMenuData } from '@/redux/modules/menu/menu'
import { setTaskList } from '@/redux/modules/taskBoard'
import { message, Modal } from 'antd'
import { ITaskType, Vm } from '@/api/interface'
import { useNavigate } from 'react-router'
import { isObject } from '@/utils/is'
import { batchOperationTip } from '@/utils/util'
import AntdFormPopup from '@components/antdFormPopup'

const baseVmInfo: FormDataType[] = [
  {
    id: 'vmName',
    content: '显示名称',
    type: 'input',
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      placeholder: '请选择输入显示名称',
    },
  },
  {
    id: 'vmMark',
    content: '描述',
    type: 'input',
    componentProps: {
      placeholder: '请选择输入显示名称',
    },
  },
]

const oneFormData: FormDataType[] = [
  ...baseVmInfo,
  {
    id: 'os',
    content: '操作系统',
    type: 'selectBoxGroup',
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      data: [
        {
          label: 'win',
          value: 'win',
          src: win,
        },
        {
          label: 'linux',
          value: 'linux',
          src: linux,
        },
      ],
    },
  },
  {
    id: 'version',
    content: '版本',
    type: 'custom',
    component: OsVersion,
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      placeholder: '请选择版本',
    },
  },
]

const baseVMFormItem: FormDataType[] = [
  {
    id: 'cpu-group',
    content: 'cpu-group',
    type: 'groupFrom',
    groupItems: [
      {
        id: 'vmCpuSockets',
        content: 'cpu',
        type: 'custom',
        component: SelecctCpu,
        formItemProps: {
          required: true,
          rules: [
            {
              required: true,
            },
          ],
        },
        componentProps: {
          allowDelete: false,
          unitOptions: [{ label: '个', value: '个' }],
          min: 2,
          max: 4,
          step: 1,
        },
      },
      {
        id: 'vmCpuAduit',
        content: 'cpu核数',
        type: 'custom',
        component: SelecctCpu,
        formItemProps: {
          required: true,
          rules: [
            {
              required: true,
            },
            {
              validator: (_: any, value: any) => {
                if (!value) {
                  return Promise.reject()
                }
                const { inputValue, unitValue, UnitObject } = value
                if (!inputValue || !unitValue) {
                  return Promise.reject()
                }
                return Promise.resolve()
              },
            },
          ],
        },
        componentProps: {
          unitOptions: [{ label: '核', value: '核' }],
          min: 2,
          max: 4,
          step: 1,
          allowDelete: false,
        },
      },
    ],
  },
  {
    id: 'vmMemSize',
    content: '内存',
    component: SelectMem,
    type: 'custom',
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      allowDelete: false,
      unitOptions: [
        { value: 'GB', label: 'GB' },
        { value: 'MB', label: 'MB' },
      ],
      min: 2,
      max: 10,
      step: 1,
    },
  },
  {
    id: 'netWork-group',
    content: 'netWork-group',
    type: 'groupFrom',
    groupItems: [
      {
        id: 'vmSwitch',
        content: '网络',
        type: 'custom',
        component: SelectNetwork,
        formItemProps: {
          required: true,
          rules: [
            {
              required: true,
            },
          ],
        },
        componentProps: {
          allowDelete: false,
          popupTitle: '选择虚拟交换机',
          tableRowKey: 'vmSwitchId',
          valueField: 'vmSwitchName',
          resultIsObject: true,
          readOnly: true,
          columns: [
            {
              title: '名称',
              dataIndex: 'vmSwitchName',
              key: 'vmSwitchName',
            },
            {
              title: 'MTU',
              dataIndex: 'mtuSize',
              key: 'mtuSize',
            },
            {
              title: 'IP地址',
              dataIndex: 'ip',
              key: 'ip',
            },
            {
              title: '网关',
              dataIndex: 'gateway',
              key: 'gateway',
            },
            {
              title: '子网掩码',
              dataIndex: 'netMask',
              key: 'netMask',
            },
          ],
        },
      },
      {
        id: 'vmNetworkMac',
        content: 'Mac地址',
        type: 'custom',
        component: VMMac,
        formItemProps: {
          rules: [
            {
              pattern: /^([0-9a-fA-F]{2})(([/\s:-][0-9a-fA-F]{2}){5})$/,
              message: '请输入正确的MAC地址！',
            },
          ],
        },
        componentProps: {
          allowDelete: false,
          placeholder: '请输入Mac地址',
          readOnly: true,
        },
      },
      {
        id: 'MTU',
        content: 'MTU',
        type: 'inputNumber',
        formItemProps: {
          required: true,
          rules: [
            {
              required: true,
            },
          ],
        },
        componentProps: {
          style: { width: '100%' },
          placeholder: '请输入MTU',
          step: 1,
        },
      },
      {
        id: 'securityGroup',
        content: '安全组',
        type: 'custom',
        component: SelectSecurityGroup,
        componentProps: {
          allowDelete: true,
          resultIsObject: true,
          popupTitle: '选择安全组',
          tableRowKey: 'securityGroupId',
          valueField: 'securityGroupName',
          readOnly: true,
          columns: [
            {
              title: '名称',
              dataIndex: 'securityGroupName',
              key: 'securityGroupName',
            },
            {
              title: '备注',
              dataIndex: 'securityGroupRemark',
              key: 'securityGroupRemark',
            },
          ],
        },
      },
    ],
  },
]

const twoFormData: FormDataType[] = [
  ...baseVMFormItem,
  {
    id: 'hardDisk-group',
    content: 'hardDisk-group',
    type: 'groupFrom',
    groupItems: [
      {
        id: 'vmDiskSize',
        content: '硬盘',
        type: 'unitNumber',
        formItemProps: {
          required: true,
          rules: [
            {
              required: true,
            },
          ],
        },
        componentProps: {
          allowDelete: false,
          unitOptions: [{ label: 'GB', value: 'GB' }],
          min: 1,
          max: 1000,
          step: 1,
        },
      },
      {
        id: 'diskCreateType',
        content: '类型',
        type: 'radio',
        formItemProps: {
          required: true,
          rules: [
            {
              required: true,
            },
          ],
        },
        componentProps: {
          options: [
            { label: '新建文件', value: 1 },
            { label: '已有文件', value: 2 },
          ],
        },
      },
      {
        type: 'custom',
        id: 'storagePool',
        content: '存储池',
        component: HardDiskPool,
        formItemProps: {
          required: true,
          rules: [
            {
              required: true,
            },
          ],
        },
        componentProps: {
          popupTitle: '选择存储池',
          tableRowKey: 'storagePoolId',
          valueField: 'storagePoolName',
          columns: [
            {
              title: '名称',
              dataIndex: 'storagePoolName',
              key: 'storagePoolName',
            },
            {
              title: '类型',
              dataIndex: 'poolTypeText',
              key: 'poolTypeText',
            },
            {
              title: '路基',
              dataIndex: 'storagePoolPath',
              key: 'storagePoolPath',
            },
            {
              title: '总容量',
              dataIndex: 'capacityText',
              key: 'capacityText',
            },
            {
              title: '状态',
              dataIndex: 'statusText',
              key: 'statusText',
            },
          ],
          resultIsObject: true,
          readOnly: true,
        },
      },
      {
        type: 'custom',
        id: 'basicVolume',
        content: '镜像文件',
        component: HardDiskFile,
        formItemProps: {
          required: true,
          rules: [
            {
              required: true,
              message: '请选择文件镜像',
            },
          ],
        },
        componentProps: {
          allowDelete: false,
          resultIsObject: true,
          fieldNames: {
            valueField: 'storagePoolId',
            title: 'storagePoolName',
            subtitle: 'poolTypeText',
            describe: 'freeSpaceText',
          },
          columns: [
            {
              title: '文件名称',
              dataIndex: 'storageVolumeName',
              key: 'storageVolumeName',
            },
            {
              title: '类型',
              dataIndex: 'filesystem',
              key: 'filesystem',
            },
            {
              title: '文件大小',
              dataIndex: 'fileSize',
              key: 'fileSize',
            },
          ],
          valueField: 'storageVolumeName',
          popupTitle: '选择存储',
          isShowPool: true,
          tableRowKey: 'storageId',
          readOnly: true,
        },
      },
    ],
  },
  {
    type: 'custom',
    id: 'driveCD',
    content: '光驱',
    component: SelectCD,
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
          message: '请选择光驱',
        },
      ],
    },
    componentProps: {
      allowDelete: false,
      resultIsObject: true,
      fieldNames: {
        valueField: 'storagePoolId',
        title: 'storagePoolName',
        subtitle: 'poolTypeText',
        describe: 'freeSpaceText',
      },
      columns: [
        {
          title: '文件名称',
          dataIndex: 'storageVolumeName',
          key: 'storageVolumeName',
        },
        {
          title: '类型',
          dataIndex: 'filesystem',
          key: 'filesystem',
        },
        {
          title: '文件大小',
          dataIndex: 'fileSize',
          key: 'fileSize',
        },
      ],
      valueField: 'storagePath',
      popupTitle: '选择光驱',
      isShowPool: true,
      tableRowKey: 'storageId',
      readOnly: true,
    },
  },

  // 暂时没有用到后期会加
  // {
  //   id: 'CD-group',
  //   content: 'CD-group',
  //   type: 'groupFrom',
  //   groupItems: [
  //     {
  //       id: 'driveCD',
  //       content: '光驱',
  //       type: 'custom',
  //       component: CDDriver,
  //       componentProps: {
  //         allowDelete: false,
  //         fieldNames: {
  //           valueField: 'id',
  //           title: 'poolShowName',
  //           subtitle: 'poolType',
  //           describe: 'size',
  //         },
  //         columns: [
  //           {
  //             title: '文件名称',
  //             dataIndex: 'fileName',
  //             key: 'fileName',
  //           },
  //           {
  //             title: '文件大小',
  //             dataIndex: 'fileSize',
  //             key: 'fileSize',
  //           },
  //           {
  //             title: '类型',
  //             dataIndex: 'type',
  //             key: 'type',
  //           },
  //         ],
  //         valueField: 'fileName',
  //         popupTitle: '选择存储',
  //         isShowPool: true,
  //         tableRowKey: 'id',
  //         readOnly: true,
  //       },
  //     },
  //     {
  //       id: 'linkType',
  //       content: '链接方式',
  //       type: 'select',
  //       componentProps: {
  //         options: [
  //           { value: '镜像文件', label: '镜像文件' },
  //           { value: 'CD/DVD', label: 'CD/DVD' },
  //         ],
  //         placeholder: '请选择链接类型',
  //       },
  //     },
  //   ],
  // },
]

export const DeleteFormData: FormDataType[] = [
  {
    id: 'rbFlag',
    content: '是否删除存储卷',
    type: 'radio',
    componentProps: {
      options: [
        { value: true, label: '是' },
        { value: false, label: '否' },
      ],
    },
  },
]

export const addVirtualMachineItem = [
  {
    key: '1',
    title: '基本信息',
    data: oneFormData,
  },
  {
    key: '2',
    title: '硬件信息',
    data: twoFormData,
  },
]

export const editVirtualMachineItem = [
  {
    key: '1',
    title: '基本信息',
    data: baseVmInfo,
  },
  {
    key: '2',
    title: '硬件信息',
    data: [
      ...baseVMFormItem,
      {
        id: 'vmDiskSize',
        content: '硬盘',
        type: 'unitNumber',
        formItemProps: {
          required: true,
          rules: [
            {
              required: true,
            },
          ],
        },
        componentProps: {
          allowDelete: false,
          unitOptions: [
            { label: 'GB', value: 'GB' },
            // { label: 'TB', value: 'TB' },
            // { label: 'MB', value: 'MB' },
          ],
          min: 1,
          max: 1000,
          step: 1,
        },
      },
      {
        id: 'cdPath',
        content: '光驱',
        type: 'input',
        componentProps: {
          disabled: true,
        },
      },
    ],
  },
]

const changeTypeItem = [
  {
    id: 'moveType',
    content: '迁移类型',
    type: 'selectBoxGroup',
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      data: [
        {
          label: '冷迁移',
          value: '冷迁移',
          describe: '冷迁移既支持本地存储也支持共享存储',
        },
        {
          label: '热迁移',
          value: '热迁移',
          describe: '热迁移不支持本地存储',
        },
      ],
      width: 200,
      space: 14,
      height: 40,
      direction: 'column',
    },
  },
]
const hostItem = [
  {
    id: 'hostInfo',
    content: '主机类型',
    type: 'custom',
    component: SelectHost,
    formItemProps: {
      required: true,
      rules: [
        {
          required: true,
        },
      ],
    },
    componentProps: {
      allowDelete: false,
      placeholder: '',
      readOnly: true,
    },
  },
]

export const changeHostItem = [
  {
    key: '1',
    title: '选择迁移类型',
    data: changeTypeItem,
  },
  {
    key: '2',
    title: '目的主机',
    data: hostItem,
  },
]

const isDisabledByState = (selectedKey: React.Key[], selectedRow: Vm.IVmType[], type: string) => {
  const length = selectedKey.length
  if (length <= 0) {
    return true
  }
  if (length === 1) {
    const { state } = selectedRow[0] || {}
    return computeVmOperationDisable(state, type)
  }
  return false
}

export const vmListOperations = [
  {
    key: 'editVirtualMachine',
    type: 'editVm',
    text: '编辑',
    disabledFn: (selectedKey: React.Key[], selectedRow: Vm.IVmType[] = []) => {
      if (selectedKey.length !== 1) {
        return true
      }
      const { state } = selectedRow[0] || {}
      if (state === '失联' || !state) {
        return true
      }
      return false
    },
  },
  {
    key: 'deleteVm',
    type: 'delVm',
    text: '删除',
    disabledFn: (selectedKey: React.Key[], selectedRow: Vm.IVmType[] = []) =>
      selectedKey?.length <= 0,
  },
  {
    key: 'more',
    type: 'operate',
    text: '批量操作',
    isSelect: true,
    menuData: [
      {
        key: 'startUpVm',
        type: 'startUp',
        label: '启动',
        disabledFn: (selectedKey: React.Key[], selectedRow: Vm.IVmType[] = []) =>
          isDisabledByState(selectedKey, selectedRow, 'startUpVm'),
      },
      {
        key: 'suspendVm',
        type: 'suspend',
        label: '暂停',
        disabledFn: (selectedKey: React.Key[], selectedRow: Vm.IVmType[] = []) =>
          isDisabledByState(selectedKey, selectedRow, 'suspendVm'),
      },
      {
        key: 'resumeVm',
        type: 'resumeVm',
        label: '恢复',
        disabledFn: (selectedKey: React.Key[], selectedRow: Vm.IVmType[] = []) =>
          isDisabledByState(selectedKey, selectedRow, 'resumeVm'),
      },
      {
        key: 'restartVm',
        type: 'restartVm',
        label: '重启',
        disabledFn: (selectedKey: React.Key[], selectedRow: Vm.IVmType[] = []) =>
          isDisabledByState(selectedKey, selectedRow, 'restartVm'),
      },
      {
        key: 'closeVm',
        type: 'close',
        label: '安全关闭',
        disabledFn: (selectedKey: React.Key[], selectedRow: Vm.IVmType[] = []) =>
          isDisabledByState(selectedKey, selectedRow, 'closeVm'),
      },
      {
        key: 'destroyVm',
        type: 'close',
        label: '关闭电源',
        disabledFn: (selectedKey: React.Key[], selectedRow: Vm.IVmType[] = []) =>
          isDisabledByState(selectedKey, selectedRow, 'destroyVm'),
      },
    ],
  },
]

export const vmStatusToDisableOperate: { [key: string]: string[] } = {
  挂起: ['consoleVM', 'startUpVm', 'suspendVm'],
  运行: ['startUpVm', 'resumeVm'],
  关机: ['closeVm', 'destroyVm', 'suspendVm', 'resumeVm', 'restartVm', 'consoleVM'],
  失联: [
    'restartVm',
    'consoleVM',
    'startUpVm',
    'resumeVm',
    'closeVm',
    'destroyVm',
    'suspendVm',
    'editVirtualMachine',
    'moveVirtualMachine',
    'snapshot',
  ],
  异常: [
    'restartVm',
    'consoleVM',
    'startUpVm',
    'resumeVm',
    'closeVm',
    'destroyVm',
    'suspendVm',
    'editVirtualMachine',
    'moveVirtualMachine',
    'snapshot',
  ],
}

export const computeVmOperationDisable = (vmStatus: string, operationType: string): boolean => {
  const disableOperateArray = vmStatusToDisableOperate[vmStatus]
  if (!disableOperateArray) {
    return true
  }
  return disableOperateArray.includes(operationType)
}

export const vmOperations = [
  {
    key: 'startUpVm',
    type: 'startUp',
    text: '启动',
    disabledFn: (vmInfo: Vm.IVmType) => computeVmOperationDisable(vmInfo.state, 'startUpVm'),
  },
  {
    key: 'closeVm',
    type: 'close',
    text: '安全关闭',
    disabledFn: (vmInfo: Vm.IVmType) => computeVmOperationDisable(vmInfo.state, 'closeVm'),
  },
  {
    key: 'destroyVm',
    type: 'close',
    text: '关闭电源',
    disabledFn: (vmInfo: Vm.IVmType) => computeVmOperationDisable(vmInfo.state, 'destroyVm'),
  },
  {
    key: 'editVirtualMachine',
    type: 'edit',
    text: '修改虚拟机',
    disabledFn: (vmInfo: Vm.IVmType) =>
      computeVmOperationDisable(vmInfo.state, 'editVirtualMachine'),
  },
  {
    key: 'consoleVM',
    type: 'edit',
    text: '控制台',
    disabledFn: (vmInfo: Vm.IVmType) => computeVmOperationDisable(vmInfo.state, 'consoleVM'),
  },
  {
    key: 'moveVirtualMachine',
    type: 'move',
    text: '迁移虚拟机',
    disabledFn: (vmInfo: Vm.IVmType) =>
      computeVmOperationDisable(vmInfo.state, 'moveVirtualMachine'),
  },
  {
    key: 'deleteVm',
    type: 'delete',
    text: '删除虚拟机',
    disabledFn: (vmInfo: Vm.IVmType) => computeVmOperationDisable(vmInfo.state, 'deleteVm'),
  },
  {
    key: 'more',
    type: 'operate',
    text: '更多操作',
    isSelect: true,
    menuData: [
      {
        key: 'suspendVm',
        type: 'suspend',
        label: '暂停',
        disabledFn: (vmInfo: Vm.IVmType) => computeVmOperationDisable(vmInfo.state, 'suspendVm'),
      },
      {
        key: 'resumeVm',
        type: 'resumeVm',
        label: '恢复',
        disabledFn: (vmInfo: Vm.IVmType) => computeVmOperationDisable(vmInfo.state, 'resumeVm'),
      },
      {
        key: 'restartVm',
        type: 'restartVm',
        label: '重启',
        disabledFn: (vmInfo: Vm.IVmType) => computeVmOperationDisable(vmInfo.state, 'restartVm'),
      },
    ],
  },
]

export const virtualMachineInitValue = {
  vmMark: '',
  vmName: '',
  os: 'win',
  version: 'Microsoft Windows Server 2016(64位)',
  vmCpuAduit: { inputValue: 2, unitValue: '个' },
  vmCpuSockets: { inputValue: 2, unitValue: '核' },
  vmMemSize: { inputValue: 10, unitValue: 'GB' },
  vmNetworkMac: '',
  MTU: '10',
  securityGroup: undefined,
  vmDiskSize: { inputValue: 50, unitValue: 'GB' },
  diskCreateType: 1,
  storagePool: undefined,
  basicVolume: undefined,
  linkType: '镜像文件',
}

const addVirtualMachineFn = async (vmInfo: any, prams: any) => {
  const { dataCenterId = '', clusterId = '', hostId = '' } = prams
  const {
    diskCreateType,
    vmCpuAduit: { inputValue: vmCpuAduit },
    vmCpuSockets: { inputValue: vmCpuSockets },
    vmDiskSize: { inputValue: vmDiskSize },
    vmMemSize: { inputValue: vmMemSize, unitValue: menUnit },
    vmMark,
    vmName,
    vmNetworkMac,
    vmSwitch,
    securityGroup,
    storagePool,
    basicVolume,
    driveCD,
  } = vmInfo
  const { vmSwitchId } = vmSwitch
  const { storagePath } = driveCD
  let securityGroupParam = {}
  const { securityGroupId } = securityGroup || {}
  if (securityGroupId) {
    securityGroupParam = { securityGroupId }
  }
  let vmNetworkMacParam = {}
  if (vmNetworkMac) {
    vmNetworkMacParam = { vmNetworkMac }
  }
  let diskData = {}
  if (diskCreateType === 1) {
    const { storagePoolId } = storagePool || {}
    if (storagePoolId) {
      diskData = { storagePoolId }
    }
  }
  if (diskCreateType === 2) {
    const { storagePoolId, storageId } = basicVolume || {}
    diskData = { storagePoolId, basicVolumeId: storageId }
  }
  const saveVmInfo = {
    vmName,
    vmMark,
    hostId,
    dataCenterId,
    clusterId,
    vmOs: 'x86_64',
    vmCpuSockets,
    vmCpuAduit,
    vmMemSize: vmMemSize + menUnit,
    vmDiskSize,
    vmSwitchId,
    diskCreateType,
    vmOsPath: storagePath,
    ...diskData,
    ...securityGroupParam,
    ...vmNetworkMacParam,
  }
  return await saveVirtualMachine(saveVmInfo)
}

const editVirtualMachineFn = async (vmInfo: any, prams: any) => {
  const { vm, vmHardware, basicStorageEntity, storageEntity } = prams?.vmInfo || {}
  const { clusterId, dataCenterId, hostId, vmId } = vm
  const { diskCreateType, vmOs, vmOsPath } = vmHardware
  const {
    vmCpuAduit: { inputValue: vmCpuAduit },
    vmCpuSockets: { inputValue: vmCpuSockets },
    vmDiskSize: { inputValue: vmDiskSize },
    vmMemSize: { inputValue: vmMemSize, unitValue: menUnit },
    vmMark,
    vmName,
    vmNetworkMac,
    vmSwitch,
    securityGroup,
    storagePool,
    basicVolume,
  } = vmInfo
  const { vmSwitchId } = vmSwitch
  let securityGroupParam = {}
  const { securityGroupId } = securityGroup || {}
  if (securityGroupId) {
    securityGroupParam = { securityGroupId }
  }
  let vmNetworkMacParam = {}
  if (vmNetworkMac) {
    vmNetworkMacParam = { vmNetworkMac }
  }
  let diskData = {}
  if (diskCreateType === 1) {
    const { storagePoolId } = basicStorageEntity || {}
    if (storagePoolId) {
      diskData = { storagePoolId }
    }
  }
  if (diskCreateType === 2) {
    const { storagePoolId, storageId } = basicStorageEntity || {}
    diskData = { storagePoolId, basicVolumeId: storageId }
  }

  const saveVmInfo = {
    vmId,
    vmName,
    vmMark,
    hostId,
    dataCenterId,
    clusterId,
    vmOs,
    vmCpuSockets,
    vmCpuAduit,
    vmMemSize: vmMemSize + menUnit,
    vmDiskSize,
    vmSwitchId,
    diskCreateType,
    vmOsPath,
    ...diskData,
    ...securityGroupParam,
    ...vmNetworkMacParam,
  }
  return await updateVirtualMachine(saveVmInfo)
}

export function VmDevice() {
  const [forStepCurrent, setForStepCurrent] = useState(0)
  const dispatch = useAppDispatch()
  const { edit } = useAppSelector(store => store.vmOperate)
  const { visible, initialValues, type, formData } = edit

  const onFinish = async (vmInfo: Record<string, any>) => {
    if (type === 'add') {
      const reulst = await addVirtualMachineFn(vmInfo, initialValues)
      if (reulst) {
        message.success('虚拟机新增成功', 1)
        dispatch(getMenuData())
        dispatch(setVmInfo({ visible: false, type: 'add', initialValues: [], formData: [] }))
        eventEmitter.emit('vmListLoad', { type: 'reset' })
        setForStepCurrent(0)
      }
    } else {
      const reulst = await editVirtualMachineFn(vmInfo, initialValues)
      if (reulst) {
        message.success('虚拟机修改成功', 1)
        dispatch(getMenuData())
        dispatch(setVmInfo({ visible: false, type: 'add', initialValues: [], formData: [] }))
        eventEmitter.emit('refreshVM')
        eventEmitter.emit('vmListLoad', { type: 'reload' })
        eventEmitter.emit('vmListLoad', { type: 'clearSelected' })
        setForStepCurrent(0)
      }
    }
  }
  const onFinishFailed = (data: any) => {
    const { errorFields = [] } = data
    for (let i = 0; i < errorFields.length; i++) {
      const currentError = errorFields[i]
      if (currentError && currentError?.name?.[0] === 'storagePool') {
        eventEmitter.emit('groupError', 'vmDiskSize')
      }
    }
  }
  const onCancel = () => {
    setForStepCurrent(0)
    dispatch(setVmInfo({ visible: false, initialValues: {}, type: 'add', formData: [] }))
  }

  const onStepChangeHandle = (current: number) => {
    setForStepCurrent(current)
  }

  const transformResultData = (data: Record<string, any>) => {
    const newTransformValue: Record<string, any> = {}
    const fileType: Record<string, any> = { 1: '新建文件夹', 2: '已有文件' }
    if (isObject(data)) {
      for (const key in data) {
        switch (key) {
          case 'vmSwitch':
            newTransformValue[key] = data[key] ? data[key]['vmSwitchName'] : ''
            break
          case 'securityGroup':
            newTransformValue[key] = data[key] ? data[key]['securityGroupName'] : ''
            break
          case 'driveCD':
            newTransformValue[key] = data[key] ? data[key]['storagePath'] : ''
            break
          case 'basicVolume':
            newTransformValue[key] = data[key] ? data[key]['storageVolumeName'] : ''
            break
          case 'storagePool':
            newTransformValue[key] = data[key] ? data[key]['storagePoolName'] : ''
            break
          case 'diskCreateType':
            newTransformValue[key] = data[key] ? fileType[data[key]] : ''
            break
          default:
            newTransformValue[key] = data[key]
            break
        }
      }
    }

    return { ...data, ...newTransformValue }
  }

  return visible ? (
    <StepPopup
      transformResultData={transformResultData}
      onFinishFailed={onFinishFailed}
      formInitialValues={initialValues}
      onStepChange={onStepChangeHandle}
      width={'800px'}
      onCancel={onCancel}
      current={forStepCurrent}
      popupTitle={type === 'add' ? '新增虚拟机' : '修改虚拟机'}
      onFinish={onFinish}
      isShowResult
      visible={visible}
      steps={formData}
    />
  ) : null
}

export function DeleteVm() {
  const dispatch = useAppDispatch()
  const navigate = useNavigate()
  const { delete: delteeData } = useAppSelector(store => store.vmOperate)
  const { visible, title, content, values, formData } = delteeData
  const deleteVmHandle = async (value: Record<string, any>) => {
    const { rbFlag } = value
    const { ids = [], hostId = '' } = values
    const {
      flag,
      errorList = [],
      successList = [],
    } = await deleteVm({ vmIds: ids, rbFlag: rbFlag ? true : false })
    const path = hostId ? `host/${hostId}` : ''
    dispatch(setTaskList([...errorList, ...successList]))
    if (flag) {
      message.success('虚拟机删除成功', 2)
      dispatch(getMenuData())
      eventEmitter.emit('vmListLoad', { type: 'reload' })
      eventEmitter.emit('vmListLoad', { type: 'clearSelected' })
      path && navigate(path)
      handleCancel()
    }
    if (!flag) {
      const errorVmName = errorList.map(item => item.operObj).join('、')
      const errorVmId = errorList.map(item => item.vmId)
      Modal.confirm({
        title: '虚拟机删除提示！',
        content: `${errorVmName}虚拟机已经失联,是否在数据库删除这个虚拟机`,
        onOk: async close => {
          const result = await deleteDbVm(errorVmId)
          if (!result) {
            close()
            handleCancel()
            return
          }
          path && navigate(path)
          dispatch(getMenuData())
          eventEmitter.emit('vmListLoad', { type: 'reload' })
          eventEmitter.emit('vmListLoad', { type: 'clearSelected' })
          handleCancel()
          close()
        },
      })
    }
  }

  const handleCancel = () => {
    dispatch(setDeleteVm({ visible: false, title: '', content: '', values: {}, formData: [] }))
  }
  return (
    <AntdFormPopup
      popupTitle={title}
      tipContent={content}
      visible={visible}
      width={'600px'}
      height={'300px'}
      formInitialValues={{ rbFlag: false }}
      formData={formData}
      labelCol={{ span: 6 }}
      wrapperCol={{ span: 18 }}
      onFinish={deleteVmHandle}
      onCancel={handleCancel}
    />
    // <DeleteModal
    //   visible={visible}
    //   title={title}
    //   content={content}
    //   handleCancel={handleCancel}
    //   handleOk={deleteVmHandle}
    // />
  )
}

interface IBatchOperationSuccess {
  dispatch: AppDispatch
  errorList: ITaskType[]
  successList: ITaskType[]
  values: (string | number)[]
  flag: boolean
  type: string
}

function batchOperationSuccess(param: IBatchOperationSuccess) {
  const { dispatch, errorList, successList, values, flag, type } = param
  dispatch(setTaskList([...errorList, ...successList]))
  if (flag) {
    message.success(`虚拟机${type}成功`, 2)
  }
  if (!flag) {
    batchOperationTip(values, errorList, successList, type)
  }
  if (successList.length > 0) {
    dispatch(getMenuData())
    eventEmitter.emit('refreshVM')
    eventEmitter.emit('vmListLoad', { type: 'reload' })
    eventEmitter.emit('vmListLoad', { type: 'clearSelected' })
  }
}

export function StartUpVm() {
  const dispatch = useAppDispatch()
  const { startUp } = useAppSelector(store => store.vmOperate)
  const { visible, title, content, values } = startUp
  const startUpVm = async () => {
    const { flag, errorList = [], successList = [] } = await startVm(values)
    batchOperationSuccess({
      dispatch,
      errorList,
      successList,
      values,
      flag,
      type: '启动',
    })
    handleCancel()
  }
  const handleCancel = () => {
    dispatch(setStartUpVm({ visible: false, title: '', content: '', values: [] }))
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={startUpVm}
    />
  )
}

export function CloseVm() {
  const dispatch = useAppDispatch()
  const { close } = useAppSelector(store => store.vmOperate)
  const { visible, title, content, values } = close
  const closeVm = async () => {
    const { flag, errorList = [], successList = [] } = await shutDownVm(values)
    batchOperationSuccess({
      dispatch,
      errorList,
      successList,
      values,
      flag,
      type: '关闭',
    })
    handleCancel()
  }
  const handleCancel = () => {
    dispatch(setCloseVm({ visible: false, title: '', content: '', values: [] }))
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={closeVm}
    />
  )
}

export function DestroyVm() {
  const dispatch = useAppDispatch()
  const { destroy } = useAppSelector(store => store.vmOperate)
  const { visible, title, content, values } = destroy
  const destroyVmHandle = async () => {
    const { flag, errorList = [], successList = [] } = await destroyVm(values)
    batchOperationSuccess({
      dispatch,
      errorList,
      successList,
      values,
      flag,
      type: '关闭电源',
    })
    handleCancel()
  }
  const handleCancel = () => {
    dispatch(setDestroyVm({ visible: false, title: '', content: '', values: [] }))
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={destroyVmHandle}
    />
  )
}

export function MoveVirtualMachine() {
  const [forStepCurrent, setForStepCurrent] = useState(0)
  const dispatch = useAppDispatch()
  const { move } = useAppSelector(store => store.vmOperate)
  const { visible, initialValues, formData, title } = move

  const moveVirtualMachine = async (vmInfo: Record<string, any>) => {
    const { hostId, vmId } = initialValues
    const { moveType, hostInfo } = vmInfo
    if (hostId === hostInfo.key) {
      Modal.error({
        title: '操作提示',
        content: '虚拟机不允许迁移到同一台主机，请选择另一台主机。',
      })
      return
    }
    const moveVmInfo = {
      vmIds: [vmId],
      moveType: moveType === '冷迁移' ? 1 : 2,
      hostId,
      destHostId: hostInfo.key,
    }
    const { flag, errorList = [], successList = [] } = await moveVm(moveVmInfo)
    dispatch(setTaskList([...errorList, ...successList]))
    if (flag) {
      dispatch(getMenuData())
      message.success('虚拟机迁移成功', 2)
      eventEmitter.emit('refreshVM')
      eventEmitter.emit('vmListLoad', { type: 'reload' })
      eventEmitter.emit('vmListLoad', { type: 'clearSelected' })
      handleCancel()
    }
    if (!flag) {
      Modal.error({
        title: '操作提示',
        content: `${errorList[0]?.errorMsg}`,
      })
    }
  }

  const onCancel = () => {
    setForStepCurrent(0)
    handleCancel()
  }

  const handleCancel = () => {
    dispatch(setMoveVm({ visible: false, title: '', initialValues: {}, formData: [] }))
  }

  const onStepChangeHandle = (current: number) => {
    setForStepCurrent(current)
  }

  return (
    <StepPopup
      formInitialValues={initialValues}
      onStepChange={onStepChangeHandle}
      width={'800px'}
      onCancel={onCancel}
      current={forStepCurrent}
      popupTitle={title}
      onFinish={moveVirtualMachine}
      isShowResult
      visible={visible}
      steps={formData}
    />
  )
}

export function SuspendVm() {
  const dispatch = useAppDispatch()
  const { suspend } = useAppSelector(store => store.vmOperate)
  const { visible, title, content, values } = suspend
  const suspendVmHandle = async () => {
    const { flag, errorList = [], successList = [] } = await suspendVm(values)
    batchOperationSuccess({
      dispatch,
      errorList,
      successList,
      values,
      flag,
      type: '暂停',
    })
    handleCancel()
  }
  const handleCancel = () => {
    dispatch(setSuspendVm({ visible: false, title: '', content: '', values: [] }))
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={suspendVmHandle}
    />
  )
}

export function ResumeVm() {
  const dispatch = useAppDispatch()
  const { resume } = useAppSelector(store => store.vmOperate)
  const { visible, title, content, values } = resume
  const resumeVmHandle = async () => {
    const { flag, errorList = [], successList = [] } = await resumeVm(values)
    batchOperationSuccess({
      dispatch,
      errorList,
      successList,
      values,
      flag,
      type: '恢复',
    })
    handleCancel()
  }
  const handleCancel = () => {
    dispatch(setResumeVm({ visible: false, title: '', content: '', values: [] }))
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={resumeVmHandle}
    />
  )
}

export function RestartVm() {
  const dispatch = useAppDispatch()
  const { restart } = useAppSelector(store => store.vmOperate)
  const { visible, title, content, values } = restart
  const restartVmHandle = async () => {
    const { flag, errorList = [], successList = [] } = await restartVm(values)
    batchOperationSuccess({
      dispatch,
      errorList,
      successList,
      values,
      flag,
      type: '重启',
    })
    handleCancel()
  }
  const handleCancel = () => {
    dispatch(setRestartVm({ visible: false, title: '', content: '', values: [] }))
  }
  return (
    <DeleteModal
      visible={visible}
      title={title}
      content={content}
      handleCancel={handleCancel}
      handleOk={restartVmHandle}
    />
  )
}
