import React, { useEffect, useState, useMemo } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { Menu, Spin, Button, Modal, MenuProps } from 'antd'
import { findAllBreadcrumb, handleRouter } from '@/utils/util'
import MenuUnfoldOutlined from '@ant-design/icons/MenuUnfoldOutlined'
import * as Icons from '@src/common/antdIcons'
import Logo from './components/Logo'
import './index.less'
import { setMenuList as setMenuListAction, getMenuData } from '@/redux/modules/menu/menu'
import { setBreadcrumbList } from '@/redux/modules/breadcrumb/breadcrumb'
import { setAuthRouter } from '@/redux/modules/auth/auth'
import { openTaskBoard } from '@/redux/modules/taskBoard'
import { useAppDispatch, useAppSelector } from '@redux/store'
import RightMenu from './components/RightMenu'
import { operateToDispath } from './util'

const hostStateToSvgName: Record<string, string> = {
  1: 'running',
  2: 'close',
  3: 'maintenance',
  4: 'unkown',
  5: 'unkown',
}

const vmStateToSvgName: Record<string, string> = {
  挂起: 'pause',
  运行: 'running',
  关机: 'close',
  失联: 'unkown',
  异常: 'unkown',
}

const LayoutMenu = () => {
  const { pathname } = useLocation()
  const isCollapse = useAppSelector(state => state.menu.isCollapse)
  const menuList = useAppSelector(state => state.menu.menuList)
  const loading = useAppSelector(state => state.menu.loading)
  const dispatch = useAppDispatch()
  const [selectedKeys, setSelectedKeys] = useState<string[]>([pathname])
  const [openKeys, setOpenKeys] = useState<string[]>([])

  // 点击当前菜单跳转页面
  const navigate = useNavigate()
  const clickMenu = (nodeInfo: Menu.MenuOptions) => {
    const { path, isLink, type } = nodeInfo
    if (type === 'empty') return
    if (isLink) window.open(isLink, '_blank')
    navigate(path)
  }
  const clickRightMenu = (item: any, operationType: string) => {
    const { dataCenterId } = item
    switch (operationType) {
      case 'addDataCenter':
        operateToDispath[operationType](dispatch)
        break
      case 'deleteDataCenter':
        operateToDispath[operationType](dispatch, [item.dataCenterId])
        break
      case 'editDataCenter':
        operateToDispath[operationType](dispatch, { dataCenterId: item.dataCenterId })
        break
      case 'addCluster':
        operateToDispath[operationType](dispatch, dataCenterId)
        break
      case 'editCluster':
        operateToDispath[operationType](dispatch, item.clusterId)
        break
      case 'deleteCluster':
        operateToDispath[operationType](dispatch, {
          dataCenterId: item.dataCenterId,
          ids: [item.clusterId],
        })
        break
      case 'addHost':
        operateToDispath[operationType](dispatch, { ...item })
        break
      case 'editHost':
        operateToDispath[operationType](dispatch, item.hostId)
        break
      case 'deleteHost':
        operateToDispath[operationType](dispatch, {
          dataCenterId: item.dataCenterId,
          clusterId: item.clusterId,
          ids: [item.hostId],
        })
        break
      case 'enterMaintain':
        operateToDispath[operationType](dispatch, item.hostId)
        break
      case 'exitMaintain':
        operateToDispath[operationType](dispatch, [item.hostId])
        break
      case 'openHost':
        operateToDispath[operationType](dispatch, [item.hostId])
        break
      case 'closeHost':
        operateToDispath[operationType](dispatch, [item.hostId])
        break
      case 'restartHost':
        operateToDispath[operationType](dispatch, [item.hostId])
        break
      case 'addVm':
        if (item.type === 'cluster') {
          if (!item.canUseHostId) {
            Modal.error({
              title: '错误提示',
              content: '在集群下增加虚拟机，必须先增加主机才能增加虚拟机。',
              okText: '确定',
            })
            return
          }
          operateToDispath[operationType](dispatch, { ...item, hostId: item.canUseHostId })
        } else {
          operateToDispath[operationType](dispatch, item)
        }
        break
      case 'editVirtualMachine':
        operateToDispath[operationType](dispatch, item.vmId)
        break
      case 'deleteVm':
        operateToDispath[operationType](dispatch, { hostId: item.hostId, ids: [item.vmId] })
        break
      case 'startUpVm':
        operateToDispath[operationType](dispatch, [item.vmId])
        break
      case 'closeVm':
        operateToDispath[operationType](dispatch, [item.vmId])
        break
      case 'destroyVm':
        operateToDispath[operationType](dispatch, [item.vmId])
        break
      case 'moveVirtualMachine':
        operateToDispath[operationType](dispatch, { hostId: item.hostId, vmId: item.vmId })
        break
      case 'suspendVm':
        operateToDispath[operationType](dispatch, [item.vmId])
        break
      case 'resumeVm':
        operateToDispath[operationType](dispatch, [item.vmId])
        break
      case 'restartVm':
        operateToDispath[operationType](dispatch, [item.vmId])
        break
      default:
        break
    }
  }
  // 定义 menu 类型
  type MenuItem = Required<MenuProps>['items'][number]
  const getItem = (
    menuItem: Menu.MenuOptions,
    icon?: React.ReactNode,
    children?: MenuItem[],
    type?: 'group',
  ): MenuItem => {
    const { path: key } = menuItem
    return {
      key,
      icon,
      children,
      label: (
        <RightMenu
          nodeInfo={menuItem}
          isCollapse={isCollapse}
          clickMenu={clickMenu}
          clickRightMenu={clickRightMenu}
        />
      ),
      type,
    } as MenuItem
  }

  const customIcons: { [key: string]: any } = Icons
  const addIcon = (item: Menu.MenuOptions) => {
    const { icon = '', type = '', state = '' } = item
    if ('cluster' === type) {
      return React.createElement(customIcons.ClusterSvg || null)
    }
    if ('dataCenter' === type) {
      return React.createElement(customIcons.DataCenterSvg || null)
    }
    if ('host' === type) {
      const hostSvg = customIcons._hostSvg
      return React.createElement(hostSvg[hostStateToSvgName[state]] || null)
    }
    if ('vm' === type) {
      const vmSvg = customIcons._vmSvg
      return React.createElement(vmSvg[vmStateToSvgName[state]] || null)
    }
    return React.createElement(customIcons[icon] || null)
  }

  // 处理后台返回菜单 key 值为 antd 菜单需要的 key 值
  const deepLoopFloat = (menuList: Menu.MenuOptions[], newArr: MenuItem[] = []) => {
    if (!menuList) {
      return []
    }
    menuList.forEach((item: Menu.MenuOptions) => {
      if (!item?.children?.length) return newArr.push(getItem(item, addIcon(item)))
      newArr.push(getItem(item, addIcon(item), deepLoopFloat(item?.children)))
    })
    return newArr
  }

  const list = useMemo<MenuItem[]>(() => {
    return deepLoopFloat(menuList)
  }, [menuList])

  // 刷新页面菜单保持高亮
  useEffect(() => {
    setSelectedKeys([pathname])
  }, [pathname])

  // 设置当前展开的 subMenu
  const onOpenChange = (openKeys: string[]) => {
    setOpenKeys(openKeys)
  }

  // 获取菜单列表并处理成 antd menu 需要的格式
  const getMenuData1 = async () => {
    const result = await dispatch(getMenuData())
    const payload = result.payload as Menu.MenuOptions[]
    dispatch(setBreadcrumbList(findAllBreadcrumb(payload)))
    const dynamicRouter = handleRouter(payload)
    dispatch(setAuthRouter(dynamicRouter))
    dispatch(setMenuListAction(payload))
  }

  useEffect(() => {
    getMenuData1()
  }, [])

  const openTask = () => {
    dispatch(openTaskBoard())
  }

  return (
    <div className='menu'>
      <Spin spinning={loading} tip='Loading...'>
        <Logo></Logo>
        <Menu
          theme='dark'
          mode='inline'
          inlineIndent={10}
          inlineCollapsed={isCollapse}
          openKeys={openKeys}
          selectedKeys={selectedKeys}
          items={list}
          onOpenChange={onOpenChange}
        />
        <div className='task-button' style={{ cursor: 'pointer' }}>
          <MenuUnfoldOutlined onClick={openTask} />
        </div>
      </Spin>
    </div>
  )
}
export default LayoutMenu
