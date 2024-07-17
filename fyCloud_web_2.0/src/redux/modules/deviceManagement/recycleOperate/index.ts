import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import { RecycleOperateState, TipsDeviceType } from '@redux/interface'

const recycleOperateState: RecycleOperateState = {
  edit: {
    visible: false,
    title: '恢复',
    content: '',
    values: [],
  },
  delete: {
    visible: false,
    title: '删除',
    content: '',
    values: [],
  },
}

const recycleOperateSlice = createSlice({
  name: 'recycleOperate',
  initialState: recycleOperateState,
  reducers: {
    setResumeStorage: (state, action: PayloadAction<TipsDeviceType>) => {
      state.edit = action.payload
    },
    setDeleteRecycle: (state, action: PayloadAction<TipsDeviceType>) => {
      state.delete = action.payload
    },
  },
})

export const { setResumeStorage, setDeleteRecycle } = recycleOperateSlice.actions
export default recycleOperateSlice.reducer
