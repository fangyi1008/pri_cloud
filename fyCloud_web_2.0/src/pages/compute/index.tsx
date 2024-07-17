import './index.less'
import ComputeTabs from './components/tabs'
import AddEditDelete from '@components/addEditDelete'
import { useAppDispatch } from '@redux/store'
import { operateToDispath } from '@/layouts/components/Menu/util'
import { H1Title } from '@/common/commonStyle'

const Compute = () => {
  const operationData = [
    {
      key: 'addDataCenter',
      type: 'add',
      text: '新增数据中心',
    },
    {
      key: 'addCluster',
      type: 'add',
      text: '新增集群',
    },
  ]
  const dispatch = useAppDispatch()
  const operation = (type: string, key: string) => {
    if (key === 'addDataCenter') {
      operateToDispath.addDataCenter(dispatch)
    } else if (key === 'addCluster') {
      operateToDispath.addCluster(dispatch)
    }
  }
  const onClick = (type: string, key: string) => {
    operation(type, key)
  }

  return (
    <div style={{ background: '#fff' }}>
      <H1Title>计算中心</H1Title>
      <AddEditDelete data={operationData} onClick={onClick}></AddEditDelete>
      <ComputeTabs />
    </div>
  )
}

export default Compute
