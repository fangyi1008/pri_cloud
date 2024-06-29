import { MonitorIP } from '@/api/interface'
import MonitorProTable from '@/components/monitorProTable'
import { ProColumns } from '@ant-design/pro-components'

const MonitorIPComponent = () => {
  const monitorHostColumns: ProColumns<MonitorIP.IMonitorIPResult>[] = [
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
  return (
    <MonitorProTable<MonitorIP.IMonitorIPResult>
      apiUrl='/htcloud/ipMonitor/list'
      title='IP监控'
      columns={monitorHostColumns}
    />
  )
}

export default MonitorIPComponent
