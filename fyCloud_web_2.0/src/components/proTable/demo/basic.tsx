import { Key, ProColumns, TableDropdown } from '@ant-design/pro-components'
import ProTable from '../index'
import { Host } from '@src/api/interface'
import { useNavigate } from 'react-router-dom'
import { useRef, RefObject, useState } from 'react'
import { Button, Space } from 'antd'
import { PlusOutlined } from '@ant-design/icons'
import { IProTableListRef } from '../type'

export default function Demo() {
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
      dataIndex: 'dataCenterId',
      ellipsis: true,
    },
    {
      title: '集群名称',
      dataIndex: 'clusterId',
      ellipsis: true,
    },
    // {
    //   title: '用户名',
    //   dataIndex: 'hostUser',
    //   ellipsis: true,
    // },
    {
      title: '主机密码',
      dataIndex: 'hostPassword',
      ellipsis: true,
    },
    {
      title: '操作系统ip',
      dataIndex: 'osIp',
      ellipsis: true,
    },
    {
      title: 'cpu型号',
      dataIndex: 'cpuType',
      ellipsis: true,
    },
    {
      title: '版本',
      dataIndex: 'virtualVersion',
      ellipsis: true,
    },
    {
      disable: true,
      title: '状态',
      dataIndex: 'state',
      filters: true,
      onFilter: true,
      ellipsis: true,
      valueEnum: {
        0: { text: '全部', status: 'Error' },
        2: {
          text: '未解决',
          status: 'Error',
        },
        3: {
          text: '已解决',
          status: 'Success',
          disabled: true,
        },
        1: {
          text: '解决中',
          status: 'Processing',
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
      width: 120,
      render: (text, record, _, action) => [
        <a
          key='editable'
          onClick={() => {
            action?.startEditable?.(record.hostId)
          }}
        >
          编辑
        </a>,
        <a href={record.hostId} target='_blank' rel='noopener noreferrer' key='view'>
          查看
        </a>,
        <TableDropdown
          key='actionGroup'
          onSelect={() => action?.reload()}
          menus={[
            { key: 'copy', name: '复制' },
            { key: 'delete', name: '删除' },
          ]}
        />,
      ],
    },
  ]

  const getHostListData = async (param: any, sort: any, filter: any) => {
    const { current: page = 1, pageSize: limit = 10, ...otherParam } = param

    const list = [
      {
        hostId: '1610537600756609025',
        hostName: 'cluster13\n',
        hostUser: 'root',
        hostPassword: '4B5A685BF0547136EFF8A2A792DEA275',
        osIp: '192.168.0.13',
        bmcIp: '',
        cpuType: 'x86_64',
        virtualVersion: '1.0.0',
        clusterId: '1672737881108',
        dataCenterId: '',
        state: '1',
        createTime: '1672817060000',
        createUserId: '1610393683394338817',
        clusterName: 'test',
        createUserName: 'admin',
        dataCenterName: 'test',
      },
      {
        hostId: '1612276327069548546',
        hostName: 'localhost.localdomain\n',
        hostUser: 'root',
        hostPassword: '4B5A685BF0547136EFF8A2A792DEA275',
        osIp: '192.168.0.14',
        bmcIp: '',
        cpuType: 'x86_64',
        virtualVersion: '1.0.0',
        clusterId: '1672737881108',
        dataCenterId: '',
        state: '1',
        createTime: '1673231605000',
        createUserId: '1610393683394338817',
        clusterName: 'test',
        createUserName: 'admin',
        dataCenterName: 'test',
      },
    ]

    return {
      data: list,
      page: 1,
      pageSize: 10,
      success: true,
      total: 20,
    }
  }
  const onClick = () => {
    console.log('listRefonClick==>', listRef.current?.getRef?.())
  }

  const toolBarRender = () => [
    <Button key='button' icon={<PlusOutlined />} type='primary'>
      新建
    </Button>,
  ]

  const onChange = (selectedRowKeys: Key[], selectedRows: Host.IHostType[]) => {
    setSelectedRowKeys(selectedRowKeys)
    console.log('onChange===>', selectedRowKeys, selectedRows)
  }
  const deleteFn = (a: Key[], b: Host.IHostType[]) => {
    console.log('delete===>', a, b)
  }

  const tableAlertOptionRender = (param: {
    selectedRowKeys: (number | string)[]
    selectedRows: Host.IHostType[]
    onCleanSelected: () => void
  }) => {
    const { selectedRowKeys, selectedRows, onCleanSelected } = param
    return (
      <Space size={16}>
        <Button
          onClick={() => {
            deleteFn(selectedRowKeys, selectedRows)
          }}
        >
          批量删除
        </Button>
        <Button>批量删除</Button>
      </Space>
    )
  }

  const [selectedRowKeys, setSelectedRowKeys] = useState<Key[]>([])

  return (
    <>
      <Button onClick={onClick}>获取ref</Button>
      <ProTable<Host.IHostType>
        ref={listRef}
        search={false}
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
          onChange,
        }}
      />
    </>
  )
}
