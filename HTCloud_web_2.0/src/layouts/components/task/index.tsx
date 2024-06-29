import TaskBoard from '@components/taskBoard'
import { useAppDispatch, useAppSelector } from '@redux/store'
import { closeTaskBoard } from '@/redux/modules/taskBoard'

const columns = [
  {
    title: '任务名称',
    dataIndex: 'operMark',
    key: 'operMark',
  },
  {
    title: '操作类型',
    dataIndex: 'operation',
    key: 'operation',
  },
  {
    title: '操作对象',
    dataIndex: 'operObj',
    key: 'operObj',
  },
  {
    title: '操作人',
    dataIndex: 'username',
    key: 'username',
  },
  {
    title: '操作结果',
    dataIndex: 'result',
    key: 'result',
  },
  {
    title: '详细信息',
    dataIndex: 'errorMsg',
    key: 'errorMsg',
  },
]

export default function Task() {
  const dispath = useAppDispatch()
  const { open, list } = useAppSelector(store => store.taskBoard)
  const onClose = () => {
    dispath(closeTaskBoard())
  }
  return (
    <>
      <TaskBoard columns={columns} data={list} visible={open} onClose={onClose} />
    </>
  )
}
