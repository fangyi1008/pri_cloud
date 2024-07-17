import { useEffect, useState } from 'react'
import { Tabs, TabsProps } from 'antd'
import VmList from '@components/vmList'
import NetworkCardList from '@components/networkCardList'
import StorageList from '@components/storageList'
import VirtualSwitchList from '@components/virtualSwitchList'

import './index.less'
import HostAbstract from '../abstract'
import { Host } from '@/api/interface'
import { useUpdateEffect } from 'ahooks'

export interface IHostTabsPropsType {
  info: Host.IHostType
  nodeInfo: any
  hostId: string | undefined
}

export default function HostTabs(props: IHostTabsPropsType) {
  const { hostId, nodeInfo, info } = props
  const [activeKey, setActiveKey] = useState<string>('abstract')
  const onTabChange = (key: string) => {
    setActiveKey(key)
  }
  useUpdateEffect(() => {
    setActiveKey('abstract')
  }, [hostId])
  const items: TabsProps['items'] = [
    {
      label: `摘要`,
      key: 'abstract',
      children: <HostAbstract hostId={hostId} info={info} nodeInfo={nodeInfo} />,
    },
    {
      label: `虚拟机`,
      key: 'vm',
      children: <VmList params={{ hostId }} />,
    },
    {
      label: `存储`,
      key: 'storage',
      children: <StorageList params={{ hostId }} />,
    },
    {
      label: `物理网卡`,
      key: 'networkCard',
      children: <NetworkCardList params={{ hostId }} />,
    },
    {
      label: `虚拟交换机`,
      key: 'virtualSwitch',
      children: <VirtualSwitchList params={{ hostId }} />,
    },
  ]

  return (
    <>
      <Tabs
        onChange={onTabChange}
        activeKey={activeKey}
        tabBarGutter={0}
        items={items}
        tabBarStyle={{ margin: 0 }}
        className='custom-tabs'
      />
    </>
  )
}
