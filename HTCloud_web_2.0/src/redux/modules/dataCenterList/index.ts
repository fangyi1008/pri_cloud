import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { Datacenter } from '@/api/interface'
import { IDataCenterList } from '@/redux/interface'
import { IProTableListRef } from '@/components/proTable/type'

const dataCenterListState: IDataCenterList = {
  refresh: {
    list: [
      {
        key: '',
        dataCenterName: '',
        username: '',
        dataCenterId: '',
        createUserId: 0,
        createTime: '',
      },
    ],
    currPage: 1,
    pageSize: 10,
    totalCount: 0,
    totalPage: 1,
  },
  listRefRefresh: {},
}

const dataCenterOperaSlice = createSlice({
  name: 'dataCenterOperateState',
  initialState: dataCenterListState,
  reducers: {
    setDataCenterList: (state, action: PayloadAction<Datacenter.IListData>) => {
      state.refresh = action.payload
    },
    setlistRefRefresh: (state, action: PayloadAction<IProTableListRef | null>) => {
      state.listRefRefresh = action.payload
    },
  },
})

export const { setDataCenterList, setlistRefRefresh } = dataCenterOperaSlice.actions
export default dataCenterOperaSlice.reducer
