import React, { useState } from 'react'
import { Button } from 'antd'
import { createTableData } from '../../../common/utils'
import { TablePaginationConfig, TableProps } from 'antd/lib/table'
import SelectPoolsPopup from '../index'

export default function Demo() {
  const [visible, setVisible] = useState<boolean>(false)
  const [pageInfo, setPageInfo] = useState<{
    current: number
    pageSize: number
  }>({
    current: 1,
    pageSize: 10,
  })
  const [selectedRowKeys, setSelectedRowKeys] = useState<(string | number)[]>([])

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

  const sourceData = createTableData(
    {
      storageVolumeName: '一个连接池',
      filesystem: '本地片',
      fileSize: '200GB',
      createUserId: 'admin',
    },
    20,
    ['fileSize', 'createUserId', 'filesystem'],
  )

  const onPoolFilesSelect = (rowKeys: React.Key[], data: any[]) => {
    console.log('onPoolFilesSelect==>', rowKeys, data)
    setSelectedRowKeys(rowKeys)
  }

  const onTableChange: TableProps<any>['onChange'] = (pagination, filters, sorter, extra) => {
    const { current, pageSize } = pagination
    setPageInfo({
      pageSize: pageSize || 1,
      current: current || 10,
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
        title={'选择连接池'}
        visible={visible}
        width={'60%'}
        centered
        destroyOnClose={true}
        tableData={sourceData}
        tableColumns={columns}
        tableSelectedKeys={selectedRowKeys}
        tablePagination={pagination}
        tableSelectionType={'checkbox'}
        tableRowKey='id'
        onTableChange={onTableChange}
        onPoolFilesSelect={onPoolFilesSelect}
        onCancel={() => setVisible(false)}
      />
    </>
  )
}
