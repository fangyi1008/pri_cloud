import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { IBaseList, IPageInfo } from '@redux/interface'

const vmListState: IBaseList = {
  selectedRowKeys: [],
  loading: false,
  pageInfo: {
    listData: [],
    pageSize: 10,
    current: 1,
    total: 0,
  },
}

const vmListSlice = createSlice({
  name: 'vmList',
  initialState: vmListState,
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

export const { setPageInfo, setSelectedRowKeys, setLoading } = vmListSlice.actions
export default vmListSlice.reducer
