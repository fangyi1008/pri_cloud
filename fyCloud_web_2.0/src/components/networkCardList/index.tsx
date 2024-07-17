import { useRef, RefObject } from 'react'
import { ProColumns } from '@ant-design/pro-components'
import { NetworkCard } from '@/api/interface'
import { IProTableListRef } from '../proTable/type'
import CustomProTable from '../proTable'
import { getPhysicalNetworkList } from '@/api/modules/physicalNetworkService'

export interface INetworkCardListtPropsType {
  params?: Record<string, any>
}

export default function NetworkCardList(props: INetworkCardListtPropsType) {
  const { params } = props
  const listRef: RefObject<IProTableListRef> = useRef(null)

  const columns: ProColumns<NetworkCard.INetworkCardType>[] = [
    {
      title: '物理接口',
      dataIndex: 'name',
      ellipsis: true,
    },
    {
      title: '型号',
      dataIndex: 'model',
      ellipsis: true,
    },
    {
      title: 'MAC地址',
      dataIndex: 'mac',
      ellipsis: true,
    },
    {
      disable: true,
      title: '状态',
      dataIndex: 'state',
    },
    {
      title: '速率',
      dataIndex: 'speed',
      ellipsis: true,
    },
    {
      title: 'MTU',
      dataIndex: 'mtu',
      ellipsis: true,
    },
    {
      title: '设备地址',
      dataIndex: 'pci',
      ellipsis: true,
    },
  ]

  const getHostListData = async (param: any) => {
    const { hostId } = param
    const { list = [] } = (await getPhysicalNetworkList(hostId)) || {}
    return {
      data: list,
      success: true,
      page: 1,
      total: list.length,
    }
  }

  return (
    <CustomProTable<NetworkCard.INetworkCardType>
      params={params}
      ref={listRef}
      search={false}
      columns={columns}
      request={getHostListData}
      rowKey={'mac'}
      headerTitle='物理网卡列表'
      tableAlertRender={false}
      rowSelection={false}
      tableAlertOptionRender={false}
    />
  )
}
