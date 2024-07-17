import { createSlice, PayloadAction, createAsyncThunk } from '@reduxjs/toolkit'
import { MenuState, TabsState } from '@/redux/interface'
import { HOME_URL } from '@/config/config'

const tabsState: TabsState = {
  // tabsActive 其实没啥用，使用 pathname 就可以了😂
  tabsActive: HOME_URL,
  tabsList: [{ title: '首页', path: HOME_URL }],
}

export const tabsSlice = createSlice({
  name: 'tabs',
  initialState: tabsState,
  reducers: {
    setTabsList: (state, action: PayloadAction<Menu.MenuOptions[]>) => {
      const { payload } = action
      state.tabsList = payload
    },
    setTabsActive: (state, action: PayloadAction<string>) => {
      const { payload } = action
      state.tabsActive = payload
    },
  },
})

export const { setTabsList, setTabsActive } = tabsSlice.actions

export default tabsSlice.reducer
