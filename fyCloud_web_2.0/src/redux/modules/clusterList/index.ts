import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { IBaseList, IPageInfo } from '@redux/interface'

const clusterListState: IBaseList = {
  selectedRowKeys: [],
  loading: false,
  pageInfo: {
    listData: [],
    pageSize: 10,
    current: 1,
    total: 0,
  },
}

const clusterListSlice = createSlice({
  name: 'clusterList',
  initialState: clusterListState,
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

export const { setPageInfo, setSelectedRowKeys, setLoading } = clusterListSlice.actions
export default clusterListSlice.reducer
