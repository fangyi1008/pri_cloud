import { useRef, Key, RefObject, useState, useEffect } from 'react'
import { ProColumns } from '@ant-design/pro-components'
import { Datacenter } from '@/api/interface'
import { useNavigate } from 'react-router'
import { IProTableListRef } from '../proTable/type'
import CustomProTable from '../proTable'
import AddEditDelete from '../addEditDelete'
import { operateToDispath } from '@layouts/components/Menu/util'
import { useAppDispatch } from '@/redux/store'
import { dataCenterListOperations } from '@/layouts/components/deviceManagement'
import { getAsyncDataCenterData } from '@/api/modules/datacenter'
import eventEmitter from '@/events/index'
export interface IDataCenterListPropsType {
  params?: Record<string, any>
}

export default function DataCenterList(props: IDataCenterListPropsType) {
  const dispatch = useAppDispatch()
  const { params } = props
  const [selectedRowKeys, setSelectedRowKeys] = useState<Key[]>([])
  const listRef: RefObject<IProTableListRef> = useRef(null)
  const navigate = useNavigate()

  const columns: ProColumns<Datacenter.IDatecenterType>[] = [
    {
      title: '编号',
      dataIndex: 'dataCenterId',
      valueType: 'indexBorder',
      copyable: true,
      width: 60,
    },
    {
      title: '数据中心名称',
      dataIndex: 'dataCenterName',
      copyable: true,
      ellipsis: true,
      render: (_, record) => (
        <a
          onClick={() => {
            navigate(`/cluster/${record.dataCenterId}`)
          }}
        >
          {_}
        </a>
      ),
    },
    {
      title: '创建者',
      dataIndex: 'createUserName',
      ellipsis: true,
      search: false,
    },
    {
      title: '创建时间',
      key: 'createTime',
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
        dataCenterListOperations.map(item => (
          <a key={item.key} onClick={() => onOpenOperation(item.type, [record.dataCenterId])}>
            {item.text}
          </a>
        )),
      ],
    },
  ]
  const getDataCenterListData = async (param: any) => {
    const { current: page = 1, pageSize: limit = 10, ...otherParam } = param
    const { currPage, pageSize, totalCount, list } =
      (await getAsyncDataCenterData({ page: page + '', limit: limit + '', ...otherParam })) || {}
    return {
      data: list,
      page: currPage,
      pageSize,
      success: true,
      total: totalCount,
    }
  }

  useEffect(() => {
    const dataCenterListRefresh = async (params: {
      type: 'reload' | 'reset' | 'clearSelected'
    }) => {
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
    eventEmitter.addListener('dataCenterListLoad', dataCenterListRefresh)
    return () => {
      eventEmitter.off('dataCenterListLoad', dataCenterListRefresh)
      // 移除注册的事件
    }
  }, [])

  const onOpenOperation = (type: string, value: Key[]) => {
    if (type === 'delDataCenter') {
      operateToDispath.deleteDataCenter(dispatch, value)
    } else if (type === 'editDataCenter') {
      operateToDispath.editDataCenter(dispatch, { dataCenterId: value[0] + '' })
    }
  }

  const addEditDeleteOnClick = (type: string) => {
    onOpenOperation(type, selectedRowKeys)
  }

  const operationsButton = dataCenterListOperations.map(item => {
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
    <CustomProTable<Datacenter.IDatecenterType>
      params={params}
      ref={listRef}
      search={{
        labelWidth: 'auto',
      }}
      columns={columns}
      request={getDataCenterListData}
      rowKey={'dataCenterId'}
      headerTitle='数据中心列表'
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
