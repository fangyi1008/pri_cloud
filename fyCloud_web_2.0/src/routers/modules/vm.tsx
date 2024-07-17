import React from 'react'
import lazyLoad from '@/routers/utils/lazyLoad'
import { LayoutIndex } from '@/routers/constant'
import { RouteObject } from '@/routers/interface'

// 虚拟机
const vmRouter: Array<RouteObject> = [
  {
    element: <LayoutIndex />,
    children: [
      {
        path: '/vm/:id',
        element: lazyLoad(React.lazy(() => import('@pages/vm/index'))),
        meta: {
          title: '虚拟机',
          key: 'vm',
        },
      },
    ],
  },
]

export default vmRouter
