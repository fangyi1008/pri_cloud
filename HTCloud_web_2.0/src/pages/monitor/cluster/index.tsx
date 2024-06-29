import { useCallback } from 'react'
import { ProColumns } from '@ant-design/pro-components'
import MonitorProTable from '@components/monitorProTable'
import { MonitorCluster } from '@/api/interface'

const MonitorClusterComponent = () => {
  const formatData = useCallback((list: MonitorCluster.IMonitorClusterResult[]) => {
    return list.map(item => {
      const { clusterName, cpuNum, hostNum, memTotal, vmNum, vmSummary } = item
      return {
        clusterName,
        cpuNum,
        hostNum,
        memTotal,
        vmNum,
        closeNum: vmSummary[1],
        otherNum: vmSummary[2],
      }
    })
  }, [])
  const monitorClusterColumns: ProColumns<MonitorCluster.IMonitorCluster>[] = [
    {
      title: '集群名称',
      dataIndex: 'clusterName',
    },
    {
      title: '主机数量',
      dataIndex: 'hostNum',
      ellipsis: true,
    },
    {
      title: '虚拟机数量',
      dataIndex: 'vmNum',
      ellipsis: true,
    },
    {
      title: '虚拟机关机数量',
      dataIndex: 'closeNum',
      ellipsis: true,
    },
    {
      title: '虚拟机其他状态数量',
      dataIndex: 'otherNum',
      ellipsis: true,
    },
    {
      title: 'cpu数量',
      dataIndex: 'cpuNum',
      ellipsis: true,
    },
    {
      title: '内存',
      dataIndex: 'memTotal',
      ellipsis: true,
    },
  ]
  return (
    <MonitorProTable<MonitorCluster.IMonitorCluster, MonitorCluster.IMonitorClusterResult>
      apiUrl='/htcloud/clusterMonitor/list'
      title='集群监控'
      columns={monitorClusterColumns}
      formatData={formatData}
    />
  )
}

export default MonitorClusterComponent
