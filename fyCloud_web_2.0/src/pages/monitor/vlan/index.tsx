import { H1Title } from '@/common/commonStyle'
import { Collapse, Empty, Table } from 'antd'
import { ColumnsType } from 'antd/lib/table'
import { useEffect, useState } from 'react'
import { MonitorVlan, MonitorIP } from '@/api/interface'
import { jsonArrayStrToArray } from '@/utils/util'

const { Panel } = Collapse

const MonitorVlanComponent = () => {
  const [data, setData] = useState<MonitorVlan.IMonitorVlanResult[]>([])

  const columns: ColumnsType<MonitorIP.IMonitorIPResult> = [
    {
      title: '虚拟机名称',
      dataIndex: 'vmShowName',
      ellipsis: true,
    },
    {
      title: '虚拟机描述',
      dataIndex: 'vmDesc',
      ellipsis: true,
    },
    {
      title: 'mac地址',
      dataIndex: 'mac',
      ellipsis: true,
    },
    {
      title: 'ip地址',
      dataIndex: 'ip',
      ellipsis: true,
    },
    {
      title: '操作系统',
      dataIndex: 'osName',
      ellipsis: true,
    },
  ]

  useEffect(() => {
    const source = new EventSource('/htcloud/vlanMonitor/list')
    source.addEventListener('message', event => {
      try {
        console.log('message==>', event)
        const result = jsonArrayStrToArray<MonitorVlan.IMonitorVlanResult>(event.data)
        const list: MonitorVlan.IMonitorVlanResult[] =
          result as unknown as MonitorVlan.IMonitorVlanResult[]
        // const TemData = [
        //   {
        //     vlan: '1',
        //     ipMonitorList: [
        //       { vmShowName: '1231', vmDesc: '123', mac: '123', ip: '123', osName: '123' },
        //       { vmShowName: '12312', vmDesc: '123', mac: '123', ip: '123', osName: '123' },
        //     ],
        //   },
        //   {
        //     vlan: '2',
        //     ipMonitorList: [
        //       { vmShowName: '123', vmDesc: '123', mac: '123', ip: '123', osName: '123' },
        //       { vmShowName: '21', vmDesc: '123', mac: '123', ip: '123', osName: '123' },
        //     ],
        //   },
        // ]
        setData(list)
      } catch (error: any) {
        console.warn(error.message)
        setData([])
      }
    })
    return () => {
      source.close()
    }
  }, [])

  return (
    <div style={{ background: '#fff', padding: '0  24px 16px 24px' }}>
      <H1Title>Vln监控</H1Title>
      {data.length > 0 ? (
        <Collapse>
          {data.map(item => (
            <Panel header={`Vln(${item.vlan})`} key={item.vlan}>
              <Table
                pagination={false}
                columns={columns}
                dataSource={item.ipMonitorList}
                size='small'
                bordered
              />
            </Panel>
          ))}
        </Collapse>
      ) : (
        <Empty />
      )}
    </div>
  )
}

export default MonitorVlanComponent
