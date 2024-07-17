import { useState } from 'react'
import { Button } from 'antd'
import TaskDesk from '../index'
import { MergeCellsOutlined } from '@ant-design/icons'
import { DataType } from '../type'

const data: DataType[] = []
for (let i = 0; i < 24; i++) {
  data.push({
    key: i,
    taskName: `taskName ${i}`,
    subject: `subject ${i}`,
    status: `status ${i}`,
    descInfo: `descInfo ${i}`,
    operator: `operator ${i}`,
    queueTime: `queueTime ${i}`,
    startTime: `startTime ${i}`,
    finishedTime: `finishedTime ${i}`,
    server: `server ${i}`,
  })
}

const columns = [
  {
    title: '任务名称',
    dataIndex: 'taskName',
    key: 'taskName',
    fixed: 'left',
    width: 120,
  },
  {
    title: '对象',
    dataIndex: 'subject',
    key: 'subject',
    width: 120,
    fixed: 'left',
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    width: 120,
  },
  {
    title: '详细信息',
    dataIndex: 'descInfo',
    key: 'descInfo',
    width: 120,
  },
  {
    title: '启动者',
    dataIndex: 'operator',
    key: 'operator',
    width: 120,
  },
  {
    title: '排队时间',
    dataIndex: 'queueTime',
    key: 'queueTime',
    width: 120,
  },
  {
    title: '开始事件',
    dataIndex: 'startTime',
    key: 'startTime',
    width: 120,
  },
  {
    title: '完成事件',
    dataIndex: 'finishedTime',
    key: 'finishedTime',
    width: 120,
  },
  {
    title: '服务器',
    dataIndex: 'server',
    key: 'server',
    fixed: 'right',
    width: 120,
  },
]

export default function Demo() {
  const [visible, setVisible] = useState(true)
  const onClick = () => {
    setVisible(false)
  }
  const onOpenClick = () => {
    setVisible(!visible)
  }

  return (
    <div>
      <Button icon={<MergeCellsOutlined style={{ fontSize: 18 }} />} onClick={onOpenClick}>
        打开/关闭任务栏
      </Button>
      <br />
      <TaskDesk
        columns={columns}
        data={data}
        tableScroll={{ x: 500, y: 300 }}
        onClick={onClick}
        visible={visible}
        title={'任务台'}
      />
    </div>
  )
}
