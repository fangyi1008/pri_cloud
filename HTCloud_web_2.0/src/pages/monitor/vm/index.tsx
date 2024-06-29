import { MonitorVM } from '@/api/interface'
import MonitorProTable from '@/components/monitorProTable'
import { ProColumns } from '@ant-design/pro-components'
import { Progress } from 'antd'

const MonitorVMComponent = () => {
  const monitorHostColumns: ProColumns<MonitorVM.IMonitorVMResult>[] = [
    {
      title: '主机名称',
      dataIndex: 'hostName',
    },
    {
      title: '虚拟机名称',
      dataIndex: 'vmShowName',
      ellipsis: true,
    },
    {
      title: '虚拟机磁盘名称',
      dataIndex: 'vmDiskName',
      ellipsis: true,
    },
    {
      title: '操作系统',
      dataIndex: 'os',
      ellipsis: true,
    },
    {
      title: '内存总量',
      dataIndex: 'memTotal',
      ellipsis: true,
    },
    {
      title: '内存利用率',
      dataIndex: 'memRate',
      width: 100,
      render: (_, record) => {
        const { memRate } = record
        if (!memRate) {
          return '--'
        }
        const percent = Number(memRate.replace('%', ''))
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
      title: 'cup数',
      dataIndex: 'cpuNum',
      ellipsis: true,
      width: 60,
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
      title: '状态',
      dataIndex: 'state',
      ellipsis: true,
      valueEnum: {
        运行: {
          text: '运行',
          status: 'Success',
        },
        关机: {
          text: '关机',
          status: 'Error',
        },
        异常: {
          text: '异常',
          status: 'Error',
        },
        挂起: {
          text: '挂起',
          status: 'Error',
        },
        暂停: {
          text: '暂停',
          status: 'Error',
        },
      },
    },
  ]
  return (
    <MonitorProTable<MonitorVM.IMonitorVMResult>
      apiUrl='/htcloud/vmMonitor/list'
      title='虚拟机监控'
      columns={monitorHostColumns}
    />
  )
}

export default MonitorVMComponent
