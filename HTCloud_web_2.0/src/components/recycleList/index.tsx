import { useRef, Key, RefObject, useState, useEffect } from 'react'
import { ProColumns } from '@ant-design/pro-components'
import { StorageVolume } from '@/api/interface'
import { IProTableListRef } from '../proTable/type'
import CustomProTable from '../proTable'
import AddEditDelete from '../addEditDelete'
import { operateToDispath } from '@layouts/components/Menu/util'
import { useAppDispatch } from '@/redux/store'
import eventEmitter from '@/events/index'
import { getStorageVolumeList } from '@/api/modules/storagePools'
import { recycleListOperations } from '@/layouts/components/deviceManagement/recycle'

export default function RecycleList(props: any) {
  const dispatch = useAppDispatch()
  const [selectedRowKeys, setSelectedRowKeys] = useState<Key[]>([])
  const selectData = useRef<StorageVolume.IStorageVolumeType[]>([])
  const listRef: RefObject<IProTableListRef> = useRef(null)

  const volumeColumns: ProColumns<StorageVolume.IStorageVolumeType>[] = [
    {
      title: '存储卷名称',
      dataIndex: 'storageVolumeName',
      copyable: true,
      ellipsis: true,
    },
    {
      title: '文件类型',
      dataIndex: 'filesystem',
      ellipsis: true,
      search: false,
    },
    {
      title: '基础镜像',
      dataIndex: 'basicVolumeName',
      ellipsis: true,
      width: 200,
      search: false,
    },
    {
      title: '存储池',
      dataIndex: 'storagePoolName',
      ellipsis: true,
      width: 200,
      search: false,
    },
    {
      title: '容量',
      dataIndex: 'capacity',
      ellipsis: true,
      search: false,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'date',
      search: false,
    },
  ]

  const getRecycleListData = async (param: any) => {
    const { current: page = 1, pageSize: limit = 10, ...otherParam } = param
    const { storageVolumeName } = otherParam
    const { currPage, pageSize, totalCount, list } =
      (await getStorageVolumeList({
        page: page + '',
        limit: limit + '',
        status: 2,
        storageVolumeName,
      })) || {}
    return {
      data: list,
      page: currPage,
      pageSize,
      success: true,
      total: totalCount,
    }
  }
  useEffect(() => {
    const recycleListRefresh = async (params: { type: 'reload' | 'reset' | 'clearSelected' }) => {
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
    eventEmitter.addListener('recycleListLoad', recycleListRefresh)
    return () => {
      eventEmitter.off('recycleListLoad', recycleListRefresh)
      // 移除注册的事件
    }
  }, [])
  const onOpenOperation = (type: string, value: Key[]) => {
    if (type === 'resumeStorage') {
      operateToDispath.resumeStorage(dispatch, value)
    } else if (type === 'deleteStorage') {
      operateToDispath.delRecycleStorage(dispatch, value)
    }
  }

  const addEditDeleteOnClick = (type: string) => {
    onOpenOperation(type, selectedRowKeys)
  }

  const operationsButton = recycleListOperations.map(item => {
    const { disabledFn, ...thoreData } = item
    const disabled = disabledFn(selectData.current)
    return {
      disabled,
      ...thoreData,
    }
  })

  const toolBarRender = () => [
    <AddEditDelete key='key' data={operationsButton} onClick={addEditDeleteOnClick} />,
  ]

  const onSelectedChange = (
    selectedRowKeys: Key[],
    selectRow: StorageVolume.IStorageVolumeType[],
  ) => {
    setSelectedRowKeys(selectedRowKeys)
    selectData.current = selectRow
  }

  return (
    <CustomProTable<StorageVolume.IStorageVolumeType>
      ref={listRef}
      search={{
        labelWidth: 'auto',
      }}
      columns={volumeColumns}
      request={getRecycleListData}
      rowKey={'storageId'}
      headerTitle='回收站'
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
