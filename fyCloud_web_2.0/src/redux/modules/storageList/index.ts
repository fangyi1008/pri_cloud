import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { IStorageList, IPageInfo } from '@redux/interface'

const storageListState: IStorageList = {
  storagePool: {
    selectedRowKeys: [],
    loading: false,
    pageInfo: {
      listData: [],
      pageSize: 10,
      current: 1,
      total: 0,
    },
  },
  storage: {
    selectedRowKeys: [],
    loading: false,
    pageInfo: {
      listData: [],
      pageSize: 10,
      current: 1,
      total: 0,
    },
  },
}

const storageListSlice = createSlice({
  name: 'storageList',
  initialState: storageListState,
  reducers: {
    setStoragePoolPageInfo: (state, action: PayloadAction<IPageInfo>) => {
      state.storagePool = { ...state.storagePool, pageInfo: action.payload }
    },
    setStoragePoolSelectedRowKeys: (state, action: PayloadAction<string[]>) => {
      state.storagePool = { ...state.storagePool, selectedRowKeys: action.payload }
    },
    setStoragePoolLoading: (state, action: PayloadAction<boolean>) => {
      state.storagePool.loading = action.payload
    },

    setStoragePageInfo: (state, action: PayloadAction<IPageInfo>) => {
      state.storage = { ...state.storage, pageInfo: action.payload }
    },
    setStorageSelectedRowKeys: (state, action: PayloadAction<string[]>) => {
      state.storage = { ...state.storage, selectedRowKeys: action.payload }
    },
    setStorageLoading: (state, action: PayloadAction<boolean>) => {
      state.storage.loading = action.payload
    },
  },
})

export const {
  setStoragePoolPageInfo,
  setStoragePoolSelectedRowKeys,
  setStoragePoolLoading,
  setStoragePageInfo,
  setStorageSelectedRowKeys,
  setStorageLoading,
} = storageListSlice.actions
export default storageListSlice.reducer
