import { MonitorStorage } from '@/api/interface'
import MonitorProTable from '@/components/monitorProTable'
import { ProColumns } from '@ant-design/pro-components'
import { Progress } from 'antd'
import { useCallback } from 'react'

const formatSizeStr = (sizeStr: string): string => {
  if (!sizeStr) {
    return '--'
  }
  return sizeStr.slice(0, sizeStr.indexOf('.')) + sizeStr.slice(sizeStr.length - 2, sizeStr.length)
}

const MonitorStorageComponent = () => {
  const formatData = useCallback((list: MonitorStorage.IMonitorStorageResult[]) => {
    return list.map(item => {
      const { diskFreeNum = '', diskTotal = '' } = item
      return {
        ...item,
        diskFreeNum: formatSizeStr(diskFreeNum),
        diskTotal: formatSizeStr(diskTotal),
      }
    })
  }, [])

  const monitorHostColumns: ProColumns<MonitorStorage.IMonitorStorageResult>[] = [
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
      title: '源路径',
      dataIndex: 'sourcePath',
      ellipsis: true,
    },
    {
      title: '磁盘容量',
      dataIndex: 'diskTotal',
      ellipsis: true,
    },
    {
      title: '可用容量',
      dataIndex: 'diskFreeNum',
      ellipsis: true,
    },
    {
      title: '磁盘利用率',
      dataIndex: 'diskRate',
      width: 100,
      render: (_, record) => {
        const { diskRate } = record
        if (!diskRate) {
          return '--'
        }
        const percent = Number(diskRate.replace('%', ''))
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
      title: '状态',
      dataIndex: 'state',
      ellipsis: true,
      valueEnum: {
        失联: { text: '失联', status: 'Error' },
        异常: { text: '异常', status: 'Error' },
        关机: {
          text: '关机',
          status: 'Error',
        },
        维护模式: {
          text: '维护模式',
          status: 'Error',
          disabled: true,
        },
        运行中: {
          text: '运行中',
          status: 'Success',
        },
      },
    },
  ]
  return (
    <MonitorProTable<MonitorStorage.IMonitorStorageResult>
      apiUrl='/htcloud/storageMonitor/list'
      title='存储监控'
      columns={monitorHostColumns}
      formatData={formatData}
    />
  )
}

export default MonitorStorageComponent
