import React from 'react'
import lazyLoad from '@/routers/utils/lazyLoad'
import { LayoutIndex } from '@/routers/constant'
import { RouteObject } from '@/routers/interface'

// 存储
const storageRouter: Array<RouteObject> = [
  {
    element: <LayoutIndex />,
    children: [
      {
        path: '/storage',
        element: lazyLoad(React.lazy(() => import('@components/storageList'))),
        meta: {
          title: '存储',
          key: 'storage',
        },
      },
    ],
  },
]

export default storageRouter
