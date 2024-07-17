import { useParams } from 'react-router-dom'
import { useEffect } from 'react'
import { Modal } from 'antd'
import ClusterTabs from './components/tabs'
import AddEditDelete from '@components/addEditDelete'
import { operateToDispath } from '@/layouts/components/Menu/util'
import { useAppDispatch, useAppSelector } from '@/redux/store'
import { reduxGetClusterInfo } from '@redux/modules/clusterInfo'
import { clusterOperations } from '@/layouts/components/deviceManagement'

const Cluster = () => {
  const { id: clusterId } = useParams()
  const { clusterInfo, hostInfo } = useAppSelector(state => state.clusterInfo)
  const dispatch = useAppDispatch()
  const operation = (type: string, key: string) => {
    if (key === 'addHost') {
      if (!(clusterInfo?.dataCenterId && clusterInfo?.clusterId)) {
        Modal.error({
          title: '错误提示',
          content: '在集群下增加主机，必须先在数据中心增加集群。',
          okText: '确定',
        })
        return
      }
      operateToDispath.addHost(dispatch, clusterInfo)
    } else if (key === 'addVirtualMachine') {
      if (!(hostInfo?.dataCenterId && hostInfo?.hostId)) {
        Modal.error({
          title: '错误提示',
          content: '在集群下增加虚拟机，必须先增加主机才能增加虚拟机。',
          okText: '确定',
        })
        return
      }
      operateToDispath.addVm(dispatch, hostInfo)
    }
  }
  const onClick = (type: string, key: string) => {
    operation(type, key)
  }

  const getClusterInfo = () => {
    dispatch(reduxGetClusterInfo(clusterId || ''))
  }

  const getOperationsButton = () => {
    const operationsButton = clusterOperations.map(item => {
      const { disabledFn, key, ...thoreData } = item
      let disabled = true
      if (key === 'addHost') {
        disabled = disabledFn && disabledFn(clusterInfo)
      } else {
        disabled = disabledFn && disabledFn(hostInfo)
      }
      return {
        disabled,
        key,
        ...thoreData,
      }
    })
    return operationsButton
  }

  useEffect(() => {
    getClusterInfo()
  }, [clusterId])
  return (
    <div style={{ background: '#fff' }}>
      <AddEditDelete data={getOperationsButton()} onClick={onClick}></AddEditDelete>
      <ClusterTabs params={{ clusterId }} />
    </div>
  )
}

export default Cluster
