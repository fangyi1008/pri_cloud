import { useRef, Key, RefObject, useState, useEffect } from 'react'
import { ProColumns } from '@ant-design/pro-components'
import { Cluster } from '@/api/interface'
import { useNavigate } from 'react-router'
import { IProTableListRef } from '../proTable/type'
import CustomProTable from '../proTable'
import AddEditDelete from '../addEditDelete'
import { operateToDispath } from '@layouts/components/Menu/util'
import { useAppDispatch } from '@/redux/store'
import { clusterListOperations } from '@/layouts/components/deviceManagement'
import { getClusterList } from '@/api/modules/cluster'
import eventEmitter from '@/events/index'

export interface IClusterListPropsType {
  params?: Record<string, any>
}

export default function ClusterList(props: IClusterListPropsType) {
  const dispatch = useAppDispatch()
  const { params } = props
  const [selectedRowKeys, setSelectedRowKeys] = useState<Key[]>([])
  const listRef: RefObject<IProTableListRef> = useRef(null)
  const navigate = useNavigate()

  const columns: ProColumns<Cluster.IClusterType>[] = [
    {
      title: '编号',
      dataIndex: 'clusterId',
      valueType: 'indexBorder',
      copyable: true,
      width: 60,
    },
    {
      title: '集群名称',
      dataIndex: 'clusterName',
      copyable: true,
      ellipsis: true,
      render: (_, record) => (
        <a
          onClick={() => {
            navigate(`/cluster/${record.clusterId}`)
          }}
        >
          {_}
        </a>
      ),
    },
    {
      title: '分布式资源调度',
      dataIndex: 'drsSwitch',
      ellipsis: true,
      search: false,
    },
    {
      title: '高可用',
      dataIndex: 'haSwitch',
      ellipsis: true,
      search: false,
    },
    {
      title: '数据中心',
      dataIndex: 'dataCenterName',
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
        clusterListOperations.map(item => (
          <a key={item.key} onClick={() => onOpenOperation(item.type, [record.clusterId])}>
            {item.text}
          </a>
        )),
      ],
    },
  ]

  const getClusterListData = async (param: any) => {
    const { current: page = 1, pageSize: limit = 10, ...otherParam } = param
    const { currPage, pageSize, totalCount, list } =
      (await getClusterList({ page: page + '', limit: limit + '', ...otherParam })) || {}
    return {
      data: list,
      page: currPage,
      pageSize,
      success: true,
      total: totalCount,
    }
  }
  useEffect(() => {
    const clusterListRefresh = async (params: { type: 'reload' | 'reset' | 'clearSelected' }) => {
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
    eventEmitter.addListener('clusterListLoad', clusterListRefresh)
    return () => {
      eventEmitter.off('clusterListLoad', clusterListRefresh)
      // 移除注册的事件
    }
  }, [])
  const onOpenOperation = (type: string, value: Key[]) => {
    if (type === 'delCluster') {
      operateToDispath.deleteCluster(dispatch, { dataCenterId: '', ids: value })
    } else if (type === 'editCluster') {
      operateToDispath.editCluster(dispatch, value[0])
    }
  }

  const addEditDeleteOnClick = (type: string) => {
    onOpenOperation(type, selectedRowKeys)
  }

  const operationsButton = clusterListOperations.map(item => {
    const { disabledFn, ...thoreData } = item
    const disabled = disabledFn(selectedRowKeys)
    return {
      disabled,
      ...thoreData,
    }
  })

  const toolBarRender = () => [
    <AddEditDelete key='key' data={operationsButton} onClick={addEditDeleteOnClick} />,
  ]

  const onSelectedChange = (selectedRowKeys: Key[]) => {
    setSelectedRowKeys(selectedRowKeys)
  }

  return (
    <CustomProTable<Cluster.IClusterType>
      params={params}
      ref={listRef}
      search={{
        labelWidth: 'auto',
      }}
      columns={columns}
      request={getClusterListData}
      rowKey={'clusterId'}
      headerTitle='集群列表'
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
