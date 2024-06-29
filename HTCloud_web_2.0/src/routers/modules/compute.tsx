import React from 'react'
import lazyLoad from '@/routers/utils/lazyLoad'
import { LayoutIndex } from '@/routers/constant'
import { RouteObject } from '@/routers/interface'

// 计算中心
const computeRouter: Array<RouteObject> = [
  {
    element: <LayoutIndex />,
    children: [
      {
        path: '/compute',
        element: lazyLoad(React.lazy(() => import('@pages/compute/index'))),
        meta: {
          title: '计算中心',
          key: 'compute',
        },
      },
    ],
  },
]

export default computeRouter
