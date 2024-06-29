import React from 'react'
import lazyLoad from '@/routers/utils/lazyLoad'
import { Navigate, useRoutes } from 'react-router-dom'
import Login from '@pages/login/index'
import { RouteObject } from 'react-router/dist/lib/context'
import { LayoutIndex } from '@/routers/constant'

// * 导入所有router
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
const metaRouters = require.context('./modules', false, /\.tsx$/)

// * 处理路由
export let routerArray: RouteObject[] = []
metaRouters.keys().forEach((item: string) => {
  routerArray = routerArray.concat(metaRouters(item).default)
})

const exampleRoute: RouteObject[] = []
const isDev = process.env.NODE_ENV === 'development'
if (isDev) {
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore
  const metaRouters1 = require.context('../pages/compoentView', false, /\.tsx$/)
  const fileUrls = metaRouters1.keys()
  const children = []
  for (let i = 0; i < fileUrls.length; i++) {
    const url = fileUrls[i]
    const strFileName = url.match(/([^\/]+)\.\w+$/)[1]
    children.push({
      path: `/${strFileName}`,
      element: lazyLoad(React.lazy(() => import(`@pages/compoentView/${strFileName}`))),
    })
  }
  if (children.length > 0) {
    exampleRoute.push({
      element: <LayoutIndex />,
      children: children,
    })
  }
}

export const rootRouter: RouteObject[] = [
  {
    path: '/',
    element: <Navigate to='/login' />,
  },
  {
    path: '/login',
    element: <Login />,
    // eslint-disable-next-line @typescript-eslint/ban-ts-comment
    // @ts-ignore
    meta: {
      requiresAuth: false,
      title: '登录页',
      key: 'login',
    },
  },
  ...routerArray,
  ...exampleRoute,
  {
    path: '*',
    element: <Navigate to='/404' />,
  },
]

const Router = () => {
  return useRoutes(rootRouter)
}

export default Router
