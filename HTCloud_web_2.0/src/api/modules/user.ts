import http from '@/api'
import { User } from '../interface'

export const getUserinfoApi = () => {
  return http.get<User.ResUser>('/htcloud/sys/user/info')
}

export const logoutApi = () => {
  return http.post<User.ResLogout>('htcloud/sys/logout')
}
