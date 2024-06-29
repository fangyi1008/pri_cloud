import { useEffect } from 'react'
import { useParams } from 'react-router-dom'
import HostTabs from './components/tabs'
import store, { useAppDispatch, useAppSelector } from '@redux/store'
import { vmOperations } from '@/layouts/components/deviceManagement/vm'
import AddEditDelete from '@components/addEditDelete'
import { reduxGetVmInfo } from '@redux/modules/vm'
import { Vm } from '@/api/interface'
import { operateToDispath } from '@/layouts/components/Menu/util'
import { Modal } from 'antd'
import { vncUrl } from '@/api/modules/virtualMachineService'
import eventEmitter from '@/events/index'
import { H1Title } from '@/common/commonStyle'

const VirtualMachine = () => {
  const { id: vmId } = useParams()
  const dispatch = useAppDispatch()
  const vmInfo = useAppSelector(state => state.vm.info)
  const initVmInfo = async (vmId: string) => {
    await dispatch(reduxGetVmInfo(vmId))
  }

  useEffect(() => {
    initVmInfo(vmId || '')
  }, [vmId])

  useEffect(() => {
    const getVmInfo = async () => {
      await dispatch(reduxGetVmInfo(vmId || ''))
    }
    eventEmitter.addListener('refreshVM', getVmInfo)
    return () => {
      eventEmitter.removeAllListeners('refreshVM')
    }
  }, [])

  const getOperationsData = (vmInfo: Vm.IVmType) => {
    return vmOperations.map(item => {
      const { menuData } = item
      if (!menuData) {
        const { disabledFn, ...thoreData } = item
        const disabled = disabledFn && vmInfo && disabledFn(vmInfo)

        return {
          disabled,
          ...thoreData,
        }
      } else {
        const newMenu = menuData.map(item => {
          const { disabledFn, ...thoreData } = item
          const disabled = disabledFn && vmInfo && disabledFn(vmInfo)
          return {
            disabled,
            ...thoreData,
          }
        })
        return {
          ...item,
          menuData: newMenu,
        }
      }
    })
  }

  const consoleVM = async () => {
    const { vmId, state = '关机', vmName } = vmInfo
    if (state !== '运行') {
      Modal.error({
        title: '操作提示',
        content: `${vmName}虚拟机未开启`,
      })
      return
    }
    const url = await vncUrl(vmId)
    if (!url) {
      Modal.error({
        title: '操作提示',
        content: `${vmName}vnc地址为空`,
      })
      return
    }
    const features = `height=700,width=800,directories=no,toolbar=no,menubar=no,resizable=no,status=no`
    const newWin = window.open('about:blank', 'new', features)
    newWin &&
      newWin.document.write(
        `<body scroll="no" style="margin: 0px;padding: 0px;border:0px;overflow:hidden;"><iframe style="margin: 0px;padding: 0px;border: 0px;width:100%;height:100%" src=${url}></iframe></body>`,
      )
  }

  const operationClick = (key: string) => {
    if (!vmId) {
      return
    }
    if (vmId) {
      switch (key) {
        case 'editVirtualMachine':
          operateToDispath.editVirtualMachine(dispatch, vmId)
          break
        case 'closeVm':
          operateToDispath.closeVm(dispatch, [vmId])
          break
        case 'startUpVm':
          operateToDispath.startUpVm(dispatch, [vmId])
          break
        case 'deleteVm':
          operateToDispath.deleteVm(dispatch, { hostId: vmInfo.hostId, ids: [vmId] })
          break
        case 'destroyVm':
          operateToDispath.destroyVm(dispatch, [vmId])
          break
        case 'suspendVm':
          operateToDispath.suspendVm(dispatch, [vmId])
          break
        case 'resumeVm':
          operateToDispath.resumeVm(dispatch, [vmId])
          break
        case 'restartVm':
          operateToDispath.restartVm(dispatch, [vmId])
          break
        case 'moveVirtualMachine':
          operateToDispath.moveVirtualMachine(dispatch, {
            hostId: vmInfo.hostId,
            vmId: vmInfo.vmId,
          })
          break
        case 'consoleVM':
          consoleVM()
          break
        default:
          break
      }
    }
  }

  const onClick = (_: string, key: string) => {
    operationClick(key)
  }
  const onMenuClick = (key: string) => {
    operationClick(key)
  }

  return (
    <div style={{ background: '#fff' }}>
      <H1Title>虚拟机名称: {vmInfo.vmName}</H1Title>
      <AddEditDelete
        data={getOperationsData(vmInfo)}
        onClick={onClick}
        onMenuClick={onMenuClick}
      ></AddEditDelete>
      <HostTabs vmInfo={vmInfo} />
    </div>
  )
}

export default VirtualMachine
