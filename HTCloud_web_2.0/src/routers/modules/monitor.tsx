import React from 'react'
import lazyLoad from '@/routers/utils/lazyLoad'
import { LayoutIndex } from '@/routers/constant'
import { RouteObject } from '@/routers/interface'

// 监控模块
const monitorRouter: Array<RouteObject> = [
  {
    element: <LayoutIndex />,
    children: [
      {
        path: '/monitor/cluster',
        element: lazyLoad(React.lazy(() => import('@pages/monitor/cluster'))),
        meta: {
          title: '集群监控',
          key: 'clusterMonitor',
        },
      },
      {
        path: '/monitor/host',
        element: lazyLoad(React.lazy(() => import('@pages/monitor/host'))),
        meta: {
          title: '主机监控',
          key: 'hostMonitor',
        },
      },
      {
        path: '/monitor/ip',
        element: lazyLoad(React.lazy(() => import('@pages/monitor/ip'))),
        meta: {
          title: 'IP监控',
          key: 'ipMonitor',
        },
      },
      {
        path: '/monitor/storage',
        element: lazyLoad(React.lazy(() => import('@pages/monitor/storage'))),
        meta: {
          title: '存储监控',
          key: 'storageMonitor',
        },
      },
      {
        path: '/monitor/vlan',
        element: lazyLoad(React.lazy(() => import('@pages/monitor/vlan'))),
        meta: {
          title: 'vlan监控',
          key: 'vlanMonitor',
        },
      },
      {
        path: '/monitor/vm',
        element: lazyLoad(React.lazy(() => import('@pages/monitor/vm'))),
        meta: {
          title: '虚拟机监控',
          key: 'vmMonitor',
        },
      },
    ],
  },
]

export default monitorRouter
