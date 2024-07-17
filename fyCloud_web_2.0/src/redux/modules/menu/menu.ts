import { createSlice, PayloadAction, createAsyncThunk } from '@reduxjs/toolkit'
import { getMenuListApi } from '@/api/modules/datacenter'
import { MenuState } from '@/redux/interface'

const menuState: MenuState = {
  isCollapse: false,
  menuList: [],
  loading: false,
}

export const getMenuData = createAsyncThunk('datecenter/getMenuList', async () => {
  const res = await getMenuListApi()
  return res
})

export const menuSlice = createSlice({
  name: 'menu',
  initialState: menuState,
  reducers: {
    updateCollapse: (state, action: PayloadAction<boolean>) => {
      const { payload } = action
      state.isCollapse = payload
    },
    setMenuList: (state, action: PayloadAction<Menu.MenuOptions[]>) => {
      const { payload } = action
      state.menuList = payload
    },
  },
  extraReducers: builder => {
    builder.addCase(getMenuData.pending, state => {
      state.loading = true
      console.log('🚀 ~ 进行中！', state)
    })
    builder.addCase(getMenuData.fulfilled, (state, { payload }) => {
      console.log('数据', payload)
      state.loading = false
      state.menuList = payload
    })
    builder.addCase(getMenuData.rejected, (state, err) => {
      state.loading = false
      console.log('🚀 ~ rejected', err)
    })
  },
})

export const { updateCollapse, setMenuList } = menuSlice.actions

export default menuSlice.reducer
