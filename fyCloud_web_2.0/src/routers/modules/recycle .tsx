import React from 'react'
import lazyLoad from '@/routers/utils/lazyLoad'
import { LayoutIndex } from '@/routers/constant'
import { RouteObject } from '@/routers/interface'

// 回收站
const recycleRouter: Array<RouteObject> = [
  {
    element: <LayoutIndex />,
    children: [
      {
        path: '/recycle',
        element: lazyLoad(React.lazy(() => import('@components/recycleList'))),
        meta: {
          title: '回收站',
          key: 'recycle',
        },
      },
    ],
  },
]

export default recycleRouter
