import { useRef, Key, RefObject, useState, useEffect } from 'react'
import { ProColumns } from '@ant-design/pro-components'
import { PortFlux, VmSwitch } from '@/api/interface'
import { IProTableListRef } from '../proTable/type'
import CustomProTable from '../proTable'
import { getPortFluxList, getVmSwitchList } from '@/api/modules/virtualSwitch'
import AddEditDelete from '../addEditDelete'
import { virtualSwitchListOperations } from '@/layouts/components/deviceManagement/VirtualSwitch'
import { operateToDispath } from '@/layouts/components/Menu/util'
import { useAppDispatch } from '@/redux/store'
import eventEmitter from '@/events/index'
import { SelectPoolWrap } from '../storageList/style'

export interface IVmSwitchListPropsType {
  params?: Record<string, any>
}

export default function VmSwitchList(props: IVmSwitchListPropsType) {
  const { params } = props
  const dispatch = useAppDispatch()
  const [portFluxParam, setPortFluxParam] = useState<Record<string, any>>({})
  const [selectedVmSwitchRowKeys, setSelectedVmSwitchRowKeys] = useState<Key[]>([])
  const listRef: RefObject<IProTableListRef> = useRef(null)
  const listDetailRef: RefObject<IProTableListRef> = useRef(null)

  const VmSwitchColumns: ProColumns<VmSwitch.IVmSwitchType>[] = [
    {
      title: '编号',
      dataIndex: 'vmSwitchId',
      valueType: 'indexBorder',
      copyable: true,
      width: 60,
    },
    {
      title: '名称',
      dataIndex: 'vmSwitchName',
      copyable: true,
      ellipsis: true,
    },
    {
      title: '网络类型',
      dataIndex: 'networkType',
      ellipsis: true,
      valueEnum: {
        1: {
          text: '管理网络',
        },
        2: {
          text: '业务网络',
        },
        3: {
          text: '存储网络',
        },
        '1|2': {
          text: '管理网络,业务网络',
        },
        '1|3': {
          text: '管理网络,存储网络',
        },
        '1|2|3': {
          text: '管理网络,业务网络,存储网络',
        },
        '2|3': {
          text: '业务网络,存储网络',
        },
      },
    },
    {
      title: '物理接口',
      dataIndex: 'netMachine',
      ellipsis: true,
    },
    {
      title: '链路聚合模式',
      dataIndex: 'linkMode',
      ellipsis: true,
      valueEnum: {
        static: {
          text: '静态',
        },
        dynamic: {
          text: '动态',
        },
        'static|dynamic': {
          text: '动态,静态',
        },
        'dynamic|static': {
          text: '动态,静态',
        },
      },
    },
    {
      title: '负载分担模式',
      dataIndex: 'loadMode',
      valueEnum: {
        equal: {
          text: '均衡',
        },
        main: {
          text: '主备',
        },
        'equal|main': {
          text: '均衡,主备',
        },
        'main|equal': {
          text: '均衡,主备',
        },
      },
      ellipsis: true,
    },
    {
      title: 'ip地址',
      dataIndex: 'ip',
      ellipsis: true,
    },
    {
      title: '子网掩码',
      dataIndex: 'netMask',
      ellipsis: true,
    },
    {
      title: '网关',
      dataIndex: 'gateway',
      ellipsis: true,
    },
    {
      title: 'MTU',
      dataIndex: 'mtuSize',
      ellipsis: true,
    },
    {
      title: '创建时间',
      key: 'showTime',
      dataIndex: 'createTime',
      valueType: 'date',
    },
  ]

  const vMColumns: ProColumns<PortFlux.IPortFluxhType>[] = [
    {
      title: '编号',
      valueType: 'indexBorder',
      width: 50,
    },
    {
      title: '虚拟机',
      dataIndex: 'vm_name',
      ellipsis: true,
    },
    {
      title: '端口',
      dataIndex: 'name',
      ellipsis: true,
    },
    {
      title: '接收报文数',
      dataIndex: 'rx_packets',
      ellipsis: true,
    },
    {
      title: '接收字节数',
      dataIndex: 'rx_bytes',
      ellipsis: true,
    },
    {
      title: '接收错报数',
      dataIndex: 'rx_errors',
      ellipsis: true,
    },
    {
      title: '发送报文数',
      dataIndex: 'tx_packets',
      ellipsis: true,
    },
    {
      title: '发送字节数',
      dataIndex: 'tx_bytes',
      ellipsis: true,
    },
    {
      title: '发送错报数',
      dataIndex: 'tx_errors',
      ellipsis: true,
    },
  ]
  const getVmSwitchListData = async (param: any) => {
    const { current: page = 1, pageSize: limit = 10, ...otherParam } = param
    const { currPage, pageSize, totalCount, list } =
      (await getVmSwitchList({ page: page + '', limit: limit + '', ...otherParam })) || {}
    return {
      data: list,
      page: currPage,
      pageSize,
      success: true,
      total: totalCount,
    }
  }

  const onOpenOperation = (key: string) => {
    switch (key) {
      case 'addVirtualSwitch':
        operateToDispath.addVirtualSwitch(dispatch, { mtuSize: 1500 })
        break
      case 'editVirtualSwitch':
        operateToDispath.editVirtualSwitch(dispatch, selectedVmSwitchRowKeys[0] as string)
        break
      case 'deleteVirtualSwitch':
        operateToDispath.deleteVirtualSwitch(dispatch, selectedVmSwitchRowKeys)
        break
      default:
        break
    }
  }

  const onClick = (_: string, key: string) => {
    onOpenOperation(key)
  }

  useEffect(() => {
    const vmListRefresh = async (params: { type: 'reload' | 'reset' | 'clearSelected' }) => {
      const { type } = params
      if (type === 'reload') {
        listRef?.current?.getRef?.()?.reload?.()
      } else if (type === 'reset') {
        listRef?.current?.getRef?.()?.reset?.()
      } else if (type === 'clearSelected') {
        listRef?.current?.getRef?.()?.clearSelected?.()
      }
    }
    const hidenPortFluxList = (param: { vmSwitchIds: string[] }) => {
      const { vmSwitchIds } = param
      const { vmSwitchId: selectedId } = portFluxParam || {}
      if (vmSwitchIds.includes(selectedId)) {
        setPortFluxParam({})
      }
    }

    const changeSelectedName = (param: { vmSwitchId: string; vmSwitchName: string }) => {
      const { vmSwitchId, vmSwitchName } = param
      const { vmSwitchId: selectedId, vmSwitchName: selectedName } = portFluxParam || {}
      if (vmSwitchId === selectedId && vmSwitchName !== selectedName) {
        setPortFluxParam({ ...portFluxParam, vmSwitchName })
      }
    }
    // 注册事件
    eventEmitter.addListener('virtualSwitchListLoad', vmListRefresh)
    eventEmitter.addListener('hidenPortFluxList', hidenPortFluxList)
    eventEmitter.addListener('changeSelectedName', changeSelectedName)
    return () => {
      // 移除注册的事件
      eventEmitter.off('virtualSwitchListLoad', vmListRefresh)
      eventEmitter.off('virtualSwitchListLoad', hidenPortFluxList)
      eventEmitter.off('changeSelectedName', changeSelectedName)
    }
  })

  const operations = virtualSwitchListOperations.map(item => {
    const { disabledFn, ...thoreData } = item
    const disabled = disabledFn ? disabledFn(selectedVmSwitchRowKeys) : false
    return {
      disabled,
      ...thoreData,
    }
  })

  const toolBarRender = () => [<AddEditDelete key='key' data={operations} onClick={onClick} />]

  const onVmSwitchSelectedChange = (selectedRowKeys: Key[]) => {
    setSelectedVmSwitchRowKeys(selectedRowKeys)
  }

  const getPortFluxListData = async (param: any) => {
    const { vmSwitchId } = param
    const { list } = (await getPortFluxList(vmSwitchId)) || {}
    return {
      data: list,
      success: true,
      page: 1,
      total: list.length,
    }
  }

  const onRowClick = (record: VmSwitch.IVmSwitchType) => {
    const { vmSwitchId, vmSwitchName } = record
    setPortFluxParam({ vmSwitchId, vmSwitchName })
  }

  return (
    <>
      <CustomProTable<VmSwitch.IVmSwitchType>
        params={params}
        ref={listRef}
        search={false}
        columns={VmSwitchColumns}
        request={getVmSwitchListData}
        rowKey={'vmSwitchId'}
        headerTitle='虚拟交换机表'
        pagination={{
          pageSize: 10,
        }}
        toolBarRender={toolBarRender}
        tableAlertRender={false}
        tableAlertOptionRender={false}
        selectedRowKeys={selectedVmSwitchRowKeys}
        rowSelection={{
          alwaysShowAlert: true,
          onChange: onVmSwitchSelectedChange,
        }}
        onRowClick={onRowClick}
      />

      {portFluxParam.vmSwitchId ? (
        <>
          <SelectPoolWrap>{`当前选中的虚拟交换机是: ${portFluxParam.vmSwitchName}`}</SelectPoolWrap>
          <CustomProTable<PortFlux.IPortFluxhType>
            params={portFluxParam}
            ref={listDetailRef}
            search={false}
            columns={vMColumns}
            request={getPortFluxListData}
            headerTitle=''
            tableAlertRender={false}
            tableAlertOptionRender={false}
            rowSelection={false}
          />
        </>
      ) : null}
    </>
  )
}
