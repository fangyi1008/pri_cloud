import React from 'react'
import lazyLoad from '@/routers/utils/lazyLoad'
import { LayoutIndex } from '@/routers/constant'
import { RouteObject } from '@/routers/interface'

// 集群
const clusterRouter: Array<RouteObject> = [
  {
    element: <LayoutIndex />,
    children: [
      {
        path: '/cluster/:id',
        element: lazyLoad(React.lazy(() => import('@pages/cluster/index'))),
        meta: {
          title: '集群',
          key: 'cluster',
        },
      },
    ],
  },
]

export default clusterRouter
