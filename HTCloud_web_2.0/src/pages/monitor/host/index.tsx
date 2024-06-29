import { useCallback } from 'react'
import { ProColumns } from '@ant-design/pro-components'
import { Progress } from 'antd'
import MonitorProTable from '@components/monitorProTable'
import { MonitorHost } from '@/api/interface'

const MonitorHostComponent = () => {
  const formatData = useCallback((list: MonitorHost.IMonitorHostResult[]) => {
    console.log('MonitorHostComponent==>', list)
    return list.map(item => {
      const {
        cpuModel,
        cpuNum,
        cpuRate,
        diskTotal,
        hostModel,
        hostName,
        memRate,
        memTotal,
        runTime,
        state,
        vmSummary = [],
      } = item
      return {
        cpuModel,
        cpuNum,
        cpuRate,
        diskTotal,
        hostModel,
        hostName,
        memRate,
        memTotal,
        runTime,
        state,
        closeNum: vmSummary[1],
        otherNum: vmSummary[2],
      }
    })
  }, [])

  const monitorHostColumns: ProColumns<MonitorHost.IMonitorHost>[] = [
    {
      title: '主机名称',
      dataIndex: 'hostName',
    },
    {
      title: '主机型号',
      dataIndex: 'hostModel',
      ellipsis: true,
    },
    {
      title: '硬盘大小',
      dataIndex: 'diskTotal',
      ellipsis: true,
    },
    {
      title: 'cup个数',
      dataIndex: 'cpuNum',
      ellipsis: true,
    },
    {
      title: 'cpu型号',
      dataIndex: 'cpuModel',
      ellipsis: true,
    },
    {
      title: 'cpu使用率',
      dataIndex: 'cpuRate',
      width: 100,
      align: 'center',
      render: (_, record) => {
        const { cpuRate } = record
        if (!cpuRate) {
          return '--'
        }
        const percent = Number(cpuRate)
        if (typeof percent === 'number') {
          const strokeColor = percent >= 80 ? 'red' : 'rgb(82, 196, 26)'
          return (
            <Progress
              type='circle'
              width={40}
              percent={percent}
              strokeColor={strokeColor}
              format={percent => `${percent}%`}
              strokeWidth={16}
            />
          )
        } else {
          return '--'
        }
      },
    },
    {
      title: '内存总量',
      dataIndex: 'memTotal',
      ellipsis: true,
    },
    {
      title: '内存使用率',
      dataIndex: 'memRate',
      width: 100,
      render: (_, record) => {
        const { memRate } = record
        if (!memRate) {
          return '--'
        }
        const percent = Number(memRate)
        if (typeof percent === 'number') {
          const strokeColor = percent >= 80 ? 'red' : 'rgb(82, 196, 26)'
          return (
            <Progress
              type='circle'
              width={40}
              percent={percent}
              strokeColor={strokeColor}
              format={percent => `${percent}%`}
              strokeWidth={16}
            />
          )
        }
      },
    },
    {
      title: '运行时间',
      dataIndex: 'runTime',
      ellipsis: true,
    },
    {
      title: '状态',
      dataIndex: 'state',
      ellipsis: true,
      valueEnum: {
        5: { text: '失联', status: 'Error' },
        4: { text: '异常', status: 'Error' },
        2: {
          text: '关机',
          status: 'Error',
        },
        3: {
          text: '维护模式',
          status: 'Success',
          disabled: true,
        },
        1: {
          text: '运行中',
          status: 'Success',
        },
      },
    },
  ]
  return (
    <MonitorProTable<MonitorHost.IMonitorHost, MonitorHost.IMonitorHostResult>
      apiUrl='/htcloud/hostMonitor/list'
      title='主机监控'
      columns={monitorHostColumns}
      formatData={formatData}
    />
  )
}

export default MonitorHostComponent
