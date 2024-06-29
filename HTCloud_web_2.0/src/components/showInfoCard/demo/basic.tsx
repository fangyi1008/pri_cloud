import { Fragment } from 'react'
import ShowInfoCard from '../index'
import { BasicCardDataType } from '../type'

export default function Demo() {
  const data: BasicCardDataType[] = [
    {
      key: 'name',
      label: '名称',
      type: 'text',
      value: '名称',
    },
    {
      key: 'desc',
      label: '描述',
      type: 'text',
      value: '描述',
    },
    {
      key: 'run_time',
      label: '连续运行时长',
      type: 'text',
      value: '连续运行时长',
    },
    {
      key: 'key',
      label: '机型',
      type: 'text',
      value: '机型',
    },

    {
      key: 'progress',
      label: '进度',
      type: 'progress',
      value: 30,
    },
  ]
  const dashboardData = [
    {
      key: 'statusProgress',
      width: 150,
      percent: 80,
      strokeColor: {
        '0%': 'orange',
        '100%': 'lightgreen',
      },
      info: [
        { key: '1', label: '主机CPU总核数', value: '4' },
        { key: '2', label: '虚拟机CPU总核数', value: '4' },
      ],
    },

    {
      key: 'storageProgress',
      width: 150,
      percent: 100,
      info: [
        { key: '1', label: '主机CPU总核数', value: '4' },
        { key: '2', label: '虚拟机CPU总核数', value: '4' },
      ],
    },
  ]

  return (
    <Fragment>
      <ShowInfoCard
        basicData={data}
        cardTitle={'基本信息'}
        type={'basic'}
        width={300}
        height={300}
      />
      <br />
      <ShowInfoCard
        cardTitle={'主机状态统计'}
        type={'dashboard'}
        dashboardData={dashboardData}
        width={400}
      />
    </Fragment>
  )
}
