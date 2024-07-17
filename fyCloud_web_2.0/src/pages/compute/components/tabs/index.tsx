import { useState } from 'react'
import { Tabs, TabsProps } from 'antd'
import DataCenterList from '@components/dataCenterList'
import ClusterList from '@components/clusterList'
import HostList from '@components/hostList'
import VmList from '@components/vmList'

import './index.less'

export default function DataCenterTabs() {
  const onTabChange = (key: string) => {
    setActiveKey(key)
  }
  const items: TabsProps['items'] = [
    // {
    //   label: `摘要`,
    //   key: 'abstract',
    //   children: <div>摘要</div>,
    // },
    {
      label: `数据中心`,
      key: 'dataCenter',
      children: <DataCenterList />,
    },
    {
      label: `集群`,
      key: 'cluster',
      children: <ClusterList />,
    },
    {
      label: `主机信息`,
      key: 'host',
      children: <HostList />,
    },
    {
      label: `虚拟机`,
      key: 'vm',
      children: <VmList />,
    },
  ]
  const [firstTab] = items
  const { key } = firstTab
  const [activeKey, setActiveKey] = useState<string>(`${key}`)

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
