import { useEffect } from 'react'
import { Outlet } from 'react-router-dom'
import { Layout } from 'antd'
import LayoutMenu from './components/Menu'
import LayoutHeader from './components/Header'
import LayoutTabs from './components/Tabs'
import LayoutFooter from './components/Footer'
import Task from './components/task'
import './index.less'
import { setAuthButtons } from '@/redux/modules/auth/auth'
import { updateCollapse } from '@redux/modules/menu/menu'
import { useAppDispatch, useAppSelector } from '@redux/store'
import {
  VmDevice,
  HostDevice,
  DataCenterDevice,
  ClusterDevice,
  DeleteDataCenter,
  DeleteCluster,
  DeleteHost,
  RestartHost,
  CloseHost,
  OpenHost,
  ExitMaintainHost,
  EnterMaintainHost,
  DeleteVm,
  StartUpVm,
  CloseVm,
  DestroyVm,
  MoveVirtualMachine,
  SuspendVm,
  ResumeVm,
  RestartVm,
  VirtualSwitch,
  DeleteVirtualSwitch,
} from './components/deviceManagement'
import {
  DeletePool,
  EditPoolDevice,
  FormatPool,
  PoolDevice,
  StartPool,
  StopPool,
} from './components/deviceManagement/storagePool'
import {
  DeleteVolume,
  DownLoadVolume,
  VolumeDevice,
} from './components/deviceManagement/storageVolume'
import { DeleteGroup, SecurityGroupDevice } from './components/deviceManagement/securityGroup'
import { DeleteSecurityRule, SecurityRuleDevice } from './components/deviceManagement/securityRule'
import { DeleteRecycleStorage, RecycleDevice } from './components/deviceManagement/recycle'

const LayoutIndex = (props: any) => {
  const { Sider, Content } = Layout
  const { isCollapse } = useAppSelector(state => state.menu)
  const dispatch = useAppDispatch()

  // 监听窗口大小变化
  const listeningWindow = () => {
    window.onresize = () => {
      return (() => {
        const screenWidth = document.body.clientWidth
        if (!isCollapse && screenWidth < 1200) dispatch(updateCollapse(true))
        if (!isCollapse && screenWidth > 1200) dispatch(updateCollapse(false))
      })()
    }
  }

  useEffect(() => {
    listeningWindow()
  }, [])

  return (
    <>
      <section className='container'>
        <Sider trigger={null} collapsed={isCollapse} width={220} theme='dark'>
          <LayoutMenu></LayoutMenu>
        </Sider>
        <Layout>
          <LayoutHeader></LayoutHeader>
          <LayoutTabs></LayoutTabs>
          <Content>
            <Outlet></Outlet>
          </Content>
          <LayoutFooter></LayoutFooter>
        </Layout>
        <Task />
      </section>
      <DeleteCluster />
      <VmDevice />
      <HostDevice />
      <DeleteHost />
      <DataCenterDevice />
      <ClusterDevice />
      <DeleteDataCenter />
      <RestartHost />
      <CloseHost />
      <OpenHost />
      <ExitMaintainHost />
      <EnterMaintainHost />
      <DeleteVm />
      <StartUpVm />
      <CloseVm />
      <DestroyVm />
      <MoveVirtualMachine />
      <SuspendVm />
      <ResumeVm />
      <RestartVm />
      <PoolDevice />
      <DeletePool />
      <EditPoolDevice />
      <StartPool />
      <StopPool />
      <FormatPool />
      <VolumeDevice />
      <DeleteVolume />
      <DownLoadVolume />
      <VirtualSwitch />
      <DeleteVirtualSwitch />
      <SecurityGroupDevice />
      <DeleteGroup />
      <SecurityRuleDevice />
      <DeleteSecurityRule />
      <RecycleDevice />
      <DeleteRecycleStorage />
    </>
  )
}

// const mapStateToProps = (state: any) => state.menu
// const mapDispatchToProps = { setAuthButtons, updateCollapse }
// export default connect(mapStateToProps, mapDispatchToProps)(LayoutIndex)
export default LayoutIndex
