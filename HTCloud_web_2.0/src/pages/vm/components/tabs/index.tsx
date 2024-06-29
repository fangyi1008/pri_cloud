import { useState } from 'react'
import { Tabs, TabsProps } from 'antd'
import Abstract from '../abstract'
import './index.less'
import { Vm } from '@/api/interface'

export interface IVmTabsPropsType {
  vmInfo: Vm.IVmType
}

export default function HostTabs(props: IVmTabsPropsType) {
  const { vmInfo } = props
  const items: TabsProps['items'] = [
    {
      label: `摘要`,
      key: 'abstract',
      children: <Abstract vmInfo={vmInfo} />,
    },
  ]
  return (
    <>
      <Tabs
        activeKey={'abstract'}
        tabBarGutter={0}
        items={items}
        tabBarStyle={{ margin: 0 }}
        className='custom-tabs'
      />
    </>
  )
}
