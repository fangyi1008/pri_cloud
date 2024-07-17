import React from 'react'
import lazyLoad from '@/routers/utils/lazyLoad'
import { LayoutIndex } from '@/routers/constant'
import { RouteObject } from '@/routers/interface'

// Qos限速策略
const hostRouter: Array<RouteObject> = [
  {
    element: <LayoutIndex />,
    children: [
      {
        path: '/qosSpeedLimitPolicy',
        element: lazyLoad(React.lazy(() => import('@pages/qosSpeedLimitPolicy/index'))),
        meta: {
          title: 'Qos限速策略',
          key: 'qosSpeedLimitPolicy',
        },
      },
    ],
  },
]

export default hostRouter
