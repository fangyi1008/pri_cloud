import React from 'react'
import lazyLoad from '@/routers/utils/lazyLoad'
import { LayoutIndex } from '@/routers/constant'
import { RouteObject } from '@/routers/interface'

// 安全策略
const securityGroupsRouter: Array<RouteObject> = [
  {
    element: <LayoutIndex />,
    children: [
      {
        path: '/securityGroups',
        element: lazyLoad(React.lazy(() => import('@pages/security/index'))),
        meta: {
          title: '安全组',
          key: 'securityGroups',
        },
      },
    ],
  },
]

export default securityGroupsRouter
