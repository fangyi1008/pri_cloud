import { useRef, Key, RefObject, useState, useEffect } from 'react'
import { ProColumns } from '@ant-design/pro-components'
import { StoragePool, StorageVolume } from '@/api/interface'
import { IProTableListRef } from '../proTable/type'
import CustomProTable from '../proTable'
import AddEditDelete from '../addEditDelete'
import { operateToDispath } from '@layouts/components/Menu/util'
import store, { useAppDispatch } from '@/redux/store'
import {
  getStoragePoolList,
  getStorageVolumeList,
  uploadCheck,
  uploadMerge,
} from '@/api/modules/storagePools'
import { storagePoolListOperations } from '@/layouts/components/deviceManagement/storagePool'
import {
  lvmVolumeListOperations,
  storageVolumeListOperations,
} from '@/layouts/components/deviceManagement/storageVolume'
import eventEmitter from '@/events/index'
import { BasicObj } from '../table/type'
import { message, Table } from 'antd'
import { SelectPoolWrap } from './style'
import SliceUpload from '../sliceUpload'
import { ISliceUploadRef } from '../sliceUpload/type'
import { useUpdateEffect } from 'ahooks'
export interface IStorageListPropsType {
  params?: Record<string, any>
}

export default function StorageList(props: IStorageListPropsType) {
  const dispatch = useAppDispatch()
  const { params } = props
  const { hostId } = params || {}
  const [selectedPoolRowKeys, setSelectedPoolRowKeys] = useState<Key[]>([])
  const [selectedPoolRowData, setSelectedPoolRowData] = useState<StoragePool.IStoragePoolType[]>([])
  const [selectedVolumeRowKeys, setSelectedVolumeRowKeys] = useState<Key[]>([])
  const [selectedVolumeData, setSelectedVolumeData] = useState<StorageVolume.IStorageVolumeType[]>(
    [],
  )
  const [clickPool, setClickPool] = useState<BasicObj>({})
  const sliceUploadRef: RefObject<ISliceUploadRef> = useRef(null)
  const listPoolRef: RefObject<IProTableListRef> = useRef(null)
  const lisVolumetRef: RefObject<IProTableListRef> = useRef(null)

  const poolColumns: ProColumns<StoragePool.IStoragePoolType>[] = [
    {
      title: '编号',
      dataIndex: 'storagePoolId',
      valueType: 'indexBorder',
      copyable: true,
      width: 60,
    },
    {
      title: '存储池名称',
      dataIndex: 'storagePoolName',
      copyable: true,
      ellipsis: true,
    },
    {
      title: '显示名称',
      dataIndex: 'poolShowName',
      copyable: true,
      ellipsis: true,
      search: false,
    },
    {
      title: '池类型',
      dataIndex: 'poolType',
      ellipsis: true,
      search: false,
    },
    {
      title: '集群',
      dataIndex: 'clusterName',
      ellipsis: true,
      search: false,
    },
    {
      title: '主机',
      dataIndex: 'hostName',
      ellipsis: true,
      search: false,
    },
    {
      title: '主机ip',
      dataIndex: 'osIp',
      ellipsis: true,
      search: false,
    },
    {
      title: '容量',
      dataIndex: 'capacity',
      ellipsis: true,
      search: false,
    },
    {
      title: '可用空间',
      dataIndex: 'freeSpace',
      ellipsis: true,
      search: false,
    },
    {
      title: '状态',
      dataIndex: 'status',
      search: false,
      valueEnum: {
        1: { text: '活跃' },
        2: { text: '不活跃' },
        3: { text: '未格式化' },
        4: { text: '暂停' },
      },
    },
    {
      title: '已使用空间',
      dataIndex: 'usedSpace',
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

  const volumeColumns: ProColumns<StorageVolume.IStorageVolumeType>[] = [
    {
      title: '编号',
      dataIndex: 'storageId',
      valueType: 'indexBorder',
      copyable: true,
      width: 60,
    },
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
    },
    {
      title: '基础镜像',
      dataIndex: 'basicVolumeName',
      ellipsis: true,
      width: 200,
    },
    {
      title: '存储路径',
      dataIndex: 'storagePath',
      ellipsis: true,
      width: 200,
    },
    {
      title: '容量',
      dataIndex: 'capacity',
      ellipsis: true,
    },
    {
      title: '创建人',
      dataIndex: 'createUserName',
      ellipsis: true,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'date',
    },
  ]

  useUpdateEffect(() => {
    setClickPool({})
  }, [hostId])

  useUpdateEffect(() => {
    setSelectedVolumeRowKeys([])
  }, [clickPool])

  const refresh = (ref: any, type: string) => {
    if (type === 'reload') {
      ref?.current?.getRef?.()?.reload?.()
    } else if (type === 'reset') {
      ref?.current?.getRef?.()?.reset?.()
    } else if (type === 'clearSelected') {
      ref?.current?.getRef?.()?.clearSelected?.()
    }
  }

  useEffect(() => {
    const storageListRefresh = async (params: {
      type: 'reload' | 'reset' | 'clearSelected'
      isPool: boolean
      value?: any
      isDelete?: boolean
    }) => {
      const { type, isPool, value, isDelete = false } = params
      if (isPool) {
        if (Object.keys(clickPool).length === 0) {
          refresh(listPoolRef, type)
          return
        }
        refresh(listPoolRef, type)
        if (isDelete && Array.isArray(value) && type === 'reload') {
          const { storagePoolId } = clickPool
          const result = value?.find((item: string) => item == storagePoolId)
          result ? setClickPool({}) : null
        } else if (
          Object.prototype.toString.call(value) === '[object Object]' &&
          type === 'reload'
        ) {
          const { storagePoolId: id } = value
          const { storagePoolId } = clickPool
          id == storagePoolId ? setClickPool({ ...clickPool, ...value }) : null
        }
      } else {
        refresh(lisVolumetRef, type)
      }
    }
    // 注册事件
    eventEmitter.addListener('poolListLoad', storageListRefresh)
    return () => {
      eventEmitter.off('poolListLoad', storageListRefresh)
      // 移除注册的事件
    }
  }, [clickPool])

  const getStoragePoolListData = async (param: any) => {
    const { current: page = 1, pageSize: limit = 10, ...otherParam } = param
    const { hostId, storagePoolName } = otherParam
    const { currPage, pageSize, totalCount, list } =
      (await getStoragePoolList({ page: page + '', limit: limit + '', hostId, storagePoolName })) ||
      {}
    return {
      data: list,
      page: currPage,
      pageSize,
      success: true,
      total: totalCount,
    }
  }

  const getStorageVolumeListData = async (param: any) => {
    const { current: page = 1, pageSize: limit = 10 } = param
    const { storagePoolId } = clickPool
    const { currPage, pageSize, totalCount, list } =
      (await getStorageVolumeList({
        page: page + '',
        limit: limit + '',
        storagePoolId: storagePoolId,
        status: 1,
      })) || {}
    return {
      data: list,
      page: currPage,
      pageSize,
      success: true,
      total: totalCount,
    }
  }

  const onOpenOperation = (type: string, value: Key[]) => {
    switch (type) {
      case 'addStoragePool':
        operateToDispath.addPool(dispatch, hostId)
        break
      case 'deleteStoragePool':
        operateToDispath.deletePool(dispatch, value)
        break
      case 'editStoragePool':
        operateToDispath.editPool(dispatch, value[0])
        break
      case 'startStoragePool':
        operateToDispath.startPool(dispatch, value[0])
        break
      case 'stopStoragePool':
        operateToDispath.stopPool(dispatch, value[0])
        break
      case 'formatStoragePool':
        operateToDispath.formatPool(dispatch, value[0])
        break
      default:
        break
    }
  }
  const onVolumeOpenOperation = (type: string, value: Key[]) => {
    switch (type) {
      case 'addStorageVolume':
        operateToDispath.addStorage(dispatch, clickPool)
        break
      case 'uploadStorageVolume':
        // eslint-disable-next-line no-case-declarations
        const { storagePoolId } = clickPool
        sliceUploadRef?.current?.openSelectFileWindow({
          options: {
            singleFile: false,
            accept: '.iso,.qcow2,.raw',
            target: '/htcloud/storage/upload',
          },
          queryParam: {
            token: store.getState().global.token || '',
            storagePoolId,
          },
        })
        break
      case 'download':
        operateToDispath.downLoadStorage(dispatch, selectedVolumeData[0])
        break
      case 'delStorageVolume':
        operateToDispath.delStorage(dispatch, value)
        break
      default:
        break
    }
  }
  const addEditDeletePoolOnClick = (type: string) => {
    onOpenOperation(type, selectedPoolRowKeys)
  }
  const addEditDeleteVolumeOnClick = (type: string) => {
    onVolumeOpenOperation(type, selectedVolumeRowKeys)
  }

  const storagePoolOperations = storagePoolListOperations.map(item => {
    const { disabledFn, ...thoreData } = item
    const disabled = disabledFn ? disabledFn(selectedPoolRowKeys, selectedPoolRowData) : false
    return {
      disabled,
      ...thoreData,
    }
  })
  const operationList =
    clickPool?.poolType === 'lvm' ? lvmVolumeListOperations : storageVolumeListOperations
  const storageVolumeOperations = operationList.map(item => {
    const { disabledFn, ...thoreData } = item
    const disabled = disabledFn ? disabledFn(selectedVolumeRowKeys, clickPool) : false
    return {
      disabled,
      ...thoreData,
    }
  })

  const storagePooltoolBarRender = () => [
    <AddEditDelete key='key' data={storagePoolOperations} onClick={addEditDeletePoolOnClick} />,
  ]

  const storageVolumetoolBarRender = () => [
    <AddEditDelete key='key' data={storageVolumeOperations} onClick={addEditDeleteVolumeOnClick} />,
  ]

  const onPoolSelectedChange = (
    selectedRowKeys: Key[],
    selectRow: StoragePool.IStoragePoolType[],
  ) => {
    setSelectedPoolRowKeys(selectedRowKeys)
    setSelectedPoolRowData(selectRow)
  }

  const onVolumeSelectedChange = (
    selectedRowKeys: Key[],
    selectRow: StorageVolume.IStorageVolumeType[],
  ) => {
    setSelectedVolumeRowKeys(selectedRowKeys)
    setSelectedVolumeData(selectRow)
  }
  const onRowClick = (record: any) => {
    setClickPool(record)
  }
  const { storagePoolName } = clickPool

  const fileCheckFn = async (file: any): Promise<boolean> => {
    const storageVolumeName = file.name
    const storagePoolId = file.params.storagePoolId
    const fileSize = file.size
    const identifier = file.uniqueIdentifier
    const checkResult = await uploadCheck({
      storageVolumeName,
      storagePoolId,
      identifier,
      fileSize,
    })
    if (checkResult) {
      const { chunkVo } = checkResult as any
      const { skipUpload } = chunkVo
      if (skipUpload) {
        message.error('当前文件已经存在不需要上传')
        file.uploader.removeFile(file)
        return true
      }
      return false
    }
    return false
  }

  const fileComplete = async (file: any) => {
    const storageVolumeName = file.name
    const storagePoolId = file.params.storagePoolId
    const identifier = file.uniqueIdentifier
    const fileSize = file.size
    const result = await uploadMerge({ storageVolumeName, storagePoolId, identifier, fileSize })
    const { code, msg } = result
    if (code === 0) {
      lisVolumetRef && refresh(lisVolumetRef, 'reload')
    } else {
      file.uploader && file.uploader.removeFile(file)
      message.error('上传文件合并失败')
    }
  }
  return (
    <>
      <SliceUpload
        autoStart={false}
        ref={sliceUploadRef}
        fileCheckFn={fileCheckFn}
        fileComplete={fileComplete}
      />
      <CustomProTable<StoragePool.IStoragePoolType>
        params={params}
        ref={listPoolRef}
        search={{
          labelWidth: 'auto',
        }}
        columns={poolColumns}
        request={getStoragePoolListData}
        rowKey={'storagePoolId'}
        headerTitle='存储池'
        toolBarRender={storagePooltoolBarRender}
        pagination={{
          pageSize: 10,
        }}
        tableAlertRender={false}
        tableAlertOptionRender={false}
        selectedRowKeys={selectedPoolRowKeys}
        rowSelection={{
          selections: [Table.SELECTION_ALL, Table.SELECTION_INVERT],
          alwaysShowAlert: true,
          onChange: onPoolSelectedChange,
        }}
        onRowClick={onRowClick}
      />
      {Object.keys(clickPool).length !== 0 ? (
        <>
          <SelectPoolWrap>{`当前选中存储池为: ${storagePoolName}`}</SelectPoolWrap>
          <CustomProTable<StorageVolume.IStorageVolumeType>
            params={clickPool}
            ref={lisVolumetRef}
            search={false}
            columns={volumeColumns}
            request={getStorageVolumeListData}
            rowKey={'storageId'}
            headerTitle='存储卷'
            toolBarRender={storageVolumetoolBarRender}
            pagination={{
              pageSize: 10,
            }}
            tableAlertRender={false}
            tableAlertOptionRender={false}
            selectedRowKeys={selectedVolumeRowKeys}
            rowSelection={{
              alwaysShowAlert: true,
              onChange: onVolumeSelectedChange,
            }}
          />
        </>
      ) : null}
    </>
  )
}
