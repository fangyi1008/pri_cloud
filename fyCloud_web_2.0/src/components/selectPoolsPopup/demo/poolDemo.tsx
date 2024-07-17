import React, { useState, useRef } from 'react'
import { PoolFileOperationType, PoolListDataType } from '../type'
import storage from '../imgs/storage.svg'
import { SearchOutlined } from '@ant-design/icons'
import { Button } from 'antd'
import { createTableData } from '../../../common/utils'
import { TablePaginationConfig, TableProps } from 'antd/lib/table'
import SelectPoolsPopup from '../index'

export default function Demo() {
  const sourceDataDefault = createTableData(
    {
      storageVolumeName: '一个连接池',
      filesystem: '本地片',
      fileSize: '200GB',
      createUserId: 'admin',
    },
    20,
    ['fileSize', 'createUserId', 'filesystem'],
  )
  const [visible, setVisible] = useState<boolean>(false)
  const [value, setValue] = useState(1)
  const [sourceData, setSourceData] = useState(sourceDataDefault)
  const [pageInfo, setPageInfo] = useState<{
    current: number
    pageSize: number
    total: number
  }>({
    current: 1,
    pageSize: 10,
    total: 100,
  })
  const [selectedRowKeys, setSelectedRowKeys] = useState<(string | number)[]>([])
  const currentPoolRef: React.MutableRefObject<PoolListDataType | undefined> = useRef()
  const operations: PoolFileOperationType[] = [
    { type: 'img', src: storage, title: '删除' },
    { type: 'icon', icon: <SearchOutlined />, title: '添加' },
  ]
  const poolListData: PoolListDataType[] = createTableData(
    {
      ss: `连接池名称`,
      describe: '可用空间200GB',
      subtitle: '本地连接池',
    },
    10,
    [],
    'value',
  )

  const columns = [
    {
      title: '文件名',
      dataIndex: 'storageVolumeName',
      key: 'storageVolumeName',
    },
    {
      title: '类型',
      dataIndex: 'filesystem',
      key: 'filesystem',
    },
    {
      title: '大小',
      dataIndex: 'fileSize',
      key: 'fileSize',
    },
    {
      title: '使用者',
      dataIndex: 'createUserId',
      key: 'createUserId',
    },
  ]

  const onPoolSelect = (item: PoolListDataType) => {
    currentPoolRef.current = item
    const { value, ss } = item
    const data = createTableData(
      {
        storageVolumeName: `${ss}下的一个文件`,
        filesystem: '本地片',
        fileSize: '200GB',
        createUserId: 'admin',
      },
      10,
      ['fileSize', 'createUserId', 'filesystem'],
    )
    setSourceData(data)
    setValue(value)
  }

  const onOperationClick = (item: PoolFileOperationType, data: PoolListDataType) => {
    console.log(item, data)
  }

  const onPoolFilesSelect = (rowKeys: React.Key[], data: any[]) => {
    console.log(rowKeys, data)
    setSelectedRowKeys(rowKeys)
  }

  const onTableChange: TableProps<any>['onChange'] = (pagination, filters, sorter, extra) => {
    const { current = 1, pageSize = 10 } = pagination
    const { poolName } = currentPoolRef.current || {}
    const data = createTableData(
      {
        storageVolumeName: `${poolName}下的一个文件`,
        filesystem: '本地片',
        fileSize: '200GB',
        createUserId: 'admin',
      },
      pageSize,
      ['fileSize', 'createUserId', 'filesystem'],
      'id',
      (current - 1) * pageSize + 1,
    )
    setSourceData(data)
    setPageInfo({
      pageSize: pageSize || 1,
      current: current || 10,
      total: 100,
    })
  }

  const pagination: TablePaginationConfig = {
    ...pageInfo,
    size: 'small',
    showSizeChanger: true,
  }

  return (
    <>
      <Button onClick={() => setVisible(true)}>type是file</Button>
      <SelectPoolsPopup
        fieldNames={{
          title: 'ss',
          valueField: 'value',
        }}
        title={'选择连接池'}
        visible={visible}
        width={'60%'}
        onCancel={() => setVisible(false)}
        centered
        isShowPool
        value={value}
        operations={operations}
        poolListData={poolListData}
        onPoolSelect={onPoolSelect}
        onOperationClick={onOperationClick}
        tableData={sourceData}
        tableColumns={columns}
        tableSelectedKeys={selectedRowKeys}
        tablePagination={pagination}
        tableSelectionType={'radio'}
        tableRowKey='id'
        onTableChange={onTableChange}
        onPoolFilesSelect={onPoolFilesSelect}
      />
    </>
  )
}
