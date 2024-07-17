import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { GlobalState, ThemeConfigProp } from '@/redux/interface'
import { SizeType } from 'antd/lib/config-provider/SizeContext'
import { User } from '@/api/interface'

const globalState: GlobalState = {
  token: '',
  userInfo: undefined,
  assemblySize: 'middle',
  language: '',
  themeConfig: {
    // 默认 primary 主题颜色
    primary: '#1890ff',
    // 深色模式
    isDark: false,
    // 色弱模式(weak) || 灰色模式(gray)
    weakOrGray: '',
    // 面包屑导航
    breadcrumb: true,
    // 标签页
    tabs: true,
    // 页脚
    footer: true,
  },
}

export const globalSlice = createSlice({
  name: 'global',
  initialState: globalState,
  reducers: {
    setUserInfo: (state, action: PayloadAction<User.UerrInfo | undefined>) => {
      const { payload } = action
      state.userInfo = payload
    },
    setToken: (state, action: PayloadAction<string>) => {
      const { payload } = action
      state.token = payload
    },
    setAssemblySize: (state, action: PayloadAction<SizeType>) => {
      const { payload } = action
      state.assemblySize = payload
    },
    setLanguage: (state, action: PayloadAction<string>) => {
      const { payload } = action
      state.language = payload
    },
    setThemeConfig: (state, action: PayloadAction<ThemeConfigProp>) => {
      const { payload } = action
      state.themeConfig = payload
    },
  },
})

export const { setToken, setAssemblySize, setLanguage, setThemeConfig, setUserInfo } =
  globalSlice.actions

export default globalSlice.reducer
