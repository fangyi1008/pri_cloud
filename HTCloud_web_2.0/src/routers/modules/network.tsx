import { LayoutIndex } from '@/routers/constant'
import { RouteObject } from '@/routers/interface'

//网络
const networkRouter: Array<RouteObject> = [
  {
    element: <LayoutIndex />,
    children: [
      {
        path: '/network',
        element: <div>网络</div>,
        meta: {
          title: '网络',
          key: 'network',
        },
      },
    ],
  },
]

export default networkRouter
