import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { IBaseList, IPageInfo } from '@redux/interface'

const hostListState: IBaseList = {
  selectedRowKeys: [],
  loading: false,
  pageInfo: {
    listData: [],
    pageSize: 10,
    current: 1,
    total: 0,
  },
}

const hostListSlice = createSlice({
  name: 'hostList',
  initialState: hostListState,
  reducers: {
    setPageInfo: (state, action: PayloadAction<IPageInfo>) => {
      state.pageInfo = action.payload
    },
    setSelectedRowKeys: (state, action: PayloadAction<string[]>) => {
      state.selectedRowKeys = action.payload
    },
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.loading = action.payload
    },
  },
})

export const { setPageInfo, setSelectedRowKeys, setLoading } = hostListSlice.actions
export default hostListSlice.reducer
