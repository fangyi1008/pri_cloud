import { useRef, Key, RefObject, useState, useEffect } from 'react'
import { ProColumns } from '@ant-design/pro-components'
import { Host } from '@/api/interface'
import { useNavigate } from 'react-router'
import { IProTableListRef } from '../proTable/type'
import CustomProTable from '../proTable'
import { getHostList } from '@src/api/modules/host'
import AddEditDelete from '../addEditDelete'
import { operateToDispath } from '@layouts/components/Menu/util'
import { useAppDispatch } from '@/redux/store'
import { hostListOperations } from '@/layouts/components/deviceManagement'
import eventEmitter from '@/events/index'

export interface IHostListPropsType {
  params?: Record<string, any>
}

export default function HostList(props: IHostListPropsType) {
  const dispatch = useAppDispatch()
  const { params } = props
  const [selectedRowKeys, setSelectedRowKeys] = useState<Key[]>([])
  const selectRow = useRef<Host.IHostType[]>([])
  const listRef: RefObject<IProTableListRef> = useRef(null)
  const navigate = useNavigate()

  const columns: ProColumns<Host.IHostType>[] = [
    {
      title: '编号',
      dataIndex: 'hostId',
      valueType: 'indexBorder',
      copyable: true,
      width: 60,
    },
    {
      title: '主机名称',
      disable: true,
      dataIndex: 'hostName',
      copyable: true,
      ellipsis: true,
      render: (_, record) => (
        <a
          onClick={() => {
            navigate(`/host/${record.hostId}`)
          }}
        >
          {_}
        </a>
      ),
    },
    {
      title: '数据中心',
      dataIndex: 'dataCenterName',
      ellipsis: true,
      search: false,
    },
    {
      title: '集群名称',
      dataIndex: 'clusterName',
      ellipsis: true,
      search: false,
    },
    {
      title: '创建者',
      dataIndex: 'createUserName',
      ellipsis: true,
      search: false,
    },
    {
      title: '操作系统ip',
      dataIndex: 'osIp',
      ellipsis: true,
      search: false,
    },
    {
      title: 'cpu型号',
      dataIndex: 'cpuType',
      ellipsis: true,
      search: false,
    },
    {
      title: '版本',
      dataIndex: 'virtualVersion',
      ellipsis: true,
      search: false,
    },
    {
      disable: true,
      title: '状态',
      dataIndex: 'state',
      filters: true,
      onFilter: true,
      ellipsis: true,
      search: false,
      valueEnum: {
        5: { text: '失联', status: 'Error' },
        4: { text: '异常', status: 'Error' },
        2: {
          text: '关机',
          status: 'Error',
        },
        3: {
          text: '维护模式',
          status: 'Success',
          disabled: true,
        },
        1: {
          text: '运行中',
          status: 'Success',
        },
      },
    },
    {
      title: '创建时间',
      key: 'showTime',
      dataIndex: 'createTime',
      valueType: 'date',
      search: false,
    },
    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      width: 90,
      render: (text, record) => [
        hostListOperations.map(item => (
          <a key={item.key} onClick={() => onOpenOperation(item.type, [record.hostId])}>
            {item.text}
          </a>
        )),
      ],
    },
  ]

  const getHostListData = async (param: any) => {
    const { current: page = 1, pageSize: limit = 10, ...otherParam } = param
    const { currPage, pageSize, totalCount, list } =
      (await getHostList({ page: page + '', limit: limit + '', ...otherParam })) || {}
    return {
      data: list,
      page: currPage,
      pageSize,
      success: true,
      total: totalCount,
    }
  }

  useEffect(() => {
    const hostListRefresh = async (params: { type: 'reload' | 'reset' | 'clearSelected' }) => {
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
    eventEmitter.addListener('hostListLoad', hostListRefresh)
    return () => {
      eventEmitter.off('hostListLoad', hostListRefresh)
      // 移除注册的事件
    }
  }, [])

  const onOpenOperation = (type: string, value: Key[]) => {
    if (type === 'delHost') {
      operateToDispath.deleteHost(dispatch, { dataCenterId: '', clusterId: '', ids: value })
    } else if (type === 'editHost') {
      operateToDispath.editHost(dispatch, value[0])
    }
  }

  const addEditDeleteOnClick = (type: string) => {
    onOpenOperation(type, selectedRowKeys)
  }

  const operations = hostListOperations.map(item => {
    const { disabledFn, ...thoreData } = item
    const disabled = disabledFn(selectRow.current)
    return {
      disabled,
      ...thoreData,
    }
  })

  const toolBarRender = () => [
    <AddEditDelete key='key' data={operations} onClick={addEditDeleteOnClick} />,
  ]

  const onSelectedChange = (selectedRowKeys: Key[], selectRowData: Host.IHostType[]) => {
    setSelectedRowKeys(selectedRowKeys)
    selectRow.current = selectRowData
  }

  return (
    <CustomProTable<Host.IHostType>
      params={params}
      ref={listRef}
      search={{
        labelWidth: 'auto',
      }}
      columns={columns}
      request={getHostListData}
      rowKey={'hostId'}
      headerTitle='主机列表'
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
