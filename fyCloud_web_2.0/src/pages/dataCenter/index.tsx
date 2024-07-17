import { useParams } from 'react-router-dom'
import AddEditDelete from '@components/addEditDelete'
import DataCenterTabs from './components/tabs'
import { operateToDispath } from '@/layouts/components/Menu/util'
import { useAppDispatch } from '@/redux/store'
import { dataCenterOperations } from '@/layouts/components/deviceManagement'
const DataCenter = () => {
  const { id: dataCenterId } = useParams()
  const dispatch = useAppDispatch()
  const operation = (type: string, key: string) => {
    if (key === 'addHost') {
      operateToDispath.addHost(dispatch, { dataCenterId })
    } else if (key === 'addCluster') {
      operateToDispath.addCluster(dispatch, dataCenterId)
    } else if (key === 'delDataCenter') {
      dataCenterId && operateToDispath.deleteDataCenter(dispatch, [dataCenterId])
    }
  }
  const onClick = (type: string, key: string) => {
    operation(type, key)
  }

  return (
    <div style={{ background: '#fff' }}>
      <AddEditDelete data={dataCenterOperations} onClick={onClick}></AddEditDelete>
      <DataCenterTabs params={{ dataCenterId }} />
    </div>
  )
}

export default DataCenter
