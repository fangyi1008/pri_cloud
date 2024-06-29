import { useRef, useState, RefObject } from 'react'
import SearchInput from '../index'
import { createTableData } from '@common/utils'
import { SearchInputRefType, SearchInputValueType } from '../type'
import { PoolFileOperationType, PoolListDataType } from '@components/selectPoolsPopup/type'
import storage from '../../selectPoolsPopup/imgs/storage.svg'
import { SearchOutlined } from '@ant-design/icons'

const columns = [
  {
    title: '名称',
    dataIndex: 'vmSwitchName',
    key: 'vmSwitchName',
  },
  {
    title: 'MTU',
    dataIndex: 'mtuSize',
    key: 'mtuSize',
  },
  {
    title: 'IP地址',
    dataIndex: 'ip',
    key: 'ip',
  },
  {
    title: '网关',
    dataIndex: 'gateway',
    key: 'gateway',
  },
  {
    title: '子网掩码',
    dataIndex: 'netMask',
    key: 'netMask',
  },
]

export default function Demo() {
  const onDelete = () => {
    setValue('')
  }

  const [value, setValue] = useState<any>(false)
  const [value2, setValue2] = useState<any>(false)

  const searchRef: RefObject<SearchInputRefType> = useRef(null)
  const searchRef1: RefObject<SearchInputRefType> = useRef(null)

  const operations: PoolFileOperationType[] = [
    { type: 'img', src: storage, title: '删除' },
    { type: 'icon', icon: <SearchOutlined />, title: '添加' },
  ]

  const onSearch = () => {
    setTimeout(() => {
      const tableData = createTableData(
        {
          vmSwitchName: '网络名称',
          mtuSize: '150',
          ip: '192.168.1.291',
          gateway: '192.168.1.1',
          netMask: '255.255.255.0',
        },
        20,
        [],
        'value',
      )
      const { vmSwitchName } = value
      const { value: key } = tableData.find(item => item.vmSwitchName === vmSwitchName) ?? {}
      searchRef?.current?.openSearchPopup({
        selectedRowKeys: [key],
        sourceData: tableData,
      })
    }, 500)
  }

  const onSearch1 = () => {
    setTimeout(() => {
      const tableData = createTableData(
        {
          vmSwitchName: '网络名称',
          mtuSize: '150',
          ip: '192.168.1.291',
          gateway: '192.168.1.1',
          netMask: '255.255.255.0',
        },
        20,
        [],
        'value',
      )

      const poolListData = createTableData(
        {
          poolName: `连接池名称`,
          poolSize: '200GB',
          poolType: '本地连接池',
        },
        20,
        [],
        'id',
      )

      searchRef1?.current?.openSearchPopup({
        selectedRowKeys: ['1'],
        sourceData: tableData,
        poolListData,
        selectedPoolKey: 1,
        pageInfo: {
          current: 1,
          total: 100,
          pageSize: 10,
        },
      })
    }, 500)
  }

  const onChange = (value?: SearchInputValueType) => {
    console.log('value====>', value)
    value && setValue(value)
  }

  const onChange1 = (value?: SearchInputValueType) => {
    value && setValue(value)
  }

  const onPoolSelect = (data: any) => {
    const { poolName, id: key } = data
    const tableData = createTableData(
      {
        vmSwitchName: `${poolName}-网络名称`,
        mtuSize: '150',
        ip: '192.168.1.291',
        gateway: '192.168.1.1',
        netMask: '255.255.255.0',
      },
      20,
      [],
      'value',
    )
    searchRef1?.current?.openSearchPopup({
      selectedRowKeys: [],
      sourceData: tableData,
      selectedPoolKey: key,
    })
  }

  const onOperationClick = (item: PoolFileOperationType, data: PoolListDataType) => {
    console.log('item', data)
  }

  const onTableChange = (pageInfo: any) => {
    const { current, pageSize } = pageInfo
    const tableData = createTableData(
      {
        vmSwitchName: '网络名称',
        mtuSize: '150',
        ip: '192.168.1.291',
        gateway: '192.168.1.1',
        netMask: '255.255.255.0',
      },
      10,
      [],
      'value',
      (current - 1) * pageSize,
    )
    searchRef1?.current?.openSearchPopup({
      sourceData: tableData,
      pageInfo: {
        current,
        total: 100,
        pageSize,
      },
    })
  }

  return (
    <>
      <SearchInput
        popupTitle={'表格'}
        ref={searchRef}
        value={value}
        resultIsObject
        columns={columns}
        tableRowKey={'value'}
        valueField={'vmSwitchName'}
        onDelete={onDelete}
        onSearch={onSearch}
        onChange={onChange}
        readOnly
      />
      <br />

      <SearchInput
        popupTitle={'表格222'}
        ref={searchRef1}
        value={value}
        isShowPool
        resultIsObject
        columns={columns}
        tableRowKey={'value'}
        valueField={'vmSwitchName'}
        operations={operations}
        fieldNames={{
          valueField: 'id',
          title: 'poolName',
          subtitle: 'poolType',
          describe: 'poolSize',
        }}
        onPoolSelect={onPoolSelect}
        onOperationClick={onOperationClick}
        onTableChange={onTableChange}
        onDelete={onDelete}
        onSearch={onSearch1}
        onChange={onChange}
        readOnly
      />
      <br />
    </>
  )
}
