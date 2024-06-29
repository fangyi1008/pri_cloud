import React from 'react'
import lazyLoad from '@/routers/utils/lazyLoad'
import { LayoutIndex } from '@/routers/constant'
import { RouteObject } from '@/routers/interface'

// 主机
const securityPolicyRouter: Array<RouteObject> = [
  {
    element: <LayoutIndex />,
    children: [
      {
        path: '/securityPolicy',
        element: lazyLoad(React.lazy(() => import('@pages/security/index'))),
        meta: {
          title: '安全策略',
          key: 'securityPolicy',
        },
      },
    ],
  },
]

export default securityPolicyRouter
