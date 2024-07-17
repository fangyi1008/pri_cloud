import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { IBaseList, IPageInfo } from '@redux/interface'

const networkCardListState: IBaseList = {
  selectedRowKeys: [],
  pageInfo: {
    listData: [],
    pageSize: 10,
    current: 1,
    total: 0,
  },
  loading: false,
}

const networkCardListSlice = createSlice({
  name: 'networkCardList',
  initialState: networkCardListState,
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

export const { setPageInfo, setSelectedRowKeys, setLoading } = networkCardListSlice.actions
export default networkCardListSlice.reducer
