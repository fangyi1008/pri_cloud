import React from 'react'
import lazyLoad from '@/routers/utils/lazyLoad'
import { LayoutIndex } from '@/routers/constant'
import { RouteObject } from '@/routers/interface'

// 数据中心
const dataCenterRouter: Array<RouteObject> = [
  {
    element: <LayoutIndex />,
    children: [
      {
        path: '/dataCenter/:id',
        element: lazyLoad(React.lazy(() => import('@pages/dataCenter/index'))),
        meta: {
          title: '数据中心',
          key: 'dataCenter',
        },
      },
    ],
  },
]

export default dataCenterRouter
