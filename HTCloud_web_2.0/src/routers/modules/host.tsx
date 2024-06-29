import React from 'react'
import lazyLoad from '@/routers/utils/lazyLoad'
import { LayoutIndex } from '@/routers/constant'
import { RouteObject } from '@/routers/interface'

// 主机
const hostRouter: Array<RouteObject> = [
  {
    element: <LayoutIndex />,
    children: [
      {
        path: '/host/:id',
        element: lazyLoad(React.lazy(() => import('@pages/host/index'))),
        meta: {
          title: '主机',
          key: 'host',
        },
      },
    ],
  },
]

export default hostRouter
