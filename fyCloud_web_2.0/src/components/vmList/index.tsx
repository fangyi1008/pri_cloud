import React, { useRef, Key, RefObject, MutableRefObject, useState, useEffect } from 'react'
import { ProColumns } from '@ant-design/pro-components'
import { Vm } from '@/api/interface'
import { useNavigate } from 'react-router'
import { IProTableListRef } from '../proTable/type'
import CustomProTable from '../proTable'
import AddEditDelete from '../addEditDelete'
import { operateToDispath } from '@layouts/components/Menu/util'
import { useAppDispatch } from '@/redux/store'
import { vmListOperations } from '@/layouts/components/deviceManagement'
import { getVmList } from '@/api/modules/virtualMachineService'
import eventEmitter from '@/events/index'

export interface IVmListPropsType {
  params?: Record<string, any>
}

export default function VmList(props: IVmListPropsType) {
  const dispatch = useAppDispatch()
  const { params } = props
  const [selectedRowKeys, setSelectedRowKeys] = useState<Key[]>([])
  const listRef: RefObject<IProTableListRef> = useRef(null)
  const selectedRowRef: MutableRefObject<Vm.IVmType[]> = useRef([])
  const navigate = useNavigate()

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
    // 注册事件
    eventEmitter.addListener('vmListLoad', vmListRefresh)
    return () => {
      eventEmitter.off('vmListLoad', vmListRefresh)
      // 移除注册的事件
    }
  })

  const columns: ProColumns<Vm.IVmType>[] = [
    {
      title: '编号',
      dataIndex: 'vmId',
      valueType: 'indexBorder',
      copyable: true,
      width: 60,
    },
    {
      title: '虚拟机名称',
      dataIndex: 'vmName',
      copyable: true,
      ellipsis: true,
      render: (_, record) => (
        <a
          onClick={() => {
            navigate(`/vm/${record.vmId}`)
          }}
        >
          {_}
        </a>
      ),
    },
    {
      title: '虚拟机操作系统IP',
      dataIndex: 'osIp',
      ellipsis: true,
    },
    {
      title: '虚拟机描述',
      dataIndex: 'vmMark',
      ellipsis: true,
    },
    {
      title: '主机',
      dataIndex: 'hostName',
      ellipsis: true,
    },
    {
      title: '集群',
      dataIndex: 'clusterName',
      ellipsis: true,
    },
    {
      title: '数据中心',
      dataIndex: 'dataCenterName',
      ellipsis: true,
    },
    {
      disable: true,
      title: '状态',
      dataIndex: 'state',
      ellipsis: true,
      valueEnum: {
        运行: {
          text: '运行',
        },
        关机: {
          text: '关机',
        },
        异常: {
          text: '异常',
        },
        挂起: {
          text: '挂起',
        },
        暂停: {
          text: '暂停',
        },
      },
    },
    {
      title: '创建时间',
      key: 'showTime',
      dataIndex: 'createTime',
      valueType: 'date',
    },
    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      width: 90,
      render: (text, record) => {
        return [
          vmListOperations.map(item => {
            const { type, disabledFn = () => false } = item
            if (type !== 'operate' && !disabledFn([record.vmId], [record])) {
              return (
                <a key={item.key} onClick={() => rowOperation(item.key, record)}>
                  {item.text}
                </a>
              )
            }
            return null
          }),
        ]
      },
    },
  ]

  const getVmListData = async (param: any) => {
    const { current: page = 1, pageSize: limit = 10, ...otherParam } = param
    const { currPage, pageSize, totalCount, list } =
      (await getVmList({ page: page + '', limit: limit + '', ...otherParam })) || {}
    return {
      data: list,
      page: currPage,
      pageSize,
      success: true,
      total: totalCount,
    }
  }

  const rowOperation = (type: string, vmInfo: Vm.IVmType) => {
    if (type === 'editVirtualMachine') {
      operateToDispath.editVirtualMachine(dispatch, vmInfo.vmId)
    } else if (type === 'deleteVm') {
      operateToDispath.deleteVm(dispatch, { ids: [vmInfo.vmId], hostId: '' })
    }
  }

  const onOperation = (key: string) => {
    switch (key) {
      case 'editVirtualMachine':
        operateToDispath.editVirtualMachine(dispatch, selectedRowKeys[0] as string)
        break
      case 'deleteVm':
        operateToDispath.deleteVm(dispatch, { ids: selectedRowKeys, hostId: '' })
        break
      case 'startUpVm':
        operateToDispath.startUpVm(dispatch, selectedRowKeys)
        break
      case 'suspendVm':
        operateToDispath.suspendVm(dispatch, selectedRowKeys)
        break
      case 'resumeVm':
        operateToDispath.resumeVm(dispatch, selectedRowKeys)
        break
      case 'restartVm':
        operateToDispath.restartVm(dispatch, selectedRowKeys)
        break
      case 'closeVm':
        operateToDispath.closeVm(dispatch, selectedRowKeys)
        break
      case 'destroyVm':
        operateToDispath.destroyVm(dispatch, selectedRowKeys)
        break
    }
  }

  const onClick = (_: string, key: string) => {
    onOperation(key)
  }

  const onMenuClick = (key: string) => {
    onOperation(key)
  }

  const operations = vmListOperations.map(item => {
    const { disabledFn, type, ...thoreData } = item
    let moreMenu: { key: string; type: string; label: string }[] = []
    if (type === 'operate') {
      moreMenu =
        item?.menuData?.map(menuItem => {
          const { disabledFn, ...otherInfo } = menuItem
          const disabled = disabledFn ? disabledFn(selectedRowKeys, selectedRowRef.current) : false
          return {
            ...otherInfo,
            disabled,
          }
        }) || []
    }
    const disabled = disabledFn ? disabledFn(selectedRowKeys, selectedRowRef.current) : false
    if (type === 'operate') {
      return {
        ...thoreData,
        type,
        menuData: moreMenu,
      }
    }
    return {
      ...thoreData,
      disabled,
      type,
    }
  })

  const toolBarRender = () => [
    <AddEditDelete key='key' data={operations} onClick={onClick} onMenuClick={onMenuClick} />,
  ]

  const onSelectedChange = (selectedRowKeys: Key[], selectedRow: Vm.IVmType[]) => {
    setSelectedRowKeys(selectedRowKeys)
    selectedRowRef.current = selectedRow
  }

  return (
    <CustomProTable<Vm.IVmType>
      params={params}
      ref={listRef}
      search={false}
      columns={columns}
      request={getVmListData}
      rowKey={'vmId'}
      headerTitle='虚拟机列表'
      toolBarRender={toolBarRender}
      pagination={{
        pageSize: 10,
      }}
      tableAlertRender={false}
      tableAlertOptionRender={false}
      selectedRowKeys={selectedRowKeys}
      rowSelection={{
        alwaysShowAlert: true,
        onChange: onSelectedChange,
      }}
    />
  )
}
