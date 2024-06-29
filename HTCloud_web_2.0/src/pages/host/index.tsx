import { useParams } from 'react-router-dom'
import HostTabs from './components/tabs'
import './index.less'
import AddEditDelete from '@components/addEditDelete'
import { operateToDispath } from '@/layouts/components/Menu/util'
import { useAppDispatch, useAppSelector } from '@/redux/store'
import { hostOperations } from '@/layouts/components/deviceManagement/host'
import { useEffect } from 'react'
import { reduxGetHostInfo } from '@/redux/modules/hostInfo'
import { Host } from '@/api/interface'
import { H1Title } from '@/common/commonStyle'

const HostInterface = () => {
  const { id: hostId } = useParams()
  const dispatch = useAppDispatch()
  const { info, nodeInfo } = useAppSelector(state => state.hostInfo)
  const { dataCenterId, clusterId } = info
  const operation = (type: string, key: string) => {
    if (hostId) {
      switch (key) {
        case 'enter':
          operateToDispath.enterMaintain(dispatch, hostId)
          break
        case 'outer':
          operateToDispath.exitMaintain(dispatch, [hostId])
          break
        case 'addVirtualMachine':
          operateToDispath.addVm(dispatch, info)
          break
        case 'editHost':
          operateToDispath.editHost(dispatch, hostId)
          break
        case 'delHost':
          operateToDispath.deleteHost(dispatch, {
            dataCenterId: dataCenterId ? dataCenterId : '',
            clusterId: clusterId ? clusterId : '',
            ids: [hostId],
          })
          break
        case 'startUp':
          operateToDispath.openHost(dispatch, [hostId])
          break
        case 'turnOff':
          operateToDispath.closeHost(dispatch, [hostId])
          break
        case 'reboot':
          operateToDispath.restartHost(dispatch, [hostId])
          break
        default:
          break
      }
    }
  }
  const onClick = (type: string, key: string) => {
    operation(type, key)
  }
  const onMenuClick = (key: string) => {
    operation('', key)
  }

  const getInfo = async () => {
    hostId && dispatch(reduxGetHostInfo(hostId))
  }

  const getOperationsButton = (list: any, hostInfo: Host.IHostType, newArr: any[] = []) => {
    list.map((item: any) => {
      const { disabledFn, menuData, ...thoreData } = item
      const disabled = disabledFn && hostInfo && disabledFn(hostInfo)
      return menuData
        ? newArr.push({ menuData: getOperationsButton(menuData, hostInfo), ...thoreData })
        : newArr.push({
            disabled,
            ...thoreData,
          })
    })
    return newArr
  }

  useEffect(() => {
    getInfo()
  }, [hostId])

  return (
    <div style={{ background: '#fff' }}>
      <H1Title>主机名称: {info.hostName}</H1Title>
      <AddEditDelete
        data={getOperationsButton(hostOperations, info)}
        onClick={onClick}
        onMenuClick={onMenuClick}
      ></AddEditDelete>
      <HostTabs hostId={hostId} info={info} nodeInfo={nodeInfo} />
    </div>
  )
}

export default HostInterface
