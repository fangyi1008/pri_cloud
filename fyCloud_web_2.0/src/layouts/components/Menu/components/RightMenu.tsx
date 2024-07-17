import type { MenuProps } from 'antd'
import { Dropdown } from 'antd'
import { computeVmOperationDisable } from '../../deviceManagement'
type DeviceMenuItemType = { label: string; key: string; disabled?: (record: any) => boolean }

const deviceMenu: Record<string, DeviceMenuItemType[]> = {
  compute: [
    {
      label: '添加数据中心',
      key: 'addDataCenter',
    },
    {
      label: '添加集群',
      key: 'addCluster',
    },
  ],
  dataCenter: [
    {
      label: '添加集群',
      key: 'addCluster',
    },
    {
      label: '添加主机',
      key: 'addHost',
    },
    {
      label: '编辑数据中心',
      key: 'editDataCenter',
    },
    {
      label: '删除数据中心',
      key: 'deleteDataCenter',
    },
  ],
  cluster: [
    {
      label: '添加主机',
      key: 'addHost',
    },
    {
      label: '添加虚拟机',
      disabled: (record: any) => {
        const { dataCenterId, canUseHostId: hostId } = record
        return !(dataCenterId && hostId)
      },
      key: 'addVm',
    },
    {
      label: '编辑集群',
      key: 'editCluster',
    },
    {
      label: '删除集群',
      key: 'deleteCluster',
    },
  ],
  host: [
    {
      label: '添加虚拟机',
      key: 'addVm',
      disabled: (record: any) => {
        return record.state !== '1'
      },
    },
    {
      label: '进入维护状态',
      disabled: (record: any) => {
        return ['3', '4', '5'].includes(record.state)
      },
      key: 'enterMaintain',
    },
    {
      label: '退出维护状态',
      disabled: (record: any) => {
        console.log('退出维护状态===>', record)
        return record.state !== '3'
      },
      key: 'exitMaintain',
    },
    {
      label: '编辑主机',
      key: 'editHost',
      disabled: (record: any) => {
        return ['3', '4', '5'].includes(record.state)
      },
    },
    {
      label: '删除主机',
      key: 'deleteHost',
      disabled: (record: any) => {
        return ['3', '4', '5'].includes(record.state)
      },
    },
    {
      label: '开机',
      disabled: (record: any) => {
        console.log('主机开机===>', record)
        return record.state !== '2'
      },
      key: 'openHost',
    },
    {
      label: '关闭',
      disabled: (record: any) => {
        console.log('主机关闭===>', record)
        return ['4', '5'].includes(record.state)
      },
      key: 'closeHost',
    },
    {
      label: '重启',
      disabled: (record: any) => {
        console.log('主机重启===>', record)
        return ['4', '5'].includes(record.state)
      },
      key: 'restartHost',
    },
  ],
  vm: [
    {
      key: 'editVirtualMachine',
      label: '编辑虚拟机',
      disabled: (vmInfo: any) => computeVmOperationDisable(vmInfo.state, 'editVirtualMachine'),
    },
    {
      key: 'deleteVm',
      label: '删除虚拟机',
      disabled: (vmInfo: any) => computeVmOperationDisable(vmInfo.state, 'deleteVm'),
    },
    {
      label: '启动',
      disabled: (vmInfo: any) => computeVmOperationDisable(vmInfo.state, 'startUpVm'),
      key: 'startUpVm',
    },
    {
      key: 'closeVm',
      disabled: (vmInfo: any) => computeVmOperationDisable(vmInfo.state, 'closeVm'),
      label: '安全关闭',
    },
    {
      key: 'destroyVm',
      disabled: (vmInfo: any) => computeVmOperationDisable(vmInfo.state, 'destroyVm'),
      label: '关闭电源',
    },
    {
      key: 'moveVirtualMachine',
      disabled: (vmInfo: any) => computeVmOperationDisable(vmInfo.state, 'moveVirtualMachine'),
      label: '迁移虚拟机',
    },
    {
      key: 'suspendVm',
      disabled: (vmInfo: any) => computeVmOperationDisable(vmInfo.state, 'suspendVm'),
      label: '暂停',
    },
    {
      key: 'resumeVm',
      disabled: (vmInfo: any) => computeVmOperationDisable(vmInfo.state, 'resumeVm'),
      label: '恢复',
    },
    {
      key: 'restartVm',
      disabled: (vmInfo: any) => computeVmOperationDisable(vmInfo.state, 'restartVm'),
      label: '重启',
    },
  ],
}

export default function RightMenu(props: any) {
  const { nodeInfo, clickMenu, clickRightMenu } = props
  const { title: label, type } = nodeInfo
  const onRightMenuClick: MenuProps['onClick'] = ({ domEvent, key: operationType }) => {
    domEvent.stopPropagation()
    clickRightMenu && clickRightMenu(nodeInfo, operationType)
  }
  const onMenuClick = (domEvent: React.MouseEvent<HTMLElement>) => {
    domEvent.stopPropagation()
    clickMenu && clickMenu(nodeInfo)
  }
  if (['dataCenter', 'cluster', 'host', 'compute', 'vm'].includes(type)) {
    const menuData = deviceMenu[type].map(deviceMenuItem => {
      const { key, label, disabled: disabledFn } = deviceMenuItem
      if (disabledFn && typeof disabledFn === 'function') {
        const disabled = disabledFn(nodeInfo)
        return {
          key,
          label,
          disabled,
        }
      }
      return {
        key,
        label,
      }
    })
    return (
      <Dropdown
        menu={{
          items: menuData,
          style: { width: '150px' },
          onClick: onRightMenuClick,
        }}
        trigger={['contextMenu']}
      >
        <span style={{ display: 'inherit' }} onClick={onMenuClick}>
          {label}
        </span>
      </Dropdown>
    )
  }
  return (
    <span style={{ display: 'inherit' }} onClick={onMenuClick}>
      {label}
    </span>
  )
}
