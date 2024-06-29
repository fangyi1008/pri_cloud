import Abstract from '../index'
import { useState } from 'react'
import { Tabs, TabsProps } from 'antd'
import './index.less'

export default function Demo() {
  const [activeKey, setActiveKey] = useState<string>('host')

  const onTabChange = (key: string) => {
    setActiveKey(key)
  }
  const items: TabsProps['items'] = [
    {
      label: `摘要`,
      key: 'abstract',
      children: <div>摘要</div>,
    },
    {
      label: `数据中心`,
      key: 'dataCenter',
      children: <div>数据中心</div>,
    },
    {
      label: `集群`,
      key: 'cluster',
      children: <div>集群</div>,
    },
    {
      label: `主机信息`,
      key: 'host',
      children: <Abstract />,
    },
    {
      label: `虚拟机`,
      key: 'vm',
      children: <div>虚拟机</div>,
    },
    {
      label: `存储`,
      key: 'storage',
      children: <div>存储</div>,
    },
    {
      label: `物理网卡`,
      key: 'networkCard',
      children: <div>物理网卡</div>,
    },
    {
      label: `虚拟交换机`,
      key: 'virtualSwitch',
      children: <div>虚拟交换机</div>,
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
